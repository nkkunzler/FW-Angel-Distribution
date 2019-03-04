package displays;

import controllers.Controller;

public class Display {

	private final String fxmlFile;
	private final Controller controller;

	public Display(String fxmlFile, Controller controller) {
		this.fxmlFile = fxmlFile;
		this.controller = controller;
	}

	/**
	 * @return String with the displays file location
	 */
	public String getFile() {
		return fxmlFile;
	}
	
	public Controller getController() {
		return controller;
	}
	
	@Override
	public String toString() {
		return fxmlFile;
	}
}
