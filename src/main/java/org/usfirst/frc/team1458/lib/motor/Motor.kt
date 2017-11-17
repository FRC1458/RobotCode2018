package org.usfirst.frc.team1458.lib.motor

import org.usfirst.frc.team1458.lib.util.maths.InputFunction

interface Motor {
    /**
     * Sets the speed of this motor
     *
     * @param speed the new speed as a double in the range [-1.0, 1.0]
     */
    fun setSpeed(speed: Double)

    /**
     * Gets the current speed of the motor
     *
     * @return the current speed of the motor as a double in the range [-1.0, 1.0]
     */
    val speed: Double

    /**
     * Stops this motor. Same as calling `setSpeed(0)`.
     */
    fun stop() {
        setSpeed(0.0)
    }

    /**
     * Get an inverted (reversed) version of the motor.
     * @return inverted motor
     */
    fun invert(): Motor {
        val superMotor = this
        return object : Motor {
            override fun setSpeed(speed: Double) {
                superMotor.setSpeed(-1 * speed)
            }

            override val speed: Double
                get() = -1 * superMotor.speed
        }
    }

    operator fun plus(otherMotor: Motor): Motor {
        return Motor.compose(this, otherMotor);
    }

    /**
     * Return a motor which is scaled with the given input function.
     *
     * @param function input function to scale motor values with
     * @return motor scaled with the input function
     */
    fun scale(function: InputFunction): Motor {
        val superMotor = this
        return object : Motor {
            override fun setSpeed(speed: Double): Motor {
                superMotor.setSpeed(function.apply(speed))
                return this
            }

            override val speed: Double
                get() = superMotor.speed
        }
    }

    /**
     * Gets the current [Direction] that this motor is moving toward.
     *
     * @return the [Direction] of this motor.
     */
    val direction: Direction
        get() {
            val speed = speed
            return if (speed > 0.001) {
                Direction.FORWARD
            } else if (speed < -0.001) {
                Direction.REVERSE
            } else {
                Direction.STOPPED
            }
        }

    enum class Direction private constructor(var value: Int) {
        FORWARD(1), REVERSE(-1), STOPPED(0)
    }

    companion object {

        /**
         * Combines the motors into a single set that can be controlled together.
         * @param motors
         * @return
         */
        fun compose(vararg motors: Motor): Motor {
            return object : Motor {
                override val speed: Double
                    get() = motors[0].speed

                override fun setSpeed(speed: Double): Motor {
                    for (motor in motors) {
                        motor.setSpeed(speed)
                    }
                    return this
                }
            }
        }
    }
}
