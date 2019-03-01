/*
 * Class to create a custom button. The button is colored corresponding to the
 * gender of the angel, blue for boy and pink for girls. There is also an
 * option to choose whether an indicating strip, which is shown above the
 * button, is to be display. The indicator strip indicates what current status
 * the angel has. These status's can be seen under the STATUS class in Angels
 * folder.
 * 
 * @author Nicholas Kunzler
 */
package customFX;

import angels.Angel;
import angels.Attribute;
import angels.Status;
import javafx.scene.control.Button;
import javafx.scene.text.Font;

public class StatusButton extends Button {

	private static final int FONT_SIZE = 42;

	/**
	 * Creates a new button, with a background color based off angel gender, and
	 * on optional indicator strip if the specified height is > 0;
	 * 
	 * @param angel           The angel in which to base button graphics off
	 * @param fontSize        The fontSize of the button.
	 * @param indicatorHeight The height of the indicator strip. If the height
	 *                        is zero or negative, no indicating strip will be
	 *                        shown.
	 */
	public StatusButton(Angel angel, int fontSize, int indicatorHeight) {
		super.setText((String) angel.get(Attribute.ID));
		super.setFont(new Font(fontSize));

		// Coloring button depending on gender
		String style = "";

		String sex = (String) angel.get(Attribute.GENDER);
		if (sex.equalsIgnoreCase("boy"))
			style = "-fx-background-color: lightblue;";
		else
			style = "-fx-background-color: lightpink;";

		// Setting the bars at top and bottom of button, indicates status
		if (indicatorHeight > 0) {
			String color = "transparent"; // Default color
			Status angelStatus = Status
					.valueOf(angel.get(Attribute.STATUS).toString());
			if (angelStatus == Status.HOLD)
				color = "gold";
			else if (angelStatus == Status.COMPLETE)
				color = "darkseagreen";
			else if (angelStatus == Status.PULL)
				color = "red";
			else if (angelStatus == Status.OUT)
				color = "orange";
			else if (angelStatus == Status.FILLING)
				color = "gray";

			style += "-fx-effect: innershadow(three-pass-box, " + color
					+ ", 0, 0, 0, " + indicatorHeight + ")";
		}
		super.setStyle(style);
	}

	public StatusButton(Angel angel) {
		this(angel, FONT_SIZE, 12);
	}

}
