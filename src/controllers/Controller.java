/**
 * This class is responsible to storing all the controllers to the fxml scenes
 * within the application. The class also uses the static instance of the
 * DisplayManager in order to manage switching between scenes.
 * 
 * @author Nicholas Kunzler
 */
package controllers;

public class Controller {

	/**
	 * The Displays enum is used to store all the displays within the
	 * application and their corresponding fxml file paths.
	 */
	public enum Displays {
		MAIN_MENU("resources/fxml/MainMenu.fxml"),
		ADD_DISPLAY("resources/fxml/AddAngel.fxml");

		private String fxmlFile;

		private Displays(String fxmlFile) {
			this.fxmlFile = fxmlFile;
		}

		/**
		 * @return The file location of the displays fxml
		 */
		public String getFile() {
			return fxmlFile;
		}
	}

	private Displays displayName;

	/**
	 * Constructor for a display.
	 * 
	 * Protected as it should only be instantiated by classes that are displays.
	 * 
	 * @param display
	 */
	protected Controller(Displays display) {
		this.displayName = display;
		DisplayManager.addController(display, this);
	}

	/**
	 * Uses the SceneManager's switchScene() method in order to switch between
	 * scenes having to only use only the GameScene class
	 * 
	 * @param sceneToSwitchTo The scene to switch two
	 */
	protected void switchScene(Displays sceneToSwitchTo) {
		DisplayManager.switchScene(displayName, sceneToSwitchTo);
	}

	/**
	 * Uses the DisplayManager's perviousDisplay() method in order to switch to
	 * the display that called the current display using the Display
	 */
	protected void previousDisplay() {
		DisplayManager.previousDisplay();
	}
}
