package game.plugins.classifiers;

import org.apache.commons.math3.linear.RealVector;

public abstract class Criterion {
	
	public abstract int decide(RealVector input);

}
