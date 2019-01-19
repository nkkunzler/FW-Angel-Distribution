package controllers;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import angels.Angel;
import angels.Attribute;
import angels.Status;
import database.DatabaseController;
import display.Displays;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import main.ExcelSheet;

public class ExportController extends Controller {

	private final int FONT_SIZE = 15;
	private final String VAR_NAM = "doc";
	private final String FILT_STR = "FILTER " + VAR_NAM + ".";

	@FXML
	private TextField filePath, fileName;
	@FXML
	private ComboBox<String> exportComboBox;
	@FXML
	private VBox attributesContainer, filterContainer;

	private DatabaseController dbController;

	private Map<Attribute, String> filters = new HashMap<>();

	public ExportController(DatabaseController dbController) {
		super(Displays.EXPORT_DISPLAY);

		this.dbController = dbController;

		excelTest();
	}

	private void excelTest() {
		Map<String, List<String>> values = new LinkedHashMap<>();
		
		List<String> idValues = new ArrayList<>();
		idValues.add("1A");
		idValues.add("1B");
		idValues.add("2A");
		values.put(Attribute.ID.toString(), idValues);

		List<String> genderValues = new ArrayList<>();
		genderValues.add("B");
		genderValues.add("G");
		genderValues.add("B");
		values.put(Attribute.GENDER.toString(), genderValues);

		Map<Integer, Integer> columns = new HashMap<>();
		columns.put(0, 2000);
		columns.put(1, 2000);
		columns.put(2, 2000);
		columns.put(3, 2000);

		ExcelSheet sheet = new ExcelSheet("EXPORTS", values, false);
		sheet.setColumnWidths(columns);
		sheet.save("C:/Users/nkkun/Desktop/Angel Exports/", "test");
	}

	private HBox filterInput() {
		HBox hbox = new HBox();

		System.out.println("Created filter Input");
		ComboBox<String> combo = createComboBox(Attribute.values());
		combo.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(
					ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				if (hbox.getChildren().size() > 2)
					hbox.getChildren().remove(1); // The filter from previous
													// selection
				switch (Attribute.valueOf(combo.getValue())) {
				case GENDER:
					hbox.getChildren().add(1, sexFilter());
					break;
				case AGE:
					hbox.getChildren().add(1, ageFilter());
					break;
				case STATUS:
					hbox.getChildren().add(1, statusFilter());
					break;
				default:
					hbox.getChildren().add(1,
							defaultFilter(Attribute.valueOf(combo.getValue())));
					break;
				}
				System.out.println(filters);
			}
		});

		// Removes the attribute when pressed
		Button removeBtn = new Button("-");
		removeBtn.setFont(new Font(FONT_SIZE));
		removeBtn.setOnAction(e -> filterContainer.getChildren().remove(hbox));

		hbox.getChildren().add(combo);
		hbox.getChildren().add(removeBtn);

		return hbox;
	}

	private ComboBox<String> sexFilter() {
		ComboBox<String> combo = createComboBox(new String[] { "BOY", "GIRL" });
		combo.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(
					ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				String filter = FILT_STR + Attribute.GENDER + " == '"
						+ combo.getValue().toLowerCase() + "'";
				filters.put(Attribute.GENDER, filter);
			}
		});
		return combo;
	}

	private HBox ageFilter() {
		HBox box = new HBox();

		TextField fromInput = new TextField();
		fromInput.setFont(new Font(FONT_SIZE));
		fromInput.setPrefWidth(50);
		fromInput.setPromptText("FROM");

		fromInput.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(
					ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				filters.put(Attribute.AGE,
						formatAgeFilter(fromInput.getText(), null));
				System.out.println(filters.get(Attribute.AGE));
			}
		});

		TextField toInput = new TextField();
		toInput.setFont(new Font(FONT_SIZE));
		toInput.setPrefWidth(50);
		toInput.setPromptText("TO");

		toInput.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(
					ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				filters.put(Attribute.AGE,
						formatAgeFilter(null, toInput.getText()));
				System.out.println(filters.get(Attribute.AGE));
			}
		});

		Label space = new Label(" _ ");

		box.getChildren().addAll(fromInput, space, toInput);

		return box;
	}

	private String formatAgeFilter(String from, String to) {
		String filterStart = FILT_STR + "AGE ";
		if (filters.get(Attribute.AGE) == null) {
			if (from != null && !from.equals("")) {
				return filterStart + ">= " + from;
			} else if (to != null && !to.equals("")) {
				return filterStart + "<= " + to;
			} else {
				return "";
			}
		}

		String ranges = filters.get(Attribute.AGE).replace("FILTER ", "");
		ranges = ranges.replace(VAR_NAM + ".AGE ", "");

		int andIdx = ranges.indexOf("&&");
		String currFrom = "";
		String currTo = "";

		if (andIdx != -1) {
			currFrom = ranges.substring(3, andIdx).trim();
			currTo = ranges.substring(andIdx + 4).trim();
		} else if (ranges.charAt(0) == '>') {
			currFrom = ranges.substring(3).trim();
		} else if (ranges.charAt(0) == '<') {
			currTo = ranges.substring(3).trim();
		}

		if (from != null)
			currFrom = from;
		else if (to != null)
			currTo = to;

		if (currFrom.equals("") && !currTo.equals(""))
			return "FILTER doc.AGE <= " + currTo;
		else if (currTo.equals("") && !currFrom.equals(""))
			return "FILTER doc.AGE >= " + currFrom;
		else if (!currFrom.equals("") && !currTo.equals(""))
			return "FILTER doc.AGE >= " + currFrom + " && doc.AGE <= " + currTo;
		return "";

	}

	private ComboBox<String> statusFilter() {
		ComboBox<String> combo = createComboBox(Status.values());
		combo.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(
					ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				String filter = FILT_STR + Attribute.STATUS + " == '"
						+ combo.getValue() + "'";
				filters.put(Attribute.STATUS, filter);
			}
		});
		return combo;
	}

	private TextField defaultFilter(Attribute attribute) {
		TextField field = new TextField();
		field.setFont(new Font(FONT_SIZE));
		field.setPromptText("Filter Value");
		field.setPrefWidth(100);

		field.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(
					ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				String filter = FILT_STR + attribute.toString() + " == '"
						+ field.getText() + "'";
				filters.put(attribute, filter);
			}
		});

		return field;
	}

	private HBox attributeInput() {
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);

		// Removes the attribute when pressed
		Button removeBtn = new Button("-");
		removeBtn.setFont(new Font(FONT_SIZE));
		removeBtn.setOnAction(e -> {
			attributesContainer.getChildren().remove(hbox);
		});

		hbox.getChildren().add(createComboBox(Attribute.values()));
		hbox.getChildren().add(removeBtn);

		return hbox;
	}

	private ComboBox<String> createComboBox(Object[] values) {
		ComboBox<String> cb = new ComboBox<>();
		cb.setButtonCell(new ListCell<String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				setStyle("-fx-font-size:" + FONT_SIZE);
				if (!empty || item != null) {
					super.setText(item.toUpperCase());
				}
			}
		});

		// Add all the attributes for an angel to the combo box
		for (Object obj : values) {
			cb.getItems().add(obj.toString());
		}

		return cb;
	}

	/**
	 * Responsible for creating a new ComboBox for allowing a new attribute to
	 * be added to the export. If a previous attribute ComboBox, which is in
	 * attributesContainer HBox, does not have a selected value, then nothing
	 * happens.
	 * 
	 * This method is called when the button with the text '+' under the
	 * Attribute title is pressed.
	 */
	@FXML
	public void addAttributeHandler() {
		List<Node> inputs = attributesContainer.getChildren();

		if (inputs.size() == 0) {
			inputs.add(attributeInput()); // Adds ComboBox of angel attributes
			return;
		}

		HBox inputContainer = ((HBox) inputs.get(inputs.size() - 1));

		@SuppressWarnings("unchecked")
		ComboBox<String> box = (ComboBox<String>) inputContainer
				.getChildren().get(0);

		if (box.getValue() != null) // If last input is not empty add input
			inputs.add(attributeInput());
	}

	/**
	 * Responsible for creating a new ComboBox for allowing a new filter for a
	 * specified attribute. to be added to the export. If a previous filter
	 * ComboBox, which is in filterContainer HBox, does not have a selected
	 * value or filter value, then nothing happens.
	 * 
	 * This method is called when the button with the text '+' under the Filters
	 * title is pressed.
	 */
	@FXML
	public void addFilterHandler() {
		List<Node> inputs = filterContainer.getChildren();
		if (inputs.size() == 0) {
			inputs.add(filterInput());
			return;
		}

		HBox inputContainer = ((HBox) inputs.get(inputs.size() - 1));

		@SuppressWarnings("unchecked")
		ComboBox<String> box = (ComboBox<String>) inputContainer
				.getChildren().get(0);

		if (box.getValue() != null) // If last input is not empty add input
			filterContainer.getChildren().add(filterInput());
	}

	/**
	 * This method is responsible for displaying the file explorer when the
	 * 'Browse' button is clicked for the file location of the export.
	 */
	@FXML
	public void browseButtonHandler() {
		DirectoryChooser dc = new DirectoryChooser();
		File file = dc.showDialog(super.getStage());

		try {
			filePath.setText(file.getAbsolutePath());
		} catch (NullPointerException npe) {
			System.err.println("Invalid File Location");
		}
	}

	@FXML
	public void exportButtonHandler() {
		if (filePath.getText().equals("")
				|| fileName.getText().equals("")) {
			System.err.println("Cannot export data. ");
			return;
		}

		StringBuilder builder = new StringBuilder();
		String attributeNames = "";
		for (Node node : attributesContainer.getChildren()) {
			ComboBox<String> box = (ComboBox<String>) ((HBox) node)
					.getChildren().get(0);
			attributeNames += box.getValue().toUpperCase() + ",";
		}

		builder.append(attributeNames + "\n"); // Column headers for columns

		String query = "FOR doc in angels\n ";
		for (Attribute attr : filters.keySet()) {
			query += filters.get(attr) + "\n ";
		}
		query += "RETURN doc";

		System.out.println(query);

		Task<List<Angel>> result = dbController.query(query);
		result.setOnSucceeded(e -> {
			try {
				PrintWriter pw = null;
				try {
					pw = new PrintWriter(new File(
							filePath.getText() + "\\" + fileName.getText()
									+ ".csv"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				for (Angel angel : result.get()) {
					String values = "";
					for (Node node : attributesContainer.getChildren()) {
						ComboBox<String> box = (ComboBox<String>) ((HBox) node)
								.getChildren().get(0);
						String strAtt = box.getValue().replace(" ", "_")
								.toUpperCase();
						Attribute attr = Attribute.valueOf(strAtt);
						System.out.println("VALUE: " + angel.get(attr));
						String value = formatString((String) angel.get(attr));
						values += "=" + "\"" + value + "\",";
					}
					values += "\n";
					builder.append(values);
					values = "";
				}
				pw.write(builder.toString());
				pw.close();
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setContentText("EXPORT SUCCESSFUL");
				alert.showAndWait();
			} catch (InterruptedException | ExecutionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
	}

	private String formatString(String string) {
		string = string.replace("[", "");
		string = string.replace("]", "");
		string = string.replace(",", " /");
		return string;
	}

	@FXML
	/**
	 * Switches back to the scene that made this scene visible. Most likely the
	 * Main Menu scene.
	 */
	public void exitButtonHandler() {
		super.previousDisplay();
	}

}
