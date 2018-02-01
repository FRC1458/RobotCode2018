package org.usfirst.frc.team1458.lib.pathfinding

import jaci.pathfinder.Pathfinder
import jaci.pathfinder.Trajectory
import org.usfirst.frc.team1458.lib.core.BaseAutoMode
import org.usfirst.frc.team1458.lib.drive.TankDrive
import org.usfirst.frc.team1458.lib.sensor.interfaces.AngleSensor
import org.usfirst.frc.team1458.lib.util.AutoDataLogger
import org.usfirst.frc.team1458.lib.util.flow.delay
import org.usfirst.frc.team1458.lib.util.flow.systemTimeMillis
import java.io.File


class SplineFollower(val left: Trajectory,
                     val right: Trajectory,
                     val drivetrain: TankDrive,
                     dt: Double? = null,
                     val gyro: AngleSensor? = null,
                     val gyro_kP: Double? = null,
                     name: String,
                     val stopFunc: () -> Boolean = { false } // Stops path if this returns true
                     ) : BaseAutoMode() {

    constructor(leftCSV: String, rightCSV: String, drivetrain: TankDrive,
                dt: Double? = null, gyro: AngleSensor? = null, gyro_kP: Double? = null,
                name: String = "Spline" +
                        leftCSV.split("/").last().replace("left", ""),
                stopFunc: () -> Boolean = { false }) :
            this(Pathfinder.readFromCSV(File(leftCSV)), Pathfinder.readFromCSV(File(rightCSV)),
                    drivetrain, dt, gyro, gyro_kP, name, stopFunc)

    val dt: Double = dt ?: (left[0].dt * 1000.0)
    override val name = name

    override fun auto() {


        val startTime = systemTimeMillis
        fun getIndex() = Math.floor((systemTimeMillis - startTime) / dt).toInt()

        var index : Int = getIndex()
        gyro?.zero()
        while((index) < (left.length()) && !stopFunc()) {
            //AutoDataLogger.currentIterationTimestamp = systemTimeMillis

            var leftVel = left[index].velocity
            var rightVel = right[index].velocity

            if(gyro != null && gyro_kP != null) {
                val angleError = Pathfinder.boundHalfDegrees(- Pathfinder.boundHalfDegrees(gyro.heading) - Pathfinder.boundHalfDegrees(Pathfinder.r2d(left[index].heading)))
                println(angleError)

                val turnAdjustment = gyro_kP * angleError

                leftVel -= turnAdjustment
                rightVel += turnAdjustment
            }

            drivetrain.setDriveVelocity(leftVel, rightVel)

            //println("${right[index].heading}, ${left[index].heading}")

            //println("${left[index].velocity},${leftVel},${right[index].velocity},${rightVel},${right[index].heading},${gyro?.angle ?: 0.0},${drivetrain.leftMaster.connectedEncoder.rate * 1.11 / 360.0},${drivetrain.rightMaster.connectedEncoder.rate * 1.11 / 360.0},${drivetrain.leftMaster._talonInstance!!.getClosedLoopError(0)},${drivetrain.rightMaster._talonInstance!!.getClosedLoopError(0)}")

            /*AutoDataLogger.putValue("Left Position", left[index].position)
            AutoDataLogger.putValue("Right Position", right[index].position)

            AutoDataLogger.putValue("Left Velocity", left[index].velocity)
            AutoDataLogger.putValue("Right Velocity", right[index].velocity)

            AutoDataLogger.putValue("Left Accel", left[index].acceleration)
            AutoDataLogger.putValue("Right Accel", right[index].acceleration)

            AutoDataLogger.putValue("Left Jerk", left[index].jerk)
            AutoDataLogger.putValue("Right Jerk", right[index].jerk)

            AutoDataLogger.putValue("Heading", right[index].heading)

            AutoDataLogger.putValue("Real Heading", gyro?.angle ?: 0.0)
            AutoDataLogger.putValue("Real Left Velocity", drivetrain.leftMaster.connectedEncoder.rate * 1.11 / 360.0)
            AutoDataLogger.putValue("Real Right Velocity", drivetrain.rightMaster.connectedEncoder.rate * 1.11 / 360.0)

            AutoDataLogger.putValue("Right Error", drivetrain.rightMaster._talonInstance!!.getClosedLoopError(0))
            AutoDataLogger.putValue("Left Error", drivetrain.leftMaster._talonInstance!!.getClosedLoopError(0))

            AutoDataLogger.endTeleop()*/


            delay(10)
            index = getIndex()
        }
        drivetrain.setRawDrive(0.0,0.0)

        System.out.println(AutoDataLogger.getCSV(AutoDataLogger.keys.toTypedArray()))
    }
}