/**
 * This class is the controller to connect between the EditStatus.fxml file and
 * the JavaFX Node objects within the file. The purpose for this class is to
 * allow for reading in of angel id's, through user input, and displaying all
 * the angels with the given id.
 * 
 * The provided id will only be an integer value, not letters will be allowed.
 * 
 * The angels matching the id provided will then be able to be selected in
 * order to be able to change the status of an angel to either complete or
 * hold.
 * 
 * @author Nicholas Kunzler
 */
package controllers;

import java.util.List;

import angels.Angel;
import angels.Attribute;
import database.DatabaseController;
import display.Displays;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class AngelSelectionController extends Controller {

	@FXML
	private Button clearButton, backButton;
	@FXML
	private TextField idLabel;
	@FXML
	private BorderPane pane;

	DatabaseController dbController;
	String dbCollection;

	/**
	 * Constructor for the controller used to accept an angel id and display the
	 * angels with the id.
	 * 
	 * @param controller The database controller used to connect to the
	 *                   database.
	 * @param collection The collection in which an angel will eventually be
	 *                   added to.
	 */
	public AngelSelectionController(DatabaseController dbController,
			String dbCollection) {
		super(Displays.ANGEL_SELECTION);

		this.dbController = dbController;
		this.dbCollection = dbCollection;
	}

	@FXML
	/**
	 * Updates the TextField that is displayed above the number pad based off of
	 * the buttons selected.
	 * 
	 * The buttons on the keypad include the numbers 0-9, in a phone keypad
	 * layout. There is also a backspace, <, and a Clear, C, button.
	 * 
	 * @param ae The ActionEvent associated with the label. This action event is
	 *           assigned in the EditStatus.fxml file.
	 */
	public void updateIDLabelActionEvent(ActionEvent ae) {
		Button button = (Button) (ae.getSource());
		String btnText = button.getText();

		if (button == clearButton) { // The clear button
			idLabel.clear();
			pane.setCenter(generateAngelIDButtons(null));
			return; // Do not need to query the database
		} else if (button == backButton) {
			if (!idLabel.getText().equals("")) // Don't delete if id is empty
				idLabel.setText(idLabel.getText().substring(0,
						idLabel.getText().length() - 1));
		} else
			idLabel.setText(idLabel.getText() + btnText);

		// Queries database for id provided and displays results on
		// the BorderPane.
		pane.setCenter(generateAngelIDButtons(idLabel.getText()));
	}

	/**
	 * Updates the TextField that is displayed above the number pad based off of
	 * the inputs provided through the keyboard.
	 * 
	 * The only keys that are recognized by keyboard input is the backspace,
	 * escape for clearing, and the number keys.
	 * 
	 * @param ae The ActionEvent associated with the label. This action event is
	 *           assigned in the EditStatus.fxml file.
	 */
	public void updateIDLabelKeyPress(KeyEvent ke) {
		if (ke.getCode() == KeyCode.ESCAPE)
			clearButton.fire(); // Calls updateIDLabelActionEvent()
		else if (ke.getCode() == KeyCode.BACK_SPACE)
			backButton.fire(); // Calls updateIDLabelActionEvent()
		else if (ke.getText().matches("\\d*")) { // Checks input is a number
			idLabel.setText(idLabel.getText() + ke.getText());
			pane.setCenter(generateAngelIDButtons(idLabel.getText()));
		}
	}

	/**
	 * Creates the buttons for the angels that match the input for the angel id.
	 * The buttons are colored corresponding to the gender of the angel, blue
	 * for boy and pink for girl. The buttons when selected change the display
	 * to allow for a selection of changing the status of the angel to complete,
	 * on hold, or pull.
	 * 
	 * @param angelID String representing the id of the angel to find within the
	 *                database
	 * @return A GridPane containing all the angels that match the angel id
	 *         input.
	 */
	private GridPane generateAngelIDButtons(String angelID) {
		// Searching for the angel id in database
		String query = "FOR doc IN angels "
				+ "FILTER LIKE(doc.id, " + "'" + angelID + "_')"
				+ "SORT doc.id "
				+ "RETURN doc";
		List<Angel> result = dbController.query(query);

		// Do nothing if there are no results
		if (result == null)
			return null;

		// Creating GridPane used to display the found angels
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);

		// Creating the buttons for each of the results
		for (int i = 0; i < result.size(); ++i) {
			Angel angel = result.get(i);
			String id = (String) angel.get(Attribute.ID);
			String sex = (String) angel.get(Attribute.SEX);
			Button btn = new Button(id);
			btn.setFont(new Font(42));

			if (sex.equalsIgnoreCase("boy")) // Boys are colored light blue
				btn.setStyle("-fx-background-color: lightblue;");
			else // Girls are colored light pink
				btn.setStyle("-fx-background-color: lightpink;");
			grid.add(btn, i % 3, i / 3); // GridPane is 3x3.

			btn.setOnAction(e -> {
				super.switchScene(Displays.ANGEL_STATUS);
				StatusSelectController ssc = (StatusSelectController) super.getController(
						Displays.ANGEL_STATUS);
				ssc.setAngel(angel);
			});
		}
		return grid;
	}

	@FXML
	/**
	 * Goes back to the scene that resulted in this scene to be displayed
	 */
	public void toMainMenu() {
		super.previousDisplay();
	}

}
