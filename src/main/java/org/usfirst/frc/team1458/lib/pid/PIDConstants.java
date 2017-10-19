package org.usfirst.frc.team1458.lib.pid;


/**
 * Container for PID constants
 * @author asinghani
 */
public class PIDConstants {
	/**
	 * Proportional
	 */
	public final double kP;

	/**
	 * Integral
	 */
	public final double kI;

	/**
	 * Derivative
	 */
	public final double kD;

	/**
	 * Feedforward
	 */
	public final double kF;

	public static final PIDConstants ZERO = new PIDConstants(0, 0, 0, 0);

	/**
	 * @param kP
	 */
	public PIDConstants(double kP) {
		this(kP, 0.0, 0.0, 0.0);
	}

	/**
	 * @param kP
	 * @param kI
	 * @param kD
	 */
	public PIDConstants(double kP, double kI, double kD) {
		this(kP, kI, kD, 0.0);
	}

	/**
	 * @param kP
	 * @param kI
	 * @param kD
	 * @param kF
	 */
	public PIDConstants(double kP, double kI, double kD, double kF) {
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
		this.kF = kF;
	}
}