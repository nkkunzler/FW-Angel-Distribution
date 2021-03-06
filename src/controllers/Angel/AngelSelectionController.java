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
package controllers.Angel;

import java.util.List;

import angels.Angel;
import angels.Attribute;
import controllers.Controller;
import customFX.StatusButton;
import database.DatabaseController;
import displays.AngelDisplays;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class AngelSelectionController extends Controller {

	private final int FONT_SIZE = 42;

	@FXML
	private Button clearButton, backButton;
	@FXML
	private TextField idLabel;
	@FXML
	private BorderPane pane;

	private DatabaseController dbController;

	/**
	 * Constructor for the controller used to accept an angel id and display the
	 * angels with the id.
	 * 
	 * @param controller The database controller used to connect to the
	 *                   database.
	 * @param collection The collection in which an angel will eventually be
	 *                   added to.
	 */
	public AngelSelectionController(DatabaseController dbController) {
		this.dbController = dbController;
	}

	@FXML
	/**
	 * Updates the TextField that is displayed above the number pad based off of
	 * the buttons selected. The numbers that are entered are then used to query
	 * the database to find all documents that contain the id of the input
	 * value.
	 * 
	 * The buttons on the keypad include the numbers 0-9, in a phone keypad
	 * layout. There is also a backspace, <, and a Clear, C, button.
	 * 
	 * @param ae The ActionEvent associated with the label. This action event is
	 *           assigned in the EditStatus.fxml file.
	 */
	public void updateIDLabelActionEvent(ActionEvent ae) {
		Button selectedBtn = (Button) (ae.getSource());
		idLabel.setText(idLabel.getText() + selectedBtn.getText());

		// Puts buttons, representing angels, with the same id as input value
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
	 * Clears the idLabel TextField, which displays the angel being searched.
	 */
	public void clearButtonHandler() {
		idLabel.clear();
		pane.setCenter(null);
	}

	/**
	 * Removes the last decimal character from the search ID value.
	 */
	public void backButtonHandler() {
		if (!idLabel.getText().isEmpty()) {
			idLabel.setText(
					idLabel.getText().substring(0,
							idLabel.getText().length() - 1));
			// Create new buttons based off new value
			pane.setCenter(generateAngelIDButtons(idLabel.getText()));
		}
	}

	/**
	 * When the user puts in an angle id number, this method is responsible for
	 * querying the database and finding all angels with the given id.
	 * 
	 * @param angelID String representing the id of the angel to find within the
	 *                database
	 * @return
	 * @return A GridPane containing all the angels that match the angel id
	 *         input.
	 */
	private GridPane generateAngelIDButtons(String angelID) {
		// Searching for the angel id in database
		String query = "FOR doc IN angels "
				+ "FILTER LIKE(doc." + Attribute.ID
				+ ", " + "'" + angelID + "_')" // Find id similar to input
				+ " SORT doc." + Attribute.ID // Sort the ID, a -> z
				+ " RETURN doc";
		List<Angel> result = dbController.query(query);

		// Creating the buttons corresponding to input id value. Done on
		// Separate thread to prevent UI locking
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);

		// Do nothing if there are no results
		if (result.size() == 0) {
			Label label = new Label("No Results Found");
			label.setFont(new Font(FONT_SIZE));
			grid.add(label, 1, 1);
			return grid;
		}

		// Creating the buttons for each of the results
		for (int i = 0; i < result.size(); ++i) {
			Angel angel = result.get(i);
			StatusButton btn = new StatusButton(angel);
			btn.setOnAction(e -> {
				super.switchScene(AngelDisplays.ANGEL_STATUS);
				StatusSelectController ssc = (StatusSelectController) AngelDisplays.ANGEL_STATUS
						.getController();
				ssc.setAngel(angel);
			});
			grid.add(btn, i % 3, i / 3); // GridPane is 3x3.
		}
		return grid;
	}

	@FXML
	/**
	 * Goes back to the scene that resulted in this scene to be displayed
	 */
	public void toMainMenu() {
		super.switchScene(AngelDisplays.ANGEL_MAIN_MENU);
	}
}
