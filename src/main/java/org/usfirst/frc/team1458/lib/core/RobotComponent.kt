package org.usfirst.frc.team1458.lib.core

/**
 * A robot component is a wrapper for a hardware object
 */
interface RobotComponent {

    /**
     * Called during every cycle in teleop mode
     */
    fun teleUpdate()

    /**
     * Calling this method should stop all functions of this component
     */
    fun stop()
}
