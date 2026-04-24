import rclpy
from rclpy.node import Node
from vision_msgs.msg import BoundingBox2D
from geometry_msgs.msg import Vector3

class campo_acercar(Node):
    def __init__(self):
        super().__init__('campo_acercar')
        self.pot_pub = self.create_publisher(Vector3, '/potencial1', 10)
        self.create_subscription(BoundingBox2D, '/ball_bbox', self.bbox_callback, 10)

        self.image_width = 640
        self.image_height = 480
        self.k = 0.005  # Ganancia para escalar el vector

    def bbox_callback(self, msg):
        cx = msg.center.position.x
        cy = msg.center.position.y
        area = msg.size_x * msg.size_y

        error_x = cx - self.image_width / 2
        error_y = cy - self.image_height / 2

        vector = Vector3()
        vector.x = self.k * error_x
        vector.y = self.k * error_y
        vector.z = min(area / (self.image_width * self.image_height), 1.0)

        self.pot_pub.publish(vector)
        self.get_logger().info(f"Publicado: {vector}")

def main(args=None):
    rclpy.init(args=args)
    node = campo_acercar()
    rclpy.spin(node)
    node.destroy_node()
    rclpy.shutdown()

if __name__ == '__main__':
    main()
