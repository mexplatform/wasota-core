package wasota.core.exceptions.graph;

/**
 * This exception should be used when some sort of error happened while loading a graph from the Store.
 * @author Ciro Baron Neto
 * 
 * Jul 3, 2016
 */
public class NotPossibleToLoadGraph extends Exception {
	
	public NotPossibleToLoadGraph() { super(); }
	
	public NotPossibleToLoadGraph(String message) { super(message); }  

}
