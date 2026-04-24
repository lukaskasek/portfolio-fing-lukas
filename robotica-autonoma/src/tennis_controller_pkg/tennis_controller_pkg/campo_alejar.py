import math
import rclpy
from rclpy.node import Node
from sensor_msgs.msg import LaserScan
from geometry_msgs.msg import Vector3
from rclpy.qos import qos_profile_sensor_data 

class campo_alejar(Node):
    def __init__(self):
        super().__init__('campo_alejar')
        self.pot_pub = self.create_publisher(Vector3, '/potencial2', 10)
        self.create_subscription(LaserScan, '/scan', self.scan_callback, qos_profile_sensor_data)

        self.threshold = 1.0  # distancia mínima para alejarse (metros)
        self.k = 0.5  # ganancia para la fuerza de alejamiento

        # Defino el sector angular de interés en grados
        self.alpha_deg = 110
        self.beta_deg = -110

        # Convertir a radianes para comparar con LaserScan
        self.alpha = math.radians(self.alpha_deg)
        self.beta = math.radians(self.beta_deg)

    def scan_callback(self, msg: LaserScan):
        # Obtener índices del sector angular
        # Angulo inicial y final del scan
        angle_min = msg.angle_min
        angle_max = msg.angle_max
        angle_increment = msg.angle_increment

        # Calcular índice para alpha y beta dentro del arreglo ranges
        # Clamp para evitar índices fuera de rango
        start_index = max(0, int((self.alpha - angle_min) / angle_increment))
        end_index = min(len(msg.ranges) - 1, int((self.beta - angle_min) / angle_increment))

        # Obtener las distancias solo dentro del sector
        sector_ranges = msg.ranges[start_index:end_index + 1]

        # Filtrar valores inválidos (0 o inf)
        sector_ranges_valid = [r for r in sector_ranges if r > 0.0 and r < float('inf')]

        if not sector_ranges_valid:
            # No hay datos válidos en el sector
            self.get_logger().info("No hay obstáculos en el sector definido.")
            vector = Vector3()
            vector.x = 0.0
            vector.y = 0.0
            vector.z = 0.0
            self.pot_pub.publish(vector)
            return

        min_dist = min(sector_ranges_valid)

        vector = Vector3()
        if min_dist < self.threshold:
            fuerza = self.k * (self.threshold - min_dist)
            vector.x = -fuerza  # Alejar hacia atrás
            vector.y = 0.0
            vector.z = 0.0
            self.get_logger().info(f"Obstáculo en sector [{self.alpha_deg},{self.beta_deg}]° a {min_dist:.2f} m, alejando con fuerza {fuerza:.2f}")
        else:
            vector.x = 0.0
            vector.y = 0.0
            vector.z = 0.0

        self.pot_pub.publish(vector)


def main(args=None):
    rclpy.init(args=args)
    node = campo_alejar()
    rclpy.spin(node)
    node.destroy_node()
    rclpy.shutdown()

if __name__ == '__main__':
    main()
