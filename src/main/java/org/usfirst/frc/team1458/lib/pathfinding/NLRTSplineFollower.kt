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


class NLRTSplineFollower(val mainTrajectory: Trajectory,
                     val drivetrain: TankDrive,
                     val gyro: AngleSensor? = null,
                     val gyro_kP: Double? = null,
                     name: String,
                     val stopFunc: () -> Boolean = { false },
                     val reversed: Boolean = false,
                     val everyIterationFunc: () -> Unit = { }
                    ) : BaseAutoMode() {

    constructor(mainTrajectoryCSV: String, drivetrain: TankDrive, gyro: AngleSensor? = null, gyro_kP: Double? = null,
                name: String = "Spline" +
                        mainTrajectoryCSV.split("/").last().replace("leftTrajectory", ""),
                stopFunc: () -> Boolean = { false },
                reversed: Boolean = false,
                everyIterationFunc: () -> Unit = { }) :
            this(Pathfinder.readFromCSV(File(mainTrajectoryCSV)), drivetrain, gyro,
                    gyro_kP, name, stopFunc, reversed, everyIterationFunc)

    val pathfinderDeltaTime: Double = (mainTrajectory[0].dt * 1000.0) // Delta Time: Time between pathfinder points
    override val name = name

    override fun auto() {
        val startTime = systemTimeMillis // Sets time start-point
        fun getIndex() = Math.floor((systemTimeMillis - startTime) / pathfinderDeltaTime).toInt()

        var trajectoryIndex : Int = getIndex() // Fetch how far down the path the robot is
        gyro?.zero() // Zero the gyroscope

        // While the path is still being followed AND stop function is false...
        while((trajectoryIndex) < (mainTrajectory.length()) && !stopFunc()) {
            //AutoDataLogger.currentIterationTimestamp = systemTimeMillis

            var desiredVelocity = mainTrajectory[trajectoryIndex].velocity

            if(gyro != null && gyro_kP != null) {
                var thetaError = Pathfinder.boundHalfDegrees(- Pathfinder.boundHalfDegrees(gyro.heading) - Pathfinder.boundHalfDegrees(Pathfinder.r2d(mainTrajectory[index].heading)))
                //println(angleError)

                val xError = mainTrajectory[trajectoryIndex].x - robotX
                val yError = mainTrajectory[trajectoryIndex].y - robotY

                if (kotlin.math.abs(thetaError) < 0.0001) {
                    thetaError = 0.0001
                }

                val vFF = mainTrajectory[trajectoryIndex].velocity
                val wFF = mainTrajectory[trajectoryIndex].angular_velocity
                val theta = mainTrajectory[trajectoryIndex].heading

                val turnAdjustment = gyro_kP * angleError

                mainVel -= turnAdjustment
                rightVel += turnAdjustment
            }
            if(reversed){
                leftVel = leftVel * -1.0
                rightVel = rightVel * -1.0
            }

            if(true) {
                drivetrain.setDriveVelocity(mainVel, mainVel) // Needs left and right velocity
            } else {
                drivetrain.setOpenLoopDrive(leftVel / (drivetrain.closedLoopScaling ?: 1000000.0),
                        rightVel / (drivetrain.closedLoopScaling ?: 1000000.0))
            }
            everyIterationFunc()


            //println("${rightTrajectory[index].heading}, ${leftTrajectory[index].heading}")

            //println("${leftTrajectory[index].velocity},${leftVel},${rightTrajectory[index].velocity},${rightVel},${rightTrajectory[index].heading},${gyro?.angle ?: 0.0},${drivetrain.leftMaster.connectedEncoder.rate * 1.11 / 360.0},${drivetrain.rightMaster.connectedEncoder.rate * 1.11 / 360.0},${drivetrain.leftMaster._talonInstance!!.getClosedLoopError(0)},${drivetrain.rightMaster._talonInstance!!.getClosedLoopError(0)}")

            SmartDashboard.putNumber("Main Velocity", mainTrajectory[trajectoryIndex].velocity)

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
            trajectoryIndex = getIndex()
        }
        drivetrain.setRawDrive(0.0,0.0)

        //System.out.println(AutoDataLogger.getCSV(AutoDataLogger.keys.toTypedArray()))
    }
}