package game.plugins.classifiers;

import game.configuration.Configurable;

import org.apache.commons.math3.linear.RealVector;

public abstract class Criterion extends Configurable {
	
	public int featureIndex;
	
	public int decide(RealVector input) {
		return decide(input.getEntry(featureIndex));
	}

	public abstract int decide(double feature);

}
