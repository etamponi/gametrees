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
package game.plugins.classifiers.selectors;

import game.core.Dataset;
import game.core.Experiment;
import game.plugins.classifiers.FeatureSelector;
import game.utils.Utils;

import java.util.Collections;
import java.util.List;

import com.ios.Property;
import com.ios.listeners.SubPathListener;
import com.ios.triggers.SimpleTrigger;

public class RandomFeatureSelector extends FeatureSelector {
	
	private List<Integer> range;
	
	public RandomFeatureSelector() {
		addTrigger(new SimpleTrigger(new SubPathListener(new Property(this, "inputEncoder"))) {
			private RandomFeatureSelector selector = RandomFeatureSelector.this;
			@Override
			public void action(Property changedPath) {
				if (selector.inputEncoder != null)
					selector.range = Utils.range(0, selector.inputEncoder.getFeatureNumber());
			}
		});
	}

	@Override
	public List<Integer> select(int n, Dataset dataset) {
		Collections.shuffle(range, Experiment.getRandom());
		return range.subList(0, n);
	}

}
