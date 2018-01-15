package org.usfirst.frc.team1458.lib.util.flow

import edu.wpi.first.wpilibj.Timer
import kotlinx.coroutines.experimental.*
import org.usfirst.frc.team1458.lib.util.Logger
import java.util.concurrent.TimeUnit


suspend fun periodic(hz: Int = 20, condition: () -> Boolean = { true }, body: () -> Unit) {
    val period : Double = (1000 / hz).toDouble()
    while (condition()) {
        val start = systemTimeMillis
        body()
        val time = systemTimeMillis - start

        if (time > period) {
            Logger.w("Periodic Looper", "Periodic loop went over expected time. " +
                    "Function execution took $time ms but loop time is only $period ms.")
        }

        delay(period - Math.min(time, period))
    }
}

suspend fun suspendUntil(pollingRate: Int = 10, condition: suspend () -> Boolean) {
    while (!condition()) delay(pollingRate.toLong(), TimeUnit.MILLISECONDS)
}
