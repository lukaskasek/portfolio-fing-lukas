# test_server.py
from server import Server
import time

# Definimos funciones que se registran en el servidor
def echo(msg):
    return msg  # string -> string

def saludo(nombre, apellido):
    # mantiene exactamente el formato original
    return f"¡Hola {nombre},{apellido}!"

def bienvenida():
    return "Bienvenido"

def suma(a, b):
    # ahora a y b ya son ints si el cliente los mandó como <int>
    return a + b

def resta(a, b):
    return a - b

def echo_concat(msg1, msg2, msg3, msg4):
    # concatena strings
    return msg1 + msg2 + msg3 + msg4

def multiplicacion(a, b):
    return a * b

def division(a, b):
    if b == 0:
        raise RuntimeError("Error interno en la ejecución del método")
    return a // b

def division_completa(a, b):
    if b == 0:
        raise RuntimeError("Error interno en la ejecución del método")    
    return [a // b, a % b]

def metodo_lento():
    time.sleep(11)  # demora más de 10 segundos
    return "Listo"


if __name__ == "__main__":
    # Crear servidor
    srv = Server("150.150.0.2", 8081)
    #srv = Server("127.0.0.1", 8081)
    # Registrar métodos
    srv.add_method(echo)
    srv.add_method(saludo)
    srv.add_method(bienvenida)
    srv.add_method(suma)
    srv.add_method(resta)
    srv.add_method(echo_concat)
    srv.add_method(multiplicacion)
    srv.add_method(division)
    srv.add_method(division_completa)
    srv.add_method(metodo_lento)

    print("Servidor escuchando en 150.150.0.2:8081 ...")
    srv.serve()


