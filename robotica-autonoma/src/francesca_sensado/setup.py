from setuptools import find_packages, setup

package_name = 'francesca_sensado'

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
    maintainer='ubuntu',
    maintainer_email='nicolas.gugliucci@fing.edu.uy',
    description='TODO: Package description',
    license='TODO: License declaration',
    tests_require=['pytest'],
    entry_points={
        'console_scripts': [
            'introspection_node = francesca_sensado.introspection_node:main',
            'lidar_filter = francesca_sensado.lidar_filter:main',
        ],
    
    },
)
