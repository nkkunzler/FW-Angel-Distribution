package controllers;

import angels.Angel;
import angels.Attribute;
import database.DatabaseController;
import display.Displays;
import javafx.fxml.FXML;

public class HoldController extends Controller {
	
	private Angel angel;
	private DatabaseController dbController;
	private String collection;

	public HoldController(DatabaseController dbController, String collection) {
		super(Displays.HOLD_DISPLAY);
		
		this.dbController = dbController;
		this.collection = collection;
	}
	
	public void addAngel(Angel angel) {
		this.angel = angel;
	}

	@FXML
	public void onHoldButtonHandler() {
		super.switchScene(Displays.ANGEL_SELECTION);
	}

}
