package org.usfirst.frc.team1458.lib.actuator

import org.usfirst.frc.team1458.lib.sensor.interfaces.PowerMeasurable
import org.usfirst.frc.team1458.lib.input.interfaces.Switch

class Compressor(private val PCMcanID: Int = 0) : PowerMeasurable {
    private val compressor = edu.wpi.first.wpilibj.Compressor(PCMcanID)

    init {
        compressor.stop()
    }

    val running
        get() = compressor.enabled()

    val enabled
        get() = compressor.closedLoopControl

    /**
     * A switch which measures if the pressure is sufficient
     */
    val pressureSwitch
        get() = Switch.create { !compressor.pressureSwitchValue }

    /**
     * Current draw of this device, in Amps
     */
    override val currentDraw: Double
        get() = compressor.compressorCurrent

    /**
     * Starts the compressor (will automatically use closed-loop control to maintain desired pressure)
     */
    fun start() = compressor.start()

    /**
     * Stops the compressor
     */
    fun stop() = compressor.stop()



}