package org.usfirst.frc.team1458.lib.util

import java.util.ArrayList
import java.util.function.Consumer

/**
 * Used when library classes require direct access to teleUpdate
 *
 * @author asinghani
 */
object GlobalTeleUpdate {
    private val handlers = ArrayList<() -> Unit>()

    fun teleUpdate() {
        handlers.stream().forEach { it.invoke() }
    }

    fun registerHandler(handler: () -> Unit) {
        handlers.add(handler)
    }
}
