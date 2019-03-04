/**
 * @author
 */
package controllers.Angel;

import java.util.List;

import angels.Angel;
import angels.Attribute;
import angels.Status;
import controllers.Controller;
import customFX.StatusButton;
import database.DBCollection;
import database.DatabaseController;
import displays.AngelDisplays;
import displays.Display;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class RequestController extends Controller {

	@FXML
	private TextField requesteeField, angelRequest;
	@FXML
	private Button listButton, backButton, lendButton;
	@FXML
	private ScrollPane scrollPane;
	@FXML
	private GridPane resultGrid;

	private DatabaseController dbController;

	public RequestController(DatabaseController dbController) {
		this.dbController = dbController;
	}

	public void generateList() {
		// Getting angels in database that have not been started
		String query = "FOR doc IN " + DBCollection.ANGELS + " FILTER doc."
				+ Attribute.STATUS + " == '" + Status.NOT_STARTED + "' LIMIT "
				+ angelRequest.getText() + " RETURN doc";
		
		scrollPane.setVisible(true);

		resultGrid.getChildren().removeAll(resultGrid.getChildren());
		createGridPane(dbController.query(query));

		scrollPane.setContent(resultGrid);

	}

	private void createGridPane(List<Angel> results) {
		// Display a unique message when there are no results
		if (results == null || results.size() == 0) {
			Label label = new Label("No Results Found");
			label.setFont(new Font(42));
			label.setAlignment(Pos.CENTER);
			resultGrid.add(label, 0, 0);
		}

		// Creating a new button for each angel returned, allowing to view angel
		for (int i = 0; i < results.size(); i++) {
			Angel angel = results.get(i);
			StatusButton btn = new StatusButton(angel, 32, 8);
			btn.setOnMouseClicked(e -> {
				if (e.getButton() == MouseButton.SECONDARY) {
					results.remove(angel);
					resultGrid.getChildren().remove(btn);
				}
				e.consume();
			});
			btn.setOnAction(e -> {
				super.switchScenePreserve(AngelDisplays.ANGEL_INFO_DISPLAY);
				HoldController controller = (HoldController) AngelDisplays.ANGEL_INFO_DISPLAY
						.getController();
				controller.addAngel(angel);
			});

			resultGrid.add(btn, i % 5, i / 5);
		}

	}

	/**
	 * 
	 */
	public void lendButtonController() {
		for (int i = 0; i < resultGrid.getChildren().size(); ++i) {
			StatusButton btn = (StatusButton) resultGrid.getChildren().get(i);
			dbController.update(btn.getText(), Attribute.LOCATION,
					requesteeField.getText(), DBCollection.ANGELS);
			dbController.update(btn.getText(), Attribute.STATUS, Status.OUT,
					DBCollection.ANGELS);
		}
		backButton.fire();
	}

	public void previousDisplay() {
		super.switchScene(AngelDisplays.ANGEL_MAIN_MENU);
	}

}
