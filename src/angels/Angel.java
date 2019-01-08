/**
 * This class allows for the representation of an angel. An angel has various
 * attributes that allow for identification and needs of an angel.
 * 
 * @author Nicholas Kunzler
 */
package angels;

import java.util.HashMap;
import java.util.Map;

public class Angel {

	private Map<String, Object> attrValues = new HashMap<>();

	/**
	 * Adds a new attribute and its value to the map of attributes.
	 * 
	 * @param attribute The attribute being assigned a value
	 * @param value     The value assigned to the attribute.
	 */
	public void addAttribute(Attribute attribute, Object value) {
		attrValues.put(attribute.toString(), value);
	}

	/**
	 * Returns a mapping of all the angels attributes. If an certain attribute
	 * does not exists for the angel, null will be returned when trying to
	 * access the attribute.
	 * 
	 * @return A map containing the angels attributes and their values
	 */
	public Map<String, Object> getAttributes() {
		return attrValues;
	}

	/**
	 * Returns the value associated with the attribute.
	 * 
	 * @param attribute The attribute to find the value for.
	 * @return A string representing the value associated with the attribute.
	 */
	public Object get(Attribute attribute) {
		return attrValues.get(attribute.toString());
	}

}
