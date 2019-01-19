package controllers;

import database.DatabaseController;
import display.Displays;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class RequestController extends Controller{

	@FXML
	private TextField requesteeField;
	@FXML
	private TextField angelField;
	@FXML
	private Button listButton;
	@FXML
	private Button backButton;
	
	private DatabaseController dbController;
	private String dbCollection;
	
	public RequestController(DatabaseController dbController, String dbCollection ) {
		super(Displays.REQUEST_DISPLAY);
		this.dbController=dbController;
		this.dbCollection=dbCollection;
	}
	
	public void generateList() {
		
	}
	
	public void previousDisplay() {
		super.switchScene(Displays.MAIN_MENU);
	}

}
