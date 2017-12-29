package org.usfirst.frc.team1458.lib.util.maths

/**
 * Math utility classes
 */
object TurtleMaths {
    fun constrain(value: Double, min: Double, max: Double) : Double {
        if(value > max) {
            return max
        } else if (value < min) {
            return min
        } else {
            return value
        }
    }

    fun shift(value: Double, minA: Double, maxA: Double, minB: Double, maxB: Double) : Double {
        return minB + ((maxB - minB) / (maxA - minA)) * (value - minA)
    }
}