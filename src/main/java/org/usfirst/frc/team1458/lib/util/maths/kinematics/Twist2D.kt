package org.usfirst.frc.team1458.lib.util.maths.kinematics

/**
 * Constant curvature arc
 */
class Twist2D(val dx : Double, val dy : Double, val dTheta : Double) {
    operator fun times(x : Double) : Twist2D {
        return Twist2D(dx * x, dy * x, dTheta * x)
    }

    companion object {
        val IDENTITY : Twist2D = Twist2D(0.0, 0.0, 0.0)
    }
}