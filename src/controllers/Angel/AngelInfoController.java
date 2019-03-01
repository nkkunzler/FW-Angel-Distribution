package controllers.Angel;

import angels.Angel;
import angels.Attribute;
import controllers.Controller;
import displays.AngelDisplays;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AngelInfoController extends Controller {
	
	@FXML
	private Label angelIDLabel;
	
	private Angel angel;
	
	public AngelInfoController() {
		super(AngelDisplays.ANGEL_INFO_DISPLAY);
	}
	
	public void addAngel(Angel angel) {
		this.angel = angel;
		
		angelIDLabel.setText(angel.get(Attribute.ID).toString());
	}
	
	public void backHandler() {
		super.previousDisplay();
	}

}
