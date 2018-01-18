package org.usfirst.frc.team1458.lib.core

import jaci.pathfinder.Pathfinder
import org.usfirst.frc.team1458.lib.drive.TankDrive
import org.usfirst.frc.team1458.lib.util.flow.delay
import org.usfirst.frc.team1458.lib.util.flow.systemTimeMillis
import java.io.File


class SplineFollower(leftCSV: String, rightCSV: String, val drivetrain: TankDrive, dt: Double? = null,
                     name: String = "Spline_" + leftCSV.split("/").last().replace("left", "")) : BaseAutoMode() {
    val left = Pathfinder.readFromCSV(File(leftCSV))
    val right = Pathfinder.readFromCSV(File(rightCSV))

    val dt: Double = dt ?: (left[0].dt * 1000.0)

    override val name = name

    override fun auto() {
        val startTime = systemTimeMillis
        fun getIndex() = Math.floor((systemTimeMillis - startTime) / dt).toInt()

        while(getIndex() < left.length()){
            val index = getIndex()
            // TODO: add gyro - https://github.com/JacisNonsense/Pathfinder/wiki/Pathfinder-for-FRC---Java#tank-drive
            drivetrain.setDriveVelocity(left[index].velocity / 10.0, right[index].velocity / 10.0)
            delay(1)
        }
        drivetrain.setRawDrive(0.0,0.0)
    }
}