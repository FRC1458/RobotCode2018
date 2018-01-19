package org.usfirst.frc.team1458.lib.core

import jaci.pathfinder.Pathfinder
import org.usfirst.frc.team1458.lib.drive.TankDrive
import org.usfirst.frc.team1458.lib.sensor.interfaces.AngleSensor
import org.usfirst.frc.team1458.lib.util.AutoDataLogger
import org.usfirst.frc.team1458.lib.util.flow.delay
import org.usfirst.frc.team1458.lib.util.flow.systemTimeMillis
import java.io.File


class SplineFollower(leftCSV: String, rightCSV: String, val drivetrain: TankDrive, dt: Double? = null, val gyro: AngleSensor? = null, val gyro_kP: Double? = null,
                     name: String = "Spline_" + leftCSV.split("/").last().replace("left", "")) : BaseAutoMode() {
    val left = Pathfinder.readFromCSV(File(leftCSV))
    val right = Pathfinder.readFromCSV(File(rightCSV))

    val dt: Double = dt ?: (left[0].dt * 1000.0)

    override val name = name

    override fun auto() {
        val startTime = systemTimeMillis
        fun getIndex() = Math.floor((systemTimeMillis - startTime) / dt).toInt()

        while(getIndex() < (left.length())) {
            AutoDataLogger.currentIterationTimestamp = systemTimeMillis
            val index = getIndex()

            var leftVel = left[index].velocity
            var rightVel = right[index].velocity

            if(gyro != null && gyro_kP != null) {
                val angleError = Pathfinder.boundHalfDegrees(Pathfinder.r2d(left[index].heading) - gyro.heading)
                val turnAdjustment = gyro_kP * angleError

                leftVel -= turnAdjustment
                rightVel += turnAdjustment
            }

            drivetrain.setDriveVelocity(leftVel, rightVel)

            AutoDataLogger.putValue("Left Position", left[index].position)
            AutoDataLogger.putValue("Right Position", right[index].position)

            AutoDataLogger.putValue("Left Velocity", left[index].velocity)
            AutoDataLogger.putValue("Right Velocity", right[index].velocity)

            AutoDataLogger.putValue("Left Accel", left[index].acceleration)
            AutoDataLogger.putValue("Right Accel", right[index].acceleration)

            AutoDataLogger.putValue("Left Jerk", left[index].jerk)
            AutoDataLogger.putValue("Right Jerk", right[index].jerk)

            AutoDataLogger.putValue("Heading", right[index].heading)

            AutoDataLogger.putValue("Real Heading", gyro?.angle ?: 0.0)
            AutoDataLogger.putValue("Real Left Velocity", drivetrain.leftMaster.connectedEncoder.rate)
            AutoDataLogger.putValue("Real Right Velocity", drivetrain.rightMaster.connectedEncoder.rate)

            AutoDataLogger.endTeleop()
            delay(1)
        }
        drivetrain.setRawDrive(0.0,0.0)

        System.out.println(AutoDataLogger.getCSV(AutoDataLogger.keys.toTypedArray()))
    }
}