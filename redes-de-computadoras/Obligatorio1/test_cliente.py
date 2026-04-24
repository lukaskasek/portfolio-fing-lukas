# test_cliente.py
from client import *
import xml.etree.ElementTree as ET
import threading
import time
import random
from queue import Queue
from client import Client 

def test_cliente():
    print('Inicio prueba de cliente')
    print('=============================')
    client = Client()
    client.connect("150.150.0.2", 8081)
    #client.connect("127.0.0.1", 8081)
    

    # primero probamos el echo mensaje
    res = client.echo("Prueba de eco")
    assert res == "Prueba de eco"
    print("eco del servidor funciona correctamente")

    # saludo
    res = client.saludo("Nombre", "Apellido")
    assert res == "¡Hola Nombre,Apellido!"
    print("saludo del servidor funciona correctamente")

    # bienvenida
    res = client.bienvenida()
    assert res == "Bienvenido"
    print("bienvenida del servidor funciona correctamente")

    # suma (ahora devuelve int, no "10")
    res = client.suma(5, 5)
    assert res == 10
    print("suma funciona correctamente")

    # resta (int)
    res = client.resta(70, 10)
    assert res == 60
    print("resta funciona correctamente")

    # echo_concat (string)
    res = client.echo_concat("h", "o", "l", "a")
    assert res == "hola"
    print("echo_concat funciona correctamente")

    # multiplicacion (int)
    res = client.multiplicacion(10, 10)
    assert res == 100
    print("multiplicación funciona correctamente")

    # division (int)
    res = client.division(8, 2)
    assert res == 4
    print("división funciona correctamente")

    # division_completa (array)
    res = client.division_completa(10, 7)
    assert res == [1, 3]
    print("division_completa funciona correctamente")

    print("=============================")
    print("Pruebas de casos sin errores completadas.")
    print("=============================")

    print("Iniciando pruebas de casos con errores.")

    # método inexistente → el cliente devuelve string Fault 2
    res = client.metodo_inexistente()
    assert isinstance(res, str) and res.startswith("Fault ")
    print("Llamada a un método inexistente genera fault OK")

    #DEBUG
    #req = b"""POST /RPC2 HTTP/1.1\r\nHost: 127.0.0.1:8080\r\nContent-Type: text/xml\r\nContent-Length: 116\r\n\r\n<?xml version="1.0"?><methodCall><methodName>test</methodName><params><param><value><int>5</int></value></param><param><value><int>0</int></value></param></params></methodCall>"""
    #client.socket.sendall(req)
    #raw = client.socket.recv(4096).decode("utf-8", errors="replace")
    #print("DEBUG RESPUESTA CRUDA DEL SERVER:\n", raw)

    print(res)

    # menos parámetros (TypeError en el server) → Fault 3
    res = client.suma(2)
    assert isinstance(res, str) and res.startswith("Fault 3")
    print("Llamada con parámetros insuficientes genera fault OK")
    print(res)

    # más parámetros (TypeError) → Fault 3
    res = client.resta(6, 2, 3)
    assert isinstance(res, str) and res.startswith("Fault 3")
    print("Llamada con más parámetros de lo esperado genera fault OK")
    print(res)

    print("=============================")
    print("Pruebas de casos con errores completadas.")
    print("=============================")


    print("Iniciando prueba de error interno en la ejecución del método")
    print("=============================")

 
    # división por cero → Fault 4
    res = client.division(5, 0)
    assert isinstance(res, str) and res.startswith("Fault 4")
    print("División por cero genera fault OK")
    print(res)



    print("=============================")
    print("Prueba de error interno completada.")

    print("Iniciando pruebas nuevas del mail")
    print("=============================")

    # Metodo sin parámetros
    res = client.suma(2)
    assert isinstance(res, str) and res.startswith("Fault 3")
    print("Llamada con parámetros insuficientes genera fault OK")
    print(res)

    # Metodo lento
    import time
    start = time.time()
    res = client.metodo_lento()
    end = time.time()
    print(f"Método lento tomó {end - start:.2f} segs, respuesta:", res)

    # Metodo que no existe
    res = client.metodo_inexistente()
    print("Método inexistente:", res)

    # echo con mensaje muy grande
    #long_text = lorem.words(20000)
    lorem_words = """
    lorem ipsum dolor sit amet consectetur adipiscing elit sed do eiusmod tempor incididunt ut labore et dolore magna aliqua
    ut enim ad minim veniam quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat
    duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur
    excepteur sint occaecat cupidatat non proident sunt in culpa qui officia deserunt mollit anim id est laborum
    """.split()
    words = [random.choice(lorem_words) for _ in range(20000)]
    textomuylargo = " ".join(words)
    res = client.echo(textomuylargo)
    assert res == textomuylargo
    print("Prueba de eco con mensaje muy grande funciona correctamente")

 # Cerrar socket del cliente
    try:
        client.clientSocket.close()
    except Exception:
        pass


#test concurrencia de clientes en el mismo servidor
def client_task(idx, host, port, errores_q):
    try:
        # cada hilo abre su propio cliente
        client = Client()
        #client.connect("150.150.0.2", 8081)
        client.connect(host, port)
        #conn = Client.connect(host, port)

        # pequeñas variaciones de inicio para el accept()
        time.sleep(random.uniform(0, 0.2))

        # echo
        msg = f"hilo_{idx}_hola"
        r = client.echo(msg)
        assert r == msg, f"hilo {idx}: echo esperado {msg}, obtuve {r}"

        # suma
        a, b = idx, idx + 7
        s = client.suma(a, b)
        assert s == a + b, f"hilo {idx}: suma incorrecta {a}+{b}!={s}"

        # cerrar
        try:
            client.clientSocket.close()
        except Exception:
            pass

    except Exception as e:
        errores_q.put((idx, repr(e)))

def prueba_concurrencia(num_clientes=5, host="150.150.0.2", port=8081):
#def prueba_concurrencia(num_clientes=5, host="127.0.0.1", port=8081):
    print(f"=== Concurrencia: {num_clientes} clientes → {host}:{port} ===")
    errores = Queue()
    hilos = []

    for i in range(num_clientes):
        t = threading.Thread(target=client_task, args=(i, host, port, errores))
        t.start()
        hilos.append(t)

    # esperar a que terminen (con timeout por si uno se cuelga)
    for t in hilos:
        t.join(timeout=5)

    # chequear errores
    if not errores.empty():
        print("\n--- ERRORES ---")
        while not errores.empty():
            idx, err = errores.get()
            print(f"Hilo {idx}: {err}")
        raise AssertionError("Falló la prueba de concurrencia")

    print("Concurrencia OK")



if __name__ == "__main__":
    test_cliente()
    prueba_concurrencia(num_clientes=5, host="150.150.0.2", port=8081)
    #prueba_concurrencia(num_clientes=5, host="127.0.0.1", port=8081)

    