package org.usfirst.frc.team1458.lib.util.maths;

/**
 * A class for shifting with more advanced ranges. This features two arrays,
 * an input and output, where the values correspond to each other. This
 * class then linearly interpolates between the points. <br />
 *
 * Beyond the defined region, it continues the slope from the outermost
 * areas. The function shift() is G0 continuous, with its derivatives
 * undefined at each of the control points.
 *
 * @author mehnadnerd
 */
public class AdvancedRangeShifter extends RangeShifter {
	private final RangeShifter[] rs;
	private final double[] sp;

	public AdvancedRangeShifter(double[] from, double[] to) {
		super(0, 0, 0, 0);
		if (from.length != to.length) {
			throw new IllegalArgumentException("From Array and To Array must be same size");
		}
		rs = new RangeShifter[from.length];
		sp = from;
		for (int i = 0; i < from.length - 2; i++) {
			rs[i] = new RangeShifter(from[i], from[i + 1], to[i], to[i + 1]);
		}
	}

	@Override
	public double shift(double d) {
		if (d < sp[0]) {
			return rs[0].shift(d);
		}
		for (int i = 0; i < rs.length - 2; i++) {
			if (d >= sp[i]) {
				return rs[i].shift(d);
			}
		}
		return rs[rs.length - 1].shift(d);
	}
}