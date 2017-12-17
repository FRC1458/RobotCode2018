package org.usfirst.frc.team1458.lib.input.fake;

import org.usfirst.frc.team1458.lib.input.Switch;

/**
 * Fake Switch which can be toggled manually
 *
 * @author asinghani
 */
public class FakeSwitch implements Switch {
	boolean value = false;

	@Override
	public boolean get() {
		return value;
	}

	public FakeSwitch set(boolean value) {
		this.value = value;
		return this;
	}
}
