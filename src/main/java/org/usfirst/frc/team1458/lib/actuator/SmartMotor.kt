package org.usfirst.frc.team1458.lib.actuator

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.IMotorController
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import org.usfirst.frc.team1458.lib.pid.PIDConstants
import org.usfirst.frc.team1458.lib.sensor.interfaces.AngleSensor
import org.usfirst.frc.team1458.lib.sensor.interfaces.PowerMeasurable


interface SmartMotor : Motor, PowerMeasurable {

    val CANid : Int
        get

    val outputVoltage : Double
        get

    var currentLimit : Double
        set

    val connectedEncoder : AngleSensor
        get

    val isEncoderWorking : Boolean
        get

    /**
     * Temperature of the motor controller in degrees Celsius
     */
    val temperature : Double
        get

    var PIDconstants : PIDConstants
        set
        get

    var PIDsetpoint : Double
        set
        get

    var PIDenabled : Boolean
        set
        get

    var brakeMode : BrakeMode
        set
        get

    override val inverted: SmartMotor
        get

    val _talonInstance : IMotorController?

    fun follow(other: SmartMotor)
    fun stopFollow()

    enum class BrakeMode(val brake: Boolean) {
        BRAKE(true), COAST(false)
    }

    companion object {
        // TODO: talon SRX create method, non-talon SRX create (with custom PID)

        fun CANtalonSRX(canID: Int): SmartMotor {
            val talon : TalonSRX = TalonSRX(canID)

            return object : SmartMotor {
                override val CANid: Int
                    get() = talon.deviceID
                override val outputVoltage: Double
                    get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
                override var currentLimit: Double
                    get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
                    set(value) {}
                override val connectedEncoder: AngleSensor
                    get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
                override val isEncoderWorking: Boolean
                    get() = false
                /**
                 * Temperature of the motor controller in degrees Celsius
                 */
                override val temperature: Double
                    get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
                override var PIDconstants: PIDConstants
                    get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
                    set(value) {}
                override var PIDsetpoint: Double
                    get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
                    set(value) {}
                override var PIDenabled: Boolean
                    get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
                    set(value) {}
                override var brakeMode: BrakeMode
                    get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
                    set(value) {}

                override fun follow(other: SmartMotor) {
                    talon.follow(other._talonInstance)
                }

                override val inverted: SmartMotor
                    get() {
                        talon.inverted = !talon.inverted
                        return this
                    }

                override fun stopFollow() {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override var speed: Double
                    get() = talon.motorOutputPercent
                    set(value) { talon.set(ControlMode.PercentOutput, value) }
                /**
                 * Current draw of this device, in Amps
                 */
                override val currentDraw: Double
                    get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

                override val _talonInstance: IMotorController?
                    get() = talon
            }
        }
    }
}