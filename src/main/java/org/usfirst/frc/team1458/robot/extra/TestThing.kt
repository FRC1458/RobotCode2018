package org.usfirst.frc.team1458.robot.extra

import org.usfirst.frc.team1458.lib.util.DataLogger
import org.usfirst.frc.team1458.lib.util.maths.kinematics.Translation2D
import org.zeromq.ZMQ
import zmq.ZMQ.term
import org.zeromq.ZMQ.REQ
import zmq.ZMQ.socket
import org.zeromq.ZMQ.context



class TestThing {
    companion object {
        fun main(args: Array<String>) {
            println("test")
            val context = ZMQ.context(1)

            //  Socket to talk to server
            println("Connecting to hello world server")

            val socket = context.socket(ZMQ.REQ)
            socket.connect("tcp://localhost:5555")

            for (requestNbr in 0..9) {
                val request = "Hello$requestNbr"
                println("Sending Hello " + requestNbr)
                socket.send(request.toByteArray(ZMQ.CHARSET), 0)

                val reply = socket.recv(0)
                println("Received " + String(reply, ZMQ.CHARSET) + " " + requestNbr)
            }


            socket.close()
            context.term()
        }
    }
}