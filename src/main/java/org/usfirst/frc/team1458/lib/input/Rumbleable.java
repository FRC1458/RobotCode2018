package org.usfirst.frc.team1458.lib.input;

/**
 * Any device which can be rumbled/vibrated (such as controllers)
 * @author asinghani
 */
public interface Rumbleable {
	/**
	 * Rumble the device with the given strength and time
	 * @param strength the strength of the rumble, between 0.0 and 1.0
	 * @param millis the time to rumble in milliseconds
	 */
	void rumble(float strength, long millis);
}