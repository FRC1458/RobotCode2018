package org.usfirst.frc.team1458.lib.core;

/**
 * Base class for autonomous mode. This is an abstract
 * implementation of AutoMode with some utility functions, but it must be
 * extended to be used.
 *
 * @author asinghani
 */
public abstract class BaseAutoMode implements AutoMode {

	@Override
	public String getName() {
		return "\"" + getClass().getSimpleName() + "\"";
	}

	@Override
	public String toString() {
		return getName();
	}

	/**
	 * The {@link #auto()} function must be implemented by the class extending
	 * SampleAutoMode
	 */
	@Override
	public abstract void auto();
}