package org.usfirst.frc.team1458.lib.core;

/**
 * A robot component is a wrapper for a hardware object
 */
public interface RobotComponent {

	/**
	 * Called during every cycle in teleop mode
	 */
	void teleUpdate();

	/**
	 * Calling this method should stop all functions of this component
	 */
	void stop();
}
