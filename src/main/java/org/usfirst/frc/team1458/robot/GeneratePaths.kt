package org.usfirst.frc.team1458.robot

import jaci.pathfinder.Trajectory
import jaci.pathfinder.Pathfinder
import jaci.pathfinder.Waypoint
import jaci.pathfinder.modifiers.TankModifier
import java.io.File


class GeneratePaths {
    companion object {
        // Highly importante config
        val DT = 0.05
        val VEL_LIMIT = 5.5
        val ACCEL_LIMIT = 2.5
        val JERK_LIMIT = 20.0

        val WHEELBASE = 1.958

        val SAVE_FOLDER = "splines/"

        val config = Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, DT, VEL_LIMIT, ACCEL_LIMIT, JERK_LIMIT)

        @JvmStatic
        fun main(args: Array<String>) {
            autonPath("testTORTUGADOR",
                    Waypoint(0.0, 0.0, 0.0),
                    Waypoint(5.0, 0.0, 0.0))


            autonPath("testMALADOR",
                    Waypoint(0.0, 1.0, 0.0),
                    Waypoint(5.0, 3.0, 0.0))
        }

        fun autonPath(name: String, vararg points: Waypoint) {
            val (left, right) = generateSpline(points.map { it.angle = Pathfinder.d2r(it.angle); it }.toTypedArray())
            val fileLeft = File(SAVE_FOLDER+name+"_left.csv")
            val fileRight = File(SAVE_FOLDER+name+"_right.csv")
            Pathfinder.writeToCSV(fileLeft, left)
            Pathfinder.writeToCSV(fileRight, right)
        }

        fun generateSpline(points: Array<Waypoint>): Pair<Trajectory, Trajectory> {
            val trajectory = Pathfinder.generate(points, config)
            val modifier = TankModifier(trajectory).modify(WHEELBASE)

            return Pair(modifier.leftTrajectory, modifier.rightTrajectory)
        }
    }
}