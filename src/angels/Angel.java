/**
 * This class allows for the representation of an angel. An angel has various
 * attributes that allow for identification and needs of an angel.
 * 
 * @author Nicholas Kunzler
 */
package angels;

import com.arangodb.entity.BaseDocument;

public class Angel {

	private BaseDocument baseDocument = new BaseDocument();
	
	public Angel() {
		
	}
	
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
	 * @return A map containing the angels attributes and their values
	 */
	public BaseDocument getAttributes() {
		return baseDocument;
	}
	
	public void setAttributes(BaseDocument newbaseDocument) {
		baseDocument = newbaseDocument;
	}

	/**
	 * Returns the value associated with the attribute.
	 * 
	 * @param attribute The attribute to find the value for.
	 * @return A string representing the value associated with the attribute.
	 */
	public Object get(Attribute attribute) {
		return baseDocument.getAttribute(attribute.toString());
	}

}
