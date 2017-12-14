package org.usfirst.frc.team1458.lib.util.maths;

/**
 * Immutable pair of values of the same type.
 *
 * @author asinghani
 */
public class Vector3<T> extends Vector2<T> {
	private final T z;

	public Vector3(T x, T y, T z) {
		super(x, y);
		this.z = z;
	}

	public T getZ() {
		return z;
	}
}
