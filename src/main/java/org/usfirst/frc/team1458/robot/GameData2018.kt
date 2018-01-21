package org.usfirst.frc.team1458.robot

import edu.wpi.first.wpilibj.DriverStation

object GameData2018 {
    enum class Side {
        LEFT, RIGHT
    }

    fun getGameDataString(): String = DriverStation.getInstance().getGameSpecificMessage().toUpperCase()

    fun getOwnSwitch(): Side {
        return if (getGameDataString()[0] == 'L') { Side.LEFT } else { Side.RIGHT }
    }

    fun getScale(): Side {
        return if (getGameDataString()[1] == 'L') { Side.LEFT } else { Side.RIGHT }
    }

    fun getOpponentSwitch(): Side {
        return if (getGameDataString()[2] == 'L') { Side.LEFT } else { Side.RIGHT }
    }
}