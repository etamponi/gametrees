package game.plugins.classifiers;

import game.core.Encoding;
import game.core.InstanceTemplate;
import game.core.blocks.Classifier;
import game.plugins.datatemplates.LabelTemplate;
import game.plugins.encoders.OneHotEncoder;

import com.ios.triggers.BoundProperties;

public class DecisionTree extends Classifier {
	
	public Node root;
	
	public DecisionTree() {
		setContent("outputEncoder", new OneHotEncoder());
		addTrigger(new BoundProperties(this, "outputEncoder"));
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
