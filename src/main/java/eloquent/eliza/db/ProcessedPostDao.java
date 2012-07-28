package eloquent.eliza.db;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.WriteConcern;

public class ProcessedPostDao {
	
	private static final String DB_IP="192.168.1.8";
	private static final String DB_NAME = "eliza";
	private static final String COLLECTION_NAME="processed_posts";
	
	private DBCollection dbCollection;
	
	private Logger logger = Logger.getLogger(getClass());
	
	public ProcessedPostDao(){
		try{
			Mongo mongo = new Mongo(DB_IP);
			DB db = mongo.getDB(DB_NAME);
			dbCollection = db.getCollection(COLLECTION_NAME);
		}
		catch(Exception ex){
			logger.error("Error connecting to DB", ex);
		}
	}
	
	/**
	 * Checks if the post exists in the database
	 * @param id
	 * @return
	 */
	public boolean contains(String id){
		BasicDBObject doc = new BasicDBObject();
		doc.put("postId", id);
		return (null!=dbCollection.findOne(doc));
	}
	
	/**
	 * Inserts the given post in the database
	 * @param post
	 */
	public void insert(ProcessedPost post){
		BasicDBObject doc = new BasicDBObject();
		doc.put("postId", post.getId());
		doc.put("from", post.getFrom().getName());
		doc.put("message", post.getMessage());
		dbCollection.insert(doc, WriteConcern.SAFE);
	}
}
