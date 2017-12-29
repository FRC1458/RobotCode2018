package org.usfirst.frc.team1458.lib.util

import org.usfirst.frc.team1458.lib.input.interfaces.Switch
import org.usfirst.frc.team1458.lib.util.flow.go
import org.usfirst.frc.team1458.lib.util.flow.periodic

object SwitchReactor {

    init {
        go { periodic(50) { update() } }
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
        lastValue.put(sourceSwitch, sourceSwitch.triggered)
    }

    /**
     * Register a function to be called when the specified Switch is untriggered.
     *
     * @param sourceSwitch the Switch
     * @param function the function to execute when the switch is untriggered
     */
    fun onUntriggered(sourceSwitch: Switch, function: () -> Unit) {
        onTriggered(sourceSwitch.inverted, function)
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
        whileTriggered(sourceSwitch.inverted, function)
    }

    private fun update() {
        onTriggeredCallbacks.keys.stream().forEach { theSwitch ->
            if (theSwitch.triggered && !(lastValue[theSwitch]!!)) {
                onTriggeredCallbacks[theSwitch]?.invoke()
            }
        }

        whileTriggeredCallbacks.keys.stream().forEach { theSwitch ->
            if (theSwitch.triggered) {
                whileTriggeredCallbacks[theSwitch]?.invoke()
            }
        }

        // Save previous values of switch (for detecting changes between iterations)
        onTriggeredCallbacks.keys.stream().forEach { theSwitch -> lastValue.put(theSwitch, theSwitch.triggered) }
    }
}