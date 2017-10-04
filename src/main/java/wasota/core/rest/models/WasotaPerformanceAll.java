package wasota.core.rest.models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import wasota.core.graph.WasotaGraphInterface;

@Component
@Scope(value="prototype")
public class WasotaPerformanceAll {

	@Autowired
	WasotaGraphInterface wasotaGraph;

	public Set<String> performanceList = new HashSet<String>();

	public void performanceByContext(String context) {

		List<String> appList = wasotaGraph.
				queries().getAlgorithmList(context);

		// get list of experiment
		List<String> expList = wasotaGraph.queries().getExperimentList(appList);

		// get list of experiment config
		List<String> expListConfig = wasotaGraph.queries().getExperimentConfigList(expList);

		// get list of execution
		List<String> executionList = wasotaGraph.queries().getExecutionList(expListConfig);

		// get list measure classification
		List<String> measureClassification = wasotaGraph.queries().getMeasureList(executionList);

		// get all performance types
		performanceList = new HashSet<String>(wasotaGraph.queries().getAllPerformanceTypes(measureClassification)); 

	}

}
