package org.usfirst.frc.team1458.lib.core

/**
 * Base class for autonomous mode. This is an abstract
 * implementation of AutoMode with some utility functions, but it must be
 * extended to be used.
 *
 * @author asinghani
 */
abstract class BaseAutoMode : AutoMode {

    override val name: String
        get() = "\"" + javaClass.simpleName + "\""

    override fun toString(): String {
        return name
    }

    /**
     * The [.auto] function must be implemented by the class extending
     * SampleAutoMode
     */
    abstract override fun auto()
}