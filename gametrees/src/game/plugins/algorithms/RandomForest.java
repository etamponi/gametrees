package game.plugins.algorithms;

import game.configuration.ErrorCheck;
import game.configuration.errorchecks.RangeCheck;
import game.core.Block;
import game.core.Dataset;
import game.core.TrainingAlgorithm;
import game.core.blocks.MetaEnsemble;
import game.plugins.classifiers.DecisionTree;
import game.plugins.classifiers.FeatureSelector;
import game.plugins.classifiers.MajorityCombiner;
import game.plugins.encoders.OneHotEncoder;
import game.utils.Utils;

public class RandomForest extends TrainingAlgorithm<MetaEnsemble> {
	
	public boolean uniqueSelector = true;
	
	public double bootstrapPercent = 0.66;
	
	public int featuresPerNode = 0;
	
	public int trees = 10;
	
	public FeatureSelector selector;
	
	public RandomForest() {
		setOptionBinding("uniqueSelector", "selector.prepareOnce");
		
		setOptionChecks("bootstrapPercent", new RangeCheck(0.01, 1.0));
		
		setOptionChecks("featuresPerNode", new ErrorCheck<Integer>() {
			@Override public String getError(Integer value) {
				if (block != null && block.getParent(0) != null) {
					if (value > block.getParent(0).getFeatureNumber())
						return "cannot be greater than input feature number (" + block.getParent(0).getFeatureNumber() + ")";
				}
				return null;
			}
		});
	}

	@Override
	public boolean isCompatible(Block block) {
		return block instanceof MetaEnsemble;
	}
	
	public void setBlock(MetaEnsemble block) {
		this.block = block;
		block.setOption("outputEncoder", new OneHotEncoder());
		block.setOption("combiner", new MajorityCombiner());
	}

	@Override
	protected void train(Dataset dataset) {
		int selectedFeatures = featuresPerNode == 0 ? (int)Utils.log2(block.getParent(0).getFeatureNumber()) + 1 : featuresPerNode;
		
		if (uniqueSelector) {
			updateStatus(0.01, "preparing feature selector");
			selector.prepare(dataset, block.getParent(0));
		}
		
		updateStatus(0.1, "start growing forest of " + trees + " trees using " + selectedFeatures + " features per node.");
		
		for(int i = 0; i < trees; i++) {
			updateStatus(0.1 + 0.9*i/trees, "growing tree " + (i+1));
			DecisionTree tree = new DecisionTree();
			tree.setOption("template", block.template);
			tree.setOption("trainingAlgorithm", new C45Like());
			tree.parents.add(block.getParent(0));
			tree.trainingAlgorithm.setOption("featuresPerNode", selectedFeatures);
			tree.trainingAlgorithm.setOption("selector", selector);
			executeAnotherTaskAndWait(0.1+0.9*(i+1)/trees, tree.trainingAlgorithm, dataset.getRandomSubset(bootstrapPercent)); // FIXME Bootstrap sample
			block.combiner.parents.add(tree);
		}
	}

	private static final String[] managed = {"combiner", "outputEncoder"};
	@Override
	public String[] getManagedBlockOptions() {
		return managed;
	}

}
