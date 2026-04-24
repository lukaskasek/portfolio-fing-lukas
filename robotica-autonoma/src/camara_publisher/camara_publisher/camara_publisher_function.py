import socket
import struct
import pickle
import cv2
import numpy as np

import rclpy
from rclpy.node import Node
from sensor_msgs.msg import Image
from cv_bridge import CvBridge

class CameraSocketPublisher(Node):
    def __init__(self):
        super().__init__('camera_socket_publisher')
        self.publisher_ = self.create_publisher(Image, '/camera/image', 10)
        self.bridge = CvBridge()

        # Declare and get parameters
        self.declare_parameter('server_ip', '172.31.224.1')
        self.declare_parameter('server_port', 8485)
        
        server_ip = self.get_parameter('server_ip').value
        server_port = self.get_parameter('server_port').value

        # Socket setup
        self.client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.client_socket.connect((server_ip, server_port))
        self.get_logger().info(f'Connected to server at {server_ip}:{server_port}')

        self.data = b""
        self.payload_size = struct.calcsize(">L")
        self.frame_count = 0

    def receive_and_publish(self):
        while rclpy.ok():
            # Receive header with frame size
            while len(self.data) < self.payload_size:
                packet = self.client_socket.recv(4096)
                if not packet:
                    self.get_logger().warn('No data received from server')
                    return
                self.data += packet

            packed_msg_size = self.data[:self.payload_size]
            self.data = self.data[self.payload_size:]
            msg_size = struct.unpack(">L", packed_msg_size)[0]

            # Receive frame
            while len(self.data) < msg_size:
                self.data += self.client_socket.recv(4096)

            frame_data = self.data[:msg_size]
            self.data = self.data[msg_size:]

            frame = pickle.loads(frame_data)
            frame = cv2.imdecode(frame, cv2.IMREAD_COLOR)

            # Publish as ROS Image message
            img_msg = self.bridge.cv2_to_imgmsg(frame, encoding="bgr8")
            self.publisher_.publish(img_msg)
            
            self.frame_count += 1
            self.get_logger().info(f'Published frame {self.frame_count}')

def main(args=None):
    rclpy.init(args=args)
    node = CameraSocketPublisher()
    try:
        node.receive_and_publish()
    except KeyboardInterrupt:
        node.get_logger().info('Node stopped by user')
    except Exception as e:
        node.get_logger().error(f'Error occurred: {str(e)}')
    finally:
        node.client_socket.close()
        node.destroy_node()
        rclpy.shutdown()

if __name__ == '__main__':
    main()
