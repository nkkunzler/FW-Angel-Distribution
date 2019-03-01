package database;

public enum DBCollection {
	
	ANGELS("angels"),
	SHOES("shoes"),
	CLOTHES("clothes");
	
	private String collectionName;
	private DBCollection(String collectionName) {
		this.collectionName = collectionName;
	}
	
	public String toString() {
		return collectionName;
	}

}
