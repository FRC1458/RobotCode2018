package org.usfirst.frc.team1458.robot

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import org.usfirst.frc.team1458.lib.core.BaseRobot
import org.usfirst.frc.team1458.lib.core.SplineFollower


class Robot : BaseRobot() {

    val oi = OI()
    val robot = RobotMapPracticeChassis()

    override fun robotSetup() {
        
    }


    override fun setupAutoModes() {
        addAutoMode(SplineFollower("/home/admin/path1_left_detailed.csv", "/home/admin/path1_right_detailed.csv", robot.drivetrain, gyro = robot.navX.yaw))
    }


    override fun threadedSetup() {

    }


    override fun teleopInit() {

    }


    override fun teleopPeriodic() {
        SmartDashboard.putNumber("left", robot.drivetrain.leftMaster.connectedEncoder.angle)
        SmartDashboard.putNumber("right", robot.drivetrain.rightMaster.connectedEncoder.angle)

        SmartDashboard.putNumber("left rate", robot.drivetrain.leftMaster.connectedEncoder.rate)
        SmartDashboard.putNumber("right rate", robot.drivetrain.rightMaster.connectedEncoder.rate)

        SmartDashboard.putNumber("Yaw Angle", robot.navX.yaw.angle)
        SmartDashboard.putNumber("Yaw Rate", robot.navX.yaw.rate)
        SmartDashboard.putNumber("Yaw Heading", robot.navX.yaw.heading)

        SmartDashboard.putNumber("Pitch Angle", robot.navX.pitch.angle)
        SmartDashboard.putNumber("Pitch Rate", robot.navX.pitch.rate)
        SmartDashboard.putNumber("Pitch Heading", robot.navX.pitch.heading)

        SmartDashboard.putNumber("Roll Angle", robot.navX.roll.angle)
        SmartDashboard.putNumber("Roll Rate", robot.navX.roll.rate)
        SmartDashboard.putNumber("Roll Heading", robot.navX.roll.heading)

        SmartDashboard.putBoolean("IsMoving", robot.navX.isMoving)
        SmartDashboard.putBoolean("IsRotating", robot.navX.isRotating)

        robot.drivetrain.cheesyDrive(oi.throttleAxis.value, oi.steerAxis.value, oi.quickTurnButton.triggered)
    }


    override fun runTest() {

    }


    override fun robotDisabled() {
        //startTime = -1.0
    }

}