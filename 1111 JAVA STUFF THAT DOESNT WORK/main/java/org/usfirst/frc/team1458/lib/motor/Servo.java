package org.usfirst.frc.team1458.lib.motor;

/**
 * This is a motor which can rotate to a specific angle.
 *
 * @author asinghani
 */
public interface Servo {
	void setAngle(double angle);

	double getAngle();
}
