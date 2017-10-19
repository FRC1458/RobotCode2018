package org.usfirst.frc.team1458.lib.sensor.power;

/**
 * An object whose power consumption (voltage) can be monitored programmatically.
 *
 * @author asinghani
 */
public interface VoltageMeasurable {
	/**
	 * Get the voltage consumed by the device
	 * @return voltage used by the device in Volts
	 */
	double getVoltage();
}
