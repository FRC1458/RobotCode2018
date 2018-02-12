package org.usfirst.frc.team1458.lib.util

import org.usfirst.frc.team1458.lib.util.flow.go
import java.net.HttpURLConnection
import java.net.URL

object SoundPlayer {
    private val playHTTP = "http://raspberrypi.local:5000/play/"
    private val freqHTTP = "http://raspberrypi.local:5000/freq/"
    private val stopHTTP = "http://raspberrypi.local:5000/stop"

    private var lastPlayed = ""

    fun play(sound: String){
        try {
            val SoundHTTP = playHTTP + sound + "@0"
            lastPlayed = sound
            go {
                val obj = URL(SoundHTTP)
                with(obj.openConnection() as HttpURLConnection){
                    println("\nSending 'GET' request to URL : $url")
                    println("Response Code : $responseCode")
                }
            }
        } catch (e: Throwable) {

        }
    }

    fun freq(freq: Int){
        try {
            val SoundHTTP = freqHTTP + lastPlayed + "@" + freq
            go {
                val obj = URL(SoundHTTP)
                with(obj.openConnection() as HttpURLConnection){
                    println("\nSending 'GET' request to URL : $url")
                    println("Response Code : $responseCode")
                }
            }
        } catch (e: Throwable) {

        }
    }

    fun stop(){
        try {
            go {
                val obj = URL(stopHTTP)
                with(obj.openConnection() as HttpURLConnection){
                    println("\nSending 'GET' request to URL : $url")
                    println("Response Code : $responseCode")
                }
            }
        } catch (e: Throwable) {

        }
    }
}