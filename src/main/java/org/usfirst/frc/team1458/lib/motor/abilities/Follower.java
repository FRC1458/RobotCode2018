package org.usfirst.frc.team1458.lib.motor.abilities;

/**
 * Used for a device which can follow other, similar devices. Often used for CAN-based motors and actuators.
 *
 * @author asinghani
 */
public interface Follower<T> {

	/**
	 * Start following another device
	 *
	 * @param toFollow the device to follow
	 * @return itself, to allow for method chaining
	 */
	Follower follow(T toFollow);

	/**
	 * Stops following the device it is following
	 * @return itself, to allow for method chaining
	 */
	Follower cancelFollow();
}
