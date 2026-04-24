import rclpy
from rosbag2_py import SequentialReader, StorageOptions, ConverterOptions
from rclpy.serialization import deserialize_message
from nav_msgs.msg import Odometry
from sensor_msgs.msg import LaserScan
import matplotlib.pyplot as plt
import math

def leer_bag(bag_path, topic_name, msg_type):
    storage_options = StorageOptions(uri=bag_path, storage_id='mcap')
    converter_options = ConverterOptions(input_serialization_format='cdr', output_serialization_format='cdr')

    reader = SequentialReader()
    reader.open(storage_options, converter_options)

    msgs = []
    while reader.has_next():
        topic, data, timestamp = reader.read_next()
        if topic == topic_name:
            msg = deserialize_message(data, msg_type)
            msgs.append((timestamp, msg))

    return msgs

def calcular_distancia_acumulada(odom_msgs):
    posiciones = [(msg.pose.pose.position.x, msg.pose.pose.position.y) for (_, msg) in odom_msgs]
    distancias = [0.0]
    for i in range(1, len(posiciones)):
        dx = posiciones[i][0] - posiciones[i-1][0]
        dy = posiciones[i][1] - posiciones[i-1][1]
        distancias.append(distancias[-1] + math.hypot(dx, dy))
    return distancias

def extraer_distancia_frente(scan_msgs):
    distancias = []
    for (_, msg) in scan_msgs:
        centro = len(msg.ranges) // 2
        distancia = msg.ranges[centro]
        if math.isinf(distancia) or math.isnan(distancia):
            distancia = None
        distancias.append(distancia)
    return distancias

# Inicializar y leer bags
rclpy.init()
odom_icp_msgs = leer_bag('choque_sim_datos', '/odom_icp', Odometry)
odom_msgs = leer_bag('choque_sim_datos', '/odom', Odometry)
scan_msgs = leer_bag('choque_sim_datos', '/scan', LaserScan)
rclpy.shutdown()

# Procesar datos
distancia_odom_icp = calcular_distancia_acumulada(odom_icp_msgs)
distancia_odom = calcular_distancia_acumulada(odom_msgs)
distancia_lidar = extraer_distancia_frente(scan_msgs)

# Alinear longitudes
n = min(len(distancia_odom), len(distancia_lidar), len(distancia_odom_icp))
distancia_odom_icp = distancia_odom_icp[:n]
distancia_odom = distancia_odom[:n]
distancia_lidar = distancia_lidar[:n]

# Graficar
plt.figure(figsize=(10, 6))
plt.plot(distancia_odom, label='Distancia acumulada por odometría')
plt.plot(distancia_odom_icp, label='Distancia acumulada por odometría ICP')
plt.plot(distancia_lidar, label='Distancia al obstáculo (LIDAR)')
plt.xlabel('Muestras', fontsize=12)
plt.ylabel('Distancia (m)', fontsize=12)
plt.title('Comparación: Odometría vs LIDAR', fontsize=14)
plt.legend()
plt.grid(True)
plt.tight_layout()
plt.show()
