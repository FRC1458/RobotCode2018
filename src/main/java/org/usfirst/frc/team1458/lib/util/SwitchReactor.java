package org.usfirst.frc.team1458.lib.util;

import org.usfirst.frc.team1458.lib.input.Switch;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Allows registering a function to be called when a switch is triggered or untriggered
 *
 * @author asinghani
 */
public class SwitchReactor {

	static {
		GlobalTeleUpdate.registerHandler(SwitchReactor::teleUpdate);
	}

	private static Map<Switch, Runnable> onTriggeredCallbacks = new HashMap<>();
	private static Map<Switch, Runnable> whileTriggeredCallbacks = new HashMap<>();

	private static Map<Switch, Boolean> lastValue = new HashMap<>();

	/**
	 * Register a function to be called when the specified Switch is triggered.
	 *
	 * @param sourceSwitch the Switch
	 * @param function the function to execute when the switch is triggered
	 */
	public static void onTriggered(Switch sourceSwitch, Runnable function) {
		onTriggeredCallbacks.put(sourceSwitch, function);
		lastValue.put(sourceSwitch, sourceSwitch.get());
	}

	/**
	 * Register a function to be called when the specified Switch is untriggered.
	 *
	 * @param sourceSwitch the Switch
	 * @param function the function to execute when the switch is untriggered
	 */
	public static void onUntriggered(Switch sourceSwitch, Runnable function) {
		onTriggered(sourceSwitch.invert(), function);
	}

	/**
	 * Register a function to be called repeatedly while the specified Switch is triggered.
	 *
	 * @param sourceSwitch the Switch
	 * @param function the function to execute repeatedly while the switch is triggered
	 */
	public static void whileTriggered(Switch sourceSwitch, Runnable function) {
		whileTriggeredCallbacks.put(sourceSwitch, function);
		lastValue.put(sourceSwitch, sourceSwitch.get());
	}

	/**
	 * Register a function to be called repeatedly while the specified Switch is untriggered.
	 *
	 * @param sourceSwitch the Switch
	 * @param function the function to execute repeatedly while the switch is untriggered
	 */
	public static void whileUntriggered(Switch sourceSwitch, Runnable function) {
		whileTriggered(sourceSwitch.invert(), function);
	}

	private static void teleUpdate() {
		onTriggeredCallbacks.keySet().stream().forEach((theSwitch) -> {
			if(theSwitch.get() && !lastValue.get(theSwitch)) {
				onTriggeredCallbacks.get(theSwitch).run();
			}
		});

		whileTriggeredCallbacks.keySet().stream().forEach((theSwitch) -> {
			if(theSwitch.get()) {
				whileTriggeredCallbacks.get(theSwitch).run();
			}
		});

		Stream.concat(onTriggeredCallbacks.keySet().stream(), whileTriggeredCallbacks.keySet().stream())
				.forEach((theSwitch) -> lastValue.put(theSwitch, theSwitch.get()));
	}
}
