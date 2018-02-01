package org.usfirst.frc.team1458.lib.actuator

import edu.wpi.first.wpilibj.DoubleSolenoid
import org.usfirst.frc.team1458.lib.input.interfaces.Switch

interface Solenoid {
    val state : State
        get

    val position : Position
        get

    fun extend()
    fun retract()

    operator fun plus(other: Solenoid): Solenoid {
        val thisThing = this
        return object : Solenoid {
            override val state: State
                get() = thisThing.state
            override val position: Position
                get() = thisThing.position

            override fun extend() {
                thisThing.extend()
                other.extend()
            }

            override fun retract() {
                thisThing.retract()
                other.retract()
            }

            override fun plus(other: Solenoid): Solenoid {
                return super.plus(other)
            }
        }
    }

    companion object {
        fun doubleSolenoid(PCMcanID: Int = 0, extendChannel: Int, retractChannel: Int) : Solenoid {
            val solenoid : DoubleSolenoid = DoubleSolenoid(PCMcanID, extendChannel, retractChannel)

            val sol = object : Solenoid {
                override val state: State
                    get() = when (solenoid.get()) {
                        DoubleSolenoid.Value.kReverse -> State.RETRACTING
                        DoubleSolenoid.Value.kForward -> State.EXTENDING
                        else -> State.STOPPED
                    }

                var _pos: Position = Position.RETRACTED

                override val position: Position
                    get() = _pos

                override fun extend() {
                    solenoid.set(DoubleSolenoid.Value.kForward)
                    _pos = Position.EXTENDED
                }

                override fun retract() {
                    solenoid.set(DoubleSolenoid.Value.kReverse)
                    _pos = Position.RETRACTED
                }
            }
            sol.retract()

            return sol
        }

        fun doubleSolenoid(PCMcanID: Int = 0, extendChannel: Int,
                           retractChannel: Int,
                           positionFunc: () -> Position = { Position.UNKNOWN }) : Solenoid {
            val solenoid : DoubleSolenoid = DoubleSolenoid(PCMcanID, extendChannel, retractChannel)

            return object : Solenoid {
                override val state: State
                    get() = when (solenoid.get()) {
                        DoubleSolenoid.Value.kReverse -> State.RETRACTING
                        DoubleSolenoid.Value.kForward -> State.EXTENDING
                        else -> State.STOPPED
                    }

                override val position: Position
                    get() = positionFunc()

                override fun extend() {
                    solenoid.set(DoubleSolenoid.Value.kForward)
                }

                override fun retract() {
                    solenoid.set(DoubleSolenoid.Value.kReverse)
                }
            }
        }

        fun doubleSolenoid(PCMcanID: Int = 0, extendChannel: Int,
                           retractChannel: Int,
                           extendedSwitch: Switch, retractedSwitch: Switch) : Solenoid {
            return doubleSolenoid(PCMcanID, extendChannel, retractChannel, {
                if (extendedSwitch.triggered && (!retractedSwitch.triggered)) Position.EXTENDED
                else if ((!extendedSwitch.triggered) && retractedSwitch.triggered) Position.RETRACTED
                else Position.UNKNOWN
            })
        }
    }

    enum class State {
        EXTENDING, RETRACTING, STOPPED
    }

    enum class Position {
        EXTENDED, RETRACTED, UNKNOWN
    }
}