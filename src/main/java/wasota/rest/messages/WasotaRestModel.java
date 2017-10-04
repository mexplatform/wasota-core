package wasota.rest.messages;

import org.json.JSONObject;

/**
 * @author Ciro Baron Neto
 * 
 * Jul 4, 2016
 */
public class WasotaRestModel {
	
	public String status;

	public String data;

	/**
	 * Constructor for Class WasotaRestMsg 
	 */
	public WasotaRestModel(String status, String data) {
		this.status = status;
		this.data = data;
	}

	
}
