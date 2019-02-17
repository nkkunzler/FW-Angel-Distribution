/**
 * This class is responsible to storing all the controllers to the fxml scenes
 * within the application. The class also uses the static instance of the
 * DisplayManager in order to manage switching between scenes.
 * 
 * @author Nicholas Kunzler
 */
package controllers;

import display.DisplayManager;
import display.Displays;
import javafx.stage.Stage;

public class Controller {

	private Displays display;

	/**
	 * Constructor for a display.
	 * 
	 * Protected as it should only be instantiated by classes that are displays.
	 * 
	 * @param display
	 */
	protected Controller(Displays display) {
		super();
		this.display = display;
		DisplayManager.addDisplay(display, this);
	}

	/**
	 * Uses the SceneManager's switchScene() method in order to switch between
	 * scenes having to only use only the GameScene class
	 * 
	 * @param sceneToSwitchTo The scene to switch two
	 */
	protected void switchScene(Displays sceneToSwitchTo) {
		DisplayManager.switchScene(display, sceneToSwitchTo);
	}

	/**
	 * Uses the DisplayManager's perviousDisplay() method in order to switch to
	 * the display that called the current display using the Display
	 */
	protected Displays previousDisplay() {
		return DisplayManager.previousDisplay();
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
	
	/**
	 * @return Returns the stage associated with the display
	 */
	protected Stage getStage() {
		return DisplayManager.getStage();
	}
}
