package wasota.core.graph.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;

import wasota.core.graph.WasotaGraphQueries;
import wasota.core.models.WasotaPerformanceModel;
import wasota.ontology.MexPredicates;

public class WasotaGraphQueriesImpl implements WasotaGraphQueries {
	
	final static Logger logger = Logger.getLogger(WasotaGraphQueries.class);

	Model model;

	public WasotaGraphQueriesImpl(Model model) {
		this.model = model;
	}

	/**
	 * making the extractor private will not allow users to instantiate without
	 * a model
	 */
	private WasotaGraphQueriesImpl() {
	}

	public HashMap<String, String> getAllContext() {

		HashMap<String, String> context = new HashMap<String, String>();

		// get all mex context

		HashSet<String> allContext = new HashSet<String>(); 

		StmtIterator contextIt = model.listStatements(null, MexPredicates.subClassOf, MexPredicates.MEX_CONTEXT);

		while (contextIt.hasNext()) {
			Statement stmt = contextIt.next();
			allContext.add(stmt.getSubject().toString());
//			System.out.println(stmt.getSubject().toString());
		}

		// for each context
		for (String _context : allContext) {

			// search for some context
			StmtIterator contextList = model.listStatements(null, null, ResourceFactory.createResource(_context));
			 System.out.println(_context.toString());
			

			while (contextList.hasNext()) {
				Statement r = contextList.next();
				StmtIterator identifier = model.listStatements(r.getSubject(), MexPredicates.label, (RDFNode) null);
				if (identifier.hasNext()) {
					String label = identifier.next().getObject().toString();
					context.put(r.getObject().toString(), label);
				}
				// System.out.println(label);
			}

		}
		return context;
	}

	/**
	 * Get all performance measures
	 */
	public List<String> getPerformanceList() {

		List<String> performanceList = new ArrayList<String>();

		// ---------- list of performance values from ontology------------
		// get all measures

		StmtIterator measures = model.listStatements(null, MexPredicates.subClassOf,
				MexPredicates.MEX_PERFORMANCE_MEASURE);

		while (measures.hasNext()) {
			StmtIterator m = model.listStatements(null, MexPredicates.domain,
					measures.next().getSubject().asResource());
			while (m.hasNext()) {
				performanceList.add(m.next().getSubject().toString());
			}
		}
		return performanceList;
	}

	/**
	 * Get algorithms based on a context
	 * 
	 * @param context
	 * @return
	 */
	public List<String> getAlgorithmList(String context) {
		// get all application for the context
		StmtIterator contextList = model.listStatements(null, MexPredicates.type,
				ResourceFactory.createResource(context));

		List<String> appList = new ArrayList<String>();

		// get list of applications
		while (contextList.hasNext()) {
			StmtIterator applications = model.listStatements(contextList.next().getSubject(),
					MexPredicates.wasAttributedTo, (RDFNode) null);

			while (applications.hasNext())
				appList.add(applications.next().getObject().toString());

		}
		return appList;
	}

	/**
	 * Get a list of experiment of a list of application
	 * 
	 * @param applicationList
	 * @return experimentList
	 */
	public List<String> getExperimentList(List<String> applicationList) {
		
		List<String> experimentList = new ArrayList<String>();
		for (String application : applicationList) {
			StmtIterator experimenIt = model.listStatements(null, MexPredicates.wasAttributedTo,
					ResourceFactory.createResource(application));
			while (experimenIt.hasNext()) {
				experimentList.add(experimenIt.next().getSubject().toString());
			}
		}

		return experimentList;
	}

	/**
	 * Get experiment configuration of a list of experiment
	 * 
	 * @param experimentList
	 * @return experimentConfigList
	 */
	public List<String> getExperimentConfigList(List<String> experimentList) {
		List<String> expListConfig = new ArrayList<String>();
		for (String exp : experimentList) {
			StmtIterator experimenConfigIt = model.listStatements(null, MexPredicates.wasStartedBy,
					ResourceFactory.createResource(exp));
			while (experimenConfigIt.hasNext()) {
				expListConfig.add(experimenConfigIt.next().getSubject().toString());
			}
		}
		return expListConfig;
	}

	/**
	 * Get execution list of a experiment config list
	 * 
	 * @param experimentConfigList
	 * @return executionList
	 */
	public List<String> getExecutionList(List<String> experimentConfigList) {
		List<String> executionList = new ArrayList<String>();
		for (String expConfig : experimentConfigList) {
			StmtIterator experimenConfigIt = model.listStatements(null, MexPredicates.wasInformedBy,
					ResourceFactory.createResource(expConfig));
			while (experimenConfigIt.hasNext()) {
				executionList.add(experimenConfigIt.next().getSubject().toString());
			}
		}

		return executionList;
	}

	/**
	 * Get measures of an execution list
	 * 
	 * @param executionList
	 * @return measureList
	 */
	public List<String> getMeasureList(List<String> executionList) {
		// get list measure classification
		List<String> measureClassification = new ArrayList<String>();
		for (String exp : executionList) {
			StmtIterator experimenTypeIt = model.listStatements(null, MexPredicates.wasGeneratedBy,
					ResourceFactory.createResource(exp));
			while (experimenTypeIt.hasNext()) {
				measureClassification.add(experimenTypeIt.next().getSubject().toString());
			}
		}
		return measureClassification;
	}

	/**
	 * get all performance types
	 * 
	 * @param measureClassification
	 * @return list of performance type
	 */
	public List<String> getAllPerformanceTypes(List<String> measureClassification) {

		List<String> performanceList = getPerformanceList();

		// get all performance types
		List<String> performanceListFinal = new ArrayList<String>();

		for (String measure : measureClassification) {
			StmtIterator perfTypeIt = model.listStatements(ResourceFactory.createResource(measure), null,
					(RDFNode) null);
			while (perfTypeIt.hasNext()) {
				String performanceType = perfTypeIt.next().getPredicate().toString();
				if (performanceList.contains(performanceType)) {
					performanceListFinal.add(performanceType);
				}
			}
		}

		return performanceListFinal;
	}

	public List<WasotaPerformanceModel> getFinalPerformanceList(List<String> measureClassification,
			List<String> performanceType) {

		List<WasotaPerformanceModel> performanceListFinal = new ArrayList<WasotaPerformanceModel>();

		for (String measure : measureClassification) {
						
			StmtIterator perfTypeIt = null;
			for (String per : performanceType) {
				perfTypeIt = model.listStatements(ResourceFactory.createResource(measure),
						ResourceFactory.createProperty(per), (RDFNode) null);
				
				Statement stmt = null;
				while (perfTypeIt.hasNext()) {
					stmt = perfTypeIt.next();

					WasotaPerformanceModel result = new WasotaPerformanceModel();
					result.performance = stmt.getPredicate().toString();
					result.performanceValue = stmt.getObject().toString().split("\\^")[0];

					result.url = stmt.getSubject().toString();

					StmtIterator stmeIt2 = model.listStatements(stmt.getSubject().asResource(),
							MexPredicates.wasGeneratedBy, (RDFNode) null);
					
					Statement measureStmt = stmeIt2.next();

					StmtIterator stmeIt = model.listStatements(measureStmt.getObject().asResource(), MexPredicates.used,
							(RDFNode) null);

					Statement stmtAlg = null;

					// get algorithms
					while (stmeIt.hasNext()) {
						stmtAlg = stmeIt.next();

						// get label
						StmtIterator algIt = model.listStatements(stmtAlg.getObject().asResource(), MexPredicates.label,
								(RDFNode) null);
						if (algIt.hasNext()) {
							Statement algIt2 = algIt.next();
							result.algorithmLbl = algIt2.getObject().toString();
						}

						algIt = model.listStatements(stmtAlg.getObject().asResource(),
								MexPredicates.HAS_ALGORITHM_CLASS, (RDFNode) null);
						if (algIt.hasNext()) {
							Statement algIt2 = algIt.next();
							result.algorithmClass = algIt2.getObject().toString();
						}
					}

					// get experiment detail

					StmtIterator it = model.listStatements(measureStmt.getObject().asResource(),
							MexPredicates.wasInformedBy, (RDFNode) null);

					// get experiment config
					StmtIterator it2 = model.listStatements(it.next().getObject().asResource(),
							MexPredicates.wasStartedBy, (RDFNode) null);

					// finally, getting experiment
					StmtIterator itEx = model.listStatements(it2.next().getSubject().asResource(),
							MexPredicates.wasStartedBy, (RDFNode) null);

					Statement experimentConfigStmt = itEx.next();

					Statement experimentStmt = model.listStatements(experimentConfigStmt.getSubject().asResource(),
							MexPredicates.wasStartedBy, (RDFNode) null).next();

					result.experimentID = model.listStatements(experimentStmt.getObject().asResource(),
							MexPredicates.identifier, (RDFNode) null).next().getObject().toString();

					result.experimentTitle = model.listStatements(experimentStmt.getObject().asResource(),
							MexPredicates.title, (RDFNode) null).next().getObject().toString();

					// get application
					StmtIterator itApp = model.listStatements(experimentStmt.getObject().asResource(),
							MexPredicates.wasAttributedTo, (RDFNode) null);

					result.userMail = model
							.listStatements(itApp.next().getObject().asResource(), FOAF.mbox, (RDFNode) null).next()
							.getObject().toString();

//					itApp = model.listStatements(experimentStmt.getObject().asResource(), MexPredicates.wasAttributedTo,
//							(RDFNode) null);

					performanceListFinal.add(result);

				}
			}
		}

		return performanceListFinal;

	}

	public List<WasotaPerformanceModel> getAllFinalPerformanceList() {

		List<WasotaPerformanceModel> returnList = new ArrayList<WasotaPerformanceModel>();
		
		for (String context : getAllContext().keySet()) {
			
			List<String> appList = getAlgorithmList(context);

			// get list of experiment
			List<String> expList = getExperimentList(appList);

			appList = null;
			
			// get list of experiment config
			List<String> expListConfig = getExperimentConfigList(expList);
			
			expList = null;
		
			// get list of execution
			List<String> executionList = getExecutionList(expListConfig);
			
			expListConfig = null;
			
			// get list measure classification
			List<String> measureClassification = getMeasureList(executionList);
			
			executionList = null;

			// get all performance types
			for (WasotaPerformanceModel w : getFinalPerformanceList(measureClassification, getAllPerformanceTypes(measureClassification))) {
				returnList.add(w);
			}
		}
		
		return returnList;

	}

}
