package game.plugins.classifiers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.RealVector;

public class Node {
	
	private RealVector probability;
	
	private Criterion criterion;
	
	private final List<Node> children = new ArrayList<>();
	
	public RealVector decide(RealVector input) {
		if (children.isEmpty())
			return probability;
		else
			return children.get(criterion.decide(input)).decide(input);
	}
	
	public void setProbability(RealVector p) {
		this.probability = p;
	}
	
	public RealVector getProbability() {
		return probability;
	}
	
	public List<Node> getChildren() {
		return children;
	}

	public Criterion getCriterion() {
		return criterion;
	}

	public void setCriterion(Criterion criterion) {
		this.criterion = criterion;
	}

}
