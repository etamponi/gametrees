package game.plugins.classifiers;

import game.core.Encoding;
import game.core.InstanceTemplate;
import game.core.blocks.Classifier;
import game.plugins.datatemplates.LabelTemplate;
import game.plugins.encoders.OneHotEncoder;

public class DecisionTree extends Classifier {
	
	public Node root = new Node();
	
	public DecisionTree() {
		setOption("outputEncoder", new OneHotEncoder());
		
		setAsInternalOptions("outputEncoder");
	}

	@Override
	public boolean isCompatible(InstanceTemplate template) {
		return template.outputTemplate instanceof LabelTemplate;
	}

	@Override
	protected Encoding classify(Encoding inputEncoded) {
		Encoding ret = new Encoding(getFeatureNumber(), inputEncoded.length());
		
		for(int j = 0; j < inputEncoded.length(); j++)
			ret.setElement(j, root.decide(inputEncoded.getElement(j)));
		
		return ret;
	}

	@Override
	public FeatureType getFeatureType(int featureIndex) {
		return FeatureType.NUMERIC;
	}

}
