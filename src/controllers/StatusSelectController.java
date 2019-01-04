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

		String status = ((String) angel.get(Attribute.STATUS)).toUpperCase();

		angelIDLabel.setText(
				angel.get(Attribute.ID) + " - "
						+ status);

		// Show warning if the angel is suppose to be pulled
		if (status.equalsIgnoreCase(Status.PULL.getStatus())) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("PULL BAG");
			alert.setContentText("BAG NEEDS TO BE PULLED");
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
				Attribute.STATUS.getType(),
				Status.COMPLETE.getStatus(),
				collection);
	}

	@FXML
	/**
	 * This method handles the onActionEvent() method for the yellow hold button
	 * on the AngelStatus.fxml display.
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
		alert.setContentText("Do you want to pull this bag?");
		alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
		alert.showAndWait();

		// If YES on warning message, set the status of angel to pull
		if (alert.resultProperty().get() == ButtonType.YES) {
			dbController.update((String) angel.get(Attribute.ID),
					Attribute.STATUS.getType(),
					Status.PULL.getStatus(),
					collection);
		}
	}

	@FXML
	/**
	 * This method handles the onActionEvent() method for the grey awaiting
	 * button on the AngelStatus.fxml display.
	 */
	public void awaitingHandler() {
		dbController.update((String) angel.get(Attribute.ID),
				Attribute.STATUS.getType(),
				Status.AWAITING.getStatus(),
				collection);
	}

	@FXML
	/**
	 * This method handles the onActionEvent() method for the gray out button on
	 * the AngelStatus.fxml display.
	 */
	public void outHandler() {
		dbController.update((String) angel.get(Attribute.ID),
				Attribute.STATUS.getType(),
				Status.OUT.getStatus(),
				collection);
	}

	@FXML
	/**
	 * Handler to switch the display to the display that called this one.
	 */
	public void toPreviousDisplay() {
		super.previousDisplay();
	}
}
