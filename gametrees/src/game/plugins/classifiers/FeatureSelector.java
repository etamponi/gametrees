package game.plugins.classifiers;

import game.configuration.Configurable;
import game.core.Block;
import game.core.Dataset;

import java.util.List;

import org.apache.commons.math3.linear.RealVector;

public abstract class FeatureSelector extends Configurable {
	
	public static abstract class Transform extends Configurable {
		
		public abstract double value(double x, double p);
		
	}
	
	public static class Divide extends Transform {
		@Override
		public double value(double x, double p) {
			return x / (p+1);
		}
	}
	
	public Transform transform = new Divide();
	
	public boolean prepareOnce = true;
	
	public abstract void prepare(Dataset dataset, Block inputEncoder);

	public abstract List<Integer> select(int n, int[] timesChoosen);
	
	protected RealVector adjust(RealVector p, int[] timesChoosen) {
		RealVector ret = p.copy();
		
		for(int i = 0; i < ret.getDimension(); i++)
			ret.setEntry(i, transform.value(p.getEntry(i), timesChoosen[i]));
		ret.mapDivideToSelf(ret.getL1Norm());
		
		return ret;
	}
	
}
