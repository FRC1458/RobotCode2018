package org.usfirst.frc.team1458.lib.util

import org.usfirst.frc.team1458.lib.util.flow.systemTimeMillis
import java.io.BufferedWriter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object Logger {
    private val destinations : ArrayList<Destination> = ArrayList()

    fun verbose(tag: String, message: String) {
        Logger.println(Severity.VERBOSE, tag, message)
    }

    fun v(tag: String, message: String) {
        Logger.println(Severity.VERBOSE, tag, message)
    }

    fun info(tag: String, message: String) {
        Logger.println(Severity.INFO, tag, message)
    }

    fun i(tag: String, message: String) {
        Logger.println(Severity.INFO, tag, message)
    }

    fun debug(tag: String, message: String) {
        Logger.println(Severity.DEBUG, tag, message)
    }

    fun d(tag: String, message: String) {
        Logger.println(Severity.DEBUG, tag, message)
    }

    fun warn(tag: String, message: String) {
        Logger.println(Severity.WARN, tag, message)
    }

    fun w(tag: String, message: String) {
        Logger.println(Severity.WARN, tag, message)
    }

    fun error(tag: String, message: String) {
        Logger.println(Severity.ERROR, tag, message)
    }

    fun e(tag: String, message: String) {
        Logger.println(Severity.ERROR, tag, message)
    }

    fun println(severity: Severity, tag: String, message: String) {
        for (dest in destinations) {
            dest.log(severity, systemTimeMillis, tag, message)
        }
    }

    fun addDestination(destination: Destination) {
        destinations.add(destination)
    }

    enum class Severity(val value : Int) {
        VERBOSE(0), INFO(1), DEBUG(2), WARN(3), ERROR(4)
    }

    interface Destination {
        fun log(severity: Severity, timestamp: Double, tag: String, message: String)

        class File : Destination {
            private val format: Format
            private val logFile: BufferedWriter
            private val minSeverity: Severity
            private val color: Boolean

            private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")

            constructor(filePath: String, format: Format, minSeverity: Severity = Severity.VERBOSE, color: Boolean) {
                this.format = format
                this.logFile = java.io.File(filePath).bufferedWriter()
                this.minSeverity = minSeverity
                this.color = color
            }

            override fun log(severity: Severity, timestamp: Double, tag: String, message: String) {
                if (severity.value < minSeverity.value) return

                var line = ""
                when (format) {
                    Format.HUMANREADABLE -> line = "[${formatter.format(Date(timestamp.toLong()))}] [${severity.toString()}] $tag: $message\n"
                    Format.JSON -> line = "{timestamp: ${timestamp.toLong()}, severity: \"${severity.toString()}\", tag: \"${tag.replace("\"", "\\\"")}\", message:\"${message.replace("\"", "\\\"")}\"},"
                    Format.CSV -> line = "${timestamp.toLong()},${severity.toString()},${tag.replace(",", "\\,")},${message.replace(",", "\\,")}\n"
                }

                if(color && format == Format.HUMANREADABLE) {
                    line = getColorString(severity) + line + "\u001B[0m"
                }

                this.logFile.write(line)
                this.logFile.flush()
            }
        }
        class Console : Destination {
            private val format : Format
            private val minSeverity : Severity
            private val color: Boolean

            private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")

            constructor(format: Format, minSeverity: Severity = Severity.VERBOSE, color: Boolean) {
                this.format = format
                this.minSeverity = minSeverity
                this.color = color
            }

            override fun log(severity: Severity, timestamp: Double, tag: String, message: String) {
                if(severity.value < minSeverity.value) return

                var line = ""
                when(format) {
                    Format.HUMANREADABLE -> line = "[${formatter.format(Date(timestamp.toLong()))}] [${severity.toString()}] $tag: $message\n"
                    Format.JSON -> line = "{timestamp: ${timestamp.toLong()}, severity: \"${severity.toString()}\", tag: \"${tag.replace("\"", "\\\"")}\", message:\"${message.replace("\"", "\\\"")}\"},"
                    Format.CSV -> line = "${timestamp.toLong()},${severity.toString()},${tag.replace(",", "\\,")},${message.replace(",", "\\,")}\n"
                }

                if(color && format == Format.HUMANREADABLE) {
                    line = getColorString(severity) + line + "\u001B[0m"
                }

                System.out.print(line)
            }
        }

        enum class Format {
            JSON, CSV, HUMANREADABLE
        }
    }
}

fun getColorString(severity: Logger.Severity): String {
    when (severity) {
        Logger.Severity.VERBOSE -> return "\u001B[97m" // White
        Logger.Severity.DEBUG -> return "\u001B[32m" // Green
        Logger.Severity.INFO -> return "\u001B[36m" // Light Blue
        Logger.Severity.WARN -> return "\u001B[93m" // Light Yellow
        Logger.Severity.ERROR -> return "\u001B[31m" // Red
        else -> return "\u001B[39m" // Default
    }
}