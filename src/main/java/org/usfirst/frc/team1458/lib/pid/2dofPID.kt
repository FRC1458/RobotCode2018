package org.usfirst.frc.team1458.lib.pid

import org.usfirst.frc.team1458.lib.util.flow.systemTimeMillis
import org.usfirst.frc.team1458.lib.util.maths.MovingAverage

/**
 * Standard synchronous PID controller (must be manually controlled)
 */
class dofPID(constants: PIDConstants, target: Double, val deadband: Double = 0.01, val decayIntegral: Boolean = false) {

    var constants: PIDConstants = constants
        set(newConstants: PIDConstants) {
            field = newConstants

            integral = 0.0
            lastTime = Double.NaN
            lastError = Double.NaN
            lastDerivative = Double.NaN
            derivativeAverage.clear()
        }


    var target: Double = target
        set(newTarget: Double) {
            field = newTarget

            integral = 0.0
            lastTime = Double.NaN
            lastError = Double.NaN
            lastDerivative = Double.NaN
            derivativeAverage.clear()
        }

    private var integral = 0.0

    private var lastTime = Double.NaN
    private var lastError = Double.NaN

    private var lastDerivative = Double.NaN

    private var derivativeAverage = MovingAverage(15)

    fun update(value: Double, derivative : Double = Double.NaN, feedforwardArgs : DoubleArray = doubleArrayOf()) : Double {
        // Calculate error
        var error = target - value

        // Calculate delta time
        var dt = (systemTimeMillis / 1000.0) - lastTime
        lastTime = systemTimeMillis / 1000.0

        // Calculate derivative
        var _derivative : Double =
                if(derivative == Double.NaN) {
                    if(lastTime == Double.NaN || lastError == Double.NaN) {
                        0.0 // No derivative in first iteration
                    } else {
                        (error - lastError) / dt // Calculate derivative
                    }
                } else {
                    derivative // Pre-calculated derivative (from sensor usually)
                }
        var lastError = error

        derivativeAverage.addNumber(_derivative)

        // Calculate integral
        integral += error * dt
        if(decayIntegral) {
            integral *= 0.75
        }

        // Calculate custom feedforward
        var customFF = constants.kF_func(feedforwardArgs)

        // Calculate output
        val output =
                + constants.kP * error
                + constants.kI * integral
                + constants.kD * derivativeAverage.average
                + constants.kF * target
                + customFF

        lastDerivative = derivativeAverage.average

        return output
    }

    fun clearIntegral() {
        integral = 0.0
    }

    /**
     * Returns whether the PID controller is currently at the target (error value is near zero)
     */
    val atTargetP : Boolean
        get() = Math.abs(lastError) < deadband

    /**
     * Returns whether the PID controller is currently at a steady-state (derivative is near zero)
     */
    val atTargetD : Boolean
        get() = Math.abs(lastDerivative) < (deadband / 10.0)

    /**
     * Returns whether the PID controller is currently at a steady-state at the target (both error and derivative are near zero)
     */
    val atTarget : Boolean
        get() = atTargetP && atTargetD

}