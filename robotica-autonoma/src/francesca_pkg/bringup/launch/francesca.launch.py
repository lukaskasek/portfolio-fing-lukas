from ament_index_python import get_package_share_directory
from launch import LaunchDescription
from launch.actions import DeclareLaunchArgument, RegisterEventHandler
from launch.conditions import IfCondition
from launch.event_handlers import OnProcessExit
from launch.substitutions import Command, FindExecutable, PathJoinSubstitution, LaunchConfiguration
from launch_ros.descriptions import ParameterValue
from launch_ros.actions import Node

def generate_launch_description():
    # Declare arguments
    declared_arguments = []
    declared_arguments.append(
        DeclareLaunchArgument(
            "gui",
            default_value="false",
            description="Start RViz2 automatically with this launch file.",
        )
    )
    declared_arguments.append(
        DeclareLaunchArgument(
            "use_dummy_motors",
            default_value="false",
            description="Start robot with mock hardware mirroring command to its states.",
        )
    )
    declared_arguments.append(
        DeclareLaunchArgument(
            'use_lidar',
            default_value="false",
            description='Enable the lidar sensor.',
        )
    )
    declared_arguments.append(
        DeclareLaunchArgument(
            'use_lidar_filter',
            default_value="false",
            description='Enable the lidar sensor filter.',
        )
    )
    declared_arguments.append(
        DeclareLaunchArgument(
            'use_camera',
            default_value="false",
            description='Enable the camera sensor.',
        )
    )
    declared_arguments.append(
        DeclareLaunchArgument(
            'use_icp_odometry',
            default_value="false",
            description='Enable the ICP odometry node.',
        )
    )
    declared_arguments.append(
        DeclareLaunchArgument(
            'use_introspection',
            default_value="false",
            description='Enable the introspection node to publish system status.',
        )
    )
    # Initialize Arguments
    gui = LaunchConfiguration("gui")
    use_dummy_motors = LaunchConfiguration("use_dummy_motors")
    use_lidar = LaunchConfiguration("use_lidar")
    use_lidar_filter = LaunchConfiguration("use_lidar_filter")
    use_camera = LaunchConfiguration("use_camera")
    use_icp_odometry = LaunchConfiguration("use_icp_odometry")
    use_introspection = LaunchConfiguration("use_introspection")

    # Setup project paths
    pkg_project_bringup = get_package_share_directory('bringup')
    pkg_project_description = get_package_share_directory('description')

    # Get URDF via xacro
    robot_description_content = Command(
        [
            PathJoinSubstitution([FindExecutable(name="xacro")]),
            " ",
            PathJoinSubstitution(
                [pkg_project_description, 'models', 'francesca', "francesca.urdf.xacro"]
            ),
            " ",
            "use_dummy_motors:=", use_dummy_motors,
            " ",
            "use_lidar:=",use_lidar,
            " ",
            "use_lidar_filter:=",use_lidar_filter,
            " ",
            "use_camera:=",use_camera,        ]
    )

    # Find configuration files
    robot_controllers = PathJoinSubstitution(
        [pkg_project_bringup, "config", "francesca_controllers.yaml"]
    )
    rviz_config_file = PathJoinSubstitution(
        [pkg_project_bringup, 'config', "francesca.rviz"]
    )

    # Node to combine the URDF with /joint_state topics to produce 
    # the /robot_description topic
    robot_state_pub_node = Node(
        package="robot_state_publisher",
        executable="robot_state_publisher",
        output="both",
        parameters=[
            {"robot_description": ParameterValue(robot_description_content, value_type=str)},           
        ],
    )

    # Start the rviz2 visualization tool
    rviz_node = Node(
        package="rviz2",
        executable="rviz2",
        name="rviz2",
        output="log",
        arguments=["-d", rviz_config_file],
        condition=IfCondition(gui),
    )

    # start the ros2_control node. It contrinas two 
    #  controllers (see francesca_controllers.yaml):
    # 1. run the differential drive controller
    # 2. publish the joint states
    # They are activated by the spawner nodes below.
    control_node = Node(
        package="controller_manager",
        executable="ros2_control_node",
        parameters=[
            robot_controllers,
            {'lock_memory': False},
        ],
        output="both",
        remappings=[
            ("/francesca_base_controller/cmd_vel", "/cmd_vel"),
            ("/francesca_base_controller/odom", "/odom"),
            ("/francesca_base_controller/transition_event", "/transition_event"),
        ],
    )

    # Activate the differential drive controller
    robot_controller_spawner = Node(
        package="controller_manager",
        executable="spawner",
        arguments=["francesca_base_controller", "--param-file", robot_controllers],
    )

    # Activate the joint state publisher controller
    joint_state_broadcaster_spawner = Node(
        package="controller_manager",
        executable="spawner",
        arguments=["joint_state_broadcaster"],
    )
    
    icp_odometry_node = Node(
        package="rtabmap_odom",
        executable="icp_odometry",
        name="icp_odometry",
        output="screen",
        remappings=[
            ("odom", "odom_icp")
        ],
        condition=IfCondition(use_icp_odometry)
    )
    
    introspection_node = Node(
        package="francesca_sensado",
        executable="introspection_node",
        name="introspection_node",
        output="screen",
        condition=IfCondition(use_introspection),
    )
   
    # Start the Lidar Node
    lidar_node = Node(
        package='hls_lfcd_lds_driver',
        executable='hlds_laser_publisher',
        output='screen',
        parameters=[{
            'port': '/dev/serial/by-id/usb-Silicon_Labs_CP2102_USB_to_UART_Bridge_Controller_0001-if00-port0',
            'frame_id': 'lidar_link',
        }],
        remappings=[
            ("/scan", "/scan_raw")
        ],
        condition=IfCondition(use_lidar),
    )


    lidar_filter = Node(
        package='francesca_sensado',
        executable='lidar_filter',
        name="lidar_filter",
        output='screen',
        condition=IfCondition(use_lidar_filter),
    )
    
    # Start the Webcam Node
    camera_node = Node(
        package='v4l2_camera',
        executable='v4l2_camera_node',
        output='screen',
        namespace='camera',
        parameters=[{
            'image_size': [640,480],
            'time_per_frame': [1, 6],
            'frame_id': 'camera_link_optical',
        }],
        #remappings=[
        #    ("/camera/image_raw", "/camera/image_raw")
        #],
        condition=IfCondition(use_camera),
    )

    # The nodes that compose our application
    nodes = [
        # Start joint_state_broadcaster_spawner after robot_controller_spawner has finished
        RegisterEventHandler(
            event_handler=OnProcessExit(
                target_action=robot_controller_spawner,
                on_exit=[joint_state_broadcaster_spawner],
            )
        ),
        # Start rviz after joint_state_broadcaster_spawner has finished
        RegisterEventHandler(
            event_handler=OnProcessExit(
                target_action=joint_state_broadcaster_spawner,
                on_exit=[rviz_node],
            )
        ),
        lidar_node,
        lidar_filter,
        camera_node,
        control_node,
        robot_state_pub_node,
        robot_controller_spawner,
        icp_odometry_node,
        introspection_node,
    ]

    return LaunchDescription(declared_arguments + nodes)

