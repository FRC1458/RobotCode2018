package org.usfirst.frc.team1458.lib.sensor;

import org.usfirst.frc.team1458.lib.util.maths.Vector2;

/**
 * This is an accelerometer with two axes of freedom
 *
 * @author asinghani
 */
public interface TwoAxisAccelerometer {

	/**
	 * Get the X-axis accelerometer.
	 *
	 * @return the accelerometer for the X-axis; never null
	 */
	Accelerometer getXDirection();

	/**
	 * Get the Y-axis accelerometer.
	 *
	 * @return the accelerometer for the Y-axis; never null
	 */
	Accelerometer getYDirection();

	/**
	 * Get the accelerometer for the axis with the given index, where 0 is the X-axis and 1 is the Y-axis.
	 *
	 * @param axis the axis direction; must be either 0 or 1
	 * @return the accelerometer; never null
	 * @throws IllegalArgumentException if the axis is invalid
	 */
	default Accelerometer getDirection(int axis) {
		if (axis == 0) return getXDirection();
		if (axis == 1) return getYDirection();
		throw new IllegalArgumentException("Please enter a valid axis number (0 or 1)");
	}

	/**
	 * Get the instantaneous multidimensional acceleration values for all 2 axes in m/s^2.
	 *
	 * @return the acceleration values for 2 axes; never null
	 */
	default Vector2<Double> getAcceleration() {
		return new Vector2<>(getXDirection().getAcceleration(), getYDirection().getAcceleration());
	}

	/**
	 * Get the instantaneous multidimensional acceleration values for all 2 axes in m/s^2.
	 *
	 * @return the acceleration values for 2 axes; never null
	 */
	default Vector2<Double> getGs() {
		return new Vector2<>(getXDirection().getGs(), getYDirection().getGs());
	}

	/**
	 * Create a 2-axis accelerometer from the two individual accelerometers.
	 *
	 * @param xAxis the accelerometer for the X-axis; not null
	 * @param yAxis the accelerometer for the Y-axis; not null
	 * @return the 2-axis accelerometer; never null
	 */
	static TwoAxisAccelerometer create(Accelerometer xAxis, Accelerometer yAxis) {
		return new TwoAxisAccelerometer() {

			@Override
			public Accelerometer getXDirection() {
				return xAxis;
			}

			@Override
			public Accelerometer getYDirection() {
				return yAxis;
			}
		};
	}
}