/**
 * This class is the controller to connect the view of the database to the
 * database model. This helps eliminate the need to change large amounts of
 * code when changing the model or the view.
 * 
 * @author Nicholas Kunzler
 */

package database;

import java.util.ArrayList;
import java.util.List;

import com.arangodb.ArangoCursor;
import com.arangodb.entity.BaseDocument;

import angels.Angel;
import angels.Attribute;

public class DatabaseController {

	private Database db;

	// final ExecutorService exec = new ThreadPoolExecutor(0, 1, 0,
	// TimeUnit.SECONDS, new SynchronousQueue<>());

	/**
	 * Creates a new database controller based off of a database. This allows
	 * for data entry and access.
	 * 
	 * @param db The database the controller will communicate with.
	 */
	public DatabaseController(Database db) {
		this.db = db;
	}

	/**
	 * Creates a new collection within the database with the desired name.
	 * 
	 * @param collectionName The name of the collection to add to the database.
	 * @return Task that returns true if the collection was added; otherwise
	 *         false is returned.
	 */
	public boolean createCollection(String collectionName) {
		return db.createCollection(collectionName);
	}

	/**
	 * Inserts a new angel within the desired collection within the database.
	 * 
	 * @param angel      The angel that will be added to the desired collection
	 * @param collection The collection in which to add the angel
	 * @return Task that returns true if the angel was successfully added to the
	 *         collection; otherwise false is returned.
	 */
	public boolean insertAngel(Angel angel, String collection) {
		return db.insert((String) angel.get(Attribute.ID),
				angel.getAttributes(), collection);
	}

	/**
	 * Queries the database collection based on the query string.
	 * 
	 * @param query      The aql query command for the collection
	 * @param collection The collection used when querying the collection
	 * @return Task that returns a list containing the results of the query.
	 *         Null is returned if the query is inconclusive.
	 */
	public List<Angel> query(String query) {
		// Executor will call this method when passing it the task
		List<Angel> results = new ArrayList<>();

		ArangoCursor<BaseDocument> documents = db.query(query);
		if (documents == null)
			return null;

		for (BaseDocument doc : documents)
			results.add(new Angel(doc));
		
		return results;
	}

	/**
	 * Returns a boolean indicating whether a given angel exists within the
	 * databases collection.
	 * 
	 * @param key        The key to check for within the collection
	 * @param collection The collection to check for the angel
	 * @return Task that returns true if the angel exists within the collection;
	 *         otherwise false is returned.
	 */
	public boolean contains(String key, String collection) {
		return db.contains(key, collection);
	}

	/**
	 * Updates the desired keys attribute with the new value within the provided
	 * collection.
	 * 
	 * @param key        String representing the key of the document being
	 *                   updated
	 * @param attribute  The attribute to update within the document.
	 * @param value      The value of the new attribute
	 * @param collection The collection in which to search for the key
	 * @return A Task that returns void. Nothing is returned.
	 */
	public void update(String key, Object attribute, Object values,
			String collection) {
		String updateQuery = "UPDATE {_key: '" + key + "'} "
				+ "WITH {" + attribute.toString() + ": '" + values
				+ "'} "
				+ "IN " + collection;
		db.query(updateQuery);
	}

	/**
	 * Deletes a document with the given key from the desired collection.
	 * 
	 * @param key        The key of the document to delete from the collection.
	 * @param collection The collection to which to remove the key.
	 * @return A Task that returns true if the document with the desired key was
	 *         deleted; otherwise false is deleted.
	 */
	public boolean delete(String key, String collection) {
		return db.delete(key, collection);
	}

	/**
	 * Closes the connection to the database.
	 */
	public void close() {
		db.shutdown();
	}
}
