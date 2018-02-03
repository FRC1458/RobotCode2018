package org.usfirst.frc.team1458.lib.util

import java.net.HttpURLConnection
import java.net.URL

object SoundPlayer {
    private val playHTTP = "http://raspberrypi.local:5000/play/"
    private val stopHTTP = "http://raspberrypi.local:5000/stop"

    fun play(sound: String){
        val SoundHTTP = playHTTP + sound + ".wav"
        val obj = URL(SoundHTTP)
        with(obj.openConnection() as HttpURLConnection){
            println("\nSending 'GET' request to URL : $url")
            println("Response Code : $responseCode")
        }
    }

    fun stop(){
        val obj = URL(stopHTTP)
        with(obj.openConnection() as HttpURLConnection){
            println("\nSending 'GET' request to URL : $url")
            println("Response Code : $responseCode")
        }
    }
}