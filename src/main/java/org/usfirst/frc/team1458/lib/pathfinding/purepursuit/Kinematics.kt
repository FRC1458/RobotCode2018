package org.usfirst.frc.team1458.lib.pathfinding.purepursuit

import org.usfirst.frc.team1458.lib.util.maths.kinematics.Delta2D
import org.usfirst.frc.team1458.lib.util.maths.kinematics.RigidTransform2D
import org.usfirst.frc.team1458.lib.util.maths.kinematics.Rotation2D

object Kinematics {

    // TODO: degrees/radians might be wrong

    fun forwardKinematics(leftWheelDelta: Double, rightWheelDelta: Double, trackScrubFactor: Double, trackWidth: Double): Delta2D {

        val deltaV = (rightWheelDelta - leftWheelDelta) / 2 * trackScrubFactor
        val deltaRotation = deltaV * 2 / trackWidth

        return forwardKinematics(leftWheelDelta, rightWheelDelta, deltaRotation)
    }

    fun forwardKinematics(leftWheelDelta: Double, rightWheelDelta: Double, deltaRotation: Double) =
            Delta2D((leftWheelDelta + rightWheelDelta) / 2, 0.0, deltaRotation)

    fun integrateForwardKinematics(currentPose: RigidTransform2D, leftWheelDelta: Double,
                                   rightWheelDelta: Double, currentHeading: Rotation2D): RigidTransform2D {

        val withGyro = forwardKinematics(leftWheelDelta, rightWheelDelta, currentPose.rotation.inverse.radians)
        return currentPose * RigidTransform2D.fromVelocity(withGyro)
    }


    fun inverseKinematics(velocity: Delta2D, wheelDiameter: Double, trackScrubFactor: Double): Pair<Double, Double> {
        if (Math.abs(velocity.deltaTheta) < 1E-6) {
            return Pair(velocity.deltaX, velocity.deltaY)
        }

        val deltaV = wheelDiameter * velocity.deltaTheta / (2 * trackScrubFactor)
        return Pair(velocity.deltaX - deltaV, velocity.deltaX + deltaV)
    }
}