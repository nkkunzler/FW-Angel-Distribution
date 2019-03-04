package controllers.Angel;

import java.util.List;

import angels.Angel;
import angels.Attribute;
import controllers.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class AngelInfoController extends Controller {

	@FXML
	private Label angelIDLabel, ageLabel, genderLabel, statusLabel, locationLabel,
			shoeLabel, shirtLabel, pantLabel;
	@FXML
	private VBox wishVBox, bookVBox, specialVBox, missingVBox;

	public AngelInfoController() {
	}

	public void addAngel(Angel angel) {
		angelIDLabel.setText(angel.get(Attribute.ID).toString());
		ageLabel.setText(angel.get(Attribute.AGE).toString());
		genderLabel.setText(angel.get(Attribute.GENDER).toString());
		statusLabel.setText(angel.get(Attribute.STATUS).toString());
		locationLabel.setText(angel.get(Attribute.LOCATION).toString());
		shoeLabel.setText(angel.get(Attribute.SHOE_SIZE).toString());
		shirtLabel.setText(angel.get(Attribute.SHIRT_SIZE).toString());
		pantLabel.setText(angel.get(Attribute.PANT_SIZE).toString());
		
		fillVBox(angel.get(Attribute.WISH), wishVBox);
		fillVBox(angel.get(Attribute.BOOK), bookVBox);
		fillVBox(angel.get(Attribute.SPECIAL), specialVBox);
		fillVBox(angel.get(Attribute.MISSING), missingVBox);
	}
	
	@SuppressWarnings("unchecked")
	private void fillVBox(Object listData, VBox vbox) {
		for (String value : (List<String>) listData) {
			Label label = new Label(value);
			label.setFont(new Font(20));
			vbox.getChildren().add(label);
		}
	}

	public void backHandler() {
		System.out.println("using previousDisplay()");
		super.previousDisplay();
	}

}
