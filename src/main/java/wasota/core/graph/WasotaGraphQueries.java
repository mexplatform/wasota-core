package wasota.core.graph;

import java.util.HashMap;
import java.util.List;

import wasota.core.models.WasotaPerformanceModel;

/**
 * Interface with the list of graph queries 
 * @author Ciro Baron Neto
 * 
 * Jul 3, 2016
 */
public interface WasotaGraphQueries {
	
	/**
	 * Return all contexts of the graph
	 * @return
	 */
	public HashMap<String, String> getAllContext();
	
	/**
	 * Get all performance measures of the graph
	 */
	public List<String> getPerformanceList();
	
	/**
	 * Get a list of algorithms based on a context
	 * 
	 * @param context
	 * @return
	 */
	public List<String> getAlgorithmList(String context);
	
	/**
	 * Get a list of experiment of a list of application
	 * 
	 * @param applicationList
	 * @return experimentList
	 */
	public List<String> getExperimentList(List<String> applicationList);
	
	/**
	 * Get experiment configuration of a list of experiment
	 * 
	 * @param experimentList
	 * @return experimentConfigList
	 */
	public List<String> getExperimentConfigList(List<String> experimentList);
	
	/**
	 * Get execution list of a experiment config list
	 * 
	 * @param experimentConfigList
	 * @return executionList
	 */
	public List<String> getExecutionList(List<String> experimentConfigList);
	
	/**
	 * Get measures of an execution list
	 * 
	 * @param executionList
	 * @return measureList
	 */
	public List<String> getMeasureList(List<String> executionList);
	
	
	/**
	 * Get all performance types
	 * 
	 * @param measureClassification
	 * @return list of performance type
	 */
	public List<String> getAllPerformanceTypes(List<String> measureClassification);
	
	
	/**
	 * Returns a list of {@link WasotaPerformanceModel} containing all performance values of a certain type. 
	 * @param measureClassification
	 * @param performanceType
	 * @return
	 */
	public List<WasotaPerformanceModel> getFinalPerformanceList(List<String> measureClassification,
			List<String> performanceType);
	
	/**
	 * Returns a list of {@link WasotaPerformanceModel} containing all performance values
	 * @return
	 */
	public List<WasotaPerformanceModel> getAllFinalPerformanceList();


}
