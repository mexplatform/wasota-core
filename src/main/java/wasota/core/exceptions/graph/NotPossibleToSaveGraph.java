package wasota.core.exceptions.graph;

/**
 * This exception should be used when some sort of error happened while saving a graph to the Store.
 * @author Ciro Baron Neto
 * 
 * Jul 3, 2016
 */
public class NotPossibleToSaveGraph extends Exception {
	
	public NotPossibleToSaveGraph() { super(); }
	
	public NotPossibleToSaveGraph(String message) { super(message); }  

}
