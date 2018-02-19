package org.usfirst.frc.team1458.robot

import edu.wpi.first.wpilibj.Joystick
import org.usfirst.frc.team1458.lib.input.FlightStick
import org.usfirst.frc.team1458.lib.input.Gamepad
import org.usfirst.frc.team1458.lib.input.interfaces.Switch

/**
 * Operator interface
 */
class OI {
    // Hardware
    val leftStick : FlightStick = FlightStick.flightStick(0)
    val rightStick : FlightStick = FlightStick.flightStick(1)

    val controlBoard = ControlBoardXbox(3)

    // Tank Drive
    var leftAxis = leftStick.pitchAxis.inverted.scale(1.0)
    var rightAxis = rightStick.pitchAxis.inverted.scale(1.0)
    var driveStraightButton = rightStick.trigger
    var turnButton = leftStick.trigger

    // Manual Shifter
    var shiftUpButton = rightStick.getButton(2)
    var shiftDownButton = leftStick.getButton(2)

    // Arcade Drive
    var steerAxis = controlBoard.xbox.rightX.scale(0.35) // leftStick.rollAxis.scale(0.35)
    var throttleAxis = controlBoard.xbox.leftY.inverted.scale(1.0) //rightStick.pitchAxis.inverted
    var quickturnButton = Switch.fromAnalog(controlBoard.xbox.rightT, threshold = 0.75, greater = true)
}