package org.usfirst.frc.team1458.lib.input;

import org.usfirst.frc.team1458.lib.util.maths.InputFunction;

/**
 * Any data source which has a range of values in [-1.0, 1.0]
 *
 * @author asinghani
 */
public interface AnalogInput {
	/**
	 * Get the value of this input
	 * @return the value, always in the range [-1.0, 1.0]
	 */
	double getValue();
	/**
	 * Create a new range that inverts the values of this instance.
	 *
	 * @return the new inverted range; never null
	 */
	default AnalogInput invert() {
		return () -> this.getValue() * -1.0;
	}

	/**
	 * Create a new range that scales the values of this instance.
	 *
	 * @param scale the scaling factor
	 * @return the new scaled range; never null
	 */
	default AnalogInput scale(double scale) {
		return () -> this.getValue() * scale;
	}

	/**
	 * Create a new range that scales the values of this instance.
	 *
	 * @param function the function that determines the scaling factor
	 * @return the new scaled range; never null
	 */
	default AnalogInput scale(InputFunction function) {
		return () -> function.apply(this.getValue());
	}
}
