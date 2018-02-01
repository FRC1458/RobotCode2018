package org.usfirst.frc.team1458.robot

import edu.wpi.first.wpilibj.XboxController
import org.usfirst.frc.team1458.lib.input.FlightStick
import org.usfirst.frc.team1458.lib.input.Gamepad
import org.usfirst.frc.team1458.lib.input.SteeringWheel

/**
 * Operator interface
 */
class OI {
    //val steer : SteeringWheel = SteeringWheel.logitechSteeringWheel(0)
    //val throttle : FlightStick = FlightStick.flightStick(1)
    val xbox : Gamepad = Gamepad.xboxController(3)

    //val steerAxis = steer.steering.scale { 0.5 * it } //.scale { if(it >= -0.195 && it <= 0.024) { 0.0 } else { it } }
    /*val steerAxis = throttle.rollAxis.scale { 0.35 * it }
    val throttleAxis = throttle.pitchAxis.inverted
    val quickTurnButton = throttle.trigger
    val autoButton = throttle.getButton(5)*/

    /*val steerAxis = xbox.leftX.scale { if(Math.abs(it) < 0.1) { 0.0 } else { it } }.scale { 0.6 * it }
    val throttleAxis = xbox.rightY.inverted.scale { if(Math.abs(it) < 0.1) { 0.0 } else { it } }*/
    //val quickTurnButton = xbox.getButton(Gamepad.Button.RBUMP)
    //val autoButton = xbox.getButton(Gamepad.Button.A)
}