/**
 * This class is the controller to connect between the AngelStatus.fxml file 
 * and the JavaFX Node objects within the file. The purpose for this class is 
 * to allow for selecting a new status for a desired angel.
 * 
 * @author Nicholas Kunzler
 */
package controllers;

import angels.Angel;
import angels.Attribute;
import angels.Status;
import database.DatabaseController;
import display.Displays;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

public class StatusSelectController extends Controller {

	@FXML
	private Label angelIDLabel;
	@FXML
	private Button completedButton, holdButton, pullButton, awaitingButton,
			outButton;

	private Angel angel;

	private DatabaseController dbController;
	private String collection;

	/**
	 * Constructor for the controller used to change the status of an angel. The
	 * types of status's can be found within the angels.Status package.
	 * 
	 * @param controller The database controller used to connect to the
	 *                   database.
	 * @param collection The collection in which an angel will eventually be
	 *                   added to.
	 */
	public StatusSelectController(DatabaseController dbController,
			String collection) {
		super(Displays.ANGEL_STATUS);

		this.dbController = dbController;
		this.collection = collection;
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

		Status status = Status.valueOf(angel.get(Attribute.STATUS).toString());

		angelIDLabel.setText(angel.get(Attribute.ID) + " - " + status);

		// Show warning if the angel is suppose to be pulled
		if (status == Status.PULL) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("PULL ANGEL");
			alert.setContentText("Angel needs to be pulled.");
			alert.showAndWait();
		}
	}

	@FXML
	/**
	 * This method handles the onActionEvent() method for the green complete
	 * button on the AngelStatus.fxml display.
	 */
	public void completeHandler() {
		dbController.update((String) angel.get(Attribute.ID),
				Attribute.STATUS.toString(),
				Status.COMPLETE.toString(),
				collection);
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("ANGEL STATUS CHANGED - COMPLETE");
		alert.setContentText("The angel status has been changed to 'Complete'");
		alert.showAndWait();
		super.previousDisplay();
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
		super.switchScene(Displays.HOLD_DISPLAY);
		HoldController controller = (HoldController) super.getController(
				Displays.HOLD_DISPLAY);
		controller.addAngel(angel);
	}

	@FXML
	/**
	 * This method handles the onActionEvent() method for the pink pull button
	 * on the AngelStatus.fxml display.
	 */
	public void pullHandler() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setContentText("Do you want to pull this angel?");
		alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
		alert.showAndWait();

		// If YES on warning message, sets the status of angel to pull
		if (alert.resultProperty().get() == ButtonType.YES) {
			dbController.update((String) angel.get(Attribute.ID),
					Attribute.STATUS.toString(),
					Status.PULL.toString(),
					collection);
			super.previousDisplay();
		}
	}

	@FXML
	/**
	 * This method handles the onActionEvent() method for the grey awaiting
	 * button on the AngelStatus.fxml display.
	 */
	public void awaitingHandler() {
		dbController.update((String) angel.get(Attribute.ID),
				Attribute.STATUS.toString(),
				Status.AWAITING.toString(),
				collection);
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("ANGEL STATUS CHANGED - AWAITING");
		alert.setContentText("The angel status has been changed to 'Awaiting'");
		alert.showAndWait();
		super.previousDisplay();
	}

	@FXML
	/**
	 * This method handles the onActionEvent() method for the gray out button on
	 * the AngelStatus.fxml display.
	 */
	public void outHandler() {
		dbController.update((String) angel.get(Attribute.ID),
				Attribute.STATUS.toString(),
				Status.OUT.toString(),
				collection);

		// TODO: Change to display dealing with handing out angels
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("ANGEL STATUS CHANGED - OUT");
		alert.setContentText("The angel status has been changed to 'Out'");
		alert.showAndWait();
		super.previousDisplay();
	}

	@FXML
	/**
	 * Handler to switch the display to the display that called this one.
	 */
	public void toPreviousDisplay() {
		super.previousDisplay();
	}
}
