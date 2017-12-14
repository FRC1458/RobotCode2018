package org.usfirst.frc.team1458.lib.core

import edu.wpi.first.wpilibj.RobotState
import edu.wpi.first.wpilibj.SampleRobot
import org.usfirst.frc.team1458.lib.util.GlobalTeleUpdate

import java.util.ArrayList

/**
 * All robot classes should extend from this class.
 *
 * @author asinghani
 */
abstract class BaseRobot : SampleRobot(), AutoModeHolder {

    override val autoModes = ArrayList<AutoMode>()
        get

    override var selectedAutoModeIndex = 0

    /**
     * Initialize the robot's hardware and basic configuration.
     */
    abstract fun robotSetup()

    /**
     * This method should be overridden and add autonomous modes (which extend AutoMode) using the method addAutoMode()
     */
    abstract fun setupAutoModes()

    /**
     * Initialize any robot configuration which is time-consuming and must be run on a separate thread.
     */
    abstract fun threadedSetup()

    /**
     * Setup for teleop (if necessary)
     */
    abstract fun teleopInit()

    /**
     * Called constantly during teleop period
     */
    abstract fun teleopPeriodic()

    /**
     * Run the robot's test mode.
     */
    abstract fun runTest()

    /**
     * Called when the robot is disabled.
     */
    abstract fun robotDisabled()

    fun addAutoMode(autoMode: AutoMode) {
        autoModes.add(autoMode)
    }

    override fun robotInit() {
        robotSetup()
        setupAutoModes()
        if (autoModes.isEmpty()) {
            autoModes.add(BlankAutoMode())
        }

    }

    override fun disabled() {
        robotDisabled()
    }

    override fun autonomous() {
        val autoMode = autoModes[selectedAutoModeIndex]
        autoMode?.auto()
    }

    override fun operatorControl() {
        teleopInit()
        while (RobotState.isOperatorControl() && RobotState.isEnabled()) {
            teleopPeriodic()
            GlobalTeleUpdate.teleUpdate()
        }
    }

    override fun test() {
        runTest()
    }
}
