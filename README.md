# Team 1458 - FRC 2018 Robot Code (Turtleshell X)

​ ​ ​The 2018 code for FRC team 1458's robot designed to run on a RoboRIO. The primary programming language used is Kotlin which is then compiled and deployed using [GradleRIO](https://github.com/Open-RIO/GradleRIO). First time setup will download about 100 MB of dependencies to the host computer. WPILib is automatically installed and the setup does not need WPILib to be installed on the host computer.

- [Documentation](https://github.com/FRC1458/RobotCode2018/wiki)

## Host Dependencies (All platforms)

​  The host dependencies need to be installed prior to installing and deploying this code to the RoboRIO. A C++ compiler is needed so if the host is a Mac then the Xcode Command Line Tools are required. If the host runs Linux, then gcc and g++ are usually installed by default. 

If building on a Windows host then Visual Studio 2017 with Visual C++ is required to build and deploy the code to the RoboRIO. Please note that deploying the code with windows has **NOT** been tested, **please use at your own risk**.

- A Mac, Linux, or Windows computer with a C++ compiler capable of C++11 compilation
- Java JRE/OpenJDK *(The RoboRIO system image has Java 8 but any version on the host computer is fine)*

## RoboRIO Dependencies *(GradleRIO)*

​ ​ ​All GradleRIO dependencies will first download **automatically** to the host computer when Gradle detects missing or mismatching dependencies. They are then separately deployed to the RoboRIO from the host computer using GradleRIO to make deploying code much simpler.

- [WPILib](https://github.com/wpilibsuite/allwpilib) *(Includes WPILib, NetworkTables Core, CameraServer Core, and OpenCV)*
- [Phoenix-FRC-lib](https://github.com/CrossTheRoadElec/Phoenix-frc-lib) *(Includes drtivers for Talon-SRX smart motor controllers)*
- [navX-MXP](https://www.pdocs.kauailabs.com/navx-mxp/software/roborio-libraries/) *(Includes drivers for the onboard navX IMU)*
- [Pathfinder](https://github.com/JacisNonsense/Pathfinder) *(Includes software for cubic spline interpolation for the autonomous period)*

## Setup Instructions

### Mac & Linux:

1. Make sure all dependencies are downloaded and installed correctly before the next steps are followed.
2. Clone the repository somewhere and navigate to the root folder. Run the `./gradlew` command This will initialize the workspace and download all the dependencies into the folders.
3. Thats it! GradleRIO should be setup and the dependencies should now be downloaded to the host. To deploy code a RoboRIO with a valid system image, make sure the RoboRIO is reachable on the network. Then run `./gradlew deploy` to deploy the code onto the RoboRIO.

### Windows (WIP):

​ ​ ​Please note that if the host is windows the steps are similar to those above, but instead of running the unix executable with `./gradlew`, you must run the `gradlew.bat` file instead (with the same arguments). No other changes should be required, but it is **highly recommended a Mac or Linux computer is used** for this process.
