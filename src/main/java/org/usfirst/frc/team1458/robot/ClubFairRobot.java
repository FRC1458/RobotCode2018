package org.usfirst.frc.team1458.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team1458.lib.core.BaseRobot;
import org.usfirst.frc.team1458.lib.hardware.Hardware;
import org.usfirst.frc.team1458.lib.input.*;
import org.usfirst.frc.team1458.lib.motor.Motor;
import org.usfirst.frc.team1458.lib.motor.MotorSet;
import org.usfirst.frc.team1458.lib.motor.abilities.BrakeMode;
import org.usfirst.frc.team1458.lib.util.maths.InputFunction;
import org.usfirst.frc.team1458.lib.util.maths.RangeShifter;
import org.usfirst.frc.team1458.lib.util.maths.TurtleMaths;

/**
 * Final robot for the club fair
 */
public class ClubFairRobot extends BaseRobot {

	XboxController xboxController;

	AnalogInput left;
	AnalogInput right;

	Switch shooterButton;
	Switch climbButton;

	Motor leftMotor;
	Motor rightMotor;

	Motor shooterMotor;
	Motor climberMotor;

	private static final InputFunction INPUT_FUNCTION = x -> x/3.0; // max 1/3 speed

	public ClubFairRobot() {

	}

	@Override
	public void robotSetup() {
		xboxController = Hardware.HumanInterfaceDevices.XboxController(0); // TODO change to correct port

		left = xboxController.getLeftY().scale(INPUT_FUNCTION);
		right = xboxController.getRightY().scale(INPUT_FUNCTION);

		shooterButton = xboxController.getButton(XboxController.XboxButton.Y);
		climbButton = xboxController.getButton(XboxController.XboxButton.A);

		// TODO change to right ports
		leftMotor = new MotorSet(
				Hardware.Motors.TalonSRX_CAN(10),
				Hardware.Motors.TalonSRX_CAN(11),
				Hardware.Motors.TalonSRX_CAN(12)
		).invert();
		rightMotor = new MotorSet(
				Hardware.Motors.TalonSRX_CAN(13),
				Hardware.Motors.TalonSRX_CAN(14),
				Hardware.Motors.TalonSRX_CAN(15)
		);

		shooterMotor = Hardware.Motors.TalonSRX_CAN(18, BrakeMode.COAST).invert();
		climberMotor = Hardware.Motors.TalonSRX_CAN(17, BrakeMode.BRAKE);
	}

	@Override
	public void setupAutoModes() {

	}

	@Override
	public void threadedSetup() {

	}

	@Override
	public void teleopInit() {

	}

	@Override
	public void teleopPeriodic() {
		leftMotor.setSpeed(left.getValue());
		rightMotor.setSpeed(right.getValue());

		shooterMotor.setSpeed(shooterButton.get() ? -0.5 : 0);
		climberMotor.setSpeed(climbButton.getValue());

		SmartDashboard.putNumber("Battery", DriverStation.getInstance().getBatteryVoltage());
		SmartDashboard.putString("Robot", RobotState.isEnabled() ? "Enabled" : "Disabled");
	}

	@Override
	public void runTest() {

	}

	@Override
	public void robotDisabled() {

	}
}
