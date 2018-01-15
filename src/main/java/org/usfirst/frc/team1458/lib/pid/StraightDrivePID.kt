package org.usfirst.frc.team1458.lib.pid

import org.usfirst.frc.team1458.lib.sensor.interfaces.AngleSensor

/**
 * PID to drive the robot straight. Use only in tele-operated control.
 */
class StraightDrivePID(val constants: PIDConstants, val gyro: AngleSensor) {
    val pid: PID = PID(constants, 0.0)

    fun getValues(left: Double, right: Double): Pair<out Double, out Double> {
        var value = pid.update(gyro.angle, gyro.rate)

        return Pair(left - value, right + value)
    }
}