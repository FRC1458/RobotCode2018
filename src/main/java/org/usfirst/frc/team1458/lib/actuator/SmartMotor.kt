package org.usfirst.frc.team1458.lib.actuator

import org.usfirst.frc.team1458.lib.pid.PIDConstants
import org.usfirst.frc.team1458.lib.sensor.interfaces.AngleSensor
import org.usfirst.frc.team1458.lib.sensor.interfaces.PowerMeasurable


interface SmartMotor : Motor, PowerMeasurable {

    val CANid : Int
        get

    val outputVoltage : Double
        get

    var currentLimit : Double
        set

    val connectedEncoder : AngleSensor
        get

    val isEncoderWorking : Boolean
        get

    /**
     * Temperature of the motor controller in degrees Celsius
     */
    val temperature : Double
        get

    var PIDconstants : PIDConstants
        set
        get

    var PIDsetpoint : Double
        set
        get

    var PIDenabled : Boolean
        set
        get

    var brakeMode : BrakeMode
        set
        get

    fun follow(other: SmartMotor)
    fun stopFollow()

    enum class BrakeMode(val brake: Boolean) {
        BRAKE(true), COAST(false)
    }

    companion object {
        // TODO: talon SRX create method, non-talon SRX create (with custom PID)
    }
}