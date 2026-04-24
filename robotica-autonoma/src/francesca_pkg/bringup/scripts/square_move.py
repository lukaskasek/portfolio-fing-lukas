#!/usr/bin/env python3

import rclpy
from rclpy.node import Node
from geometry_msgs.msg import TwistStamped
import time

class SquareMover(Node):
    def __init__(self):
        super().__init__('square_mover')
        self.publisher = self.create_publisher(TwistStamped, '/cmd_vel', 10)

    def publish_twist(self, linear_x=0.0, linear_y=0.0, linear_z=0.0,
                      angular_x=0.0, angular_y=0.0, angular_z=0.0,
                      num_msgs=1, frequency=10):
        period = 1.0 / frequency
        
        twist_msg = TwistStamped()
        twist_msg.twist.linear.x = linear_x
        twist_msg.twist.linear.y = linear_y
        twist_msg.twist.linear.z = linear_z
        twist_msg.twist.angular.x = angular_x
        twist_msg.twist.angular.y = angular_y
        twist_msg.twist.angular.z = angular_z
        twist_msg.header.frame_id = "base_link"
        
        for _ in range(num_msgs):
            twist_msg.header.stamp = self.get_clock().now().to_msg()
            self.publisher.publish(twist_msg)
            time.sleep(period)

        # Publicar un mensaje de stop para asegurar frenado
        stop_msg = TwistStamped()
        stop_msg.twist.linear.x = 0.0
        stop_msg.twist.angular.z = 0.0
        stop_msg.header.frame_id = "base_link"
        for _ in range(3):
            stop_msg.header.stamp = self.get_clock().now().to_msg()
            self.publisher.publish(stop_msg)
            time.sleep(period)

    def run(self):
        for i in range(4):
            self.get_logger().info(f'Lado {i+1}: Avanzando...')
            self.publish_twist(linear_x=0.5, angular_z=0.0, num_msgs=4, frequency=2)
            self.get_logger().info('Esperando 5 segundos...')
            time.sleep(5)

            self.get_logger().info(f'Lado {i+1}: Girando...')
            self.publish_twist(linear_x=0.0, angular_z=1.0, num_msgs=12, frequency=2)
            self.get_logger().info('Esperando 5 segundos...')
            time.sleep(5)

        self.get_logger().info('Completado el cuadrado.')

def main(args=None):
    rclpy.init(args=args)
    node = SquareMover()
    node.run()
    node.destroy_node()
    rclpy.shutdown()

if __name__ == '__main__':
    main()
