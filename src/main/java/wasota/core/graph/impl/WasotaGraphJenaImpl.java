package wasota.core.graph.impl;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URL;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import wasota.core.exceptions.CannotAddMexNamespaces;
import wasota.core.graph.WasotaGraphInterface;
import wasota.core.graph.WasotaGraphQueries;
import wasota.properties.WasotaProperties;
import wasota.utils.Formats;

@Service
@Scope(value="singleton")
public class WasotaGraphJenaImpl implements WasotaGraphInterface {

	final static Logger logger = Logger.getLogger(WasotaGraphJenaImpl.class);

	Model model = ModelFactory.createDefaultModel();

	WasotaGraphQueries queries = new WasotaGraphQueriesImpl(model);

	String graphName;

	@Override
	public Boolean writeAsStream(OutputStream out) {
		if (model.write(out, "TTL") != null)
			return true;
		return false;
	}

	@Override
	public void readAsStream(InputStream in, String format) {
		model.read(in, null, Formats.getJenaFormat(format));
	}

	@Override
	public Boolean writeAsString(StringWriter strWriter) {
		if (model.write(strWriter, "TTL") != null)
			return true;
		return false;
	}

	/**
	 * Start the main model adding MEX namespaces
	 * 
	 * @throws CannotAddMexNamespaces
	 */
	@Override
	public void addMexNamespacesToModel() throws CannotAddMexNamespaces {

		try {
			mergeGraph(new URL(WasotaProperties.MEX_CORE_DOWNLOAD).openConnection().getInputStream());
			mergeGraph(new URL(WasotaProperties.MEX_PERF_DOWNLOAD).openConnection().getInputStream());
			mergeGraph(new URL(WasotaProperties.MEX_ALGO_DOWNLOAD).openConnection().getInputStream());

			logger.debug("MEX namespaces added to graph.");

		} catch (Exception e1) {
			e1.printStackTrace();
			throw new CannotAddMexNamespaces("We couldn't load the MEX namespaces to the Model.");
		}

	}

	/**
	 * Merge a JENA graph (as inputstream) to model
	 * 
	 * @param graphStream
	 */
	public void mergeGraph(InputStream graphStream) {
		Model m = ModelFactory.createDefaultModel();
		m.read(graphStream, null, "TTL");
		model.add(m);
	}

	@Override
	public WasotaGraphQueries queries() {
		return queries;
	}

}
