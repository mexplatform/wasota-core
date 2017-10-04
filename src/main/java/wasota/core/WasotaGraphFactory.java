package wasota.core;

import org.springframework.beans.factory.annotation.Autowired;

import wasota.core.exceptions.CannotAddMexNamespaces;
import wasota.core.graph.WasotaGraphInterface;

public class WasotaGraphFactory {
	
	@Autowired
	static WasotaGraphInterface wasotaGraph;
	
	public static WasotaGraphInterface getNewGraph() {
		try {
			WasotaGraphInterface graph = (WasotaGraphInterface) wasotaGraph.getClass().newInstance();
			graph.addMexNamespacesToModel();
			return graph;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (CannotAddMexNamespaces e) {
			e.printStackTrace();
		}
		return null;
	}
}
