package org.usfirst.frc.team1458.lib.util.maths.kinematics

import org.usfirst.frc.team1458.lib.util.maths.Extrapolable
import org.usfirst.frc.team1458.lib.util.maths.Interpolable
import org.usfirst.frc.team1458.lib.util.maths.TurtleMaths
import org.usfirst.frc.team1458.lib.util.maths.eq
import java.text.DecimalFormat


/**
 * Translation in a 2D coordinate system - just an (x, y) pair.
 *
 * Relative to the robot, X is forward/back and y is left/right.
 */
class Translation2D(val x: Double, val y : Double) : Interpolable<Translation2D>, Extrapolable<Translation2D> {

    constructor() : this(0.0, 0.0)

    val magnitude : Double = Math.hypot(x, y)
    val direction : Rotation2D
            get() = Rotation2D(x, y)

    val inverse : Translation2D
        get() = Translation2D(-x, -y)

    operator fun plus(other : Translation2D) =
            Translation2D(this.x + other.x, this.y + other.y)

    operator fun minus(other : Translation2D) =
            Translation2D(this.x - other.x, this.y - other.y)

    operator fun times(other : Double) =
            Translation2D(this.x * other, this.y * other)

    operator fun times(other : Rotation2D) = this rotatedBy other

    operator fun div(other : Double) =
            Translation2D(this.x / other, this.y / other)

    infix fun rotatedBy(other : Rotation2D) : Translation2D =
            Translation2D(x * other.cos - y * other.sin, x * other.sin + y * other.cos);

    operator fun unaryPlus() = this
    operator fun unaryMinus() = this.inverse

    // Dot Product
    infix fun dot(other: Translation2D) = this.x * other.x + this.y * other.y
    operator fun times(other : Translation2D) = this dot other

    // Cross Product
    infix fun cross(other: Translation2D) = this.x * other.y - other.x * this.y
    infix fun x(other: Translation2D) = this cross other

    fun angleWith(other: Translation2D) : Rotation2D {
        if(this == other) return Rotation2D()

        var cosAngle = (this * other) / (this.magnitude * other.magnitude)

        if(cosAngle.isNaN()) {
            return Rotation2D()
        }
        return Rotation2D.fromRadians(Math.acos(TurtleMaths.constrain(cosAngle, -1.0, 1.0)))
    }

    fun equals(other: Translation2D) : Boolean {
        return this.x eq other.x && this.y eq other.y
    }

    override fun toString(): String {
        return "(" + DecimalFormat("#0.000").format(x) + ", " + DecimalFormat("#0.000").format(y) + ")"
    }

    /**
     * Interpolates {@code this} (which is time point 0) with {@code other} (which is time point 1) at time point {@code x} (which must be in the closed interval [0, 1])
     */
    override fun interpolate(other: Translation2D, x: Double): Translation2D {
        if (x <= 0) {
            return this
        } else if (x >= 1) {
            return other
        }
        return Translation2D(x * (other.x - this.x) + this.x, x * (other.y - this.y) + this.y);
    }

    /**
     * Extrapolates a value from {@code this} (which is time point 0) and {@code other} (which is time point 1) at time point {@code x} (which should be greater than 1)
     */
    override fun extrapolate(other: Translation2D, x: Double): Translation2D {
        if(x <= 1) return interpolate(other, x)

        return Translation2D(x * (other.x - this.x) + this.x, x * (other.y - this.y) + this.y);
    }

    companion object {
        val ZERO : Translation2D = Translation2D(0.0, 0.0)
        val IDENTITY : Translation2D = Translation2D(1.0, 1.0)

        val FORWARD : Translation2D = Translation2D(1.0, 0.0)
        val BACKWARD : Translation2D = Translation2D(-1.0, 0.0)

        val LEFT : Translation2D = Translation2D(0.0, 1.0)
        val RIGHT : Translation2D = Translation2D(0.0, -1.0)
    }
}