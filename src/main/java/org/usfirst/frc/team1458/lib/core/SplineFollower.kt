package org.usfirst.frc.team1458.lib.core

import jaci.pathfinder.Pathfinder
import org.usfirst.frc.team1458.lib.drive.TankDrive
import org.usfirst.frc.team1458.lib.sensor.interfaces.AngleSensor
import org.usfirst.frc.team1458.lib.util.DataLogger
import org.usfirst.frc.team1458.lib.util.flow.delay
import org.usfirst.frc.team1458.lib.util.flow.systemTimeMillis
import java.io.File


class SplineFollower(leftCSV: String, rightCSV: String, val drivetrain: TankDrive, dt: Double? = null, val gyro: AngleSensor? = null,
                     name: String = "Spline_" + leftCSV.split("/").last().replace("left", "")) : BaseAutoMode() {
    val left = Pathfinder.readFromCSV(File(leftCSV))
    val right = Pathfinder.readFromCSV(File(rightCSV))

    val dt: Double = dt ?: (left[0].dt * 1000.0)

    override val name = name

    override fun auto() {
        val startTime = systemTimeMillis
        fun getIndex() = Math.floor((systemTimeMillis - startTime) / dt).toInt()

        while(getIndex() < (left.length())) {
            DataLogger.currentIterationTimestamp = systemTimeMillis
            val index = getIndex()
            // TODO: add gyro - https://github.com/JacisNonsense/Pathfinder/wiki/Pathfinder-for-FRC---Java#tank-drive
            drivetrain.setDriveVelocity(left[index].velocity, right[index].velocity)

            DataLogger.putValue("Left Position", left[index].position)
            DataLogger.putValue("Right Position", right[index].position)

            DataLogger.putValue("Left Velocity", left[index].velocity)
            DataLogger.putValue("Right Velocity", right[index].velocity)

            DataLogger.putValue("Left Accel", left[index].acceleration)
            DataLogger.putValue("Right Accel", right[index].acceleration)

            DataLogger.putValue("Left Jerk", left[index].jerk)
            DataLogger.putValue("Right Jerk", right[index].jerk)

            DataLogger.putValue("Heading", right[index].heading)

            DataLogger.putValue("Real Heading", gyro?.angle ?: 0.0)
            DataLogger.putValue("Real Left Velocity", drivetrain.leftMaster.connectedEncoder.rate)
            DataLogger.putValue("Real Right Velocity", drivetrain.rightMaster.connectedEncoder.rate)

            DataLogger.endTeleop()
            delay(1)
        }
        drivetrain.setRawDrive(0.0,0.0)

        System.out.println(DataLogger.getCSV(DataLogger.keys.toTypedArray()))
    }
}