package org.usfirst.frc.team1458.lib.core;

public interface AutoMode {
	void auto();

	default String getName() {
		return "\"" + getClass().getSimpleName() + "\"";
	}
}
