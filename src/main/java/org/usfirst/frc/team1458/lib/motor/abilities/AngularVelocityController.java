package org.usfirst.frc.team1458.lib.motor.abilities;

/**
 * Interface for controllers which can maintain a provided velocity
 *
 * @author asinghani
 */
public interface AngularVelocityController {
	/**
	 * Set the controller to maintain the given angular velocity
	 *
	 * @param velocity The angular velocity to maintain, in degrees/second
	 */
	void setVelocity(double velocity);
}
