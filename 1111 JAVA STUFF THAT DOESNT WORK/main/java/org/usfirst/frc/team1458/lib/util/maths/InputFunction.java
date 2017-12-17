package org.usfirst.frc.team1458.lib.util.maths;

import java.util.ArrayList;

/**
 * A function which can be applied to an input value to scale it in any form
 */
public interface InputFunction {
	// Some commonly used functions
	InputFunction IDENTITY = x -> x;
	InputFunction INVERSE = x -> -1 * x;
	InputFunction HALF = x -> 0.5 * x;
	InputFunction QUARTER = x -> 0.25 * x;

	InputFunction QUADRATIC = x -> x * Math.abs(x);

	InputFunction INPUT_SMOOTHER_5 = new InputFunction() {
		private final int samplesToAverage = 5;
		private ArrayList<Double> samples = new ArrayList<>(samplesToAverage);

		@Override
		public double apply(double t) {
			samples.add(t);

			if (samples.size() > samplesToAverage) {
				samples.remove(0);
			}

			return TurtwigMaths.average(samples);
		}
	};

	/**
	 * The original logistic step function with 2 steps
	 */
	InputFunction LOGISTIC_2_STEP = x -> x - (Math.sin(4 * Math.PI * x) / (4 * Math.PI));

	/**
	 * A logistic step function with more steps (4 as opposed to 2)
	 */
	InputFunction LOGISTIC_4_STEP = x -> x - (Math.sin(8 * Math.PI * x) / (8 * Math.PI));

	/**
	 * This function will map a value from [-1.0, 1.0] to [0.0, 1.0].
	 */
	InputFunction ANALOG_INPUT_TO_POSITIVE = x -> (x + 1.0) / 2.0;

	double apply(double value);

	default InputFunction invert() {
		return scale(InputFunction.INVERSE);
	}

	default InputFunction scale(InputFunction newFunc) {
		return (value) -> newFunc.apply(apply(value));
	}
}
