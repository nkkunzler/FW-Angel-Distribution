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

import controllers.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class DisplayManager {

	private static Stage stage;
	private static Display mainDisplay = null;
	private static Map<Display, Controller> controllers;
	private static Deque<Display> sceneStack;

	/**
	 * Stores the stage for which the scenes will be placed.
	 * 
	 * @param primaryStage The main stage for where the scenes will be placed
	 */
	public DisplayManager(Stage primaryStage) {
		stage = primaryStage;

		// Stack of scenes visited up to the current scene
		sceneStack = new ArrayDeque<>(); // Stack
		// preservedSceneStack = new ArrayDeque<>();

		// Map that maps Display to their controllers
		controllers = new HashMap<>();
	}

	public static void addDisplay(Display display,
			Controller controller) {
		controllers.put(display, controller);
	}

	/**
	 * Switches between two different Display.
	 * 
	 * @param currentScene    The scene you are currently one
	 * @param sceneToSwitchTo The scene you would like to go to.
	 */
	public static void switchScene(Display currentScene,
			Display sceneToSwitchTo) {

		// Removing previous style classes that were loaded to display
		if (stage.getScene().getRoot().getStyleClass().size() > 1)
			stage.getScene().getRoot().getStyleClass().remove(0);

		try {
			sceneStack.push(currentScene);
			FXMLLoader loader = new FXMLLoader(DisplayManager.class
					.getClassLoader().getResource(sceneToSwitchTo.getFile()));
			loader.setController(controllers.get(sceneToSwitchTo));
			stage.getScene().setRoot(loader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Switches between two different Display while maintaining the information
	 * on the current scene, currentScene.
	 * 
	 * @param currentScene    The scene you are currently one
	 * @param sceneToSwitchTo The scene you would like to go to.
	 */
	public static void switchScenePreserve(Display currentScene,
			Display sceneToSwitchTo) {
		// Removing previous style classes that were loaded to display
		if (stage.getScene().getRoot().getStyleClass().size() > 1)
			stage.getScene().getRoot().getStyleClass().remove(0);

		try {
			sceneStack.push(currentScene);
			FXMLLoader loader = new FXMLLoader(DisplayManager.class
					.getClassLoader().getResource(sceneToSwitchTo.getFile()));
			loader.setController(controllers.get(sceneToSwitchTo));
			stage.getScene().setRoot(loader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * When called, the stage will replace the current scene being viewed with
	 * the scene that moved you to the current scene.
	 */
	public static Display previousDisplay() {
		if (sceneStack.isEmpty()) {
			System.err.println("There is no previous scene");
			return null;
		}

		// Removing previous style classes that were loaded to display
		if (stage.getScene().getRoot().getStyleClass().size() > 1)
			stage.getScene().getRoot().getStyleClass().remove(0);

		Display prevDisplay = null;
		try {
			prevDisplay = sceneStack.pop();
			FXMLLoader loader;
			loader = new FXMLLoader(DisplayManager.class
					.getClassLoader().getResource(prevDisplay.getFile()));
			loader.setController(controllers.get(prevDisplay));
			stage.getScene().setRoot(loader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prevDisplay;
	}

	/**
	 * Returns the controller associated with the display requested.
	 * 
	 * @param display The display to get the controller for
	 * @return The controller associated with the display.
	 */
	public static Controller getController(Display display) {
		return controllers.get(display);
	}

	/**
	 * Sets the main display for the application. The main display can only be
	 * set once.
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
		if (controllers.get(mainDisplay) == null) {
			System.err.println("Controller for main display does not exist");
		}

		try {
			FXMLLoader loader = new FXMLLoader(DisplayManager.class
					.getClassLoader().getResource(mainDisplay.getFile()));
			loader.setController(controllers.get(mainDisplay));
			return loader.load();
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
