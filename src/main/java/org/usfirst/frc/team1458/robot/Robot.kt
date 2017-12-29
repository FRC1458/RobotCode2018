package org.usfirst.frc.team1458.robot

import kotlinx.coroutines.experimental.runBlocking
import org.usfirst.frc.team1458.lib.sensor.interfaces.DistanceSensor
import org.usfirst.frc.team1458.lib.util.flow.*


class Robot {
    companion object {
        fun main(args : Array<String>) {
            /*Logger.addDestination(Logger.Destination.Console(Logger.Destination.Format.HUMANREADABLE, color = true))
            Logger.addDestination(Logger.Destination.File("file1.txt", Logger.Destination.Format.CSV, color = false, minSeverity = Logger.Severity.WARN))
            Logger.addDestination(Logger.Destination.File("file2.txt", Logger.Destination.Format.HUMANREADABLE, color = true))

            go { periodic (2) { Logger.w("DATA", "DATA") } }

            runBlocking { suspendUntil { false } }*/

            runBlocking {
                var a = 0.0
                var sensor : DistanceSensor = DistanceSensor.create { a }
                for(i in 1..100) {
                    for (j in 1..10) {
                        a += 1.0
                        delay(5)
                        println(sensor.velocity)
                    }
                }
            }
        }
    }
}