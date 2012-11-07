package game.plugins.classifiers.transforms;

import game.plugins.classifiers.Transform;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class Division extends Transform {

	@Override
	public RealVector value(final RealVector p, final int[] timesChoosen) {
		RealVector ret = new ArrayRealVector(p.getDimension());
		for(int i = 0; i < p.getDimension(); i++)
			ret.setEntry(i, p.getEntry(i)/(timesChoosen[i]+1));
		return ret;
	}
	
}