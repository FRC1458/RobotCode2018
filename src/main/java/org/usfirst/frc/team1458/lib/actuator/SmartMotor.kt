package org.usfirst.frc.team1458.lib.actuator

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.FeedbackDevice
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

    //var currentLimit : Double
    //    set

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
            val timeoutMs = 20

            val talon : TalonSRX = TalonSRX(canID)
            talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, timeoutMs);
            talon.configNominalOutputForward(0.0, timeoutMs);
            talon.configNominalOutputReverse(0.0, timeoutMs);
            talon.configPeakOutputForward(1.0, timeoutMs);
            talon.configPeakOutputReverse(-1.0, timeoutMs);

            return object : SmartMotor {
                override val CANid: Int
                    get() = talon.deviceID
                override val outputVoltage: Double
                    get() = talon.motorOutputVoltage
                override val connectedEncoder: AngleSensor
                    get() = AngleSensor.create({ talon.getSelectedSensorPosition(0).toDouble() },
                            { talon.getSelectedSensorVelocity(0).toDouble() })
                override val isEncoderWorking: Boolean
                    get() = TODO("Impl") //talon
                /**
                 * Temperature of the motor controller in degrees Celsius
                 */
                override val temperature: Double
                    get() = talon.temperature
                override var PIDconstants: PIDConstants = PIDConstants(0.0)
                    get
                    set(value) {
                        field = value
                        talon.config_kP(0, value.kP, timeoutMs)
                        talon.config_kI(0, value.kI, timeoutMs)
                        talon.config_kD(0, value.kD, timeoutMs)
                        talon.config_kF(0, value.kF, timeoutMs)
                    }
                override var PIDsetpoint: Double = 0.0
                    get
                    set(value) {
                        field = value
                        talon.set(ControlMode.Velocity, value); // TODO scale
                    }

                override var brakeMode: BrakeMode
                    get() = TODO("not implemented") 
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
                    get() = TODO("not implemented") 

                override val _talonInstance: IMotorController?
                    get() = talon
            }
        }
    }
}