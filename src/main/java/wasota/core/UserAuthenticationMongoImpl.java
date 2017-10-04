package wasota.core;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import wasota.core.authentication.UserAuth;
import wasota.mongo.collections.UserModel;
import wasota.mongo.exceptions.MissingPropertiesException;
import wasota.mongo.exceptions.NoPKFoundException;
import wasota.mongo.exceptions.ObjectAlreadyExistsException;

/**
 * Implementation of {@link UserAuthenticationMongoImpl} using MongoDB
 * @author Ciro Baron Neto
 * 
 * Jul 3, 2016
 */
@Service
public class UserAuthenticationMongoImpl implements UserAuthenticationServiceInterface{

	final static Logger logger = Logger.getLogger(UserAuthenticationMongoImpl.class);
	

	/**
	 * Add new user in MongoDB
	 */
	public boolean addUser(String user, String email, String pass) throws Exception {

		UserModel u = new UserModel(user, email, pass);
		try {
			u.update(true);
			logger.info("New user added/updated: " + user);
		} catch (MissingPropertiesException | ObjectAlreadyExistsException | NoPKFoundException e) {
			e.printStackTrace();
			throw new Exception("Error adding user: " + e.getMessage());
		}
		return true;
	}

	/**
	 * Loads a user from MongoDB
	 */
	public UserAuth loadUser(String user) {

		UserModel u = new UserModel(user);
		if (u.find(true)) {
			logger.info("User loaded: " + user);
			return  new UserAuth(u.getUser(), u.getPass(), u.getEmail());
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see wasota.core.authentication.UserAuthenticationInterface#getAuthenticatedUser()
	 */
	
	@Override
	public UserAuth getAuthenticatedUser() {
		// query the spring security to find out the authenticated user
		return loadUser(SecurityContextHolder.getContext().getAuthentication().getName());
	}

}
