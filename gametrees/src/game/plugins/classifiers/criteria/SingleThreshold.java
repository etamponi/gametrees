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
package game.plugins.classifiers.criteria;

import game.plugins.classifiers.SingleFeatureCriterion;

public class SingleThreshold extends SingleFeatureCriterion {
	
	private double threshold;
	
	public SingleThreshold(int featureIndex, double threshold) {
		super(featureIndex);
		this.threshold = threshold;
	}

	@Override
	public int decide(double feature) {
		if (feature <= threshold)
			return 0;
		else
			return 1;
	}

}
