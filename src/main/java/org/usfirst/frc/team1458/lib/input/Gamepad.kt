package org.usfirst.frc.team1458.lib.input

import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj.Joystick
import kotlin.concurrent.schedule
import org.usfirst.frc.team1458.lib.input.interfaces.AnalogInput
import org.usfirst.frc.team1458.lib.input.interfaces.POV
import org.usfirst.frc.team1458.lib.input.interfaces.Switch
import java.util.*

interface Gamepad {

    val leftX
        get() = getAxis(0)
    val leftY
        get() = getAxis(1)
    val leftT
        get() = getAxis(2)

    val rightX
        get() = getAxis(4)
    val rightY
        get() = getAxis(5)
    val rightT
        get() = getAxis(3)

    fun getButton(button: Button): Switch = getButton(button.number)

    fun getAxis(axis: Int): AnalogInput
    fun getButton(button: Int): Switch
    fun getPOV(): POV

    fun rumble(strength: Double, timeMillis: Double)

    enum class Button(val number: Int) {
        A(1), B(2), X(3), Y(4), LBUMP(5), RBUMP(6), SELECT(7), START(8), LSTICK(9), RSTICK(10)
    }

    companion object {
        fun create(getButtonFunc: (Int) -> Switch, getAxisFunc: (Int) -> AnalogInput,
                   getPOVFunc: () -> POV, rumbleFunc: (Double, Double) -> Unit): Gamepad {
            return object : Gamepad {
                override fun getAxis(axis: Int): AnalogInput = getAxisFunc(axis)

                override fun getButton(button: Int): Switch = getButtonFunc(button)

                override fun getPOV(): POV = getPOVFunc()

                override fun rumble(strength: Double, timeMillis: Double) = rumbleFunc(strength, timeMillis)
            }
        }

        fun xboxController(USBport: Int): Gamepad {
            var j = Joystick(USBport)

            fun rumble(strength: Double, timeMillis: Double) {
                j.setRumble(GenericHID.RumbleType.kLeftRumble, strength)
                j.setRumble(GenericHID.RumbleType.kRightRumble, strength)

                Timer().schedule(timeMillis.toLong()) {
                    j.setRumble(GenericHID.RumbleType.kLeftRumble, 0.0)
                    j.setRumble(GenericHID.RumbleType.kRightRumble, 0.0)
                }
            }

            return create({ button -> Switch.create { j.getRawButton(button) } },
                          { axis -> AnalogInput.create { j.getRawAxis(axis) }  },
                          { POV.create { POV.Direction.fromAngle(j.pov) } },
                          ::rumble)
        }
    }
}