package controllers;

import java.util.List;

import angels.Angel;
import angels.Attribute;
import angels.Status;
import database.DatabaseController;
import display.Displays;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
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
	private ScrollPane scrollPane;

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
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		
		createGridPane(grid, dbController.query(query));
		
		scrollPane.setContent(grid);

	}

	private void createGridPane(GridPane pane, List<Angel> results) {
		if (results.size() == 0) {
			Label label = new Label("No angels left to distribute");
			label.setFont(new Font(22));
			pane.add(label, 0, 0);
			return;
		}

		for (int i = 0; i < results.size(); i++) {
			Angel angel = results.get(i);
			Label label = new Label((String) angel.get(Attribute.ID));
			label.setFont(new Font(22));
			if (angel.get(Attribute.GENDER).equals("boy"))
				label.setStyle("-fx-background-color: lightblue;");
			else
				label.setStyle("-fx-background-color: lightpink;");
			pane.add(label, i % 3, i / 3);
		}

	}

	public void previousDisplay() {
		super.switchScene(Displays.MAIN_MENU);
	}

}
