package org.usfirst.frc.team1458.lib.util

import com.sun.xml.internal.fastinfoset.util.StringArray
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import org.usfirst.frc.team1458.lib.util.flow.go
import org.usfirst.frc.team1458.lib.util.flow.periodic
import org.usfirst.frc.team1458.lib.util.flow.systemTimeMillis
import org.usfirst.frc.team1458.lib.util.maths.TurtleMaths
import org.usfirst.frc.team1458.lib.util.maths.format
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.*

object TelemetryLogger {
    var header: String = ""
    var data: String = ""

    var logKeys: Array<out String>? = null
    var currentIterationData: HashMap<String, String> = HashMap()

    private var fileWriter : BufferedWriter? = null
    var iteration = 0

    fun newFilename() : String {
        val logNumber = File("/home/lvuser/logNumber.txt").readText().trim().toInt() + 1
        File("/home/lvuser/logNumber.txt").writeText(logNumber.toString())

        return "/home/lvuser/logs/Log$logNumber.csv"
    }


    fun setup(keys: Array<String>) {
        val filename = newFilename()
        val _keys = arrayOf("timestamp").plus(keys)
        header = _keys.reduce({ acc, key -> acc + "," + key}).removeSuffix(",") + "\n"
        logKeys = _keys

        fileWriter = BufferedWriter(FileWriter(filename, false))
        fileWriter?.write(header)
        fileWriter?.flush()
    }

    fun startIteration() {
        currentIterationData.clear()
        currentIterationData["timestamp"] = systemTimeMillis.toString()
    }

    fun putValue(key: String, value: String) {
        currentIterationData[key] = value
    }

    fun putValue(key: String, value: Double) = putValue(key, value.format(2))
    fun putValue(key: String, value: Int) = putValue(key, value.toString())
    fun putValue(key: String, value: Float) = putValue(key, value.format(2))
    fun putValue(key: String, value: Long) = putValue(key, value.toString())
    fun putValue(key: String, value: Boolean) = putValue(key, value.toString())

    fun endIteration() {
        var line = logKeys?.map { key -> currentIterationData[key] }?.
                reduce({ acc, value -> acc + "," + value }) + "\n"
        data += line

        //System.out.println(line)
        fileWriter?.write(line)
        fileWriter?.flush()


        iteration++
    }

}