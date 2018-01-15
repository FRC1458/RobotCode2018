package org.usfirst.frc.team1458.lib.input.interfaces

import org.usfirst.frc.team1458.lib.util.maths.TurtleMaths

interface AnalogInput {
    /**
     * Value in the closed interval [-1.0, 1.0]
     */
    val value : Double
        get

    val inverted : AnalogInput
        get() = create { value * -1.0 }

    operator fun unaryMinus() : AnalogInput {
        return inverted
    }

    infix fun scale(scale: Double) : AnalogInput {
        return create { value * scale }
    }

    operator fun times(other: Double) : AnalogInput {
        return scale(other)
    }

    fun scale(scaleFunc: (Double) -> Double) : AnalogInput {
        return create { scaleFunc(value) }
    }

    companion object {
        fun create(func: () -> Double) : AnalogInput {
            return object : AnalogInput {
                override val value: Double
                    get() = TurtleMaths.constrain(func(), -1.0, 1.0)
            }
        }
    }
}