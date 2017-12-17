package org.usfirst.frc.team1458.lib.sensor;

import java.util.function.DoubleSupplier;

/**
 * A sensor that returns an angle. The sensor can be zeroed to reset the position at which the angle is 0 degrees
 */
public interface AngleSensor extends Zeroable {

	/**
	 * Gets the angular displacement in continuous degrees.
	 *
	 * @return the positive or negative angular displacement in degrees
	 */
	double getAngle();

	/**
	 * Gets the heading of this sensor in degrees in the range [0, 360).
	 *
	 * @return the heading of this sensor
	 */
	default double getHeading() {
		double diff = getAngle() % 360;
		return diff >= 0 ? diff : 360 + diff;
	}

	/**
	 * Gets the angular displacement in continuous radians.
	 *
	 * @return the positive or negative angular displacement in radians
	 */
	default double getAngleRadians() {
		return Math.toRadians(getAngle());
	}

	/**
	 * Gets the rate of rotation in degrees/sec.
	 *
	 * @return the rate of rotation
	 */
	double getRate();

	/**
	 * Gets the rate of rotation in radians/sec.
	 *
	 * @return the rate of rotation
	 */
	default double getRateRadians() {
		return Math.toRadians(getRate());
	}

	/**
	 * Change the output so that the current angle is considered to be 0
	 *
	 * @return this object to allow chaining of methods; never null
	 */
	AngleSensor zero();

	/**
	 * Calculate the displacement required for this angle sensor to reach the target angle (may be positive or negative)
	 *
	 * @param targetAngle the target angle
	 * @param tolerance the tolerance within which the angle sensor is considered to have reached the target angle
	 * @return the displacement required for this angle sensor to reach the target angle (may be positive or negative),
	 * 		   or 0.0 if the two angles are already within the specified tolerance
	 */
	default double computeAngleChangeTo(double targetAngle, double tolerance) {
		double diff = targetAngle - this.getAngle();
		return Math.abs(diff) <= Math.abs(tolerance) ? 0.0 : diff;
	}

	default AngleSensor invert() {
		return AngleSensor.invert(this);
	}

	/**
	 * Create an angle sensor from the given function that returns the angle.
	 *
	 * @param angleFunction the function that returns the angle; may not be null
	 * @return the angle sensor
	 */
	static AngleSensor create(DoubleSupplier angleFunction) {
		return new AngleSensor() {
			private volatile double zero = 0;

			private double lastValue = angleFunction.getAsDouble();
			private long lastTime = System.currentTimeMillis() / 1000; // Seconds

			@Override
			public double getAngle() {
				lastValue = angleFunction.getAsDouble();
				lastTime = System.currentTimeMillis() / 1000;
				return lastValue - zero;
			}

			@Override
			public double getRate() {
				double rate = (angleFunction.getAsDouble() - lastValue) / (System.currentTimeMillis() - lastTime);

				lastValue = angleFunction.getAsDouble();
				lastTime = System.currentTimeMillis() / 1000;

				return rate;
			}

			@Override
			public AngleSensor zero() {
				lastValue = angleFunction.getAsDouble();
				lastTime = System.currentTimeMillis() / 1000;

				zero = lastValue;
				return this;
			}
		};
	}

	/**
	 * Inverts the specified {@link AngleSensor} so that negative angles become positive angles.
	 *
	 * @param sensor the {@link AngleSensor} to invert
	 * @return an {@link AngleSensor} that reads the opposite of the original sensor
	 */
	static AngleSensor invert(AngleSensor sensor) {
		return new AngleSensor() {
			@Override
			public double getAngle() {
				return -1 * sensor.getAngle();
			}

			@Override
			public double getRate() {
				return -1 * sensor.getRate();
			}

			@Override
			public AngleSensor zero() {
				sensor.zero();
				return this;
			}
		};
	}
}