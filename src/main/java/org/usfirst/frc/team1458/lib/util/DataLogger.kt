package org.usfirst.frc.team1458.lib.util

import org.usfirst.frc.team1458.lib.util.maths.TurtleMaths
import java.util.*

object DataLogger {
    private val data: MutableMap<String, MutableList<Pair<Double, Double>>> = HashMap()
    private val timestamps: MutableSet<Double> = HashSet()

    private val currentIterationPlaced: MutableSet<String> = HashSet()

    var currentIterationTimestamp: Double = 0.0
        set(value) {
            field = value
            currentIterationPlaced.clear()
        }

    fun addKey(key: String) {
        data[key] = ArrayList()
    }

    private fun putValue(key: String, value: Double, timestamp: Double) {
        if(!data.containsKey(key)) {
            addKey(key)
        }

        if(currentIterationPlaced.add(key)) {
            data[key]!!.add(Pair(timestamp, value))
            timestamps.add(timestamp)
        } else {
            Logger.w("DataLogger", "Multiple logs for same key $key in single iteration")
        }
    }

    fun endTeleop() {
        for(key in data.keys) {
            if(!currentIterationPlaced.contains(key)) {
                Logger.w("DataLogger", "No logs for key $key in single iteration. Adding previous value")
                data[key]!!.add(Pair(currentIterationTimestamp, data[key]!!.last().second))
            }
        }
    }

    /*fun putValue(key: String, value: Int, timestamp: Double) = putValue(key, value.toDouble(), timestamp)
    fun putValue(key: String, value: Float, timestamp: Double) = putValue(key, value.toDouble(), timestamp)
    fun putValue(key: String, value: Long, timestamp: Double) = putValue(key, value.toDouble(), timestamp)
    fun putValue(key: String, value: Boolean, timestamp: Double) = putValue(key, if(value) { 1.0 } else { 0.0 }, timestamp)*/

    fun putValue(key: String, value: Double) = putValue(key, value, currentIterationTimestamp)
    fun putValue(key: String, value: Int) = putValue(key, value.toDouble())
    fun putValue(key: String, value: Float) = putValue(key, value.toDouble())
    fun putValue(key: String, value: Long) = putValue(key, value.toDouble())
    fun putValue(key: String, value: Boolean) = putValue(key, if(value) { 1.0 } else { 0.0 })

    /**
     * @return a List of Pairs of (timestamp, value)
     */
    fun getTimestampedData(key: String): List<Pair<Double, Double>>? {
        return data[key]
    }

    /**
     * @return a List of values
     */
    fun getData(key: String): List<Double>? {
        return data[key]?.map { it.second }
    }

    fun getAverage(key: String): Double? {
        val data = getData(key)
        return if (data == null) { null } else { data.reduce({ acc, d -> (acc + d) }) / data.size }
    }

    fun getStdDev(key: String): Double? {
        val data = getData(key)
        return if (data == null) { null } else { TurtleMaths.calculateSD(data) }
    }

    fun getCSV(keys: Array<String>): String? {
        return null // TODO finishg
    }
}