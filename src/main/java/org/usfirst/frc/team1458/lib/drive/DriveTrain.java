package org.usfirst.frc.team1458.lib.drive;

/**
 * This is a generic class for any sort of robot chassis train (tank, swerve, mecanum, etc).
 * Any unsupported operations should cause the implementation to throw an UnsupportedOperationException.
 */
public interface DriveTrain {

	void driveForward(double distance, double speed, double tolerance);

	/**
	 * Convenience method to drive the robot backward.
	 * By default this has exact same behaviour as driveForward(), but with opposite direction.
	 * @param distance
	 * @param speed
	 * @param tolerance
	 */
	default void driveBackward(double distance, double speed, double tolerance) {
		driveForward(-1*distance, speed, tolerance);
	}

	void driveRight(double distance, double speed, double tolerance);

	/**
	 * Convenience method to drive the robot left.
	 * By default this has exact same behaviour as driveRight(), but with opposite direction.
	 * @param distance
	 * @param speed
	 * @param tolerance
	 */
	default void driveLeft(double distance, double speed, double tolerance) {
		driveRight(-1*distance, speed, tolerance);
	}

	void turnRight(double angle, double speed, double tolerance);

	/**
	 * Convenience method to turn the robot left.
	 * By default this has exact same behaviour as turnRight(), but with opposite direction.
	 * @param angle
	 * @param speed
	 * @param tolerance
	 */
	default void turnLeft(double angle, double speed, double tolerance) {
		turnRight(-1*angle, speed, tolerance);
	}

	void stop();
}