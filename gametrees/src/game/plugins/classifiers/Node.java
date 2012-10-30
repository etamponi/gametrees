package game.plugins.classifiers;

import game.configuration.Configurable;
import game.configuration.ConfigurableList;

import org.apache.commons.math3.linear.RealVector;

public class Node extends Configurable {
	
	private RealVector probability;
	
	public Criterion criterion;
	
	public ConfigurableList children = new ConfigurableList(this, Node.class);
	
	public RealVector decide(RealVector input) {
		if (children.isEmpty())
			return probability;
		else
			return children.get(criterion.decide(input), Node.class).decide(input);
	}
	
	public void setProbabilities(RealVector p) {
		this.probability = p;
	}
	
	public RealVector getProbability() {
		return probability;
	}

}
