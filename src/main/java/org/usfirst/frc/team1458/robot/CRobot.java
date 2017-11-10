package org.usfirst.frc.team1458.robot;

import edu.wpi.cscore.HttpCamera;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team1458.lib.core.AutoMode;
import org.usfirst.frc.team1458.lib.core.BaseRobot;
import org.usfirst.frc.team1458.lib.hardware.Hardware;
import org.usfirst.frc.team1458.lib.input.*;
import org.usfirst.frc.team1458.lib.motor.Motor;
import org.usfirst.frc.team1458.lib.motor.MotorSet;
import org.usfirst.frc.team1458.lib.motor.abilities.BrakeMode;
import org.usfirst.frc.team1458.lib.pid.PID;
import org.usfirst.frc.team1458.lib.pid.PIDConstants;
import org.usfirst.frc.team1458.lib.util.maths.InputFunction;
import org.usfirst.frc.team1458.lib.util.maths.RangeShifter;
import org.usfirst.frc.team1458.lib.util.maths.TurtleMaths;

/**
 * Final robot for the club fair
 */
public class CRobot extends BaseRobot {

	XboxController xboxController;

	AnalogInput left;
	AnalogInput right;

	Switch shooterButton;
	Switch climbButton;

	Motor leftMotor;
	Motor rightMotor;

	Motor shooterMotor;
	Motor climberMotor;

	VisionTracking vision;

	PID pid;

	private static final InputFunction INPUT_FUNCTION = x -> x/3.0; // max 1/3 speed

	public CRobot() {

	}

	@Override
	public void robotSetup() {
		xboxController = Hardware.HumanInterfaceDevices.XboxController(1); // TODO change to correct port

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

		vision = new VisionTracking(new HttpCamera("HTTP camera", "http://localhost:5800/?action=stream"));

		// TODO TUNE
		pid = new PID(new PIDConstants(0.0025 , 0.000025, 0.001), 160, 20, true);
	}

	@Override
	public void setupAutoModes() {
		addAutoMode(new AutoMode() {
			@NotNull
			@Override
			public String getName() {
				return "AUTOMODE";
			}

			@Override
			public void auto() {
				leftMotor.setSpeed(0.3);
				rightMotor.setSpeed(-0.3);

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				leftMotor.stop();
				rightMotor.stop();
			}
		});
	}

	@Override
	public void threadedSetup() {

	}

	@Override
	public void teleopInit() {

	}

	@Override
	public void teleopPeriodic() {
		double x = vision.getShooterTargetX();
		SmartDashboard.putNumber("Shooter Target", x);


		if(climbButton.get()) {
			double value = pid.newValue(x);
			leftMotor.setSpeed(value);
			rightMotor.setSpeed(-value);

			// TODO make sure target is correct
			shooterMotor.setSpeed(pid.atTarget() ? -0.7 : 0);
		} else {
			leftMotor.setSpeed(left.getValue());
			rightMotor.setSpeed(right.getValue());

			shooterMotor.setSpeed(shooterButton.get() ? -0.7 : 0);
		}

		SmartDashboard.putNumber("Battery", DriverStation.getInstance().getBatteryVoltage());
		SmartDashboard.putString("Robot", RobotState.isEnabled() ? "Enabled" : "Disabled");

		//System.out.println(x);
	}

	@Override
	public void runTest() {

	}

	@Override
	public void robotDisabled() {

	}
}