package game.plugins.classifiers;

import game.configuration.Configurable;

import org.apache.commons.math3.linear.RealVector;

public abstract class Transform extends Configurable {
	
	public abstract RealVector value(final RealVector p, final int[] timesChoosen);
	
}
