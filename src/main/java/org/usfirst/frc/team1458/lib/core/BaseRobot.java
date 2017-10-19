package org.usfirst.frc.team1458.lib.core;

import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.SampleRobot;
import org.usfirst.frc.team1458.lib.util.GlobalTeleUpdate;

import java.util.ArrayList;

/**
 * All robot classes should extend from this class.
 *
 * @author asinghani
 */
public abstract class BaseRobot extends SampleRobot implements AutoModeHolder {

	protected ArrayList<AutoMode> autoModes = new ArrayList<>();
	protected int selectedAutoMode = 0;

	/**
	 * Initialize the robot's hardware and basic configuration.
	 */
	public abstract void robotSetup();

	/**
	 * This method should be overridden and add autonomous modes (which extend AutoMode) using the method addAutoMode()
	 */
	public abstract void setupAutoModes();

	/**
	 * Initialize any robot configuration which is time-consuming and must be run on a separate thread.
	 */
	public abstract void threadedSetup();

	/**
	 * Setup for teleop (if necessary)
	 */
	public abstract void teleopInit();

	/**
	 * Called constantly during teleop period
	 */
	public abstract void teleopPeriodic();

	/**
	 * Run the robot's test mode.
	 */
	public abstract void runTest();

	/**
	 * Called when the robot is disabled.
	 */
	public abstract void robotDisabled();

	@Override
	public ArrayList<? extends AutoMode> getAutoModes() {
		return autoModes;
	}

	@Override
	public void setSelectedAutoModeIndex(int index) {
		this.selectedAutoMode = index;
	}

	@Override
	public int getSelectedAutoModeIndex() {
		return selectedAutoMode;
	}

	public final void addAutoMode(AutoMode autoMode) {
		autoModes.add(autoMode);
	}

	@Override
	protected final void robotInit() {
		robotSetup();
		setupAutoModes();
		if(autoModes.isEmpty()) {
			autoModes.add(new BlankAutoMode());
		}

	}

	@Override
	protected final void disabled() {
		robotDisabled();
	}

	@Override
	public final void autonomous() {
		AutoMode autoMode = autoModes.get(selectedAutoMode);
		if(autoMode != null) {
			autoMode.auto();
		}
	}

	@Override
	public final void operatorControl() {
		teleopInit();
		while(RobotState.isOperatorControl() && RobotState.isEnabled()) {
			teleopPeriodic();
			GlobalTeleUpdate.teleUpdate();
		}
	}

	@Override
	public final void test() {
		runTest();
	}
}
