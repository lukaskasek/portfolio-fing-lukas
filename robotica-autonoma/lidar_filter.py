import rclpy
from rclpy.node import Node
from rclpy.qos import qos_profile_sensor_data
from sensor_msgs.msg import LaserScan
import math

MIN_DISTANCE_FORWARD_DETECTION = 0.2
MIN_DISTANCE_BACK_DETECTION = 0.3

class LidarFilter(Node):
    def __init__(self):
        super().__init__('lidar_filter')
        self.sub = self.create_subscription(LaserScan, '/scan', self.callback, qos_profile_sensor_data)
        self.pub = self.create_publisher(LaserScan, '/scan_filtered', 10)

    def callback(self, msg):
        for i, r in enumerate(msg.ranges):
            if math.isinf(r) or math.isnan(r):
                msg.ranges[i] = r
                continue

            angle = msg.angle_min + i * msg.angle_increment

            # Parte trasera (90°): 135°–225°
            if angle < -2.356 or angle > 2.356: #TODO: Chequear que esto sea respecto a como esta puesto el LiDAR y ver si las distancias son correctas
                if r > MIN_DISTANCE_BACK_DETECTION:
                    # publish point
                    msg.ranges[i] = r
                else:
                    msg.ranges[i] = math.inf
            else:
                if angle > -0.5  or angle < 0.5:
                    msg.ranges[i] = r
                elif r > MIN_DISTANCE_FORWARD_DETECTION:
                    # publish point
                    msg.ranges[i] = r
                else:
                    msg.ranges[i] = math.inf
       
        #self.get_logger().info()
        self.pub.publish(msg)
        

def main(args=None):
    rclpy.init(args=args)
    node = LidarFilter()
    rclpy.spin(node)
    rclpy.shutdown()