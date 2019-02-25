package controllers;

import java.util.List;

import angels.Angel;
import angels.Attribute;
import angels.Status;
import customFX.StatusButton;
import database.DatabaseController;
import display.Displays;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class RequestController extends Controller {

	@FXML
	private TextField requesteeField;
	@FXML
	private TextField angelRequest;
	@FXML
	private Button listButton;
	@FXML
	private Button backButton;
	@FXML
	private Button lendButton;
	@FXML
	private ScrollPane scrollPane;
	@FXML
	private GridPane resultGrid;

	private DatabaseController dbController;
	private String dbCollection;

	public RequestController(DatabaseController dbController,
			String dbCollection) {
		super(Displays.REQUEST_DISPLAY);
		this.dbController = dbController;
		this.dbCollection = dbCollection;
	}

	public void generateList() {
		String query = "FOR doc IN " + dbCollection + " FILTER doc."
				+ Attribute.STATUS + " == '" + Status.AWAITING + "' LIMIT "
				+ angelRequest.getText() + " RETURN doc";

		resultGrid.getChildren().removeAll(resultGrid.getChildren());
		createGridPane(dbController.query(query));

		scrollPane.setContent(resultGrid);

	}

	private void createGridPane(List<Angel> results) {
		// Display a unique message when there are no results
		if (results == null)
			return;
		if (results.size() == 0) {
			Label label = new Label("No Results Found");
			label.setFont(new Font(42));
			label.setAlignment(Pos.CENTER);
			resultGrid.add(label, 0, 0);
		}

		// Creating a new button for each angel returned, allowing to view angel
		for (int i = 0; i < results.size(); i++) {
			Angel angel = results.get(i);
			StatusButton btn = new StatusButton(angel, 32, 8);
			// Go to HoldDisplay.fxml display when pressed
			btn.setOnMouseClicked(e -> {
				if (e.getButton() == MouseButton.SECONDARY) {
					results.remove(angel);
					resultGrid.getChildren().remove(btn);
				}
				e.consume();
			});
			btn.setOnAction(e -> {
				super.switchScene(Displays.HOLD_DISPLAY);
				HoldController controller = (HoldController) super.getController(
						Displays.HOLD_DISPLAY);
				controller.addAngel(angel);
			});

			resultGrid.add(btn, i % 5, i / 5);
		}

	}

	public void lendButtonController() {
		for (int i = 0; i < resultGrid.getChildren().size(); ++i) {
			StatusButton btn = (StatusButton) resultGrid.getChildren().get(i);
			dbController.update(btn.getText(), Attribute.LOCATION,
					requesteeField.getText(), dbCollection);
			dbController.update(btn.getText(), Attribute.STATUS, Status.OUT,
					dbCollection);
		}
		backButton.fire();
	}

	public Displays previousDisplay() {
		super.switchScene(Displays.MAIN_MENU);
		return null;
	}

}
