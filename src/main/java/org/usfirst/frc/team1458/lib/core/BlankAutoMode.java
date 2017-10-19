package org.usfirst.frc.team1458.lib.core;

/**
 * Blank autonomous mode
 *
 * @author asinghani
 */
public class BlankAutoMode implements AutoMode {
	@Override
	public String getName() {
		return "Blank Autonomous Mode";
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public void auto() {
		// Do nothing
	}
}