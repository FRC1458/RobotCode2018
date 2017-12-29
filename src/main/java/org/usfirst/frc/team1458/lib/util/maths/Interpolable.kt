package org.usfirst.frc.team1458.lib.util.maths

/**
 * Interpolable classes can be proportionally interpolated with another value
 */
interface Interpolable<T> {

    /**
     * Interpolates {@code this} (which is time point 0) with {@code other} (which is time point 1) at time point {@code x} (which must be in the closed interval [0, 1])
     */
    fun interpolate(other : T, x : Double = 0.5) : T
}