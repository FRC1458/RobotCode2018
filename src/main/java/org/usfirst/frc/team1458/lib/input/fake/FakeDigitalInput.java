package org.usfirst.frc.team1458.lib.input.fake;

import org.usfirst.frc.team1458.lib.input.DigitalInput;

/**
 * Fake DigitalInput which can be set manually
 *
 * @author asinghani
 */
public class FakeDigitalInput implements DigitalInput {
	int value = 0;

	@Override
	public int getValue() {
		return value;
	}

	public FakeDigitalInput set(int value) {
		this.value = value;
		return this;
	}
}
