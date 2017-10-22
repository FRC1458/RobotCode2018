package org.usfirst.frc.team1458.robot

import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.RobotState
import edu.wpi.first.wpilibj.buttons.Button
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import org.usfirst.frc.team1458.lib.core.BaseRobot
import org.usfirst.frc.team1458.lib.hardware.Hardware
import org.usfirst.frc.team1458.lib.input.*
import org.usfirst.frc.team1458.lib.motor.Motor
import org.usfirst.frc.team1458.lib.motor.MotorSet
import org.usfirst.frc.team1458.lib.motor.abilities.BrakeMode
import org.usfirst.frc.team1458.lib.util.maths.InputFunction
import org.usfirst.frc.team1458.lib.util.maths.RangeShifter
import org.usfirst.frc.team1458.lib.util.maths.TurtleMaths

/**
 * Final robot for the club fair
 */
open class ClubFairRobot : BaseRobot() {

    internal lateinit var xboxController: XboxController

    internal lateinit var left: AnalogInput
    internal lateinit var right: AnalogInput

    internal lateinit var shooterButton: Switch
    internal lateinit var climbButton: Switch

    internal lateinit var leftMotor: Motor
    internal lateinit var rightMotor: Motor

    internal lateinit var shooterMotor: Motor
    internal lateinit var climberMotor: Motor

    override fun robotSetup() {
        xboxController = Hardware.HumanInterfaceDevices.XboxController(0) // TODO change to correct port

        left = xboxController.leftY.scale(InputFunction.IDENTITY)
        right = xboxController.rightY.scale(InputFunction.IDENTITY)

        shooterButton = xboxController.getButton(XboxController.XboxButton.Y)
        climbButton = xboxController.getButton(XboxController.XboxButton.A)

        // TODO change to right ports
        leftMotor = MotorSet(
                Hardware.Motors.TalonSRX_CAN(10),
                Hardware.Motors.TalonSRX_CAN(11),
                Hardware.Motors.TalonSRX_CAN(12)).invert()
        rightMotor = MotorSet(
                Hardware.Motors.TalonSRX_CAN(13),
                Hardware.Motors.TalonSRX_CAN(14),
                Hardware.Motors.TalonSRX_CAN(15))

        shooterMotor = Hardware.Motors.TalonSRX_CAN(18, BrakeMode.COAST).invert()
        climberMotor = Hardware.Motors.TalonSRX_CAN(17, BrakeMode.BRAKE)
    }

    override fun setupAutoModes() {

    }

    override fun threadedSetup() {

    }

    override fun teleopInit() {

    }

    override fun teleopPeriodic() {
        leftMotor.speed = left.value
        rightMotor.speed = right.value

        shooterMotor.setSpeed(if (shooterButton.get()) -0.5 else 0.0)
        climberMotor.speed = climbButton.value.toDouble()

        SmartDashboard.putNumber("Battery", DriverStation.getInstance().batteryVoltage)
        SmartDashboard.putString("Robot", if (RobotState.isEnabled()) "Enabled" else "Disabled")
    }

    override fun runTest() {

    }

    override fun robotDisabled() {

    }

    companion object {

        //private val INPUT_FUNCTION : InputFunction = { it / 3.0 }  // max 1/3 speed
    }
}
