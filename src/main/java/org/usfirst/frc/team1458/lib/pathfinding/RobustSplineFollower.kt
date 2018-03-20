package org.usfirst.frc.team1458.lib.pathfinding

import jaci.pathfinder.Pathfinder
import jaci.pathfinder.Trajectory
import org.usfirst.frc.team1458.lib.core.BaseAutoMode
import org.usfirst.frc.team1458.lib.drive.TankDrive
import org.usfirst.frc.team1458.lib.sensor.interfaces.AngleSensor
import org.usfirst.frc.team1458.lib.util.flow.delay
import org.usfirst.frc.team1458.lib.util.flow.systemTimeMillis


class RobustSplineFollower(val leftTrajectory: Trajectory,
                           val rightTrajectory: Trajectory,
                           val drivetrain: TankDrive,
                           val gyro: AngleSensor? = null,
                           val gyro_kP: Double = 0.0,
                           val kP: Double,
                           val kD: Double = 0.0,
                           val kV: Double,
                           name: String,
                           val stopFunc: () -> Boolean = { false },
                           val reversed: Boolean = false,
                           val everyIterationFunc: () -> Unit = { }
                    ) : BaseAutoMode() {

    val dt: Double = leftTrajectory[0].dt * 1000.0
    override val name = name

    override fun auto() {
        val startTime = systemTimeMillis
        fun getIndex() = Math.floor((systemTimeMillis - startTime) / dt).toInt()

        var index : Int = getIndex()
        gyro?.zero()

        drivetrain.rightEnc.zero()
        drivetrain.leftEnc.zero()

        while((index) < (leftTrajectory.length()) && !stopFunc()) {
            var left = leftTrajectory[index].velocity
            var right = rightTrajectory[index].velocity

            if(gyro != null && gyro_kP != null) {
                val angleError = Pathfinder.boundHalfDegrees(- Pathfinder.boundHalfDegrees(gyro.heading) - Pathfinder.boundHalfDegrees(Pathfinder.r2d(leftTrajectory[index].heading)))

                val turnAdjustment = gyro_kP * angleError

                left -= turnAdjustment
                right += turnAdjustment
            }
            if(reversed){
                left *= -1.0
                right *= -1.0
            }

            drivetrain.setOpenLoopDrive(leftTrajectory, rightTrajectory)
            everyIterationFunc()

            delay(10)
            index = getIndex()
        }
        drivetrain.setRawDrive(0.0,0.0)
    }
}