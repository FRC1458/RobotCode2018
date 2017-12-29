package org.usfirst.frc.team1458.lib.input.interfaces

interface PowerMeasurable {
    /**
     * Current draw of this device, in Amps
     */
    val currentDraw : Double
        get

    companion object {
        fun create(current: () -> Double) : PowerMeasurable {
            return object : PowerMeasurable {
                override val currentDraw: Double
                    get() = current()
            }
        }
    }
}