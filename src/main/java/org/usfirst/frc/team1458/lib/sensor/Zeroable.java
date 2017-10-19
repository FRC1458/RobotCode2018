package org.usfirst.frc.team1458.lib.sensor;

/**
 * A component that can be zeroed or reset.
 *
 * @author asinghani
 */
public interface Zeroable {
	/**
	 * Reset the component so the current value is zero.
	 * @return this object to allow chaining of methods; never null
	 */
	Zeroable zero();
}
