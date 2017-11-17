package org.usfirst.frc.team1458.lib.util.units;

import org.usfirst.frc.team1458.lib.util.annotation.Immutable;
import org.usfirst.frc.team1458.lib.util.maths.TurtwigMaths;

/**
 * This class represents a distance value.
 */
@Immutable
public interface Distance extends Unit {
	Distance ZERO = Distance.createInches(0.0);
	Distance ERROR = Distance.createInches(Double.NaN);

	static Distance createInches(double inches) {
		return () -> inches;
	}

	static Distance createFeet(double feet) {
		return () -> feet/12;
	}

	static Distance createCentimetres(double cm) {
		return () -> cm/2.54;
	}

	static Distance createMetres(double metres) {
		return () -> metres/0.0254;
	}

	static boolean isError(Distance distance) {
		return Double.isNaN(distance.getValue());
	}

	/**
	 * Get the value in inches
	 */
	double getValue();

	/**
	 * Get the value in inches
	 */
	default double getInches() {
		return getValue();
	}

	/**
	 * Get the value in feet
	 */
	default double getFeet() {
		return getValue() * 12;
	}

	/**
	 * Get the value in centimetres
	 */
	default double getCentimetres() {
		return getValue() * 2.54;
	}

	/**
	 * Get the value in metres
	 */
	default double getMetres() {
		return getValue() * 0.0254;
	}

	/**
	 * Get the negative of the value. Useful for moving the robot backwards.
	 */
	default Distance invert() {
		return Distance.createInches(getInches() * -1);
	}

	default boolean equals(Distance distance) {
		if (this == distance) return true;
		return TurtwigMaths.absDiff(distance.getValue(), this.getValue()) < 0.000001;
	}
}
