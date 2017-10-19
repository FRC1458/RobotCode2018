package org.usfirst.frc.team1458.lib.motor.abilities;

/**
 * This defines any actuator which can be set to either brake or coast mode.
 *
 * @author asinghani
 */
public interface BrakeModeSelectable {

	/**
	 * Change the brake mode of this actuator (normally break/coast)
	 *
	 * @param mode the mode which the actuator should be set to
	 * @return itself, to allow for method chaining
	 */
	BrakeModeSelectable setBrakeMode(BrakeMode mode);
}

