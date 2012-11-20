package game.plugins.classifiers;

import org.apache.commons.math3.linear.RealVector;

public abstract class Criterion {
	
	private int featureIndex;
	
	public Criterion(int featureIndex) {
		this.featureIndex = featureIndex;
	}
	
	public int decide(RealVector input) {
		return decide(input.getEntry(featureIndex));
	}

	public abstract int decide(double feature);

}
