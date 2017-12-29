package org.usfirst.frc.team1458.lib.sensor

import com.kauailabs.navx.frc.AHRS
import edu.wpi.first.wpilibj.I2C
import edu.wpi.first.wpilibj.SPI
import edu.wpi.first.wpilibj.SerialPort

class NavX {
    private var navX : AHRS

    private constructor(port: SPI.Port) {
        navX = AHRS(port)
    }
    private constructor(port: I2C.Port) {
        navX = AHRS(port)
    }
    private constructor(port: SerialPort.Port) {
        navX = AHRS(port)
    }

    // TODO Implement all functions

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