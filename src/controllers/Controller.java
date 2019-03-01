/**
 * This class is responsible to storing all the controllers to the fxml scenes
 * within the application. The class also uses the static instance of the
 * DisplayManager in order to manage switching between scenes.
 * 
 * @author Nicholas Kunzler
 */
package controllers;

import displays.Display;
import displays.DisplayManager;
import javafx.stage.Stage;

public class Controller {

	private Display display;

	/**
	 * Constructor for a display.
	 * 
	 * Protected as it should only be instantiated by classes that are Display.
	 * 
	 * @param display
	 */
	protected Controller(Display display) {
		super();
		this.display = display;
		DisplayManager.addDisplay(display, this);
	}

	/**
	 * Uses the SceneManager's switchScene() method in order to switch between
	 * scenes having to only use only the Controller class
	 * 
	 * @param sceneToSwitchTo The scene to switch two
	 */
	protected void switchScene(Display sceneToSwitchTo) {
		DisplayManager.switchScene(display, sceneToSwitchTo);
	}

	/**
	 * Uses the SceneManager's switchScenePreserve() method in order to switch
	 * between scenes while preserving the current state of the current scene.
	 * This method is to be used over switchScene() if maintaining the info on
	 * the current scene is important when having to switch back.
	 * 
	 * @param sceneToSwitchTo
	 */
	protected void switchScenePreserve(Display sceneToSwitchTo) {
		DisplayManager.switchScenePreserve(display, sceneToSwitchTo);
	}

	/**
	 * Uses the DisplayManager's perviousDisplay() method in order to switch to
	 * the display that called the current display using the Display
	 */
	protected Display previousDisplay() {
		return DisplayManager.previousDisplay();
	}

	/**
	 * Returns the controller associated with the desired display.
	 * 
	 * @param display The display to which to get the controller
	 * @return The controller associated with the desired display
	 */
	protected Controller getController(Display display) {
		return DisplayManager.getController(display);
	}

	/**
	 * @return Returns the stage associated with the display
	 */
	protected Stage getStage() {
		return DisplayManager.getStage();
	}
}
