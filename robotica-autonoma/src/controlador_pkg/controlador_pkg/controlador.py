#!/usr/bin/env python3
import rclpy
from rclpy.node import Node
from geometry_msgs.msg import Vector3, TwistStamped
import time

class controlador(Node):
    def __init__(self):
        super().__init__('controlador')

        # Suscripciones a dos tópicos de potencial
        self.sub1 = self.create_subscription(Vector3, '/potencial1', self.pot1_callback, 10)
        self.sub2 = self.create_subscription(Vector3, '/potencial2', self.pot2_callback, 10)
        self.pub = self.create_publisher(TwistStamped, '/cmd_vel', 10)

        # PID parámetros (pueden ajustarse)
        self.kp = 0.1
        self.ki = 0.0
        self.kd = 0.2

        self.prev_error = 0.0
        self.integral = 0.0
        self.prev_time = time.time()

        # Últimos valores recibidos
        self.pot1 = None
        self.pot2 = None

        # Timer para publicar periódicamente (ej. 20 Hz)
        self.timer = self.create_timer(0.05, self.control_loop)

        self.get_logger().info("Nodo PID de control inicializado y escuchando /potencial1 y /potencial2")

    def pot1_callback(self, msg):
        self.pot1 = msg

    def pot2_callback(self, msg):
        self.pot2 = msg

    def control_loop(self):
        if self.pot1 is None and self.pot2 is None:
            return  # No hay datos aún

        # Usar vector potencial1 si está presente, sino (0,0,0)
        pot1_x = self.pot1.x if self.pot1 else 0.0
        pot1_z = self.pot1.z if self.pot1 else 0.0

        # Usar vector potencial2 si está presente, sino (0,0,0)
        pot2_x = self.pot2.x if self.pot2 else 0.0
        pot2_z = self.pot2.z if self.pot2 else 0.0

        # Combinar señales sumando
        combined_x = pot1_x + pot2_x
        combined_z = pot1_z + pot2_z

        current_time = time.time()
        dt = current_time - self.prev_time
        self.prev_time = current_time

        self.integral += combined_x * dt
        derivative = (combined_x - self.prev_error) / dt if dt > 0 else 0.0
        self.prev_error = combined_x

        # Control angular con PID (sobre error combinado)
        angular_z = (self.kp * combined_x + self.ki * self.integral + self.kd * derivative) * (-1)

        # Control lineal según z combinado (ejemplo)
        if combined_z < 0.1:
            linear_x = -0.3 * (0.1 - combined_z) * 10
        else:
            linear_x = 0.0

        twist = TwistStamped()
        twist.header.stamp = self.get_clock().now().to_msg()
        twist.header.frame_id = 'base_link'
        twist.twist.linear.x = float(linear_x)
        twist.twist.angular.z = float(angular_z)

        self.pub.publish(twist)

        self.get_logger().info(f"[PID] combined_error: {combined_x:.2f}, lin_x: {linear_x:.2f}, ang_z: {angular_z:.2f}")

def main(args=None):
    rclpy.init(args=args)
    node = controlador()
    rclpy.spin(node)
    node.destroy_node()
    rclpy.shutdown()

if __name__ == '__main__':
    main()
