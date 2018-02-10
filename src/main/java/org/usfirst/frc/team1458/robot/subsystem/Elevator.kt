package org.usfirst.frc.team1458.robot.subsystem

import org.usfirst.frc.team1458.lib.actuator.PositionController
import org.usfirst.frc.team1458.lib.input.interfaces.Switch

/**
 * Controller for the elevator on the robot
 * @author asinghani
 */
class Elevator(val positionController: PositionController, val topLimit: Switch, val bottomLimit: Switch) {

    /**
     * Tele-operated update loop (position measured as height from lowest position, in inches)
     */
    fun update(position: Double) {

    }

    fun update(position: Position) = update(position.height)

    enum class Position(val height: Double) {
        GROUND(0.0), EXCHANGE(5.0), SWITCH(24.0), SCALE(72.0)
    }
}