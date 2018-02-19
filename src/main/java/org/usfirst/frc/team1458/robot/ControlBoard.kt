package org.usfirst.frc.team1458.robot

import edu.wpi.first.wpilibj.Joystick

class ControlBoard(val usbPort: Int) {
    val joystick = Joystick(usbPort)

    val elevator1 = joystick.getRawButton(0) // Scale Position (top)
    val elevator2 = joystick.getRawButton(1) // Switch Position
    val elevator3 = joystick.getRawButton(2) // Exchange Position
    val elevator4 = joystick.getRawButton(3) // Ground Position

    val intakePull = joystick.getRawButton(4)
    val intakePush = joystick.getRawButton(5)

    val lift = joystick.getRawButton(6)
    val winch = joystick.getRawButton(7)
}