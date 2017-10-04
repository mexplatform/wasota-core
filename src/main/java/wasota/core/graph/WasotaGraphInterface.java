package wasota.core.graph;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;

import wasota.core.exceptions.CannotAddMexNamespaces;

/**
 * Interface with the methods used to deal with the graph. The implementation can be done using triplestores (e.g. JENA, Virtuoso, etc).
 * @author Ciro Baron Neto
 * 
 * Jul 3, 2016
 */
public interface WasotaGraphInterface {
	
	/**
	 * Writes the content of the graph to the {@link OutputStream} parameter
	 * @param out
	 * @return 
	 */
	Boolean writeAsStream(OutputStream out);
	
	/**
	 * Writes the content of the graph to the {@link StringWriter} parameter
	 * @param strWriter
	 * @return
	 */
	Boolean writeAsString(StringWriter strWriter);

	/**
	 * Reads content from a {@link InputStream} and write to the graph
	 * @param in input Strem
	 * @param format RDF serialization format
	 */
	void readAsStream(InputStream in, String format);
	
	/**
	 * Load all MEX vocabularies to the graph
	 * @throws CannotAddMexNamespaces
	 */
	public void addMexNamespacesToModel() throws CannotAddMexNamespaces;
	
	
	/**
	 * Merge the {@link InputStream} paramenter to the graph
	 * @param graphStream
	 */
	public void mergeGraph(InputStream graphStream);
	
	
	/**
	 * Interface with the queries of a Wasota Graph
	 * @return
	 */
	public WasotaGraphQueries queries();
	
}
