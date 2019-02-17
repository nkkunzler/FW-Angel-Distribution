/**
 * This class is the controller to connect between the HoldDisplay.fxml file 
 * and the JavaFX Node objects within the file. The purpose for this class is
 * to allow for the user to select the variety of reasons for why the angel
 * is going into hold. 
 * 
 * The reasons for hold include missing: shirts, pants, underwear, shoes,
 * socks, wishes, and special items requested.
 * 
 * @author Nicholas Kunzler
 */

package controllers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import angels.Angel;
import angels.Attribute;
import angels.Status;
import customFX.Popup;
import database.DatabaseController;
import display.Displays;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class HoldController extends Controller {

	@FXML
	private Label angelIDLabel;
	@FXML
	private VBox selectionArea;
	@FXML
	private VBox selectedItemsVBox;

	private Angel angel;
	private DatabaseController dbController;
	private String collection;

	/**
	 * Constructor for the controller used to put an angel on hold.
	 * 
	 * @param controller The database controller used to connect to the
	 *                   database.
	 * @param collection The collection in which an angel will eventually be
	 *                   added to.
	 */
	public HoldController(DatabaseController dbController, String collection) {
		super(Displays.HOLD_DISPLAY);

		this.dbController = dbController;
		this.collection = collection;
	}

	/**
	 * Adds an angel to the display for the purpose of selecting the items that
	 * cause the angel to go on hold. This method populates a VBox based off of
	 * the values associated with the angel.
	 * 
	 * The items that may cause the angel to go on hold include: Clothing,
	 * Shoes, Wish, Book, and/or Special Item.
	 * 
	 * @param angel The angel that is going onto hold.
	 */
	public void addAngel(Angel angel) {
		this.angel = angel;

		angelIDLabel.setText(angel.get(Attribute.ID) + "");
		List<Node> container = selectionArea.getChildren();

		// Creating the hold clothing items: shirts, pants, underwear
		List<String> list = Arrays.asList("SHIRTS", "PANTS", "UNDERWEAR");
		container.add(newSection("CLOTHES", list));

		// Creating the hold shoes items: shoes and socks
		container.add(newSection("SHOES", Arrays.asList("SHOES", "SOCKS")));

		// Creating the hold book items, displays all the desired books
		container.add(newSection(Attribute.BOOK));

		// Creating the hold wish items, displays all desired wishes
		container.add(newSection(Attribute.WISH));

		// Creating the hold special items, displays all special items
		container.add(newSection(Attribute.SPECIAL));
	}

	/**
	 * See the method: newSection(String sectionName, List<String> sectionItems)
	 * for additional information.
	 * 
	 * @param attribute The attribute that will represent the section. The
	 *                  attribute must be associated with a list, otherwise
	 *                  nothing will happen.
	 * @return VBox representing a section, contains a Label and GridPane.
	 */
	@SuppressWarnings("unchecked")
	private VBox newSection(Attribute attribute) {
		try {
			return newSection(attribute.toString(),
					(List<String>) angel.get(attribute));
		} catch (ClassCastException cce) {
			System.err.println("Angel Attribute '" + attribute.toString()
					+ " does not correspond to a List<String>");
		}
		return new VBox();
	}

	/**
	 * Creates a new section that displays a certain attribute of an angel. A
	 * section contains CheckBoxes that allow the user to select different items
	 * associated with the attribute.
	 * 
	 * @param sectionName  String representing the name of the section. Will be
	 *                     used as a title for the section.
	 * @param sectionItems List of strings containing the names of the items.
	 * @return VBox representing a section, contains a Label and a GridPane.
	 */
	private VBox newSection(String sectionName, List<String> sectionItems) {
		VBox section = new VBox();
		section.setSpacing(10);

		// The label for the section
		Label label = new Label(sectionName.toUpperCase());
		label.setFont(Font.font("System", FontWeight.NORMAL, 22));
		label.setUnderline(true);
		section.getChildren().add(label);

		// Creating the GridPane for the items in the section
		section.getChildren().add(createGridPane(sectionItems));

		return section;
	}

	/**
	 * Creates a new GridPane of CheckBoxes with the labels assigned from the
	 * items list.
	 * 
	 * @param items List of strings which will be assigned to CheckBoxes.
	 * @return GridPane containing CheckBoxes with the text from the items list.
	 */
	@SuppressWarnings("unchecked")
	private GridPane createGridPane(List<String> items) {
		GridPane pane = new GridPane();
		pane.setVgap(10);
		pane.setHgap(20);

		int row = 0;
		int col = 0;
		int numChars = 0;
		Set<String> itemSet = new HashSet<>(
				(List<String>) angel.get(Attribute.MISSING));

		for (int i = 0; i < items.size(); ++i) {
			// CheckBox on the left of the screen
			CheckBox cb = createCheckBox(items.get(i));

			// If items are in hold then pre-check the CheckBox
			if (itemSet.contains(cb.getText())) {
				cb.fire();
			}

			// If text for a CheckBox is longer than 20 characters, new
			// CheckBoxes within the section are moved down one column.
			numChars += cb.getText().length();
			if (numChars >= 20 && col <= 1) {
				if (items.size() != 1)
					row++;
				col++;
				numChars = 0;
			}
			pane.add(cb, row++ % 2, col++ / 2); // Adding CheckBox to pane
		}
		
		return pane;
	}

	/**
	 * Creates a CheckBox that is used when display all the items associated
	 * with the angel. These items include clothes, shoes, wishes, special
	 * items, and books.
	 * 
	 * @param text String that will be placed to the right of the CheckBox
	 * @return CheckBox that that is unselected and has the given text to the
	 *         right of the CheckBox.
	 */
	private CheckBox createCheckBox(String text) {
		CheckBox cb = new CheckBox(text.toUpperCase());
		cb.setFont(Font.font("System", FontWeight.BOLD, 20));

		cb.setOnAction(e -> {
			if (cb.isSelected()) { // Adds label showing new missing item
				Label label = new Label(cb.getText());
				label.setFont(Font.font("System", FontWeight.BOLD, 20));
				selectedItemsVBox.getChildren().add(label);
			} else { // Removes unselected CheckBox from missing items list
				selectedItemsVBox.getChildren().removeIf(
						p -> cb.getText().equals(((Labeled) p).getText()));
			}
		});

		return cb;
	}

	/**
	 * This method is called when the 'PUT ON HOLD' button in the
	 * HoldDisplay.fxml file is pressed. This method puts all selected items,
	 * which represents the items that are missing, into the database under the
	 * attribute of MISSING.
	 */
	@FXML
	public void onHoldButtonHandler() {
		// TODO: Figure out a way to insert an array into the database without
		// have to convert the array into a string.
		String missingItems = convertArrToStr(selectedItemsVBox.getChildren());

		// If no missingItems, [], ask if the item should be marked as complete
		if (missingItems.equals("[]")) {
			String contentText = "This angel has nothing on hold.\n"
					+ "Would you like to mark it as complete?";
			Popup popup = new Popup(AlertType.INFORMATION, contentText,
					ButtonType.YES, ButtonType.NO);

			// User wants to put item on hold, so do so.
			if (popup.getSelection() == ButtonType.YES) {
				dbController.update((String) angel.get(Attribute.ID),
						Attribute.STATUS, Status.COMPLETE, collection);
			}
		} else { // Missing items indicate that item needs to go on hold
			dbController.update(
					(String) angel.get(Attribute.ID),
					Attribute.STATUS, Status.HOLD, collection);
		}

		// Changing the missing items in the database to newly selected items
		dbController.query(
				"UPDATE {_key: '" + angel.get(Attribute.ID) + "'} WITH {'"
						+ Attribute.MISSING + "':" + missingItems + "} "
						+ "IN " + collection);

		super.switchScene(Displays.ANGEL_SELECTION);
	}

	/**
	 * TODO: TRY TO REMOVE THE NEED TO DO THIS IN THE FUTURE
	 * 
	 * 
	 * Converts an ObservableList<Node> into a string form usable by an arangodb
	 * database query.
	 * 
	 * The string will have a form similar to the one as below, " marks not
	 * included.
	 * 
	 * " ['item one', 'item two'] "
	 * 
	 * @param array ObservableList<?> that will be converted to a string
	 * @return A string representation of the array
	 */
	private String convertArrToStr(ObservableList<?> array) {
		String strArray = "[";

		if (array.size() == 0) {
			strArray += "]";
		} else {
			for (int i = 0; i < array.size() - 1; ++i) {
				String text = ((Label) array.get(i)).getText();
				strArray += "'" + text + "', ";
			}
			String text = ((Label) array.get(array.size() - 1)).getText();
			strArray += "'" + text + "']";
		}
		return strArray;
	}

	/**
	 * Cancels the hold operation and returns back to the StatusSelection
	 * display.
	 */
	@FXML
	public void cancelButtonHandler() {
		super.previousDisplay();
		StatusSelectController controller = (StatusSelectController) super.getController(
				Displays.ANGEL_STATUS);
		controller.setAngel(angel);
	}
}
