package org.usfirst.frc.team1458.lib.input.fake;

import org.usfirst.frc.team1458.lib.input.AnalogInput;

/**
 * Fake analog input whose value can be manually set
 *
 * @author asinghani
 */
public class FakeAnalogInput implements AnalogInput {
	private double value = 0;

	/**
	 * Get the value of this input
	 *
	 * @return the value, always in the range [-1.0, 1.0]
	 */
	@Override
	public double getValue() {
		return value;
	}

	/**
	 * Set the value of this input
	 *
	 * @param value the value to set this input to
	 * @return itself, to allow for chaining
	 */
	public FakeAnalogInput setValue(double value) {
		this.value = value;
		return this;
	}
}
