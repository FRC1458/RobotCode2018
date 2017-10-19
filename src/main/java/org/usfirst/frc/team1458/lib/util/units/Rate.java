package org.usfirst.frc.team1458.lib.util.units;


import org.usfirst.frc.team1458.lib.util.annotation.Immutable;
import org.usfirst.frc.team1458.lib.util.maths.TurtleMaths;

/**
 * A class representing a rate or speed 
 */
@Immutable
interface Rate<T extends Unit> extends Unit {
	Rate<Distance> DISTANCE_ZERO = Rate.create(0.0);
	Rate<Angle> ANGLE_ZERO = Rate.create(0.0);

	/**
	 * Get the value in the units T/second
	 */
	@Override
	double getValue();

	static <T extends Unit> Rate<T> create(T value) {
		return () -> (value.getValue() / Time.ONE_SECOND.getValue());
	}

	static <T extends Unit> Rate<T> create(T value, Time time) {
		return () -> (value.getValue() / time.getValue());
	}

	static <T extends Unit> Rate<T> create(double value) {
		return () -> value;
	}

	default boolean equals(Rate<T> rate) {
		if (this == rate) return true;

		return TurtleMaths.absDiff(rate.getValue(), this.getValue()) < 0.000001;
	}
}
