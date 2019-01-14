package customFX;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.StageStyle;

public class Popup extends Alert{
	
	private static final int FONT_SIZE = 18;
	
	public Popup(AlertType alertType, String headerText, String contentText, ButtonType... buttons) {
		super(alertType);
		
		super.initStyle(StageStyle.UTILITY);
		
		if (buttons.length != 0)
			super.getButtonTypes().setAll(buttons);
		
		super.getDialogPane().setStyle("-fx-font-size: " + FONT_SIZE);
		
		super.setHeaderText(headerText);
		super.setContentText(contentText);
		super.showAndWait();
	}
	
	public Popup(AlertType alertType, String contentText, ButtonType... buttons) {
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
	
	public ButtonType getSelection() {
		return super.resultProperty().get();
	}

}
