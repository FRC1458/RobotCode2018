package org.usfirst.frc.team1458.lib.input;

/**
 * This is an xbox controller.
 *
 * @author asinghani
 */
public interface XboxController extends Rumbleable {

	/**
	 * Get a direct reference to the left X axis
	 * @return an AnalogInput encompassing the left X axis
	 */
	default AnalogInput getLeftX() {
		return getAxis(0);
	}

	/**
	 * Get a direct reference to the left Y axis
	 * @return an AnalogInput encompassing the left Y axis
	 */
	default AnalogInput getLeftY() {
		return getAxis(1);
	}

	/**
	 * Get a direct reference to the left trigger axis
	 * @return an AnalogInput encompassing the left trigger axis
	 */
	default AnalogInput getLeftT() {
		return getAxis(2);
	}

	/**
	 * Get a direct reference to the right X axis
	 * @return an AnalogInput encompassing the right X axis
	 */
	default AnalogInput getRightX() {
		return getAxis(4);
	}

	/**
	 * Get a direct reference to the right Y axis
	 * @return an AnalogInput encompassing the right Y axis
	 */
	default AnalogInput getRightY() {
		return getAxis(5);
	}

	/**
	 * Get a direct reference to the right trigger axis
	 * @return an AnalogInput encompassing the right trigger axis
	 */
	default AnalogInput getRightT() {
		return getAxis(3);
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
	default Switch getButton(XboxButton button) {
		return getButton(button.val);
	}

	/**
	 * Get a direct reference to the button specified by the parameter
	 * @param button which button to use
	 * @return a Switch encompassing the desired button
	 */
	Switch getButton(int button);

	/**
	 * Get a reference to the controller's POV switch
	 * @return the pov switch (as a POV object)
	 */
	POV getPOV();

	enum XboxButton {
		A(1), B(2), X(3), Y(4), LBUMP(5), RBUMP(6), SELECT(7), START(8), LSTICK(9), RSTICK(10);
		public final int val;

		XboxButton(int i) {
			val = i;
		}
	}
}
