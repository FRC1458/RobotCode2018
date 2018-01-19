package org.usfirst.frc.team1458.robot

import org.usfirst.frc.team1458.lib.input.FlightStick
import org.usfirst.frc.team1458.lib.input.SteeringWheel

/**
 * Operator interface stuff
 */
class OI {
    val steer : SteeringWheel = SteeringWheel.logitechSteeringWheel(0)
    val throttle : FlightStick = FlightStick.flightStick(1)

    val steerAxis = steer.steering
    val throttleAxis = throttle.pitchAxis.scale { it * 0.4 }.inverted
    val quickTurnButton = throttle.trigger
}