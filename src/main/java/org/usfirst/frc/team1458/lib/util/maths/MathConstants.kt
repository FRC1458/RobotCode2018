package org.usfirst.frc.team1458.lib.util.maths

val PI : Double = Math.PI

val EPSILON = 1E-8
val INV_EPSILON = 1E8


/**
 * Compares two numbers while accounting for rounding errors / precision
 */
infix fun Double.eq(other : Double) : Boolean = Math.abs(this - other) < EPSILON