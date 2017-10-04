package wasota.core.graph.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import wasota.core.WasotaGraphStarter;
import wasota.core.exceptions.graph.NotPossibleToLoadGraph;
import wasota.core.exceptions.graph.NotPossibleToSaveGraph;
import wasota.core.graph.GraphStoreInterface;
import wasota.core.graph.WasotaGraphInterface;
import wasota.properties.WasotaProperties;
import wasota.utils.FileUtils;

public class GraphStoreFSImpl implements GraphStoreInterface {

	final static Logger logger = Logger.getLogger(GraphStoreInterface.class);
 
	private HashMap<String, String> hashAndGraphNameMap = new HashMap<String, String>();
	
	@Autowired
	WasotaGraphInterface wasotaGraph;

	@Override
	public Boolean loadGraph(String namedGraph, WasotaGraphInterface graph, String format)
			throws NotPossibleToLoadGraph {

		// get file
		String path = WasotaProperties.GRAPH_FOLDER_PATH + "/" + FileUtils.stringToHash(namedGraph);

		File file = new File(path);

		if (file.exists()) {
			try { 
				graph.readAsStream(new FileInputStream(file), format);
				logger.info("Graph '" + namedGraph + "' loaded from store.");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				throw new NotPossibleToLoadGraph(e.getMessage());
			}
			return true;
		}
		throw new NotPossibleToLoadGraph("Not possible to find the graph in the FS. Path: " + path);

	}

	@Override
	public Boolean saveGraph(String namedGraph, WasotaGraphInterface graph) throws NotPossibleToSaveGraph {
		try {

			// hash of the graph name
			String hash = FileUtils.stringToHash(namedGraph);

			// save dataset sending an outputstream from the FS
			FileOutputStream fos = new FileOutputStream(new File(WasotaProperties.GRAPH_FOLDER_PATH + "/" + hash));

			graph.writeAsStream(fos);

			fos.close();

			// save hash in the main index (so we can keep the hash reference)
			addDatasetToIndex(hash, namedGraph);
			logger.info("Graph '" + namedGraph + "' saved in the store.");

			StringWriter graphContent = new StringWriter();
			graph.writeAsString(graphContent);

			InputStream stream = new ByteArrayInputStream(graphContent.toString().getBytes(StandardCharsets.UTF_8));

			wasotaGraph.mergeGraph(stream);

			logger.info("Graph '" + namedGraph + "' merged with main wasota graph.");

		} catch (IOException e) {
			e.printStackTrace();
			throw new NotPossibleToSaveGraph(e.getMessage());
		}
		return null;
	}

	private void saveHashName() {
		try {
			FileOutputStream fos = new FileOutputStream(new File(WasotaProperties.INDEX_PATH));
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(hashAndGraphNameMap);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadHashName() {
		try {
			File f = new File(WasotaProperties.INDEX_PATH);
			if (!f.exists())
				return;
			FileInputStream fis = new FileInputStream(new File(WasotaProperties.INDEX_PATH));
			ObjectInputStream ios = new ObjectInputStream(fis);
			hashAndGraphNameMap = (HashMap<String, String>) ios.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void addDatasetToIndex(String hash, String namedGraph) {
		hashAndGraphNameMap.put(hash, namedGraph);
		saveHashName();
	}

	@Override
	public List<String> getAllGraphNames() {
		loadHashName();
		List<String> list = new ArrayList<String>();
		list.addAll(hashAndGraphNameMap.values());
		return list;
	}

}
