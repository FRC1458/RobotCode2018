package org.usfirst.frc.team1458.lib.pathfinding

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import jaci.pathfinder.Pathfinder
import jaci.pathfinder.Trajectory
import org.usfirst.frc.team1458.lib.core.BaseAutoMode
import org.usfirst.frc.team1458.lib.drive.TankDrive
import org.usfirst.frc.team1458.lib.sensor.interfaces.AngleSensor
import org.usfirst.frc.team1458.lib.util.flow.delay
import org.usfirst.frc.team1458.lib.util.flow.systemTimeMillis
import java.io.File


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

    constructor(leftCSV: String,
                rightCSV: String,
                drivetrain: TankDrive,
                gyro: AngleSensor? = null,
                gyro_kP: Double = 0.0,
                kP: Double,
                kD: Double = 0.0,
                kV: Double,
                name: String = "Spline" +
                        leftCSV.split("/").last().replace("leftTrajectory", ""),
                stopFunc: () -> Boolean = { false },
                reversed: Boolean = false,
                everyIterationFunc: () -> Unit = { }) : this(Pathfinder.readFromCSV(File(leftCSV)),
            Pathfinder.readFromCSV(File(rightCSV)), drivetrain, gyro, gyro_kP, kP, kD, kV,
            name, stopFunc, reversed, everyIterationFunc)

    val dt: Double = leftTrajectory[0].dt * 1000.0
    override val name = name

    var lastLeftError = 0.0
    var lastRightError = 0.0

    override fun auto() {
        val startTime = systemTimeMillis
        fun getIndex() = Math.floor((systemTimeMillis - startTime) / dt).toInt()

        var index : Int = getIndex()
        gyro?.zero()

        drivetrain.rightEnc.zero()
        drivetrain.leftEnc.zero()


        while((index) < (leftTrajectory.length()) && !stopFunc()) {
            val leftError = leftTrajectory[index].position - drivetrain.leftEnc.distanceFeet
            val rightError = rightTrajectory[index].position - drivetrain.rightEnc.distanceFeet

            SmartDashboard.putNumber("LeftError", leftError)
            SmartDashboard.putNumber("RightError", rightError)

            SmartDashboard.putNumber("LeftD", ((leftError - lastLeftError) / dt))
            SmartDashboard.putNumber("RightD", ((rightError - lastRightError) / dt))


            var left = kP * leftError -
                    kD * ((leftError - lastLeftError) / 10.0) +
                    kV * leftTrajectory[index].velocity

            var right = kP * rightError -
                    kD * ((rightError - lastRightError) / 10.0) +
                    kV * rightTrajectory[index].velocity

            if(gyro != null && gyro_kP != null) {
                val angleError = Pathfinder.boundHalfDegrees(- Pathfinder.boundHalfDegrees(gyro.heading) - Pathfinder.boundHalfDegrees(Pathfinder.r2d(leftTrajectory[index].heading)))

                val turnAdjustment = gyro_kP * angleError

                left += turnAdjustment
                right -= turnAdjustment
            }
            if(reversed){
                left *= -1.0
                right *= -1.0
            }

            drivetrain.setOpenLoopDrive(left, right)
            everyIterationFunc()

            delay(10)
            lastLeftError = leftError
            lastRightError = rightError

            index = getIndex()
        }
        drivetrain.setRawDrive(0.0,0.0)
    }
}