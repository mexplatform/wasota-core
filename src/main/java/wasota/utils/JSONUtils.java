package wasota.utils;

import org.json.JSONException;
import org.json.JSONObject;

import wasota.core.exceptions.ParameterNotFound;

public class JSONUtils {

	public static String getField(String jsonObject, String filed) throws ParameterNotFound {
		JSONObject o = new JSONObject(jsonObject.toString());
		try {
			return o.get(filed).toString();
		} catch (JSONException e) {
			throw new ParameterNotFound(filed);
		}
	}

}
