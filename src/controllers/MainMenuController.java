/**
 * This class is the controller to connect between the MainMenu.fxml file and
 * the JavaFX Node objects within the file. The purpose for this class is to
 * allow for switching between different displays to add, view, or edit angles
 * and additional settings.
 * 
 * @author Nicholas Kunzler
 */

package controllers;

import javafx.fxml.FXML;

public class MainMenuController extends Controller {

	/**
	 * Constructor that connects the MainMenu.fxml file to this controller.
	 */
	public MainMenuController() {
		super(Displays.MAIN_MENU);
	}

	@FXML
	/**
	 * Switches the scene from the MainMenu to the AddAngel.fxml display.
	 * 
	 * This method is called by the AddAngel.fxml file when the 'Add Angel'
	 * button has an action.
	 */
	public void switchToAddAngel() {
		super.switchScene(Displays.ADD_DISPLAY);
	}
}
