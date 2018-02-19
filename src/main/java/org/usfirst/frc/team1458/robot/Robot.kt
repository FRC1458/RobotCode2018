package org.usfirst.frc.team1458.robot

import edu.wpi.first.wpilibj.AnalogInput
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import org.usfirst.frc.team1458.lib.core.BaseRobot
import org.usfirst.frc.team1458.lib.actuator.Compressor
import org.usfirst.frc.team1458.lib.actuator.SmartMotor
import org.usfirst.frc.team1458.lib.core.AutoMode
import org.usfirst.frc.team1458.lib.input.interfaces.Switch
import org.usfirst.frc.team1458.lib.pathfinding.SplineFollower
import org.usfirst.frc.team1458.lib.util.SoundPlayer
import org.usfirst.frc.team1458.lib.util.TelemetryLogger
import org.usfirst.frc.team1458.lib.util.flow.delay
import org.usfirst.frc.team1458.lib.util.flow.systemTimeMillis
import org.usfirst.frc.team1458.lib.util.maths.TurtleMaths


class Robot : BaseRobot() {

    val oi = OI()
    val robot = RobotMapFinalChassis(oi)

    val compressor = Compressor()

    var directionFwd = true
    var first = true

    var testincrementer = 0
    val sensorTESTTHINGY = AnalogInput(3) // TODO: remove this - it definitely will break things

    val mag1 = Switch.fromDIO(8).inverted
    val mag2 = Switch.fromDIO(9).inverted

    val elev1 = SmartMotor.CANtalonSRX(20).inverted
    val elev2 = SmartMotor.CANtalonSRX(21).inverted

    override fun robotSetup() {
        SmartDashboard.putString("Arcade", "a")

        //TelemetryLogger.setup("log1.csv", "gyro")


        SmartDashboard.putNumber("MotorSpeed", 0.0)

        TelemetryLogger.setup("/home/lvuser/goodlog.csv", "Throttle", "Steer", "QuickTurn", "CurrentDrawLeft", "CurrentDrawRight", "Incrementer", "Mag1", "Mag2")
    }



    override fun setupAutoModes() {
        /*addAutoMode(AutoMode.create {
            if(first) {
                first = false
                println("motor_speed,right_velocity,right_stddev,left_velocity,left_stddev,direction")
            }

            val speed = SmartDashboard.getNumber("MotorSpeed", 0.0)

            if(directionFwd) {
                robot.drivetrain.tankDrive(speed, speed)
            } else {
                robot.drivetrain.tankDrive(-speed, -speed)
            }
            directionFwd = !directionFwd

            delay(500)
            val start = systemTimeMillis
            val rightArr = ArrayList<Double>()
            val leftArr = ArrayList<Double>()
            while(systemTimeMillis <= start + 500) {
                rightArr.add(robot.drivetrain.rightMaster._talonInstance!!.getSelectedSensorVelocity(0).toDouble())
                leftArr.add(robot.drivetrain.leftMaster._talonInstance!!.getSelectedSensorVelocity(0).toDouble())

                delay(1)
            }
            robot.drivetrain.tankDrive(0.0, 0.0)

            println("$speed,${rightArr.average()},${TurtleMaths.calculateSD(rightArr)}," +
                    "${leftArr.average()},${TurtleMaths.calculateSD(leftArr)}," +
                    "${(if(directionFwd) {"FORWARD"} else {"REVERSE"})}")
        })*/

        /*leftCSV = if(GameData2018.getOwnSwitch() == GameData2018.Side.LEFT) {
            "/home/admin/pathleft_left_detailed.csv"
        } else {
            "/home/admin/pathright_left_detailed.csv"
        },
        rightCSV = if(GameData2018.getOwnSwitch() == GameData2018.Side.LEFT) {
            "/home/admin/pathleft_right_detailed.csv"
        } else {
            "/home/admin/pathright_right_detailed.csv"
        },*/

        addAutoMode(AutoMode.create {
            robot.drivetrain.tankDrive(0.0, 0.0)
            delay(500)
            SplineFollower (
                    leftCSV = "/home/admin/straight6feet_left_detailed.csv",
                    rightCSV = "/home/admin/straight6feet_right_detailed.csv",
                    drivetrain = robot.drivetrain, gyro = robot.navX.yaw, gyro_kP = 0.0,
                    name = "OwnSwitch", stopFunc = { !(isAutonomous && isEnabled) },
                    everyIterationFunc = { robot.intake.update(true, false) }
            ).auto()
            robot.intake.update(false, false)
        })
    }


    override fun threadedSetup() {

    }


    override fun teleopInit() {
        SoundPlayer.play("robotenabled.wav")
        SoundPlayer.play("dimelo.mp3")

        SmartDashboard.putString("Gear", "Low")
        robot.drivetrain.lowGear()

        robot.drivetrain.leftMaster.connectedEncoder.zero()
        robot.drivetrain.rightMaster.connectedEncoder.zero()
    }


    override fun teleopPeriodic() {

        robot.climber.update()

        SmartDashboard.putNumber("TESTHALLEFFECT", sensorTESTTHINGY.value.toDouble())

        SmartDashboard.putBoolean("CUBE IN INTAKE", robot.intake.cubeInIntake)

        SmartDashboard.putNumber("Angle", robot.navX.yaw.angle)

        robot.drivetrain.cheesyDrive(oi.throttleAxis.value, oi.steerAxis.value, oi.quickturnButton.triggered)

        SmartDashboard.putNumber("Throttle", oi.throttleAxis.value)
        SmartDashboard.putNumber("Steer", oi.steerAxis.value)
        SmartDashboard.putBoolean("QuickTurn", oi.quickturnButton.triggered)
        SmartDashboard.putNumber("LeftOutput", robot.drivetrain.leftMaster.PIDsetpoint)
        SmartDashboard.putNumber("RightOutput", robot.drivetrain.rightMaster.PIDsetpoint)
        SmartDashboard.putNumber("LeftError", robot.drivetrain.leftMaster._talonInstance!!.getClosedLoopError(0).toDouble())
        SmartDashboard.putNumber("RightError", robot.drivetrain.rightMaster._talonInstance!!.getClosedLoopError(0).toDouble())


        SmartDashboard.putNumber("LeftCurrent", robot.drivetrain.leftMaster._talonInstance!!.outputCurrent)
        SmartDashboard.putNumber("RightCurrent", robot.drivetrain.rightMaster._talonInstance!!.outputCurrent)

        TelemetryLogger.putValue("Throttle", oi.throttleAxis.value)
        TelemetryLogger.putValue("Steer", oi.steerAxis.value)
        TelemetryLogger.putValue("QuickTurn", oi.quickturnButton.triggered)

        TelemetryLogger.putValue("Incrementer", ++testincrementer)
        SmartDashboard.putNumber("Incrementer", testincrementer.toDouble())

        TelemetryLogger.putValue("Mag1", mag1.triggered)
        SmartDashboard.putBoolean("Mag1", mag1.triggered)

        TelemetryLogger.putValue("Mag2", mag2.triggered)
        SmartDashboard.putBoolean("Mag2", mag2.triggered)

        TelemetryLogger.putValue("CurrentDrawLeft", robot.drivetrain.leftMaster._talonInstance!!.outputCurrent)
        TelemetryLogger.putValue("CurrentDrawRight", robot.drivetrain.rightMaster._talonInstance!!.outputCurrent)


        /*when {
            oi.driveStraightButton.triggered -> robot.drivetrain.tankDrive(oi.rightAxis.value, oi.rightAxis.value)
            oi.turnButton.triggered -> robot.drivetrain.tankDrive(oi.leftAxis.value, -oi.leftAxis.value)
            else -> robot.drivetrain.tankDrive(oi.leftAxis.value, oi.rightAxis.value)
        }*/

       /* if(oi.leftStick.getButton(3).triggered) {
            intakeWheel1.speed = 0.9
            intakeWheel2.speed = 0.9
        } else if(oi.rightStick.getButton(3).triggered) {
            intakeWheel1.speed = -0.9
            intakeWheel2.speed = -0.9
        } else {
            intakeWheel1.speed = 0.0
            intakeWheel2.speed = 0.0
        }*/

        robot.intake.update()

        if(oi.leftStick.getButton(4).triggered) {

            robot.drivetrain.leftMaster.connectedEncoder.zero()
            robot.drivetrain.rightMaster.connectedEncoder.zero()
        }

        if(oi.shiftUpButton.triggered) {
            robot.drivetrain.highGear()
            SmartDashboard.putString("Gear", "High")
        } else if(oi.shiftDownButton.triggered) {
            robot.drivetrain.lowGear()
            SmartDashboard.putString("Gear", "Low")
        }

        val speed = if(oi.controlBoard.elevator1.triggered && !mag1.triggered) { 0.4 } else if(oi.controlBoard.elevator3.triggered && !mag2.triggered) { -0.4 } else { 0.0 }

        SmartDashboard.putNumber("ELEVATOR REEE", speed)

        elev1.speed = speed
        elev2.speed = speed
    }


    override fun runTest() {
        compressor.start()
    }

    override fun robotDisabled() {
        robot.drivetrain.tankDrive(0.0, 0.0)
        SoundPlayer.stop()
        SoundPlayer.play("robotdisabled.wav")

        compressor.stop()
    }

}
