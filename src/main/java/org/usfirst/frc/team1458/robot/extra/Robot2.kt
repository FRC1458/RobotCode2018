package org.usfirst.frc.team1458.robot.extra

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import org.usfirst.frc.team1458.lib.actuator.SmartMotor
import org.usfirst.frc.team1458.lib.core.AutoMode
import org.usfirst.frc.team1458.lib.core.BaseRobot
import org.usfirst.frc.team1458.lib.pathfinding.SplineFollower

class Robot2 : BaseRobot() {

    val t1 = SmartMotor.CANtalonSRX(14)
    val t2 = SmartMotor.CANtalonSRX(13)

    override fun robotSetup() {

    }


    override fun setupAutoModes() {

    }


    override fun threadedSetup() {

    }


    override fun teleopInit() {

    }


    override fun teleopPeriodic() {
        t1.speed = 0.1
        t2.speed = 0.1
    }


    override fun runTest() {

    }

    override fun robotDisabled() {

    }

    override fun disabledPeriodic() {

    }

}