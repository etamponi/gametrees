package game.plugins.classifiers.criteria;

import game.plugins.classifiers.Criterion;

public class Partition extends Criterion {
	
	@Override
	public int decide(double feature) {
		return (int)feature;
	}

}
