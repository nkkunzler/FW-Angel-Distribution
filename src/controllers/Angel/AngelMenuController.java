/**
 * This class is the controller to connect between the MainMenu.fxml file and
 * the JavaFX Node objects within the file. The purpose for this class is to
 * allow for switching between different displays to add, view, or edit angles
 * and additional settings.
 * 
 * @author Nicholas Kunzler
 */

package controllers.Angel;

import controllers.Controller;
import displays.AngelDisplays;
import javafx.fxml.FXML;

public class AngelMenuController extends Controller {

	/**
	 * Switches the scene from the AngelMainMenu to the AddAngel.fxml display.
	 * 
	 * This method is called by the AngelMainMenu.fxml file when the 'Add Angel'
	 * button has an action.
	 */
	@FXML
	public void switchToAddAngel() {
		super.switchScene(AngelDisplays.ADD_DISPLAY);
	}

	/**
	 * Switches the scene from the AngelMainMenu to the EditAngel.fxml display.
	 * 
	 * This method is called by the AngelMainMenu.fxml file when the 'Edit
	 * Angel' button has an action.
	 */
	@FXML
	public void switchToAngelSelection() {
		super.switchScene(AngelDisplays.ANGEL_SELECTION);
	}

	/**
	 * Switches the scene from the AngelMainMenu to the ExportDisplay.fxml
	 * display.
	 * 
	 * This method is called by the AngelMainMenu.fxml file when the 'Export'
	 * button has an action.
	 */
	@FXML
	public void switchToExportDisplay() {
		super.switchScene(AngelDisplays.EXPORT_DISPLAY);
	}

	/**
	 * Switches the scene from the MainMenu to the RequestDisplay.fxml display.
	 * 
	 * This method is called by the AngelMainMenu.fxml file when the 'Request
	 * Angel' button has an action.
	 */
	@FXML
	public void switchToRequestDisplay() {
		super.switchScene(AngelDisplays.REQUEST_DISPLAY);
	}

	/**
	 * Switches the scene from the MainMenu to the SearchDisplay.fxml display.
	 * 
	 * This method is called by the AngelMainMenu.fxml file when the 'Search'
	 * button has an action.
	 */
	@FXML
	public void switchToSearchDisplay() {
		super.switchScene(AngelDisplays.SEARCH_DISPLAY);
	}

	/**
	 * Switches the scene from the AngelMainMenu to the MainMenu.fxml display.
	 * 
	 * This method is called by the AngelMainMenu.fxml file when the 'To Main
	 * Menu' button has an action.
	 */
	@FXML
	public void switchToMainMenu() {
		super.switchScene(AngelDisplays.MAIN_MENU);
	}
}
