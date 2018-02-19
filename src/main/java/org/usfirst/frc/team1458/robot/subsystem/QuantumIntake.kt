package org.usfirst.frc.team1458.robot.subsystem

import edu.wpi.first.wpilibj.AnalogInput
import org.usfirst.frc.team1458.lib.actuator.SmartMotor
import org.usfirst.frc.team1458.lib.input.interfaces.Switch
import org.usfirst.frc.team1458.lib.util.flow.systemTimeMillis

class QuantumIntake(val intakeButton: Switch, val releaseButton: Switch,
                    val leftMotor: SmartMotor, val rightMotor: SmartMotor,
                    val cubeDetector: AnalogInput, val cubeThreshold: Double,
                    val intakeSpeed : Double = 1.0, val releaseSpeed : Double = 1.0,
                    val releaseTime : Double = 3.0, val intakeTime : Double = 1.0) {

    val cubeInIntake : Boolean
        get() = cubeDetector.value > cubeThreshold

    var intaking = false
    var releasing = false

    var startTime = Double.MAX_VALUE - 1

    fun update() {
        if(intakeButton.triggered && !cubeInIntake) {
            leftMotor.speed = intakeSpeed
            rightMotor.speed = intakeSpeed
        } else if(releaseButton.triggered) {
            leftMotor.speed = -releaseSpeed
            rightMotor.speed = -releaseSpeed
        } else {
            leftMotor.speed = 0.0
            rightMotor.speed = 0.0
            startTime = Double.MAX_VALUE - 1
        }
    }

    fun update(pullIn: Boolean, pushOut: Boolean) {
        if(pullIn && !cubeInIntake) {
            leftMotor.speed = intakeSpeed
            rightMotor.speed = intakeSpeed
        } else if(pushOut) {
            leftMotor.speed = -releaseSpeed
            rightMotor.speed = -releaseSpeed
        } else {
            leftMotor.speed = 0.0
            rightMotor.speed = 0.0
            startTime = Double.MAX_VALUE - 1
        }
    }
}