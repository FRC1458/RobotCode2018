package org.usfirst.frc.team1458.lib.drive

import org.usfirst.frc.team1458.lib.actuator.SmartMotor
import org.usfirst.frc.team1458.lib.actuator.Solenoid
import org.usfirst.frc.team1458.lib.drive.util.AutoshiftHelper
import org.usfirst.frc.team1458.lib.drive.util.CheesyDriveHelper
import org.usfirst.frc.team1458.lib.pid.PIDConstants
import org.usfirst.frc.team1458.lib.sensor.interfaces.AngleSensor


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
                val closedLoopScaling: Double? = null, pidConstantsLowGearLeft: PIDConstants? = null, pidConstantsLowGearRight: PIDConstants? = null,

                // Shifter
                var shifter: Solenoid? = null, pidConstantsHighGearLeft: PIDConstants? = null, pidConstantsHighGearRight: PIDConstants? = null, val autoShift: Boolean = false,
                val shiftUpSpeed: Double? = null, val shiftDownSpeed: Double? = null, val shiftCooldown: Double = 0.0,

                var gyro: AngleSensor? = null) {

    var pidConstantsLowGearLeft = pidConstantsLowGearLeft
        set(value) {
            field = value
            refreshPIDConstants()
        }

    var pidConstantsLowGearRight = pidConstantsLowGearRight
        set(value) {
            field = value
            refreshPIDConstants()
        }

    var pidConstantsHighGearLeft = pidConstantsHighGearLeft
        set(value) {
            field = value
            refreshPIDConstants()
        }

    var pidConstantsHighGearRight = pidConstantsHighGearRight
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
                (pidConstantsLowGearLeft != null) && (pidConstantsLowGearRight != null) &&
                (if(autoShift) { pidConstantsHighGearLeft != null && pidConstantsHighGearRight != null } else { true }) &&
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
        var constantsLeft: PIDConstants = (if(highGear) { pidConstantsHighGearLeft } else { pidConstantsLowGearLeft }) ?: PIDConstants.DISABLE
        var constantsRight: PIDConstants = (if(highGear) { pidConstantsHighGearRight } else { pidConstantsLowGearRight }) ?: PIDConstants.DISABLE
        leftMaster.PIDconstants = constantsLeft
        rightMaster.PIDconstants = constantsRight
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
            leftMaster.PIDsetpoint = left //* (360.0 / wheelCircumference)  // Calculate deg/sec from meters/sec

            rightMaster.PIDsetpoint = right// * (360.0 / wheelCircumference) // Calculate deg/sec from meters/sec

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

    fun cheesyDrive(forward: Double, turn: Double, quickturn: Boolean = false) {
        val (left, right) = CheesyDriveHelper.cheesyDrive(forward, turn, quickturn, highGear)
        if(closedLoopControl && closedLoopReady && closedLoopScaling != null) {
            setDriveVelocity(left * closedLoopScaling, right * closedLoopScaling, forward * closedLoopScaling)
        } else {
            setRawDrive(left, right, forward)
        }
    }

    // TODO: integrate motion profiling / pure pursuit
}
// TODO: ADD STRAIGHT DRIVE PID
