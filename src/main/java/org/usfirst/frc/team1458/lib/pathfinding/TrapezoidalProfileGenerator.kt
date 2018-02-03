package org.usfirst.frc.team1458.lib.pathfinding

object TrapezoidalProfileGenerator {
    fun generate(startPos: Double, endPos: Double, dt: Double, velLimit: Double, accelLimit: Double): Array<Waypoint1D> {
        val list = ArrayList<Waypoint1D>()

        var dist = endPos - startPos

        if(2 * (velLimit / accelLimit) * velLimit > dist) {


        } else {

        }

        return list.toTypedArray()
    }
}