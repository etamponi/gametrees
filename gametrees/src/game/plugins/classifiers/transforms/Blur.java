package game.plugins.classifiers.transforms;

import game.plugins.classifiers.Transform;
import game.utils.Utils;

import org.apache.commons.math3.linear.RealVector;

public class Blur extends Transform {

	@Override
	public RealVector value(RealVector p, int[] timesChoosen) {
		RealVector ret = p.copy();
		int sum = Utils.sum(timesChoosen);
		while(sum-- > 0) {
			blur(ret);
		}
		return ret;
	}

	private void blur(RealVector v) {
		for(int i = 0; i < v.getDimension(); i++) {
			double sum = 0;
			for(int j = i-1; j <= i+1; j++) {
				if (j == v.getDimension())
					sum += v.getEntry(j-2);
				else if (j == -1)
					sum += v.getEntry(j+2);
				else
					sum += v.getEntry(j);
			}
			v.setEntry(i, sum/3);
		}
	}

}
