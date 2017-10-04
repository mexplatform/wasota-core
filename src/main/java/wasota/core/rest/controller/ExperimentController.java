/**
 * 
 */
package wasota.core.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import wasota.core.WasotaGraphStarter;
import wasota.core.WasotaGraphFactory;
import wasota.core.exceptions.CannotAddMexNamespaces;
import wasota.core.exceptions.graph.NotPossibleToLoadGraph;
import wasota.core.exceptions.graph.NotPossibleToSaveGraph;
import wasota.core.experiments.ExperimentsServiceInterface;
import wasota.core.graph.GraphStoreInterface;
import wasota.core.graph.WasotaGraphInterface;
import wasota.core.models.WasotaPerformanceModel;
import wasota.rest.messages.WasotaRestModel;
import wasota.rest.messages.WasotaRestMsg;

/**
 * @author Ciro Baron Neto
 * 
 *         Jul 4, 2016
 */

@RestController
public class ExperimentController {

	@Autowired
	GraphStoreInterface graphStore;

	@Autowired
	ExperimentsServiceInterface experimentService;
	
	@Autowired
	WasotaGraphInterface wasotaGraph;

	/**
	 * Return the number of experiments
	 * 
	 * @return
	 */
	@RequestMapping(value = "/experiments/size", method = RequestMethod.GET)
	public WasotaRestModel getExperimentSize() {

		int numberOfExperiments = experimentService.numberOfExperiments();
		WasotaRestModel msg = new WasotaRestModel(WasotaRestMsg.OK, String.valueOf(numberOfExperiments).toString());
		return msg;

	}

	/**
	 * Return experiment list
	 * 
	 * @return
	 */
	@RequestMapping(value = "/experiments/list", method = RequestMethod.GET)
	public List<WasotaPerformanceModel> getExperimentList() {

		return wasotaGraph.queries().getAllFinalPerformanceList();

	}

	/**
	 * Return context list of a graph
	 * 
	 * @return
	 * @throws NotPossibleToSaveGraph
	 * @throws NotPossibleToLoadGraph
	 * @throws CannotAddMexNamespaces
	 */
	@RequestMapping(value = "/experiments/graphContext", method = RequestMethod.GET)
	public List<String> getGraphContextList(@RequestParam(value = "graphName", required = true) String graphName)
			throws NotPossibleToLoadGraph, CannotAddMexNamespaces {

		WasotaGraphInterface graph = WasotaGraphFactory.getNewGraph();
		graphStore.loadGraph(graphName, graph, "ttl");

		experimentService.getContextList(graph);

		return null;

	}

}
