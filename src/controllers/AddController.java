/**
 * This class is the controller to connect between the AddAngel.fxml file and
 * the JavaFX Node objects within the file. The purpose for this class is to
 * allow for reading inputs from the AddAngel display and adding the inputed
 * values into the database. 
 * 
 * @author Nicholas Kunzler
 */
package controllers;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import angels.Angel;
import angels.Attribute;
import angels.Status;
import customFX.Popup;
import database.DatabaseController;
import display.Displays;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

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
	private VBox inputsContainer;

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
	@FXML
	public void validateAngelID(KeyEvent e)
			throws InterruptedException, ExecutionException {
		if (e.getCode() == KeyCode.TAB) {
			String angelID = idInput.getText().toUpperCase();

			if (angelID.equals("")) { // Can't have an empty angel id
				showPopup("Invalid ID", "An Angel ID must be provided");
				idInput.requestFocus();
				return;
			}

			// Checking to see that a character was included, else an A is added
			if (!angelID.matches(".*[a-zA-Z]+.*")) {
				idInput.setText(angelID + "A");
				angelID = angelID + "A";
			}

			// Checking to see if the angel id already exist in the database
			Task<Boolean> task = dbController.contains(angelID, dbCollection);
			Thread thread = new Thread(task);
			thread.start();
			thread.join(); // Force thread to finish before allowing more input

			if (task.get()) {
				showPopup("Invalid ID",
						"Angel ID '" + angelID + "' already exists");
				idInput.clear();
				e.consume(); // Prevents next enabled Node to be auto selected
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
	@FXML
	public void validateSex(KeyEvent e) {
		if (e.getCode() == KeyCode.TAB) {
			String input = sexInput.getText().toLowerCase();

			if (input.equals("b")) {
				sexInput.setText("boy");
				input = "boy";
			} else if (input.equals("g")) {
				sexInput.setText("girl");
				input = "girl";
			}

			if (!input.equals("boy") && !input.equals("girl")) {
				showPopup("Invalid Gender", "Enter 'boy' or 'girl'");
				sexInput.clear();
				e.consume(); // Prevents next enabled Node from being selected
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
	@FXML
	public void validateAge(KeyEvent e) {
		if (e.getCode() == KeyCode.TAB) {
			int age = -1;

			try {
				age = Integer.valueOf(ageInput.getText());
			} catch (NumberFormatException nfe) {
				showPopup("Invalid Age", "Enter a number between 1 and 12");
				ageInput.clear();
				e.consume(); // Prevents next enabled Node from being selected
				return;
			}

			// Valid ages are between 1 and 12, inclusive
			if (age < 0 || age > 12) {
				showPopup("Invalid Age", "Enter a number between 1 and 12");
				ageInput.clear();
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
	@FXML
	public void validateShoe(KeyEvent e) {
		if (e.getCode() == KeyCode.TAB) {
			String text = shoeInput.getText().toLowerCase(); // Text from input

			if (text.equals("") || text.equals("none")) { // No shoes are valid
				shoeInput.setText("none");
				clothesInput.setDisable(false);
				return;
			}

			char firstChar = text.charAt(0); // Checking for prefix
			if (firstChar != 'w' && firstChar != 'm' && firstChar != 'k') {
				// Check to see if last character is a W, meaning a wide shoe
				char wide = text.charAt(text.length() - 1) == 'w' ? 'W' : '\0';

				// Need to check for 'W' for proper parsing of shoe size
				int shoeSize;
				try {
					if (wide == 'W')
						shoeSize = Integer
								.valueOf(text.substring(0, text.length() - 1));
					else
						shoeSize = Integer.valueOf(text);
				} catch (NumberFormatException nfe) {
					showPopup("Invalid Shoe Size",
							"Please enter a number indicating shoe size");
					shoeInput.clear();
					e.consume(); // Prevents next Node from being selected
					return;
				}

				// Determines if the shoe is for a kid, man, or women
				int age = Integer.valueOf(ageInput.getText());
				char ageLevel = 'K';
				if (age >= 8 && shoeSize >= 1) {
					if (sexInput.getText().equalsIgnoreCase("boy"))
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
	@FXML
	public void validateClothesSize(KeyEvent e) {
		if (e.getCode() == KeyCode.TAB) {
			// It is possible not to have any clothes
			if (clothesInput.getText().equals("")) {
				clothesInput.setText("none");
			} else {
				String size = convertClothesSizeToDimensions(sexInput.getText(),
						clothesInput.getText());
				clothesInput.setText(size);
			}
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
	@FXML
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
	@FXML
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
	@FXML
	public void validateWish(KeyEvent e) {
		if (wishInput.getText().length() >= MAX_WISH_CHARS) {
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
	@FXML
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
	@FXML
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

	/**
	 * Goes back to the scene that resulted in this scene to be displayed
	 */
	@FXML
	public void toMainMenu() {
		super.previousDisplay();
	}

	/**
	 * If the user presses enter while the Add Angel button is selected, then
	 * the button will be pressed.
	 * 
	 * @param e KeyEvent that is triggered every time a button is pressed.
	 */
	@FXML
	public void addButtonKeyHandler(KeyEvent e) {
		if (e.getCode() == KeyCode.ENTER)
			addAngelButton.fire();
	}

	/**
	 * Button handler to add a new angel to the database. Takes inputs from the
	 * scene, converts them to angel attributes, used to create an angel, and
	 * then add the angel to the database.
	 */
	@FXML
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
		String[] specs = specialInput.getText().replaceAll(", ", ",")
				.split(",");

		angel.addAttribute(Attribute.WISH, wishes);
		angel.addAttribute(Attribute.BOOK, books);
		angel.addAttribute(Attribute.SPECIAL, specs);

		// Default values when the angels are first created
		angel.addAttribute(Attribute.STATUS, Status.AWAITING);
		angel.addAttribute(Attribute.MISSING, new String[0]);
		angel.addAttribute(Attribute.LOCATION, "Family Resource");

		// If the angel was added reset the inputs and indicate success
		Task<Boolean> task = dbController.insertAngel(angel, dbCollection);
		ExecutorService exec = Executors.newSingleThreadExecutor();
		exec.execute(task);
		exec.shutdown();

		task.setOnSucceeded(e -> {
			showPopup("Successful", "Angel '" + idInput.getText()
					+ "' + was added successfully");
			resetInputs();
		});

		task.setOnFailed(e -> showPopup("Error", "Angel could not be added"));
	}

	/**
	 * Method is responsible clearing the TextField inputs from a previous angel
	 * addition and disables the TextFields to their original setup.
	 */
	private void resetInputs() {
		// Clearing all the inputs after AngelID to prepare for new angel entry
		for (int i = 1; i < inputsContainer.getChildren().size(); ++i) {
			((TextInputControl) inputsContainer.getChildren().get(i)).clear();
			((TextInputControl) inputsContainer.getChildren().get(i))
					.setDisable(true);
		}

		// Clearing the AngelID TextField
		((TextInputControl) inputsContainer.getChildren().get(0)).clear();
		addAngelButton.setDisable(true);
	}

	/**
	 * TEMP solution for indicating that an input is invalid
	 * 
	 * @param message The message to display
	 */
	private void showPopup(String header, String message) {
		new Popup(AlertType.WARNING, header, message);
	}
}
