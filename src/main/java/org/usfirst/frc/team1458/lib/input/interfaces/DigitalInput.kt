package org.usfirst.frc.team1458.lib.input.interfaces

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import org.usfirst.frc.team1458.lib.util.SwitchReactor
import org.usfirst.frc.team1458.lib.util.maths.TurtleMaths

interface DigitalInput {
    val value : Int
        get

    companion object {
        fun create(func: () -> Int) : DigitalInput {
            return object : DigitalInput {
                override val value: Int
                    get() = func()
            }
        }

        fun fromAnalog(analogInput: AnalogInput, min: Double, max: Double) : DigitalInput {
            return create { TurtleMaths.shift(analogInput.value, -1.0, 1.0, min, max).toInt() }
        }

        fun fromUpDown(up: Switch, down: Switch, min: Int, max: Int, defaultValue: Int = min, smartDashboardKey: String? = null) : DigitalInput {
            var value = defaultValue
            if(smartDashboardKey != null) SmartDashboard.putNumber(smartDashboardKey, value.toDouble())

            SwitchReactor.onTriggered(up, {
                if(value < max) value++
                if(smartDashboardKey != null) SmartDashboard.putNumber(smartDashboardKey, value.toDouble())
            })
            SwitchReactor.onTriggered(down, {
                if(value > min) value--
                if(smartDashboardKey != null) SmartDashboard.putNumber(smartDashboardKey, value.toDouble())
            })

            return create { value }
        }
    }
}