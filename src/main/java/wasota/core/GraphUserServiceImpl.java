/**
 * 
 */
package wasota.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import wasota.core.authentication.UserAuth;
import wasota.core.exceptions.CannotAddMexNamespaces;
import wasota.core.exceptions.ExperimentNotFound;
import wasota.core.exceptions.graph.NotPossibleToLoadGraph;
import wasota.core.experiments.ExperimentsServiceInterface;
import wasota.core.graph.GraphStoreInterface;
import wasota.core.graph.WasotaGraphInterface;
import wasota.core.models.WasotaPerformanceModel;
import wasota.mongo.collections.UserGraph;

/**
 * @author Ciro Baron Neto
 * 
 *         Jul 3, 2016
 */

@Service
public class GraphUserServiceImpl implements GraphUserServiceInterface {
	
	@Autowired
	GraphStoreInterface graphStore;
	
	@Autowired
	ExperimentsServiceInterface experimentService;

	@Override
	public List<String> getAllGraphs(UserAuth user) {

		List<String> graphs = new ArrayList<>();

		// get graphs
		DBCursor graphList = UserGraph.getDBInstance().getCollection(UserGraph.COLLECTION)
				.find(new BasicDBObject(UserGraph.USER, user.getUser()));

		for (DBObject graph : graphList) {
			graphs.add(graph.get(UserGraph.GRAPH_NAME).toString());

		}
		return graphs;
	}

	@Override
	public List<WasotaPerformanceModel> getAllPerformance(UserAuth user) {
		List<String> graphNames = getAllGraphs(user);
		List<WasotaPerformanceModel> performanceList = new ArrayList<>();
		HashMap<String, WasotaPerformanceModel> map = new HashMap<String, WasotaPerformanceModel>();

		for (String graphName : graphNames) {
			WasotaGraphInterface wasotaGraph = WasotaGraphFactory.getNewGraph();
			try {
				wasotaGraph.addMexNamespacesToModel();
			} catch (CannotAddMexNamespaces e1) {
				e1.printStackTrace();
			}
			try {
				graphStore.loadGraph(graphName, wasotaGraph, "ttl");

			} catch (NotPossibleToLoadGraph e) {
				e.printStackTrace();
			}
			performanceList.addAll(wasotaGraph.queries().getAllFinalPerformanceList());
		}
		
		// remove duplicates
		for(WasotaPerformanceModel model : performanceList){
			map.put(model.url, model);
		}
		performanceList = new ArrayList<>();

		for(WasotaPerformanceModel mdl : map.values())
			performanceList.add(mdl);
		
		for(WasotaPerformanceModel model : performanceList){
			try {
				if(!experimentService.isPublic(model.url))
					model.visible = false;
			} catch (ExperimentNotFound e) {
				model.visible = true;
			}
		}
		
		return performanceList;
	}

}
