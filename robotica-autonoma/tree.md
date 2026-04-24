# List of files in this repository

```txt
.
├── ideas.txt
├── LICENSE
├── README.md
├── src                                             The source code for the project
│   └── francesca_pkg                                 The main package
│       ├── application
│       ├── bringup                                     Stuff necessary to start the system
│       │   ├── CMakeLists.txt
│       │   ├── config                                    Configuration files
│       │   │   ├── francesca_controllers.yaml              ros2_control configuration file
│       │   │   ├── francesca.rviz                          rviz configuration file
│       │   │   ├── gz_bridge.yaml                          gz <-> ros2 translation specification
│       │   │   └── joystick.yaml                           confiration used by the joystick.launch.py
│       │   ├── launch                                    Launch files
│       │   │   ├── francesca_gz.launch.py                  Start francesca inside simulator
│       │   │   ├── francesca.launch.py                     Start real francesca
│       │   │   └── joystick.launch.py                      Start a joystick remote control
│       │   └── package.xml
│       ├── description                                 Stuff describing the robot
│       │   ├── CMakeLists.txt
│       │   ├── hooks                                     ?
│       │   │   ├── description.dsv.in
│       │   │   └── description.sh.in
│       │   ├── models                                    URDF models
│       │   │   └── francesca                               the robot
│       │   │       ├── butia.xacro                           macro for generating the base
│       │   │       ├── camera.xacro                          macro for generating the camera
│       │   │       ├── dimensions_macros.xacro               macro storing the dimensions of stuff
│       │   │       ├── francesca.urdf.xacro                  main urdf for francesca (also a macro)
│       │   │       ├── inertial_macros.xacro                 macro with utility functions for intertia moments
│       │   │       ├── lidar.xacro                           macro for generating the lidar
│       │   │       ├── materials_macros.xacro                macro with utility functions for colors and stuff
│       │   │       └── ros2_control.xacro                    macro for generating the ros_control description
│       │   └── package.xml
│       ├── gazebo                                      Gazebo configuration
│       │   ├── CMakeLists.txt                            this + hooks/ + include/ + src/ shows how to build a module for gz
│       │   ├── hooks
│       │   │   ├── CMakeLists.txt
│       │   │   ├── gazebo.dsv.in
│       │   │   ├── gazebo.sh.in
│       │   │   ├── package.xml
│       │   │   └── README.md
│       │   ├── include
│       │   │   └── gazebo
│       │   │       ├── BasicSystem.hh
│       │   │       └── FullSystem.hh
│       │   ├── package.xml
│       │   ├── src
│       │   │   ├── BasicSystem.cc
│       │   │   └── FullSystem.cc
│       │   └── worlds                                  Contains the world specification files (SDFs)
│       │       └── francesca_world.sdf                   a simple world with a box and a cylinder
│       ├── LICENSE
│       └── README.md
└── tree.txt                                        This file                                  

```
