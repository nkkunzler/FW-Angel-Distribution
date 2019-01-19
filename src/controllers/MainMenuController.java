/**
 * This class is the controller to connect between the MainMenu.fxml file and
 * the JavaFX Node objects within the file. The purpose for this class is to
 * allow for switching between different displays to add, view, or edit angles
 * and additional settings.
 * 
 * @author Nicholas Kunzler
 */

package controllers;

import display.Displays;
import javafx.fxml.FXML;

public class MainMenuController extends Controller {

	/**
	 * Constructor that connects the MainMenu.fxml file to this controller.
	 */
	public MainMenuController() {
		super(Displays.MAIN_MENU);
	}

	/**
	 * Switches the scene from the MainMenu to the AddAngel.fxml display.
	 * 
	 * This method is called by the MainMenu.fxml file when the 'Add Angel'
	 * button has an action.
	 */
	@FXML
	public void switchToAddAngel() {
		super.switchScene(Displays.ADD_DISPLAY);
	}
	
	/**
	 * Switches the scene from the MainMenu to the EditAngel.fxml display.
	 * 
	 * This method is called by the MainMenu.fxml file when the 'Edit Angel'
	 * button has an action.
	 */
	@FXML
	public void switchToAngelSelection() {
		super.switchScene(Displays.ANGEL_SELECTION);
	}
	
	/**
	 * Switches the scene from the MainMenu to the ExportDisplay.fxml display.
	 * 
	 * This method is called by the MainMenu.fxml file when the 'Export'
	 * button has an action.
	 */
	@FXML
	public void switchToExportDisplay() {
		super.switchScene(Displays.EXPORT_DISPLAY);
	}
	// TODO : add comments
	@FXML
	public void switchToRequestDisplay() {
		super.switchScene(Displays.REQUEST_DISPLAY);
	}
}


