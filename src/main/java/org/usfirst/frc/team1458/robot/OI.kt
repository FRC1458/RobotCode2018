package org.usfirst.frc.team1458.robot

import org.usfirst.frc.team1458.lib.input.FlightStick
import org.usfirst.frc.team1458.lib.input.SteeringWheel

/**
 * Operator interface
 */
class OI {
    val steer : SteeringWheel = SteeringWheel.logitechSteeringWheel(0)
    val throttle : FlightStick = FlightStick.flightStick(1)

    val steerAxis = steer.steering//.scale { if(it >= -0.195 && it <= 0.024) { 0.0 } else { it } }
    val throttleAxis = throttle.pitchAxis.inverted
    val quickTurnButton = throttle.trigger
    val autoButton = throttle.getButton(5)
}