package org.usfirst.frc.team1458.lib.input;

import org.usfirst.frc.team1458.lib.util.maths.TurtwigMaths;

/**
 * Any sensor which has multiple digital states (represented as integers)
 *
 * @author asinghani
 */
public interface DigitalInput {
	/**
	 * Get the value of this digital input
	 * @return the value of this sensor
	 */
	int getValue();

	/**
	 * Creates a digital input mapped from the given analog input
	 * @param analogInput the analog input to map values from
	 * @param min the minimum value that the digital input should return
	 * @param max the maximum value that the digital input should return
	 * @return a new digital input based on the existing analog input
	 */
	static DigitalInput fromAnalog(AnalogInput analogInput, double min, double max) {
		return () -> (int) TurtwigMaths.shift(analogInput.getValue(), -1.0, 1.0, min, max);
	}

	/**
	 * Create a DigitalInput which can be incremented or decremented using two buttons
	 * @param up the Switch which will increment this value when pressed
	 * @param down the Switch which will decrement this value when pressed
	 * @param min the minimum value of this DigitalInput; this is also the default value
	 * @param max the maximum value of this DigitalInput
	 * @return a new DigitalInput controlled by the two buttons
	 */
	static DigitalInput fromUpDown(Switch up, Switch down, int min, int max) {
		return fromUpDown(up, down, min, max, min);
	}

	/**
	 * Create a DigitalInput which can be incremented or decremented using two buttons
	 * @param up the Switch which will increment this value when pressed
	 * @param down the Switch which will decrement this value when pressed
	 * @param min the minimum value of this DigitalInput
	 * @param max the maximum value of this DigitalInput
	 * @param defaultValue the default value for this DigitalInput
	 * @return a new DigitalInput controlled by the two buttons
	 */
	static DigitalInput fromUpDown(Switch up, Switch down, int min, int max, int defaultValue) {
		return new DigitalInput() {

			{
				// TODO FIX
				//SwitchReactor.onTriggered(up, this::onUp);
				//SwitchReactor.onTriggered(down, this::onDown);
			}

			int value = defaultValue;

			@Override
			public int getValue() {
				return value;
			}

			public void onUp() {
				value++;
				if(value > max) {
					value = max;
				}
			}

			public void onDown() {
				value--;
				if(value < min) {
					value = min;
				}
			}
		};
	}
}
