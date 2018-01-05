package org.usfirst.frc.team1458.lib.input

import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj.Joystick
import org.usfirst.frc.team1458.lib.input.interfaces.AnalogInput
import org.usfirst.frc.team1458.lib.input.interfaces.POV
import org.usfirst.frc.team1458.lib.input.interfaces.Switch
import java.util.*
import kotlin.concurrent.schedule

interface FlightStick {

    val pitchAxis
        get() = getAxis(1)

    val rollAxis
        get() = getAxis(0)

    val yawAxis
        get() = getAxis(3)

    val throttle
        get() = getAxis(2)

    val trigger
        get() = getButton(1)

    fun getAxis(axis: Int): AnalogInput
    fun getButton(button: Int): Switch
    fun getPOV(): POV

    companion object {
        fun create(getButtonFunc: (Int) -> Switch, getAxisFunc: (Int) -> AnalogInput,
                   getPOVFunc: () -> POV): FlightStick {
            return object : FlightStick {
                override fun getAxis(axis: Int): AnalogInput = getAxisFunc(axis)

                override fun getButton(button: Int): Switch = getButtonFunc(button)

                override fun getPOV(): POV = getPOVFunc()
            }
        }

        fun flightStick(USBport: Int): FlightStick {
            var j = Joystick(USBport)

            return FlightStick.create({ button -> Switch.create { j.getRawButton(button) } },
                    { axis -> AnalogInput.create { j.getRawAxis(axis) } },
                    { POV.create { POV.Direction.fromAngle(j.pov) } })
        }
    }
}