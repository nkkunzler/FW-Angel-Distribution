package main;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import customFX.Popup;
import javafx.scene.control.Alert.AlertType;

public class ExcelSheet {

	private final int MAX_COL = 47;
	private final int MAX_ROW = 20;

	private Workbook workbook;
	private Sheet sheet;

	public ExcelSheet(String sheetName,
			Map<String, List<String>> columnValues, boolean showHeader) {
		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet(sheetName);

		// Creating a new row for the header text
		Row header = sheet.createRow(0);

		Object[] keySet = columnValues.keySet().toArray();

		// If there are headers display on the header row
		if (showHeader) {
			for (int i = 0; i < keySet.length; ++i)
				// Creating a new cell to store the headers in each cell
				createCell(header, i, keySet[i].toString());
		}

		// Need to limit the number of columns, to be print friendly
		for (int i = 0; i < keySet.length; ++i) {
			int currRow = showHeader ? 1 : 0;
			for (int j = 0; j < columnValues.get(keySet[i]).size(); ++j) {
				Row row = sheet.getRow(j);
				if (row == null)
					row = sheet.createRow(currRow++);
				createCell(row, i, columnValues.get(keySet[i]).get(j));
			}
		}
	}

	/**
	 * Creates a new Excel cell at the specified row and column. This Excel cell
	 * is a standard Excel set width with thick borders.
	 * 
	 * @param row       Integer representing the row at which to create the cell
	 * @param column    Integer representing the column at which to create the
	 *                  cell
	 * @param cellValue The string value that will be placed within the cell
	 */
	private void createCell(Row row, int column, String cellValue) {
		CellStyle style = workbook.createCellStyle();
		BorderStyle borderStyle = BorderStyle.THICK;
		style.setBorderBottom(borderStyle);
		style.setBorderLeft(borderStyle);
		style.setBorderRight(borderStyle);
		style.setBorderTop(borderStyle);

		Cell cell = row.createCell(column);
		cell.setCellStyle(style);
		cell.setCellValue(cellValue);
	}

	/**
	 * Using a map which resizes the column width,specified by the key of map,
	 * to the desired width, the value.
	 * 
	 * @param columnWidths Map<Integer, Integer> that maps the column to the
	 *                     desired width
	 */
	public void setColumnWidths(Map<Integer, Integer> columnWidths) {
		for (Integer col : columnWidths.keySet())
			sheet.setColumnWidth(col, columnWidths.get(col));
	}

	/**
	 * Using the desired path and file name, this method saves the current state
	 * of the ExcelSheet as an .xlsx, or Excel, file.
	 * 
	 * @param path     The desired file path for the file
	 * @param fileName The name of the file to save.
	 */
	public void save(String path, String fileName) {
		String fileLocation = path + fileName + ".xlsx";

		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(fileLocation);
		} catch (FileNotFoundException e1) {
			new Popup(AlertType.ERROR, "Invalid file path provided.");
		}

		// Checking to make sure the file location is valid
		if (outputStream == null) {
			new Popup(AlertType.ERROR, "File was unable to be created.");
		}

		// Saving the workbook to the specified file location
		try {
			workbook.write(outputStream);
			workbook.close();
		} catch (IOException e) {
			new Popup(AlertType.ERROR,
					"Excel writing error. Excel sheet my be empty or corrupted.");
		}
	}

}
