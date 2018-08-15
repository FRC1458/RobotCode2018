package org.usfirst.frc.team1458.lib.pathfinding

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import jaci.pathfinder.Pathfinder
import jaci.pathfinder.Trajectory
import org.usfirst.frc.team1458.lib.core.BaseAutoMode
import org.usfirst.frc.team1458.lib.drive.TankDrive
import org.usfirst.frc.team1458.lib.sensor.interfaces.AngleSensor
import org.usfirst.frc.team1458.lib.util.flow.delay
import org.usfirst.frc.team1458.lib.util.flow.systemTimeMillis
import org.usfirst.frc.team1458.lib.util.maths.kinematics.Twist2D
import java.io.File
import java.lang.Math.toDegrees
import kotlin.math.abs


class NLRTSplineFollower(val mainTrajectory: Trajectory,
                         val drivetrain: TankDrive,
                         val gyro: AngleSensor? = null,
                         val gyro_kP: Double? = null,
                         override val name: String,
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

    override fun auto() {

        // TODO Make sure units n'stuff match up...
        val startTime = systemTimeMillis // Sets time start-point
        fun getIndex() = Math.floor((systemTimeMillis - startTime) / pathfinderDeltaTime).toInt() // Gets path segment index
        var trajectoryIndex : Int = getIndex() // Fetch how far down the path the robot is

        fun calculateRotationalVelcoity(radians: Double, seconds: Double): Double = radians / seconds
        var rotationalVelocity: Double = 0.0 // Sets rotational velocity to 0

        fun calculateAngleDifference(a: Double, b: Double): Pair<Double, Double> {
            val d = Math.abs(a - b) % 360
            var r = if (d > 180) 360 - d else d

            val sign = if (a - b in 0.0..180.0 || a - b <= -180 && a - b >= -360) 1 else -1
            r *= sign
            return Pair(d, r)
        }

        fun getRobotData(): Twist2D = Twist2D.IDENTITY // TODO See if this actually works
        var robotData: Twist2D = getRobotData()
        var oldRobotData: Twist2D

        // gyro?.zero() // Zero the gyroscope

        // While the path is still being followed AND stop function is false...
        while((trajectoryIndex) < (mainTrajectory.length()) && !stopFunc()) {
            // AutoDataLogger.currentIterationTimestamp = systemTimeMillis

            var desiredVelocity = mainTrajectory[trajectoryIndex].velocity

            // if(gyro != null && gyro_kP != null) {
            if(true) {
                var thetaError = Pathfinder.boundHalfDegrees(- Pathfinder.boundHalfDegrees(robotData.dTheta) - Pathfinder.boundHalfDegrees(Pathfinder.r2d(mainTrajectory[trajectoryIndex].heading))) // TODO This is a very confusing and long line - see why the heck we need it!
                //println(thetaError)

                val xError = mainTrajectory[trajectoryIndex].x - robotData.dx
                val yError = mainTrajectory[trajectoryIndex].y - robotData.dx

                if (kotlin.math.abs(thetaError) < 0.0001) {
                    thetaError = 0.0001
                }

                /* Replace ros_pathfinder with these vars, FOR REFERENCE
                val vFF = mainTrajectory[trajectoryIndex].velocity
                val wFF = rotationalVelocity // TODO check if needs abs value velocity or relative!
                val theta = mainTrajectory[trajectoryIndex].heading
                */

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

            oldRobotData = robotData
            delay(10)
            robotData = getRobotData() // TODO See if this ALSO actually works
            trajectoryIndex = getIndex()
        }
        drivetrain.setRawDrive(0.0,0.0)

        //System.out.println(AutoDataLogger.getCSV(AutoDataLogger.keys.toTypedArray()))
    }
}