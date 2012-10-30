package game.plugins.classifiers.criteria;

import org.apache.commons.math3.linear.RealVector;

import game.plugins.classifiers.Criterion;

public class Partition extends Criterion {
	
	public int featureIndex;

	@Override
	public int decide(RealVector input) {
		return (int)input.getEntry(featureIndex);
	}

}
