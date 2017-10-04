package wasota.core.models;

public class WasotaPerformanceModel implements Comparable<WasotaPerformanceModel> {

	public String userMail;

	public String experimentID;

	public String experimentTitle;

	public String algorithmLbl;

	public String algorithmClass;

	public String performance;

	public String performanceValue;

	public String url;
	
	public Boolean visible = true;

	@Override
	public int compareTo(WasotaPerformanceModel o) {
		Double thisValue = Double.valueOf(this.performanceValue);
		Double oValue = Double.valueOf(o.performanceValue);
		
		return Double.compare(thisValue, oValue);
	}


}
