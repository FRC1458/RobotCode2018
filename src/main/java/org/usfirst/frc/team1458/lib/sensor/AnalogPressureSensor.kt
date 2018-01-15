package org.usfirst.frc.team1458.lib.sensor

import edu.wpi.first.wpilibj.AnalogInput

/**
 * Analog pressure sensor for pneumatics such as https://www.andymark.com/product-p/am-3219.htm
 */
class AnalogPressureSensor(val channel: Int, val supplyVoltage: Double = 5.0) {
    val analogSensor = AnalogInput(channel)

    /**
     * Pressure, in PSI
     */
    val pressure : Double
        get() = 250 * (analogSensor.voltage / supplyVoltage) - 25

    val pressureBar : Double
        get() = 0.0689476 * pressure
}