package org.usfirst.frc.team1458.robot

import org.usfirst.frc.team1458.lib.input.Gamepad
import org.usfirst.frc.team1458.lib.input.interfaces.POV
import org.usfirst.frc.team1458.lib.input.interfaces.Switch

class ControlBoardXbox(val usbPort: Int) {
    val xbox = Gamepad.xboxController(usbPort)

    val elevator1 = xbox.getButton(Gamepad.Button.Y) // Scale Position
    val elevator2 = xbox.getButton(Gamepad.Button.B)  // Switch Position
    val elevator3 = xbox.getButton(Gamepad.Button.A)  // Exchange Position
    val elevator4 = xbox.getButton(Gamepad.Button.X)  // Ground Position

    val intakePull = Switch.fromPOV(xbox.getPOV(), POV.Direction.WEST) or Switch.fromPOV(xbox.getPOV(), POV.Direction.NORTHWEST) or Switch.fromPOV(xbox.getPOV(), POV.Direction.SOUTHWEST)
    val intakePush = Switch.fromPOV(xbox.getPOV(), POV.Direction.EAST) or Switch.fromPOV(xbox.getPOV(), POV.Direction.NORTHEAST) or Switch.fromPOV(xbox.getPOV(), POV.Direction.SOUTHEAST)

    val lift = xbox.getButton(Gamepad.Button.LBUMP)
    val winch = xbox.getButton(Gamepad.Button.RBUMP)
}