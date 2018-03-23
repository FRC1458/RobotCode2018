package org.usfirst.frc.team1458.lib.drive

import org.usfirst.frc.team1458.lib.actuator.SmartMotor
import org.usfirst.frc.team1458.lib.actuator.Solenoid
import org.usfirst.frc.team1458.lib.drive.util.AutoshiftHelper
import org.usfirst.frc.team1458.lib.drive.util.CheesyDriveHelper
import org.usfirst.frc.team1458.lib.pid.PIDConstants
import org.usfirst.frc.team1458.lib.sensor.interfaces.AngleSensor
import org.usfirst.frc.team1458.lib.sensor.interfaces.DistanceSensor
import org.usfirst.frc.team1458.lib.util.flow.systemTimeMillis
import org.usfirst.frc.team1458.lib.util.flow.systemTimeSeconds
import java.lang.Math.abs
import kotlin.math.abs


/**
 * Tank drive class
 *
 * @param leftMaster Master motor for leftTrajectory side
 * @param rightMaster Master motor for rightTrajectory side
 * @param leftMotors Other drive motors for leftTrajectory side
 * @param rightMotors Other drive motors for rightTrajectory side
 *
 * @param closedLoopControl Whether to enable closed-loop control for the drivetrain
 * @param shifter Shifter for high/low gear (extended is high gear, retracted is low)
 * @param pidConstantsLowGear Default PID constants (used for low gear if using shifter)
 * @param pidConstantsHighGear PID constants for high gear
 * @param wheelDiameter Diameter of the wheels
 * @param closedLoopScaling Scale between [-1.0, 1.0] and actual closed-loop velocity (meters/second) for driver control
 */
class TankDrive(val leftMaster: SmartMotor,
                val rightMaster: SmartMotor,
                val leftMotors: Array<SmartMotor> = arrayOf(),
                val rightMotors: Array<SmartMotor> = arrayOf(),

                // Closed Loop Control
                var closedLoopControl: Boolean = false,
                wheelDiameter: Double? = null,
                val closedLoopScaling: Double? = null,
                pidConstantsLowGearLeft: PIDConstants? = null,
                pidConstantsLowGearRight: PIDConstants? = null,

                // Shifter
                var shifter: Solenoid? = null,
                pidConstantsHighGearLeft: PIDConstants? = null,
                pidConstantsHighGearRight: PIDConstants? = null,
                val autoShift: Boolean = false,
                val shiftUpSpeed: Double? = null,
                val shiftDownSpeed: Double? = null,
                val shiftCooldown: Double = 0.0,

                var gyro: AngleSensor? = null,
                var accelLimit: Double = 1e100) {

    val wheelCircumference = wheelDiameter?.times(Math.PI)

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

    var leftTarget = 0.0
    var rightTarget = 0.0
    var lastTime = 0.0

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

    val leftEnc : DistanceSensor = object : DistanceSensor {
        override val distanceMeters: Double
            get() = leftMaster.connectedEncoder.angle * (wheelCircumference ?: 0.0) * 0.3048 / 360.0

        override val velocity: Double
            get() = leftMaster.connectedEncoder.rate * (wheelCircumference ?: 0.0) * 0.3048 / 360.0

        override fun zero() {
            leftMaster.connectedEncoder.zero()
        }
    }

    val rightEnc : DistanceSensor = object : DistanceSensor {
        override val distanceMeters: Double
            get() = rightMaster.connectedEncoder.angle * (wheelCircumference ?: 0.0) * 0.3048 / 360.0

        override val velocity: Double
            get() = rightMaster.connectedEncoder.rate * (wheelCircumference ?: 0.0) * 0.3048 / 360.0

        override fun zero() {
            rightMaster.connectedEncoder.zero()
        }
    }

    init {
        leftMaster.brakeMode = SmartMotor.BrakeMode.BRAKE
        rightMaster.brakeMode = SmartMotor.BrakeMode.BRAKE

        // Set both motors to drive
        for(motor in leftMotors) {
            motor.follow(leftMaster)
            motor.brakeMode = SmartMotor.BrakeMode.BRAKE
        }

        for(motor in rightMotors) {
            motor.follow(rightMaster)
            motor.brakeMode = SmartMotor.BrakeMode.BRAKE
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
            if(highGear) {
                shifter?.retract()
                highGear = false
                refreshPIDConstants()
            }
        }
    }

    fun highGear() {
        if(shifter != null) {
            if(!highGear) {
                shifter?.extend()
                highGear = true
                refreshPIDConstants()
            }
        }
    }

    fun setOpenLoopDrive(left: Double, right: Double) {
        leftMaster.speed = left
        rightMaster.speed = right
    }

    fun setDriveVelocity(left: Double, right: Double, forwardSpeed: Double? = null) {
        if(wheelCircumference != null) {
            leftTarget = left * (360.0 / wheelCircumference)
            rightTarget = right * (360.0 / wheelCircumference)
        } else {
            leftTarget = left
            rightTarget = right
        }

        // Accel limit
        if(Math.abs((leftTarget - leftMaster.PIDsetpoint) / (lastTime - systemTimeSeconds)) > accelLimit) {
            leftMaster.PIDsetpoint = leftMaster.PIDsetpoint + accelLimit * (lastTime - systemTimeSeconds)
        } else {
            leftMaster.PIDsetpoint = leftTarget
        }

        if(Math.abs((rightTarget - rightMaster.PIDsetpoint) / (lastTime - systemTimeSeconds)) > accelLimit) {
            rightMaster.PIDsetpoint = rightMaster.PIDsetpoint + accelLimit * (lastTime - systemTimeSeconds)
        } else {
            rightMaster.PIDsetpoint = rightTarget
        }
        lastTime = systemTimeSeconds

        if(shiftDownSpeed != null && shiftUpSpeed != null && forwardSpeed != null && canAutoShift) {
            autoshiftHelper?.autoshift(forwardSpeed, leftMaster.PIDsetpoint, rightMaster.PIDsetpoint, this::lowGear, this::highGear)
        }
    }

    fun setRawDrive(left: Double, right: Double, forwardSpeed: Double? = null) {
        leftTarget = left
        rightTarget = right

        // Accel limit
        if(Math.abs((leftTarget - leftMaster.speed) / (lastTime - systemTimeSeconds)) > accelLimit) {
            leftMaster.speed = leftMaster.speed + accelLimit * (lastTime - systemTimeSeconds)
        } else {
            leftMaster.speed = leftTarget
        }

        if(Math.abs((rightTarget - rightMaster.speed) / (lastTime - systemTimeSeconds)) > accelLimit) {
            rightMaster.speed = rightMaster.speed + accelLimit * (lastTime - systemTimeSeconds)
        } else {
            rightMaster.speed = rightTarget
        }
        lastTime = systemTimeSeconds

        if(forwardSpeed != null && canAutoShift && closedLoopScaling != null) {
            autoshiftHelper?.autoshift(forwardSpeed, leftMaster.speed * closedLoopScaling, rightMaster.speed * closedLoopScaling, this::lowGear, this::highGear)
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
            setDriveVelocity((forward + turn) * closedLoopScaling, (forward - turn) * closedLoopScaling, forward * closedLoopScaling)
        } else {
            setRawDrive(forward + turn, forward - turn, forward)
        }
    }

    fun scaledArcadeDrive(forward: Double, turn: Double, quickturn: Boolean = false) {
        if(quickturn) {
            arcadeDrive(forward, turn)
        } else {
            if(closedLoopControl && closedLoopReady && closedLoopScaling != null) {
                setDriveVelocity((forward + turn * forward) * closedLoopScaling, (forward - turn * forward) * closedLoopScaling, forward * closedLoopScaling)
            } else {
                setRawDrive(forward + turn * forward, forward - turn * forward, forward)
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

}
