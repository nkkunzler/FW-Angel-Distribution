/**
 * This class is used to store all of the displays within the application.
 * 
 * New displays that are added needed to be added as a constant with a
 * provided .fxml file.
 * 
 * @author Nicholas Kunzler
 */
package displays;

import controllers.MainMenuController;
import controllers.Angel.AddController;
import controllers.Angel.AngelInfoController;
import controllers.Angel.AngelMenuController;
import controllers.Angel.AngelSelectionController;
import controllers.Angel.ExportController;
import controllers.Angel.HoldController;
import controllers.Angel.RequestController;
import controllers.Angel.SearchDisplayController;
import controllers.Angel.StatusSelectController;
import database.DatabaseController;

public class AngelDisplays {
	// Display the main menu when the application first start ups
	public static final Display MAIN_MENU = new Display(
			"resources/fxml/MainMenu.fxml",
			new MainMenuController());

	// Main display for the angel main menu
	public static final Display ANGEL_MAIN_MENU = new Display(
			"resources/fxml/Angel/AngelMainMenu.fxml",
			new AngelMenuController());

	// Display to create and add a new angel to the database
	public static final Display ADD_DISPLAY = new Display(
			"resources/fxml/Angel/AddAngel.fxml",
			new AddController(DatabaseController.getInstance()));

	// Display where the user can enter an angel id and select desired angel
	public static final Display ANGEL_SELECTION = new Display(
			"resources/fxml/Angel/AngelSelection.fxml", 
			new AngelSelectionController(DatabaseController.getInstance()));

	// Display where the user can change the status of the angel
	public static final Display ANGEL_STATUS = new Display(
			"resources/fxml/Angel/AngelStatus.fxml",
			new StatusSelectController(DatabaseController.getInstance()));

	// Displays where the user adds the items holding the angel
	public static final Display HOLD_DISPLAY = new Display(
			"resources/fxml/Angel/HoldDisplay.fxml",
			new HoldController(DatabaseController.getInstance()));

	// Displays where the user can request angels for distribution
	public static final Display REQUEST_DISPLAY = new Display(
			"resources/fxml/Angel/RequestDisplay.fxml",
			new RequestController(DatabaseController.getInstance()));

	// Displays where the user can export the database
	public static final Display EXPORT_DISPLAY = new Display(
			"resources/fxml/Angel/ExportDisplay.fxml",
			new ExportController(DatabaseController.getInstance()));

	// Displays where the user can search the database
	public static final Display SEARCH_DISPLAY = new Display(
			"resources/fxml/Angel/SearchDisplay.fxml",
			new SearchDisplayController(DatabaseController.getInstance()));

	// Displays where the user can see all information of a given angel
	public static final Display ANGEL_INFO_DISPLAY = new Display(
			"resources/fxml/Angel/AngelInfoDisplay.fxml",
			new AngelInfoController());
}
