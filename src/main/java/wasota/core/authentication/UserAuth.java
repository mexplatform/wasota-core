package wasota.core.authentication;

/**
 * Class which holds the basic data for the user authentication process
 * 
 * @author Ciro Baron Neto
 * 
 * Jul 3, 2016
 */

public class UserAuth {

	private String user;

	private String password;

	private String email;

	/**
	 * 
	 * Constructor for Class UserAuth 
	 * @param user
	 * @param password
	 * @param email
	 */
	public UserAuth(String user, String password, String email) {
		this.user = user;
		this.password = password;
		this.email = email;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user 
	 * Set the user value.
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            Set the password value.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            Set the email value.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

}
