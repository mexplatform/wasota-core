package wasota.ontology;

public class MexNS {

	final public static String MEX_ALGO = "http://mex.aksw.org/mex-algo#";
	final public static String MEX_CORE = "http://mex.aksw.org/mex-core#";
	final public static String MEX_PERF = "http://mex.aksw.org/mex-perf#";
	final public static String PROV = "http://www.w3.org/ns/prov#";

	public static String getQueryPrefixes(){
		
		return "PREFIX owl: <http://www.w3.org/2002/07/owl#> "+
				"PREFIX this: <http://mex.aksw.org/examples/> "+
				"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "+
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "+
				"PREFIX dct: <http://purl.org/dc/terms/> "+
				"PREFIX mexcore: <http://mex.aksw.org/mex-core#> "+
				"PREFIX doap: <http://usefulinc.com/ns/doap#> "+
				"PREFIX mexperf: <http://mex.aksw.org/mex-perf#> "+
				"PREFIX mexalgo: <http://www.w3.org/ns/dcat#> "+
				"PREFIX dcat: <http://mex.aksw.org/examples/> "+
				"PREFIX prov: <http://www.w3.org/ns/prov#> "+
				"PREFIX foaf: <http://xmlns.com/foaf/0.1/> "+
				"PREFIX dc: <http://purl.org/dc/elements/1.1/> "
				+ "";
		}

}
