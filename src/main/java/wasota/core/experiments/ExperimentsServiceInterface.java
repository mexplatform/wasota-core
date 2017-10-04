package wasota.core.experiments;

import java.util.List;

import wasota.core.authentication.UserAuth;
import wasota.core.exceptions.ExperimentNotFound;
import wasota.core.exceptions.UserNotAllowed;
import wasota.core.graph.WasotaGraphInterface;

/**
 * Interface with methods for experiments
 * @author Ciro Baron Neto
 * 
 * Jul 3, 2016
 */
public interface ExperimentsServiceInterface {

	/**
	 * Return if an experiment is public (not binded to a user) or private (it is binded to a user)
	 * @param experimentURI
	 * @return
	 */
	public Boolean isPublic(String experimentURI) throws ExperimentNotFound;
	
	/**
	 * Change an experiment to public or private
	 * @param experimentURI
	 * @param user - the authenticated user
	 * @return
	 */
	public Boolean changeExperimentState(String experimentURI, UserAuth user)  throws UserNotAllowed ;
	
	/**
	 * Return the number of experiments
	 * @return - number of experiments
	 */
	public int numberOfExperiments();
	
	
	/**
	 * Get a list of experiments URI of a graph
	 * @param graph
	 * @return
	 */
	public List<String> getContextList(WasotaGraphInterface graph) ;
	
	
	
}
