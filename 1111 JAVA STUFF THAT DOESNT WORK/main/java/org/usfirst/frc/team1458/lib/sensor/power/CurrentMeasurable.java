package org.usfirst.frc.team1458.lib.sensor.power;

/**
 * An object whose current consumption can be monitored programmatically.
 *
 * @author asinghani
 */
public interface CurrentMeasurable {
	/**
	 * Get the current consumed by the device
	 * @return current used by the device in Amps
	 */
	double getCurrent();
}
