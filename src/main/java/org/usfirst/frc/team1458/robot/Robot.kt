package org.usfirst.frc.team1458.robot

import com.ctre.phoenix.motorcontrol.can.TalonSRX
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import org.usfirst.frc.team1458.lib.actuator.SmartMotor
import org.usfirst.frc.team1458.lib.core.BaseRobot
import org.usfirst.frc.team1458.lib.input.Gamepad
import org.usfirst.frc.team1458.lib.input.interfaces.Switch
import org.usfirst.frc.team1458.lib.util.flow.*
import java.util.*


class Robot : BaseRobot() {

    // TODO: add encoders and enable closed-loop control and tune PID constants
    /*val drivetrain : TankDrive =
            TankDrive(SmartMotor.CANtalonSRX(12), SmartMotor.CANtalonSRX(15).inverted,
                    arrayOf(SmartMotor.CANtalonSRX(10),SmartMotor.CANtalonSRX(11)),
                    arrayOf(SmartMotor.CANtalonSRX(13).inverted,SmartMotor.CANtalonSRX(14).inverted),
                    false, 12.0, 5.0, PIDConstants(0.01, 0.0, 0.0, 0.5))
    */
    val xboxController : Gamepad = Gamepad.xboxController(3)

    var startTime: Double = -1.0
    var dataListRight = ArrayList<Double>(5000)
    var dataListLeft = ArrayList<Double>(5000)

    val talonLeft = SmartMotor.CANtalonSRX(12)
    val talonRight = SmartMotor.CANtalonSRX(15).inverted

    val toggleThing = Switch.toggleSwitch(xboxController.getButton(Gamepad.Button.A))

    override fun robotSetup() {
        SmartMotor.CANtalonSRX(10).follow(talonLeft)
        SmartMotor.CANtalonSRX(11).follow(talonLeft)

        SmartMotor.CANtalonSRX(13).inverted.follow(talonRight)
        SmartMotor.CANtalonSRX(14).inverted.follow(talonRight)

        //talonLeft.PIDconstants = PIDConstants(0.15, kI = 0.001, kD = 0.01, kF = 1.0/6.5305)
        //talonRight.PIDconstants = PIDConstants(0.15, kI = 0.001, kD = 0.01, kF = 1.0/6.8777) // TODO change scaling on FF gain
        //talonLeft.PIDconstants = PIDConstants(0.0, kF = 1.0/6530.5)
        //talonRight.PIDconstants = PIDConstants(0.0, kF = 1.0/6877.7)

    }


    override fun setupAutoModes() {

    }


    override fun threadedSetup() {

    }


    override fun teleopInit() {
        dataListRight = ArrayList<Double>(5000)
        dataListLeft = ArrayList<Double>(5000)

        var i = 0.0
        while(i < 3000.0) {
            i += 100.0
            delay(30)
            talonLeft.PIDsetpoint = i
            talonRight.PIDsetpoint = i
        }

        talonLeft.PIDsetpoint = 3000.0
        talonRight.PIDsetpoint = 3000.0

        delay(3200)

        i = 3000.0
        while(i > 0) {
            i -= 100.0
            delay(30)
            talonLeft.PIDsetpoint = i
            talonRight.PIDsetpoint = i
        }

        talonLeft.PIDsetpoint = 0.0
        talonRight.PIDsetpoint = 0.0

        delay(2000)

        i = 0.0
        while(i < 3000.0) {
            i += 100.0
            delay(30)
            talonLeft.PIDsetpoint = -i
            talonRight.PIDsetpoint = -i
        }

        talonLeft.PIDsetpoint = -3000.0
        talonRight.PIDsetpoint = -3000.0

        delay(3200)

        i = 3000.0
        while(i > 0) {
            i -= 100.0
            delay(30)
            talonLeft.PIDsetpoint = -i
            talonRight.PIDsetpoint = -i
        }

        talonLeft.PIDsetpoint = 0.0
        talonRight.PIDsetpoint = 0.0
    }


    override fun teleopPeriodic() {

        SmartDashboard.putNumber("LeftPos", talonLeft.connectedEncoder.angle)
        SmartDashboard.putNumber("LeftVel", talonLeft.connectedEncoder.rate)

        SmartDashboard.putNumber("RightPos", talonRight.connectedEncoder.angle)
        SmartDashboard.putNumber("RightVel", talonRight.connectedEncoder.rate)

        //talonLeft.speed = 0.5
        //talonRight.speed = 0.5
        /*if(toggleThing.triggered) {
            talonLeft.speed = (TurtleMaths.deadband(xboxController.leftY.inverted.value) + TurtleMaths.deadband(xboxController.rightX.value))
            talonRight.speed = (TurtleMaths.deadband(xboxController.leftY.inverted.value) - TurtleMaths.deadband(xboxController.rightX.value))
        } else {
            talonLeft.PIDsetpoint = Math.min((TurtleMaths.deadband(xboxController.leftY.inverted.value) + TurtleMaths.deadband(xboxController.rightX.value)) * 5000.0, 5000.0)
            talonRight.PIDsetpoint = Math.min((TurtleMaths.deadband(xboxController.leftY.inverted.value) - TurtleMaths.deadband(xboxController.rightX.value)) * 5000.0, 5000.0)
        }*/



        //System.out.println("${systemTimeMillis.toLong()},${talonLeft.PIDsetpoint},${talonLeft.connectedEncoder.rate},${talonRight.PIDsetpoint},${talonRight.connectedEncoder.rate},${talonLeft._talonInstance?.getClosedLoopError(0)},${talonRight._talonInstance?.getClosedLoopError(0)}")
        System.out.println("$toggleThing")
        /*if(xboxController.getButton(Gamepad.Button.LBUMP).triggered) {
            drivetrain.arcadeDrive(xboxController.leftY.value, xboxController.rightX.value)
            return
        }*/
        //drivetrain.arcadeDrive(xboxController.leftY.value, xboxController.rightX.value)
        /*drivetrain.scaledArcadeDrive(xboxController.leftY.value, xboxController.rightX.value,
                xboxController.getButton(Gamepad.Button.LBUMP).triggered, { 0.6 * Math.pow(it, 0.5) + 0.2 })*/

        //System.out.println()

        /*if(startTime == -1.0) {
            startTime = systemTimeMillis
        }
        val speed = 0.5
        if(systemTimeMillis - startTime < 4000.0){
            talonLeft.PIDsetpoint = 2000.0
            talonRight.PIDsetpoint = 2000.0
            //System.out.println("${(systemTimeMillis - startTime).toLong()}," +
            //        "${drivetrain.leftMaster.connectedEncoder.angle},${drivetrain.leftMaster.connectedEncoder.rate}," +
            //        "${drivetrain.rightMaster.connectedEncoder.angle},${drivetrain.rightMaster.connectedEncoder.rate}")
        }
        else {
            talonLeft.PIDsetpoint = 0.0
            talonRight.PIDsetpoint = 0.0
        }*/


        /**
         * On Ground 50% Speed
         * [70.72] Avg Left:  3030.286666666667,  StdDev Left: 107.8075035524997
           [70.72] Avg Right: 3156.7933333333335, StdDev Right: 90.28084305223463

         * On Blocks 50% Speed
         * [272.08] Avg Left:  3079.4133333333334,  StdDev Left: 17.38052038602092
           [272.08] Avg Right: 3314.286666666667, StdDev Right: 12.015732279899654

         * [339.32] Avg Left:  3049.8866666666668,  StdDev Left: 14.606636239128502
           [339.32] Avg Right: 3317.9, StdDev Right: 12.644761761298627

         */

        //System.out.println(systemTimeMillis - startTime)

        // TODO - make a utility class for average and standard deviation
        /*if(systemTimeMillis - startTime > 4000.0 && systemTimeMillis - startTime < 4100.0) {
            var totalLeft : Double = 0.0
            for(data in dataListLeft) {
                totalLeft += data
            }
            var avgLeft = totalLeft / dataListLeft.size

            var totalRight : Double = 0.0
            for(data in dataListRight) {
                totalRight += data
            }
            var avgRight = totalRight / dataListRight.size

            var stdDevLeft = TurtleMaths.calculateSD(dataListLeft)
            var stdDevRight = TurtleMaths.calculateSD(dataListRight)

            System.out.println("Avg Left:  $avgLeft,  StdDev Left: $stdDevLeft")
            System.out.println("Avg Right: $avgRight, StdDev Right: $stdDevRight")

        } else if(systemTimeMillis - startTime > 1000.0) {
            dataListRight.add((talonRight._talonInstance?.getClosedLoopError(0) ?: 0).toDouble())
            dataListLeft.add((talonLeft._talonInstance?.getClosedLoopError(0) ?: 0).toDouble())
        }*/



    }


    override fun runTest() {

    }


    override fun robotDisabled() {
        startTime = -1.0
    }

}