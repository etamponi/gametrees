package game.plugins.classifiers.selectors;

import game.core.Dataset;
import game.plugins.classifiers.FeatureSelector;
import game.utils.Utils;

import java.util.Collections;
import java.util.List;

public class RandomFeatureSelector extends FeatureSelector {
	
	private List<Integer> range;

	@Override
	public void prepare(Dataset dataset) {
		range = Utils.range(0, inputEncoder.getFeatureNumber());
	}

	@Override
	public List<Integer> select(int n, int[] timesChoosen, Dataset dataset) {
		if (n > 0) {
			Collections.shuffle(range);
			return range.subList(0, n);
		} else {
			return range;
		}
	}

}
