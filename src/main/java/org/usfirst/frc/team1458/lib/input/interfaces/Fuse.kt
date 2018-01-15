package org.usfirst.frc.team1458.lib.input.interfaces

import java.util.*
import kotlin.concurrent.schedule

interface Fuse : Switch {
    fun trigger()
    fun reset()

    companion object {
        fun create() : Fuse {
            return object : Fuse {
                var _triggered = false

                override val triggered: Boolean
                    get() =_triggered

                override fun trigger() {
                    _triggered = true
                }

                override fun reset() {
                    _triggered = false
                }
            }
        }

        /**
         * Fuse which resets automatically after the specified number of milliseconds
         */
        fun autoResetting(delay: Double) : Fuse {
            return object : Fuse {
                var _triggered = false

                override val triggered: Boolean
                    get() =_triggered

                override fun trigger() {
                    _triggered = true
                    Timer().schedule(delay.toLong()) { reset() }
                }

                override fun reset() {
                    _triggered = false
                }
            }
        }
    }
}