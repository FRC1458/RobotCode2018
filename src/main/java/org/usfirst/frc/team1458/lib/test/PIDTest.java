package org.usfirst.frc.team1458.lib.test;

import org.usfirst.frc.team1458.lib.pid.PID;
import org.usfirst.frc.team1458.lib.pid.PIDConstants;
import org.usfirst.frc.team1458.lib.util.maths.TurtleMaths;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * TODO: Add Comment
 *
 * @author asinghani
 */
public class PIDTest {
	public static void main(String[] args) throws Exception {
		final int target = 90;
		final PIDConstants constants = new PIDConstants(1.0/90.0, 0.0/90.0, 0.0/90.0, 0.00/90.0);

		PID pid = new PID(constants, target, 8, true);

		List<Double> pidHistory = new ArrayList<>();
		List<Double> positionHistory = new ArrayList<>();

		/**
		 * Simulation
		 */
		Random r = new Random();
		double position = 0;
		int i = 0;
		do {
			i++;
			double pidValue = pid.newValue(position);

			pidValue = TurtleMaths.fitRange(pidValue, -1, 1);

			position += pidValue;

			//System.out.println("position = "+position+", pidValue = "+pidValue);

			Thread.sleep(10);

			pidHistory.add(pidValue);
			positionHistory.add(position);
			position += 1 * (r.nextDouble() - 0.5); // Random noise

		} while (!pid.atTarget() && i < 10000);

		System.out.println("{"+pidHistory.toString().replace("[", "{").replace("]", "}")+","+
				positionHistory.toString().replace("[", "{").replace("]", "}")+"}");
	}
}
