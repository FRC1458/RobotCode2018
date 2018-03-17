package org.usfirst.frc.team1458.robot

import org.usfirst.frc.team1458.lib.input.FlightStick

/**
 * Operator interface
 */
class OI {
    // Hardware
    val leftStick : FlightStick = FlightStick.flightStick(1)
    val rightStick : FlightStick = FlightStick.flightStick(0)

    val controlBoard = ControlBoard(5)

    // Manual Shifter
    var shiftUpButton = rightStick.getButton(2)
    var shiftDownButton = leftStick.getButton(2)

    // Arcade Drive
    var steerAxis = leftStick.rollAxis.scale(0.5) //controlBoard.xbox.rightX.scale(0.35) //
    var throttleAxis = rightStick.pitchAxis.inverted //controlBoard.xbox.leftY.inverted.scale(1.0) //
    var invertButton = leftStick.getButton(6)
    var slowDownButton = rightStick.trigger

    val switchCamera = leftStick.getButton(3)
}