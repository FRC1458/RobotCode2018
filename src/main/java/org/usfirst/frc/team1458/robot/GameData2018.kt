package org.usfirst.frc.team1458.robot

import edu.wpi.first.wpilibj.DriverStation

object GameData2018 {
    enum class Side {
        LEFT, RIGHT, ERROR
    }

    fun getGameDataString(): String? = DriverStation.getInstance().getGameSpecificMessage()

    fun getOwnSwitch(): Side {
        if(getGameDataString() == null || getGameDataString()!!.length < 3) return Side.ERROR
        return if (getGameDataString()!!.toUpperCase()[0] == 'L') { Side.LEFT } else { Side.RIGHT }
    }

    fun getScale(): Side {
        if(getGameDataString() == null || getGameDataString()!!.length < 3) return Side.ERROR
        return if (getGameDataString()!!.toUpperCase()[1] == 'L') { Side.LEFT } else { Side.RIGHT }
    }

    fun getOpponentSwitch(): Side {
        if(getGameDataString() == null || getGameDataString()!!.length < 3) return Side.ERROR
        return if (getGameDataString()!!.toUpperCase()[2] == 'L') { Side.LEFT } else { Side.RIGHT }
    }
}