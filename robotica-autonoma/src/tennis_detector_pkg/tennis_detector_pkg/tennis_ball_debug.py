import rclpy
from rclpy.node import Node
from sensor_msgs.msg import Image
from vision_msgs.msg import BoundingBox2D
from cv_bridge import CvBridge
import cv2

class TennisBallDebug(Node):
    def __init__(self):
        super().__init__('tennis_ball_debug')
        self.bridge = CvBridge()

        self.create_subscription(Image, '/camera/image', self.image_callback, 10)
        self.create_subscription(BoundingBox2D, '/ball_bbox', self.pose_callback, 10)

        self.image = None

    def pose_callback(self, msg):
        if self.image is not None:
            cx, cy = msg.center.position.x, msg.center.position.y
            w, h = msg.size_x, msg.size_y
            x1 = int(cx - w / 2)
            y1 = int(cy - h / 2)
            x2 = int(cx + w / 2)
            y2 = int(cy + h / 2)

            cv2.rectangle(self.image, (x1, y1), (x2, y2), (0, 255, 0), 2)

            cv2.imshow("Tennis Ball Debug", self.image)
            cv2.waitKey(1)

    def image_callback(self, msg):
        self.image = self.bridge.imgmsg_to_cv2(msg, desired_encoding='bgr8')


def main(args=None):
    rclpy.init(args=args)
    node = TennisBallDebug()
    rclpy.spin(node)
    node.destroy_node()
    rclpy.shutdown()

if __name__ == '__main__':
    main()
