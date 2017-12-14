package org.usfirst.frc.team1458.robot;

import org.usfirst.frc.team1458.lib.core.BaseRobot;
import org.usfirst.frc.team1458.lib.hardware.Hardware;
import org.usfirst.frc.team1458.lib.input.*;
import org.usfirst.frc.team1458.lib.motor.Motor;
import org.usfirst.frc.team1458.lib.util.maths.InputFunction;
import org.usfirst.frc.team1458.lib.util.maths.TurtwigMaths;

/**
 * This is our testbed for the bot
 */
public class TestBedRobot extends BaseRobot {

	FlightStick leftStick;
	FlightStick rightStick;

	XboxController xboxController;

	AnalogInput left;
	AnalogInput right;

	Motor leftMotor;
	Motor rightMotor;

	Switch highGear;
	Switch turn;

	DigitalInput speed;

	private static final InputFunction LOW_GEAR = InputFunction.QUARTER;
	private static final InputFunction HIGH_GEAR = InputFunction.IDENTITY;

	public TestBedRobot() {

	}

	@Override
	public void robotSetup() {
		leftStick = Hardware.HumanInterfaceDevices.FlightStick(1);
		rightStick = Hardware.HumanInterfaceDevices.FlightStick(0);

		left = leftStick.getPitch();
		right = rightStick.getPitch();

		highGear = Switch.toggleSwitch(Switch.or(leftStick.getButton(2), rightStick.getButton(2)));

		turn = Switch.or(leftStick.getTrigger(), rightStick.getTrigger());

		speed = DigitalInput.fromUpDown(
				Switch.fromPOV(leftStick.getPOV(), POV.Direction.EAST),
				Switch.fromPOV(leftStick.getPOV(), POV.Direction.WEST),
				-10, 10, 0
		);

		/*left = xboxController.getLeftY();
		right = xboxController.getRightY();
		highGear = Switch.toggleSwitch(xboxController.getButton(XboxController.XboxButton.A));

		turn = Switch.or(xboxController.getButton(XboxController.XboxButton.LBUMP),
				xboxController.getButton(XboxController.XboxButton.RBUMP));

		speed = DigitalInput.fromUpDown(
				Switch.fromPOV(xboxController.getPOV(), POV.Direction.EAST),
				Switch.fromPOV(xboxController.getPOV(), POV.Direction.WEST),
				-10, 10, 0
		);*/


		leftMotor = Hardware.Motors.TalonSRX_CAN(15);
		rightMotor = Hardware.Motors.TalonSRX_CAN(10).invert();
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
		if(turn.get()) {
			double motorValue = TurtwigMaths.shift(speed.getValue(), -10, 10, -1.0, 1.0);
			leftMotor.setSpeed(getTransmission().apply(motorValue));
			rightMotor.setSpeed(getTransmission().apply(-1 * motorValue));
		} else {
			leftMotor.setSpeed(getTransmission().apply(left.getValue()));
			rightMotor.setSpeed(getTransmission().apply(right.getValue()));
		}
	}

	private InputFunction getTransmission() {
		return highGear.get() ? HIGH_GEAR : LOW_GEAR;
	}

	@Override
	public void runTest() {

	}

	@Override
	public void robotDisabled() {

	}
}
