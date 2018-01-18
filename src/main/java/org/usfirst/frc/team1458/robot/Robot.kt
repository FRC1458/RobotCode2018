package org.usfirst.frc.team1458.robot

import com.ctre.phoenix.motorcontrol.can.TalonSRX
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import org.usfirst.frc.team1458.lib.actuator.SmartMotor
import org.usfirst.frc.team1458.lib.core.AutoMode
import org.usfirst.frc.team1458.lib.core.BaseRobot
import org.usfirst.frc.team1458.lib.drive.TankDrive
import org.usfirst.frc.team1458.lib.input.Gamepad
import org.usfirst.frc.team1458.lib.input.interfaces.Switch
import org.usfirst.frc.team1458.lib.pid.PIDConstants
import org.usfirst.frc.team1458.lib.util.flow.*
import java.util.*
import jaci.pathfinder.Pathfinder
import jaci.pathfinder.Trajectory
import jaci.pathfinder.Waypoint
import jaci.pathfinder.modifiers.TankModifier
import jaci.pathfinder.followers.EncoderFollower
import org.usfirst.frc.team1458.lib.input.FlightStick
import org.usfirst.frc.team1458.lib.sensor.NavX
import java.io.File


class Robot : BaseRobot() {

    var modifier : TankModifier? = null

    // TODO: add encoders and enable closed-loop control and tune PID constants
    val drivetrain : TankDrive =
            TankDrive(SmartMotor.CANtalonSRX(12), SmartMotor.CANtalonSRX(15).inverted,
                    arrayOf(SmartMotor.CANtalonSRX(10),SmartMotor.CANtalonSRX(11)),
                    arrayOf(SmartMotor.CANtalonSRX(13).inverted,SmartMotor.CANtalonSRX(14).inverted),

                    true, 12.57, 6000.0,
                    pidConstantsLowGearLeft = PIDConstants(0.15, kI = 0.001, kD = 0.01, kF = 1.0/6530.5),
                    pidConstantsLowGearRight = PIDConstants(0.15, kI = 0.001, kD = 0.01, kF = 1.0/6877.7))

    val steer : FlightStick = FlightStick.flightStick(0)
    val throttle : FlightStick = FlightStick.flightStick(1)

    val steerAxis = steer.rollAxis
    val throttleAxis = throttle.pitchAxis.scale { it * 0.4 }.inverted
    val quickTurnButton = throttle.trigger

    val navX = NavX.Micro_I2C()

    override fun robotSetup() {

        //talonLeft.PIDconstants = PIDConstants(0.15, kI = 0.001, kD = 0.01, kF = 1.0/6.5305)
        //talonRight.PIDconstants = PIDConstants(0.15, kI = 0.001, kD = 0.01, kF = 1.0/6.8777) // TODO change scaling on FF gain
        //talonLeft.PIDconstants = PIDConstants(0.0, kF = 1.0/6530.5)
        //talonRight.PIDconstants = PIDConstants(0.0, kF = 1.0/6877.7)


        /*val points = arrayOf(
                Waypoint(-4.0, -1.0, Pathfinder.d2r(-45.0)),
                Waypoint(-2.0, -2.0, 0.0),
                Waypoint(0.0, 0.0, 0.0)
        )

        val config = Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, 1.0/25.0, 3.5, 1.5, 10.0)
        val trajectory = Pathfinder.generate(points, config)*/

        //val myFile = File("~/test_.csv")
        //val trajectory = Pathfinder.readFromCSV(myFile)

        //modifier = TankModifier(trajectory).modify(0.5) // TODO add width in meters
        //val file = File("~/path.csv")
        //Pathfinder.writeToCSV(file, trajectory)

        System.out.println("Path Generation Finished")
        System.out.println("turtwig is da best")
    }


    override fun setupAutoModes() {

    }


    override fun threadedSetup() {

    }


    override fun teleopInit() {
        /*dataListRight = ArrayList<Double>(5000)
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
        talonRight.PIDsetpoint = 0.0*/
    }


    override fun teleopPeriodic() {

        //SmartDashboard.putNumber("LeftPos", talonLeft.connectedEncoder.angle)
        //SmartDashboard.putNumber("LeftVel", talonLeft.connectedEncoder.rate)

        //SmartDashboard.putNumber("RightPos", talonRight.connectedEncoder.angle)
        //SmartDashboard.putNumber("RightVel", talonRight.connectedEncoder.rate)





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
        //System.out.println("$toggleThing")
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
        SmartDashboard.putNumber("left",drivetrain.leftMaster.connectedEncoder.angle)
        SmartDashboard.putNumber("right",drivetrain.rightMaster.connectedEncoder.angle)



        SmartDashboard.putNumber("Yaw Angle", navX.yaw.angle)
        SmartDashboard.putNumber("Yaw Rate", navX.yaw.rate)
        SmartDashboard.putNumber("Yaw Heading", navX.yaw.heading)

        SmartDashboard.putNumber("Pitch Angle", navX.pitch.angle)
        SmartDashboard.putNumber("Pitch Rate", navX.pitch.rate)
        SmartDashboard.putNumber("Pitch Heading", navX.pitch.heading)

        SmartDashboard.putNumber("Roll Angle", navX.roll.angle)
        SmartDashboard.putNumber("Roll Rate", navX.roll.rate)
        SmartDashboard.putNumber("Roll Heading", navX.roll.heading)

        SmartDashboard.putBoolean("IsMoving", navX.isMoving)
        SmartDashboard.putBoolean("IsRotating", navX.isRotating)

        drivetrain.arcadeDrive(throttleAxis.value, steerAxis.value)//, quickTurnButton.triggered)
    }


    override fun runTest() {

    }


    override fun robotDisabled() {
        //startTime = -1.0
    }

}