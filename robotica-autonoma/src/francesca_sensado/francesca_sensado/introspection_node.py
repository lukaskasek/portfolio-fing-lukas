import rclpy
from rclpy.node import Node
from std_msgs.msg import String
import psutil
import platform
import datetime

class IntrospectionNode(Node):

    def __init__(self):
        super().__init__('introspection_node')
        self.publisher_ = self.create_publisher(String, 'francesca/status', 10)
        self.timer = self.create_timer(5.0, self.publish_status)

    def publish_status(self):
        battery = psutil.sensors_battery()
        cpu = psutil.cpu_percent()
        ram = psutil.virtual_memory()
        uptime = datetime.datetime.now() - datetime.datetime.fromtimestamp(psutil.boot_time())
        system = platform.system()
        node_name = self.get_name()

        msg = String()
        msg.data = (
            f"[{node_name}] Sistema: {system} | "
            f"Carga batería: {battery.percent if battery else 'N/A'}% | "
            f"Conectado: {battery.power_plugged if battery else 'N/A'} | "
            f"CPU: {cpu}% | RAM: {ram.percent}% | "
            f"Uptime: {str(uptime).split('.')[0]}"
        )

        self.publisher_.publish(msg)
        self.get_logger().info(f'Publicado: {msg.data}')


def main(args=None):
    rclpy.init(args=args)
    node = IntrospectionNode()
    rclpy.spin(node)
    node.destroy_node()
    rclpy.shutdown()


if __name__ == '__main__':
    main()
