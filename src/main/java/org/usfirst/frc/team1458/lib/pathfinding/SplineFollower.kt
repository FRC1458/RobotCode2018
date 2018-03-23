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


class SplineFollower(val left: Trajectory,
                     val right: Trajectory,
                     val drivetrain: TankDrive,
                     dt: Double? = null,
                     val gyro: AngleSensor? = null,
                     val gyro_kP: Double? = null,
                     name: String,
                     val stopFunc: () -> Boolean = { false },
                     val reversed: Boolean = false,
                     val everyIterationFunc: () -> Unit = { }
                    ) : BaseAutoMode() {

    constructor(leftCSV: String, rightCSV: String, drivetrain: TankDrive,
                dt: Double? = null, gyro: AngleSensor? = null, gyro_kP: Double? = null,
                name: String = "Spline" +
                        leftCSV.split("/").last().replace("leftTrajectory", ""),
                stopFunc: () -> Boolean = { false },
                reversed: Boolean = false,
                everyIterationFunc: () -> Unit = { }) :
            this(Pathfinder.readFromCSV(File(leftCSV)), Pathfinder.readFromCSV(File(rightCSV)),
                    drivetrain, dt, gyro, gyro_kP, name, stopFunc, reversed, everyIterationFunc)

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
                //println(angleError)

                val turnAdjustment = gyro_kP * angleError

                leftVel -= turnAdjustment
                rightVel += turnAdjustment
            }
            if(reversed){
                leftVel = leftVel * -1.0
                rightVel = rightVel * -1.0
            }

            drivetrain.setDriveVelocity(leftVel, rightVel)
            everyIterationFunc()


            //println("${rightTrajectory[index].heading}, ${leftTrajectory[index].heading}")

            //println("${leftTrajectory[index].velocity},${leftVel},${rightTrajectory[index].velocity},${rightVel},${rightTrajectory[index].heading},${gyro?.angle ?: 0.0},${drivetrain.leftMaster.connectedEncoder.rate * 1.11 / 360.0},${drivetrain.rightMaster.connectedEncoder.rate * 1.11 / 360.0},${drivetrain.leftMaster._talonInstance!!.getClosedLoopError(0)},${drivetrain.rightMaster._talonInstance!!.getClosedLoopError(0)}")

            SmartDashboard.putNumber("Left Velocity", left[index].velocity)
            SmartDashboard.putNumber("Right Velocity", right[index].velocity)

            SmartDashboard.putNumber("Real Left Velocity", drivetrain.leftMaster.connectedEncoder.rate * 1.11 / 360.0)
            SmartDashboard.putNumber("Real Right Velocity", drivetrain.rightMaster.connectedEncoder.rate * 1.11 / 360.0)

            SmartDashboard.putNumber("Right Error", drivetrain.rightMaster._talonInstance!!.getClosedLoopError(0).toDouble())
            SmartDashboard.putNumber("Left Error", drivetrain.leftMaster._talonInstance!!.getClosedLoopError(0).toDouble())


            // TODO remove to unbork


            /*System.out.print(" Left Desired Velocity="+ left[index].velocity)
            System.out.print(" Right Desired Velocity="+ right[index].velocity)

            System.out.print(" Left Real Velocity="+ drivetrain.leftEnc.velocity * 39.3700787402 / 12.0)
            System.out.print(" Right Real Velocity="+ drivetrain.rightEnc.velocity * 39.3700787402 / 12.0)

            System.out.print(" RightError="+ drivetrain.rightMaster._talonInstance!!.getClosedLoopError(0).toDouble())
            System.out.print(" LeftError="+ drivetrain.leftMaster._talonInstance!!.getClosedLoopError(0).toDouble())
            System.out.println("")*/

            delay(10)
            index = getIndex()
        }
        drivetrain.setRawDrive(0.0,0.0)

        //System.out.println(AutoDataLogger.getCSV(AutoDataLogger.keys.toTypedArray()))
    }
}