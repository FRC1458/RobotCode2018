package org.usfirst.frc.team1458.robot

import org.usfirst.frc.team1458.lib.actuator.SmartMotor
import org.usfirst.frc.team1458.lib.drive.TankDrive
import org.usfirst.frc.team1458.lib.pid.PIDConstants
import org.usfirst.frc.team1458.lib.sensor.NavX

class RobotMapPracticeChassis: IRobotMap {
    override val drivetrain : TankDrive = TankDrive(
            leftMaster = SmartMotor.CANtalonSRX(12),
            rightMaster = SmartMotor.CANtalonSRX(15).inverted,
            leftMotors = arrayOf(SmartMotor.CANtalonSRX(10), SmartMotor.CANtalonSRX(11)),
            rightMotors = arrayOf(SmartMotor.CANtalonSRX(13), SmartMotor.CANtalonSRX(14)).map { it.inverted }.toTypedArray(),
            closedLoopControl = true,
            wheelDiameter = 0.354,
            closedLoopScaling = 13.0,

            pidConstantsLowGearLeft = PIDConstants(0.15, kI = 0.001, kD = 0.01, kF = 1.0/6530.5),
            pidConstantsLowGearRight = PIDConstants(0.15, kI = 0.001, kD = 0.01, kF = 1.0/6877.7)
    )

     //val navX = NavX.Micro_I2C()
}