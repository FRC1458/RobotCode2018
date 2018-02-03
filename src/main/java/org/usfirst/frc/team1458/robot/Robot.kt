package org.usfirst.frc.team1458.robot

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import org.usfirst.frc.team1458.lib.core.BaseRobot
import jaci.pathfinder.Pathfinder
import jaci.pathfinder.Trajectory
import jaci.pathfinder.Waypoint
import org.usfirst.frc.team1458.lib.actuator.Compressor
import org.usfirst.frc.team1458.lib.core.AutoMode
import org.usfirst.frc.team1458.lib.pathfinding.SplineFollower
import org.usfirst.frc.team1458.lib.util.flow.delay
import org.usfirst.frc.team1458.lib.util.flow.systemTimeMillis


class Robot : BaseRobot() {

    val oi = OI()
    val robot = RobotMapPracticeChassis()

    var drivingSpline = false
    //var driveToExchange : DriveToExchange = DriveToExchange()

    override fun robotSetup() {
        Compressor().start()
    }


    override fun setupAutoModes() {
        addAutoMode(AutoMode.create {
            robot.drivetrain.tankDrive(0.0, 0.0)
            delay(500)
            SplineFollower (
                    leftCSV = if(GameData2018.getOwnSwitch() == GameData2018.Side.LEFT) {
                        "/home/admin/pathleft_left_detailed.csv"
                    } else {
                        "/home/admin/pathright_left_detailed.csv"
                    },
                    rightCSV = if(GameData2018.getOwnSwitch() == GameData2018.Side.LEFT) {
                        "/home/admin/pathleft_right_detailed.csv"
                    } else {
                        "/home/admin/pathright_right_detailed.csv"
                    },
                    drivetrain = robot.drivetrain,
                    //gyro = robot.navX.yaw,
                    gyro_kP = -0.12,
                    name = "OwnSwitch",
                    stopFunc = { !(isAutonomous && isEnabled) }
            ).auto()
        })
        /* Debug Thingy
        addAutoMode(AutoMode.create {
            robot.drivetrain.setRawDrive(1.0, 1.0)

            val time = systemTimeMillis
            while(systemTimeMillis - time < 2000.0){

                //SmartDashboard.putNumber("lefterror", robot.drivetrain.leftMaster._talonInstance.getClosedLoopError(0).toDouble())
                //SmartDashboard.putNumber("righterror", robot.drivetrain.rightMaster._talonInstance.getClosedLoopError(0).toDouble())
                //SmartDashboard.putNumber("left rate", robot.drivetrain.leftMaster.connectedEncoder.rate!! * 1.11 / 360.0)
                SmartDashboard.putNumber("right rate", robot.drivetrain.rightMaster.connectedEncoder.rate!! * 1.11 / 360.0)
                SmartDashboard.putNumber("left STU", robot.drivetrain.leftMaster._talonInstance!!.getSelectedSensorVelocity(0).toDouble())
                SmartDashboard.putNumber("right STU", robot.drivetrain.rightMaster._talonInstance!!.getSelectedSensorVelocity(0).toDouble())
                delay(2)
            }
            robot.drivetrain.setRawDrive(0.0, 0.0)
        })*/
    }


    override fun threadedSetup() {

    }


    override fun teleopInit() {

    }


    override fun teleopPeriodic() {
        SmartDashboard.putNumber("left", robot.drivetrain.leftMaster.connectedEncoder.angle)
        SmartDashboard.putNumber("right", robot.drivetrain.rightMaster.connectedEncoder.angle)

        SmartDashboard.putNumber("lefterror", robot.drivetrain.leftMaster._talonInstance!!.getClosedLoopError(0).toDouble())
        SmartDashboard.putNumber("righterror", robot.drivetrain.rightMaster._talonInstance!!.getClosedLoopError(0).toDouble())
        SmartDashboard.putNumber("left rate", robot.drivetrain.leftMaster.connectedEncoder.rate * Math.PI * 1.11 / 360.0)
        SmartDashboard.putNumber("right rate", robot.drivetrain.rightMaster.connectedEncoder.rate * Math.PI * 1.11 / 360.0)

       /* SmartDashboard.putNumber("Yaw Angle", robot.navX.yaw.angle)
        SmartDashboard.putNumber("Yaw Rate", robot.navX.yaw.rate)
        SmartDashboard.putNumber("Yaw Heading", robot.navX.yaw.heading)

        SmartDashboard.putNumber("Pitch Angle", robot.navX.pitch.angle)
        SmartDashboard.putNumber("Pitch Rate", robot.navX.pitch.rate)
        SmartDashboard.putNumber("Pitch Heading", robot.navX.pitch.heading)

        SmartDashboard.putNumber("Roll Angle", robot.navX.roll.angle)
        SmartDashboard.putNumber("Roll Rate", robot.navX.roll.rate)
        SmartDashboard.putNumber("Roll Heading", robot.navX.roll.heading)

        SmartDashboard.putBoolean("IsMoving", robot.navX.isMoving)
        SmartDashboard.putBoolean("IsRotating", robot.navX.isRotating)*/
        //SmartDashboard.putNumber("WheelOut", oi.steerAxis.value)

        /*if(oi.autoButton.triggered) {
            if(!drivingSpline) {
                drivingSpline = true


            }
        } else {
            robot.drivetrain.tankDrive(oi.xbox.leftY.value, oi.xbox.rightY.value)
        }*/
        when {
            oi.driveStraightButton.triggered -> robot.drivetrain.tankDrive(oi.rightAxis.value, oi.rightAxis.value)
            oi.turnButton.triggered -> robot.drivetrain.tankDrive(oi.leftAxis.value, -oi.leftAxis.value)
            else -> robot.drivetrain.tankDrive(oi.leftAxis.value, oi.rightAxis.value)
        }

        //robot.drivetrain.scaledArcadeDrive(oi.throttleAxis.value, oi.steerAxis.value, oi.quickturnButton.triggered)
    }


    override fun runTest() {

    }

    override fun robotDisabled() {
        //startTime = -1.0
        robot.drivetrain.tankDrive(0.0, 0.0)
    }

}
