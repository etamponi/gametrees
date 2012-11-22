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

import game.core.Block;
import game.core.Dataset;

import java.util.List;

import com.ios.IObject;

public abstract class FeatureSelector extends IObject {
	
	public Block inputEncoder;

	public abstract List<Integer> select(int n, Dataset dataset);
	
}
