package org.usfirst.frc.team1458.lib.actuator

import com.ctre.phoenix.motorcontrol.*
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
        fun CANtalonSRX(canID: Int, ticksPerRotation: Double = 1024.0, unitScaling: Boolean = true): SmartMotor {
            val timeoutMs = 20

            val talon : TalonSRX = TalonSRX(canID)
            talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, timeoutMs)
            talon.configNominalOutputForward(0.0, timeoutMs)
            talon.configNominalOutputReverse(0.0, timeoutMs)
            talon.configPeakOutputForward(1.0, timeoutMs)
            talon.configPeakOutputReverse(-1.0, timeoutMs)

            fun talonRotationsToDegrees(talonUnits: Double): Double {
                val revolutions = talonUnits / (4.0 * ticksPerRotation) // 4x encoder ticks are counted per revolution
                val degrees = revolutions * 360.0
                return degrees
            }

            fun degreesToTalonRotations(degrees: Double): Double {
                val revolutions = degrees / 360.0
                val talonUnits = revolutions * (4.0 * ticksPerRotation) // 4x encoder ticks are counted per revolution
                return talonUnits
            }

            fun talonVelocityToDegreesPerSecond(talonUnitsPerDecisecond: Double): Double {
                val degreesPerDecisecond = talonRotationsToDegrees(talonUnitsPerDecisecond)
                val degreesPerSecond = degreesPerDecisecond * 10.0

                return degreesPerSecond
            }

            fun degreesPerSecondToTalonVelocity(degreesPerSecond: Double): Double {
                val degreesPerDecisecond = degreesPerSecond / 10.0
                val talonUnitsPerDecisecond = degreesToTalonRotations(degreesPerDecisecond)
                return talonUnitsPerDecisecond
            }

            return object : SmartMotor {
                override val CANid: Int
                    get() = talon.deviceID

                override val outputVoltage: Double
                    get() = talon.motorOutputVoltage

                // TODO test scaling
                override val connectedEncoder: AngleSensor
                    get() = if(unitScaling) {
                        AngleSensor.create({ talonRotationsToDegrees(talon.getSelectedSensorPosition(0).toDouble()) },
                                { talonVelocityToDegreesPerSecond(talon.getSelectedSensorVelocity(0).toDouble()) })
                    } else {
                        AngleSensor.create({ (talon.getSelectedSensorPosition(0).toDouble()) },
                                { (talon.getSelectedSensorVelocity(0).toDouble()) })
                    }

                override val isEncoderWorking: Boolean
                    get() = if(Math.abs(speed) > 0.25 || Math.abs(PIDsetpoint) > 50) { Math.abs(connectedEncoder.rate) > 0.001 } else { true }

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
                        talon.config_kF(0, value.kF * 1023.0, timeoutMs)
                    }
                override var PIDsetpoint: Double = 0.0
                    get
                    set(value) {
                        field = value
                        if(unitScaling) {
                            talon.set(ControlMode.Velocity, degreesPerSecondToTalonVelocity(value)) // TODO test scaling
                        } else {
                            talon.set(ControlMode.Velocity, (value))
                        }
                    }

                override var brakeMode: BrakeMode = BrakeMode.BRAKE
                    get
                    set(value) {
                        field = value
                        talon.setNeutralMode(when(field) {
                            BrakeMode.BRAKE -> NeutralMode.Brake
                            BrakeMode.COAST -> NeutralMode.Coast
                        })
                    }

                override fun follow(other: SmartMotor) {
                    talon.follow(other._talonInstance)
                }

                override val inverted: SmartMotor
                    get() {
                        talon.inverted = !talon.inverted
                        return this
                    }

                override fun stopFollow() {
                    talon.set(ControlMode.PercentOutput, 0.0)
                }

                override var speed: Double
                    get() = talon.motorOutputPercent
                    set(value) { talon.set(ControlMode.PercentOutput, value) }
                /**
                 * Current draw of this device, in Amps
                 */
                override val currentDraw: Double
                    get() = talon.outputCurrent

                override val _talonInstance: IMotorController?
                    get() = talon
            }
        }
    }
}