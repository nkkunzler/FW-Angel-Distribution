package main;

import angels.Angel;
import angels.Attribute;
import angels.Status;
import controllers.AddController;
import controllers.AngelSelectionController;
import controllers.ExportController;
import controllers.HoldController;
import controllers.MainMenuController;
import controllers.StatusSelectController;
import database.Database;
import database.DatabaseController;
import display.DisplayManager;
import display.Displays;
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

		Database db = new Database(DB_NAME, user, password);
		dbController = new DatabaseController(db);
		dbController.createCollection(ANGEL_COLLECTION);

		// Uncomment if you want to generate angels within the angels database.
		//populateDatabase();

		launch();
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setWidth(TEMP_SIZE);
		stage.setHeight(TEMP_SIZE);
		// stage.setMaximized(true);

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

	/**
	 * Creating all the controllers need for the displays.
	 */
	private void initControllers() {
		new MainMenuController();
		new AddController(dbController, ANGEL_COLLECTION);
		new AngelSelectionController(dbController, ANGEL_COLLECTION);
		new StatusSelectController(dbController, ANGEL_COLLECTION);
		new HoldController(dbController, ANGEL_COLLECTION);
		new ExportController(dbController);
	}

	/**
	 * Used to populate the database with 10 angels
	 * 
	 * Not the nicest looking
	 */
	private static void populateDatabase() {
		String[] kid_clothes_size = { "2T", "2", "3", "3T", "4", "5", "6/7",
				"S", "M" };
		String[] adult_clothes_size = { "7/8", "8", "8/10", "10/12", "L", "M",
				"S", "XL" };
		String[] men_shoe_size = { "M2", "M3", "M4", "M5", "M6", "M7", "M8",
				"M9", "M10", "M11", "M12", "M13", "M7W", "M6W", "M3W", "M9W",
				"M4W" };
		String[] women_shoe_size = { "W2", "W3", "W4", "W5", "W6", "W7", "W8",
				"W9", "W10", "W11", "W12", "W13", "W7W", "W6W", "W3W", "W9W",
				"W4W" };
		String[] kid_shoe_size = { "K2", "K3", "K4", "K5", "K6", "K7", "K8",
				"K9", "K10", "K11", "K12", "K13", "K7W", "K6W", "K3W", "K9W",
				"K4W" };

		String[] books = { "Moon and Sixpence", "Political Philosophers",
				"Short History of  World", "Trembling of a Leaf",
				"Doctor on  Brain",
				"Simpsons & ir Mamatical Secrets", "Pattern Classification",
				"From Beirut to Jerusalem", "Code Book", "Age of  Warrior",
				"Final Crisis",
				"Killing Joke", "Flashpoint", "Batman Earth One",
				"Crisis on Infinite Earths", "Numbers Behind Numb3rs",
				"Superman Earth One", "Christmas Carol" };

		String[] wish = { "doll", "action figure", "shoes", "clothes",
				"coloring book", "penciles", "paw patrol", "slime",
				"headphones", "lol dolls", "puzzles", "baseball bat",
				"dodgeball", "soccer ball" };

		for (int angelNum = 1; angelNum <= 10; angelNum++) {
			for (int charIndex = 0; charIndex < Math.random() * 6
					+ 1; ++charIndex) {
				int row = angelNum;
				String id = "";
				int age = -1;
				String sex = "";
				String shoe_size = "";
				String clothes_size = "";
				String[] wishes = { wish[(int) (Math.random() * wish.length)],
						wish[(int) (Math.random() * wish.length)] };
				String[] book = { books[(int) (Math.random() * books.length)] };
				String[] special = { wish[(int) (Math.random() * wish.length)],
						wish[(int) (Math.random() * wish.length)] };
				String letter = (char) (65 + charIndex) + "";
				id = row + letter;
				age = (int) (Math.random() * 13) + 1;
				sex = "boy";
				if ((int) (Math.random() * 2) == 0)
					sex = "girl";
				if (age <= 8) {
					shoe_size = kid_shoe_size[(int) (Math.random()
							* kid_shoe_size.length)];
					clothes_size = kid_clothes_size[(int) (Math.random()
							* kid_clothes_size.length)];
				} else if (sex.equals("boy")) {
					shoe_size = men_shoe_size[(int) (Math.random()
							* men_shoe_size.length)];
					clothes_size = adult_clothes_size[(int) (Math.random()
							* adult_clothes_size.length)];
				} else {
					shoe_size = women_shoe_size[(int) (Math.random()
							* women_shoe_size.length)];
					clothes_size = adult_clothes_size[(int) (Math.random()
							* adult_clothes_size.length)];
				}
				Angel angel = new Angel();
				// The basic angel attributes
				angel.addAttribute(Attribute.ID, id);
				angel.addAttribute(Attribute.SEX, sex);
				angel.addAttribute(Attribute.AGE, age);
				angel.addAttribute(Attribute.SHOE_SIZE, shoe_size);
				angel.addAttribute(Attribute.CLOTHES_SIZE, clothes_size);
				angel.addAttribute(Attribute.SHIRT_SIZE, clothes_size);
				angel.addAttribute(Attribute.PANT_SIZE, clothes_size);

				angel.addAttribute(Attribute.WISH, wishes);
				angel.addAttribute(Attribute.BOOK, book);
				angel.addAttribute(Attribute.SPECIAL, special);

				// Default values when the angels are first created
				angel.addAttribute(Attribute.STATUS, Status.AWAITING);
				angel.addAttribute(Attribute.MISSING, new String[0]);
				angel.addAttribute(Attribute.LOCATION, "Family Resource");

				// If the angel was added reset the inputs and indicate success
				dbController.insertAngel(angel, "angels");
			}
		}
	}

}
