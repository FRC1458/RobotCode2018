package org.usfirst.frc.team1458.lib.core

/**
 * Blank autonomous mode
 *
 * @author asinghani
 */
class BlankAutoMode : AutoMode {
    override val name: String
        get() = "Blank Autonomous Mode"

    override fun toString(): String {
        return name
    }

    override fun auto() {
        // Do nothing
    }
}