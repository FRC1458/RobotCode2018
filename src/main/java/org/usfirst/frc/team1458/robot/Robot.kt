package org.usfirst.frc.team1458.robot

import edu.wpi.first.wpilibj.AnalogInput
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import org.usfirst.frc.team1458.lib.core.BaseRobot
import org.usfirst.frc.team1458.lib.actuator.Compressor
import org.usfirst.frc.team1458.lib.actuator.SmartMotor
import org.usfirst.frc.team1458.lib.core.AutoMode
import org.usfirst.frc.team1458.lib.input.interfaces.Switch
import org.usfirst.frc.team1458.lib.pathfinding.SplineFollower
import org.usfirst.frc.team1458.lib.util.TelemetryLogger
import org.usfirst.frc.team1458.lib.util.flow.delay
import org.usfirst.frc.team1458.lib.util.flow.systemTimeMillis
import edu.wpi.cscore.MjpegServer
import edu.wpi.cscore.UsbCamera
import edu.wpi.cscore.VideoMode
import edu.wpi.first.wpilibj.DriverStation
import org.usfirst.frc.team1458.lib.pathfinding.RobustSplineFollower
import org.usfirst.frc.team1458.lib.pathfinding.purepursuit.Kinematics
import org.usfirst.frc.team1458.lib.sensor.PDP
import org.usfirst.frc.team1458.lib.util.maths.kinematics.RigidTransform2D
import org.usfirst.frc.team1458.lib.util.maths.kinematics.Rotation2D
import org.usfirst.frc.team1458.lib.util.maths.kinematics.Translation2D


class Robot : BaseRobot() {

    val oi = OI()
    val robot = RobotMapFinalChassis(oi)

    var drivetrainInverted = false
    var lastTriggered = false

    val compressor = Compressor()

    // Elevator stuff
    val mag1 = Switch.fromDIO(8).inverted
    val mag2 = Switch.fromDIO(9).inverted
    val elev1 = SmartMotor.CANtalonSRX(20).inverted
    val elev2 = SmartMotor.CANtalonSRX(21).inverted

    var frontCamera = UsbCamera("FrontCamera", 1)
    var rearCamera = UsbCamera("RearCamera", 0)
    val cameraServer = MjpegServer("CameraStream", 5801)

    val selectAutoAsDriveFwd = Switch.fromDIO(0).inverted // Pull to GND to select drive forward auto

    override fun robotSetup() {
        TelemetryLogger.setup(
                // General power measurements
                arrayOf("pdp_voltage", "pdp_temperature", "pdp_total_current",
                        "pdp_total_power", "pdp_total_energy") +

                // PDP per-channel measurements
                Array(16, { i -> "pdp_current_$i" }) +

                // General info (DS, FMS, Field)
                arrayOf("ds_connected", "fms_connected", "is_active", "mode") +

                // Control Board
                arrayOf("elevator1", "elevator3", "intake_pull", "intake_push", "lift", "winch") +

                // Joysticks
                arrayOf("steer_axis", "throttle_axis", "invert_button", "slowdown_button") +

                // Sensors
                arrayOf("mag1", "mag2", "drivetrain_inverted", "left_velo", "right_velo") +

                arrayOf("left_error", "right_error", "left_desired_velo", "right_desired_velo") +

                arrayOf("climberlift_current", "climberlift_voltage",
                        "climberwinch_current", "climberwinch_voltage",
                        "intake1_current", "intake1_voltage",
                        "intake2_current", "intake2_voltage",
                        "left1_current", "left1_voltage",
                        "left2_current", "left2_voltage",
                        "right1_current", "right1_voltage",
                        "right2_current", "right2_voltage",
                        "elevator1_current", "elevator1_voltage",
                        "elevator2_current", "elevator2_voltage")
        )

        frontCamera.videoMode = VideoMode(VideoMode.PixelFormat.kMJPEG, 320, 240, 15)
        frontCamera.brightness = 1
        rearCamera.videoMode = VideoMode(VideoMode.PixelFormat.kMJPEG, 320, 240, 15)
        rearCamera.brightness = 1

        cameraServer.source = frontCamera
    }


    override fun setupAutoModes() {
        val spline_kP = 0.15
        val spline_kD = 0.01
        val spline_kV = 1.0 / 6.9
        val spline_gyro_kP = 0.0 // 1.0 / 40.0

        addAutoMode(AutoMode.create {
            elev1.speed = 0.0
            elev2.speed = 0.0
            robot.intake.update(false, false)

            // Drop the Intake
            robot.drivetrain.tankDrive(0.7, 0.7)
            delay(200)
            robot.drivetrain.tankDrive(-0.7, -0.7)
            delay(200)
            robot.drivetrain.tankDrive(0.0, 0.0)
            delay(50)


            robot.drivetrain.tankDrive(-0.30, -0.30)
            delay(700)
            robot.drivetrain.tankDrive(0.0, 0.0)
            delay(50)

            var startTime = systemTimeMillis
            while(systemTimeMillis - startTime < 1500) {
                robot.intake.update(true, false)
            }
            robot.intake.update(false, false)

            startTime = systemTimeMillis
            elev1.speed = 0.4
            elev2.speed = 0.4

            while(GameData2018.getOwnSwitch() == GameData2018.Side.ERROR) {
                delay(5)
            }

            if(GameData2018.getOwnSwitch() == GameData2018.Side.LEFT) {
                SplineFollower (
                        leftCSV = "/home/admin/centerToLeft_left_detailed.csv",
                        rightCSV = "/home/admin/centerToLeft_right_detailed.csv",
                        drivetrain = robot.drivetrain, //gyro = robot.navX.yaw, gyro_kP = spline_gyro_kP,
                        //kP = spline_kP, kD = spline_kD, kV = spline_kV,
                        name = "OwnSwitch", stopFunc = { !(isAutonomous && isEnabled) },
                        everyIterationFunc = {
                            if(systemTimeMillis - startTime < 2800) {
                                elev1.speed = 0.4
                                elev2.speed = 0.4
                            } else {
                                elev1.speed = 0.0
                                elev2.speed = 0.0
                            }
                        }
                ).auto()
            } else {
                SplineFollower (
                        leftCSV = "/home/admin/centerToRight_left_detailed.csv",
                        rightCSV = "/home/admin/centerToRight_right_detailed.csv",
                        drivetrain = robot.drivetrain, //gyro = robot.navX.yaw, gyro_kP = spline_gyro_kP,
                        //kP = spline_kP, kD = spline_kD, kV = spline_kV,
                        name = "OwnSwitch", stopFunc = { !(isAutonomous && isEnabled) },
                        everyIterationFunc = {
                            if(systemTimeMillis - startTime < 2800) {
                                elev1.speed = 0.4
                                elev2.speed = 0.4
                            } else {
                                elev1.speed = 0.0
                                elev2.speed = 0.0
                            }
                        }
                ).auto()
            }

            val startTime2 = systemTimeMillis
            while(systemTimeMillis - startTime2 < 800) {
                robot.intake.update(false, true)
            }
            robot.intake.update(false, false)

            SplineFollower (
                    leftCSV = "/home/admin/auton2_left_detailed.csv",
                    rightCSV = "/home/admin/auton2_right_detailed.csv",
                    drivetrain = robot.drivetrain, //gyro = robot.navX.yaw, gyro_kP = spline_gyro_kP,
                    //kP = spline_kP, kD = spline_kD, kV = spline_kV,
                    name = "OwnSwitch", stopFunc = { !(isAutonomous && isEnabled) },
                    reversed = true
            ).auto()
        })

        // not good
        /*addAutoMode(AutoMode.create {
            robot.drivetrain.tankDrive(0.7, 0.7)
            delay(200)
            robot.drivetrain.tankDrive(-0.7, -0.7)
            delay(200)
            robot.drivetrain.tankDrive(0.0, 0.0)
            delay(50)

            RobustSplineFollower (
                    leftCSV = "/home/admin/auton1_left_detailed.csv",
                    rightCSV = "/home/admin/auton1_right_detailed.csv",
                    drivetrain = robot.drivetrain, gyro = robot.navX.yaw, gyro_kP = spline_gyro_kP,
                    kP = spline_kP, kD = spline_kD, kV = spline_kV,
                    name = "OwnSwitch", stopFunc = { !(isAutonomous && isEnabled) },
                    everyIterationFunc = { robot.intake.update(true, false) }
            ).auto()
            robot.intake.update(false, false)

            elev1.speed = 0.8
            elev2.speed = 0.8
            delay(350)
            elev1.speed = 0.0
            elev2.speed = 0.0


            RobustSplineFollower (
                    leftCSV = "/home/admin/auton2_left_detailed.csv",
                    rightCSV = "/home/admin/auton2_right_detailed.csv",
                    drivetrain = robot.drivetrain, gyro = robot.navX.yaw, gyro_kP = spline_gyro_kP,
                    kP = spline_kP, kD = spline_kD, kV = spline_kV,
                    name = "OwnSwitch", stopFunc = { !(isAutonomous && isEnabled) },
                    reversed = true
            ).auto()


            val startTime = systemTimeMillis
            elev1.speed = 0.6
            elev2.speed = 0.6

            if(GameData2018.getOwnSwitch() == GameData2018.Side.LEFT) {
                RobustSplineFollower (
                        leftCSV = "/home/admin/finalleft_left_detailed.csv",
                        rightCSV = "/home/admin/finalleft_right_detailed.csv",
                        drivetrain = robot.drivetrain, gyro = robot.navX.yaw, gyro_kP = spline_gyro_kP,
                        kP = spline_kP, kD = spline_kD, kV = spline_kV,
                        name = "OwnSwitch", stopFunc = { !(isAutonomous && isEnabled) },
                        everyIterationFunc = {
                            if(systemTimeMillis - startTime < 1900) {
                                elev1.speed = 0.6
                                elev2.speed = 0.6
                            } else {
                                elev1.speed = 0.0
                                elev2.speed = 0.0
                            }
                        }
                ).auto()
            } else {
                RobustSplineFollower (
                        leftCSV = "/home/admin/finalright_left_detailed.csv",
                        rightCSV = "/home/admin/finalright_right_detailed.csv",
                        drivetrain = robot.drivetrain, gyro = robot.navX.yaw, gyro_kP = spline_gyro_kP,
                        kP = spline_kP, kD = spline_kD, kV = spline_kV,
                        name = "OwnSwitch", stopFunc = { !(isAutonomous && isEnabled) },
                        everyIterationFunc = {
                            if(systemTimeMillis - startTime < 1900) {
                                elev1.speed = 0.6
                                elev2.speed = 0.6
                            } else {
                                elev1.speed = 0.0
                                elev2.speed = 0.0
                            }
                        }
                ).auto()
            }

            val startTime2 = systemTimeMillis
            while(systemTimeMillis - startTime2 < 800) {
                robot.intake.update(false, true)
            }
            robot.intake.update(false, false)

            RobustSplineFollower (
                    leftCSV = "/home/admin/auton2_left_detailed.csv",
                    rightCSV = "/home/admin/auton2_right_detailed.csv",
                    drivetrain = robot.drivetrain, gyro = robot.navX.yaw, gyro_kP = spline_gyro_kP,
                    kP = spline_kP, kD = spline_kD, kV = spline_kV,
                    name = "OwnSwitch", stopFunc = { !(isAutonomous && isEnabled) },
                    reversed = true
            ).auto()
        })*/

        // Drive Forward Only
        addAutoMode(AutoMode.create {
            // Shake robot to drop intake
            robot.drivetrain.tankDrive(0.7, 0.7)
            delay(200)
            robot.drivetrain.tankDrive(-0.7, -0.7)
            delay(200)
            robot.drivetrain.tankDrive(0.0, 0.0)
            delay(750)

            robot.drivetrain.tankDrive(0.4, 0.4)
            delay(4000)
            robot.drivetrain.tankDrive(0.0, 0.0)
            delay(50)

        })
    }

    override fun selectAutoMode() {
        selectedAutoModeIndex = when {
            selectAutoAsDriveFwd.triggered -> 1
            else -> 0
        }
    }

    override fun threadedSetup() {

    }


    override fun teleopInit() {
        lastTriggered = false
        robot.drivetrain.lowGear()
        robot.drivetrain.leftMaster.connectedEncoder.zero()
        robot.drivetrain.rightMaster.connectedEncoder.zero()
    }

    //var transform : RigidTransform2D = RigidTransform2D(Translation2D.ZERO, Rotation2D.FORWARD)
    //var lastLeft = 0.0
    //var lastRight = 0.0

    override fun teleopPeriodic() {

        SmartDashboard.putNumber("GYRO TEST GOOD ", robot.navX.yaw.heading)

        // Press button to invert drivetrain
        if(oi.invertButton.triggered && !lastTriggered) {
            drivetrainInverted = !drivetrainInverted

            // Switch camera if drivetrain inverted
            if(drivetrainInverted) {
                cameraServer.source = rearCamera
            } else {
                cameraServer.source = frontCamera
            }
        }
        lastTriggered = oi.invertButton.triggered

        // Manual Shift
        if(oi.shiftUpButton.triggered) {
            robot.drivetrain.highGear()
        } else if(oi.shiftDownButton.triggered) {
            robot.drivetrain.lowGear()
        }

        // Drivetrain
        robot.drivetrain.arcadeDrive(
                if (drivetrainInverted) { -0.5 * (oi.throttleAxis.value) }
                else if (oi.slowDownButton.triggered) { 0.5 * oi.throttleAxis.value }
                else { oi.throttleAxis.value },
                if (drivetrainInverted) { (oi.steerAxis.value) } else { oi.steerAxis.value }
        )

        //System.out.println("invert = $drivetrainInverted, throttle = ${oi.throttleAxis.value}, steer = ${oi.steerAxis.value}")

        // Subsystems
        robot.climber.update()
        robot.intake.update()

        // Elevator control
        val speed = if(oi.controlBoard.elevator1.triggered && !mag1.triggered) { 0.8 }
                    else if(oi.controlBoard.elevator3.triggered && !mag2.triggered) { -0.8 }
                    else { 0.0 }
        elev1.speed = speed
        elev2.speed = speed


        // Logging

        TelemetryLogger.putValue("pdp_voltage", PDP.voltage)
        TelemetryLogger.putValue("pdp_temperature", PDP.temperature)
        TelemetryLogger.putValue("pdp_total_current", PDP.totalCurrent)
        TelemetryLogger.putValue("pdp_total_power", PDP.totalPower)
        TelemetryLogger.putValue("pdp_total_energy", PDP.totalEnergy)

        for (i in 0..15) {
            TelemetryLogger.putValue("pdp_current_$i", PDP.getCurrent(i))
        }

        TelemetryLogger.putValue("ds_connected", DriverStation.getInstance().isDSAttached)
        TelemetryLogger.putValue("fms_connected", DriverStation.getInstance().isFMSAttached)
        TelemetryLogger.putValue("is_active", DriverStation.getInstance().isDSAttached)
        TelemetryLogger.putValue("mode", when {
            DriverStation.getInstance().isDisabled -> 0
            DriverStation.getInstance().isAutonomous -> 1
            DriverStation.getInstance().isOperatorControl -> 2
            DriverStation.getInstance().isTest -> 3
            else -> -1
        })

        TelemetryLogger.putValue("elevator1", oi.controlBoard.elevator1.triggered)
        TelemetryLogger.putValue("elevator3", oi.controlBoard.elevator3.triggered)
        TelemetryLogger.putValue("intake_pull", oi.controlBoard.intakePull.triggered)
        TelemetryLogger.putValue("intake_push", oi.controlBoard.intakePush.triggered)
        TelemetryLogger.putValue("lift", oi.controlBoard.lift.triggered)
        TelemetryLogger.putValue("winch", oi.controlBoard.winch.triggered)

        TelemetryLogger.putValue("steer_axis", oi.steerAxis.value)
        TelemetryLogger.putValue("throttle_axis", oi.throttleAxis.value)
        TelemetryLogger.putValue("invert_button", oi.invertButton.triggered)
        TelemetryLogger.putValue("slowdown_button", oi.slowDownButton.triggered)

        TelemetryLogger.putValue("mag1", mag1.triggered)
        TelemetryLogger.putValue("mag2", mag2.triggered)
        TelemetryLogger.putValue("drivetrain_inverted", drivetrainInverted)
        TelemetryLogger.putValue("left_velo", robot.drivetrain.leftEnc.velocityFeetSec)
        TelemetryLogger.putValue("right_velo", robot.drivetrain.rightEnc.velocityFeetSec)

        TelemetryLogger.putValue("left_error", robot.drivetrain.leftMaster._talonInstance?.getClosedLoopError(0) ?: -100000000)
        TelemetryLogger.putValue("right_error", robot.drivetrain.rightMaster._talonInstance?.getClosedLoopError(0) ?: -100000000)
        TelemetryLogger.putValue("left_desired_velo", robot.drivetrain.leftMaster.PIDsetpoint)
        TelemetryLogger.putValue("right_desired_velo", robot.drivetrain.rightMaster.PIDsetpoint)

        TelemetryLogger.putValue("climberlift_current", robot.climber.liftMotor.currentDraw)
        TelemetryLogger.putValue("climberlift_voltage", robot.climber.liftMotor.outputVoltage)
        TelemetryLogger.putValue("climberwinch_current", robot.climber.winchMotor.currentDraw)
        TelemetryLogger.putValue("climberwinch_voltage", robot.climber.winchMotor.outputVoltage)
        TelemetryLogger.putValue("intake1_current", robot.intake.leftMotor.currentDraw)
        TelemetryLogger.putValue("intake1_voltage", robot.intake.leftMotor.outputVoltage)
        TelemetryLogger.putValue("intake2_current", robot.intake.rightMotor.currentDraw)
        TelemetryLogger.putValue("intake2_voltage", robot.intake.rightMotor.outputVoltage)

        TelemetryLogger.putValue("left1_current", robot.drivetrain.leftMaster.currentDraw)
        TelemetryLogger.putValue("left1_voltage", robot.drivetrain.leftMaster.outputVoltage)
        TelemetryLogger.putValue("left2_current", robot.drivetrain.leftMotors[0].currentDraw)
        TelemetryLogger.putValue("left2_voltage", robot.drivetrain.leftMotors[0].outputVoltage)

        TelemetryLogger.putValue("right1_current", robot.drivetrain.rightMaster.currentDraw)
        TelemetryLogger.putValue("right1_voltage", robot.drivetrain.rightMaster.outputVoltage)
        TelemetryLogger.putValue("right2_current", robot.drivetrain.rightMotors[0].currentDraw)
        TelemetryLogger.putValue("right2_voltage", robot.drivetrain.rightMotors[0].outputVoltage)

        TelemetryLogger.putValue("elevator1_current", elev1.currentDraw)
        TelemetryLogger.putValue("elevator1_voltage", elev1.outputVoltage)

        TelemetryLogger.putValue("elevator2_current", elev2.currentDraw)
        TelemetryLogger.putValue("elevator2_voltage", elev2.outputVoltage)

        //val left = robot.drivetrain.
        //transform = Kinematics.integrateForwardKinematics(transform, , malador, robot.navX.yaw.orientation)

    }


    override fun runTest() {
        compressor.start()
        while(isEnabled && isTest) {
            if(oi.controlBoard.winch.triggered) {
                robot.climber.winchMotor.speed = -0.25
            } else {
                robot.climber.winchMotor.speed = 0.0
            }
        }
    }

    override fun robotDisabled() {
        robot.drivetrain.tankDrive(0.0, 0.0)
        compressor.stop()
    }

    override fun disabledPeriodic() {
        if(oi.switchCamera.triggered && !lastTriggered) {
            val temp = frontCamera
            frontCamera = rearCamera
            rearCamera = temp
        }
        lastTriggered = oi.switchCamera.triggered
        cameraServer.source = frontCamera
    }

}

