package org.usfirst.frc.team1458.lib.actuator

import com.ctre.CANTalon
import org.usfirst.frc.team1458.lib.input.interfaces.PowerMeasurable


interface SmartMotor : Motor, PowerMeasurable {
    fun test() {
        val talon : CANTalon = CANTalon(1)
    }

    /**
     * Functions:
     * PowerMeasurable stuff
     * GetRealVoltage
     * SetCurrentLimit
     * EncPos/EncVel
     *
     * Follow
     *
     */
}