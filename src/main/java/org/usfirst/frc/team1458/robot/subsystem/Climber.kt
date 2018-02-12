package org.usfirst.frc.team1458.robot.subsystem

import com.ctre.phoenix.motorcontrol.can.TalonSRX
import org.usfirst.frc.team1458.lib.actuator.SmartMotor

import org.usfirst.frc.team1458.lib.input.interfaces.Switch

class Climber(
        val upStage: SmartMotor,
        val climbStage: SmartMotor,
        val limitSwitch: Switch,
        val upSwitch: Switch,
        val climbSwitch: Switch,
        val upSpeed: Double,
        val climbSpeed: Double,
        val upMaxCurrent: Double,
        val climbMaxCurrent: Double
    )
{
  fun update(){
      if(upSwitch.triggered&&!limitSwitch.triggered&&upStage.currentDraw<=upMaxCurrent){
          upStage.speed=upSpeed

      }
      if(climbSwitch.triggered&&climbStage.currentDraw<=climbMaxCurrent){
              climbStage.speed=climbSpeed
      }
  }
}






