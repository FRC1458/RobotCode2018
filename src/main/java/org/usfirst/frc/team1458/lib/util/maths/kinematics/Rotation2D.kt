package org.usfirst.frc.team1458.lib.util.maths.kinematics

import org.usfirst.frc.team1458.lib.util.maths.*
import java.text.DecimalFormat

/**
 * Represents a rotation in 2 dimensions (stores in degrees for better precision).
 *
 * Uses the standard unit circle
 */
class Rotation2D(private val _angle: Double) : Interpolable<Rotation2D>, Extrapolable<Rotation2D> {

    val degrees : Double = {
        var deg = _angle
        while(deg >= 360) {
            deg -= 360
        }
        while(deg < 0) {
            deg += 360
        }

        deg
    }()
    val radians : Double = Math.toRadians(degrees)

    val sin : Double = Math.sin(radians)
    val cos : Double = Math.cos(radians)
    val tan : Double = {
        var t = Math.tan(radians)
        if(t > INV_EPSILON) t = Double.NaN
        else if(t < EPSILON) t = 0.0
        t
    }()

    val normal : Rotation2D
            get() = Rotation2D(-sin, cos)
    val inverse : Rotation2D
            get() = Rotation2D(-degrees)
    val unitVector : Translation2D
            get() = Translation2D(cos, sin)

    constructor() : this(0.0)
    constructor(x : Double, y : Double) : this(Math.toDegrees(Math.atan(y / x)))

    fun equals(other : Rotation2D) : Boolean {
        return this.degrees eq other.degrees
    }

    operator fun plus(other : Rotation2D) : Rotation2D {
        return Rotation2D(degrees + other.degrees)
    }

    operator fun minus(other : Rotation2D) : Rotation2D {
        return Rotation2D(degrees - other.degrees)
    }

    operator fun times(other : Double) : Rotation2D {
        return Rotation2D(degrees * other)
    }

    operator fun div(other : Double) : Rotation2D {
        return Rotation2D(degrees / other)
    }

    infix fun rotatedBy(other : Rotation2D) : Rotation2D {
        return Rotation2D(this.degrees + other.degrees)
    }


    override fun toString(): String {
        return "(" + DecimalFormat("#0.000").format(degrees) + " deg)"
    }
    override fun interpolate(other: Rotation2D, x: Double): Rotation2D {
        if (x <= 0) {
            return this
        } else if (x >= 1) {
            return other
        }
        val deltaAngle = this.inverse rotatedBy other
        return this rotatedBy (deltaAngle * x)
    }

    override fun extrapolate(other: Rotation2D, x: Double): Rotation2D {
        if (x <= 1) {
            return interpolate(other, x)
        }
        val deltaAngle = this.inverse rotatedBy other
        return this rotatedBy (deltaAngle * x)
    }

    companion object {
        fun fromRadians(radians: Double) = Rotation2D(Math.toDegrees(radians))

        val FORWARD : Rotation2D = Rotation2D(0.0)
        val BACKWARD : Rotation2D = Rotation2D(180.0)

        val LEFT : Rotation2D = Rotation2D(90.0)
        val RIGHT : Rotation2D = Rotation2D(270.0)
    }
}