/**
 * 
 */
package wasota.core;

import java.util.List;

import wasota.core.authentication.UserAuth;
import wasota.core.models.WasotaPerformanceModel;

/**
 * @author Ciro Baron Neto
 * 
 * Jul 3, 2016
 */
public interface GraphUserServiceInterface {
	
	/**
	 * Load all graphs binded to a user
	 * @param user
	 * @return a list with the name of the graphs binded to the user 
	 */
	public List<String> getAllGraphs(UserAuth user);

	/**
	 * Return all experiments performance by user
	 * @param user
	 * @return
	 */
	public List<WasotaPerformanceModel> getAllPerformance(UserAuth user);
	
}
