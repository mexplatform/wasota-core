package wasota.mongo.collections;

import wasota.mongo.MongoSuperClass;

public class UserExperiment extends MongoSuperClass {

	private static final String USER = "user";

	private static final String VISIBLE = "visible";

	private static final String EXPERIMENT = "experiment";

	private static final String COLLECTION = "graphExperiment"; 

	public UserExperiment(String experiment, String user) {
		super(COLLECTION);
		addKey(EXPERIMENT);
		setUser(user);
		setExperiment(experiment);
	}
	
	public UserExperiment(String experiment) {
		super(COLLECTION);
		addKey(EXPERIMENT);
		setExperiment(experiment);
		find(true);
	}


	public String getUser() {
		return getField(USER).toString();
	}

	public Boolean getVisible() {
		return Boolean.valueOf(getField(VISIBLE).toString());
	}
	
	public String getExperiment() {
		return getField(EXPERIMENT).toString();
	}


	public void setUser(String user){
		addField(USER, user);
	}
	
	public void setVisible(Boolean visible){
		addField(VISIBLE, visible);
	}
	
	public void setExperiment(String experiment){
		addField(EXPERIMENT, experiment);
	}
	
	
	
	
}
