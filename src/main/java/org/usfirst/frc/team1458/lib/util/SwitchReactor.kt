package org.usfirst.frc.team1458.lib.util

import org.usfirst.frc.team1458.lib.input.Switch

import java.util.HashMap
import java.util.stream.Stream

/**
 * Allows registering a function to be called when a switch is triggered or untriggered
 *
 * @author asinghani
 */
object SwitchReactor {

    init {
        GlobalTeleUpdate.registerHandler(SwitchReactor::teleUpdate)
    }

    private val onTriggeredCallbacks = HashMap<Switch, () -> Unit>()
    private val whileTriggeredCallbacks = HashMap<Switch, () -> Unit>()

    private val lastValue = HashMap<Switch, Boolean>()

    /**
     * Register a function to be called when the specified Switch is triggered.
     *
     * @param sourceSwitch the Switch
     * @param function the function to execute when the switch is triggered
     */
    fun onTriggered(sourceSwitch: Switch, function: () -> Unit) {
        onTriggeredCallbacks.put(sourceSwitch, function)
        lastValue.put(sourceSwitch, sourceSwitch.get())
    }

    /**
     * Register a function to be called when the specified Switch is untriggered.
     *
     * @param sourceSwitch the Switch
     * @param function the function to execute when the switch is untriggered
     */
    fun onUntriggered(sourceSwitch: Switch, function: () -> Unit) {
        onTriggered(sourceSwitch.invert(), function)
    }

    /**
     * Register a function to be called repeatedly while the specified Switch is triggered.
     *
     * @param sourceSwitch the Switch
     * @param function the function to execute repeatedly while the switch is triggered
     */
    fun whileTriggered(sourceSwitch: Switch, function: () -> Unit) {
        whileTriggeredCallbacks.put(sourceSwitch, function)
    }

    /**
     * Register a function to be called repeatedly while the specified Switch is untriggered.
     *
     * @param sourceSwitch the Switch
     * @param function the function to execute repeatedly while the switch is untriggered
     */
    fun whileUntriggered(sourceSwitch: Switch, function: () -> Unit) {
        whileTriggered(sourceSwitch.invert(), function)
    }

    private fun teleUpdate() {
        onTriggeredCallbacks.keys.stream().forEach { theSwitch ->
            if (theSwitch.get() && !(lastValue[theSwitch]!!)) {
                onTriggeredCallbacks[theSwitch]?.invoke()
            }
        }

        whileTriggeredCallbacks.keys.stream().forEach { theSwitch ->
            if (theSwitch.get()) {
                whileTriggeredCallbacks[theSwitch]?.invoke()
            }
        }

        // Save previous values of switch (for detecting changes between iterations)
        onTriggeredCallbacks.keys.stream().forEach { theSwitch -> lastValue.put(theSwitch, theSwitch.get()) }
    }
}
