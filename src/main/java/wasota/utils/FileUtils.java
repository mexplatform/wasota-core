package wasota.utils;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtils {

	final static Logger logger = LoggerFactory.getLogger(FileUtils.class);

	public static String stringToHash(String str) {
		String original = str;
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(original.getBytes());
			byte[] digest = md.digest();
			StringBuffer sb = new StringBuffer();
			for (byte b : digest) {
				sb.append(String.format("%02x", b & 0xff));
			}

			logger.debug("Creating hash name for:" + original);
			logger.debug("digested(hex):" + sb.toString());
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void removeFile(String filePath) {
		try {
			File file = new File(filePath);
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}

	public static String getASCIIFormat(String str) {
		return str.replaceAll("[^A-Za-z0-9]", "");
	}

}
