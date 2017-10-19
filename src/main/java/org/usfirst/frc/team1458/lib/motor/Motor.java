package org.usfirst.frc.team1458.lib.motor;

import org.usfirst.frc.team1458.lib.util.maths.InputFunction;

public interface Motor {
	/**
	 * Sets the speed of this motor
	 *
	 * @param speed the new speed as a double in the range [-1.0, 1.0]
	 * @return this object to allow chaining of methods
	 */
	Motor setSpeed(double speed);

	/**
	 * Gets the current speed of the motor
	 *
	 * @return the current speed of the motor as a double in the range [-1.0, 1.0]
	 */
	double getSpeed();

	/**
	 * Stops this motor. Same as calling `setSpeed(0)`.
	 */
	default void stop() {
		setSpeed(0.0);
	}

	/**
	 * Get an inverted (reversed) version of the motor.
	 * @return inverted motor
	 */
	default Motor invert() {
		Motor superMotor = this;
		return new Motor() {
			@Override
			public Motor setSpeed(double speed) {
				superMotor.setSpeed(-1 * speed);
				return this;
			}

			@Override
			public double getSpeed() {
				return -1 * superMotor.getSpeed();
			}
		};
	}

	/**
	 * Return a motor which is scaled with the given input function.
	 *
	 * @param function input function to scale motor values with
	 * @return motor scaled with the input function
	 */
	default Motor scale(InputFunction function) {
		Motor superMotor = this;
		return new Motor() {
			@Override
			public Motor setSpeed(double speed) {
				superMotor.setSpeed(function.apply(speed));
				return this;
			}

			@Override
			public double getSpeed() {
				return superMotor.getSpeed();
			}
		};
	}

	/**
	 * Combines the motors into a single set that can be controlled together.
	 * @param motors
	 * @return
	 */
	static Motor compose(Motor... motors) {
		return new Motor() {
			@Override
			public double getSpeed() {
				return motors[0].getSpeed();
			}

			@Override
			public Motor setSpeed(double speed) {
				for(Motor motor : motors) {
					motor.setSpeed(speed);
				}
				return this;
			}
		};
	}

	/**
	 * Gets the current {@link Direction} that this motor is moving toward.
	 *
	 * @return the {@link Direction} of this motor.
	 */
	default Direction getDirection() {
		double speed = getSpeed();
		if(speed > 0.001) {
			return Direction.FORWARD;
		} else if(speed < -0.001) {
			return Direction.REVERSE;
		} else {
			return Direction.STOPPED;
		}
	}

	enum Direction {
		FORWARD(1), REVERSE(-1), STOPPED(0);
		public int value;

		Direction(int value) {
			this.value = value;
		}
	}
}
