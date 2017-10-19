package org.usfirst.frc.team1458.lib.motor;

import org.usfirst.frc.team1458.lib.sensor.AngleSensor;

/**
 * Motor + Encoder which can maintain a constant velocity
 *
 * @author asinghani
 */
public class VelocityControlledMotor {
	private Motor motor;
	private AngleSensor encoder;

	public VelocityControlledMotor(Motor motor, AngleSensor encoder) {
		this.motor = motor;
		this.encoder = encoder;
	}

	
}
