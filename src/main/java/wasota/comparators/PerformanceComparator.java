/**
 * 
 */
package wasota.comparators;

import java.util.Comparator;

import wasota.core.models.WasotaPerformanceModel;

/**
 * @author Ciro Baron Neto
 * 
 *         Jul 11, 2016
 */
public class PerformanceComparator implements Comparator<WasotaPerformanceModel> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(WasotaPerformanceModel o1, WasotaPerformanceModel o2) {
		Double thisValue = Double.valueOf(o1.performanceValue);
		Double oValue = Double.valueOf(o2.performanceValue);
		System.out.println(Double.compare(thisValue, oValue));

		if (Double.compare(thisValue, oValue) >= 1)
			return -1;
		else if (Double.compare(thisValue, oValue) < 0)
			return 1;
		else
			return 0;

	}

}
