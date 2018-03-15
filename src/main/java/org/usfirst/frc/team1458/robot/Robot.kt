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

    val frontCamera = UsbCamera("FrontCamera", 1)
    val rearCamera = UsbCamera("RearCamera", 0)
    val cameraServer = MjpegServer("CameraStream", 5801)

    override fun robotSetup() {
        frontCamera.videoMode = VideoMode(VideoMode.PixelFormat.kMJPEG, 480, 320, 30)
        frontCamera.brightness = 1
        rearCamera.videoMode = VideoMode(VideoMode.PixelFormat.kMJPEG, 480, 320, 30)
        rearCamera.brightness = 1

        cameraServer.source = frontCamera
    }

    override fun setupAutoModes() {
        addAutoMode(AutoMode.create {
            robot.drivetrain.tankDrive(0.7, 0.7)
            delay(200)
            robot.drivetrain.tankDrive(-0.7, -0.7)
            delay(200)
            robot.drivetrain.tankDrive(0.0, 0.0)
            delay(50)
            SplineFollower (
                    leftCSV = "/home/admin/auton1_left_detailed.csv",
                    rightCSV = "/home/admin/auton1_right_detailed.csv",
                    drivetrain = robot.drivetrain, gyro = robot.navX.yaw, gyro_kP = 0.0,
                    name = "OwnSwitch", stopFunc = { !(isAutonomous && isEnabled) },
                    everyIterationFunc = { robot.intake.update(true, false) }
            ).auto()
            robot.intake.update(false, false)

            elev1.speed = 0.8
            elev2.speed = 0.8
            delay(350)
            elev1.speed = 0.0
            elev2.speed = 0.0


            SplineFollower (
                    leftCSV = "/home/admin/auton2_left_detailed.csv",
                    rightCSV = "/home/admin/auton2_right_detailed.csv",
                    drivetrain = robot.drivetrain, gyro = robot.navX.yaw, gyro_kP = 0.0,
                    name = "OwnSwitch", stopFunc = { !(isAutonomous && isEnabled) },
                    reversed = true
            ).auto()


            val startTime = systemTimeMillis
            elev1.speed = 0.6
            elev2.speed = 0.6

            if(GameData2018.getOwnSwitch() == GameData2018.Side.LEFT) {
                SplineFollower (
                        leftCSV = "/home/admin/auton4_left_detailed.csv",
                        rightCSV = "/home/admin/auton4_right_detailed.csv",
                        drivetrain = robot.drivetrain, gyro = robot.navX.yaw, gyro_kP = 0.0,
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
                SplineFollower (
                        leftCSV = "/home/admin/auton2_left_detailed.csv",
                        rightCSV = "/home/admin/auton2_right_detailed.csv",
                        drivetrain = robot.drivetrain, gyro = robot.navX.yaw, gyro_kP = 0.0,
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

            SplineFollower (
                    leftCSV = "/home/admin/auton2_left_detailed.csv",
                    rightCSV = "/home/admin/auton2_right_detailed.csv",
                    drivetrain = robot.drivetrain, gyro = robot.navX.yaw, gyro_kP = 0.0,
                    name = "OwnSwitch", stopFunc = { !(isAutonomous && isEnabled) },
                    reversed = true
            ).auto()
        })

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
            delay(2000)
            robot.drivetrain.tankDrive(0.0, 0.0)
            delay(50)

        })
    }


    override fun threadedSetup() {

    }


    override fun teleopInit() {
        robot.drivetrain.lowGear()
        robot.drivetrain.leftMaster.connectedEncoder.zero()
        robot.drivetrain.rightMaster.connectedEncoder.zero()
    }


    override fun teleopPeriodic() {

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
                if (drivetrainInverted) { -0.5 * (oi.throttleAxis.value) } else { oi.throttleAxis.value },
                if (drivetrainInverted) { (oi.steerAxis.value) } else { oi.steerAxis.value }
        )

        System.out.println("invert = $drivetrainInverted, throttle = ${oi.throttleAxis.value}, steer = ${oi.steerAxis.value}")

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

        // TODO MOVE THIS TO ROBOTSETUP
        /*TelemetryLogger.setup(
                    // General power measurements
                    arrayOf("pdp_voltage", "pdp_temperature", "pdp_total_current",
                            "pdp_total_power", "pdp_total_energy") +

                    // PDP per-channel measurements
                    Array(16, { i -> "pdp_current_$i" }) +

                    // General info (DS, FMS, Field)
                    arrayOf("ds_connected", "fms_connected", "is_active", "mode") +
                    arrayOf())
        */
        // TODO MORE ABOVE ^^^^^^^^^^^^^^


        //DriverStation.getInstance()

        // Controls
        //TelemetryLogger.putValue("Throttle", oi.throttleAxis.value)
        //TelemetryLogger.putValue("Steer", oi.steerAxis.value)
        //TelemetryLogger.putValue("QuickTurn", oi.quickturnButton.triggered)

        // Sensors
        //TelemetryLogger.putValue("Mag1", mag1.triggered)
        //TelemetryLogger.putValue("Mag2", mag2.triggered)

        // Motors

        // Power

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

}
