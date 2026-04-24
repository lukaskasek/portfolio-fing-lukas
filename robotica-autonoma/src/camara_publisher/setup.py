from setuptools import find_packages, setup

package_name = 'camara_publisher'

setup(
    name=package_name,
    version='0.0.0',
    packages=find_packages(exclude=['test']),
    data_files=[
        ('share/ament_index/resource_index/packages',
            ['resource/' + package_name]),
        ('share/' + package_name, ['package.xml']),
    ],
    install_requires=['setuptools', "opencv-python", "numpy"],
    zip_safe=True,
    maintainer='estefany03',
    maintainer_email='estefany.bica@fing.edu.uy',
    description='ROS 2 node for publishing camera images from a socket connection',
    license='Apache-2.0',
    tests_require=['pytest'],
    entry_points={
        'console_scripts': [
            'camara_publisher = camara_publisher.camara_publisher_function:main',
        ],
    },
)
