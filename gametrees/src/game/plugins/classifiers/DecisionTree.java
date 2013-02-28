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
package game.plugins.classifiers;

import game.core.Data;
import game.core.DatasetTemplate;
import game.core.Element;
import game.core.blocks.Classifier;

public class DecisionTree extends Classifier {
	
	public Node root;

	@Override
	public Data classify(Data input) {
		Data ret = new Data();
		for(Element i: input) {
			ret.add(new Element(root.decide(i)));
		}
		return ret;
	}

	@Override
	public String classifierCompatibilityError(DatasetTemplate template) {
		return null;
	}

	@Override
	protected void updateOutputTemplate() {
		// Nothing to do
	}

}
