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

public class Partition extends SingleFeatureCriterion {
	
	public Partition(int featureIndex) {
		super(featureIndex);
	}
	
	@Override
	public int decide(double feature) {
		return (int)feature;
	}

}
