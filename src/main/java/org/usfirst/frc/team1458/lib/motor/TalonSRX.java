package org.usfirst.frc.team1458.lib.motor;

import com.ctre.CANTalon;
import org.usfirst.frc.team1458.lib.motor.abilities.BrakeMode;
import org.usfirst.frc.team1458.lib.motor.abilities.BrakeModeSelectable;
import org.usfirst.frc.team1458.lib.motor.abilities.Follower;
import org.usfirst.frc.team1458.lib.sensor.power.PowerMeasurable;
import org.usfirst.frc.team1458.lib.util.maths.InputFunction;

/**
 * Represents a CAN Talon SRX motor controller with all the additional functionality.
 *
 * @author asinghani
 */
// TODO implement simulatability
public class TalonSRX implements Motor, Follower<TalonSRX>, PowerMeasurable, BrakeModeSelectable {
	private final CANTalon motor;
	private boolean directControl = true;
	private InputFunction inputFunction = InputFunction.IDENTITY;

	public TalonSRX(int CANid) {
		this.motor = new CANTalon(CANid);
	}

	public TalonSRX(int CANid, BrakeMode brakeMode) {
		this(CANid);
		setBrakeMode(brakeMode);
	}

	public TalonSRX(int CANid, TalonSRX master) {
		this(CANid);
		follow(master);
	}

	@Override
	public TalonSRX setSpeed(double speed) {
		if(directControl) {
			motor.set(speed);
		}
		return this;
	}

	@Override
	public double getSpeed() {
		return motor.getSpeed();
	}

	@Override
	public TalonSRX follow(TalonSRX toFollow) {
		motor.changeControlMode(CANTalon.TalonControlMode.Follower);
		motor.set(toFollow.getID());
		directControl = false;

		return this;
	}

	@Override
	public TalonSRX cancelFollow() {
		motor.changeControlMode(CANTalon.TalonControlMode.Follower);
		motor.set(0);
		directControl = false;

		return this;
	}

	@Override
	public TalonSRX setBrakeMode(BrakeMode mode) {
		motor.enableBrakeMode(mode == BrakeMode.BRAKE);
		return this;
	}

	@Override
	public TalonSRX invert() {
		inputFunction = inputFunction.invert();
		return this;
	}

	@Override
	public TalonSRX scale(InputFunction newInputFunction) {
		inputFunction = inputFunction.scale(newInputFunction);
		return this;
	}

	public int getID() {
		return motor.getDeviceID();
	}

	@Override
	public double getVoltage() {
		return motor.getOutputVoltage();
	}

	@Override
	public double getCurrent() {
		return motor.getOutputCurrent();
	}
}
