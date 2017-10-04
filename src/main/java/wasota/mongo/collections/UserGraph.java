package wasota.mongo.collections;

import wasota.mongo.MongoSuperClass;

public class UserGraph extends MongoSuperClass {

	public static final String GRAPH_NAME = "graph";

	public static final String FILE = "file";

	public static final String USER = "user";

	public static final String VISIBLE = "visible";

	public static final String COLLECTION = "userGraph";

	public UserGraph(String graph, String file, String user) {
		super(COLLECTION);
		setFile(file);
		setUser(user);
		setGraphName(graph);
		addKey(GRAPH_NAME);
	}
	
	public UserGraph(String user) {
		super(COLLECTION);
		setUser(user);
		search();
		addKey(GRAPH_NAME);
	}

	public String getGraphName() {
		return getField(GRAPH_NAME).toString();
	}

	public String getFile() {
		return getField(FILE).toString();
	}

	public String getUser() {
		return getField(USER).toString();
	}

	public String getVisible() {
		return getField(VISIBLE).toString();
	}

	
	public void setGraphName(String graph){
		addField(GRAPH_NAME, graph);
	}
	
	public void setFile(String file){
		addField(FILE, file);
	}
	
	public void setUser(String user){
		addField(USER, user);
	}
	
	public void setVisible(String visible){
		addField(VISIBLE, visible);
	}
	
	
	
	
}
