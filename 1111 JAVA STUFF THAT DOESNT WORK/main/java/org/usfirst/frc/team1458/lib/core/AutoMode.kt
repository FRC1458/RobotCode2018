package org.usfirst.frc.team1458.lib.core

interface AutoMode {
    fun auto()

    val name: String
        get() = "\"" + javaClass.getSimpleName() + "\""
}
