package org.usfirst.frc.team1458.robot

import edu.wpi.first.wpilibj.AnalogInput
import org.usfirst.frc.team1458.lib.actuator.SmartMotor
import org.usfirst.frc.team1458.lib.actuator.Solenoid
import org.usfirst.frc.team1458.lib.drive.TankDrive
import org.usfirst.frc.team1458.lib.input.interfaces.DigitalInput
import org.usfirst.frc.team1458.lib.input.interfaces.Switch
import org.usfirst.frc.team1458.lib.pid.PIDConstants
import org.usfirst.frc.team1458.lib.sensor.NavX
import org.usfirst.frc.team1458.robot.subsystem.Climber
import org.usfirst.frc.team1458.robot.subsystem.QuantumIntake

class RobotMapFinalChassis(val oi: OI) {
    val drivetrain : TankDrive = TankDrive(
            leftMaster = SmartMotor.CANtalonSRX(16),
            rightMaster = SmartMotor.CANtalonSRX(22).inverted,
            leftMotors = arrayOf(SmartMotor.CANtalonSRX(25)),
            rightMotors = arrayOf(SmartMotor.CANtalonSRX(23).inverted),
            closedLoopControl = true,
            wheelDiameter = 0.5,
            closedLoopScaling = 6.0, // TODO: determine

            pidConstantsLowGearLeft =  PIDConstants(0.5, kI = 0.001, kD = 0.05, kF = 1.0/1798.8), // These are decent
            pidConstantsLowGearRight = PIDConstants(0.5, kI = 0.001, kD = 0.05, kF = 1.0/1806.4), // These are decent2

            shifter = Solenoid.doubleSolenoid(extendChannel = 1, retractChannel = 0)
                    + Solenoid.doubleSolenoid(extendChannel = 2, retractChannel = 3),

            pidConstantsHighGearLeft = PIDConstants(0.15, kI = 0.001, kD = 0.01, kF = 1.0/6530.5), // TODO: determine
            pidConstantsHighGearRight = PIDConstants(0.15, kI = 0.001, kD = 0.01, kF = 1.0/6877.7), // TODO: determine
            autoShift = false,
            shiftUpSpeed = 10.0, // TODO: determine
            shiftDownSpeed = 12.0, // TODO: determine
            shiftCooldown = 3.0,

            accelLimit = 1000000.0 // feet/sec^2
    )

    val intake = QuantumIntake(oi.controlBoard.intakePull, oi.controlBoard.intakePush,
            SmartMotor.CANtalonSRX(17).inverted, SmartMotor.CANtalonSRX(19), AnalogInput(0), 1950.0)

    val climber = Climber(
            liftMotor = SmartMotor.CANtalonSRX(60),
            winchMotor = SmartMotor.CANtalonSRX(24),
            topLimit = Switch.fromDIO(3),
            liftControl = oi.controlBoard.lift,
            climbControl = oi.controlBoard.winch,
            climbAfterWinchSpeed = 0.3,
            climbAfterControl = oi.controlBoard.climberStay,
            liftSpeed = 0.4,
            liftAfterSpeed = -0.25,
            winchSpeed = 1.0,
            winchAfterSpeed = 0.0,
            liftAfterTime = 2000.0
    )

    val navX = NavX.MXP_I2C()
}