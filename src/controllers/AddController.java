/**
 * This class is the controller to connect between the AddAngel.fxml file and
 * the JavaFX Node objects within the file. The purpose for this class is to
 * allow for reading inputs from the AddAngel display and adding the inputed
 * values into the database. 
 * 
 * @author Nicholas Kunzler
 */
package controllers;

import angels.Angel;
import angels.Attribute;
import angels.Status;
import database.DatabaseController;
import display.Displays;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class AddController extends Controller {

	// Maximum number of characters allowed for book, wish, and special inputs.
	// This is so that the text can fit onto an angel.
	private final int MAX_BOOK_CHARS = 35;
	private final int MAX_WISH_CHARS = 85;
	private final int MAX_SPECIAL_CHARS = 60;

	@FXML
	// All the TextFields input within the AddAngel.fxml display
	private TextField idInput, sexInput, ageInput, shoeInput, clothesInput,
			shirtInput, pantsInput, bookInput;

	@FXML
	private TextArea wishInput, specialInput;
	@FXML
	private Button addAngelButton;

	private DatabaseController dbController;
	private String dbCollection;

	/**
	 * Constructor for the controller used to validate and add a new angel from
	 * the inputs of the AddAngel.fxml scene.
	 * 
	 * @param controller The database controller used to connect to the
	 *                   database.
	 * @param collection The collection in which an angel will eventually be
	 *                   added to.
	 */
	public AddController(DatabaseController controller, String collection) {
		super(Displays.ADD_DISPLAY); // Scene the controller is associated with
		dbController = controller;
		dbCollection = collection;
	}

	/**
	 * Checking to make sure that the angel id is valid.
	 * 
	 * A valid angel id is one that does not already exist within the databases
	 * collection specified within the constructor.
	 * 
	 * @param e KeyEvent that is triggered every time a button is pressed.
	 */
	public void validateAngelID(KeyEvent e) {
		//TODO: Have the idInput be focused on when the scene first starts
		if (e.getCode() == KeyCode.TAB) {
			String angelID = idInput.getText().toUpperCase();
			if (angelID.equals("")) {
				showMessage("Invalid ID", "An Angel ID must be provided");
				idInput.requestFocus();
				return;
			}
			if (dbController.contains(angelID, dbCollection)) {
				showMessage("Invalid ID",
						"Angel ID '" + angelID + "' already exists");
				e.consume();
			} else {
				sexInput.setDisable(false);
			}
		}
	}

	/**
	 * Checking to make sure the sex of the angel is valid.
	 * 
	 * A valid sex is either a 'boy' or 'girl', capitalization does not matter.
	 * 
	 * @param e KeyEvent that is triggered every time a button is pressed.
	 */
	public void validateSex(KeyEvent e) {
		if (e.getCode() == KeyCode.TAB) {
			String input = sexInput.getText().toLowerCase();

			if (!input.equals("boy") && !input.equals("girl")) {
				showMessage("Invalid Gender", "Enter 'boy' or 'girl'");
				e.consume();
			} else {
				ageInput.setDisable(false);
			}
		}
	}

	/**
	 * Checking to make sure the age of the angel is valid.
	 * 
	 * A valid age is any values between 0 and 12, inclusive.
	 * 
	 * @param e KeyEvent that is triggered every time a button is pressed.
	 */
	public void validateAge(KeyEvent e) {
		if (e.getCode() == KeyCode.TAB) {
			int age = -1;

			try {
				age = Integer.valueOf(ageInput.getText());
			} catch (NumberFormatException nfe) {
				showMessage("Invalid Age", "Enter a number between 1 and 12");
				return;
			}

			// Valid ages are between 1 and 12, inclusive
			if (age < 0 || age > 12) {
				showMessage("Invalid Age", "Enter a number between 1 and 12");
				e.consume();
			} else {
				shoeInput.setDisable(false);
			}
		}
	}

	/**
	 * Checking to make sure the shoe size of the angel is valid.
	 * 
	 * A valid shoe will contain a 'M', 'W', 'K', or 'W' to indicated men,
	 * women, or kid, size shoe. 'W' indicates a wide shoe size.
	 * 
	 * Input does not require that these are added as they will be added
	 * automatically if not specified. If specified that values entered will
	 * remain.
	 * 
	 * @param e KeyEvent that is triggered every time a button is pressed.
	 */
	public void validateShoe(KeyEvent e) {
		if (e.getCode() == KeyCode.TAB) {
			String text = shoeInput.getText().toLowerCase(); // Text from input

			char firstChar = text.charAt(0); // Checking to see if prefix exist
			if (firstChar != 'w' && firstChar != 'm' && firstChar != 'k') {
				// Check to see if last character is a W, meaning a wide shoe
				char wide = text.charAt(text.length() - 1) == 'w' ? 'W' : '\0';

				// Need to check for 'w' for proper parsing of shoe size
				int shoeSize;
				if (wide == 'W')
					shoeSize = Integer
							.valueOf(text.substring(0, text.length() - 1));
				else
					shoeSize = Integer.valueOf(text);

				// Determines if the shoe is for a kid, man, or women
				int age = Integer.valueOf(ageInput.getText());
				char ageLevel = 'K';
				if (age >= 8 && shoeSize >= 1) {
					String sex = sexInput.getText().toLowerCase();
					if (sex.equals("boy"))
						ageLevel = 'M';
					else
						ageLevel = 'W';
				}

				// Setting the text field to display the prefix shoe prefix
				shoeInput.setText("" + ageLevel + shoeSize + wide);
			}
			clothesInput.setDisable(false);
		}
	}

	/**
	 * Checking to make sure the clothes size of the angel is valid.
	 * 
	 * A valid clothes size is is numeric value. 10/12 or 7
	 * 
	 * If 'S', 'M', 'L', 'XL' are input, then those values will be automatically
	 * converted to a clothes size, such as 10/12.
	 * 
	 * @param e KeyEvent that is triggered every time a button is pressed.
	 */
	public void validateClothesSize(KeyEvent e) {
		if (e.getCode() == KeyCode.TAB) {
			String size = convertClothesSizeToDimensions(sexInput.getText(),
					clothesInput.getText());
			clothesInput.setText(size);
			shirtInput.setDisable(false);
		}
	}

	/**
	 * Checking to make sure the shirt size of the angel is valid.
	 * 
	 * A valid shirt size is the same as a valid clothes size, see above.
	 * 
	 * If the input is empty, then the clothes size provided will be inserted.
	 * 
	 * @param e KeyEvent that is triggered every time a button is pressed.
	 */
	public void validateShirtSize(KeyEvent e) {
		if (e.getCode() == KeyCode.TAB) {
			// Takes clothes size if empty
			if (shirtInput.getText().equals("")) {
				shirtInput.setText(clothesInput.getText());
			} else {
				String size = convertClothesSizeToDimensions(sexInput.getText(),
						shirtInput.getText());
				shirtInput.setText(size);
			}
			pantsInput.setDisable(false);
		}
	}

	/**
	 * Checking to make sure the pant size of the angel is valid.
	 * 
	 * A valid pant size is the same as a valid clothes size, see above.
	 * 
	 * If the input is empty, then the clothes size provided will be inserted.
	 * 
	 * @param e KeyEvent that is triggered every time a button is pressed.
	 */
	public void validatePantSize(KeyEvent e) {
		if (e.getCode() == KeyCode.TAB) {
			// Takes clothes size if empty
			if (pantsInput.getText().equals("")) {
				pantsInput.setText(clothesInput.getText());
			} else {
				String size = convertClothesSizeToDimensions(sexInput.getText(),
						pantsInput.getText());
				pantsInput.setText(size);
			}
			wishInput.setDisable(false);
			wishInput.requestFocus();
		}
	}

	/**
	 * This method converts the standard clothes size of 'S', 'M', 'L', and 'XL'
	 * to their corresponding sizes.
	 * 
	 * @param sex  The sex of the angel, either 'boy' or 'girl'
	 * @param size The size of the clothing
	 * @return The new size of the clothing.
	 */
	private String convertClothesSizeToDimensions(String sex, String size) {
		switch (size.toLowerCase()) {
		case "s":
			if (sex.equalsIgnoreCase("boy")) // Boys have larger sizes
				return "8/10";
			return "7/8";
		case "m":
			if (sex.equalsIgnoreCase("boy"))
				return "10/12";
			return "10";
		case "l":
			if (sex.equalsIgnoreCase("boy"))
				return "14/16";
			return "12/14";
		case "xl":
			if (sex.equalsIgnoreCase("boy"))
				return "18/20";
			return "14/16";
		default:
			return size;
		}
	}

	/**
	 * Checking to make sure the wish of the angel is valid.
	 * 
	 * A valid wish is either the values typed or an empty wish. If the wish is
	 * empty, then the string 'Age Appropriate' is automatically added as the
	 * input.
	 * 
	 * @param e KeyEvent that is triggered every time a button is pressed.
	 */
	public void validateWish(KeyEvent e) {
		if (wishInput.getText().length() > MAX_WISH_CHARS) {
			wishInput.deletePreviousChar();
		}

		if (e.getCode() == KeyCode.TAB) {
			if (wishInput.getText().equals(""))
				wishInput.setText("Age Appropriate");
			e.consume();
			bookInput.setDisable(false);
			bookInput.requestFocus();
		}
	}

	/**
	 * Checking to make sure the book of the angel is valid.
	 * 
	 * A valid book is either the values typed or an empty wish. If the book is
	 * empty, then the string 'Age Appropriate' is automatically added as the
	 * input.
	 * 
	 * @param e KeyEvent that is triggered every time a button is pressed.
	 */
	public void validateBook(KeyEvent e) {
		if (bookInput.getText().length() > MAX_BOOK_CHARS) {
			bookInput.deletePreviousChar();
		}

		if (e.getCode() == KeyCode.TAB) {
			if (bookInput.getText().equals(""))
				bookInput.setText("Age Appropriate");
			e.consume();
			specialInput.setDisable(false);
			specialInput.requestFocus();
		}
	}

	/**
	 * Checking to make sure the special of the angel is valid.
	 * 
	 * A valid special is either the values typed or an empty wish. If the
	 * special is empty, then the string 'Age Appropriate' is automatically
	 * added as the input.
	 * 
	 * @param e KeyEvent that is triggered every time a button is pressed.
	 */
	public void validateSpecial(KeyEvent e) {
		if (specialInput.getText().length() > MAX_SPECIAL_CHARS) {
			specialInput.deletePreviousChar();
		}

		if (e.getCode() == KeyCode.TAB) {
			if (specialInput.getText().equals(""))
				specialInput.setText("Age Appropriate");
			e.consume();
			addAngelButton.setDisable(false);
			addAngelButton.requestFocus();
		}
	}

	@FXML
	/**
	 * Button handler to add a new angel to the database. Takes inputs from the
	 * scene, converts them to angel attributes, used to create an angel, and
	 * then add the angel to the database.
	 */
	public void addButtonHandler() {
		Angel angel = new Angel();
		// The basic angel attributes
		angel.addAttribute(Attribute.ID, idInput.getText().toUpperCase());
		angel.addAttribute(Attribute.SEX, sexInput.getText().toLowerCase());
		angel.addAttribute(Attribute.AGE, Integer.valueOf(ageInput.getText()));
		angel.addAttribute(Attribute.SHOE_SIZE, shoeInput.getText());
		angel.addAttribute(Attribute.CLOTHES_SIZE, clothesInput.getText());
		angel.addAttribute(Attribute.SHIRT_SIZE, shirtInput.getText());
		angel.addAttribute(Attribute.PANT_SIZE, pantsInput.getText());

		// Converting into lists as there are multiple values
		String[] wishes = wishInput.getText().replaceAll(", ", ",").split(",");
		String[] books = bookInput.getText().replaceAll(", ", ",").split(",");
		String[] specs = specialInput.getText().replaceAll(", ", ",").split(",");
		for (String wish : wishes)
			angel.addAttribute(Attribute.WISH, wish.toLowerCase());
		for (String book : books)
			angel.addAttribute(Attribute.BOOK, book.toLowerCase());
		for (String spec : specs)
			angel.addAttribute(Attribute.SPECIAL, spec.toLowerCase());

		// Default values when the angels are first created
		angel.addAttribute(Attribute.STATUS, Status.AWAITING.getStatus());
		angel.addAttribute(Attribute.MISSING, new String[0]);
		angel.addAttribute(Attribute.LOCATION, "Family Resource");

		// If the angel was added reset the inputs and indicate success
		if (dbController.insertAngel(angel, dbCollection)) {
			showMessage("Successful", "Angel was added successfully");
			resetInputs();
		} else {
			showMessage("Unsuccessful", "Angel addition was unsuccessfully");
		}
	}

	/**
	 * Method is responsible clearing the TextField inputs from a previous angel
	 * addition and disables the TextFields to their original setup.
	 */
	private void resetInputs() {
		// Clearing all the inputs to prepare for new angel entry
		idInput.clear();
		sexInput.clear();
		ageInput.clear();
		shoeInput.clear();
		clothesInput.clear();
		shirtInput.clear();
		pantsInput.clear();
		wishInput.clear();
		bookInput.clear();
		specialInput.clear();

		// Disabling TextFields to make sure all values are added
		sexInput.setDisable(true);
		ageInput.setDisable(true);
		shoeInput.setDisable(true);
		clothesInput.setDisable(true);
		shirtInput.setDisable(true);
		pantsInput.setDisable(true);
		wishInput.setDisable(true);
		bookInput.setDisable(true);
		specialInput.setDisable(true);
		addAngelButton.setDisable(true);
	}

	/**
	 * TEMP solution for indicating that an input is invalid
	 * 
	 * @param message The message to display
	 */
	private void showMessage(String header, String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(header);
		alert.setContentText(message);
		alert.showAndWait();
	}

	@FXML
	/**
	 * Goes back to the scene that resulted in this scene to be displayed
	 */
	public void toMainMenu() {
		super.previousDisplay();
	}
}
