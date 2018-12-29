/**
 * This class allows for the representation of an angel. An angel has various
 * attributes that allow for identification and needs of an angel.
 * 
 * @author Nicholas Kunzler
 */
package main;

import java.util.HashMap;
import java.util.Map;

public class Angel {

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

	private Map<String, Object> attrValues = new HashMap<>();

	/**
	 * Adds a new attribute and its value to the map of attributes.
	 * 
	 * @param attribute The attribute being assigned a value
	 * @param value     The value assigned to the attribute.
	 */
	public void addAttribute(Attribute attribute, Object value) {
		attrValues.put(attribute.getType(), value);
	}

	/**
	 * Adds a new attribute and its value to the map of attributes. If the
	 * string representing an attribute does not exists, then an
	 * IllegalArgumentException is thrown indicating the enum does not exist.
	 * 
	 * @param attribute The attribute being assigned a value
	 * @param value     The value assigned to the attribute.
	 */
	public void addAttribute(String attribute, Object value) {
		try {
			this.addAttribute(Attribute.valueOf(attribute.toUpperCase()),
					value);
		} catch (IllegalArgumentException ae) {
			System.err.println(
					"Enum for attribute '" + attribute + "' not found.");
		}
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
	 * Returns the Enum Attribute associated with the desired string attribute name
	 * @param strAttribute The string version of the Enum to get
	 * @return The attribute associated with the string version
	 */
	public Attribute getAttribute(String strAttribute) {
		return Attribute.valueOf(strAttribute.toUpperCase());
	}

	/**
	 * Returns the value associated with the attribute.
	 * 
	 * @param attribute The attribute to find the value for.
	 * @return A string representing the value associated with the attribute.
	 */
	public String get(Attribute attribute) {
		return attrValues.get(attribute.getType()) + "";
	}

}
