package org.usfirst.frc.team1458.lib.actuator

import edu.wpi.first.wpilibj.DoubleSolenoid

interface Solenoid {
    fun test() {
        edu.wpi.first.wpilibj.DoubleSolenoid(1,2 ).get() = DoubleSolenoid.Value.kForward
    }

    fun extend()
    fun retract()
}