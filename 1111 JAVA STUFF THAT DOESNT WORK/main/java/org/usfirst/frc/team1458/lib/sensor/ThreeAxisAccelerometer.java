package org.usfirst.frc.team1458.lib.sensor;

import org.usfirst.frc.team1458.lib.util.maths.Vector3;

/**
 * This is an accelerometer with three axes of freedom
 *
 * @author asinghani
 */
public interface ThreeAxisAccelerometer extends TwoAxisAccelerometer {

	/**
	 * Get the Z-axis accelerometer.
	 *
	 * @return the accelerometer for the Z-axis; never null
	 */
	Accelerometer getZDirection();

	/**
	 * Get the accelerometer for the axis with the given index, where 0 is the X-axis, 1 is the Y-axis, and 2 is the Z-axis
	 *
	 * @param axis the axis direction; must be either 0, 1, or 2
	 * @return the accelerometer; never null
	 * @throws IllegalArgumentException if the axis is invalid
	 */
	default Accelerometer getDirection(int axis) {
		if (axis == 0) return getXDirection();
		if (axis == 1) return getYDirection();
		if (axis == 2) return getZDirection();
		throw new IllegalArgumentException("Please enter a valid axis number (0, 1, or 2)");
	}

	/**
	 * Get the instantaneous multidimensional acceleration values for all 2 axes in m/s^2.
	 *
	 * @return the acceleration values for 2 axes; never null
	 */
	default Vector3<Double> getAcceleration() {
		return new Vector3<>(getXDirection().getAcceleration(), getYDirection().getAcceleration(), getZDirection().getAcceleration());
	}

	/**
	 * Get the instantaneous multidimensional acceleration values for all 2 axes in m/s^2.
	 *
	 * @return the acceleration values for 2 axes; never null
	 */
	default Vector3<Double> getGs() {
		return new Vector3<>(getXDirection().getGs(), getYDirection().getGs(), getZDirection().getGs());
	}

	/**
	 * Create a 3-axis accelerometer from the three individual accelerometers.
	 *
	 * @param xAxis the accelerometer for the X-axis; not null
	 * @param yAxis the accelerometer for the Y-axis; not null
	 * @param zAxis the accelerometer for the Z-axis; not null
	 * @return the 3-axis accelerometer; never null
	 */
	static ThreeAxisAccelerometer create(Accelerometer xAxis, Accelerometer yAxis, Accelerometer zAxis) {
		return new ThreeAxisAccelerometer() {

			@Override
			public Accelerometer getXDirection() {
				return xAxis;
			}

			@Override
			public Accelerometer getYDirection() {
				return yAxis;
			}

			@Override
			public Accelerometer getZDirection() {
				return zAxis;
			}
		};
	}

	/**
	 * Create a 3-axis accelerometer from a 2-axis accelerometer and a 1-axis accelerometer.
	 *
	 * @param accel the accelerometer for the X and Y axes; not null
	 * @param zAxis the accelerometer for the Z-axis; not null
	 * @return the 3-axis accelerometer; never null
	 */
	static ThreeAxisAccelerometer create(TwoAxisAccelerometer accel, Accelerometer zAxis) {
		return null; // TODO NOT MAKE THIS SUPER BROKEN  //compose(accel.getXDirection(), accel.getYDirection(), zAxis);
	}
}