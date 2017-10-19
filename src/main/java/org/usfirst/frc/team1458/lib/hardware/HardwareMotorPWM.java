package org.usfirst.frc.team1458.lib.hardware;

import edu.wpi.first.wpilibj.PWMSpeedController;
import org.usfirst.frc.team1458.lib.motor.Motor;
import org.usfirst.frc.team1458.lib.util.annotation.Internal;

/**
 * This is a wrapper around any PWM motor.
 *
 * @author asinghani
 */
// TODO implement simulation
@Internal
public class HardwareMotorPWM implements Motor {
	private final PWMSpeedController motor;

	public HardwareMotorPWM(PWMSpeedController motor) {
		this.motor = motor;
	}

	@Override
	public Motor setSpeed(double speed) {
		motor.setSpeed(speed);
		return this;
	}

	@Override
	public double getSpeed() {
		return motor.getSpeed();
	}
}
