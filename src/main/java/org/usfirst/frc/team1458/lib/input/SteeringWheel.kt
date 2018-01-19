package org.usfirst.frc.team1458.lib.input

import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj.Joystick
import org.usfirst.frc.team1458.lib.input.interfaces.AnalogInput
import org.usfirst.frc.team1458.lib.input.interfaces.POV
import org.usfirst.frc.team1458.lib.input.interfaces.Switch
import java.util.*

interface SteeringWheel {

    val steering
        get() = getAxis(0)

    fun getButton(button: Button): Switch = getButton(button.number)

    fun getAxis(axis: Int): AnalogInput
    fun getButton(button: Int): Switch

    enum class Button(val number: Int) {
        // TODO implement
    }

    companion object {
        fun create(getButtonFunc: (Int) -> Switch, getAxisFunc: (Int) -> AnalogInput): SteeringWheel {
            return object : SteeringWheel {
                override fun getAxis(axis: Int): AnalogInput = getAxisFunc(axis)

                override fun getButton(button: Int): Switch = getButtonFunc(button)
            }
        }

        fun logitechSteeringWheel(USBport: Int): SteeringWheel {
            var j = Joystick(USBport)

            return create({ button -> Switch.create { j.getRawButton(button) } },
                          { axis -> AnalogInput.create { j.getRawAxis(axis) } })
        }
    }
}