package game.plugins.algorithms;

import game.configuration.ErrorCheck;
import game.core.Block;
import game.core.Block.FeatureType;
import game.core.Dataset;
import game.core.Dataset.InstanceIterator;
import game.core.Dataset.SampleIterator;
import game.core.Instance;
import game.core.Sample;
import game.core.TrainingAlgorithm;
import game.plugins.classifiers.Criterion;
import game.plugins.classifiers.DecisionTree;
import game.plugins.classifiers.FeatureSelector;
import game.plugins.classifiers.Node;
import game.plugins.classifiers.criteria.Partition;
import game.plugins.classifiers.criteria.SingleThreshold;
import game.plugins.classifiers.selectors.RandomFeatureSelector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class C45Like extends TrainingAlgorithm<DecisionTree> {
	
	public boolean binarySplitNominal = false;
	
	public int featuresPerNode = 0;
	
	public int minimumSamples = 2;
	
	public FeatureSelector selector;
	
	public C45Like() {
		setOption("selector", new RandomFeatureSelector());
		
		setOptionChecks("featuresPerNode", new ErrorCheck<Integer>() {
			@Override public String getError(Integer value) {
				if (block.getParent(0) != null) {
					if (value > block.getParent(0).getFeatureNumber())
						return "cannot be greater than input feature number (" + block.getParent(0).getFeatureNumber() + ")";
				}
				return null;
			}
		});
	}
	
	@Override
	public boolean isCompatible(Block block) {
		return block instanceof DecisionTree
				&& block.getOption("template.inputTemplate.sequence", boolean.class) == false;
	}

	@Override
	protected void train(Dataset dataset) {
		updateStatus(0.0, "preparing feature selector");
		selector.prepare(dataset, block.getParent(0));
		updateStatus(0.1, "feature selector prepared, start training");
		recursiveTrain(dataset, block.root, new int[block.getParent(0).getFeatureNumber()]);
	}

	private void recursiveTrain(Dataset dataset, Node node, int[] timesChoosen) {

		if (dataset.size() <= minimumSamples) {
			// This is a leaf
			node.setProbabilities(getProbabilities(dataset));
			return;
		}
		
		if (information(dataset) == 0) {
			// This is a leaf
			node.setProbabilities(getProbabilities(dataset));
			return;
		}
		
		Criterion criterion = bestCriterion(dataset, timesChoosen);
		if (criterion == null) {
			// This is a leaf
			node.setProbabilities(getProbabilities(dataset));
			return;
		}
		
		node.criterion = criterion;
		timesChoosen = Arrays.copyOf(timesChoosen, timesChoosen.length);
		timesChoosen[criterion.featureIndex]++;
		for(Dataset split: split(dataset, criterion)) {
			Node child = new Node();
			node.children.add(child);
			recursiveTrain(split, child, timesChoosen);
		}
	}
	
	private static class CriterionWithGain {
		private Criterion criterion;
		private double gain;
		
		public CriterionWithGain(Criterion criterion, double gain) {
			this.criterion = criterion;
			this.gain = gain;
		}
	}
	
	private Criterion bestCriterion(Dataset dataset, int[] timesChoosen) {
		CriterionWithGain ret = new CriterionWithGain(null, 0);
		
		List<Integer> possibleFeatures = selector.select(featuresPerNode, timesChoosen);
		
		for(int feature: possibleFeatures) {
			CriterionWithGain current = bestCriterionFor(feature, dataset);
			if (current.gain > ret.gain)
				ret = current;
		}
		
		return ret.criterion;
	}
	
	private static class FeatureValue implements Comparable<FeatureValue> {
		private double value;
		private String label;
		
		private FeatureValue(double value, String label) {
			this.value = value;
			this.label = label;
		}
		
		@Override
		public int compareTo(FeatureValue o) {
			return Double.compare(this.value, o.value);
		}
	}
	
	private CriterionWithGain bestCriterionFor(int feature, Dataset dataset) {
		
		if (block.getParent(0).getFeatureType(feature) == FeatureType.NOMINAL && binarySplitNominal == false) {
			
			Partition criterion = new Partition();
			criterion.featureIndex = feature;
			double gain = gain(split(dataset, criterion));
			
			return new CriterionWithGain(criterion, gain);
			
		} else {
			
			CriterionWithGain ret = new CriterionWithGain(null, 0);
			
			List<FeatureValue> values = new ArrayList<>(dataset.size());
			SampleIterator it = dataset.encodedSampleIterator(block.getParent(0), block.outputEncoder, false);
			while(it.hasNext()) {
				Sample sample = it.next();
				values.add(new FeatureValue(sample.getEncodedInput().getEntry(feature), (String)sample.getOutput()));
			}
			Collections.sort(values);
			
			Map<String, Double> lesserCount = new HashMap<>();
			Map<String, Double> greaterCount = countPerLabel(values);
			double information = information(greaterCount);
			double threshold = Double.NaN;
			
			FeatureValue prev = values.get(0);
			int count = 1;
			for(int i = 1; i < values.size(); i++) {
				FeatureValue curr = values.get(i);
				if (!lesserCount.containsKey(prev.label))
					lesserCount.put(prev.label, 0.0);
				if (!prev.label.equals(curr.label)) {
					lesserCount.put(prev.label, lesserCount.get(prev.label)+count);
					greaterCount.put(prev.label, greaterCount.get(prev.label)-count);
					count = 1;
					if (prev.value < curr.value) {
						double gain = information + gain(lesserCount, greaterCount);
						if (gain > ret.gain) {
							threshold = (prev.value + curr.value)/2;
							ret.gain = gain;
						}
					}
				} else {
					count++;
				}
				prev = curr;
			}
			
			if (!Double.isNaN(threshold)) {
				SingleThreshold c = new SingleThreshold();
				c.threshold = threshold;
				c.featureIndex = feature;
				ret.criterion = c;
			}
			return ret;
		}
	}
	
	private Map<String, Double> countPerLabel(List<FeatureValue> values) {
		Map<String, Double> ret = new HashMap<>();
		for(FeatureValue value: values) {
			if (!ret.containsKey(value.label))
				ret.put(value.label, 0.0);
			ret.put(value.label, ret.get(value.label)+1);
		}
		return ret;
	}

	private double gain(Map<String, Double> split1, Map<String, Double> split2) {
		double ret = 0;
		
		double totalCount = count(split1) + count(split2);
		ret -= information(split1) * count(split1) / totalCount;
		ret -= information(split2) * count(split2) / totalCount;
		
		return ret;
	}
	
	private double information(Map<String, Double> map) {
		double[] prob = getProbabilities(map);
		double info = 0;
		for (double p: prob)
			if (p > 0)
				info += p * Math.log(p);
		return -info;
	}

	private double[] getProbabilities(Map<String, Double> map) {
		double total = count(map);
		double[] ret = new double[map.keySet().size()];
		int i = 0;
		for(double count: map.values())
			ret[i++] = count / total;
		return ret;
	}

	private double count(Map<String, Double> map) {
		double ret = 0;
		for(double count: map.values())
			ret += count;
		return ret;
	}

	private double information(Dataset dataset) {
		RealVector prob = getProbabilities(dataset);
		double info = 0;
		for (double p: prob.toArray())
			if (p > 0)
				info += p * Math.log(p);
		return -info;
	}
	
	private double gain(List<Dataset> splits) {
		double ret = 0;
		double total = 0;
		for (Dataset dataset: splits) {
			ret -= dataset.size() * information(dataset);
			total += dataset.size();
		}
		ret = ret / total;
		return ret;
	}

	private List<Dataset> split(Dataset dataset, Criterion criterion) {
		List<List<Integer>> indices = new ArrayList<>();
		indices.add(new ArrayList<Integer>());
		indices.add(new ArrayList<Integer>());
		
		InstanceIterator it = dataset.instanceIterator();
		while(it.hasNext()) {
			int split = criterion.decide(block.getParent(0).transform(it.next().getInput()).getElement(0));
			indices.get(split).add(it.getCurrentIndex());
		}
		
		List<Dataset> ret = new ArrayList<>(2);
		ret.add(new Dataset(dataset, indices.get(0)));
		ret.add(new Dataset(dataset, indices.get(1)));
		
		return ret;
	}

	private RealVector getProbabilities(Dataset dataset) {
		Map<String, Double> prob = new HashMap<>();
		InstanceIterator it = dataset.instanceIterator();
		double sum = 0;
		while(it.hasNext()) {
			Instance i = it.next();
			String key = i.getOutput().get(0).toString();
			if (!prob.containsKey(key))
				prob.put(key, 0.0);
			prob.put(key, prob.get(key)+1.0);
			sum++;
		}
		
		List<String> labels = block.template.outputTemplate.getOption("labels");
		
		RealVector ret = new ArrayRealVector(labels.size());
		for(String key: prob.keySet()) {
			ret.setEntry(labels.indexOf(key), prob.get(key)/sum);
		}

		return ret;
	}

	static private final String[] managed = {"root"};
	@Override
	public String[] getManagedBlockOptions() {
		return managed;
	}

}
