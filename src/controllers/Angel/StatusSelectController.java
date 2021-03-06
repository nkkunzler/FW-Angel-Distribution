/**
 * This class is the controller to connect between the AngelStatus.fxml file 
 * and the JavaFX Node objects within the file. The purpose for this class is 
 * to allow for selecting a new status for a desired angel.
 * 
 * @author Nicholas Kunzler
 */
package controllers.Angel;

import java.util.Optional;

import angels.Angel;
import angels.Attribute;
import angels.Status;
import controllers.Controller;
import customFX.Popup;
import database.DBCollection;
import database.DatabaseController;
import displays.AngelDisplays;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;
import javafx.util.Pair;

public class StatusSelectController extends Controller {

	@FXML
	private Label angelIDLabel;
	@FXML
	private Button completedButton, holdButton, pullButton, awaitingButton,
			outButton;

	private Angel angel;

	private DatabaseController dbController;

	/**
	 * Constructor for the controller used to change the status of an angel. The
	 * types of status's can be found within the angels.Status package.
	 * 
	 * @param controller The database controller used to connect to the
	 *                   database.
	 * @param            DBCollections.ANGELS The DBCollections.ANGELS in which
	 *                   an angel will eventually be added to.
	 */
	public StatusSelectController(DatabaseController dbController) {
		this.dbController = dbController;
	}

	/**
	 * This method should be called immediately after the AngelStatus.fxml
	 * display is shown. This method allows for the angel to be altered based
	 * off the status selected.
	 * 
	 * This method is called in the AngelSelectionController.java class when a
	 * button, which is generated when wanting to select an angel, is selected.
	 * 
	 * @param angelToChange
	 */
	public void setAngel(Angel angelToChange) {
		angel = angelToChange;

		// Converts an Angel's string status to a STATUS object
		Status status = Status.valueOf(angel.get(Attribute.STATUS).toString());

		// Label below the scene title
		angelIDLabel.setText(angel.get(Attribute.ID) + " - " + status);

		// Show warning if the angel is suppose to be pulled
		if (status == Status.PULL) {
			Popup popup = new Popup(AlertType.ERROR,
					"Angel '" + angel.get(Attribute.ID)
							+ "' needs to be pulled.\nAlter Status?",
					ButtonType.NO, ButtonType.YES);

			// If the user does not want to alter the status, go back
			if (popup.getSelection() == ButtonType.NO)
				super.switchScene(AngelDisplays.ANGEL_SELECTION);
		}

		// All items must be off hold before being able to set as complete
		if (status == Status.HOLD)
			completedButton.setDisable(true);

	}

	@FXML
	/**
	 * This method handles the onActionEvent() method for the green complete
	 * button on the AngelStatus.fxml display.
	 * 
	 * TODO: Prompt for additional information before going to complete
	 */
	public void completeHandler() {
		Dialog<Pair<Boolean, Boolean>> dialog = new Dialog<>();
		dialog.getDialogPane().setStyle("-fx-font-size: " + 18);
		dialog.initStyle(StageStyle.UTILITY);
		dialog.setHeaderText("Please provide additional information");
		dialog.setTitle("Complete Additional Information");
		dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
		dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

		VBox vbox = new VBox();
		vbox.setSpacing(10);

		CheckBox shoeCB = new CheckBox(
				"Shoes Originally Included in Bag?\n(Check if no shoes needed)");
		CheckBox clothesCB = new CheckBox(
				"Clothes Originally Included in Bag?\n(Check if no clothes needed)");

		vbox.getChildren().addAll(shoeCB, clothesCB);
		dialog.getDialogPane().setContent(vbox);
		dialog.setResultConverter(selectedBtn -> {
			if (selectedBtn == ButtonType.OK)
				return new Pair<>(shoeCB.isSelected(), shoeCB.isSelected());
			return null;
		});
		Optional<Pair<Boolean, Boolean>> result = dialog.showAndWait();
		result.ifPresent(values -> {
			// Updating the status of the angel to complete
			dbController.update(angel.get(Attribute.ID).toString(),
					Attribute.STATUS.toString(),
					Status.COMPLETE.toString(),
					DBCollection.ANGELS);

			// Updating status to be 'on site', meaning main location
			dbController.update(angel.get(Attribute.ID).toString(),
					Attribute.LOCATION.toString(), "on_site",
					DBCollection.ANGELS);

			String todos = "TODO:";
			if (!shoeCB.isSelected())
				todos += " DECREASE SHOE INVENTORY BY 1\n";
			if (!clothesCB.isSelected())
				todos += " DECREASE CLOTHES INVENTORY BY 1\n";
			new Popup("The angel status has been updated.\n" + todos);
			super.switchScene(AngelDisplays.ANGEL_SELECTION);
		});
	}

	@FXML
	/**
	 * This method handles the onActionEvent() method for the yellow hold button
	 * on the AngelStatus.fxml display.
	 * 
	 * When the Hold Button is selected, the scene will change to allow for the
	 * user to select which items are causing the angel to go onto display.
	 */
	public void holdHandler() {
		super.switchScene(AngelDisplays.HOLD_DISPLAY);
		HoldController controller = (HoldController) AngelDisplays.HOLD_DISPLAY
				.getController();
		controller.addAngel(angel);
	}

	@FXML
	/**
	 * This method handles the onActionEvent() method for the pink pull button
	 * on the AngelStatus.fxml display.
	 */
	public void pullHandler() {
		// Ask the user if they want to pull the angel
		Popup popup = new Popup(AlertType.WARNING,
				"Do you want to pull this angel?",
				ButtonType.YES, ButtonType.NO);

		// If YES on warning message, sets the status of angel to pull
		if (popup.getSelection() == ButtonType.YES) {
			dbController.update(
					(String) angel.get(Attribute.ID),
					Attribute.STATUS.toString(),
					Status.PULL.toString(),
					DBCollection.ANGELS);

			new Popup("The angel status has been altered to:\n'PULL'");
			super.switchScene(AngelDisplays.ANGEL_SELECTION);
		}
	}

	@FXML
	/**
	 * This method handles the onActionEvent() method for the grey awaiting
	 * button on the AngelStatus.fxml display.
	 */
	public void fillingHandler() {
		// Change status to "Filling"
		dbController.update((String) angel.get(Attribute.ID),
				Attribute.STATUS.toString(),
				Status.FILLING.toString(),
				DBCollection.ANGELS);

		// Updating status to be 'on site', meaning main location
		dbController.update(angel.get(Attribute.ID).toString(),
				Attribute.LOCATION.toString(), "on_site", DBCollection.ANGELS);

		new Popup("The angel status has been altered to 'Filling'",
				ButtonType.OK);
		super.switchScene(AngelDisplays.ANGEL_SELECTION);
	}

	@FXML
	/**
	 * Switching the display to a display that shows all information in regards
	 * to the angel.
	 */
	public void infoHandler() {
		super.switchScenePreserve(AngelDisplays.ANGEL_INFO_DISPLAY);
		AngelInfoController controller = (AngelInfoController) AngelDisplays.ANGEL_INFO_DISPLAY
				.getController();
		controller.addAngel(angel);
	}

	@FXML
	/**
	 * When the OUT button on the AngelStatus.fxml file is selected a dialog box
	 * will appear to prompt for additional information. The information
	 * gathered is to get the name of the company/person requesting to take the
	 * angel. The purpose of this to allow for lookup of the angel by
	 * company/personal name.
	 * 
	 * This method handles the onActionEvent() method for the gray out button on
	 * the AngelStatus.fxml display.
	 */
	public void outHandler() {
		TextInputDialog dialog = new TextInputDialog("Requestee");
		dialog.getDialogPane().setStyle("-fx-font-size: " + 18);
		dialog.initStyle(StageStyle.UTILITY);
		dialog.setHeaderText("Please provide requestee name");
		dialog.setTitle("Additional Information");
		dialog.setContentText("Enter Requestee: ");

		Optional<String> result = dialog.showAndWait();
		result.ifPresent(requestee -> {
			// Only update if the user pressed okay
			if (dialog.resultProperty().get() != null) {
				System.out.println("updating");
				// Update the status to out
				dbController.update(angel.get(Attribute.ID).toString(),
						Attribute.STATUS, Status.OUT, DBCollection.ANGELS);
				// Update the location to be the requester
				dbController.update(angel.get(Attribute.ID).toString(),
						Attribute.LOCATION, requestee, DBCollection.ANGELS);
				new Popup("Angel has been updated succesfully", ButtonType.OK);
				super.switchScene(AngelDisplays.ANGEL_SELECTION);
			}
		});

	}

	@FXML
	/**
	 * Handler to switch the display to the display that called this one.
	 */
	public void toPreviousDisplay() {
		super.switchScene(AngelDisplays.ANGEL_SELECTION);
	}
}
