package org.usfirst.frc.team1458.lib.input;

import org.usfirst.frc.team1458.lib.util.units.Angle;

/**
 * This is a component which can point in any of 8 directions (or centered).
 *
 * @author asinghani
 */
public interface POV extends DigitalInput {

	/**
	 * Get the direction that this switch is pointing
	 * @return the direction
	 */
	Direction getDirection();

	/**
	 * Get the angle corresponding with the direction that this switch is pointing
	 * @return the angle; null if centered
	 */
	default Angle getAngle() {
		Direction direction = getDirection();
		return null; // TODO FIX //direction.angle == -1 ? null : new Angle(direction.angle);
	}

	/**
	 * Get the value of this switch
	 * @return the angle that this switch is pointed toward
	 */
	@Override
	default int getValue() {
		return getDirection().angle;
	}

	enum Direction {
		CENTER(-1), NORTH(0), NORTHEAST(45), EAST(90), SOUTHEAST(135), SOUTH(180), SOUTHWEST(225), WEST(270), NORTHWEST(315);
		public final int angle;

		Direction(int angle) {
			this.angle = angle;
		}

		public static Direction fromAngle(int angle) {
			switch(angle) {
				case 0:
					return NORTH;
				case 45:
					return NORTHEAST;
				case 90:
					return EAST;
				case 135:
					return SOUTHEAST;
				case 180:
					return SOUTH;
				case 225:
					return SOUTHWEST;
				case 270:
					return WEST;
				case 315:
					return NORTHWEST;
				default:
					return CENTER;
			}
		}
	}
}
