package org.usfirst.frc.team1458.lib.motor;

import org.usfirst.frc.team1458.lib.motor.abilities.Follower;

/**
 * Immutable set of motors. Allows motors to be treated as ONE or several motors.
 *
 * @author asinghani
 */
public class FollowerMotorSet implements Motor {
	private final Motor master;

	public <T extends Motor> FollowerMotorSet(T master, Follower<T>... motors) {
		this.master = master;

		for(Follower<T> motor : motors) {
			motor.follow(master);
		}
	}

	@Override
	public FollowerMotorSet setSpeed(double speed) {
		master.setSpeed(speed);
		return this;
	}

	@Override
	public double getSpeed() {
		return master.getSpeed();
	}
}