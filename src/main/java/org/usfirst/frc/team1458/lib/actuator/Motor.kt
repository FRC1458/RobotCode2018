package org.usfirst.frc.team1458.lib.actuator

interface Motor : Stoppable {
    var speed : Double
        set
        get

    override fun stop() {
        speed = 0.0
    }

    val inverted : Motor
        get() {
            val superMotor = this
            return create({ superMotor.speed = -1 * it }, { -1 * superMotor.speed })
        }

    operator fun plus(otherMotor: Motor): Motor {
        return compose(this, otherMotor)
    }

    operator fun unaryMinus(): Motor {
        return this.inverted
    }

    fun scale(function: (Double) -> Double): Motor {
        val superMotor = this
        return create { superMotor.speed = function(it) }
    }

    val direction: Direction
        get() {
            return when {
                speed > 0.001 -> Direction.FORWARD
                speed < -0.001 -> Direction.REVERSE
                else -> Direction.STOPPED
            }
        }

    enum class Direction constructor(var value: Double) {
        FORWARD(1.0), REVERSE(-1.0), STOPPED(0.0)
    }

    companion object {
        fun create(setSpeed: (Double) -> Unit) : Motor {
            var _speed: Double = 0.0
            return object : Motor {
                override var speed: Double
                    set(speed: Double) {
                        setSpeed(speed)
                        _speed = speed
                    }
                    get() = _speed
            }
        }

        fun create(setSpeed: (Double) -> Unit, getSpeed: () -> Double) : Motor {
            return object : Motor {
                override var speed: Double
                    set(speed: Double) = setSpeed(speed)
                    get() = getSpeed()
            }
        }

        /**
         * Combines the motors into a single set that can be controlled together.
         * @param motors
         * @return
         */
        fun compose(vararg motors: Motor): Motor {
            return create({
                for (motor in motors) {
                    motor.speed = it
                }
            }, { motors[0].speed })
        }

        fun Jaguar(channel: Int) : Motor {
            var motor = edu.wpi.first.wpilibj.Jaguar(channel)
            return create(motor::setSpeed, motor::getSpeed)
        }

        fun Spark(channel: Int) : Motor {
            var motor = edu.wpi.first.wpilibj.Spark(channel)
            return create(motor::setSpeed, motor::getSpeed)
        }

        fun TalonSR(channel: Int) : Motor {
            var motor = edu.wpi.first.wpilibj.Talon(channel)
            return create(motor::setSpeed, motor::getSpeed)
        }

        fun Victor888(channel: Int) : Motor {
            var motor = edu.wpi.first.wpilibj.Victor(channel)
            return create(motor::setSpeed, motor::getSpeed)
        }

        fun VictorSP(channel: Int) : Motor {
            var motor = edu.wpi.first.wpilibj.VictorSP(channel)
            return create(motor::setSpeed, motor::getSpeed)
        }

        /*fun PWMTalonSRX(channel: Int) : Motor {
            var motor = edu.wpi.first.wpilibj.TalonSRX(channel)
            return create(motor::setSpeed, motor::getSpeed)
        }*/
    }
}