package wasota.core;

import wasota.core.exceptions.graph.NotPossibleToLoadGraph;
import wasota.core.exceptions.graph.NotPossibleToSaveGraph;
import wasota.core.graph.WasotaGraphInterface;

/**
 * Interface which offers useful methods for loading a graph, creating a graph, etc..
 * @author Ciro Baron Neto
 * 
 * Jul 3, 2016
 */

public interface GraphServiceInterface {
	
	/**
	 * Service to create a new graph object
	 * @param graph
	 * @param graphName
	 * @param format
	 * @return
	 * @throws NotPossibleToSaveGraph
	 */
	public Boolean createGraph(String graph, String graphName, String format) throws NotPossibleToSaveGraph;
	
	/**
	 * Service to create a graph and bind to a user
	 * @param graph
	 * @param graphName
	 * @param user
	 * @param format
	 * @return
	 * @throws NotPossibleToSaveGraph
	 */
	public Boolean createGraphWithUser(String graph, String graphName, String user, String format) throws NotPossibleToSaveGraph;
	
	/**
	 * Load a graph 
	 * @param graphName
	 * @return
	 * @throws NotPossibleToLoadGraph
	 */
	public WasotaGraphInterface loadGraph(String graphName) throws NotPossibleToLoadGraph;

	
}
