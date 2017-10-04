package wasota.mongo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.BulkWriteResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import wasota.mongo.exceptions.MissingPropertiesException;
import wasota.mongo.exceptions.NoPKFoundException;
import wasota.mongo.exceptions.ObjectAlreadyExistsException;
import wasota.properties.WasotaProperties;

public class MongoSuperClass {

	// defining mongodb connection
	protected static MongoClient mongo = null;

	// defining mongodb database
	private static DB db;

	// defining collection name
	// @JsonIgnore
	public String COLLECTION_NAME = null;

	// defining mongodb _id
	// @JsonIgnore
	public static String ID = "_id";

	// defining PrimaryKeys field
	// @JsonIgnore
	public ArrayList<Object> keys = new ArrayList<Object>();

	// mongodb collection object
	protected DBCollection collection;

	// mongodb object the will persist with this class
	public DBObject mongoDBObject = new BasicDBObject();

	// list of mandatory fields to check before store the object
	protected ArrayList<String> mandatoryFields = new ArrayList<String>();

	protected void addMandatoryField(String field) {
		mandatoryFields.add(field);
	}

	public MongoSuperClass(String collectionName) {
		this.COLLECTION_NAME = collectionName;
	}

	public MongoSuperClass(String collectionName, DBObject obj) {
		this.COLLECTION_NAME = collectionName;
		this.mongoDBObject = obj;
	}

	// public DBSuperClass2() {
	// }

	// add pair key/value to the persistence object
	protected void addField(String key, String val) {
		mongoDBObject.put(key, val);
	}

	// add pair key/value to the persistence object
	protected void addField(String key, Object val) {
		mongoDBObject.put(key, val);
	}

	protected void addField(String key, Double val) {
		mongoDBObject.put(key, val);
	}

	// add pair key/value to the persistence object
	protected void addField(String key, int val) {
		mongoDBObject.put(key, val);
	}

	// add pair key/value to the persistence object
	protected void addField(String key, boolean val) {
		mongoDBObject.put(key, val);
	}

	// get a value given a key
	protected Object getField(String key) {
		return mongoDBObject.get(key);
	}

	// get a value given a key
	protected Object getField(Integer key) {
		return mongoDBObject.get(String.valueOf(key));
	}

	// add pair key/value to the persistence object
	protected void addField(String key, ArrayList<Integer> val) {
		mongoDBObject.put(key, val);
	}

	static public DBCollection getCollection(String collection) {
		return getDBInstance().getCollection(collection);
	}

	// get mongobd db instance
	public static DB getDBInstance() {
		try {
			if (mongo == null) { 
				if (WasotaProperties.MONGODB_DB == null)
					new WasotaProperties().loadProperties();
				if (WasotaProperties.MONGODB_SECURE_MODE) {
					MongoCredential credential = MongoCredential.createMongoCRCredential(
							WasotaProperties.MONGODB_USERNAME, WasotaProperties.MONGODB_DB,
							WasotaProperties.MONGODB_PASSWORD.toCharArray());
					mongo = new MongoClient(new ServerAddress(WasotaProperties.MONGODB_HOST), Arrays.asList(credential));
				} else {
					mongo = new MongoClient(WasotaProperties.MONGODB_HOST, WasotaProperties.MONGODB_PORT);
				}
				db = mongo.getDB(WasotaProperties.MONGODB_DB);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return db;
	} 

	/**
	 * Insert a new object in MongoDB database
	 * 
	 * @param checkBeforeInsert
	 *            query database and only insert if object is not there.
	 * @throws ObjectAlreadyExistsException
	 * @throws NoPKFoundException
	 */
	public void insert(boolean checkBeforeInsert) throws ObjectAlreadyExistsException, NoPKFoundException {

		if (checkBeforeInsert)
			if (find(false))
				throw new ObjectAlreadyExistsException(
						"Can't save object with PK: " + getKeys() + ". Object already exists.");

		// saving object to mongodb
		getCollection().insert(mongoDBObject);
	} 

	/**
	 * Query a object based on the list of PKs.
	 * 
	 * @param update
	 *            update object case found.
	 * @return true case the object is found.
	 */
	public boolean find(boolean update) {

		BasicDBList list = new BasicDBList();

		for (Object pk : getKeys()) {
			if (pk instanceof String) {
				String pks = (String) pk;
				if (getField(pks) != null) {
					list.add(new BasicDBObject(pks, getField(pks)));
				}
			} else if (pk instanceof Integer) {
				Integer pks = (Integer) pk;
				if (getField(pks) != null) {
					list.add(new BasicDBObject(String.valueOf(pks), getField(pks)));
				}
			}
		}

		if (list.size() == 0)
			return false;

		DBCursor cursor = getCollection().find(new BasicDBObject("$or", list));

		if (cursor.size() > 0) {
			if (update) {
				mongoDBObject = cursor.next();
			}
			return true;
		} else
			return false;
	}

	/**
	 * Query a object based on the a key
	 * 
	 * @param update
	 *            update object case found.
	 * @return true case the object is found.
	 */
	public boolean find(Boolean update, String key, Object value) {

		DBCursor cursor = getCollection().find(new BasicDBObject(key, value));

		if (cursor.size() > 0) {
			if (update) {
				mongoDBObject = cursor.next();
			}
			return true;
		} else
			return false;
	}

	/**
	 * Update a object based on the key
	 * 
	 * @param create
	 *            create object case not found.
	 * @return true case the object is found.
	 */
	public void update(Boolean create, String key, Object value) {

		try {
			checkMandatoryFields();
			DBCursor cursor = getCollection().find(new BasicDBObject(key, value));

			if (cursor.size() > 0) {
				// mongoDBObject = cursor.next();
				getCollection().update(new BasicDBObject(key, value), mongoDBObject);
			} else {
				if (create) {
					getCollection().insert(mongoDBObject);
				}
			}

		} catch (MissingPropertiesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Update an object.
	 * 
	 * @param create
	 *            set whether the object should be created case doesn't exist.
	 * @return true case successfully updated
	 * @throws MissingPropertiesException
	 * @throws NoPKFoundException
	 * @throws ObjectAlreadyExistsException
	 */
	public boolean update(boolean create)
			throws MissingPropertiesException, ObjectAlreadyExistsException, NoPKFoundException {

		if (create)
			if (!find(false)) {
				insert(false);
				return true;
			}

		checkMandatoryFields();
		BasicDBList list = new BasicDBList();

		for (Object pk : getKeys()) {
			if (pk instanceof String) {
				String pks = (String) pk;
				if (getField(pks) != null) {
					list.add(new BasicDBObject(pks, getField(pks)));
				}
			} else if (pk instanceof Integer) {
				Integer pks = (Integer) pk;
				if (getField(pks) != null) {
					list.add(new BasicDBObject(String.valueOf(pks), getField(pks)));
				}
			}
		}

		getCollection().update(new BasicDBObject("$or", list), mongoDBObject);
		find(true);

		return true;
	}

	public boolean updateBasedOnKeys(boolean create)
			throws MissingPropertiesException, ObjectAlreadyExistsException, NoPKFoundException {

		checkMandatoryFields();

		if (create)
			if (!find(false))
				insert(false);

		BasicDBList list = new BasicDBList();

		for (Object pk : getKeys()) {
			if (pk instanceof String) {
				String pks = (String) pk;
				if (getField(pks) != null) {
					list.add(new BasicDBObject(pks, getField(pks)));
				}
			} else if (pk instanceof Integer) {
				Integer pks = (Integer) pk;
				if (getField(pks) != null) {
					list.add(new BasicDBObject(String.valueOf(pks), getField(pks)));
				}
			}
		}

		getCollection().update(new BasicDBObject("$and", list), mongoDBObject);

		return true;
	}

	/**
	 * Remove an object
	 * 
	 * @return true case successfully removed
	 * @throws MissingPropertiesException
	 */
	public boolean remove() throws MissingPropertiesException {
		checkMandatoryFields();
		DBCursor d = collection.find(mongoDBObject);
		if (d.hasNext()) {
			collection.remove(d.next());
			return true;
		}
		return false;
	}

	/**
	 * MongoDB bulk save
	 * 
	 * @return
	 */

	protected boolean bulkSave2(List<DBObject> objects) {
		BulkWriteOperation builder = getCollection().initializeUnorderedBulkOperation();
		for (DBObject doc : objects) {
			builder.insert(doc);
		}
		BulkWriteResult result = builder.execute();
		return result.isAcknowledged();
	}

	// @JsonIgnore
	protected DBCollection getCollection() {
		if (collection == null) {
			collection = getDBInstance().getCollection(COLLECTION_NAME);
		}
		return collection;
	}

	protected DBObject search() {
		DBCursor d = collection.find(mongoDBObject);
		if (d.hasNext())
			return d.next();
		return null;
	}

	// @JsonIgnore
	public ArrayList<Object> getKeys() {
		return keys;
	}

	protected void addKey(String key) {
		keys.add(key);
	}

	private void checkMandatoryFields() throws MissingPropertiesException {
		for (String field : mandatoryFields) {
			checkField(field);
		}
	}

	private boolean checkField(String key) throws MissingPropertiesException {
		if (mongoDBObject.get(key) == null)
			throw new MissingPropertiesException("Missing filed: " + key.toString());
		return true;
	}

	public String getID() {
		if (getField(ID) != null)
			return getField(ID).toString();
		else
			return null;
	}

	public void setID(Object id) {
		addField(ID, id);
	}
	
	public ArrayList<DBObject> getAll(){ 
		DBCursor cursor  = getCollection().find();
		
		ArrayList<DBObject> list = new ArrayList<DBObject>();
		while(cursor.hasNext()){
			DBObject obj = cursor.next();
			list.add(obj);
		}
		return list;
	}
	
	public ArrayList<DBObject> getAll(BasicDBObject query){ 
		DBCursor cursor  = getCollection().find(query);
		
		ArrayList<DBObject> list = new ArrayList<DBObject>();
		while(cursor.hasNext()){
			DBObject obj = cursor.next();
			list.add(obj);
		}
		return list;
	}

}
