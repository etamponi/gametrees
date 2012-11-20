package game.plugins.classifiers;

import game.core.Block;
import game.core.Dataset;

import java.util.List;

import com.ios.IObject;

public abstract class FeatureSelector extends IObject {
	
	public Block inputEncoder;

	public abstract List<Integer> select(int n, Dataset dataset);
	
}
