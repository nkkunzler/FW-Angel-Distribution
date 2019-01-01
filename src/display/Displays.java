package display;

public class Displays {

	private static final String BASE_LOC = "resources/fxml/";;

	public static final Displays MAIN_MENU = new Displays("MainMenu.fxml");
	public static final Displays ADD_DISPLAY = new Displays("AddAngel.fxml");
	public static final Displays EDIT_DISPLAY = new Displays("EditStatus.fxml");

	private String fxmlFile;

	private Displays(String fxmlFile) {
		this.fxmlFile = BASE_LOC + fxmlFile;
	}

	public String getFile() {
		return fxmlFile;
	}
}
