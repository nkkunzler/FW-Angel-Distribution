/**
 * Class responsible for basic ArangoDB operations. These operations include:
 * 
 * contains() - Determines weather a key exists within the database
 * insert() - Adds a new document to a desired collection
 * delete() - Deletes a desired document from a collection
 * query() - Queries the database based off the AQL query language
 * createCollection() - creates a collection within the database
 * createDatabase() - creates a database
 * 
 * @author Nicholas Kunzler
 */

package database;

import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.entity.BaseDocument;

import customFX.Popup;
import javafx.scene.control.Alert;

public class Database {

	private String dbName;
	private ArangoDB arangoDB;

	public Database(String dbName, String user, String password) {
		this.dbName = dbName;
		arangoDB = new ArangoDB.Builder().user(user).password(password).build();
		createDatabase();
	}

	/**
	 * Returns weather a given documents key exists within the desired
	 * collection.
	 * 
	 * @param key        The key to search for within the collection
	 * @param collection The collection in which to search for the key
	 * @return True if the document exists within the collection; otherwise
	 *         false is returned.
	 */
	protected boolean contains(String key, String collection) {
		return arangoDB.db(dbName).collection(collection).documentExists(key);
	}

	/**
	 * Inserts a new document within the given collection. The document is based
	 * off the parameters, key and attributes.
	 * 
	 * @param key        The unique key assigned with the document
	 * @param attributes The attributes associated with said key
	 * @param collection The collection in which the document will be placed
	 * @return True if the document was successfully added; otherwise false is
	 *         returned.
	 */
	protected boolean insert(String key, BaseDocument object, String collection) {
		if (!contains(key, collection)) {
			try {
				arangoDB.db(dbName).collection(collection)
						.insertDocument(object);
			} catch (ArangoDBException e) {
				new Popup(Alert.AlertType.ERROR, e.getException(),
						"Internal database error occured when inserting");
			}
			return true;
		}
		
		return false;
	}

	/**
	 * Deletes an entry from a collection that matches the given key.
	 * 
	 * @param key        The key from the collection to delete
	 * @param collection The collection from where to delete the collection
	 * @return True if the entry was deleted; otherwise false is returned.
	 */
	protected boolean delete(String key, String collection) {
		try {
			arangoDB.db(dbName).collection(collection).deleteDocument(key);
			return true;
		} catch (ArangoDBException e) {
			new Popup(Alert.AlertType.ERROR, e.getException(),
					"Internal database error occured when deleting");
			return false;
		}
	}

	/**
	 * Queries the ArangoDB with the specified query within the collection.
	 * 
	 * @param query      The query in which to query the collection
	 * @param collection The collection on which the query is running
	 * @return ArangoCursor list containing the contents of the query. If the
	 *         query could not be executed, null is returned.
	 */
	protected ArangoCursor<BaseDocument> query(String query) {
		try {
			ArangoCursor<BaseDocument> cursor = arangoDB.db(dbName).query(query,
					BaseDocument.class);
			return cursor;
		} catch (ArangoDBException c) {
			new Popup(Alert.AlertType.ERROR, c.getException(),
					"Internal database error occured when querying:\n" + query);
			System.out.println("HI");
			return null;
		}
	}

	/**
	 * Creates a new collection within the database. If the collection already
	 * exists within the database then nothing happens.
	 * 
	 * A collection is similar to a table.
	 * 
	 * @param name The name of the collection to add
	 * 
	 * @return True if a new collection was added to the database; otherwise
	 *         false is returned.
	 */
	protected boolean createCollection(String name) {
		if (!arangoDB.db(dbName).collection(name).exists()) {
			// The collection does not exist so try to create it
			try {
				arangoDB.db(dbName).createCollection(name);
				return true;
			} catch (ArangoDBException e) {
				new Popup(Alert.AlertType.ERROR, e.getException(),
						"Internal database error occured when creating a collection named:\n"
								+ name);
			}
		}
		return false;
	}

	/**
	 * Creates a new database if the provided database does not exists.
	 * 
	 * @return True if a new database was created; otherwise false is returned.
	 */
	private boolean createDatabase() {
		if (!arangoDB.getDatabases().contains(dbName)) {
			// The database does not exists so try to create one
			try {
				arangoDB.createDatabase(dbName);
				return true;
			} catch (ArangoDBException e) {
				new Popup(Alert.AlertType.ERROR, e.getException(),
						"Internal database error occured when creating a database named:\n"
								+ dbName);
			}
		}
		return false;
	}

	/**
	 * Closes the connection the the ArangoDB database.
	 */
	protected void shutdown() {
		arangoDB.shutdown();
	}

}
