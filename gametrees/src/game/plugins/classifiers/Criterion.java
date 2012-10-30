package game.plugins.classifiers;

import game.configuration.Configurable;

import org.apache.commons.math3.linear.RealVector;

public abstract class Criterion extends Configurable {
	
	public abstract int decide(RealVector input);

}
