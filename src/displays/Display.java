package displays;

public class Display {
	
	private final String fxmlFile;
	
	public Display(String fxmlFile) {
		this.fxmlFile = fxmlFile;
	}
	
	/**
	 * @return String with the displays file location
	 */
	public String getFile() {
		return fxmlFile;
	}
}
