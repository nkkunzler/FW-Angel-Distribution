/**
 * This class is responsible to storing all the controllers to the fxml scenes
 * within the application. The class also uses the static instance of the
 * DisplayManager in order to manage switching between scenes.
 * 
 * @author Nicholas Kunzler
 */
package controllers;

import angels.Angel;
import display.DisplayManager;
import display.Displays;

public class Controller {

	private Displays displayName;

	/**
	 * Constructor for a display.
	 * 
	 * Protected as it should only be instantiated by classes that are displays.
	 * 
	 * @param display
	 */
	protected Controller(Displays display) {
		super();
		this.displayName = display;
		DisplayManager.addDisplay(display, this);
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

	/**
	 * Returns the controller associated with the desired display.
	 * 
	 * @param display The display to which to get the controller
	 * @return The controller associated with the desired display
	 */
	protected Controller getController(Displays display) {
		return DisplayManager.getController(display);
	}
}
