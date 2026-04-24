import rclpy
from rclpy.node import Node
from sensor_msgs.msg import Image
from cv_bridge import CvBridge
from ultralytics import YOLO
from vision_msgs.msg import BoundingBox2D
import cv2

DEBUG_DRAW = True #TODO pass as a ROS 2 parameter
COFIDENCE_THRESHOLD = 0.8

if DEBUG_DRAW:
    text_none = "Ball: NONE"
    font = cv2.FONT_HERSHEY_SIMPLEX
    font_scale = 0.5
    thickness = 1

class TennisBallDetector(Node):
    def __init__(self):
        super().__init__('tennis_ball_detector')
        self.subscription = self.create_subscription(Image,'/camera/image',self.image_callback,10)
        self.publisher = self.create_publisher(BoundingBox2D, '/ball_bbox', 10)
        self.bridge = CvBridge()
        self.model = YOLO("/francesca_ws/src/tennis_detector_pkg/yolo_model/tennis0.pt")

    def image_callback(self, msg):
        frame = self.bridge.imgmsg_to_cv2(msg, desired_encoding='bgr8')
        results = self.model(frame,conf=COFIDENCE_THRESHOLD)[0]

        if results.boxes is not None and len(results.boxes) > 0:
            # Elegir el bounding box con mayor confianza
            max_box = max(results.boxes, key=lambda b: float(b.conf))

            # Extraer datos
            x1, y1, x2, y2 = map(int, max_box.xyxy[0])
            conf = float(max_box.conf)

            bounding_box = BoundingBox2D()
            bounding_box.center.position.x = (float)(x1+x2)/2
            bounding_box.center.position.y = (float)(y1+y2)/2
            bounding_box.center.theta = 0.0
            bounding_box.size_x = (float)(x2-x1)
            bounding_box.size_y = (float)(y2-y1)
            
            self.publisher.publish(bounding_box)
            
            if DEBUG_DRAW:
                self.image = self.bridge.imgmsg_to_cv2(msg, desired_encoding='bgr8')
                cv2.rectangle(self.image, (x1, y1), (x2, y2), (0, 255, 0), 2)
                cv2.putText(self.image, f"Ball: {conf:.2f}", (x1, y1 - 10),font, font_scale, (0, 255, 0), thickness)   
                cv2.imshow("Tennis Ball Detection", self.image)
                cv2.waitKey(1)
        else:
            if DEBUG_DRAW:
                self.image = self.bridge.imgmsg_to_cv2(msg, desired_encoding='bgr8')
                text_size = cv2.getTextSize(text_none, font, font_scale, thickness)[0]
                text_x = self.image.shape[1] // 2 - text_size[0] // 2
                text_y = self.image.shape[0] // 2 + text_size[1] // 2
                cv2.putText(self.image, text_none, (text_x, text_y), font, font_scale, (0, 0, 255), thickness)   
                cv2.imshow("Tennis Ball Detection", self.image)
                cv2.waitKey(1)

def main(args=None):
    rclpy.init(args=args)
    node = TennisBallDetector()
    rclpy.spin(node)
    node.destroy_node()
    rclpy.shutdown()

if __name__ == '__main__':
    main()
