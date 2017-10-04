package wasota.core.experiments.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import wasota.core.WasotaGraphStarter;
import wasota.core.authentication.UserAuth;
import wasota.core.exceptions.ExperimentNotFound;
import wasota.core.exceptions.UserNotAllowed;
import wasota.core.experiments.ExperimentsServiceInterface;
import wasota.core.graph.WasotaGraphInterface;
import wasota.core.models.WasotaPerformanceModel;
import wasota.mongo.collections.UserExperiment;
import wasota.mongo.exceptions.MissingPropertiesException;
import wasota.mongo.exceptions.NoPKFoundException;
import wasota.mongo.exceptions.ObjectAlreadyExistsException;

/**
 * Implementation of {@link ExperimentsServiceInterface}
 * 
 * @author Ciro Baron Neto
 * 
 *         Jul 3, 2016
 */
public class ExperimentServicesImpl implements ExperimentsServiceInterface {

	final static Logger logger = Logger.getLogger(ExperimentsServiceInterface.class);
	
	@Autowired
	WasotaGraphInterface wasotaGraph;

	@Override
	public Boolean isPublic(String experimentURI) throws ExperimentNotFound {
		
		UserExperiment experiment = new UserExperiment(experimentURI);
		if (experiment.find(true)) {
			if (experiment.getVisible()){
				return true;
			}
			else
				return false;
		}
		else
			throw new ExperimentNotFound();
		
	}

	@Override
	public Boolean changeExperimentState(String experimentURI, UserAuth user) throws UserNotAllowed {

		logger.info("Changing experiment: " + experimentURI);

		UserExperiment userExperiment = new UserExperiment(experimentURI);

		if (userExperiment.getUser().equals(user.getUser())) {
			if (userExperiment.getVisible())
				userExperiment.setVisible(false);
			else
				userExperiment.setVisible(true);

			try {
				userExperiment.update(false);
			} catch (MissingPropertiesException | ObjectAlreadyExistsException | NoPKFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		} else {
			logger.error("Failed! User: " + user.getUser() + " is not the experiment owner.");
			throw new UserNotAllowed("Failed! User: " + user.getUser() + " is not the experiment owner.");

		}
	}

	@Override
	public int numberOfExperiments() {

		List<WasotaPerformanceModel> performanceList = wasotaGraph.queries()
				.getAllFinalPerformanceList();
		Set<String> experimentURLs = new HashSet<>();

		for (WasotaPerformanceModel model : performanceList) {
			experimentURLs.add(model.url);
		}

		return experimentURLs.size();
	}

	@Override
	public List<String> getContextList(WasotaGraphInterface graph) {

		HashMap<String, String> l = graph.queries().getAllContext();
		
		for(String s : l.keySet())
			System.out.println(s);
		return null;
	}

}
