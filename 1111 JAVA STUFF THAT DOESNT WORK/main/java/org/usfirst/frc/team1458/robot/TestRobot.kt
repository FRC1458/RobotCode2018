package org.usfirst.frc.team1458.robot

import edu.wpi.cscore.HttpCamera
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.RobotState
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import org.usfirst.frc.team1458.lib.core.AutoMode
import org.usfirst.frc.team1458.lib.core.BaseRobot
import org.usfirst.frc.team1458.lib.hardware.Hardware
import org.usfirst.frc.team1458.lib.input.AnalogInput
import org.usfirst.frc.team1458.lib.input.Switch
import org.usfirst.frc.team1458.lib.input.XboxController
import org.usfirst.frc.team1458.lib.motor.Motor
import org.usfirst.frc.team1458.lib.motor.abilities.BrakeMode
import org.usfirst.frc.team1458.lib.pid.PID
import org.usfirst.frc.team1458.lib.pid.PIDConstants
import org.usfirst.frc.team1458.lib.util.maths.InputFunction

/**
 * Final robot for the club fair
 */
class TestRobot : BaseRobot() {

    internal var xboxController: XboxController? = null

    internal var left: AnalogInput? = null
    internal var right: AnalogInput? = null

    internal var shooterButton: Switch? = null
    internal var climbButton: Switch? = null

    internal var leftDrive: Motor? = null
    internal var rightDrive: Motor? = null

    internal var shooterMotor: Motor? = null
    internal var climberMotor: Motor? = null

    internal var vision: VisionTracking? = null

    internal var pid: PID? = null

    private val INPUT_FUNCTION = InputFunction.HALF // Max half speed

    override fun robotSetup() {
        xboxController = Hardware.HumanInterfaceDevices.XboxController(1) // TODO change to correct port

        left = xboxController?.leftY?.scale(INPUT_FUNCTION)
        right = xboxController?.rightY?.scale(INPUT_FUNCTION)

        shooterButton = xboxController?.getButton(XboxController.XboxButton.Y)
        climbButton = xboxController?.getButton(XboxController.XboxButton.A)

        val leftMotor1 = Hardware.Motors.TalonSRX_CAN(10)
        val leftMotor2 = Hardware.Motors.TalonSRX_CAN(11)
        val leftMotor3 = Hardware.Motors.TalonSRX_CAN(12)

        val rightMotor1 = Hardware.Motors.TalonSRX_CAN(13)
        val rightMotor2 = Hardware.Motors.TalonSRX_CAN(14)
        val rightMotor3 = Hardware.Motors.TalonSRX_CAN(15)

        leftDrive = -(leftMotor1 + leftMotor2 + leftMotor3)
        rightDrive = rightMotor1 + rightMotor2 + rightMotor3

        shooterMotor = Hardware.Motors.TalonSRX_CAN(18, BrakeMode.COAST).invert()
        climberMotor = Hardware.Motors.TalonSRX_CAN(17, BrakeMode.BRAKE)

        vision = VisionTracking(HttpCamera("HTTP camera", "http://localhost:5800/?action=stream"))

        // TODO TUNE
        pid = PID(PIDConstants(0.0025, 0.000025, 0.001), 160.0, 20.0, true)
    }

    override fun setupAutoModes() {
        addAutoMode(object : AutoMode {
            override val name: String
                get() = "AUTOMODE"

            override fun auto() {
                leftDrive?.setSpeed(0.3)
                rightDrive?.setSpeed(-0.3)

                try {
                    Thread.sleep(500)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                leftDrive?.stop()
                rightDrive?.stop()
            }
        })
    }

    override fun threadedSetup() {

    }

    override fun teleopInit() {

    }

    override fun teleopPeriodic() {
        val x = vision!!.shooterTargetX
        SmartDashboard.putNumber("Shooter Target", x)


        if (climbButton!!.get()) {
            if (x == -1.0) {
                leftDrive?.stop()
                rightDrive?.stop()
                shooterMotor?.stop()
            } else {
                val value = pid!!.newValue(x)
                leftDrive?.setSpeed(value)
                rightDrive?.setSpeed(-value)

                // TODO make sure target is correct
                shooterMotor?.setSpeed(if (pid!!.atTarget()) -0.7 else 0.0)
            }
        } else {
            leftDrive?.setSpeed(left!!.value)
            rightDrive?.setSpeed(right!!.value)

            shooterMotor?.setSpeed(if (shooterButton!!.get()) -0.7 else 0.0)
        }

        SmartDashboard.putNumber("Battery", DriverStation.getInstance().batteryVoltage)
        SmartDashboard.putString("Robot", if (RobotState.isEnabled()) "Enabled" else "Disabled")

        //System.out.println(x);
    }

    override fun runTest() {

    }

    override fun robotDisabled() {

    }
}