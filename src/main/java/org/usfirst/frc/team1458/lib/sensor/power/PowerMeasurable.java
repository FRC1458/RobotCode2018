package org.usfirst.frc.team1458.lib.sensor.power;

/**
 * Any object whose power consumption can be measured programmatically.
 *
 * @author asinghani
 */
public interface PowerMeasurable extends VoltageMeasurable, CurrentMeasurable {
	/**
	 * Get the power consumed by the device
	 * @return power used by the device in Watts
	 */
	default double getPower() {
		return getVoltage() * getCurrent();
	}

	/**
	 * Creates a PowerMeasurable object from two functions for voltage and current
	 * @param voltageMeasurable the function for getting the voltage
	 * @param currentMeasurable the function for getting the current
	 * @return a PowerMeasurable created from the two functions
	 */
	static PowerMeasurable create(VoltageMeasurable voltageMeasurable, CurrentMeasurable currentMeasurable) {
		return new PowerMeasurable() {
			@Override
			public double getCurrent() {
				return currentMeasurable.getCurrent();
			}

			@Override
			public double getVoltage() {
				return voltageMeasurable.getVoltage();
			}
		};
	}
}
