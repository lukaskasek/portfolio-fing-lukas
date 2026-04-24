from setuptools import find_packages, setup

package_name = 'pid_example'

setup(
    name=package_name,
    version='0.0.0',
    packages=find_packages(exclude=['test']),
    data_files=[
        ('share/ament_index/resource_index/packages',
            ['resource/' + package_name]),
        ('share/' + package_name, ['package.xml']),
    ],
    install_requires=['setuptools'],
    zip_safe=True,
    maintainer='Mercedes Marzoa',
    maintainer_email='mmarzoa@fing.edu.uy',
    description='Color tracker using a basic PID controller',
    license='Apache-2.0',
    tests_require=['pytest'],
    entry_points={
        'console_scripts': [
            'color_center = pid_example.color_center:main',
            'color_tracker_pid = pid_example.color_tracker_pid:main'
        ],
    },
)
