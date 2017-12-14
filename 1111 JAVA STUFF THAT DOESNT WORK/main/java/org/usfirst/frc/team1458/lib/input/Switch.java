package org.usfirst.frc.team1458.lib.input;

import org.usfirst.frc.team1458.lib.util.GlobalTeleUpdate;
import org.usfirst.frc.team1458.lib.util.units.Angle;

import java.util.Random;

/**
 * Any sensor which has two states: on and off
 *
 * @author asinghani
 */
public interface Switch extends DigitalInput {

	Switch ALWAYS_ON = () -> true;
	Switch ALWAYS_OFF = () -> false;
	Switch RANDOM = new Random()::nextBoolean;

	/**
	 * Returns whether the switch is on or off
	 * @return true if switch is on, false if it is off
	 */
	boolean get();

	/**
	 * Get the value of this digital input
	 *
	 * @return the value of this sensor
	 */
	@Override
	default int getValue() {
		return get() ? 1 : 0;
	}

	/**
	 * Get a switch which is the inverted version of this switch
	 * @return the inverted switch
	 */
	default Switch invert() {
		return () -> !this.get();
	}

	/**
	 * Return a new switch that is only triggered when <em>all</em> switches are triggered.
	 * @param switches a list of switches
	 * @return the logical AND of the switches; never null
	 */
	static Switch and(final Switch... switches) {
		return () -> {
			boolean value = true;
			for(Switch s : switches) {
				value = value && s.get();
			}
			return value;
		};
	}

	/**
	 * Return a new switch that is only triggered when <em>any</em> switch is triggered.
	 * @param switches a list of switches which must all be triggered
	 * @return the logical OR of the switches; never null
	 */
	static Switch or(Switch... switches) {
		return () -> {
			for(Switch s : switches) {
				if(s.get()) {
					return true;
				}
			}
			return false;
		};
	}

	/**
	 * Returns a new switch which is only trigerred when this analog input's value is greater than 0.5
	 * @param analogInput the analog input to create a switch from
	 * @return a new switch created from the analog input; never null
	 */
	static Switch fromAnalog(AnalogInput analogInput) {
		return fromAnalog(analogInput, 0.5, true);
	}

	/**
	 * Returns a new switch which is only trigerred when this analog input's value is greater than the given threshold
	 * @param analogInput the analog input to create a switch from
	 * @param threshold the minimum value of the analog input for the switch to be triggered
	 * @return a new switch created from the analog input; never null
	 */
	static Switch fromAnalog(AnalogInput analogInput, double threshold) {
		return fromAnalog(analogInput, threshold, true);
	}

	/**
	 * Returns a new switch which is only trigerred when this analog input's value is greater/less than the given threshold
	 * @param analogInput the analog input to create a switch from
	 * @param threshold the minimum value of the analog input for the switch to be triggered
	 * @param greater this should be false if the threshold is negative (essentially inverts the value)
	 * @return a new switch created from the analog input; never null
	 */
	static Switch fromAnalog(AnalogInput analogInput, double threshold, boolean greater) {
		return greater ?
				() -> analogInput.getValue() > threshold :
				() -> analogInput.getValue() < threshold;
	}

	static Switch toggleSwitch(Switch sourceSwitch) {
		return new Switch() {
			boolean lastValue = false;
			boolean toggle = false;

			{
				// TODO FIX
				//GlobalTeleUpdate.registerHandler(this::teleUpdate);
			}

			@Override
			public boolean get() {
				return toggle;
			}

			public void teleUpdate() {
				if(lastValue != sourceSwitch.get() && sourceSwitch.get()) {
					toggle = !toggle;
				}
				lastValue = sourceSwitch.get();
			}
		};
	}

	static Switch fromPOV(POV pov, POV.Direction direction) {
		return () -> pov.getDirection() == direction;
	}

	static Switch fromPOV(POV pov, Angle angle) {
		return () -> pov.getAngle().equals(angle);
	}
}
