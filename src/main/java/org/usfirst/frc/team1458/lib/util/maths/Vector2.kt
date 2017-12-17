package org.usfirst.frc.team1458.lib.util.maths

/**
 * 3-element vector used to represent position/translation
 */
data class Vector3 (val x : Double, val y : Double, val z : Double) {

    val magnitude
        get() = Math.sqrt(x * x + y * y + z * z)

    val normalized
        get() = Vector3(x / magnitude, y / magnitude, z / magnitude)

    fun get(i : Int) : Double {
        return if (i == 0) { x } else if (i == 1) { y } else if (i == 2) { z } else { throw new IllegalArgumentException("Index must be 0, 1, or 2") }
    }

    fun equals(other : Vector3) : Boolean {
        return Math.abs(x - other.x) < 0.0001 && Math.abs(y - other.y) < 0.0001 && Math.abs(z - other.z) < 0.0001
    }

    /**
     * Adds another vector to this vector
     */
    fun add(other : Vector3) : Vector3 {
        return Vector3(x + other.x, y + other.y, z + other.z)
    }

    /**
     * Subtracts another vector from this vector
     */
    fun subtract(other : Vector3) : Vector3 {
        return Vector3(x - other.x, y - other.y, z - other.z)
    }

    /**
     * Multiplies this vector by a scalar
     */
    fun multiply(scalar : Double) : Vector3 {
        return Vector3(x * scalar, y * scalar, z * scalar)
    }

    /**
     * Divides this vector by a scalar
     */
    fun divide(scalar : Double) : Vector3 {
        return Vector3(x / scalar, y / scalar, z / scalar)
    }

    override fun toString() : String {
        return "<$x, $y, $z>"
    }

}