package wasota.core;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import wasota.core.exceptions.CannotAddMexNamespaces;
import wasota.core.exceptions.graph.NotPossibleToLoadGraph;
import wasota.core.exceptions.graph.NotPossibleToSaveGraph;
import wasota.core.graph.GraphStoreInterface;
import wasota.core.graph.WasotaGraphInterface;
import wasota.core.graph.impl.WasotaGraphJenaImpl;
import wasota.core.models.WasotaPerformanceModel;
import wasota.mongo.collections.UserExperiment;
import wasota.mongo.collections.UserGraph;
import wasota.mongo.exceptions.MissingPropertiesException;
import wasota.mongo.exceptions.NoPKFoundException;
import wasota.mongo.exceptions.ObjectAlreadyExistsException;
import wasota.utils.FileUtils;

@Service
public class GraphServiceImpl implements GraphServiceInterface {

	final static Logger logger = Logger.getLogger(GraphServiceImpl.class);

	@Autowired
	GraphStoreInterface graphStore;
	
	/**
	 * Create a new graph and save to the store.
	 * 
	 * @param graph
	 * @param namedGraph
	 * @return
	 * @throws NotPossibleToSaveGraph
	 */

	public Boolean createGraph(String graph, String namedGraph, String format) throws NotPossibleToSaveGraph {

		WasotaGraphInterface wasotaGraph = new WasotaGraphJenaImpl();
		try {
			wasotaGraph.readAsStream(new ByteArrayInputStream(graph.getBytes("UTF-8")), format);
			graphStore.saveGraph(namedGraph, wasotaGraph);

			logger.info("Graph uploaded/saved: " + namedGraph);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return true;
	}

	public Boolean createGraphWithUser(String graph, String namedGraph, String user, String format)
			throws NotPossibleToSaveGraph {

		WasotaGraphInterface wasotaGraph = new WasotaGraphJenaImpl();
		try {
			wasotaGraph.readAsStream(new ByteArrayInputStream(graph.getBytes("UTF-8")), format);
			graphStore.saveGraph(namedGraph, wasotaGraph);

			logger.info("Graph uploaded/saved: " + namedGraph);

			// update user/graph relation
			UserGraph userGraph = new UserGraph(namedGraph, FileUtils.stringToHash(namedGraph), user);
			userGraph.update(true);

			// now update the relation graph-experiment
			try {

				WasotaGraphInterface wasotaGraphImpl = WasotaGraphFactory.getNewGraph();
				wasotaGraphImpl.readAsStream(new ByteArrayInputStream(graph.getBytes("UTF-8")), format);
				wasotaGraphImpl.addMexNamespacesToModel();

				List<WasotaPerformanceModel> finalPerformanceList = wasotaGraphImpl.queries()
						.getAllFinalPerformanceList();

				for (WasotaPerformanceModel model : finalPerformanceList) {
					UserExperiment graphExperiment = new UserExperiment(model.url, user);
					graphExperiment.setVisible(true);
					graphExperiment.update(true);
					logger.info(model.url + " experiment added for user: '" + user + "'");
				}

			} catch (CannotAddMexNamespaces e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (UnsupportedEncodingException | MissingPropertiesException | ObjectAlreadyExistsException
				| NoPKFoundException e) {
			e.printStackTrace();
			throw new NotPossibleToSaveGraph(e.getMessage());
		}
		return true;
	}

	public WasotaGraphInterface loadGraph(String namedGraph) throws NotPossibleToLoadGraph {

		WasotaGraphInterface wasotaGraph = new WasotaGraphJenaImpl();

		graphStore.loadGraph(namedGraph, wasotaGraph, "ttl");

		logger.info("Graph loaded: " + namedGraph);

		return wasotaGraph;
	}

}
