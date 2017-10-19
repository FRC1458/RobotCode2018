package org.usfirst.frc.team1458.lib.core;

import java.util.ArrayList;

/**
 * Allows switching between multiple AutoModes
 *
 * @author asinghani
 */
public interface AutoModeHolder {
	ArrayList<? extends AutoMode> getAutoModes();

	void setSelectedAutoModeIndex(int index);

	int getSelectedAutoModeIndex();
}