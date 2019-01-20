package controllers;

import java.util.List;
import java.util.concurrent.ExecutionException;

import angels.Angel;
import angels.Attribute;
import angels.Status;
import database.DatabaseController;
import display.Displays;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class RequestController extends Controller {

	@FXML
	private TextField requesteeField;
	@FXML
	private TextField angelField;
	@FXML
	private Button listButton;
	@FXML
	private Button backButton;
	@FXML
	private ScrollPane scrollPane;

	private DatabaseController dbController;
	private String dbCollection;

	public RequestController(DatabaseController dbController, String dbCollection) {
		super(Displays.REQUEST_DISPLAY);
		this.dbController = dbController;
		this.dbCollection = dbCollection;
	}

	public GridPane generateList() {
		String query = "For doc IN" + dbController + "FILTER doc." + Attribute.STATUS + "==" + Status.AWAITING + "LIMIT"
				+ angelField.getText() + "RETURN doc";
		Task<List<Angel>> results = dbController.query(query);
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		results.setOnSucceeded(e ->{
			
			try {
				createGridPane(grid, results.get());
			} catch (InterruptedException | ExecutionException e1) {
				e1.printStackTrace();
			}
		});
		scrollPane.setContent(grid);
		return grid;
			

	}
	private void createGridPane(GridPane pane, List<Angel>results) {
		if(results.size() == 0) {
			pane.add(new Label("No Angels to give out"),0,0);
			return;
		}
		
		for(int i=0; i < results.size(); i++) {
			Angel angel=results.get(i);
			Label label=new Label((String)angel.get(Attribute.ID));
			pane.add(label, i%3, i/3);
		}
		
	}

	public void previousDisplay() {
		super.switchScene(Displays.MAIN_MENU);
	}

}
