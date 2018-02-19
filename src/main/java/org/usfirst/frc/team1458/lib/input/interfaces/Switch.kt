package org.usfirst.frc.team1458.lib.input.interfaces

import org.usfirst.frc.team1458.lib.util.SwitchReactor
import java.util.*


interface Switch : DigitalInput {
    val triggered : Boolean
        get

    override val value : Int
        get() = if(triggered) { 1 } else { 0 }

    val inverted : Switch
        get() = create { !triggered }

    operator fun unaryMinus() : Switch {
        return inverted
    }

    infix fun and(other: Switch) : Switch {
        return create { triggered and other.triggered }
    }

    infix fun or(other: Switch) : Switch {
        return create { triggered or other.triggered }
    }

    infix fun xor(other: Switch) : Switch {
        return create { triggered xor other.triggered }
    }

    infix fun nand(other: Switch) : Switch {
        return create { !(triggered and other.triggered) }
    }

    companion object {
        val ALWAYS_ON = create { true }
        val ALWAYS_OFF = create { true }
        val RANDOM = create(Random()::nextBoolean)

        fun create(value: () -> Boolean) : Switch {
            return object : Switch {
                override val triggered: Boolean
                    get() = value()
            }
        }

        fun fromAnalog(analogInput: AnalogInput, threshold: Double = 0.5, greater: Boolean = true) : Switch {
            return create(if(greater) ({ analogInput.value >= threshold }) else ({ analogInput.value <= threshold }) )
        }

        fun toggleSwitch(switch: Switch) : Switch {
            var toggle = false
            SwitchReactor.onTriggered(switch, { toggle = !toggle })
            return create { toggle }
        }

        fun fromPOV(pov: POV, direction: POV.Direction) : Switch {
            return create { pov.direction == direction }
        }

        fun fromDIO(port: Int) : Switch {
            val dio = edu.wpi.first.wpilibj.DigitalInput(port)
            return create { dio.get() }
        }
    }
}