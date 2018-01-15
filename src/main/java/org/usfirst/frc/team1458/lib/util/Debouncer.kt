package org.usfirst.frc.team1458.lib.util

import org.usfirst.frc.team1458.lib.util.flow.systemTimeMillis

class Debouncer(val minTimeMillis: Double) {
    var startTime: Double = 0.0
    var previousState: Boolean = false

    fun get(currentState: Boolean): Boolean {
        if (currentState && !previousState) {
            startTime = systemTimeMillis
        }
        previousState = currentState
        return currentState && (systemTimeMillis - startTime >= minTimeMillis)
    }
}