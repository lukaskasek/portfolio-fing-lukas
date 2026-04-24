from ament_index_python import get_package_share_directory
from launch import LaunchDescription
from launch.actions import DeclareLaunchArgument, RegisterEventHandler, SetEnvironmentVariable, IncludeLaunchDescription
from launch.launch_description_sources import PythonLaunchDescriptionSource
from launch.conditions import IfCondition
from launch.event_handlers import OnProcessExit
from launch.substitutions import Command, FindExecutable, PathJoinSubstitution, LaunchConfiguration
from launch_ros.descriptions import ParameterValue
from launch_ros.actions import SetParameter
from launch_ros.actions import Node

import os

def generate_launch_description():
    # Declare arguments
    declared_arguments = []
    declared_arguments.append(
        DeclareLaunchArgument(
            "gui",
            default_value="true",
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
            "world",
            default_value="francesca_world",
            description="Gz sim World.",
        )
    )
    declared_arguments.append(
        DeclareLaunchArgument(
            'use_lidar',
            default_value="true",
            description='Enable the lidar sensor.',
        )
    )
    declared_arguments.append(
        DeclareLaunchArgument(
            'use_camera',
            default_value="true",
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
    declared_arguments.append(
        DeclareLaunchArgument(
            'use_sim_time',
            default_value="true",
            description='If true, use simulated clock',
        )
    )

    # Initialize Arguments
    gui = LaunchConfiguration("gui")
    use_dummy_motors = LaunchConfiguration("use_dummy_motors")
    use_lidar = LaunchConfiguration("use_lidar")
    use_camera = LaunchConfiguration("use_camera")
    use_icp_odometry = LaunchConfiguration("use_icp_odometry")
    use_introspection = LaunchConfiguration("use_introspection")
    use_sim_time = LaunchConfiguration('use_sim_time', default=True)

    # Setup project paths
    pkg_project_bringup = get_package_share_directory('bringup')
    pkg_project_gazebo = get_package_share_directory('gazebo')
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
            "use_dummy_motors:=",use_dummy_motors,
            " ",
            "use_lidar:=",use_lidar,
            " ",
            "use_camera:=",use_camera,
            " ",
            "use_gazebo:=true",
        ]
    )

    # Find configuration files
    robot_controllers = PathJoinSubstitution(
        [pkg_project_bringup, "config", "francesca_controllers.yaml"]
    )
    rviz_config_file = PathJoinSubstitution(
        [pkg_project_bringup, 'config', "francesca.rviz"]
    )
    gz_bridge_config_file = PathJoinSubstitution(
        [pkg_project_bringup, 'config', "gz_bridge.yaml"]
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
    
    # Start the gazebo application and load the world
    gazebo = IncludeLaunchDescription(
        PythonLaunchDescriptionSource([os.path.join(
            get_package_share_directory('ros_gz_sim'), 'launch'), '/gz_sim.launch.py']),
            launch_arguments=[
            ('gz_args', [LaunchConfiguration('world'), '.sdf',
                            # ' -v 4',
                            ' -r'
                        ]
            )
        ]
    )

    # Inserts the robot from the robot_decription topic into gazebo
    gz_spawn_entity = Node(
        package='ros_gz_sim',
        executable='create',
        output='both',
        arguments=['-topic', '/robot_description',
                   '-x', '0.0',
                   '-y', '0.0',
                   '-z', '0.07',
                   '-R', '0.0',
                   '-P', '0.0',
                   '-Y', '0.0',
                   '-name', 'francesca_robot',
                   '-allow_renaming', 'true'],
    )
    
    # Node that translates between ros and gazebo messages
    bridge = Node(
        package='ros_gz_bridge',
        executable='parameter_bridge',
        #arguments=['/clock@rosgraph_msgs/msg/Clock[gz.msgs.Clock'],
        parameters=[{
            'config_file': gz_bridge_config_file,
            'qos_overrides./tf_static.publisher.durability': 'transient_local',
        }],
        output='both'
    )
    
    # Node that translates between ros and gazebo cameras
    ros_gz_image_bridge = Node(
        package="ros_gz_image",
        executable="image_bridge",
        arguments=["/camera/image_raw"],
        condition=IfCondition(use_camera),
    )
    
    icp_odometry_node = Node(
        package="rtabmap_odom",
        executable="icp_odometry",
        name="icp_odometry",
        output="screen",
        remappings=[
            ("odom", "odom_icp")
        ],
        parameters=[{"use_sim_time": use_sim_time}],
        condition=IfCondition(use_icp_odometry)
    )
    
    introspection_node = Node(
        package="francesca_sensado",
        executable="introspection_node",
        name="introspection_node",
        output="screen",
        condition=IfCondition(use_introspection),
    )

    # Activate the joint state publisher controller
    # The controller itself is inside gazebo
    joint_state_broadcaster_spawner = Node(
        package="controller_manager",
        executable="spawner",
        arguments=["joint_state_broadcaster"],
    )

    # Activate the differential drive controller
    # The controller itself is inside gazebo
    francesca_base_controller_spawner = Node(
        package="controller_manager",
        executable="spawner",
        arguments=["francesca_base_controller", "--param-file", robot_controllers],
    )

    # The nodes that compose our application
    # Notice there are no nodes for ros_controller, camera, or 
    # Lidar: their equivalent exist inside gazebo.
    nodes = [
        # Start joint_state_broadcaster_spawner controller after gazebo (and its ros2_control)
        RegisterEventHandler(
            event_handler=OnProcessExit(
                target_action=gz_spawn_entity,
                on_exit=[joint_state_broadcaster_spawner],
            )
        ),
        # Start the francesca_base_controller controller after gazebo (and its ros2_control)
        RegisterEventHandler(
            event_handler=OnProcessExit(
                target_action=joint_state_broadcaster_spawner,
                on_exit=[francesca_base_controller_spawner],
            )
        ),
        # Start rviz after joint_state_broadcaster_spawner has finished
        RegisterEventHandler(
            event_handler=OnProcessExit(
                target_action=joint_state_broadcaster_spawner,
                on_exit=[rviz_node],
            )
        ),
        robot_state_pub_node,
        gazebo,
        bridge,
        ros_gz_image_bridge,
        gz_spawn_entity,
        icp_odometry_node,
        introspection_node,
    ]
    
    # When using gazebo, we want all the Nodes to use the same
    # simulated time source.
    set_parameters = [
        SetParameter(name='use_sim_time', value=use_sim_time)
    ]

    return LaunchDescription(
        declared_arguments + set_parameters + nodes
    )
