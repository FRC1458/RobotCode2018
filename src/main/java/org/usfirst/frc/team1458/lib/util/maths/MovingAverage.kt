package org.usfirst.frc.team1458.lib.util.maths

import java.util.ArrayList

class MovingAverage(internal var maxSize: Int) {

    internal var numbers = ArrayList<Double>()

    fun addNumber(newNumber: Double) {
        numbers.add(newNumber)
        if (numbers.size > maxSize) {
            numbers.removeAt(0)
        }
    }

    val average: Double
        get() {
            var total = 0.0

            for (number in numbers) {
                total += number
            }

            return total / numbers.size
        }

    val size: Int
        get() = numbers.size

    val isUnderMaxSize: Boolean
        get() = size < maxSize

    fun clear() {
        numbers.clear()
    }

}