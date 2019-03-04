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
	/**
	 * Uses the SceneManager's switchScene() method in order to switch between
	 * scenes having to only use only the Controller class
	 * 
	 * @param sceneToSwitchTo The scene to switch two
	 */
	protected void switchScene(Display sceneToSwitchTo) {
		DisplayManager.switchScene(sceneToSwitchTo, false);
	}

	protected void switchScene(Display sceneToSwitchTo,
			boolean addToSceneStack) {
		DisplayManager.switchScene(sceneToSwitchTo, true);
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
		DisplayManager.switchScenePreserve(sceneToSwitchTo);
	}
	
	/**
	 * Uses the DisplayManager's perviousDisplay() method in order to switch to
	 * the display that called the current display using the Display
	 */
	protected void previousDisplay() {
		DisplayManager.previousDisplay();
	}

	/**
	 * @return Returns the stage associated with the display
	 */
	protected Stage getDisplayStage() {
		return DisplayManager.getStage();
	}
}
