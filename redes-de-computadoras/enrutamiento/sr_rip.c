/*-----------------------------------------------------------------------------
 * File:  sr_rip.c
 * Date:  Mon Sep 22 23:15:59 GMT-3 2025
 * Authors: Santiago Freire
 * Contact: sfreire@fing.edu.uy
 *
 * Description:
 *
 * Data structures and methods for handling RIP protocol
 *
 *---------------------------------------------------------------------------*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <pthread.h>
#include <sys/time.h>
#include <arpa/inet.h>
#include "sr_router.h"
#include "sr_rt.h"
#include "sr_rip.h"

#include "sr_utils.h"

static pthread_mutex_t rip_metadata_lock = PTHREAD_MUTEX_INITIALIZER;

/* Configuración para pruebas de conteo a infinito */
#define ENABLE_SPLIT_HORIZON 1     /* 1 = activado, 0 = desactivado */
#define ENABLE_TRIGGERED_UPDATES 1 /* 1 = activado, 0 = desactivado */

/* Dirección MAC de multicast para los paquetes RIP */
uint8_t rip_multicast_mac[6] = {0x01, 0x00, 0x5E, 0x00, 0x00, 0x09};

/* Función de validación de paquetes RIP */
int sr_rip_validate_packet(sr_rip_packet_t *packet, unsigned int len)
{
    if (len < sizeof(sr_rip_packet_t))
    {
        return 0;
    }

    if (packet->command != RIP_COMMAND_REQUEST && packet->command != RIP_COMMAND_RESPONSE)
    {
        return 0;
    }

    if (packet->version != RIP_VERSION)
    {
        return 0;
    }

    if (packet->zero != 0)
    {
        return 0;
    }

    unsigned int expected_len = sizeof(struct sr_rip_packet_t) +
                                ((len - sizeof(struct sr_rip_packet_t)) / sizeof(struct sr_rip_entry_t)) *
                                    sizeof(struct sr_rip_entry_t);

    if (len != expected_len)
    {
        return 0;
    }

    return 1;
}

int sr_rip_update_route(struct sr_instance *sr,
                        const struct sr_rip_entry_t *rte,
                        uint32_t src_ip,
                        const char *in_ifname)
{
    /*
     * Procesa una entrada RIP recibida por una interfaz.
     *

     *  - Si la métrica anunciada es >= 16:
     *      - Si ya existe una ruta coincidente aprendida desde el mismo vecino, marca la ruta
     *        como inválida, pone métrica a INFINITY y fija el tiempo de garbage collection.
     *      - Si no, ignora el anuncio de infinito.
     *  - Calcula la nueva métrica sumando el coste del enlace de la interfaz; si resulta >=16,
     *    descarta la actualización.
     *  - Si la ruta no existe, inserta una nueva entrada en la tabla de enrutamiento.
     *  - Si la entrada existe pero está inválida, la revive actualizando métrica, gateway,
     *    learned_from, interfaz y timestamps.
     *  - Si la entrada fue aprendida del mismo vecino:
     *      - Actualiza métrica/gateway/timestamps si cambian; si no, solo refresca el timestamp.
     *  - Si la entrada viene de otro origen:
     *      - Reemplaza la ruta si la nueva métrica es mejor.
     *      - Si la métrica es igual y el next-hop coincide, refresca la entrada.
     *      - En caso contrario (peor métrica o diferente camino), ignora la actualización.
     *  - Actualiza campos relevantes: metric, gw, route_tag, learned_from, interface,
     *    last_updated, valid y garbage_collection_time según corresponda.
     *
     * Valores de retorno:
     *  - -1: entrada inválida o fallo al obtener la interfaz.
     *  -  1: la tabla de rutas fue modificada (inserción/actualización/eliminación).
     *  -  0: no se realizaron cambios.
     *
     */
    if (!sr || !rte || !in_ifname)
        return -1;

    time_t now = time(NULL);

    /* Obtener interfaz */
    struct sr_if *in_if = sr_get_interface(sr, in_ifname);
    if (!in_if)
        return -1;

    /* Ignorar anuncios originados por esta misma interfaz */
    if (src_ip == in_if->ip)
    {
        return 0;
    }

    uint32_t adv_metric = ntohl(rte->metric);
    uint32_t new_metric = adv_metric + (in_if->cost ? in_if->cost : 1);

    if (adv_metric >= INFINITY)
    {
        /* Ruta invalida anunciada */
        pthread_mutex_lock(&rip_metadata_lock);
        for (struct sr_rt *rt = sr->routing_table; rt; rt = rt->next)
        {
            if (rt->dest.s_addr == rte->ip && rt->mask.s_addr == rte->mask)
            {
                if (rt->learned_from == src_ip && rt->valid)
                {
                    rt->valid = 0;
                    rt->metric = INFINITY;
                    rt->garbage_collection_time = now;
                    pthread_mutex_unlock(&rip_metadata_lock);
                    return 1;
                }
                pthread_mutex_unlock(&rip_metadata_lock);
                return 0;
            }
        }
        pthread_mutex_unlock(&rip_metadata_lock);
        return 0;
    }

    struct in_addr dest, mask, gw;
    dest.s_addr = rte->ip;
    mask.s_addr = rte->mask;
    gw.s_addr = src_ip;

    pthread_mutex_lock(&rip_metadata_lock);
    struct sr_rt *found = NULL;
    for (struct sr_rt *rt = sr->routing_table; rt; rt = rt->next)
    {
        if (rt->dest.s_addr == dest.s_addr && rt->mask.s_addr == mask.s_addr)
        {
            found = rt;
            break;
        }
    }

    if (new_metric >= INFINITY)
    {
        if (found && found->learned_from == src_ip && found->valid)
        {
            found->valid = 0;
            found->metric = INFINITY;
            found->garbage_collection_time = now;
            pthread_mutex_unlock(&rip_metadata_lock);
            return 1;
        }
        pthread_mutex_unlock(&rip_metadata_lock);
        return 0;
    }

    if (!found)
    {
        sr_add_rt_entry(sr, dest, gw, mask, in_ifname,
                        new_metric, ntohs(rte->route_tag), src_ip,
                        now, 1, 0);
        pthread_mutex_unlock(&rip_metadata_lock);
        return 1;
    }

    int changed = 0;
    if (!found->valid)
    {
        found->valid = 1;
        found->metric = new_metric;
        found->gw = gw;
        found->learned_from = src_ip;
        found->route_tag = ntohs(rte->route_tag);
        found->last_updated = now;
        found->garbage_collection_time = 0;
        strncpy(found->interface, in_ifname, sr_IFACE_NAMELEN - 1);
        found->interface[sr_IFACE_NAMELEN - 1] = '\0';
        changed = 1;
    }
    else if (found->learned_from == src_ip)
    {
        if (found->metric != new_metric || found->gw.s_addr == 0)
        {
            found->metric = new_metric;
            found->gw = gw; // actualizar siempre el gateway
            changed = 1;
        }
        found->route_tag = ntohs(rte->route_tag);
        found->last_updated = now;
        found->garbage_collection_time = 0;
        strncpy(found->interface, in_ifname, sr_IFACE_NAMELEN - 1);
        found->interface[sr_IFACE_NAMELEN - 1] = '\0';
    }
    else if (new_metric < found->metric)
    {
        found->metric = new_metric;
        found->gw = gw;
        found->learned_from = src_ip;
        found->route_tag = ntohs(rte->route_tag);
        strncpy(found->interface, in_ifname, sr_IFACE_NAMELEN - 1);
        found->interface[sr_IFACE_NAMELEN - 1] = '\0';
        found->last_updated = now;
        found->garbage_collection_time = 0;
        changed = 1;
    }
    else if (new_metric == found->metric && found->gw.s_addr == gw.s_addr)
    {
        found->last_updated = now;
    }

    pthread_mutex_unlock(&rip_metadata_lock);

    if (changed)
    {
        printf("\n*** RIP: Métrica cambió: %s → metric=%u (vía %s en %s) ***\n",
               inet_ntoa(*(struct in_addr *)&rte->ip),
               new_metric,
               inet_ntoa(*(struct in_addr *)&src_ip),
               in_ifname);
        print_routing_table(sr); /*si modifica una metrica, se modifica la tabla y hay que imprimirla*/
    }

    return changed;
}

void sr_handle_rip_packet(struct sr_instance *sr,
                          const uint8_t *packet,
                          unsigned int pkt_len,
                          unsigned int ip_off,
                          unsigned int rip_off,
                          unsigned int rip_len,
                          const char *in_ifname)
{
    sr_rip_packet_t *rip_packet = (struct sr_rip_packet_t *)(packet + rip_off);

    /* Validar paquete RIP */

    /* Si es un RIP_COMMAND_REQUEST, enviar respuesta por la interfaz donde llegó, se sugiere usar función auxiliar sr_rip_send_response */

    /* Si no es un REQUEST, entonces es un RIP_COMMAND_RESPONSE. En caso que no sea un REQUEST o RESPONSE no pasa la validación. */

    /* Procesar entries en el paquete de RESPONSE que llegó, se sugiere usar función auxiliar sr_rip_update_route */

    /* Si hubo un cambio en la tabla, generar triggered update e imprimir tabla */
    if (!sr || !packet || !in_ifname)
        return;

    if (!sr_rip_validate_packet(rip_packet, rip_len))
    {
        printf("RIP: paquete inválido. Ignorando.\n");
        return;
    }

    struct sr_ip_hdr *iphdr = (struct sr_ip_hdr *)(packet + ip_off);
    uint32_t src_ip = iphdr->ip_src;

    if (rip_packet->command == RIP_COMMAND_REQUEST)
    {
        /* No imprimir cada REQUEST */
        struct sr_if *in_if = sr_get_interface(sr, in_ifname);
        if (in_if)
            sr_rip_send_response(sr, in_if, src_ip);
        return;
    }

    if (rip_packet->command == RIP_COMMAND_RESPONSE)
    {
        if (rip_len < sizeof(sr_rip_packet_t))
            return;
        unsigned int n_entries = (rip_len - sizeof(sr_rip_packet_t)) / sizeof(sr_rip_entry_t);
        /* No imprimir cada RESPONSE recibida */

#if ENABLE_TRIGGERED_UPDATES
        int changed = 0;
#endif

        for (unsigned int i = 0; i < n_entries; ++i)
        {
            sr_rip_entry_t *rte = &rip_packet->entries[i];
#if ENABLE_TRIGGERED_UPDATES
            int res = sr_rip_update_route(sr, rte, src_ip, in_ifname);
            if (res == 1)
                changed = 1;
#else
            sr_rip_update_route(sr, rte, src_ip, in_ifname);
#endif
        }

#if ENABLE_TRIGGERED_UPDATES
        if (changed)
        {
            printf("\n*** RIP: CAMBIO EN TABLA DE RUTAS - Triggered update enviado ***\n");
            for (struct sr_if *it = sr->if_list; it; it = it->next)
                sr_rip_send_response(sr, it, htonl(RIP_IP));
            print_routing_table(sr);
        }
#endif
    }
}

void sr_rip_send_response(struct sr_instance *sr, struct sr_if *interface, uint32_t ipDst)
{

    /* Reservar buffer para paquete completo con cabecera Ethernet */

    /* Construir cabecera Ethernet */

    /* Construir cabecera IP */
    /* RIP usa TTL=1 */

    /* Construir cabecera UDP */

    /* Construir paquete RIP con las entradas de la tabla */
    /* Armar encabezado RIP de la respuesta */
    /* Recorrer toda la tabla de enrutamiento  */
    /* Considerar split horizon con poisoned reverse y rutas expiradas por timeout cuando corresponda */
    /* Normalizar métrica a rango RIP (1..INFINITY) */

    /* Armar la entrada RIP:
       - family=2 (IPv4)
       - route_tag desde la ruta
       - ip/mask toman los valores de la tabla
       - next_hop: siempre 0.0.0.0 */

    /* Calcular longitudes del paquete */

    /* Calcular checksums */

    /* Enviar paquete */

    if (!sr || !interface)
        return;

    /* PASO 1: Contar cuantas entradas RIP necesitamos */
    pthread_mutex_lock(&rip_metadata_lock); // Mutex para el uso de tabla enrutamiento

    unsigned int num_entries = 0;
    for (struct sr_rt *rt = sr->routing_table; rt; rt = rt->next)
    {
        /* Incluimos todas las rutas */
        num_entries++;
    }

    if (num_entries == 0)
    {
        pthread_mutex_unlock(&rip_metadata_lock);
        return; /* No hay rutas que anunciar */
    }

    /* PASO 2: Calcular tamaños del paquete */
    unsigned int rip_len = sizeof(sr_rip_packet_t) + (num_entries * sizeof(sr_rip_entry_t));
    unsigned int udp_len = sizeof(sr_udp_hdr_t) + rip_len;
    unsigned int ip_len = sizeof(sr_ip_hdr_t) + udp_len;
    unsigned int total_len = sizeof(sr_ethernet_hdr_t) + ip_len;

    /* PASO 3: Reservar buffer para el paquete completo */
    uint8_t *packet = (uint8_t *)malloc(total_len);
    if (!packet)
    {
        pthread_mutex_unlock(&rip_metadata_lock);
        printf("RIP: malloc falló en send_response\n");
        return;
    }
    memset(packet, 0, total_len);

    /* PASO 4: Obtener punteros a los headers */
    sr_ethernet_hdr_t *eth = (sr_ethernet_hdr_t *)packet;
    sr_ip_hdr_t *ip = (sr_ip_hdr_t *)(packet + sizeof(sr_ethernet_hdr_t));
    sr_udp_hdr_t *udp = (sr_udp_hdr_t *)(packet + sizeof(sr_ethernet_hdr_t) + sizeof(sr_ip_hdr_t));
    sr_rip_packet_t *rip = (sr_rip_packet_t *)(packet + sizeof(sr_ethernet_hdr_t) + sizeof(sr_ip_hdr_t) + sizeof(sr_udp_hdr_t));

    /* PASO 5: Construir cabecera Ethernet */
    memcpy(eth->ether_shost, interface->addr, ETHER_ADDR_LEN); 
    uint8_t rip_mcast_mac[6] = {0x01, 0x00, 0x5E, 0x00, 0x00, 0x09};
    memcpy(eth->ether_dhost, rip_mcast_mac, ETHER_ADDR_LEN); 
    eth->ether_type = htons(ethertype_ip);                  

    /* PASO 6: Construir cabecera IP */
    ip->ip_v = 4;  
    ip->ip_hl = 5; 
    ip->ip_tos = 0;
    ip->ip_len = htons(ip_len);
    ip->ip_id = htons(rand() & 0xFFFF);
    ip->ip_off = 0;
    ip->ip_ttl = 1; /* RIP usa TTL=1 */
    ip->ip_p = 17;  /* Protocolo UDP */
    ip->ip_src = interface->ip;
    ip->ip_dst = ipDst; /* Puede ser RIP_IP (multicast) o IP específica */
    ip->ip_sum = 0;
    ip->ip_sum = cksum(ip, sizeof(sr_ip_hdr_t));

    /* PASO 7: Construir cabecera UDP */
    udp->src_port = htons(RIP_PORT);
    udp->dst_port = htons(RIP_PORT);
    udp->length = htons(udp_len);
    udp->checksum = 0; /* RIP usa checksum 0 en UDP */

    /* PASO 8: Construir encabezado RIP */
    rip->command = RIP_COMMAND_RESPONSE;
    rip->version = RIP_VERSION;
    rip->zero = 0;

    /* PASO 9: Llenar entradas RIP desde la tabla de enrutamiento */
    unsigned int entry_idx = 0;
    for (struct sr_rt *rt = sr->routing_table; rt; rt = rt->next)
    {
        sr_rip_entry_t *entry = &rip->entries[entry_idx];

        /* Campos básicos de la entrada */
        entry->family_identifier = htons(2); /* AF_INET = 2 para IPv4 */
        entry->route_tag = htons(rt->route_tag);
        entry->ip = rt->dest.s_addr;
        entry->mask = rt->mask.s_addr;
        entry->next_hop = htonl(0); /* Siempre 0.0.0.0 según RFC */

        /* Aplicar Split Horizon con Poisoned Reverse */
        uint8_t metric_to_send = rt->metric;

        /* Si la ruta es inválida, anunciar con métrica infinita */
        if (!rt->valid)
        {
            metric_to_send = INFINITY;
        }
#if ENABLE_SPLIT_HORIZON
        /* Split Horizon con Poisoned Reverse:
           Si la ruta fue aprendida desde esta interfaz (learned_from != 0),
           y la interfaz de salida coincide con la interfaz por donde enviamos,
           anunciar con métrica infinita (poisoned reverse) */
        else if (rt->learned_from != 0 &&
                 strcmp(rt->interface, interface->name) == 0)
        {
            metric_to_send = INFINITY;
        }
#endif

        /* Normalizar mtrica al rango RIP (1..INFINITY) */
        if (metric_to_send == 0)
        {
            metric_to_send = 1; /* Min es 1 */
        }
        else if (metric_to_send > INFINITY)
        {
            metric_to_send = INFINITY; /* Max es 16 */
        }

        entry->metric = htonl(metric_to_send);

        entry_idx++;
    }

    pthread_mutex_unlock(&rip_metadata_lock);

    /* PASO 10: Enviar el paquete */
    sr_send_packet(sr, packet, total_len, interface->name);
    /* No imprimir cada respuesta enviada */

    /* PASO 11: Liberar memoria */
    free(packet);
}

void *sr_rip_send_requests(void *arg)
{
    sleep(3); // Esperar a que se inicialice todo
    struct sr_instance *sr = arg;
    /*struct sr_if* interface = sr->if_list; la calculo en el for */

    // Se envia un Request RIP por cada interfaz:
    /* Reservar buffer para paquete completo con cabecera Ethernet */

    /* Construir cabecera Ethernet */

    /* Construir cabecera IP */
    /* RIP usa TTL=1 */

    /* Construir cabecera UDP */

    /* Construir paquete RIP */

    /* Entrada para solicitar la tabla de ruteo completa */

    /* Calcular longitudes del paquete */

    /* Calcular checksums */

    /* Enviar paquete */
    for (struct sr_if *iface = sr->if_list; iface; iface = iface->next)
    {
        /* Tamaños */
        unsigned int rip_len = sizeof(sr_rip_packet_t) + sizeof(sr_rip_entry_t);
        unsigned int udp_len = sizeof(sr_udp_hdr_t) + rip_len;
        unsigned int ip_len = sizeof(sr_ip_hdr_t) + udp_len;
        unsigned int total_len = sizeof(sr_ethernet_hdr_t) + ip_len;

        uint8_t *packet = (uint8_t *)malloc(total_len);
        if (!packet)
        {
            printf("RIP: malloc falló\n");
            continue;
        }
        memset(packet, 0, total_len);

        /* Punteros a headers */
        sr_ethernet_hdr_t *eth = (sr_ethernet_hdr_t *)packet;
        sr_ip_hdr_t *ip = (sr_ip_hdr_t *)(packet + sizeof(sr_ethernet_hdr_t));
        sr_udp_hdr_t *udp = (sr_udp_hdr_t *)(packet + sizeof(sr_ethernet_hdr_t) + sizeof(sr_ip_hdr_t));
        sr_rip_packet_t *rip = (sr_rip_packet_t *)(packet + sizeof(sr_ethernet_hdr_t) + sizeof(sr_ip_hdr_t) + sizeof(sr_udp_hdr_t));

        /* Ethernet: src MAC de la interfaz, dst MAC multicast RIPv2 */
        memcpy(eth->ether_shost, iface->addr, ETHER_ADDR_LEN);
        uint8_t rip_mcast_mac[6] = {0x01, 0x00, 0x5E, 0x00, 0x00, 0x09};
        memcpy(eth->ether_dhost, rip_mcast_mac, ETHER_ADDR_LEN);
        eth->ether_type = htons(ethertype_ip);

        /* IP: TTL=1, proto UDP, src = IP de la interfaz, dst = 224.0.0.9 */
        ip->ip_v = 4;
        ip->ip_hl = 5;
        ip->ip_tos = 0;
        ip->ip_len = htons(ip_len);
        ip->ip_id = htons(rand() & 0xFFFF);
        ip->ip_off = 0;
        ip->ip_ttl = 1; /* RIP → TTL=1 */
        ip->ip_p = 17;  /* UDP */
        ip->ip_src = iface->ip;
        ip->ip_dst = htonl(RIP_IP); /* 224.0.0.9 */
        ip->ip_sum = 0;
        ip->ip_sum = cksum(ip, sizeof(sr_ip_hdr_t)); 

        /* UDP: 520 → 520; checksum 0 en RIP */
        udp->src_port = htons(RIP_PORT);
        udp->dst_port = htons(RIP_PORT);
        udp->length = htons(udp_len);
        udp->checksum = 0;

        /* RIP: Request para tabla completa */
        rip->command = RIP_COMMAND_REQUEST;
        rip->version = RIP_VERSION;
        rip->zero = 0;

        sr_rip_entry_t *e = &rip->entries[0];
        e->family_identifier = htons(0); /* request full table */
        e->route_tag = 0;
        e->ip = htonl(0);
        e->mask = htonl(0);
        e->next_hop = htonl(0);
        e->metric = htonl(INFINITY); /* 16 */

        /* Enviar */
        sr_send_packet(sr, packet, total_len, iface->name);
        /* No imprimir cada REQUEST */

        free(packet);
    }
    return NULL;
}

/* Periodic advertisement thread */
void *sr_rip_periodic_advertisement(void *arg)
{
    struct sr_instance *sr = arg;

    sleep(2); // Esperar a que se inicialice todo

    // Agregar las rutas directamente conectadas
    /************************************************************************************/
    pthread_mutex_lock(&rip_metadata_lock);
    struct sr_if *int_temp = sr->if_list;
    while (int_temp != NULL)
    {
        struct in_addr ip;
        ip.s_addr = int_temp->ip;
        struct in_addr gw;
        gw.s_addr = 0x00000000;
        struct in_addr mask;
        mask.s_addr = int_temp->mask;
        struct in_addr network;
        network.s_addr = ip.s_addr & mask.s_addr;
        uint8_t metric = int_temp->cost ? int_temp->cost : 1;

        struct sr_rt *prev = NULL;
        struct sr_rt *iter = sr->routing_table;
        while (iter)
        {
            struct sr_rt *next = iter->next;
            if (iter->dest.s_addr == network.s_addr && iter->mask.s_addr == mask.s_addr)
            {
                sr_del_rt_entry(&sr->routing_table, iter);
                if (prev)
                    prev->next = next;
            }
            else
            {
                prev = iter;
            }
            iter = next;
        }
        Debug("-> RIP: Adding the directly connected network [%s, ", inet_ntoa(network));
        Debug("%s] to the routing table\n", inet_ntoa(mask));
        sr_add_rt_entry(sr,
                        network,
                        gw,
                        mask,
                        int_temp->name,
                        metric,
                        0,
                        htonl(0),
                        time(NULL),
                        1,
                        0);
        int_temp = int_temp->next;
    }

    pthread_mutex_unlock(&rip_metadata_lock);
    Debug("\n-> RIP: Printing the forwarding table\n");
    print_routing_table(sr);
    /************************************************************************************/

    /*
        Espera inicial de RIP_ADVERT_INTERVAL_SEC antes del primer envío.
        A continuación entra en un bucle infinito que, cada RIP_ADVERT_INTERVAL_SEC segundos,
        recorre la lista de interfaces (sr->if_list) y envía una respuesta RIP por cada una,
        utilizando la dirección de multicast definida (RIP_IP).
        Esto implementa el envío periódico de rutas (anuncios no solicitados) en RIPv2.
    */

    /* Bucle infinito: envío periódico de anuncios RIP */
    while (1)
    {
        sleep(RIP_ADVERT_INTERVAL_SEC); /* Esperar intervalo de anuncio (10 seg) */

        /* Silencioso: no imprimir cada anuncio periódico */

        /* Enviar respuesta RIP por cada interfaz hacia la dirección multicast */
        for (struct sr_if *iface = sr->if_list; iface; iface = iface->next)
        {
            sr_rip_send_response(sr, iface, htonl(RIP_IP));
        }
    }

    return NULL;
}

/* Chequea las rutas y marca las que expiran por timeout */
void *sr_rip_timeout_manager(void *arg)
{
    struct sr_instance *sr = arg;

    /*  - Bucle periódico que espera 1 segundo entre comprobaciones.
        - Recorre la tabla de enrutamiento y para cada ruta dinámica (aprendida de un vecino) que no se haya actualizado
        en el intervalo de timeout (RIP_TIMEOUT_SEC), marca la ruta como inválida, fija su métrica a
        INFINITY y anota el tiempo de inicio del proceso de garbage collection.
        - Si se detectan cambios, se desencadena una actualización (triggered update)
        hacia los vecinos y se actualiza/visualiza la tabla de enrutamiento.
        - Imprimir la tabla si hay cambios
        - Se debe usar el mutex rip_metadata_lock para proteger el acceso concurrente
          a la tabla de enrutamiento.
    */
    while (1)
    {
        sleep(1);
        time_t now = time(NULL);
#if ENABLE_TRIGGERED_UPDATES
        int changed = 0;
#endif

        pthread_mutex_lock(&rip_metadata_lock);
        for (struct sr_rt *rt = sr->routing_table; rt; rt = rt->next)
        {
            if (rt->learned_from && rt->valid &&
                difftime(now, rt->last_updated) > RIP_TIMEOUT_SEC)
            {
                rt->valid = 0;
                rt->metric = INFINITY;
                rt->garbage_collection_time = now;
#if ENABLE_TRIGGERED_UPDATES
                changed = 1;
#endif
            }
        }
        pthread_mutex_unlock(&rip_metadata_lock);

#if ENABLE_TRIGGERED_UPDATES
        if (changed)
        {
            printf("\n*** RIP: TIMEOUT - Ruta(s) expirada(s) ***\n");
            for (struct sr_if *it = sr->if_list; it; it = it->next)
                sr_rip_send_response(sr, it, htonl(RIP_IP));
            print_routing_table(sr);
        }
#endif
    }

    return NULL;
}

/* Chequea las rutas marcadas como garbage collection y las elimina si expira el timer */
void *sr_rip_garbage_collection_manager(void *arg)
{
    struct sr_instance *sr = arg;
    /*
        - Bucle infinito que espera 1 segundo entre comprobaciones.
        - Recorre la tabla de enrutamiento y elimina aquellas rutas que:
            * estén marcadas como inválidas (valid == 0) y
            * lleven más tiempo en garbage collection que RIP_GARBAGE_COLLECTION_SEC
              (current_time >= garbage_collection_time + RIP_GARBAGE_COLLECTION_SEC).
        - Si se detectan eliminaciones, se imprime la tabla.
        - Se debe usar el mutex rip_metadata_lock para proteger el acceso concurrente
          a la tabla de enrutamiento.
    */
    while (1)
    {
        sleep(1);
        time_t now = time(NULL);
        int deleted = 0;

        pthread_mutex_lock(&rip_metadata_lock);
        struct sr_rt *rt = sr->routing_table;
        while (rt)
        {
            struct sr_rt *next = rt->next;
            if (!rt->valid && rt->garbage_collection_time &&
                difftime(now, rt->garbage_collection_time) > RIP_GARBAGE_COLLECTION_SEC)
            {
                sr_del_rt_entry(&sr->routing_table, rt);
                deleted = 1;
            }
            rt = next;
        }
        pthread_mutex_unlock(&rip_metadata_lock);

        if (deleted)
            print_routing_table(sr);
    }

    return NULL;
}

/* Inicialización subsistema RIP */
int sr_rip_init(struct sr_instance *sr)
{
    /* Inicializar mutex */
    if (pthread_mutex_init(&sr->rip_subsys.lock, NULL) != 0)
    {
        printf("RIP: Error initializing mutex\n");
        return -1;
    }

    /* Iniciar hilo avisos periódicos */
    if (pthread_create(&sr->rip_subsys.thread, NULL, sr_rip_periodic_advertisement, sr) != 0)
    {
        printf("RIP: Error creating advertisement thread\n");
        pthread_mutex_destroy(&sr->rip_subsys.lock);
        return -1;
    }

    /* Iniciar hilo timeouts */
    pthread_t timeout_thread;
    if (pthread_create(&timeout_thread, NULL, sr_rip_timeout_manager, sr) != 0)
    {
        printf("RIP: Error creating timeout thread\n");
        pthread_cancel(sr->rip_subsys.thread);
        pthread_mutex_destroy(&sr->rip_subsys.lock);
        return -1;
    }

    /* Iniciar hilo garbage collection */
    pthread_t garbage_collection_thread;
    if (pthread_create(&garbage_collection_thread, NULL, sr_rip_garbage_collection_manager, sr) != 0)
    {
        printf("RIP: Error creating garbage collection thread\n");
        pthread_cancel(sr->rip_subsys.thread);
        pthread_mutex_destroy(&sr->rip_subsys.lock);
        return -1;
    }

    /* Iniciar hilo requests */
    pthread_t requests_thread;
    if (pthread_create(&requests_thread, NULL, sr_rip_send_requests, sr) != 0)
    {
        printf("RIP: Error creating requests thread\n");
        pthread_cancel(sr->rip_subsys.thread);
        pthread_mutex_destroy(&sr->rip_subsys.lock);
        return -1;
    }

    return 0;
}
