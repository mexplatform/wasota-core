package wasota.core.graph;

import java.util.List;

import wasota.core.exceptions.graph.NotPossibleToLoadGraph;
import wasota.core.exceptions.graph.NotPossibleToSaveGraph;

/**
 * Interface with methods to deal where the graph will be stored and loaded. The implementation can be done over a file system, database, etc.
 * @author Ciro Baron Neto
 * 
 * Jul 3, 2016
 */
public interface GraphStoreInterface {
	
	/**
	 * Save the {@link WasotaGraphInterface} parameter to the store
	 * @param graphName
	 * @param graph
	 * @return boolean
	 * @throws NotPossibleToSaveGraph
	 */
	public Boolean saveGraph(String graphName, WasotaGraphInterface graph) throws NotPossibleToSaveGraph;

	/**
	 * Load a graph from the store to the {@link WasotaGraphInterface} parameter
	 * @param graphName - the name of the graph which will be loaded from the store
	 * @param graph - {@link WasotaGraphInterface} instance which will be written with the loaded graph content
	 * @param format - RDF serialization format
	 * @return boolean
	 * @throws NotPossibleToLoadGraph
	 */
	public Boolean loadGraph(String graphName, WasotaGraphInterface graph, String format) throws NotPossibleToLoadGraph;

	/**
	 * Return all graph names stored in the store
	 * @return List of string with the name os the name of the graphs
	 */
	public List<String> getAllGraphNames();

}
