package wasota.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class WasotaProperties {

	/**
	 * Load properties from file
	 */
	public void loadProperties() {
		Properties prop = new Properties();
		InputStream inputStream = null;
		try {
			String propFileName = "resources/config.properties";

			inputStream = new FileInputStream(propFileName);

			prop.load(inputStream);

			// get the property value and print it out
			MONGODB_HOST = prop.getProperty("MONGODB_HOST");
			MONGODB_PORT = Integer.valueOf(prop.getProperty("MONGODB_PORT"));
			MONGODB_DB = prop.getProperty("MONGODB_DB");
			MONGODB_SECURE_MODE = Boolean.valueOf(prop.getProperty("MONGODB_SECURE_MODE"));
			MONGODB_USERNAME = prop.getProperty("MONGODB_USERNAME");
			MONGODB_PASSWORD = prop.getProperty("MONGODB_PASSWORD");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	final public static String GRAPH_FOLDER_PATH = "./graphs/";
	final public static String INDEX_FOLDER_PATH = "./index/index";
	final public static String INDEX_PATH = INDEX_FOLDER_PATH + "/index";
	final public static String MEX_CORE_DOWNLOAD = "http://mex.aksw.org/mex-core";
	final public static String MEX_PERF_DOWNLOAD = "http://mex.aksw.org/mex-perf";
	final public static String MEX_ALGO_DOWNLOAD = "http://mex.aksw.org/mex-algo";

	// mongodb properties
	public static String MONGODB_HOST;
	public static int MONGODB_PORT;
	public static String MONGODB_DB;
	public static Boolean MONGODB_SECURE_MODE;
	public static String MONGODB_USERNAME;
	public static String MONGODB_PASSWORD;

}
