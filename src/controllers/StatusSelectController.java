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
import customFX.Popup;
import database.DatabaseController;
import display.Displays;
import javafx.fxml.FXML;
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
			Popup popup = new Popup(AlertType.ERROR,
					"Angel '" + angel.get(Attribute.ID)
							+ "' needs to be pulled.\nAlter Status?",
					ButtonType.NO, ButtonType.YES);

			if (popup.getSelection() == ButtonType.NO)
				super.previousDisplay();
		}

		// All items must be off hold before being able to set as complete
		if (status == Status.HOLD)
			completedButton.setDisable(true);

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

		new Popup("The angel status has been altered to:\n'COMPLETE'");
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
		Popup popup = new Popup(AlertType.WARNING,
				"Do you want to pull this angel?",
				ButtonType.YES, ButtonType.NO);

		// If YES on warning message, sets the status of angel to pull
		if (popup.getSelection() == ButtonType.YES) {
			dbController.update(
					(String) angel.get(Attribute.ID),
					Attribute.STATUS.toString(),
					Status.PULL.toString(),
					collection);

			new Popup("The angel status has been altered to:\n'PULL'");
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

		new Popup("The angel status has been altered to:\n'AWAITING'",
				ButtonType.OK);
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
		new Popup("The angel status has been altered to:\n'OUT'",
				ButtonType.OK);
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
