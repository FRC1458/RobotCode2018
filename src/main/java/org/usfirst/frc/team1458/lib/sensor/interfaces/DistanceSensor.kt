package org.usfirst.frc.team1458.lib.sensor.interfaces

import org.usfirst.frc.team1458.lib.util.flow.go
import org.usfirst.frc.team1458.lib.util.flow.periodic
import org.usfirst.frc.team1458.lib.util.flow.systemTimeMillis
import org.usfirst.frc.team1458.lib.util.maths.MovingAverage
import edu.wpi.first.wpilibj.Ultrasonic
import edu.wpi.first.wpilibj.AnalogInput





interface DistanceSensor : Zeroable {

    val inverted : DistanceSensor
        get() = create({ -distanceMeters }, { -velocity })

    val distanceMeters : Double
        get

    val distanceCentimeters : Double
        get() = distanceMeters * 100

    val distanceInches : Double
        get() = distanceMeters * 39.3700787402

    val distanceFeet : Double
        get() = distanceInches / 12.0

    /**
     * Velocity in meters / second
     */
    val velocity : Double

    companion object {
        fun create(dist: () -> Double, vel: () -> Double) : DistanceSensor {
            return object : DistanceSensor {
                @Volatile private var zero = 0.0

                override val distanceMeters: Double
                    get() = dist() - zero

                override val velocity: Double
                    get() = vel()

                override fun zero() {
                    zero = dist()
                }
            }
        }

        fun create(samplesToAverage: Int = 10, dist: () -> Double): DistanceSensor {
            var sensor = object : DistanceSensor {
                @Volatile private var zero = 0.0
                @Volatile var _velocity = 0.0

                override val distanceMeters: Double
                    get() = dist() - zero

                override val velocity: Double
                    get() = _velocity

                override fun zero() {
                    zero = dist()
                }
            }

            var lastVal = 0.0
            var lastTime = systemTimeMillis - 1
            var movingAverage = MovingAverage(samplesToAverage)
            go { periodic (hz = 50) {
                movingAverage.addNumber((sensor.distanceMeters - lastVal) / ((systemTimeMillis - lastTime) / 1000))
                println("Sensor:"+(sensor.distanceMeters - lastVal))
                sensor._velocity = movingAverage.average
                lastVal = sensor.distanceMeters
                lastTime = systemTimeMillis
            } }

            return sensor
        }

        fun digitalUltrasonic(ping: Int, echo: Int) : DistanceSensor {
            val ultrasonic = Ultrasonic(ping, echo)
            ultrasonic.setAutomaticMode(true)
            return create { ultrasonic.rangeMM / 1000.0 }
        }

        fun analogUltrasonic(channel: Int, voltsToMeters: Double) : DistanceSensor {
            val ultrasonic = AnalogInput(channel)
            return DistanceSensor.create { ultrasonic.voltage * voltsToMeters }
        }

        fun distanceEncoder(angleSensor: AngleSensor, wheelCircumference: Double, gearing: Double = 1.0) : DistanceSensor {
            return DistanceSensor.create({ angleSensor.angle * wheelCircumference * gearing }, { angleSensor.rate * wheelCircumference * gearing })
        }

        fun distanceEncoder(portA: Int, portB: Int = portA + 1, ratio: Double = 1.0, wheelCircumference: Double, reverse: Boolean = false, gearing: Double = 1.0) : DistanceSensor {
            val angleSensor = AngleSensor.encoder(portA, portB, ratio, reverse)
            return DistanceSensor.distanceEncoder(angleSensor, wheelCircumference, gearing)
        }
    }
}