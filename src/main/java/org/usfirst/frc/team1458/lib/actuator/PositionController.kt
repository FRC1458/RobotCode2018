package org.usfirst.frc.team1458.lib.actuator

/**
 * Interface for a controller that can control the position of a subsystem or actuator
 */
interface PositionController {
    var targetPosition: Double
        set
        get

    val actualPosition: Double
        get

    fun update()
}