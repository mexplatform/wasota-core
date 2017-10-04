package wasota.core.exceptions;

/**
 * Exception to be used when a user doesn't have permission to do some action
 * @author Ciro Baron Neto
 * 
 * Jul 3, 2016
 */
public class UserNotAllowed extends Exception {
	
	public UserNotAllowed() { super(); }
	
	public UserNotAllowed(String message) { super(message); }  

	
	
}
