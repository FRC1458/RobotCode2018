package org.usfirst.frc.team1458.lib.util.maths;

import java.util.ArrayList;

/**
 * A class holding helpful methods for maths-related things.
 *
 * @author mehnadnerd & asinghani
 */
public class TurtleMaths {
	/**
	 * Fit the double to a specified range. Equivalent to: (toFit > max ? max :
	 * toFit < min ? min: toFit)
	 *
	 * @param toFit
	 *            number to fit in range
	 * @param min
	 *            minimum value for toFit
	 * @param max
	 *            Maximum value for toFit
	 * @return
	 */
	public static double fitRange(double toFit, double min, double max) {
		if (toFit > max) {
			return max;
		}
		if (toFit < min) {
			return min;
		}
		return toFit;
	}

	/**
	 * Returns the absolute difference between the two numbers
	 *
	 * @param a
	 *            1st value
	 * @param b
	 *            2nd value
	 * @return The absolute difference of the two, equal to Math.abs(a-b)
	 */
	public static double absDiff(double a, double b) {
		return Math.abs(a - b);
	}

	/**
	 * Returns the bigger of the two double values. Equivalent to: (a > b ? a :
	 * b)
	 *
	 * @param a
	 * @param b
	 * @return The larger of a and b
	 */
	public static double biggerOf(double a, double b) {
		return (a > b ? a : b);
	}

	/**
	 * Returns the bigger of the two int values
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	public static int biggerOf(int a, int b) {
		return (a > b ? a : b);
	}

	/**
	 * Returns the smallest of two double values
	 *
	 * @param a
	 * @param b
	 * @return The smaller of the two values, or b if they are equal or such
	 */
	public static double smallerOf(double a, double b) {
		return (a < b ? a : b);
	}

	/**
	 * Returns the smaller of the two int values
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	public static int smallerOf(int a, int b) {
		return (a < b ? a : b);
	}

	/**
	 * Quickly shift numbers using RangeShifter
	 *
	 * @return The shifted value.
	 */
	public static double shift(double value, double minA, double maxA, double minB, double maxB) {
		return new RangeShifter(minA, maxA, minB, maxB).shift(value);
	}

	/**
	 * Rounds a double to a certain number of places past the decimal point
	 *
	 * @param toRound
	 *            The double to round
	 * @param decimalPlaces
	 *            The number of digits past the decimal point to keep, negative
	 *            numbers are supported.
	 *
	 * @return the rounded value
	 */
	public static double round(double toRound, int decimalPlaces) {
		toRound *= Math.pow(10, decimalPlaces);
		toRound = Math.round(Math.round(toRound));
		toRound /= Math.pow(10, decimalPlaces);
		return toRound;
	}

	/**
	 * Normalises a slope to between zero and ONE. Used in arcade chassis code
	 *
	 * @param slope
	 *            The slope to normalise
	 * @return The slope normalised to (0, 1], it will be absolute valued and
	 *         recipocaled as nessecary
	 */
	public static double normaliseSlope(double slope) {
		slope = Math.abs(slope);
		if (slope > 1) {
			slope = 1 / slope;
		}
		if (Double.isNaN(slope) || Double.isInfinite(slope)) {
			slope = 0;
		}
		return slope;
	}

	/**
	 * Calculates percent Error
	 *
	 * @param actual
	 *            The ideal or "correct" value
	 * @param measured
	 *            The measured value
	 * @return
	 */
	public static double percentError(double actual, double measured) {
		return TurtleMaths.absDiff(actual, measured) / actual;
	}

	/**
	 * Mathematical way for joystick deadband, if |input| < deadband, returns
	 * zero, otherwise returns input
	 *
	 * @param input
	 *            number to check against deadband
	 * @param deadbandRange
	 *            Range at which should round to zero
	 * @return Input or zero
	 */
	public static double deadband(double input, double deadbandRange) {
		if (Math.abs(input) < deadbandRange) {
			return 0;
		}

		return input;

	}

	/**
	 * Average a series of doubles
	 *
	 * @param num
	 *            The doubles to average
	 * @return The arithmetic mean of the numbers
	 */
	public static double avg(double... num) {
		double sum = 0;
		for (double d : num) {
			sum += d;
		}
		return sum / num.length;
	}

	public static double average(ArrayList<Double> num) {
		double sum = 0;
		for (double d : num) {
			sum += d;
		}
		return sum / num.size();
	}

	/**
	 * Scales a value quadratically while preserving signs, i.e 1 -> 1, .5 ->
	 * .25, and -1 -> -1
	 *
	 * @param toScale
	 *            The value to be scaled
	 * @return The scaled value
	 */
	public static double quadraticScale(double toScale) {
		return toScale * Math.abs(toScale);
	}

	/**
	 * Scales a value in a logistic step format, the exact function approximates
	 * a linear function but has logistic-like steps in every interval of 1/2
	 *
	 * Function is: y = x - sin(4pi*x)/4pi
	 *
	 * @param toScale
	 *            The number to be scaled
	 * @return The scaled number
	 */
	public static double logisticStepScale(double toScale) {
		return toScale - (Math.sin(4 * Math.PI * toScale) / (4 * Math.PI));
	}

	/**
	 * Convenience method that yields -1 if b is true, 1 otherwise
	 *
	 * @return
	 */
	public static int reverseBool(boolean isReversed) {
		return isReversed ? -1 : 1;
	}

	/**
	 * Hiding the constructor so cannot be initialised
	 */
	private TurtleMaths() {}
}