#!/usr/bin/env python3
import rclpy
from rclpy.node import Node
from sensor_msgs.msg import Image
from geometry_msgs.msg import Twist
from cv_bridge import CvBridge
import cv2
import numpy as np

class ColorCenter(Node):
    def __init__(self):
        super().__init__('color_center')

        self.image_sub = self.create_subscription(Image, '/camera/image', self.image_callback, 10)
        self.bridge = CvBridge()

        # Ventana y barras para configurar HSV
        cv2.namedWindow("Conf. HSV", cv2.WINDOW_NORMAL)
        cv2.resizeWindow("Conf. HSV", 400, 300)

        cv2.createTrackbar("Lower H", "Conf. HSV", 5, 179, lambda x: None)
        cv2.createTrackbar("Upper H", "Conf. HSV", 20, 179, lambda x: None)

        cv2.createTrackbar("Lower S", "Conf. HSV", 185, 255, lambda x: None)
        cv2.createTrackbar("Upper S", "Conf. HSV", 255, 255, lambda x: None)

        cv2.createTrackbar("Lower V", "Conf. HSV", 100, 255, lambda x: None)
        cv2.createTrackbar("Upper V", "Conf. HSV", 255, 255, lambda x: None)

        self.get_logger().info("Nodo para encontrar el centro de un objeto de determinado color")

    def image_callback(self, msg):
        frame = self.bridge.imgmsg_to_cv2(msg, desired_encoding='bgr8')
        hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)

        # Leer valores de sliders
        lh = cv2.getTrackbarPos("Lower H", "Conf. HSV")
        ls = cv2.getTrackbarPos("Lower S", "Conf. HSV")
        lv = cv2.getTrackbarPos("Lower V", "Conf. HSV")
        uh = cv2.getTrackbarPos("Upper H", "Conf. HSV")
        us = cv2.getTrackbarPos("Upper S", "Conf. HSV")
        uv = cv2.getTrackbarPos("Upper V", "Conf. HSV")

        lower_orange = np.array([lh, ls, lv])
        upper_orange = np.array([uh, us, uv])

        # Segmentacion por color
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
                cx = int(M["m10"] / M["m00"])
                cy = int(M["m01"] / M["m00"])

                # Dibujar centro 
                cv2.circle(frame, (cx, cy), 5, (0, 255, 0), -1)
                h, w = frame.shape[:2]

        # Mostrar para debug
        cv2.imshow("Mascara", mask)
        cv2.imshow("Centro del objeto", frame)
        cv2.waitKey(1)


def main(args=None):
    rclpy.init(args=args)
    node = ColorCenter()
    rclpy.spin(node)
    node.destroy_node()
    cv2.destroyAllWindows()
    rclpy.shutdown()

if __name__ == '__main__':
    main()
