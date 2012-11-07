package game.plugins.classifiers.transforms;

import game.plugins.classifiers.Transform;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class Exponential extends Transform {
	
	public double tau = 1;

	@Override
	public RealVector value(final RealVector p, final int[] timesChoosen) {
		RealVector ret = new ArrayRealVector(p.getDimension());
		for(int i = 0; i < p.getDimension(); i++)
			ret.setEntry(i, p.getEntry(i) * Math.exp(- timesChoosen[i] / tau));
		return ret;
		
	}

}
