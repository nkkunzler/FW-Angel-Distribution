/**
 * This class is responsible for switching between different Display within
 * the application.
 * 
 * @author Nicholas Kunzler
 */

package displays;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class DisplayManager {

	private static Stage stage;
	private static Display mainDisplay = null;
	private static FXMLLoader loader;
	private static Display currDisplay;

	private static Deque<Display> sceneStack;
	private static Map<Display, FXMLLoader> preservedScenes;

	/**
	 * Stores the stage for which the scenes will be placed.
	 * 
	 * @param primaryStage The main stage for where the scenes will be placed
	 */
	public DisplayManager(Stage primaryStage) {
		stage = primaryStage;

		// Stack of scenes visited up to the current scene
		sceneStack = new ArrayDeque<>(); // Stack

		// Scenes that will be preserved, meaning all information on the scene
		// will remain in going back to the scene.
		preservedScenes = new HashMap<>();
	}

	/**
	 * Switches between two different Display.
	 * 
	 * @param currentScene    The scene you are currently one
	 * @param sceneToSwitchTo The scene you would like to go to.
	 */
	public static void switchScene(Display sceneToSwitchTo, boolean saveToStack) {

		// Removing previous style classes that were loaded to display
		if (stage.getScene().getRoot().getStyleClass().size() > 1)
			stage.getScene().getRoot().getStyleClass().remove(0);

		if (preservedScenes.containsKey(sceneToSwitchTo)) {
			loader = preservedScenes.get(sceneToSwitchTo);
			currDisplay = sceneToSwitchTo;
			stage.getScene().setRoot(preservedScenes.remove(sceneToSwitchTo).getRoot());
			return;
		}

		try {
			if (saveToStack)
				sceneStack.push(currDisplay);
			loader = new FXMLLoader(DisplayManager.class.getClassLoader()
					.getResource(sceneToSwitchTo.getFile()));
			loader.setController(sceneToSwitchTo.getController());
			loader.load();
			stage.getScene().setRoot(loader.getRoot());
			currDisplay = sceneToSwitchTo;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Switches between two different Display while maintaining the information on
	 * the current scene, currentScene.
	 * 
	 * @param currentScene    The scene you are currently one
	 * @param sceneToSwitchTo The scene you would like to go to.
	 */
	public static void switchScenePreserve(Display sceneToSwitchTo) {
		preservedScenes.put(currDisplay, loader);
		switchScene(sceneToSwitchTo, true);
	}

	/**
	 * When called, the stage will replace the current scene being viewed with the
	 * scene that moved you to the current scene.
	 */
	public static void previousDisplay() {
		if (sceneStack.size() != 0)
			switchScene(sceneStack.pop(), false);
		else
			System.err.println("NO PREVIOUS DISPLAY");
	}

	/**
	 * Sets the main display for the application. The main display can only be set
	 * once.
	 * 
	 * @param display The display to make the main display.
	 */
	public static void setMainDisplay(Display display) {
		if (mainDisplay != null)
			return;
		mainDisplay = display;
	}

	/**
	 * Returns the main display within this DisplayManager.
	 * 
	 * @return The root of the main display.
	 */
	public static Parent getMainDisplay() {
		try {
			loader = new FXMLLoader(DisplayManager.class.getClassLoader()
					.getResource(mainDisplay.getFile()));
			loader.setController(mainDisplay.getController());
			loader.load();
			currDisplay = mainDisplay;
			return loader.getRoot();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @return The instance of the stage
	 */
	public static Stage getStage() {
		return stage;
	}
}
