package org.usfirst.frc.team1458.robot

import org.usfirst.frc.team1458.lib.actuator.SmartMotor
import org.usfirst.frc.team1458.lib.actuator.Solenoid
import org.usfirst.frc.team1458.lib.drive.TankDrive
import org.usfirst.frc.team1458.lib.pid.PIDConstants
import org.usfirst.frc.team1458.lib.sensor.NavX

class RobotMapFinalChassis: IRobotMap {
    override val drivetrain : TankDrive = TankDrive(
            leftMaster = SmartMotor.CANtalonSRX(16),
            rightMaster = SmartMotor.CANtalonSRX(22).inverted,
            leftMotors = arrayOf(SmartMotor.CANtalonSRX(25)),
            rightMotors = arrayOf(SmartMotor.CANtalonSRX(23).inverted),
            closedLoopControl = false,
            wheelDiameter = 0.5,
            closedLoopScaling = 18.0, // TODO: determine

            pidConstantsLowGearLeft = PIDConstants(0.15, kI = 0.001, kD = 0.01, kF = 1.0/6530.5), // TODO: determine
            pidConstantsLowGearRight = PIDConstants(0.15, kI = 0.001, kD = 0.01, kF = 1.0/6877.7), // TODO: determine

            shifter = Solenoid.doubleSolenoid(extendChannel = 1, retractChannel = 0)
                    + Solenoid.doubleSolenoid(extendChannel = 2, retractChannel = 3),

            pidConstantsHighGearLeft = PIDConstants(0.15, kI = 0.001, kD = 0.01, kF = 1.0/6530.5), // TODO: determine
            pidConstantsHighGearRight = PIDConstants(0.15, kI = 0.001, kD = 0.01, kF = 1.0/6877.7), // TODO: determine
            autoShift = false,
            shiftUpSpeed = 10.0, // TODO: determine
            shiftDownSpeed = 12.0, // TODO: determine
            shiftCooldown = 3.0
    )

    //override val navX = NavX.MXP_I2C()
}