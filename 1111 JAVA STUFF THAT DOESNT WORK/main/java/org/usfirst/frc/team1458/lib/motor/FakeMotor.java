package org.usfirst.frc.team1458.lib.motor;

/**
 * Fake motor
 *
 * @author asinghani
 */
public class FakeMotor implements Motor {

	private double speed = 0.0;

	/**
	 * Instantiates FakeMotor
	 * @param args will be ignored by the FakeMotor
	 */
	public FakeMotor(Object... args) {

	}

	@Override
	public Motor setSpeed(double speed) {
		this.speed = speed;
		return this;
	}

	@Override
	public double getSpeed() {
		return speed;
	}
}
