package game.plugins.classifiers.criteria;

import game.plugins.classifiers.Criterion;

public class SingleThreshold extends Criterion {
	
	private double threshold;
	
	public SingleThreshold(int featureIndex, double threshold) {
		super(featureIndex);
		this.threshold = threshold;
	}

	@Override
	public int decide(double feature) {
		if (feature <= threshold)
			return 0;
		else
			return 1;
	}

}
