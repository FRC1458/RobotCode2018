package org.usfirst.frc.team1458.lib.input.interfaces

interface POV {
    val direction : Direction
        get

    enum class Direction constructor(val angle: Int?) {
        CENTER(null), NORTH(0), NORTHEAST(45), EAST(90), SOUTHEAST(135), SOUTH(180), SOUTHWEST(225), WEST(270), NORTHWEST(315);

        companion object {

            fun fromAngle(angle: Int?): Direction {
                when (angle) {
                    0 -> return NORTH
                    45 -> return NORTHEAST
                    90 -> return EAST
                    135 -> return SOUTHEAST
                    180 -> return SOUTH
                    225 -> return SOUTHWEST
                    270 -> return WEST
                    315 -> return NORTHWEST
                    else -> return CENTER
                }
            }
        }
    }

    companion object {
        fun create(func: () -> Direction) : POV {
            return object : POV {
                override val direction: Direction
                    get() = func()
            }
        }
    }
}