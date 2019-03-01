package export;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import customFX.Popup;
import javafx.scene.control.Alert.AlertType;

public class ExcelSheet {

	// Maximum width for a standard letter paper in landscape mode
	private final int LANDSCAPE_WIDTH = 28672;
	private final int MAX_ROW = 34;
	
	private int maxCol = 0;

	private Workbook workbook;
	private Sheet sheet;

	public ExcelSheet(String sheetName,
			Map<String, List<String>> columnValues,
			Map<Integer, Integer> columnWidths) {

		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet(sheetName);
		
		if (columnWidths != null)
			setWidths(columnWidths, columnValues.size());

		writeData(columnValues);
	}
	
	private void setWidths(Map<Integer, Integer> columnWidths, int values) {
		// Finds max number of columns that can fit on a landscape letter page
		int currWidth = 0;
		for (Integer colWidths : columnWidths.values())
			currWidth += colWidths;
		maxCol = LANDSCAPE_WIDTH / currWidth * values;
		// Setting the widths of the columns
		for (Integer column : columnWidths.keySet()) {
			for (int offset = 0; offset <= maxCol; offset += values) {
				sheet.setColumnWidth(column + offset, columnWidths.get(column));
			}
		}
	}

	/**
	 * 
	 * @param colValues
	 * @param showheader
	 */
	private void writeData(Map<String, List<String>> colValues) {

		Object[] keySet = colValues.keySet().toArray();

		int currMaxRow = MAX_ROW;
		int rowNum = 0;
		int colNum = 0;
		int i = 0;

		while (true) {
			if (rowNum >= currMaxRow) {
				rowNum -= MAX_ROW;
				colNum += colValues.size();
			}
			if (colNum > maxCol) {
				rowNum = currMaxRow;
				currMaxRow += MAX_ROW;
				colNum = 0;
			}
			Row row = sheet.getRow(rowNum);
			if (row == null)
				row = sheet.createRow(rowNum);
			if (i >= colValues.get(keySet[0]).size())
				return;

			for (int col = 0; col < keySet.length; col++) {
				createCell(row, colNum + col,
						colValues.get(keySet[col]).get(i));
			}

			rowNum++;
			i++;
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

		BorderStyle borderStyle = BorderStyle.THIN;
		style.setBorderBottom(borderStyle);
		style.setBorderLeft(borderStyle);
		style.setBorderRight(borderStyle);
		style.setBorderTop(borderStyle);
		
		style.setAlignment(HorizontalAlignment.CENTER);
		
		Cell cell = row.createCell(column);
		cell.setCellStyle(style);
		cell.setCellValue(cellValue);
	}

	/**
	 * Using the desired path and file name, this method saves the current state
	 * of the ExcelSheet as an .xlsx, or Excel, file.
	 * 
	 * @param path     The desired file path for the file
	 * @param fileName The name of the file to save.
	 * @throws IOException
	 */
	public void save(String path, String fileName) {
		String fileLocation = path + fileName + ".xlsx";

		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(fileLocation);
		} catch (FileNotFoundException e1) {
			new Popup(AlertType.ERROR,
					"Invalid file path provided.\nFile was unable to be created.");
			try {
				workbook.close();
			} catch (IOException e) {
				new Popup(AlertType.ERROR,
						"Unable to close Excel File. Please relaunch application.");
			}
		}

		// Saving the workbook to the specified file location
		try {
			workbook.write(outputStream);
		} catch (IOException e) {
			new Popup(AlertType.ERROR,
					"Excel writing error. Excel sheet my be already open or corrupted.");
		} finally {
			try {
				workbook.close();
			} catch (IOException e) {
				new Popup(AlertType.ERROR,
						"Unable to close Excel File. Please relaunch application.");
			}
		}
	}

}
