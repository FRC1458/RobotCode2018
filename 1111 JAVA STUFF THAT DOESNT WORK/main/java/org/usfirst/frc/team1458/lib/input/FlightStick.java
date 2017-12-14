package org.usfirst.frc.team1458.lib.input;

/**
 * This is a single flight controller (pitch, roll, yaw/rotation) + possible throttle. Also includes several buttons & trigger.
 *
 * @author asinghani
 */
public interface FlightStick {

	/**
	 * Get the pitch axis of the joystick
	 * @return AnalogInput which returns values from joystick's pitch
	 */
	default AnalogInput getPitch() {
		return getAxis(1);
	}

	/**
	 * Get the roll axis of the joystick
	 * @return AnalogInput which returns values from joystick's roll
	 */
	default AnalogInput getRoll() {
		return getAxis(0);
	}

	/**
	 * Get the yaw axis of the joystick
	 * @return AnalogInput which returns values from joystick's yaw
	 */
	default AnalogInput getYaw() {
		return getAxis(3);
	}

	/**
	 * Get the throttle axis of the joystick
	 * @return AnalogInput which returns values from joystick's throttle
	 */
	default AnalogInput getThrottle() {
		return getAxis(2);
	}

	/**
	 * Get a reference to the trigger of the joystick
	 * @return the trigger of the joystick, represented as a switch
	 */
	default Switch getTrigger() {
		return getButton(1);
	}

	/**
	 * Get a direct reference to the axis specified by the parameter
	 * @param axis which axis to use
	 * @return an AnalogInput encompassing the desired axis
	 */
	AnalogInput getAxis(int axis);

	/**
	 * Get a direct reference to the button specified by the parameter
	 * @param button which button to use
	 * @return a Switch encompassing the desired button
	 */
	Switch getButton(int button);

	/**
	 * Get a reference to the flight stick's POV switch
	 * @return the pov switch (as a POV object)
	 */
	POV getPOV();
}
