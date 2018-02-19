package org.usfirst.frc.team1458.robot

import edu.wpi.first.wpilibj.AnalogInput
import org.usfirst.frc.team1458.lib.actuator.SmartMotor
import org.usfirst.frc.team1458.lib.drive.TankDrive
import org.usfirst.frc.team1458.lib.input.interfaces.Switch
import org.usfirst.frc.team1458.lib.pid.PIDConstants
import org.usfirst.frc.team1458.lib.sensor.NavX
import org.usfirst.frc.team1458.robot.subsystem.Climber
import org.usfirst.frc.team1458.robot.subsystem.QuantumIntake

class RobotMapPracticeChassis(oi: OI) {
    val drivetrain : TankDrive = TankDrive(
            leftMaster = SmartMotor.CANtalonSRX(12),
            rightMaster = SmartMotor.CANtalonSRX(15).inverted,
            leftMotors = arrayOf(SmartMotor.CANtalonSRX(10), SmartMotor.CANtalonSRX(11)),
            rightMotors = arrayOf(SmartMotor.CANtalonSRX(13), SmartMotor.CANtalonSRX(14)).map { it.inverted }.toTypedArray(),
            closedLoopControl = true,
            wheelDiameter = 0.354,
            closedLoopScaling = 13.0,

            pidConstantsLowGearLeft =  PIDConstants(0.15, kI = 0.001, kD = 0.01, kF = 1.0/6530.5),
            pidConstantsLowGearRight = PIDConstants(0.15, kI = 0.001, kD = 0.01, kF = 1.0/6877.7)
    )

    val intake = QuantumIntake(oi.controlBoard.intakePull, oi.controlBoard.intakePush,
            SmartMotor.CANtalonSRX(17).inverted, SmartMotor.CANtalonSRX(19), AnalogInput(0), 1950.0)

    val climber = Climber(
            liftMotor = SmartMotor.CANtalonSRX(-1),
            winchMotor = SmartMotor.CANtalonSRX(-1),
            topLimit = Switch.fromDIO(2).inverted,
            liftControl = oi.controlBoard.lift,
            climbControl = oi.controlBoard.winch,
            liftSpeed = 0.8,
            liftAfterSpeed = 0.2,
            winchSpeed = 0.8,
            winchAfterSpeed = 0.0,
            liftAfterTime = 2000.0
    )

     val navX = NavX.Micro_I2C()
}