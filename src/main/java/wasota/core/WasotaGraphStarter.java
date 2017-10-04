/**
 * 
 */
package wasota.core;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import wasota.core.exceptions.CannotAddMexNamespaces;
import wasota.core.exceptions.graph.NotPossibleToLoadGraph;
import wasota.core.graph.GraphStoreInterface;
import wasota.core.graph.WasotaGraphInterface;

/**
 * @author Ciro Baron Neto
 * 
 *         Jul 3, 2016
 */

public class WasotaGraphStarter {

	final static Logger logger = Logger.getLogger(WasotaGraphStarter.class);

	@Autowired
	GraphStoreInterface graphStore;
	
	@Autowired
	WasotaGraphInterface wasotaGraph;

	public void go() {

		try {

			// add MEX namespaces to graph
			wasotaGraph.addMexNamespacesToModel();
			logger.info("MEX vocabulaies loaded.");

			List<String> graphNames = graphStore.getAllGraphNames();

			// load all stored graphs
			for (String namedGraph : graphNames) {
				graphStore.loadGraph(namedGraph, wasotaGraph, "ttl");
			}

			logger.info("Loaded " + graphNames.size() + " graphs.");

		} catch (CannotAddMexNamespaces | NotPossibleToLoadGraph e) {
			e.printStackTrace();
		}

	}

}
