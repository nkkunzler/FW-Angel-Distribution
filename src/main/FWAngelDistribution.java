package main;

import controllers.AddController;
import controllers.DisplayManager;
import controllers.MainMenuController;
import controllers.Controller.Displays;
import database.Database;
import database.DatabaseController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FWAngelDistribution extends Application {
	
	private static final int TEMP_SIZE = 800;

	private static final String DB_NAME = "FW_Distribution";
	private static final String ANGEL_COLLECTION = "angels";
	private static DatabaseController dbController;

	public static void main(String[] args) {
		String user = System.getenv("ArangoDB_user");
		String password = System.getenv("ArangoDB_password");

		// TODO: Put database operations on a new thread?
		Database db = new Database(DB_NAME, user, password);
		dbController = new DatabaseController(db);
		dbController.createCollection(ANGEL_COLLECTION);

		launch();
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setWidth(TEMP_SIZE);
		stage.setHeight(TEMP_SIZE);
		//stage.setMaximized(true);

		// Creating the display manager to deal with switching displays
		new DisplayManager(stage);

		// Initializing the controllers for all the scenes
		initControllers();

		// Setting the first display to be the main menu
		DisplayManager.setMainDisplay(Displays.MAIN_MENU);

		stage.setScene(new Scene(DisplayManager.getMainDisplay()));
		stage.show();
		stage.setOnCloseRequest(e -> dbController.close()); // Close connection
	}

	private void initControllers() {
		new MainMenuController();
		new AddController(dbController, ANGEL_COLLECTION);
	}

}
