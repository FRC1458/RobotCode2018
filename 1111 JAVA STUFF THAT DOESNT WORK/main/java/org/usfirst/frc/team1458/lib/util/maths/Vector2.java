package org.usfirst.frc.team1458.lib.util.maths;

/**
 * Immutable pair of values of the same type.
 *
 * @author asinghani
 */
public class Vector2<T> {
	private final T x;
	private final T y;

	public Vector2(T x, T y) {
		this.x = x;
		this.y = y;
	}

	public T getX() {
		return x;
	}

	public T getY() {
		return y;
	}
}
