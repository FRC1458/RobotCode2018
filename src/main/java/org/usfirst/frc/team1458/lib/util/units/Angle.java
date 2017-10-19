package org.usfirst.frc.team1458.lib.util.units;

import org.usfirst.frc.team1458.lib.util.annotation.Immutable;
import org.usfirst.frc.team1458.lib.util.maths.TurtleMaths;

/**
 * This class represents an angle.
 */
@Immutable
public interface Angle extends Unit {
	Angle ZERO = Angle.createDegrees(0.0);
	
	static Angle createRevolutions(double revolutions) {
		return () -> (revolutions * 360);
	}

	static Angle createRadians(double radians) {
		return () -> Math.toDegrees(radians);
	}

	static Angle createDegrees(double degrees) {
		return () -> degrees;
	}

	/**
	 * Get the value in degrees
	 */
	double getValue();

	/**
	 * Get the value in radians
	 */
	default double getRadians() {
		return Math.toRadians(getValue());
	}

	/**
	 * Get the value in degrees
	 */
	default double getDegrees() {
		return getValue();
	}

	/**
	 * Get the value in number of revolutions
	 */
	default double getRevolutions() {
		return getValue()/360.0;
	}

	/**
	 * Get the negative of the value.
	 */
	default Angle invert() {
		return Angle.createDegrees(getDegrees() * -1);
	}

	default boolean equals(Angle angle) {
		if (this == angle) return true;

		return TurtleMaths.absDiff(angle.getValue(), this.getValue()) < 0.000001;
	}
}
