package org.usfirst.frc.team1458.lib.sensor.interfaces

import edu.wpi.first.wpilibj.*
import edu.wpi.first.wpilibj.interfaces.Accelerometer


interface Accelerometer {
    /**
     * Acceleration on x-axis in m/s^2
     */
    val x : Double
        get

    /**
     * Acceleration on y-axis in m/s^2
     */
    val y : Double
        get

    /**
     * Acceleration on z-axis in m/s^2
     */
    val z : Double
        get

    companion object {
        fun create(xSource: () -> Double, ySource: () -> Double, zSource: () -> Double) : org.usfirst.frc.team1458.lib.sensor.interfaces.Accelerometer {
            return object : org.usfirst.frc.team1458.lib.sensor.interfaces.Accelerometer {
                override val x: Double
                    get() = xSource()
                override val y: Double
                    get() = ySource()
                override val z: Double
                    get() = zSource()
            }
        }

        fun ADXL345(port: I2C.Port, range: Accelerometer.Range = Accelerometer.Range.k8G) : org.usfirst.frc.team1458.lib.sensor.interfaces.Accelerometer {
            var accelerometer = ADXL345_I2C(port, range)
            return create(accelerometer::getX, accelerometer::getY, accelerometer::getZ)
        }

        fun ADXL345(port: SPI.Port, range: Accelerometer.Range = Accelerometer.Range.k8G) : org.usfirst.frc.team1458.lib.sensor.interfaces.Accelerometer {
            var accelerometer = ADXL345_SPI(port, range)
            return create(accelerometer::getX, accelerometer::getY, accelerometer::getZ)
        }

        fun builtIn() : org.usfirst.frc.team1458.lib.sensor.interfaces.Accelerometer {
            var accelerometer = BuiltInAccelerometer()
            return create(accelerometer::getX, accelerometer::getY, accelerometer::getZ)
        }
    }
}