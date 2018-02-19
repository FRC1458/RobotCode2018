package org.usfirst.frc.team1458.robot.subsystem

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import org.usfirst.frc.team1458.lib.actuator.SmartMotor

import org.usfirst.frc.team1458.lib.input.interfaces.Switch
import org.usfirst.frc.team1458.lib.util.flow.systemTimeMillis

class Climber(val liftMotor: SmartMotor,
              val winchMotor: SmartMotor,
              val topLimit: Switch,
              val liftControl: Switch,
              val climbControl: Switch,

              val liftSpeed: Double = 0.5,
              val liftAfterSpeed: Double = -0.2,
              val winchSpeed: Double = 0.5,
              val winchAfterSpeed: Double = 0.0,
              val liftAfterTime: Double = 3000.0,

              val liftCurrentLimit: Double = 1e5,
              val winchCurrentLimit: Double = 1e5) {

    var winchStarted = false

    var climbStart = 0.0

    fun update(){
        SmartDashboard.putBoolean("LIMIT SWITCH TOP REE", topLimit.triggered)
        if(liftControl.triggered && topLimit.triggered &&
                liftMotor.currentDraw <= liftCurrentLimit && !climbControl.triggered) {
            liftMotor.speed = liftSpeed
        } else if (climbControl.triggered && systemTimeMillis - climbStart < liftAfterTime) {
            liftMotor.speed = liftAfterSpeed
        } else {
            liftMotor.speed = 0.0
        }

        if(climbControl.triggered && winchMotor.currentDraw <= winchCurrentLimit){
            winchMotor.speed = winchSpeed
            if(!winchStarted) {
                climbStart = systemTimeMillis
            }
            winchStarted = true
        } else if (winchStarted && winchMotor.currentDraw <= winchCurrentLimit) {
            winchMotor.speed = winchAfterSpeed
        } else {
            winchMotor.speed = 0.0
            climbStart = 0.0
        }
    }
}






