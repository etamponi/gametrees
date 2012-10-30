package game.plugins.classifiers.selectors;

import game.core.Dataset;
import game.plugins.classifiers.DecisionTree;
import game.plugins.classifiers.FeatureSelector;
import game.utils.Utils;

import java.util.Collections;
import java.util.List;

public class RandomFeatureSelector extends FeatureSelector {
	
	private List<Integer> range;

	@Override
	public void prepare(Dataset dataset, DecisionTree block) {
		range = Utils.range(0, block.getParent(0).getFeatureNumber());
	}

	@Override
	public List<Integer> select(int n) {
		if (n > 0) {
			Collections.shuffle(range);
			return range.subList(0, n);
		} else {
			return range;
		}
	}

}
