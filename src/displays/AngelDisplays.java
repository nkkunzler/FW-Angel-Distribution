/**
 * This class is used to store all of the displays within the application.
 * 
 * New displays that are added needed to be added as a constant with a
 * provided .fxml file.
 * 
 * @author Nicholas Kunzler
 */
package displays;

// Could try to make Enum
public class AngelDisplays {
	// Display the main menu when the app first start ups
	public static final Display MAIN_MENU = new Display(
			"resources/fxml/MainMenu.fxml");

	// Main display for the angel main menu
	public static final Display ANGEL_MAIN_MENU = new Display(
			"resources/fxml/Angel/AngelMainMenu.fxml");

	// Display to create and add a new angel to the database
	public static final Display ADD_DISPLAY = new Display(
			"resources/fxml/Angel/AddAngel.fxml");

	// Display where the user can enter an angel id and select desired angel
	public static final Display ANGEL_SELECTION = new Display(
			"resources/fxml/Angel/AngelSelection.fxml");

	// Display where the user can change the status of the angel
	public static final Display ANGEL_STATUS = new Display(
			"resources/fxml/Angel/AngelStatus.fxml");

	// Displays where the user adds the items holding the angel
	public static final Display HOLD_DISPLAY = new Display(
			"resources/fxml/Angel/HoldDisplay.fxml");

	// Displays where the user can request angels for distribution
	public static final Display REQUEST_DISPLAY = new Display(
			"resources/fxml/Angel/RequestDisplay.fxml");

	// Displays where the user can export the database
	public static final Display EXPORT_DISPLAY = new Display(
			"resources/fxml/Angel/ExportDisplay.fxml");

	// Displays where the user can search the database
	public static final Display SEARCH_DISPLAY = new Display(
			"resources/fxml/Angel/SearchDisplay.fxml");

	// Displays where the user can see all information of a given angel
	public static final Display ANGEL_INFO_DISPLAY = new Display(
			"resources/fxml/Angel/AngelInfoDisplay.fxml");
}
