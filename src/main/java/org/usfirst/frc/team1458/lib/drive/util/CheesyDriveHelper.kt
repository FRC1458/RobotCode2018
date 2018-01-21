package org.usfirst.frc.team1458.lib.drive.util

import org.usfirst.frc.team1458.lib.util.maths.TurtleMaths

/**
 * Utility class for cheesy drive - Converted from https://github.com/Team254/FRC-2017-Public/blob/master/src/com/team254/lib/util/CheesyDriveHelper.java
 */
object CheesyDriveHelper {

    private val kThrottleDeadband = 0.02
    private val kWheelDeadband = 0.02

    private val kHighWheelNonLinearity = 0.65
    private val kLowWheelNonLinearity = 0.5

    private val kHighNegInertiaScalar = 4.0

    private val kLowNegInertiaThreshold = 0.65
    private val kLowNegInertiaTurnScalar = 3.5
    private val kLowNegInertiaCloseScalar = 4.0
    private val kLowNegInertiaFarScalar = 5.0

    private val kHighSensitivity = 0.95
    private val kLowSensitiity = 1.3

    private val kQuickStopDeadband = 0.2
    private val kQuickStopWeight = 0.1
    private val kQuickStopScalar = 5.0

    private var mOldWheel = 0.0
    private var mQuickStopAccumlator = 0.0
    private var mNegInertiaAccumlator = 0.0

    fun cheesyDrive(forward: Double, wheel: Double, quickturn: Boolean, isHighGear: Boolean): Pair<Double, Double> {
        var throttle = forward
        var wheel = wheel

        wheel = handleDeadband(wheel, kWheelDeadband)
        throttle = handleDeadband(throttle, kThrottleDeadband)

        val negInertia = wheel - mOldWheel
        mOldWheel = wheel

        val wheelNonLinearity: Double
        if (isHighGear) {
            wheelNonLinearity = kHighWheelNonLinearity
            val denominator = Math.sin(Math.PI / 2.0 * wheelNonLinearity)
            // Apply a sin function that's scaled to make it feel better.
            wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / denominator
            wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / denominator
        } else {
            wheelNonLinearity = kLowWheelNonLinearity
            val denominator = Math.sin(Math.PI / 2.0 * wheelNonLinearity)
            // Apply a sin function that's scaled to make it feel better.
            wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / denominator
            wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / denominator
            wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / denominator
        }

        var leftPwm: Double
        var rightPwm: Double
        val overPower: Double
        val sensitivity: Double

        val angularPower: Double
        val linearPower: Double

        // Negative inertia!
        val negInertiaScalar: Double
        if (isHighGear) {
            negInertiaScalar = kHighNegInertiaScalar
            sensitivity = kHighSensitivity
        } else {
            if (wheel * negInertia > 0) {
                // If we are moving away from 0.0, aka, trying to get more wheel.
                negInertiaScalar = kLowNegInertiaTurnScalar
            } else {
                // Otherwise, we are attempting to go back to 0.0.
                if (Math.abs(wheel) > kLowNegInertiaThreshold) {
                    negInertiaScalar = kLowNegInertiaFarScalar
                } else {
                    negInertiaScalar = kLowNegInertiaCloseScalar
                }
            }
            sensitivity = kLowSensitiity
        }
        val negInertiaPower = negInertia * negInertiaScalar
        mNegInertiaAccumlator += negInertiaPower

        wheel = wheel + mNegInertiaAccumlator
        if (mNegInertiaAccumlator > 1) {
            mNegInertiaAccumlator -= 1.0
        } else if (mNegInertiaAccumlator < -1) {
            mNegInertiaAccumlator += 1.0
        } else {
            mNegInertiaAccumlator = 0.0
        }
        linearPower = throttle

        // Quickturn!
        if (quickturn) {
            if (Math.abs(linearPower) < kQuickStopDeadband) {
                val alpha = kQuickStopWeight
                mQuickStopAccumlator = (1 - alpha) * mQuickStopAccumlator + alpha * TurtleMaths.constrain(wheel, -1.0, 1.0) * kQuickStopScalar
            }
            overPower = 1.0
            angularPower = wheel
        } else {
            overPower = 0.0
            angularPower = Math.abs(throttle) * wheel * sensitivity - mQuickStopAccumlator
            if (mQuickStopAccumlator > 1) {
                mQuickStopAccumlator -= 1.0
            } else if (mQuickStopAccumlator < -1) {
                mQuickStopAccumlator += 1.0
            } else {
                mQuickStopAccumlator = 0.0
            }
        }

        leftPwm = linearPower
        rightPwm = leftPwm
        leftPwm += angularPower
        rightPwm -= angularPower

        if (leftPwm > 1.0) {
            rightPwm -= overPower * (leftPwm - 1.0)
            leftPwm = 1.0
        } else if (rightPwm > 1.0) {
            leftPwm -= overPower * (rightPwm - 1.0)
            rightPwm = 1.0
        } else if (leftPwm < -1.0) {
            rightPwm += overPower * (-1.0 - leftPwm)
            leftPwm = -1.0
        } else if (rightPwm < -1.0) {
            leftPwm += overPower * (-1.0 - rightPwm)
            rightPwm = -1.0
        }
        System.out.println("leftpwm = $leftPwm, rightpwm = $rightPwm")
        return Pair(leftPwm, rightPwm)
    }

    private fun handleDeadband(`val`: Double, deadband: Double): Double {
        return if (Math.abs(`val`) > Math.abs(deadband)) `val` else 0.0
    }
}