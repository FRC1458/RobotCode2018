package org.usfirst.frc.team1458.robot

import org.usfirst.frc.team1458.lib.actuator.SmartMotor
import org.usfirst.frc.team1458.lib.pid.PID
import org.usfirst.frc.team1458.lib.pid.PIDConstants

class Lift(val liftMotor: SmartMotor) {
    val constants = PIDConstants(0.0,0.0,0.0,0.0, { 0.0 })
    fun setLift(tar: Double){
        liftMotor.PIDsetpoint = tar
    }






}