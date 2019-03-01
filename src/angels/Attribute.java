/**
 * This class represents the different attributes associated with an angel.
 * These attributes are used without the program in order maintain naming
 * conventions.
 * 
 * @author Nicholas Kunzler
 */

package angels;

/**
 * All the attributes associated with an angel.
 */
public enum Attribute {
	ID, 		// Single ID Number
	GENDER, 	// ComboBox status select: Boy, Girl
	AGE, 		// Filter Values: 1 - 12
	SHOE_SIZE, 	// Single Shoe Size
	CLOTHES_SIZE,// Single Shoe Size
	SHIRT_SIZE,	// Single Shirt Size
	PANT_SIZE, 	// Single pant size
	WISH, 		// Key word
	BOOK, 		// Key word
	SPECIAL, 	// Key word
	STATUS, 	// ComboBox status select: Status values
	MISSING, 	// Items missing
	LOCATION; 	// The current location of the angel
}