package org.usfirst.frc.team1458.lib.util.maths.kinematics

import org.usfirst.frc.team1458.lib.util.maths.Extrapolable
import org.usfirst.frc.team1458.lib.util.maths.Interpolable

class RigidTransform2D(val translation : Translation2D, val rotation : Rotation2D) :
        Interpolable<RigidTransform2D>, Extrapolable<RigidTransform2D> {

    constructor() : this(Translation2D(), Rotation2D())

    constructor(translation: Translation2D) : this(translation, Rotation2D())
    constructor(rotation: Rotation2D) : this(Translation2D(), rotation)

    val inverse : RigidTransform2D
        get() = RigidTransform2D(translation.inverse.rotatedBy(rotation.inverse), rotation.inverse)

    val normal : RigidTransform2D
        get() = RigidTransform2D(translation, rotation.normal)

    operator fun times(other: RigidTransform2D) : RigidTransform2D {
        return RigidTransform2D(translation + other.translation.rotatedBy(other.rotation),
                rotation rotatedBy other.rotation)
    }

    /**
     * Interpolates {@code this} (which is time point 0) with {@code other} (which is time point 1) at time point {@code x} (which must be in the closed interval [0, 1])
     */
    override fun interpolate(other: RigidTransform2D, x: Double): RigidTransform2D {
        TODO("not implemented")
    }

    /**
     * Extrapolates a value from {@code this} (which is time point 0) and {@code other} (which is time point 1) at time point {@code x} (which should be greater than 1)
     */
    override fun extrapolate(other: RigidTransform2D, x: Double): RigidTransform2D {
        TODO("not implemented")
    }

    override fun toString(): String {
        return "T: $translation, R: $rotation"
    }
}
