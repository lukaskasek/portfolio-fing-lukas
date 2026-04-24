from setuptools import find_packages, setup

package_name = 'tennis_detector_pkg'

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
    description='Vision based tennis ball detector',
    license='Apache-2.0',
    tests_require=['pytest'],
    entry_points={
        'console_scripts': [
            'tennis_ball_debug = tennis_detector_pkg.tennis_ball_debug:main',
            'tennis_ball_detector = tennis_detector_pkg.tennis_ball_detector:main',
        ],
    },
)
