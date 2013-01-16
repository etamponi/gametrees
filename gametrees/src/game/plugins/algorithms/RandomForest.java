/*******************************************************************************
 * Copyright (c) 2012 Emanuele Tamponi.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Emanuele Tamponi - initial API and implementation
 ******************************************************************************/
package game.plugins.algorithms;

import game.core.Dataset;
import game.core.DatasetTemplate;
import game.core.TrainingAlgorithm;
import game.core.blocks.MetaEnsemble;
import game.plugins.blocks.classifiers.MajorityCombiner;
import game.plugins.blocks.pipes.ProbabilityToLabel;
import game.plugins.classifiers.DecisionTree;
import game.plugins.datatemplates.LabelTemplate;
import game.plugins.trainingalgorithms.TargetLabelDecoder;
import game.utils.Utils;

import com.ios.ErrorCheck;
import com.ios.errorchecks.RangeCheck;
import com.ios.triggers.MasterSlaveTrigger;


public class RandomForest extends TrainingAlgorithm<MetaEnsemble> {
	
	public double bootstrapPercent = 0.66;
	
	public int featuresPerNode = 0;
	
	public int trees = 10;
	
	public RandomForest() {
		addErrorCheck("bootstrapPercent", new RangeCheck(0.01, 1.0));
		
		addErrorCheck("featuresPerNode", new ErrorCheck<Integer>() {
			public RandomForest wrapper = RandomForest.this;
			@Override public String getError(Integer value) {
				if (wrapper.block != null && wrapper.block.getParentTemplate() != null) {
					if (value > wrapper.block.getParentTemplate().size())
						return "cannot be greater than input feature number (" + block.getParentTemplate() + ")";
					if (value < 0)
						return "cannot be negative";
				}
				return null;
			}
		});
		
		addErrorCheck("block", new ErrorCheck<MetaEnsemble>() {
			@Override
			public String getError(MetaEnsemble value) {
				if (value.getParentTemplate() != null) {
					if (!(new DecisionTree().supportsInputTemplate(value.getParentTemplate())))
						return "cannot handle " + value.getParentTemplate();
					else
						return null;
				} else {
					return "invalid parent template (null)";
				}
			}
		});
		
		addTrigger(new MasterSlaveTrigger(this, "block.datasetTemplate.targetTemplate", "block.outputTemplate", "block.combiner.outputTemplate"));
	}

	@Override
	protected void train(Dataset dataset) {
		int featuresPerNode = this.featuresPerNode == 0 ? (int)Utils.log2(block.getParentTemplate().size()) + 1 : this.featuresPerNode;
		
		block.setContent("combiner", new MajorityCombiner());
		
		updateStatus(0.1, "start growing forest of " + trees + " trees using " + featuresPerNode + " features per node.");
		
		for(int i = 0; i < trees; i++) {
			updateStatus(0.1 + 0.9*i/trees, "growing tree " + (i+1));
			DecisionTree tree = new DecisionTree();
			tree.setContent("datasetTemplate", block.datasetTemplate);
			tree.setContent("trainingAlgorithm", new RealFeaturesTree());
			tree.parents.add(block.getParent());
			tree.trainingAlgorithm.setContent("featuresPerNode", featuresPerNode);
			executeAnotherTaskAndWait(0.1+0.9*(i+1)/trees, tree.trainingAlgorithm, dataset.getRandomSubset(bootstrapPercent)); // FIXME Bootstrap sample
			
			ProbabilityToLabel dec = new ProbabilityToLabel();
			dec.setContent("datasetTemplate", block.datasetTemplate);
			dec.setContent("trainingAlgorithm", new TargetLabelDecoder());
			dec.parents.add(tree);
			block.combiner.parents.add(dec);
		}
	}

	@Override
	protected String getTrainingPropertyNames() {
		return "combiner";
	}

	@Override
	protected boolean isCompatible(DatasetTemplate datasetTemplate) {
		return datasetTemplate.sequences == false && datasetTemplate.targetTemplate.isSingletonTemplate(LabelTemplate.class);
	}

}

