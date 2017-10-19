package org.usfirst.frc.team1458.lib.motor;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Immutable set of motors. Allows motors to be treated as ONE or several motors.
 *
 * @author asinghani
 */
public class MotorSet implements Motor {
	private final ArrayList<Motor> motorSet;
	private double speed = 0;

	public MotorSet(Motor... motors) {
		motorSet = new ArrayList<>(Arrays.asList(motors));
	}

	@Override
	public MotorSet setSpeed(double speed) {
		this.speed = speed;

		for(Motor motor : motorSet){
			motor.setSpeed(speed);
		}
		return this;
	}

	public MotorSet setSpeed(int motor, double val) {
		motorSet.get(motor).setSpeed(val);
		return this;
	}

	@Override
	public double getSpeed() {
		return speed;
	}

	public double getSpeed(int motor) {
		return motorSet.get(motor).getSpeed();
	}

	public Motor getMotor(int motor) {
		return motorSet.get(motor);
	}
}