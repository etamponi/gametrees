package game.plugins.classifiers.criteria;

import game.plugins.classifiers.Criterion;

public class Partition extends Criterion {
	
	public Partition(int featureIndex) {
		super(featureIndex);
	}
	
	@Override
	public int decide(double feature) {
		return (int)feature;
	}

}
