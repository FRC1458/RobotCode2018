package org.usfirst.frc.team1458.lib.util.units;

/**
 * Represents a class which can have mathematical operations applied to it.
 *
 * @author asinghani
 */
public interface Mathable<T> {
	/**
	 * Add the given value to this value.
	 * @param operand
	 * @return the result of the operation
	 */
	Mathable<T> plus(T operand);

	/**
	 * Subtract the given value from this value.
	 * @param operand
	 * @return the result of the operation
	 */
	Mathable<T> minus(T operand);

	/**
	 * Scale this value by the given scalar
	 * @param scalar
	 * @return the result of the operation
	 */
	Mathable<T> scale(double scalar);
}
