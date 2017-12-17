package org.usfirst.frc.team1458.lib.core

import java.util.ArrayList

/**
 * Allows switching between multiple AutoModes
 *
 * @author asinghani
 */
interface AutoModeHolder {
    val autoModes: ArrayList<AutoMode>
        get

    var selectedAutoModeIndex: Int

    val selectedAutoMode: AutoMode
        get() = autoModes[selectedAutoModeIndex]
}