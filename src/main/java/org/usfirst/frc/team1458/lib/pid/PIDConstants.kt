package org.usfirst.frc.team1458.lib.pid

/**
 * Class to hold PID constants
 *
 * @param kP Proportional gain
 * @param kI Integral gain
 * @param kD Derivative gain
 * @param kF Feedforward gain
 * @param kF_func Custom feedforward function (allows the feedforward to be calculated from multiple inputs)
 */
data class PIDConstants(val kP: Double, val kI: Double = 0.0, val kD: Double = 0.0, val kF: Double = 0.0, val kF_func: (DoubleArray) -> Double = { 0.0 }) {
    companion object {
        val DISABLE = PIDConstants(0.0, 0.0, 0.0, 0.0, { 0.0 })
    }
}