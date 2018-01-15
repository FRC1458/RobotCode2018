package org.usfirst.frc.team1458.lib.core

interface AutoMode {
    fun auto()

    val name: String
        get() = "\"" + javaClass.getSimpleName() + "\""

    companion object {
        var autoModeNumber = 0

        fun create(name: String = "AutoMode"+(autoModeNumber++).toString(), func: () -> Unit): AutoMode {
            return object: AutoMode {
                override fun auto() {
                    func()
                }
            }
        }
    }
}
