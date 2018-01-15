package org.usfirst.frc.team1458.lib.util.maths

/**
 * Extrapolable classes can be proportionally extrapolated from itself and another value
 */
interface Extrapolable<T> {

    /**
     * Extrapolates a value from {@code this} (which is time point 0) and {@code other} (which is time point 1) at time point {@code x} (which should be greater than 1)
     */
    fun extrapolate(other : T, x : Double = 2.0) : T
}