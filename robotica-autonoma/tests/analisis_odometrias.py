import rclpy
from rosbag2_py import SequentialReader, StorageOptions, ConverterOptions
from rclpy.serialization import deserialize_message
from nav_msgs.msg import Odometry
import matplotlib.pyplot as plt

def leer_bag(bag_path, topic_name, msg_type):
    storage_options = StorageOptions(uri=bag_path, storage_id='mcap')
    converter_options = ConverterOptions(input_serialization_format='cdr', output_serialization_format='cdr')

    reader = SequentialReader()
    reader.open(storage_options, converter_options)

    topic_types = reader.get_all_topics_and_types()
    type_map = {topic.name: topic.type for topic in topic_types}

    print(f"Buscando mensajes en el tópico: {topic_name}")

    msgs = []

    while reader.has_next():
        topic, data, timestamp = reader.read_next()
        if topic == topic_name:
            msg = deserialize_message(data, msg_type)
            msgs.append((timestamp, msg))

    return msgs

# Inicializar ROS
rclpy.init()

# Leer datos del bag
odom_msgs_choque = leer_bag('choque_sim_datos', '/odom', Odometry)
odom_icp_msgs_choque = leer_bag('choque_sim_datos', '/odom_icp', Odometry)

odom_msgs_recorrido = leer_bag('recorrido_sim_datos', '/odom', Odometry)
odom_icp_msgs_recorrido = leer_bag('recorrido_sim_datos', '/odom_icp', Odometry)

# Finalizar ROS
rclpy.shutdown()

###################################################################################

# Extraer posiciones x, y
xs_odom = [msg.pose.pose.position.x for (_, msg) in odom_msgs_choque]
ys_odom = [msg.pose.pose.position.y for (_, msg) in odom_msgs_choque]

xs_odom_icp = [msg.pose.pose.position.x for (_, msg) in odom_icp_msgs_choque]
ys_odom_icp = [msg.pose.pose.position.y for (_, msg) in odom_icp_msgs_choque]

# Graficar trayectoria
plt.figure(figsize=(10, 6))
plt.plot(xs_odom, ys_odom, marker='o', linestyle='-', markersize=4, color='steelblue', label='Encoders (/odom)')
plt.plot(xs_odom_icp, ys_odom_icp, marker='s', linestyle='-', markersize=4, color='darkorange', label='LIDAR (/odom_icp)')
plt.ylim([-0.05,0.05])
plt.xlabel('Posición X (m)', fontsize=16)
plt.ylabel('Posición Y (m)', fontsize=16)
plt.title('Trayectoria del Robot - Prueba de colisión controlada', fontsize=16, fontweight='bold')
plt.grid(True, linestyle='--', alpha=0.5)
plt.legend(loc='best', fontsize=14)
plt.tick_params(axis='both', labelsize=14)
plt.tight_layout()

# Guardar la figura
plt.savefig("trayectoria_choque_sim.svg", dpi=300)

#############################################################################

# Extraer posiciones x, y
xs_odom = [msg.pose.pose.position.x for (_, msg) in odom_msgs_recorrido]
ys_odom = [msg.pose.pose.position.y for (_, msg) in odom_msgs_recorrido]

xs_odom_icp = [msg.pose.pose.position.x for (_, msg) in odom_icp_msgs_recorrido]
ys_odom_icp = [msg.pose.pose.position.y for (_, msg) in odom_icp_msgs_recorrido]

# Graficar trayectoria
plt.figure(figsize=(10, 6))
plt.plot(xs_odom, ys_odom, marker='o', linestyle='-', markersize=4, color='steelblue', label='Encoders (/odom)')
plt.plot(xs_odom_icp, ys_odom_icp, marker='s', linestyle='-', markersize=4, color='darkorange', label='LIDAR (/odom_icp)')

plt.xlabel('Posición X (m)', fontsize=16)
plt.ylabel('Posición Y (m)', fontsize=16)
plt.title('Trayectoria del Robot - Prueba de precisión de giro y deslizamiento', fontsize=16, fontweight='bold')
plt.grid(True, linestyle='--', alpha=0.5)
plt.legend(loc='best', fontsize=14)
plt.tick_params(axis='both', labelsize=14)
plt.tight_layout()

# Guardar la figura
plt.savefig("trayectoria_recorrido_sim.svg", dpi=300)

# Mostrar en pantalla
plt.show()