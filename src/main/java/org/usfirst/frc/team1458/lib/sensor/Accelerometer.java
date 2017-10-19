package org.usfirst.frc.team1458.lib.sensor;

/**
 * A single-axis accelerometer capable of sensing acceleration in one direction.
 *
 * @author asinghani
 */
public interface Accelerometer {

	/**
	 * Get the acceleration in g's
	 * @return the acceleration
	 */
	double getGs();

	/**
	 * Get the acceleration in meters per second squared
	 * @return the acceleration in m/s^2
	 */
	default double getAcceleration() {
		return getGs() * 9.80665;
	}
}