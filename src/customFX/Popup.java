/*
 * The purpose of this class is to provide a simple to use and similar looking
 * alert dialog messages. 
 * 
 * @author Nicholas Kunzler
 */

package customFX;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.StageStyle;

public class Popup extends Alert {

	private static final int FONT_SIZE = 18;

	public Popup(AlertType alertType, String headerText, String contentText,
			ButtonType... buttons) {
		super(alertType);
		
		super.initStyle(StageStyle.UTILITY);

		// If custom buttons, then set them for the buttons on alert
		if (buttons.length != 0)
			super.getButtonTypes().setAll(buttons);

		super.getDialogPane().setStyle("-fx-font-size: " + FONT_SIZE);

		// Setting text of alert to text passed in
		super.setHeaderText(headerText);
		super.setContentText(contentText);
		super.showAndWait();
	}

	public Popup(AlertType alertType, String contentText,
			ButtonType... buttons) {
		this(alertType, "", contentText, buttons);
	}

	public Popup(String headerText, String contentText, ButtonType... buttons) {
		this(AlertType.INFORMATION, headerText, contentText, buttons);
	}

	public Popup(String contentText, ButtonType... buttons) {
		this(AlertType.INFORMATION, "", contentText, buttons);
	}

	public Popup(AlertType alertType, String headerText, String contentText) {
		this(alertType, headerText, contentText, new ButtonType[0]);
	}

	public Popup(AlertType alertType, String contentText) {
		this(alertType, "", contentText);
	}

	public Popup(String headerText, String contentText) {
		this(AlertType.INFORMATION, headerText, contentText);
	}

	public Popup(String contentText) {
		this(AlertType.INFORMATION, "", contentText, ButtonType.OK);
	}

	/**
	 * @return The ButtonType, a string, indicating which button on the Popup
	 *         was pressed.
	 */
	public ButtonType getSelection() {
		return super.resultProperty().get();
	}

}
