package org.usfirst.frc.team1458.lib.drive.util

import org.usfirst.frc.team1458.lib.input.interfaces.Fuse
import org.usfirst.frc.team1458.lib.util.Debouncer

/**
 * Manages auto-shifter for drivetrain
 *
 * Based on FRC449's implementation (see https://github.com/blair-robot-project/449-central-repo/blob/master/RoboRIO/src/main/java/org/usfirst/frc/team449/robot/components/AutoshiftComponent.java)
 *
 * @param upshiftSpeed Speed threshold for shifting up
 * @param downshiftSpeed Speed threshold for shifting down
 * @param shiftCooldown Cooldown between shifts in seconds. Zero by default
 */
class AutoshiftHelper(val upshiftSpeed: Double, val downshiftSpeed: Double, val shiftCooldown: Double = 0.0) {
    val upshiftCooldown = Fuse.autoResetting(shiftCooldown * 1000)
    val downshiftCooldown = Fuse.autoResetting(shiftCooldown * 1000)

    val upshiftDebouncer = Debouncer(500.0)
    val downshiftDebouncer = Debouncer(500.0)

    var highGear = false

    fun shouldDownshift(forward: Double, left: Double, right: Double): Boolean {
        var shouldShift = Math.max(Math.abs(left), Math.abs(right)) < downshiftSpeed
        shouldShift = shouldShift || (Math.abs(forward) < 0.1)
        shouldShift = shouldShift && (!downshiftCooldown.triggered)
        shouldShift = downshiftDebouncer.get(shouldShift)

        return shouldShift
    }

    fun shouldUpshift(forward: Double, left: Double, right: Double): Boolean {
        var shouldShift = Math.max(Math.abs(left), Math.abs(right)) > upshiftSpeed
        shouldShift = shouldShift && forward >= 0.1
        shouldShift = shouldShift && (!upshiftCooldown.triggered)
        shouldShift = upshiftDebouncer.get(shouldShift)

        return shouldShift
    }

    fun autoshift(forward: Double, left: Double, right: Double, downshiftFunc: () -> Unit, upshiftFunc: () -> Unit) {
        if(shouldDownshift(forward, left, right) && highGear) {
            downshiftFunc()
            upshiftCooldown.trigger() // Cannot upshift until cooldown ends
        } else if(shouldUpshift(forward, left, right) && !highGear) {
            upshiftFunc()
            downshiftCooldown.trigger() // Cannot downshift until cooldown ends
        }
    }
}