package controllers;

import java.util.List;

import angels.Angel;
import angels.Attribute;
import database.DatabaseController;
import display.Displays;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class SearchDisplayController extends Controller {

	@FXML
	private TextField keywordTextField;
	@FXML
	private VBox keywordVBox;
	@FXML
	private ComboBox<String> attributeComboBox;
	@FXML
	private Button searchButton;
	@FXML
	private ScrollPane resultPane;
	@FXML
	private GridPane resultGridPane;

	private DatabaseController dbController;
	private String collection;

	public SearchDisplayController(DatabaseController dbController,
			String collection) {
		super(Displays.SEARCH_DISPLAY);

		this.dbController = dbController;
		this.collection = collection;
	}

	/**
	 * The code within this method runs after all FXML objects have been loaded
	 */
	public void initialize() {
		populateComboBox(attributeComboBox);
		resultPane.setVisible(false);
	}

	@FXML
	/*
	 * Populates the ComboBox on the top right of the screen with all attributes
	 * associated with an angel.
	 */
	private void populateComboBox(ComboBox<String> box) {
		box.setButtonCell(new ListCell<String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				setStyle("-fx-font-size:" + 18);
				if (!empty || item != null) {
					super.setText(item.toUpperCase());
				}
			}
		});

		// Add all the attributes for an angel to the combo box
		for (Object obj : Attribute.values()) {
			box.getItems().add(obj.toString());
		}

	}

	/**
	 * Creates a new HBox to allow for a new search criteria field to be
	 * created. The field contains an +, for adding a new field, - for removing
	 * a field, ComboBox for selecting an attribute, and TextField for entering
	 * keyword.
	 * 
	 * @return HBox contain the Nodes for a new search field
	 */
	private HBox createNewKeywordSearch() {
		HBox hb = new HBox();
		hb.setSpacing(20);

		TextField field = new TextField();
		field.setPromptText("Keyword");
		field.setFont(new Font(20));

		ComboBox<String> cb = new ComboBox<String>();
		cb.setPromptText("Select Attribute");
		populateComboBox(cb);

		Button addSearchBtn = new Button("+");
		addSearchBtn.setFont(new Font(16));
		addSearchBtn.setOnAction(e -> {
			// Only create a new search field when the current one has a value
			if (!field.getText().isEmpty())
				keywordVBox.getChildren().add(createNewKeywordSearch());
		});

		Button rmSearchBtn = new Button("-");
		rmSearchBtn.setFont(new Font(16));
		rmSearchBtn.setOnAction(e -> keywordVBox.getChildren().remove(hb));

		hb.getChildren().addAll(field, cb, addSearchBtn, rmSearchBtn);
		return hb;
	}

	/**
	 * Creates a new keyword search input when the plus, to the top right of the
	 * screen, is pressed.
	 */
	@FXML
	public void addSearchController() {
		if (!keywordTextField.getText().isEmpty())
			keywordVBox.getChildren().add(createNewKeywordSearch());
	}

	/*
	 * When the user presses the 'Search' button at the top of the display then
	 * this method is called.
	 */
	@FXML
	public void search() {
		String query = createSearchQuery();
		
		if (query == null)
			return;

		// Clearing the panes from the previous search result
		resultPane.setVisible(true);
		resultGridPane.getChildren().removeAll(resultGridPane.getChildren());

		// Results of the query
		List<Angel> results = dbController.query(query);

		// Display a unique message when there are no results
		if (results.size() == 0) {
			Label label = new Label("No Results Found");
			label.setFont(new Font(42));
			label.setAlignment(Pos.CENTER);
			resultGridPane.add(label, 0, 0);
		}

		// Creating a new button for each angel returned, allowing to view angel
		for (int i = 0; i < results.size(); i++) {
			Angel angel = results.get(i);
			Button btn = resultButton(angel);
			resultGridPane.add(btn, i % 5, i / 5);
		}
	}

	/**
	 * Following method adds the filter contents given in the keyword inputs.
	 * 
	 * Creates something like: FILTER CONTAINS(LOWER(doc.ID, LOWER(1A))) FILTER
	 * CONTAINS(LOWER(doc.AGE, LOWER(10)))
	 */
	@SuppressWarnings("unchecked")
	private String createSearchQuery() {
		String query = "FOR doc IN " + collection;

		boolean containsFilter = false; // If no filters null is returned
		HBox keywordHBox;
		TextField keywordTF; // First child in HBox
		ComboBox<String> keywordCB; // Second child in HBox

		// keywordVBox is where the keyword TextField and CB are located
		for (int i = 0; i < keywordVBox.getChildren().size(); ++i) {
			keywordHBox = (HBox) keywordVBox.getChildren().get(i);
			keywordTF = (TextField) keywordHBox.getChildren().get(0);
			keywordCB = (ComboBox<String>) keywordHBox.getChildren().get(1);

			// Do not include empty filters
			if (keywordTF.getText().isEmpty())
				continue;
			// Default of CB is ID
			if (keywordCB.getValue() == null)
				keywordCB.setValue("ID");
			containsFilter = true;
			query += " FILTER CONTAINS(LOWER(doc." + keywordCB.getValue()
					+ "), LOWER('" + keywordTF.getText() + "'))\n";
		}
		query += " RETURN doc";
		
		if (containsFilter)
			return query;
		return null;
	}

	/**
	 * Creates a button, with colors corresponding to the two genders. The
	 * buttons are linked to go the HoldDisplay.fxml display to show which items
	 * the angel possess.
	 * 
	 * @param angel The angel corresponding to the button
	 * @return A button with the color corresponding to the angel color and
	 *         linked to transfer to HoldDisplay Display when pressed.
	 */
	private Button resultButton(Angel angel) {
		Button btn = new Button();
		btn.setAlignment(Pos.CENTER);
		btn.setFont(new Font(24));
		btn.setText(angel.get(Attribute.ID).toString());

		// Go to HoldDisplay.fxml display when pressed
		btn.setOnAction(e -> {
			super.switchScene(Displays.HOLD_DISPLAY);
			HoldController controller = (HoldController) super.getController(
					Displays.HOLD_DISPLAY);
			controller.addAngel(angel);
		});

		// Coloring button depending on gender
		String sex = (String) angel.get(Attribute.GENDER);
		if (sex.equalsIgnoreCase("boy"))
			btn.setStyle("-fx-background-color: lightblue;");
		else
			btn.setStyle("-fx-background-color: lightpink;");
		return btn;
	}

	@FXML
	/*
	 * When the user presses the 'Back' button on the bottom of the display this
	 * method is called.
	 */
	public Displays previousDisplay() {
		return super.previousDisplay();
	}

}
