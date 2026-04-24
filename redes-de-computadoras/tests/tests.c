#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

/* Interfaces de cada router */
const char *v1[] = {"100.0.0.50", "10.0.0.1", "10.0.2.1"};
const char *v2[] = {"10.0.0.2", "200.0.0.50", "10.0.1.1"};
const char *v3[] = {"10.0.2.2", "200.100.0.50", "10.0.1.2"};
const char *v4[] = {"200.0.0.10", "150.150.0.1", "200.200.0.1"};
const char *v5[] = {"200.100.0.15", "100.100.0.1", "200.200.0.2"};

/* Servidores */
const char *srv1 = "150.150.0.2";
const char *srv2 = "100.100.0.2";

/* Ejecutar comandos y mostrar salida */
void run(const char *cmd)
{
    printf(">> %s\n", cmd);
    FILE *fp = popen(cmd, "r");
    if (!fp)
        return;

    char buf[2048];
    while (fgets(buf, sizeof(buf), fp))
        printf("%s", buf);

    pclose(fp);
}

/* Ejecutar ocultando salida (para verificar éxito) */
int runSilent(const char *cmd)
{
    return system(cmd);
}

/* Ping con resultado + salida completa */
void doPing(const char *ip)
{
    char silent[128];
    snprintf(silent, sizeof(silent), "ping -c 3 %s > /dev/null 2>&1", ip);

    int ok = runSilent(silent);

    if (ok == 0)
        printf("[ OK ] Ping a %s\n", ip);
    else
        printf("[FAIL] Ping a %s\n", ip);

    char full[128];
    snprintf(full, sizeof(full), "ping -c 3 %s", ip);
    run(full);
}

/* Traceroute */
void doTrace(const char *ip)
{
    printf("\nTraceroute a %s\n", ip);
    char cmd[128];
    snprintf(cmd, sizeof(cmd), "traceroute -n %s", ip);
    run(cmd);
}

/* Pruebas para un router (todas sus interfaces) */
void testRouter(const char *nombre, const char *lista[], int cant)
{
    printf("\n===========================\n");
    printf("Pruebas RIP del router %s\n", nombre);
    printf("===========================\n");

    for (int i = 0; i < cant; i++)
    {
        doPing(lista[i]);
        doTrace(lista[i]);
    }
}

/* Port Unreachable desde routers (cuando llegan UDP no RIP) */
void testPortUnreachable(const char *ip)
{
    printf("\nPrueba ICMP Port Unreachable contra %s\n", ip);

    /* udp random port (no 520) hacia IP de una interfaz de router */
    char cmd[128];
    snprintf(cmd, sizeof(cmd), "nc -u -w 1 %s 9999 > /dev/null 2>&1", ip);

    int ok = runSilent(cmd);

    if (ok == 0)
        printf("[ OK ] Se envió tráfico UDP; revisar con tcpdump/wireshark que el router genere ICMP Port Unreachable.\n");
    else
        printf("[WARN] nc retornó error (verificar instalación de netcat).\n");
}

/* Net unreachable hacia una red inexistente */
void testNetUnreachable(void)
{
    printf("\nPrueba Net Unreachable (red inexistente 123.123.123.0/24)\n");
    doPing("123.123.123.123");
    doTrace("123.123.123.123");
}

/* RPC (para ver que RIP mantiene conectividad) */
void testRPC(void)
{
    printf("\nPruebas RPC con RIP (cuando ya convergió):\n");

    /*run("python3.8 /home/redes/obligatorio2-enrutamiento/Obligatorio1/test_cliente.py");*/
    run("python3.8 /home/redes/obligatorio2-enrutamiento/Obligatorio1/test_cliente2.py");

    printf("RPC ejecutado.\n");
}



/* MAIN */
int main(void)
{

    printf("=========================================\n");
    printf(" PRUEBAS AUTOMATIZADAS - Parte 2 (RIP)  \n");
    printf("=========================================\n");

    /* 1) Conectividad básica con RIP ya convergido */
    printf("\n===== PRUEBA 1: Conectividad básica con RIP =====\n");

    testRouter("vhost1", v1, 3);
    testRouter("vhost2", v2, 3);
    testRouter("vhost3", v3, 3);
    testRouter("vhost4", v4, 3);
    testRouter("vhost5", v5, 3);

    printf("\nPruebas con servidores (RIP ya convergido):\n");
    doPing(srv1);
    doTrace(srv1);

    doPing(srv2);
    doTrace(srv2);

    /* 2) ICMP Port / Net unreachable (igual que Parte 1, pero ahora con ruteo dinámico) */
    printf("\n===== PRUEBA 2: ICMP de error con RIP =====\n");
    testPortUnreachable("100.0.0.50"); /* IP de una interfaz de router */
    testNetUnreachable();

    /* 3) RPC sobre rutas aprendidas por RIP */
    printf("\n===== PRUEBA 3: RPC usando rutas RIP =====\n");
    testRPC();

    printf("\nFIN DE TODAS LAS PRUEBAS RIP\n");
    return 0;
}
