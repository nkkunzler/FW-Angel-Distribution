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

import main.Angel;

public class DatabaseController {

	private Database db;

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
	 * @return True if the collection was added; otherwise false is returned.
	 */
	public boolean createCollection(String collectionName) {
		return db.createCollection(collectionName);
	}

	/**
	 * Inserts a new angel within the desired collection within the database.
	 * 
	 * @param angel      The angel that will be added to the desired collection
	 * @param collection The collection in which to add the angel
	 * @return True if the angel was successfully added to the collection;
	 *         otherwise false is returned.
	 */
	public boolean insertAngel(Angel angel, String collection) {
		return db.insert(angel.get(Angel.Attribute.ID), angel.getAttributes(),
				collection);
	}

	/**
	 * Queries the database collection based on the query string.
	 * 
	 * @param query      The aql query command for the collection
	 * @param collection The collection used when quering the collection
	 * @return A list containing the results of the query. Null is
	 *         returned if the query is inconclusive.
	 */
	public List<BaseDocument> query(String query) {
		List<BaseDocument> results = new ArrayList<>();
		for (BaseDocument doc : db.query(query))
			results.add(doc);
		return results;
	}

	/**
	 * Returns a boolean indicating whether a given angel exists within the
	 * databases collection.
	 * 
	 * @param key      The key to check for within the collection
	 * @param collection The collection to check for the angel
	 * @return True if the angel exists within the collection; otherwise false
	 *         is returned.
	 */
	public boolean contains(String key, String collection) {
		return db.contains(key, collection);
	}

	/**
	 * Deletes a document with the given key from the desired collection.
	 * 
	 * @param key        The key of the document to delete from the collection.
	 * @param collection The collection to which to remove the key.
	 * @return True if the document with the desired key was deleted; otherwise
	 *         false is deleted.
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
