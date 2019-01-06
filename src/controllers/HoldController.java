/**
 * This class is the controller to connect between the HoldDisplay.fxml file 
 * and the JavaFX Node objects within the file. The purpose for this class is
 * to allow for the user to select the varity of reasons for why the angel
 * is going into hold. 
 * 
 * The reasons for hold include missing: shirts, pants, underwear, shoes,
 * socks, wishes, and special items requested.
 * 
 * @author Nicholas Kunzler
 */

package controllers;

import java.util.Arrays;
import java.util.List;

import angels.Angel;
import angels.Attribute;
import angels.Status;
import database.DatabaseController;
import display.Displays;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
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
			return this.newSection(attribute.getType(),
					(List<String>) angel.get(attribute));
		} catch (ClassCastException cce) {
			System.err.println("Angel Attribute '" + attribute.getType()
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

		// The label for the section
		Label label = new Label(sectionName.toUpperCase());
		label.setUnderline(true);
		label.setFont(Font.font("System", FontWeight.BOLD, 20));
		section.getChildren().add(label);

		// Creating the GridPane for the items in the section
		section.getChildren().add(createGridPane(sectionItems));

		return section;
	}

	private GridPane createGridPane(List<String> items) {
		GridPane pane = new GridPane();
		pane.setVgap(10);
		pane.setHgap(20);

		int row = 0;
		int col = 0;
		int numChars = 0;
		for (int i = 0; i < items.size(); ++i) {
			CheckBox cb = new CheckBox(items.get(i).toUpperCase());
			cb.setFont(new Font(18));

			numChars += cb.getText().length();

			if (numChars >= 20 && col < 2) {
				row++;
				col++;
				numChars = 0;
			}

			pane.add(cb, row++ % 2, col++ / 2);

			// TODO: Bug if there are two identical items on hold it will select
			// both items. Should probably only select one of them.
			if (((List<String>) angel.get(Attribute.MISSING))
					.contains(cb.getText())) {
				cb.setSelected(true);
				Label label = new Label(cb.getText());
				label.setFont(new Font(18));
				selectedItemsVBox.getChildren().add(label);
			}

			cb.setOnAction(e -> { // A checkbox was selected or unselected
				if (cb.isSelected()) {
					Label label = new Label(cb.getText());
					label.setFont(new Font(18));
					// Adding the selected missing item to VBox to be displayed
					selectedItemsVBox.getChildren().add(label);
				} else {
					// Removing unselected value from the missing items display
					selectedItemsVBox.getChildren().removeIf(
							p -> cb.getText().equals(((Labeled) p).getText()));
				}
			});
		}
		return pane;
	}

	@FXML
	public void onHoldButtonHandler() {
		// TODO: Figure out a way to insert an array into the database without
		// have to convert the array into a string.
		String items = "[";
		int numOfItems = selectedItemsVBox.getChildren().size() - 1;

		if (numOfItems == -1) {
			items += "]";
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
			alert.setContentText(
					"This angel has nothing on hold.\n"
							+ "Would you like to mark it as complete?");
			alert.setHeaderText("Mark As Complete?");
			alert.showAndWait();
			if (alert.resultProperty().get() == ButtonType.YES) {
				dbController.update((String) angel.get(Attribute.ID),
						Attribute.STATUS.getType(), Status.COMPLETE,
						collection);
			}
		} else {

			for (int i = 0; i < numOfItems; ++i) {
				Label label = (Label) selectedItemsVBox.getChildren().get(i);
				items += "\"" + label.getText() + "\", ";
			}
			Label label = (Label) selectedItemsVBox.getChildren()
					.get(numOfItems);
			items += "\"" + label.getText() + "\"]";
			dbController.update((String) angel.get(Attribute.ID),
					Attribute.STATUS.getType(), Status.HOLD, collection);
		}

		dbController.query(
				"UPDATE {_key: '" + angel.get(Attribute.ID) + "'} WITH {'"
						+ Attribute.MISSING.getType()
						+ "':" + items + "} IN " + collection);
		super.switchScene(Displays.ANGEL_SELECTION);
	}

	@FXML
	public void cancelButtonHandler() {
		super.previousDisplay();
		StatusSelectController controller = (StatusSelectController) super.getController(
				Displays.ANGEL_STATUS);
		controller.setAngel(angel);
	}

}
