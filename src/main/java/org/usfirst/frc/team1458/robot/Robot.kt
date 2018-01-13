package org.usfirst.frc.team1458.robot

import kotlinx.coroutines.experimental.runBlocking
import org.usfirst.frc.team1458.lib.actuator.SmartMotor
import org.usfirst.frc.team1458.lib.core.BaseRobot
import org.usfirst.frc.team1458.lib.drive.TankDrive
import org.usfirst.frc.team1458.lib.input.Gamepad
import org.usfirst.frc.team1458.lib.pid.PIDConstants
import org.usfirst.frc.team1458.lib.sensor.interfaces.DistanceSensor
import org.usfirst.frc.team1458.lib.util.flow.*


class Robot : BaseRobot() {

    // TODO: add encoders and enable closed-loop control and tune PID constants
    val drivetrain : TankDrive =
            TankDrive(SmartMotor.CANtalonSRX(10).inverted, SmartMotor.CANtalonSRX(13),
                    arrayOf(SmartMotor.CANtalonSRX(11).inverted,SmartMotor.CANtalonSRX(12).inverted),
                    arrayOf(SmartMotor.CANtalonSRX(14),SmartMotor.CANtalonSRX(15)),
                    false, 12.0, 5.0, PIDConstants(0.01, 0.0, 0.0, 0.5))

    val xboxController : Gamepad = Gamepad.xboxController(3)

    override fun robotSetup() {

    }


    override fun setupAutoModes() {

    }


    override fun threadedSetup() {

    }


    override fun teleopInit() {

    }


    override fun teleopPeriodic() {
        //drivetrain.tankDrive(xboxController.leftY.value, xboxController.rightY.value)
        drivetrain.arcadeDrive(xboxController.leftY.value, xboxController.rightX.value)
        /*drivetrain.scaledArcadeDrive(xboxController.leftY.value, xboxController.rightX.value,
                xboxController.getButton(Gamepad.Button.LBUMP).triggered, { 0.6 * Math.pow(it, 0.5) + 0.2 })*/

    }


    override fun runTest() {

    }


    override fun robotDisabled() {

    }

    companion object {
        fun main(args : Array<String>) {
            /*Logger.addDestination(Logger.Destination.Console(Logger.Destination.Format.HUMANREADABLE, color = true))
            Logger.addDestination(Logger.Destination.File("file1.txt", Logger.Destination.Format.CSV, color = false, minSeverity = Logger.Severity.WARN))
            Logger.addDestination(Logger.Destination.File("file2.txt", Logger.Destination.Format.HUMANREADABLE, color = true))

            go { periodic (2) { Logger.w("DATA", "DATA") } }

            runBlocking { suspendUntil { false } }*/

            runBlocking {
                var a = 0.0
                var sensor : DistanceSensor = DistanceSensor.create { a }
                for(i in 1..100) {
                    for (j in 1..10) {
                        a += 1.0
                        delay(5)
                        println(sensor.velocity)
                    }
                }
            }
        }
    }
}