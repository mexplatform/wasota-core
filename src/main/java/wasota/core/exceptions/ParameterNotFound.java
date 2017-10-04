package wasota.core.exceptions;

/**
 * Exception to be used when some parameter is missing in a request
 * 
 * @author Ciro Baron Neto
 * 
 *         Jul 3, 2016
 */
public class ParameterNotFound extends Exception {

	public ParameterNotFound() {
		super();
	}

	public ParameterNotFound(String parameterName) {
		super("The parameter '" + parameterName + "' is required and was not found.");
	}

}
