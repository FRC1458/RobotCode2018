package org.usfirst.frc.team1458.robot

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import jaci.pathfinder.modifiers.TankModifier
import org.usfirst.frc.team1458.lib.actuator.SmartMotor
import org.usfirst.frc.team1458.lib.core.AutoMode
import org.usfirst.frc.team1458.lib.core.BaseRobot
import org.usfirst.frc.team1458.lib.core.SplineFollower
import org.usfirst.frc.team1458.lib.drive.TankDrive
import org.usfirst.frc.team1458.lib.pid.PIDConstants
import org.usfirst.frc.team1458.lib.sensor.NavX


class Robot : BaseRobot() {

    var modifier : TankModifier? = null

    // TODO: add encoders and enable closed-loop control and tune PID constants
    val drivetrain : TankDrive =
            TankDrive(SmartMotor.CANtalonSRX(12), SmartMotor.CANtalonSRX(15).inverted,
                    arrayOf(SmartMotor.CANtalonSRX(10),SmartMotor.CANtalonSRX(11)),
                    arrayOf(SmartMotor.CANtalonSRX(13).inverted,SmartMotor.CANtalonSRX(14).inverted),

                    true, 1.11 /*1.0475*/, 6.0,
                    pidConstantsLowGearLeft = PIDConstants(0.15, kI = 0.001, kD = 0.01, kF = 1.0/6530.5),
                    pidConstantsLowGearRight = PIDConstants(0.15, kI = 0.001, kD = 0.01, kF = 1.0/6877.7))

    val oi = OI()

    val navX = NavX.Micro_I2C()

    override fun robotSetup() {
        /*val points = arrayOf(
                Waypoint(-4.0, -1.0, Pathfinder.d2r(-45.0)),
                Waypoint(-2.0, -2.0, 0.0),
                Waypoint(0.0, 0.0, 0.0)
        )

        val config = Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, 1.0/25.0, 3.5, 1.5, 10.0)
        val trajectory = Pathfinder.generate(points, config)*/
    }


    override fun setupAutoModes() {
        /*addAutoMode(AutoMode.create {
            drivetrain.setDriveVelocity(1.5, 1.5)
        })*/
        addAutoMode(SplineFollower("/home/admin/path1_left_detailed.csv",
                "/home/admin/path1_right_detailed.csv", drivetrain, gyro = navX.yaw))
    }


    override fun threadedSetup() {

    }


    override fun teleopInit() {
        /*dataListRight = ArrayList<Double>(5000)
        dataListLeft = ArrayList<Double>(5000)

        var i = 0.0
        while(i < 3000.0) {
            i += 100.0
            delay(30)
            talonLeft.PIDsetpoint = i
            talonRight.PIDsetpoint = i
        }

        talonLeft.PIDsetpoint = 3000.0
        talonRight.PIDsetpoint = 3000.0

        delay(3200)

        i = 3000.0
        while(i > 0) {
            i -= 100.0
            delay(30)
            talonLeft.PIDsetpoint = i
            talonRight.PIDsetpoint = i
        }

        talonLeft.PIDsetpoint = 0.0
        talonRight.PIDsetpoint = 0.0

        delay(2000)

        i = 0.0
        while(i < 3000.0) {
            i += 100.0
            delay(30)
            talonLeft.PIDsetpoint = -i
            talonRight.PIDsetpoint = -i
        }

        talonLeft.PIDsetpoint = -3000.0
        talonRight.PIDsetpoint = -3000.0

        delay(3200)

        i = 3000.0
        while(i > 0) {
            i -= 100.0
            delay(30)
            talonLeft.PIDsetpoint = -i
            talonRight.PIDsetpoint = -i
        }

        talonLeft.PIDsetpoint = 0.0
        talonRight.PIDsetpoint = 0.0*/
    }


    override fun teleopPeriodic() {
        SmartDashboard.putNumber("left", drivetrain.leftMaster.connectedEncoder.angle)
        SmartDashboard.putNumber("right", drivetrain.rightMaster.connectedEncoder.angle)

        SmartDashboard.putNumber("left rate", drivetrain.leftMaster.connectedEncoder.rate)
        SmartDashboard.putNumber("right rate", drivetrain.rightMaster.connectedEncoder.rate)

        SmartDashboard.putNumber("Yaw Angle", navX.yaw.angle)
        SmartDashboard.putNumber("Yaw Rate", navX.yaw.rate)
        SmartDashboard.putNumber("Yaw Heading", navX.yaw.heading)

        SmartDashboard.putNumber("Pitch Angle", navX.pitch.angle)
        SmartDashboard.putNumber("Pitch Rate", navX.pitch.rate)
        SmartDashboard.putNumber("Pitch Heading", navX.pitch.heading)

        SmartDashboard.putNumber("Roll Angle", navX.roll.angle)
        SmartDashboard.putNumber("Roll Rate", navX.roll.rate)
        SmartDashboard.putNumber("Roll Heading", navX.roll.heading)

        SmartDashboard.putBoolean("IsMoving", navX.isMoving)
        SmartDashboard.putBoolean("IsRotating", navX.isRotating)

        drivetrain.cheesyDrive(oi.throttleAxis.value, oi.steerAxis.value, oi.quickTurnButton.triggered)
    }


    override fun runTest() {

    }


    override fun robotDisabled() {
        //startTime = -1.0
    }

}