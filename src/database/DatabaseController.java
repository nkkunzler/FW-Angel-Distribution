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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.arangodb.ArangoCursor;
import com.arangodb.entity.BaseDocument;

import angels.Angel;
import angels.Attribute;
import javafx.concurrent.Task;

public class DatabaseController {

	private Database db;
	
	private static final ExecutorService exec = Executors.newFixedThreadPool(2, new ThreadFactory() {
		
		@Override
		public Thread newThread(Runnable r) {
			Thread t = Executors.defaultThreadFactory().newThread(r);
			t.setDaemon(true);
			return t;
		}
	});

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
	public Task<Boolean> createCollection(String collectionName) {
		Task<Boolean> task = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				if (isCancelled())
					return false;

				return db.createCollection(collectionName);
			}
		};
		
		exec.execute(task);

		return task;

	}

	/**
	 * Inserts a new angel within the desired collection within the database.
	 * 
	 * @param angel      The angel that will be added to the desired collection
	 * @param collection The collection in which to add the angel
	 * @return Task that returns true if the angel was successfully added to the
	 *         collection; otherwise false is returned.
	 */
	public Task<Boolean> insertAngel(Angel angel, String collection) {
		Task<Boolean> task = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				if (isCancelled())
					return false;

				return db.insert((String) angel.get(Attribute.ID),
						angel.getAttributes(), collection);
			}
		};
		
		exec.execute(task);

		return task;

	}

	/**
	 * Queries the database collection based on the query string.
	 * 
	 * @param query      The aql query command for the collection
	 * @param collection The collection used when quering the collection
	 * @return Task that returns a list containing the results of the query.
	 *         Null is returned if the query is inconclusive.
	 */
	public Task<List<Angel>> query(String query) {
		Task<List<Angel>> task = new Task<List<Angel>>() {
			// Executor will call this method when passing it the task
			@Override
			protected List<Angel> call() throws Exception {
				List<Angel> results = new ArrayList<>();

				if (isCancelled())
					return results;

				ArangoCursor<BaseDocument> documents = db.query(query);
				if (documents == null)
					return null;
				for (BaseDocument doc : documents) {
					Angel angel = new Angel();
					for (Attribute attr : Attribute.values()) {
						angel.addAttribute(attr,
								doc.getAttribute(attr.toString()));
					}
					results.add(angel);
				}
				return results;
			}
		};
		
		exec.execute(task);
		
		return task;
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
	public Task<Boolean> contains(String key, String collection) {
		Task<Boolean> task = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				if (isCancelled())
					return false;
				return db.contains(key, collection);
			}
		};
		
		exec.execute(task);

		return task;
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
	public Task<Void> update(String key, Object attribute, Object values,
			String collection) {
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				if (isCancelled())
					return null;

				String updateQuery = "UPDATE {_key: '" + key + "'} "
						+ "WITH {" + attribute.toString() + ": '" + values
						+ "'} "
						+ "IN " + collection;
				db.query(updateQuery);
				return null;
			}
		};
		
		exec.execute(task);

		return task;
	}

	/**
	 * Deletes a document with the given key from the desired collection.
	 * 
	 * @param key        The key of the document to delete from the collection.
	 * @param collection The collection to which to remove the key.
	 * @return A Task that returns true if the document with the desired key was
	 *         deleted; otherwise false is deleted.
	 */
	public Task<Boolean> delete(String key, String collection) {
		Task<Boolean> task = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				if (isCancelled())
					return false;
				return db.delete(key, collection);
			}
		};
		
		exec.execute(task);

		return task;
	}

	/**
	 * Closes the connection to the database.
	 */
	public void close() {
		exec.shutdownNow();
		db.shutdown();
	}
}
