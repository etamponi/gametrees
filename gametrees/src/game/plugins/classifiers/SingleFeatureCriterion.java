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

import game.core.Element;

import org.apache.commons.math3.linear.RealVector;

public abstract class SingleFeatureCriterion extends Criterion {
	
	private int featureIndex;
	
	public SingleFeatureCriterion(int featureIndex) {
		this.featureIndex = featureIndex;
	}
	
	@Override
	public int decide(Element input) {
		return decide(input.get(featureIndex, RealVector.class).getEntry(0));
	}

	public abstract int decide(double feature);

}
