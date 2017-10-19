package org.usfirst.frc.team1458.lib.util.maths;

/**
 * A class to help with scaling values
 */
public class RangeShifter implements InputFunction {
	private final double minA;
	private final double minB;
	private final double rngA;
	private final double rngB;

	/**
	 * Construct a new RangeShifter
	 *
	 * @param minA
	 *            Minimum point of first range
	 * @param maxA
	 *            Maximum point of first range
	 * @param minB
	 *            Minimum point of second range
	 * @param maxB
	 *            Maximum point of second range
	 */
	public RangeShifter(double minA, double maxA, double minB, double maxB) {
		this.minA = minA;
		this.rngA = maxA - minA;
		this.minB = minB;
		this.rngB = maxB - minB;
	}

	/**
	 * Use the RangeShifter to actually shift numbers.
	 *
	 * @param toShift
	 *            The number to shift
	 * @return The shifted value.
	 */
	public double shift(double toShift) {
		return minB + (rngB / rngA) * (toShift - minA);
	}

	@Override
	public double apply(double value) {
		return shift(value);
	}
}