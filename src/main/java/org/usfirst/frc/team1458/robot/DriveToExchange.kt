package org.usfirst.frc.team1458.robot

import jaci.pathfinder.Trajectory
import kotlinx.coroutines.experimental.Job
import org.usfirst.frc.team1458.lib.drive.TankDrive
import org.usfirst.frc.team1458.lib.sensor.interfaces.AngleSensor
import org.usfirst.frc.team1458.lib.util.maths.kinematics.Translation2D
import jaci.pathfinder.Pathfinder
import jaci.pathfinder.Waypoint
import jaci.pathfinder.modifiers.TankModifier
import org.usfirst.frc.team1458.lib.pathfinding.SplineFollower
import org.usfirst.frc.team1458.lib.util.flow.go


class DriveToExchange(val trajectoryConfig: Trajectory.Config,
                      val wheelbaseWidth: Double,
                      val drivetrain: TankDrive,
                      val gyro: AngleSensor?,
                      val gyro_kP: Double?,
                      val zedOffset: Translation2D, // Offset of ZED from center of robot
                      val cubeDistance: Double // Distance from center of robot to front of cube
                      ) {

    val coroutine : Job? = null

    fun driveToExchange(targetCenterCoordinates: Translation2D) {
        val relativePosition = targetCenterCoordinates + zedOffset
        val relativeRotation = relativePosition.direction

        val points = arrayOf(
                Waypoint(0.0, 0.0, 0.0),
                Waypoint(relativePosition.x, relativePosition.y, relativeRotation.radians)
        )
        val trajectory = Pathfinder.generate(points, trajectoryConfig)
        val tankModifier = TankModifier(trajectory).modify(wheelbaseWidth)

        val left = tankModifier.leftTrajectory
        val right = tankModifier.rightTrajectory

        val splineFollower = SplineFollower(
                left = left, right = right,
                drivetrain = drivetrain, gyro = gyro,
                gyro_kP = gyro_kP, name = "")

        go {

        }
    }
}