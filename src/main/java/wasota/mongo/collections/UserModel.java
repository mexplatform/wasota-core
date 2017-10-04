package wasota.mongo.collections;

import wasota.mongo.MongoSuperClass;
import wasota.mongo.exceptions.MissingPropertiesException;
import wasota.mongo.exceptions.NoPKFoundException;
import wasota.mongo.exceptions.ObjectAlreadyExistsException;

public class UserModel extends MongoSuperClass{

	private static final String USER = "user";

	private static final String PASS = "pass";

	private static final String EMAIL = "email";


	private static final String COLLECTION = "user";
	

	public UserModel(String user, String email, String pass) {
		super(COLLECTION);
		addKey(USER);
		addKey(EMAIL);
		
		setUser(user);
		setPass(pass);
		setEmail(email);
		
	}
	
	public UserModel() {
		super(COLLECTION);	
		addKey(USER);
		addKey(EMAIL);

	}

	
	public UserModel(String user) {
		super(COLLECTION);
		addKey(USER);
		addKey(EMAIL);

		setUser(user);
		find(true);
	}

	
	
	public String getUser() {
		return getField(USER).toString();
	}

	public String getPass() {
		return getField(PASS).toString();
	}

	public String getEmail() {
		return getField(EMAIL).toString();
	}

	public void setUser(String user) {
		addField(USER, user);
	}

	public void setPass(String pass) {
		addField(PASS, pass);
	}

	public void setEmail(String email) {
		addField(EMAIL, email);
	}

}
