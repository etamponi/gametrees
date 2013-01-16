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
import game.core.Element;
import game.core.ElementTemplate;
import game.core.ValueTemplate;
import game.core.blocks.Classifier;
import game.plugins.datatemplates.VectorTemplate;

public class DecisionTree extends Classifier {
	
	public Node root;

	@Override
	protected Data transduce(Data input) {
		Data ret = new Data();
		for(Element i: input) {
			ret.add(new Element(root.decide(i)));
		}
		return ret;
	}

	@Override
	public boolean supportsInputTemplate(ElementTemplate inputTemplate) {
		if (inputTemplate.isEmpty())
			return false;
		for (ValueTemplate tpl: inputTemplate) {
			if (!(tpl instanceof VectorTemplate) || tpl.getContent("dimension", int.class) != 1)
				return false;
		}
		return true;
	}

	@Override
	protected void setup() {
		// Nothing to do
	}

}
