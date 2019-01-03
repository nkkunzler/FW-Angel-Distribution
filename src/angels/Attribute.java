package angels;

/**
 * All the attributes associated with an angel.
 */
public enum Attribute {
	ID("id"),
	SEX("sex"),
	AGE("age"),
	SHOE_SIZE("shoe_size"),
	CLOTHES_SIZE("clothes_size"),
	SHIRT_SIZE("shirt_size"),
	PANT_SIZE("pant_size"),
	WISH("wish"),
	BOOK("book"),
	SPECIAL("special"),
	STATUS("status"),
	MISSING("mising"),
	LOCATION("location");

	private final String type;

	private Attribute(String type) {
		this.type = type;
	}

	public final String getType() {
		return this.type;
	}
}