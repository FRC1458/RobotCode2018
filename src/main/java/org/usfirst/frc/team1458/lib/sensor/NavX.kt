package org.usfirst.frc.team1458.lib.sensor

import com.kauailabs.navx.frc.AHRS
import edu.wpi.first.wpilibj.I2C
import edu.wpi.first.wpilibj.SPI
import edu.wpi.first.wpilibj.SerialPort
import org.usfirst.frc.team1458.lib.sensor.interfaces.Accelerometer
import org.usfirst.frc.team1458.lib.sensor.interfaces.AngleSensor

class NavX(private val navX: AHRS) {
    private constructor(port: SPI.Port) : this(AHRS(port))
    private constructor(port: I2C.Port) : this(AHRS(port))
    private constructor(port: SerialPort.Port) : this(AHRS(port))

    val pitch = AngleSensor.create { navX.pitch.toDouble() }
    val roll = AngleSensor.create { navX.roll.toDouble() }
    val yaw = AngleSensor.create({ navX.fusedHeading.toDouble() }, navX::getRate)

    val magneticDisturbance
        get() = navX.isMagneticDisturbance

    val isMoving
        get() = navX.isMoving

    val isRotating
        get() = navX.isRotating

    val accel : Accelerometer = Accelerometer.create({ navX.worldLinearAccelX.toDouble() }, { navX.worldLinearAccelY.toDouble() }, { navX.worldLinearAccelZ.toDouble() })

    companion object {
        fun USB() : NavX = NavX(SerialPort.Port.kUSB)
        fun USB1() : NavX = NavX(SerialPort.Port.kUSB1)
        fun USB2() : NavX = NavX(SerialPort.Port.kUSB2)

        fun MXP_Serial() : NavX = NavX(SerialPort.Port.kMXP)
        fun MXP_SPI() : NavX = NavX(SPI.Port.kMXP)
        fun MXP_I2C() : NavX = NavX(I2C.Port.kMXP)

        fun Micro_I2C() : NavX = NavX(I2C.Port.kOnboard)
    }
}