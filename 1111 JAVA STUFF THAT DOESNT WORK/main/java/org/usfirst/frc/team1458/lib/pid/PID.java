package org.usfirst.frc.team1458.lib.pid;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.util.BaseSystemNotInitializedException;

/**
 * Full PID controller including feedforward
 *
 * @author asinghani
 */
public class PID {
	private final PIDConstants constants;

	private double target;
	private final double deadband;

	private double lastError = 0;
	private double derivative = 0;
	private double integral = 0;
	private double lastTime = -1;
	private boolean decay = false;

	/**
	 * Initializes PID controller with provided PID constants and initial target. Deadband defaults to 0.01 and decay is disabled.
	 * @param constants
	 * @param target
	 */
	public PID(PIDConstants constants, double target) {
		this.constants = constants;
		this.target = target;
		this.deadband = 0.01;
		this.decay = false;

		this.lastError = Double.POSITIVE_INFINITY;
	}

	/**
	 * Initializes PID controller with provided PID constants, initial target, and deadband. Decay is disabled.
	 * @param constants
	 * @param target
	 * @param deadband
	 */
	public PID(PIDConstants constants, double target, double deadband) {
		this.constants = constants;
		this.target = target;
		this.deadband = Math.abs(deadband);
		this.decay = false;

		this.lastError = Double.POSITIVE_INFINITY;
	}

	/**
	 * Initializes PID controller with provided PID constants, initial target, deadband, and decay option.
	 * @param constants
	 * @param target
	 * @param deadband
	 * @param decay
	 */
	public PID(PIDConstants constants, double target, double deadband, boolean decay) {
		this.constants = constants;
		this.target = target;
		this.deadband = Math.abs(deadband);
		this.decay = decay;

		this.lastError = Double.POSITIVE_INFINITY;
	}

	public void setTarget(double d) {
		this.target = d;
	}

	/**
	 * Get new value from PID
	 *
	 * @param value
	 */
	public double newValue(double value) {
		// Find error
		double error = target - value;

		// Find derivative
		derivative = (lastError - error) / (getTime() - lastTime);
		if (lastTime == -1) {
			lastTime = getTime();
			derivative = 0;
		}
		lastTime = getTime();

		double output = constants.kP * error + // P term
				constants.kI * integral - // I term
				constants.kD * derivative + // D term
				constants.kF * target; // F term (open-loop)

		//System.out.println(constants.kP * error + " " + constants.kI * sum + " " + constants.kD * derivative + " " + constants.kF * target + " " + output);

		// Find integral
		integral += error;

		if (decay) { // This decays the I term so it doesn't spiral out of control when the error is large
			integral = 0.75 * integral;
		}

		lastError = error;

		return output;
	}

	public boolean atTargetP() {
		return Math.abs(lastError) < deadband;
	}

	public boolean atTargetD() {
		return Math.abs(derivative) < (deadband / 15.0); // D term
	}
	public boolean atTarget() {
		return atTargetP() && atTargetD();
	}

	private double getTime() {
		double time = System.currentTimeMillis() / 1000.0;
		try {
			time = Timer.getFPGATimestamp();
		} catch (BaseSystemNotInitializedException e) {}

		return time;
	}
}