package org.usfirst.frc.team1458.lib.util.units;

import org.usfirst.frc.team1458.lib.util.annotation.Immutable;
import org.usfirst.frc.team1458.lib.util.maths.TurtwigMaths;

/**
 * Represents an amount of time
 */
@Immutable
public interface Time extends Unit {
	Time ZERO = Time.createSeconds(0.0);
	Time ONE_SECOND = Time.createSeconds(1.0);

	static Time createSeconds(double seconds) {
		return () -> seconds;
	}

	static Time createMillis(long millis) {
		return () -> (millis / 1000.0);
	}

	static Time createMinutes(double minutes) {
		return () -> (minutes * 60.0);
	}

	double getValue();

	default double getSeconds() {
		return getValue();
	}

	default long getMillis() {
		return (long) (getValue() * 1000.0);
	}

	default double getMinutes() {
		return getValue() / 60.0;
	}

	default boolean equals(Time time) {
		if (this == time) return true;
		return TurtwigMaths.absDiff(time.getValue(), this.getValue()) < 0.000001;
	}
}
