/**
 * This class is used to store all of the displays within the application.
 * 
 * New displays that are added needed to be added as a constant with a
 * provided .fxml file.
 * 
 * @author Nicholas Kunzler
 */
package display;

public enum Displays {

	// Display first shown on app start up
	MAIN_MENU("resources/fxml/MainMenu.fxml"),

	// Display to create and add a new angel to the database
	ADD_DISPLAY("resources/fxml/AddAngel.fxml"),

	// Display where the user can enter an angel id and select desired angel
	ANGEL_SELECTION("resources/fxml/AngelSelection.fxml"),

	// Display where the user can change the status of the angel
	ANGEL_STATUS("resources/fxml/AngelStatus.fxml"),
	
	// Displays where the user adds the items holding the angel
	HOLD_DISPLAY("resources/fxml/HoldDisplay.fxml"),
	
	// Displays where the user can export the database
	EXPORT_DISPLAY("resources/fxml/ExportDisplay.fxml");

	private final String fxmlFile;
	
	private Displays(String fxmlFile) {
		this.fxmlFile = fxmlFile;
	}
	/**
	 * @return String with the displays file location
	 */
	public String getFile() {
		return fxmlFile;
	}
}
