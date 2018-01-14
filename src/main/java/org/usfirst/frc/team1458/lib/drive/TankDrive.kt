package org.usfirst.frc.team1458.lib.drive

import org.usfirst.frc.team1458.lib.actuator.SmartMotor
import org.usfirst.frc.team1458.lib.actuator.Solenoid
import org.usfirst.frc.team1458.lib.drive.util.AutoshiftHelper
import org.usfirst.frc.team1458.lib.pid.PIDConstants


/**
 * Tank drive class
 *
 * @param leftMaster Master motor for left side
 * @param rightMaster Master motor for right side
 * @param leftMotors Other drive motors for left side
 * @param rightMotors Other drive motors for right side
 *
 * @param closedLoopControl Whether to enable closed-loop control for the drivetrain
 * @param shifter Shifter for high/low gear (extended is high gear, retracted is low)
 * @param pidConstantsLowGear Default PID constants (used for low gear if using shifter)
 * @param pidConstantsHighGear PID constants for high gear
 * @param wheelCircumference Circumference of the wheels (in meters)
 * @param closedLoopScaling Scale between [-1.0, 1.0] and actual closed-loop velocity (meters/second) for driver control
 */
class TankDrive(val leftMaster: SmartMotor, val rightMaster: SmartMotor,
                val leftMotors: Array<SmartMotor> = arrayOf(), val rightMotors: Array<SmartMotor> = arrayOf(),

                // Closed Loop Control
                var closedLoopControl: Boolean = false, val wheelCircumference: Double? = null,
                val closedLoopScaling: Double? = null, pidConstantsLowGear: PIDConstants? = null,

                // Shifter
                var shifter: Solenoid? = null, pidConstantsHighGear: PIDConstants? = null, val autoShift: Boolean = false,
                val shiftUpSpeed: Double? = null, val shiftDownSpeed: Double? = null, val shiftCooldown: Double = 0.0) {

    // TODO: make seperate pid constants for left/right
    var pidConstantsLowGear = pidConstantsLowGear
        set(value) {
            field = value
            refreshPIDConstants()
        }

    var pidConstantsHighGear = pidConstantsHighGear
        set(value) {
            field = value
            refreshPIDConstants()
        }

    var highGear = false

    val autoshiftHelper = {
        if(shiftUpSpeed != null && shiftDownSpeed != null) AutoshiftHelper(shiftUpSpeed, shiftDownSpeed, shiftCooldown)
        else null
    }()

    /**
     * Returns true if closed loop mode is ready or false if open-loop should be used as a fallback
     */
    val closedLoopReady : Boolean
        get() = closedLoopControl &&
                (wheelCircumference != null) &&
                (closedLoopScaling != null) &&
                (pidConstantsLowGear != null) &&
                (if(autoShift) { pidConstantsHighGear != null } else { true }) &&
                leftMaster.isEncoderWorking &&
                rightMaster.isEncoderWorking

    val canAutoShift : Boolean
        get() = autoShift &&
                shifter != null &&
                shiftUpSpeed != null &&
                shiftDownSpeed != null &&
                if(closedLoopControl) { closedLoopReady } else { closedLoopScaling != null } &&
                autoshiftHelper != null

    init {
        // Set both motors to drive
        for(motor in leftMotors) {
            motor.follow(leftMaster)
        }

        for(motor in rightMotors) {
            motor.follow(rightMaster)
        }

        shifter?.retract()
        highGear = false

        refreshPIDConstants()
    }

    private fun refreshPIDConstants() {
        var constants: PIDConstants = (if(highGear) { pidConstantsHighGear } else { pidConstantsLowGear }) ?: PIDConstants.DISABLE
        leftMaster.PIDconstants = constants
        rightMaster.PIDconstants = constants
    }

    fun lowGear() {
        if(shifter != null) {
            shifter?.retract()
            highGear = false
            refreshPIDConstants()
        }
    }

    fun highGear() {
        if(shifter != null) {
            shifter?.extend()
            highGear = true
            refreshPIDConstants()
        }
    }

    fun setDriveVelocity(left: Double, right: Double, forwardSpeed: Double? = null) {
        if(wheelCircumference != null) {
            leftMaster.PIDsetpoint = left * (360.0 / wheelCircumference)  // Calculate deg/sec from meters/sec

            rightMaster.PIDsetpoint = right * (360.0 / wheelCircumference) // Calculate deg/sec from meters/sec

            if(shiftDownSpeed != null && shiftUpSpeed != null && forwardSpeed != null && canAutoShift) {
                autoshiftHelper?.autoshift(forwardSpeed, left, right, this::lowGear, this::highGear)
            }
        }
    }

    fun setRawDrive(left: Double, right: Double, forwardSpeed: Double? = null) {
        leftMaster.speed = left

        rightMaster.speed = right

        if(forwardSpeed != null && canAutoShift && closedLoopScaling != null) {
            autoshiftHelper?.autoshift(forwardSpeed, left * closedLoopScaling, right * closedLoopScaling, this::lowGear, this::highGear)
        }
    }

    fun tankDrive(left: Double, right: Double) {
        if(closedLoopControl && closedLoopReady && closedLoopScaling != null) {
            setDriveVelocity(left * closedLoopScaling, right * closedLoopScaling, ((left+right)/2.0) * closedLoopScaling)
        } else {
            setRawDrive(left, right, (left+right)/2.0)
        }
    }

    fun arcadeDrive(forward: Double, turn: Double) {
        if(closedLoopControl && closedLoopReady && closedLoopScaling != null) {
            setDriveVelocity((forward - turn) * closedLoopScaling, (forward + turn) * closedLoopScaling, forward * closedLoopScaling)
        } else {
            setRawDrive(forward - turn, forward + turn, forward)
        }
    }

    fun scaledArcadeDrive(forward: Double, turn: Double, quickturn: Boolean = false, scaleFun : (Double) -> Double = { 0.6 * Math.pow(it, 0.5) + 0.2 }) {
        if(quickturn) {
            arcadeDrive(forward, turn)
        } else {
            if(closedLoopControl && closedLoopReady && closedLoopScaling != null) {
                setDriveVelocity((forward - turn * scaleFun(forward)) * closedLoopScaling, (forward + turn * scaleFun(forward)) * closedLoopScaling, forward * closedLoopScaling)
            } else {
                setRawDrive(forward - turn * scaleFun(forward), forward + turn * scaleFun(forward), forward)
            }
        }
    }

    //fun cheesyDrive(forward: Double, turn: Double) {
        // TODO: Implement
    //}

    // TODO: integrate motion profiling / pure pursuit
}
// TODO: ADD STRAIGHT DRIVE PID
