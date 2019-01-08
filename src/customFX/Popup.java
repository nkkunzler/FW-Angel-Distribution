package customFX;

import javafx.scene.control.Alert;
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
	
	public Popup(AlertType alertType, String headerText, String contentText) {
		super(alertType);
		
		super.initStyle(StageStyle.UTILITY);
		
		super.getDialogPane().setStyle("-fx-font-size: " + FONT_SIZE);
		
		super.setHeaderText(headerText);
		super.setContentText(contentText);
		super.showAndWait();
	}
	
	public ButtonType getSelection() {
		return super.resultProperty().get();
	}

}
