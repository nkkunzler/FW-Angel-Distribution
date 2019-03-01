package export;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
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

	private final int DEFAULT_COL_WIDTH = 2048;
	private final int MAX_COL_WIDTH = 28000;
	private final int MAX_COL = 14;
	private final int MAX_ROW = 33;

	private Workbook workbook;
	private Sheet sheet;

	public ExcelSheet(String sheetName,
			Map<String, List<String>> columnValues,
			Map<Integer, Integer> columnWidths, boolean showHeader) {
		
		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet(sheetName);
		
		// Setting the widths of the desired columns
		for (Integer col : columnWidths.keySet()) {
			for (int colOffset = 0; colOffset < 12; colOffset += columnValues.size())
				sheet.setColumnWidth(colOffset, columnWidths.get(col));
		}
		
		// If there are headers display on the header row
		writeHeaderRow(columnValues, showHeader);

		writeData(columnValues);
	}

	/**
	 * 
	 * @param columnValues
	 * @param showHeader
	 */
	private void writeHeaderRow(Map<String, List<String>> columnValues,
			boolean showHeader) {

		if (!showHeader)
			return;

		// Creating a new row for the header text
		Row headerRow = sheet.createRow(0);

		Object[] keySet = columnValues.keySet().toArray();
		int columnWidth = 0;

		if (columnWidth > MAX_COL_WIDTH)
			new Popup("To many columns. Some information will be missing");
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
			if (rowNum > currMaxRow) {
				rowNum = 0;
				colNum += colValues.size();
			}
			if (colNum > MAX_COL) {
				rowNum = ((rowNum / MAX_ROW) + 1 ) * MAX_ROW + 1;
				currMaxRow += MAX_ROW;
				colNum = 0;
			}
			Row row = sheet.getRow(rowNum);
			if (row == null)
				row = sheet.createRow(rowNum);
			if (i >= colValues.get(keySet[0]).size())
				return;
			
			for (int col = 0; col < keySet.length; col++)
				createCell(row, colNum + col, colValues.get(keySet[col]).get(i));
			
			rowNum++;
			i++;
		}

//		// int maxRow = colValues.get(keySet[0]).size() / entriesPerRow;
//		int maxCol = colValues.size() * entriesPerRow;
//		int index = 0;
//		int i = 0;
//
//		int entryCol = 1;
//		int rowNum = 1;
//		int maxRow = MAX_ROW;
//		int entry = 0;
//
//		while (true) {
//			Row row = sheet.getRow(rowNum);
//			if (row == null)
//				row = sheet.createRow(rowNum);
//			for (int col = entryCol - 1; col < entryCol * colValues.size(); ++col) {
//				entry++;
//				if (entry > colValues.get(keySet[0]).size())
//					return;
//				if (index >= colValues.get(keySet[i]).size())
//					return;
//				createCell(row, col, colValues.get(keySet[i])
//						.get(index));
//			}
//			rowNum++;
//			if (rowNum > MAX_ROW) {
//				rowNum = maxRow - MAX_ROW;
//				entryCol++;
//			}
//			if (entryCol > entriesPerRow) {
//				maxRow += MAX_ROW;
//				rowNum = MAX_ROW;
//				entryCol = 0;
//			}
//		}
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

		BorderStyle borderStyle = BorderStyle.MEDIUM;
		style.setBorderBottom(borderStyle);
		style.setBorderLeft(borderStyle);
		style.setBorderRight(borderStyle);
		style.setBorderTop(borderStyle);

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
					"Excel writing error. Excel sheet my be already open or corrupted.");
		}
	}

}