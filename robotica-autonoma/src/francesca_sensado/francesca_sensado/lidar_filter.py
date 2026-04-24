import rclpy
from rclpy.node import Node
from rclpy.qos import qos_profile_sensor_data
from sensor_msgs.msg import LaserScan
import math

MIN_FRONT_DISTANCE = 0.05  # Distancia mínima aceptada (en metros)
FRONT_ANGLE_LIMIT = 1.396  # ±80° en radianes

class LidarFilter(Node):
    def __init__(self):
        super().__init__('lidar_filter')
        self.sub = self.create_subscription(LaserScan, '/scan_raw', self.callback, qos_profile_sensor_data)
        self.pub = self.create_publisher(LaserScan, '/scan', 10)

    def callback(self, msg):
        filtered_ranges = list(msg.ranges)

        for i, r in enumerate(msg.ranges):
            if math.isinf(r) or math.isnan(r):
                continue

            angle = msg.angle_min + i * msg.angle_increment

            # Mantenemos solo los puntos que estén entre -80° y +80°
            if FRONT_ANGLE_LIMIT + 2.8 >= angle >= -FRONT_ANGLE_LIMIT + 3.3:
                if r < MIN_FRONT_DISTANCE:
                    filtered_ranges[i] = math.inf  # Muy cerca → eliminar
                # else: se mantiene
            else:
                filtered_ranges[i] = math.inf  # Fuera del cono frontal → eliminar

        msg.ranges = filtered_ranges
        self.pub.publish(msg)

def main(args=None):
    rclpy.init(args=args)
    node = LidarFilter()
    rclpy.spin(node)
    rclpy.shutdown()
