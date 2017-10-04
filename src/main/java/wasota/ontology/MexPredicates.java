package wasota.ontology;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.vocabulary.DCTerms;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

public class MexPredicates {
	
	public static final Property type = RDF.type;
	
	public static final Property identifier = DCTerms.identifier;

	public static final Property title = DCTerms.title;

	public static final Property label = RDFS.label;

	public static final Property domain = RDFS.domain;

	
	public static final Property subClassOf = RDFS.subClassOf;

	public static final Property wasAttributedTo = property(MexNS.PROV, "wasAttributedTo");

	public static final Property wasStartedBy = property(MexNS.PROV, "wasStartedBy");
	
	public static final Property wasInformedBy = property(MexNS.PROV, "wasInformedBy");

	public static final Property wasGeneratedBy = property(MexNS.PROV, "wasGeneratedBy");

	public static final Property used = property(MexNS.PROV, "used");

		
	public static final Resource MEX_ALGORITHM = resource(MexNS.MEX_ALGO, "Algorithm");
	
	public static final Property HAS_ALGORITHM_CLASS= property(MexNS.MEX_ALGO, "hasAlgorithmClass");
	
	public static final Resource MEX_CONTEXT = resource(MexNS.MEX_CORE, "Context");

	public static final Resource MEX_PERFORMANCE_MEASURE = resource(MexNS.MEX_PERF, "PerformanceMeasure");

	
	
	protected static final Resource resource(String ns, String local) {
		return ResourceFactory.createResource(ns + local);
	}

	protected static final Property property(String ns, String local) {
		return ResourceFactory.createProperty(ns, local);
	}
	
}
