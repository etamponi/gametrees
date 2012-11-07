package game.plugins.classifiers;

import game.configuration.Configurable;
import game.core.Block;
import game.core.Dataset;
import game.plugins.classifiers.transforms.Division;

import java.util.List;

import org.apache.commons.math3.linear.RealVector;

public abstract class FeatureSelector extends Configurable {
	
	public Block inputEncoder;
	
	public Transform transform = new Division();
	
	public boolean prepareOnce = true;
	
	public abstract void prepare(Dataset dataset);

	public abstract List<Integer> select(int n, int[] timesChoosen, Dataset dataset);
	
	protected RealVector adjust(RealVector p, int[] timesChoosen) {
		RealVector ret = transform.value(p, timesChoosen);
		
		ret.mapDivideToSelf(ret.getL1Norm());
		
		return ret;
	}
	
}
