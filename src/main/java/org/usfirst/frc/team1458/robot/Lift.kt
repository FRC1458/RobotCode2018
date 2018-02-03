package org.usfirst.frc.team1458.robot

import org.usfirst.frc.team1458.lib.actuator.SmartMotor
import org.usfirst.frc.team1458.lib.pid.PID
import org.usfirst.frc.team1458.lib.pid.PIDConstants

class Lift(tar:Double, liftMotor: SmartMotor) {
    var tar:Double = tar
    set(value)
    {
        field = value
        pid.target=tar
    }
    val constants = PIDConstants(0.0,0.0,0.0,0.0, { 0.0 })
    val pid: PID = PID(constants,tar)






}