package game.plugins.classifiers;

import game.configuration.Configurable;
import game.core.Dataset;

import java.util.List;

public abstract class FeatureSelector extends Configurable {
	
	public abstract void prepare(Dataset dataset, DecisionTree block);

	public abstract List<Integer> select(int n);
	
}
