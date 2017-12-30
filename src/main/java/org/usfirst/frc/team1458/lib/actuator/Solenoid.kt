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

    companion object {
        fun doubleSolenoid(PCMcanID: Int = 0, extendChannel: Int,
                           retractChannel: Int, initialState: Solenoid.State,
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
    }

    enum class State {
        EXTENDING, RETRACTING, STOPPED
    }

    enum class Position {
        EXTENDED, RETRACTED, UNKNOWN
    }
}