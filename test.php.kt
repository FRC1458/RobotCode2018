package org.usfirst.frc.team1458.robot

class Robot : BaseRobot() {
    val oi = OI()
    val robot = RobotMapPracticeChassis()

    override fun robotSetup() {
        Compressor().start()
    }

    override fun teleopPeriodic() {
        robot.drivetrain.cheesyDrive(
                            oi.throttleAxis.value,
                            oi.steerAxis.value,
                            oi.quickturnButton.triggered)
    }
}
