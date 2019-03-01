/**
 * This class allows for the representation of an angel. An angel has various
 * attributes that allow for identification and needs of an angel.
 * 
 * @author Nicholas Kunzler
 */
package angels;

import com.arangodb.entity.BaseDocument;

public class Angel implements Comparable<Angel> {

	private BaseDocument baseDocument = new BaseDocument();

	public Angel() {
	};

	/**
	 * Creates a new instance of an Angel with the given values from the given
	 * BaseDocument
	 * 
	 * @param baseDocument Document containing direct information from the
	 *                     database a specific angel
	 * 
	 */
	public Angel(BaseDocument baseDocument) {
		this.baseDocument = baseDocument;
	}

	/**
	 * Adds a new attribute and its value to the map of attributes.
	 * 
	 * @param attribute The attribute being assigned a value
	 * @param value     The value assigned to the attribute.
	 */
	public void addAttribute(Attribute attribute, Object value) {
		if (attribute == Attribute.ID)
			baseDocument.setKey(value.toString());

		baseDocument.addAttribute(attribute.toString(), value);
	}

	/**
	 * Returns a mapping of all the angels attributes. If an certain attribute
	 * does not exists for the angel, null will be returned when trying to
	 * access the attribute.
	 * 
	 * @return A BaseDocument containing the angels attributes and their values
	 */
	public BaseDocument getAttributes() {
		return baseDocument;
	}

	/**
	 * Returns the value associated with the attribute.
	 * 
	 * @param attribute The attribute to find the value for.
	 * @return A string, in form of an object, representing the value associated
	 *         with the attribute.
	 */
	public Object get(Attribute attribute) {
		return baseDocument.getAttribute(attribute.toString());
	}

	/**
	 * Method used to compare two angels. This comparison is done with the
	 * angels ID values. Since ID values are unique this is the standard for
	 * comparing.
	 * 
	 * This assumes that there are no leading zeros to the string ID values.
	 * 
	 * @param Angel o The angel to compare to this angel
	 * @return -1 if this.ID <. o.ID, 1 if this.ID > o.ID, 0 means equal.
	 */
	@Override
	public int compareTo(Angel o) {
		String thisID = this.get(Attribute.ID).toString();
		String oID = o.get(Attribute.ID).toString();

		// If string length is larger, the value must be larger
		if (thisID.length() > oID.length())
			return 1;
		else if (thisID.length() < oID.length())
			return -1;

		// Lengths equal so check 
		int i = -1;
		while (i++ < thisID.length()) {
			if (thisID.charAt(i) > oID.charAt(i))
				return 1;
			else if (thisID.charAt(i) < oID.charAt(i))
				return -1;
		}
		return 0; // ID's are equal
	}
}
