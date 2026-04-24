# client_wsl.py
import socket
import struct
import pickle
import cv2

client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client_socket.connect(('172.31.224.1', 8485))  # Reemplazá con la IP de Windows

data = b""
payload_size = struct.calcsize(">L")

while True:
    # Recibir header con el tamaño del frame
    while len(data) < payload_size:
        packet = client_socket.recv(4096)
        if not packet:
            break
        data += packet

    packed_msg_size = data[:payload_size]
    data = data[payload_size:]
    msg_size = struct.unpack(">L", packed_msg_size)[0]

    # Recibir el frame
    while len(data) < msg_size:
        data += client_socket.recv(4096)

    frame_data = data[:msg_size]
    data = data[msg_size:]

    frame = pickle.loads(frame_data)
    frame = cv2.imdecode(frame, cv2.IMREAD_COLOR)

    cv2.imshow("Desde Windows", frame)
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

client_socket.close()