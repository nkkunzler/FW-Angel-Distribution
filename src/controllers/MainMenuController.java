package controllers;

import displays.AngelDisplays;
import javafx.fxml.FXML;

public class MainMenuController extends Controller {

	public MainMenuController() {
		super(AngelDisplays.MAIN_MENU);
	}
	
	@FXML
	private void switchToAngelMenu() {
		super.switchScene(AngelDisplays.ANGEL_MAIN_MENU);
	}

}
