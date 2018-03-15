package org.usfirst.frc.team1458.robot

import edu.wpi.first.wpilibj.Joystick
import org.usfirst.frc.team1458.lib.input.interfaces.Switch

class ControlBoard(val usbPort: Int) {
    val joystick = Joystick(usbPort)

    val elevator1 = Switch.create { joystick.getRawButton(4) }
    val elevator3 = Switch.create { joystick.getRawButton(5) }

    val intakePull = Switch.create { joystick.getRawButton(7) }
    val intakePush = Switch.create { joystick.getRawButton(6) }

    val lift = Switch.create { joystick.getRawButton(9) }
    val winch = Switch.create { joystick.getRawButton(10) }
}