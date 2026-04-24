/**********************************************************************
 * file:  sr_router.c
 *
 * Descripción:
 *
 * Este archivo contiene todas las funciones que interactúan directamente
 * con la tabla de enrutamiento, así como el método de entrada principal
 * para el enrutamiento.
 *
 **********************************************************************/

#include <stdio.h>
#include <assert.h>
#include <stdlib.h>
#include <string.h>

#include "sr_if.h"
#include "sr_rt.h"
#include "sr_router.h"
#include "sr_protocol.h"
#include "sr_arpcache.h"
#include "sr_utils.h"
#include "sr_rip.h"

/* Debug macro: set to 0 for quiet mode, 1 for verbose logging */
#ifndef SR_DEBUG
#define SR_DEBUG 0
#endif

#if SR_DEBUG
#define DPRINTF(...)              \
  do                              \
  {                               \
    fprintf(stderr, __VA_ARGS__); \
  } while (0)
#else
#define DPRINTF(...) \
  do                 \
  {                  \
  } while (0)
#endif

/*---------------------------------------------------------------------
 * Method: sr_init(void)
 * Scope:  Global
 *
 * Inicializa el subsistema de enrutamiento
 *
 *---------------------------------------------------------------------*/

void sr_init(struct sr_instance *sr)
{
  assert(sr);

  /* Inicializa la caché y el hilo de limpieza de la caché */
  sr_arpcache_init(&(sr->cache));

  /* Inicializa el subsistema RIP */
  sr_rip_init(sr);

  /* Inicializa los atributos del hilo */
  pthread_attr_init(&(sr->attr));
  pthread_attr_setdetachstate(&(sr->attr), PTHREAD_CREATE_JOINABLE);
  pthread_attr_setscope(&(sr->attr), PTHREAD_SCOPE_SYSTEM);
  pthread_attr_setscope(&(sr->attr), PTHREAD_SCOPE_SYSTEM);
  pthread_t thread;

  /* Hilo para gestionar el timeout del caché ARP */
  pthread_create(&thread, &(sr->attr), sr_arpcache_timeout, sr);

} /* -- sr_init -- */

/* Envía un paquete ICMP de error */
void sr_send_icmp_error_packet(uint8_t type,
                               uint8_t code,
                               struct sr_instance *sr,
                               uint32_t ipDst,
                               uint8_t *ipPacket)
{

  DPRINTF("*** -> Sending ICMP error (type=%d, code=%d)\n", type, code);

  /* reservamos memoria para el tamaño total del paquete: Ethernet + IP + ICMP tipo 3 */
  unsigned int len = sizeof(sr_ethernet_hdr_t) + sizeof(sr_ip_hdr_t) + sizeof(sr_icmp_t3_hdr_t);
  uint8_t *packet = (uint8_t *)malloc(len);
  if (!packet)
    return;

  /* Punteros a cada una d klas 3 cabeceras para escribirlas luego */
  sr_ethernet_hdr_t *eHdr = (sr_ethernet_hdr_t *)packet;
  sr_ip_hdr_t *ipHdr = (sr_ip_hdr_t *)(packet + sizeof(sr_ethernet_hdr_t));
  sr_icmp_t3_hdr_t *icmpHdr = (sr_icmp_t3_hdr_t *)(packet + sizeof(sr_ethernet_hdr_t) + sizeof(sr_ip_hdr_t));

  /* --------- Usar LPM existente para buscar ruta --------- */
  struct sr_rt *best = sr_find_longest_prefix_match(sr, ipDst);

  if (!best)
  {
    fprintf(stderr, "sr_send_icmp_error_packet: no route to destination ");
    print_addr_ip_int(ntohl(ipDst));
    fprintf(stderr, "\n");
    free(packet);
    return;
  }

  struct sr_if *iface = sr_get_interface(sr, best->interface);
  if (!iface)
  {
    fprintf(stderr, "sr_send_icmp_error_packet: no output interface found\n");
    free(packet);
    return;
  }

  /* ---------------------- Construir el header ICMP (tipo 3 x eso sr_icmp_t3_hdr_t) ---------------------- */
  icmpHdr->icmp_type = type;
  icmpHdr->icmp_code = code;
  icmpHdr->unused = 0;
  icmpHdr->next_mtu = 0;

  /* copiarle los primeros 28 bytes del paq original (el header IP + 8 bytes de datos) */
  memset(icmpHdr->data, 0, ICMP_DATA_SIZE);
  memcpy(icmpHdr->data, ipPacket, ICMP_DATA_SIZE);

  icmpHdr->icmp_sum = 0;
  icmpHdr->icmp_sum = cksum(icmpHdr, sizeof(sr_icmp_t3_hdr_t)); /* hacerle el checksum */

  /* ----------------------- Construir el header IP ----------------------- */
  ipHdr->ip_v = 4;
  ipHdr->ip_hl = sizeof(sr_ip_hdr_t) / 4;
  ipHdr->ip_tos = 0;
  ipHdr->ip_len = htons(sizeof(sr_ip_hdr_t) + sizeof(sr_icmp_t3_hdr_t));
  ipHdr->ip_id = htons(0); /* pasa de little a big */
  ipHdr->ip_off = htons(IP_DF);
  ipHdr->ip_ttl = 64;
  ipHdr->ip_p = ip_protocol_icmp; /* indicamos q es un paquete ICMP */
  ipHdr->ip_src = iface->ip;
  ipHdr->ip_dst = ipDst;
  ipHdr->ip_sum = 0;
  ipHdr->ip_sum = cksum(ipHdr, sizeof(sr_ip_hdr_t));

  /* -------- Construir el header Ethernet (ejemplo de la letra de la tarea) ------ */
  memcpy(eHdr->ether_shost, iface->addr, ETHER_ADDR_LEN);
  eHdr->ether_type = htons(ethertype_ip);

  /* Determinar next hop igual que en forwarding */
  uint32_t nextHop = (best->gw.s_addr == 0) ? ipDst : best->gw.s_addr;

  /* Intentar resolver MAC del next hop con ARP cache */
  struct sr_arpentry *arpEntry = sr_arpcache_lookup(&(sr->cache), nextHop); /* chequea si esta en la cache */
  if (arpEntry)
  { /* caso esta en la cache */
    memcpy(eHdr->ether_dhost, arpEntry->mac, ETHER_ADDR_LEN);
    sr_send_packet(sr, packet, len, iface->name);
    free(arpEntry);
    free(packet); /* free de la memoria que hicimos malloc al principio */
  }
  else
  { /* si NO está en la cache */
    /* Encolar la solicitud ARP (sr_arpcache_queuereq copia el paquete asi q podemos hacer free) */
    DPRINTF("*** DEBUG: ICMP error packet needs ARP for nextHop ");
#if SR_DEBUG
    print_addr_ip_int(ntohl(nextHop));
    DPRINTF(" using iface %s\n", iface->name);
#endif
    sr_arpcache_queuereq(&(sr->cache), nextHop, packet, len, iface->name);
    free(packet);
  }
  DPRINTF("*** -> ICMP error packet prepared and sent/queued.\n");

} /* -- sr_send_icmp_error_packet -- */

/* Envia un paquete ECHO reply */
void sr_send_icmp_echo_reply_packet(
    struct sr_instance *sr,
    uint32_t ipDst,
    uint8_t *ipPacket,
    unsigned int len)
{
  /* Crear un nuevo paquete ICMP */
  uint8_t *icmp_packet = (uint8_t *)malloc(len);
  if (!icmp_packet)
    return;

  /*Header Ethernet*/
  sr_ethernet_hdr_t *newEthHdr = (sr_ethernet_hdr_t *)icmp_packet;
  memset(newEthHdr->ether_dhost, 0, ETHER_ADDR_LEN);
  memset(newEthHdr->ether_shost, 0, ETHER_ADDR_LEN);
  newEthHdr->ether_type = htons(ethertype_ip);

  /* IP header original */
  sr_ip_hdr_t *orig_ip_hdr = (sr_ip_hdr_t *)(ipPacket + sizeof(sr_ethernet_hdr_t));

  /* Crear y rellenar el encabezado IP */
  sr_ip_hdr_t *ip_hdr = (sr_ip_hdr_t *)(icmp_packet + sizeof(sr_ethernet_hdr_t));
  memcpy(ip_hdr, orig_ip_hdr, sizeof(sr_ip_hdr_t));
  ip_hdr->ip_ttl = INIT_TTL;            
  ip_hdr->ip_src = orig_ip_hdr->ip_dst; 
  ip_hdr->ip_dst = ipDst;               
  ip_hdr->ip_sum = 0;
  ip_hdr->ip_sum = cksum(ip_hdr, sizeof(sr_ip_hdr_t));

  /* Crear y rellenar el encabezado ICMP */
  sr_icmp_hdr_t *icmp_hdr = (sr_icmp_hdr_t *)(icmp_packet + sizeof(sr_ethernet_hdr_t) + sizeof(sr_ip_hdr_t));
  icmp_hdr->icmp_type = 0;
  icmp_hdr->icmp_code = 0;
  icmp_hdr->icmp_sum = 0;
  size_t icmp_data = len - sizeof(sr_ethernet_hdr_t) - sizeof(sr_ip_hdr_t) - sizeof(sr_icmp_hdr_t);
  memcpy((uint8_t *)icmp_hdr + sizeof(sr_icmp_hdr_t), ipPacket + sizeof(sr_ethernet_hdr_t) + sizeof(sr_ip_hdr_t) + sizeof(sr_icmp_hdr_t), icmp_data);
  icmp_hdr->icmp_sum = cksum(icmp_hdr, sizeof(sr_icmp_hdr_t) + icmp_data);

  struct sr_rt *ruta = sr_find_longest_prefix_match(sr, ipDst);
  if (!ruta)
  {
    DPRINTF("*** DEBUG: sr_send_icmp_echo_reply_packet: no route for dst ");
#if SR_DEBUG
    print_addr_ip_int(ntohl(ipDst));
#endif
    free(icmp_packet);
    return;
  }

  struct sr_if *iface = sr_get_interface(sr, ruta->interface);
  if (!iface)
  {
    fprintf(stderr, "*** DEBUG: sr_send_icmp_echo_reply_packet: no iface for route to ");
    print_addr_ip_int(ntohl(ipDst));
    free(icmp_packet);
    return;
  }

  /* Verificar NextHop, cuidado con el 0.0.0.0 */
  uint32_t NextHop = (ruta->gw.s_addr == 0) ? ip_hdr->ip_dst : ruta->gw.s_addr;

  /* Verificar si la dirección MAC del siguiente salto está en la caché ARP */
  struct sr_arpentry *entry = sr_arpcache_lookup(&(sr->cache), NextHop);
  if (entry)
  {
    /* Si la dirección está en caché, reenviar el paquete */
    DPRINTF("** Entrada ARP encontrada. Reenviando el paquete.\n");
    memcpy(newEthHdr->ether_dhost, entry->mac, ETHER_ADDR_LEN);
    memcpy(newEthHdr->ether_shost, iface->addr, ETHER_ADDR_LEN);
#if SR_DEBUG
    print_hdr_eth(icmp_packet);
#endif
    sr_send_packet(sr, icmp_packet, len, iface->name);
    free(entry); /* Liberar la entrada ARP */
  }
  else
  {
    /* Si la dirección no está en caché, enviar una solicitud ARP */
    DPRINTF("** No se encontró entrada ARP. Agregando el paquete a la cola de solicitudes ARP.\n");
    struct sr_arpreq *arp_req = sr_arpcache_queuereq(&(sr->cache), NextHop, icmp_packet, len, iface->name);
    handle_arpreq(sr, arp_req);
  }

  /* Liberar memoria */
  free(icmp_packet);
} /* -- sr_send_icmp_echo_reply_packet -- */

void sr_handle_ip_packet(struct sr_instance *sr,
                         uint8_t *packet /* lent */,
                         unsigned int len,
                         uint8_t *srcAddr,
                         uint8_t *destAddr,
                         char *interface /* lent */,
                         sr_ethernet_hdr_t *eHdr)
{

  /*
   * COLOQUE ASÍ SU CÓDIGO
   * SUGERENCIAS:
   * - Obtener el cabezal IP y direcciones
   * - Verificar si el paquete es para una de mis interfaces o si hay una coincidencia en mi tabla de enrutamiento
   * - Si no es para una de mis interfaces y no hay coincidencia en la tabla de enrutamiento, enviar ICMP net unreachable
   * - Si es para mí, verificar si es un paquete ICMP echo request y responder con un echo reply
   * - Si es para mí o a la IP multicast de RIP, verificar si contiene un datagrama UDP y es destinado al puerto RIP, en ese caso pasarlo al subsistema RIP.
   * - Sino, verificar TTL, ARP y reenviar si corresponde (puede necesitar una solicitud ARP y esperar la respuesta)
   * - No olvide imprimir los mensajes de depuración
   */
  DPRINTF("*** -> It is an IP packet. Print IP header.\n");
  /* Obtener el cabezal IP y direcciones */
  sr_ip_hdr_t *ipHdr = (sr_ip_hdr_t *)(packet + sizeof(sr_ethernet_hdr_t));

  if (len < sizeof(sr_ethernet_hdr_t) + sizeof(sr_ip_hdr_t) || ip_cksum(ipHdr, sizeof(sr_ip_hdr_t)) != ipHdr->ip_sum)
  {
    printf("Paquete IP inválido recibido.\n");
    return;
  }

  /* Verificar si el paquete es para una de mis interfaces o si hay una coincidencia en mi tabla de enrutamiento */
  struct sr_if *target_if = sr_get_interface_given_ip(sr, ipHdr->ip_dst);
  /* Verificar si el paquete es para una de mis interfaces o a la IP multicast de RIP */
  int is_multicast_rip = (ipHdr->ip_dst == htonl(RIP_IP));

  /* Si NO es para mí (ni unicast ni multicast RIP), verificar TTL antes de forward */
  if (!target_if && !is_multicast_rip)
  {
    if (ipHdr->ip_ttl <= 1)
    {
      DPRINTF("** TTL agotado. Enviando ICMP Time Exceeded.\n");
      sr_send_icmp_error_packet(11, 0, sr, ipHdr->ip_src, packet + sizeof(sr_ethernet_hdr_t));
      return;
    }
  }

  if (target_if || is_multicast_rip)
  {

    /* Paquete ICMP → responder con Echo Reply */
    if (ipHdr->ip_p == ip_protocol_icmp)
    {
      sr_icmp_hdr_t *icmpHdr = (sr_icmp_hdr_t *)(packet + sizeof(sr_ethernet_hdr_t) + sizeof(sr_ip_hdr_t));
      if (icmpHdr->icmp_type == 8)
      {
        DPRINTF("ICMP Echo Request recibido. Respondiendo con Echo Reply.\n");
        sr_send_icmp_echo_reply_packet(sr, ipHdr->ip_src, packet, len);
      }
      return;
    }

    /* Paquete UDP -> puede ser RIP */
    else if (ipHdr->ip_p == ip_protocol_udp)
    {
      sr_udp_hdr_t *udpHdr = (sr_udp_hdr_t *)(packet + sizeof(sr_ethernet_hdr_t) + sizeof(sr_ip_hdr_t));
      if (ntohs(udpHdr->dst_port) == RIP_PORT)
      {
        DPRINTF("Paquete UDP-RIP recibido. Llamando a sr_handle_rip_packet().\n");
        unsigned int ip_off = sizeof(sr_ethernet_hdr_t);
        unsigned int udp_off = ip_off + sizeof(sr_ip_hdr_t);
        unsigned int rip_off = udp_off + sizeof(sr_udp_hdr_t);
        unsigned int rip_len = len - rip_off;
        sr_handle_rip_packet(sr, packet, len, ip_off, rip_off, rip_len, interface);
        return;
      }
      else if (!is_multicast_rip)
      {
        /* este es UDP pero no es RIP, misma condicion que antes */
        DPRINTF("Paquete UDP no RIP. Respondiendo con ICMP Port Unreachable.\n");
        sr_send_icmp_error_packet(3, 3, sr, ipHdr->ip_src, packet + sizeof(sr_ethernet_hdr_t));
        return;
      }
      else
      {
        DPRINTF("Paquete UDP multicast no RIP. Ignorando.\n");
        return;
      }
    }

    /* TCP u otro -> ICMP Port Unreachable */
    else if (!is_multicast_rip)
    {
      DPRINTF("Paquete TCP/UDP recibido. Respondiendo con ICMP Port Unreachable.\n");
      sr_send_icmp_error_packet(3, 3, sr, ipHdr->ip_src, packet + sizeof(sr_ethernet_hdr_t));
      return;
    }
    else
    {
      DPRINTF("Paquete multicast TCP/UDP. Ignorando.\n");
      return;
    }
  }

  /* Si no es para mí, buscar lpm */
  DPRINTF("*** DEBUG: Looking up route for dst ");
#if SR_DEBUG
  print_addr_ip_int(ntohl(ipHdr->ip_dst));
#endif
  struct sr_rt *rt = sr_find_longest_prefix_match(sr, ipHdr->ip_dst);

  /* Si no se encontró ruta, verificar si el router recien inicio (RIP aún propagando) */
  if (!rt)
  {
    static time_t router_start_time = 0;
    if (router_start_time == 0)
      router_start_time = time(NULL);

    time_t now = time(NULL);
    double seconds_since_start = difftime(now, router_start_time);

    /* Evita ICMP prematuros durante los primeros 15 segundos mientras RIP intercambia tablas */
    if (seconds_since_start < 15)
    {
      DPRINTF("Aún inicializando (RIP no completo). Descartando paquete sin enviar ICMP.\n");
      return;
    }

    /* Después del período de arranque, si sigue sin ruta, enviar ICMP Net Unreachable */
    DPRINTF("No se encontró ruta en la tabla de enrutamiento. Enviando ICMP Net Unreachable.\n");
    sr_send_icmp_error_packet(3, 0, sr, ipHdr->ip_src, packet + sizeof(sr_ethernet_hdr_t));
    return;
  }

  ipHdr->ip_ttl--;
  ipHdr->ip_sum = 0;
  ipHdr->ip_sum = cksum(ipHdr, sizeof(sr_ip_hdr_t));

  /* Determinar next hop */
  uint32_t nextHop = (rt->gw.s_addr == 0) ? ipHdr->ip_dst : rt->gw.s_addr;

  /* Obtener interfaz de salida */
  struct sr_if *out_if = sr_get_interface(sr, rt->interface);
  if (!out_if)
  {
    printf("*** -> Invalid outgoing interface. Dropping packet.\n");
    return;
  }

  /* Debug */
  DPRINTF("*** DEBUG: Selected route: dest=");
#if SR_DEBUG
  print_addr_ip_int(ntohl(rt->dest.s_addr));
  DPRINTF(" mask=");
  print_addr_ip_int(ntohl(rt->mask.s_addr));
  DPRINTF(" gw=");
  print_addr_ip_int(ntohl(rt->gw.s_addr));
  DPRINTF(" iface=%s\n", rt->interface);

  DPRINTF("*** DEBUG: nextHop (network order) = ");
  print_addr_ip_int(ntohl(nextHop));
  DPRINTF("*** DEBUG: out_if=%s ip=", out_if->name);
  print_addr_ip_int(ntohl(out_if->ip));
#endif

  if (nextHop == out_if->ip)
  {
    DPRINTF("WARNING: next hop coincide con mi propia IP en %s, usando destino original.\n", out_if->name);
    nextHop = ipHdr->ip_dst;
  }

  /* Buscar MAC del next hop en caché ARP */
  struct sr_arpentry *arpEntry = sr_arpcache_lookup(&(sr->cache), nextHop);
  if (arpEntry)
  {
    DPRINTF("Entrada ARP encontrada. Reenviando el paquete.\n");
    memcpy(eHdr->ether_dhost, arpEntry->mac, ETHER_ADDR_LEN);
    memcpy(eHdr->ether_shost, out_if->addr, ETHER_ADDR_LEN);
    sr_send_packet(sr, packet, len, out_if->name);
    free(arpEntry);
  }
  else
  {
    DPRINTF("No se encontró entrada ARP. Agregando el paquete a la cola de solicitudes ARP.\n");
    DPRINTF("*** DEBUG: Enqueuing packet for ARP resolution: nextHop=");
#if SR_DEBUG
    print_addr_ip_int(ntohl(nextHop));
    DPRINTF(" via iface %s\n", out_if->name);
#endif
    struct sr_arpreq *arpReq = sr_arpcache_queuereq(&(sr->cache), nextHop, packet, len, out_if->name);
    handle_arpreq(sr, arpReq);
  }
}

/* FUNCION AUXILIAR */
struct sr_rt *sr_find_longest_prefix_match(struct sr_instance *sr, uint32_t ip_dst)
{
  struct sr_rt *best_match = NULL;
  struct sr_rt *rt_walker = sr->routing_table;
  uint32_t longest_mask = 0;

  while (rt_walker != NULL)
  {
    /* Aplicar la máscara de la tabla de enrutamiento a la IP de destino */
    if ((rt_walker->dest.s_addr & rt_walker->mask.s_addr) == (ip_dst & rt_walker->mask.s_addr))
    {
      /* Buscar la coincidencia con la máscara más larga */
      if (ntohl(rt_walker->mask.s_addr) > ntohl(longest_mask))
      {
        longest_mask = rt_walker->mask.s_addr;
        best_match = rt_walker;
      }
    }
    rt_walker = rt_walker->next;
  }

  return best_match;
}

/* Gestiona la llegada de un paquete ARP */
void sr_handle_arp_packet(struct sr_instance *sr,
                          uint8_t *packet /* lent */,
                          unsigned int len,
                          uint8_t *srcAddr,
                          uint8_t *destAddr,
                          char *interface /* lent */,
                          sr_ethernet_hdr_t *eHdr)
{

  /* Imprimo el cabezal ARP */
  DPRINTF("*** -> It is an ARP packet. Print ARP header.\n");
#if SR_DEBUG
  print_hdr_arp(packet + sizeof(sr_ethernet_hdr_t));
#endif

  /* COLOQUE SU CÓDIGO AQUÍ
  SUGERENCIAS:
  - Verifique si se trata de un ARP request o ARP reply
  - Si es una ARP request, antes de responder verifique si el mensaje consulta por la dirección MAC asociada a una dirección IP configurada en una interfaz del router
  - Si es una ARP reply, agregue el mapeo MAC->IP del emisor a la caché ARP y envíe los paquetes que hayan estado esperando por el ARP reply
  */

  if (len < sizeof(sr_ethernet_hdr_t) + sizeof(sr_arp_hdr_t))
    return;

  sr_arp_hdr_t *arp = (sr_arp_hdr_t *)(packet + sizeof(sr_ethernet_hdr_t));
  uint16_t op = ntohs(arp->ar_op);

  struct sr_if *myInterface = sr_get_interface_given_ip(sr, arp->ar_tip);

  /* Verifico si se trata de un ARP request o ARP reply */
  if (op == arp_op_request)
  {
    DPRINTF("**** -> It is an ARP request.\n");

    /* Si el ARP request es para una de mis interfaces */
    if (myInterface)
    {
      DPRINTF("***** -> ARP request is for one of my interfaces.\n");

      /* Inserto el mapeo del emisor en la caché ARP */
      sr_arpcache_insert(&(sr->cache), arp->ar_sha, arp->ar_sip);

      /* Construyo la respuesta ARP */
      memcpy(eHdr->ether_shost, myInterface->addr, ETHER_ADDR_LEN);
      memcpy(eHdr->ether_dhost, arp->ar_sha, ETHER_ADDR_LEN);

      memcpy(arp->ar_tha, arp->ar_sha, ETHER_ADDR_LEN);
      memcpy(arp->ar_sha, myInterface->addr, ETHER_ADDR_LEN);
      arp->ar_tip = arp->ar_sip;
      arp->ar_sip = myInterface->ip;
      arp->ar_op = htons(arp_op_reply);

#if SR_DEBUG
      print_hdrs(packet, len);
#endif
      sr_send_packet(sr, packet, len, myInterface->name);
    }

    DPRINTF("******* -> ARP request processing complete.\n");
  }
  else if (op == arp_op_reply)
  {
    DPRINTF("**** -> It is an ARP reply.\n");

    /* Agrego el mapeo MAC->IP del emisor a la caché ARP */
    struct sr_arpreq *req = sr_arpcache_insert(&(sr->cache), arp->ar_sha, arp->ar_sip);

    if (req)
    {
      struct sr_if *iface = sr_get_interface(sr, interface);
      if (iface)
      {
        sr_arp_reply_send_pending_packets(sr, req, arp->ar_sha, iface->addr, iface);
      }
      sr_arpreq_destroy(&(sr->cache), req);
    }

    DPRINTF("******* -> ARP reply processing complete.\n");
  }
}

/*
 * ***** A partir de aquí no debería tener que modificar nada ****
 */

/* Envía todos los paquetes IP pendientes de una solicitud ARP */
void sr_arp_reply_send_pending_packets(struct sr_instance *sr,
                                       struct sr_arpreq *arpReq,
                                       uint8_t *dhost,
                                       uint8_t *shost,
                                       struct sr_if *iface)
{

  struct sr_packet *currPacket = arpReq->packets;
  sr_ethernet_hdr_t *ethHdr;
  uint8_t *copyPacket;

  while (currPacket != NULL)
  {
    ethHdr = (sr_ethernet_hdr_t *)currPacket->buf;
    memcpy(ethHdr->ether_shost, shost, sizeof(uint8_t) * ETHER_ADDR_LEN);
    memcpy(ethHdr->ether_dhost, dhost, sizeof(uint8_t) * ETHER_ADDR_LEN);

    copyPacket = malloc(sizeof(uint8_t) * currPacket->len);
    memcpy(copyPacket, ethHdr, sizeof(uint8_t) * currPacket->len);

#if SR_DEBUG
    print_hdrs(copyPacket, currPacket->len);
#endif
    sr_send_packet(sr, copyPacket, currPacket->len, iface->name);
    free(copyPacket);
    currPacket = currPacket->next;
  }
}

/*---------------------------------------------------------------------
 * Method: sr_handlepacket(uint8_t* p,char* interface)
 * Scope:  Global
 *
 * This method is called each time the router receives a packet on the
 * interface.  The packet buffer, the packet length and the receiving
 * interface are passed in as parameters. The packet is complete with
 * ethernet headers.
 *
 * Note: Both the packet buffer and the character's memory are handled
 * by sr_vns_comm.c that means do NOT delete either.  Make a copy of the
 * packet instead if you intend to keep it around beyond the scope of
 * the method call.
 *
 *---------------------------------------------------------------------*/

void sr_handlepacket(struct sr_instance *sr,
                     uint8_t *packet /* lent */,
                     unsigned int len,
                     char *interface /* lent */)
{
  assert(sr);
  assert(packet);
  assert(interface);

  DPRINTF("*** -> Received packet of length %d \n", len);

  /* Obtengo direcciones MAC origen y destino */
  sr_ethernet_hdr_t *eHdr = (sr_ethernet_hdr_t *)packet;
  uint8_t *destAddr = malloc(sizeof(uint8_t) * ETHER_ADDR_LEN);
  uint8_t *srcAddr = malloc(sizeof(uint8_t) * ETHER_ADDR_LEN);
  memcpy(destAddr, eHdr->ether_dhost, sizeof(uint8_t) * ETHER_ADDR_LEN);
  memcpy(srcAddr, eHdr->ether_shost, sizeof(uint8_t) * ETHER_ADDR_LEN);
  uint16_t pktType = ntohs(eHdr->ether_type);

  if (is_packet_valid(packet, len))
  {
    if (pktType == ethertype_arp)
    {
      sr_handle_arp_packet(sr, packet, len, srcAddr, destAddr, interface, eHdr);
    }
    else if (pktType == ethertype_ip)
    {
      sr_handle_ip_packet(sr, packet, len, srcAddr, destAddr, interface, eHdr);
    }
  }

  /* Liberar memoria */
  free(destAddr);
  free(srcAddr);

} /* end sr_ForwardPacket */