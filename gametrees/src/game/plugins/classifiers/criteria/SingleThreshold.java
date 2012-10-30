package game.plugins.classifiers.criteria;

import game.plugins.classifiers.Criterion;

import org.apache.commons.math3.linear.RealVector;

public class SingleThreshold extends Criterion {
	
	public int featureIndex;
	
	public double threshold;

	@Override
	public int decide(RealVector input) {
		if (input.getEntry(featureIndex) <= threshold)
			return 0;
		else
			return 1;
	}

}
