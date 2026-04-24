
# Francesca

Francesca is a basic ROS2 control system for a Butiá2 robot. It is as simple and straightforward as possible (not much, thanks to ROS) and is expected to be used as a base for developing other robots.

* Docker image provided.

* ROS2 Jazzy and newer structure.

* ros2_control architecture.

* URDF model for the robot.

* Gazebo integration.

* Dynamixel motors.

The project's structure is described [here](tree.md).

## Installation

### Using Docker

If you want to use the provided Docker image, you can use it directly from VSCode.

You can also build it manually:

```sh
docker image build --rm -t francesca_ws:jazzy .devcontainer/
docker run -it --user ubuntu -v $PWD:/francesca_ws francesca_ws:jazzy /francesca_ws/.devcontainer/postCreateCommand.sh
```

Then you can run from there:

```sh
docker run -it --privileged --user ubuntu --network=host --ipc=host \
  -v $PWD:/francesca_ws \
  -v /tmp/.X11-unix:/tmp/.X11-unix \
  -v /dev:/dev \
  --env=DISPLAY \
 francesca_ws:jazzy /bin/bash
```

> [!TIP]
> Instead of running /bin/bash from the docker you can run tilix, a tiling graphical console.

#### Note on networking and Docker

By default, the docker image is configured to use ROS_AUTOMATIC_DISCOVERY_RANGE=LOCALHOST, which means nodes will only communicate with others on the same host. If you want to use the system through a network, you can change the configuration setting environment variables as follows:

```sh
docker run -it --privileged --user ubuntu --network=host --ipc=host \
  -v $PWD:/francesca_ws \
  -v /tmp/.X11-unix:/tmp/.X11-unix \
  -v /dev:/dev \
  -e ROS_AUTOMATIC_DISCOVERY_RANGE=SUBNET \
  -e ROS_DOMAIN_ID=15 \
  --env=DISPLAY \
 francesca_ws:jazzy /bin/bash
```

Notice that we also set the ROS_DOMAIN_ID to avoid colliding with other robots on the same network.

### Local installation

If you want to install locally, check the `.devcontainer/Dockerfile` file to see what packages are needed:

```sh
sudo apt-get install -y \
 ros-${ROS_DISTRO}-ros-gz \
 ros-${ROS_DISTRO}-gz-ros2-control
 ros-${ROS_DISTRO}-joint-state-publisher-gui \
 ros-${ROS_DISTRO}-ros2-control \
 ros-${ROS_DISTRO}-ros2-controllers \
 ros-${ROS_DISTRO}-dynamixel-hardware \
 ros-${ROS_DISTRO}-v4l2-camera \
 ros-${ROS_DISTRO}-joy \
 ros-${ROS_DISTRO}-teleop-twist-joy
```

In Docker, the project is placed in the `/francesca_ws` directory. To rebuild the project, call:

```sh
colcon build --cmake-args -DBUILD_TESTING=ON --symlink-install
```

> [!TIP]
> If the build fails, try removing the old `install` and `build` directories.

## Running

There are two launch files, one for the Gazebo simulation and another for the real robot.

### In Gazebo

To run within Gazebo, call:

```sh
ros2 launch bringup francesca_gz.launch.py use_dummy_motors:=true
```

The `use_dummy_motors` parameter uses dummy motors instead of physical Dynamixel servos. This launcher will also start an RViz visualization by default. To turn it off, add a `gui:=false` parameter.

### Physical robot

To run the real robot call:

```sh
ros2 launch bringup francesca.launch.py
```

This will not open any window by default, but you can make it open an RViz visualization by passing a `gui:=true` parameter.

#### Note on Dynamixel motors

The connection for the motors is configured in the [ros2_control.xacro](src/francesca_pkg/description/models/francesca/ros2_control.xacro) URDF macro. You might be interested in the `usb_port` and `baud_rate` fields to configure the connection to the USB adapter and the two `id` fields to specify the ID numbers for the left and right motors.

If you are using a Dynamixel motor without a full-rotation encoder, such as AX-12 or AX-18, you can make the differential controller update odometry directly from the cmd_vel. To achieve this set `open_loop: true` in the [`francesca_controllers.yaml`](src/francesca_pkg/bringup/config/francesca_controllers.yaml) file.

> [!NOTE]
> When using `use_dummy_motors`, the Dynamixel controller does not update joint positions (encoders). This means that in RViz, the wheels will not spin, nor will the robot move. RViz will work correctly when used with Gazebo, as the simulator will update the joint positions. You can make the differential controller update odometry directly from the cmd_vel setting `open_loop:true` (as described above). The robot will move in RViz, even without the wheels spinning.

### Enabing sensors

You can also start the Lidar and the v4l2-compatible camera by passing the parameters `use_lidar:=true` and `use_camera:=true`. Depending on the launch script, this will run on either the real or the gazebo-simulated device. The topics used are `/scan` for the Lidar, `/camera/image_raw` and `camera/camera_info` for the camera.

## Controlling the robot

The robot uses the following topics:

* `/cmd_vel`

* `/odom`

For example, to drive the robot in circles, do:

```sh
ros2 topic pub /cmd_vel geometry_msgs/msg/TwistStamped "{twist: {linear: {x: 0.1, y: 0.0, z: 0.0}, angular: {x: 0.0, y: 0.0, z: 1.0}}}"
```

As another example, you can control the robot with a joystick running the provided launch file (in a separate console):

```sh
ros2 launch bringup joystick.launch.py
```

To control the robot with the keyboard (in a separate console), run:

```sh
ros2 run teleop_twist_keyboard teleop_twist_keyboard --ros-args -p stamped:=true
```

## Authors and acknowledgment

<jvisca@fing.edu.uy>

<mmarzoa@fing.edu.uy>

[Grupo MINA](https://www.fing.edu.uy/inco/grupos/mina/), Facultad de Ingeniería - Udelar, 2024

## License

Apache 2.0
