package database;

import java.util.List;

import com.arangodb.ArangoCursor;
import com.arangodb.entity.BaseDocument;

public class DatabaseTester {

	DatabaseController dbController;

	public DatabaseTester(DatabaseController dbController) {
		this.dbController = dbController;
	}

	public List<BaseDocument> getID(String angelID) {
		String query = "FOR doc IN angels " 
				+ "FILTER LIKE(doc.id, " + "'" + angelID + "_')"
				+ "RETURN doc";
		List<BaseDocument> result = dbController.query(query);
		System.out.println("FINISHED QUERY");
		return result;
	}
}
