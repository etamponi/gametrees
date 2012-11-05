package game.plugins.classifiers.criteria;

import game.plugins.classifiers.Criterion;

public class SingleThreshold extends Criterion {
	
	public double threshold;

	@Override
	public int decide(double feature) {
		if (feature <= threshold)
			return 0;
		else
			return 1;
	}

}
