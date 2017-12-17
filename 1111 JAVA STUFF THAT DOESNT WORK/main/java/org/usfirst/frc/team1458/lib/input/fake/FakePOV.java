package org.usfirst.frc.team1458.lib.input.fake;

import org.usfirst.frc.team1458.lib.input.POV;

/**
 * Fake POV Switch which can be toggled manually
 *
 * @author asinghani
 */
public class FakePOV implements POV {
	Direction value = Direction.CENTER;

	/**
	 * Get the direction that this switch is pointing
	 *
	 * @return the direction
	 */
	@Override
	public Direction getDirection() {
		return value;
	}

	public FakePOV set(Direction value) {
		this.value = value;
		return this;
	}
}
