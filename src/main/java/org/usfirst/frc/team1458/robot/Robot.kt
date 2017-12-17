package org.usfirst.frc.team1458.robot

import org.usfirst.frc.team1458.lib.util.flow.delay
import org.usfirst.frc.team1458.lib.util.flow.systemTimeMillis
import java.util.*

/**
 * TODO: Add Comment
 *
 * @author asinghani
 */

class Robot {
    companion object {
        fun main(args : Array<String>) {
            while (true) {
                var lastStartMillis = systemTimeMillis

                //code
                System.out.println("Starting " + (systemTimeMillis % 1000000))
                var d = Random().nextDouble()*50
                System.out.println("Waiting " + d)
                delay(d)
                System.out.println("Ending " + (systemTimeMillis % 1000000))
                System.out.println()


                //code

                var lastEndMillis = systemTimeMillis

                var nextStartMillis : Double = lastStartMillis

                while(nextStartMillis < lastEndMillis || (nextStartMillis.toLong() % 20) != 0L) {
                    nextStartMillis += 1
                }

                while(systemTimeMillis < nextStartMillis) {
                    delay(1)
                }
            }
        }
    }
}