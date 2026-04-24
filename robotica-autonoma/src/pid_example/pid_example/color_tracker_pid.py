#!/usr/bin/env python3
import rclpy
from rclpy.node import Node
from sensor_msgs.msg import Image
from geometry_msgs.msg import TwistStamped
from cv_bridge import CvBridge
import cv2
import numpy as np
import time

class ColorTrackerPID(Node):
    def __init__(self):
        super().__init__('color_tracker_pid')

        self.image_sub = self.create_subscription(Image,'/camera/image',self.image_callback,10)
        self.cmd_pub = self.create_publisher(TwistStamped, '/cmd_vel', 10)
        self.bridge = CvBridge()

        # PID parameters
        self.kp = 0.003
        self.ki = 0.00
        self.kd = 0.00

        self.prev_error = 0.0
        self.integral = 0.0
        self.prev_time = time.time()

        self.get_logger().info("Nodo PID para seguimiento de objeto inicializado")

    def image_callback(self, msg):
        frame = self.bridge.imgmsg_to_cv2(msg, desired_encoding='bgr8')
        hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)

        # Rango para color naranja en HSV (ajustable)
        lower_orange = np.array([33, 32, 100])
        upper_orange = np.array([54, 107, 255])
        mask = cv2.inRange(hsv, lower_orange, upper_orange)

        # # Filtros morfoloficos
        # kernel = np.ones((3, 3), np.uint8)
        # mask = cv2.morphologyEx(mask, cv2.MORPH_OPEN, kernel)
        # mask = cv2.morphologyEx(mask, cv2.MORPH_CLOSE, kernel)

        # Encontrar contornos
        contours, _ = cv2.findContours(mask, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

        if contours:
            # Usar el contorno mas grande
            c = max(contours, key=cv2.contourArea)
            M = cv2.moments(c)

            if M["m00"] > 0:
                cx = int(M["m10"] / M["m00"])  # Centro del objeto
                cy = int(M["m01"] / M["m00"])

                # Dibujar
                cv2.circle(frame, (cx, cy), 5, (0, 0, 255), -1)
                h, w = frame.shape[:2]
                cv2.line(frame, (w // 2, 0), (w // 2, h), (255, 0, 0), 2)

                # Error: diferencia con el centro
                error = (w // 2) - cx

                # PID
                current_time = time.time()
                dt = current_time - self.prev_time
                self.prev_time = current_time

                self.integral += error * dt
                derivative = (error - self.prev_error) / dt if dt > 0 else 0.0
                self.prev_error = error

                angular_z = self.kp * error + self.ki * self.integral + self.kd * derivative

                # Publicar comando
                twist = TwistStamped()
                twist.header.stamp = self.get_clock().now().to_msg()
                twist.header.frame_id = 'base_link'
                twist.twist.angular.z = float(angular_z)
                self.cmd_pub.publish(twist)

                self.get_logger().info(f"Error: {error} | Angular Z: {angular_z:.3f}")
        
        # Mostrar para debug
        cv2.imshow("Mascara", mask)
        cv2.imshow("Seguimiento", frame)
        cv2.waitKey(1)


def main(args=None):
    rclpy.init(args=args)
    node = ColorTrackerPID()
    rclpy.spin(node)
    node.destroy_node()
    cv2.destroyAllWindows()
    rclpy.shutdown()

if __name__ == '__main__':
    main()

