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

		Row header = sheet.createRow(0);

		Object[] keySet = columnValues.keySet().toArray();

		if (showHeader) {
			for (int i = 0; i < keySet.length; ++i)
				createCell(header, i, keySet[i].toString());
		}
		
		for (int i = 0; i < keySet.length; ++i) {
			int currRow = showHeader ? 1 : 0;
			for (int j = 0; j < columnValues.get(keySet[i]).size(); ++j) {
				Row row = sheet.getRow(j);
				if (row == null)
					row = sheet.createRow(currRow++);
				createCell(row, i, columnValues.get(keySet[i]).get(j));
			}
		}
		
		double val = Math.random() * 5;
		String fileName = "test.out";
	    FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    PrintWriter printWriter = new PrintWriter(fileWriter);
	    printWriter.print("Some String");
	    printWriter.printf("Product name is %s and its price is %d $", "iPhone", 1000);
	    printWriter.close();
	}

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

	public void setColumnWidths(Map<Integer, Integer> columnWidths) {
		for (Integer col : columnWidths.keySet())
			sheet.setColumnWidth(col, columnWidths.get(col));
	}

	public void save(String path, String fileName) {
		String fileLocation = path + fileName + ".xlsx";

		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(fileLocation);
		} catch (FileNotFoundException e1) {
			new Popup(AlertType.ERROR, "Invalid file path provided.");
		}

		if (outputStream == null) {
			new Popup(AlertType.ERROR, "File was unable to be created.");
		}

		try {
			workbook.write(outputStream);
			workbook.close();
		} catch (IOException e) {
			new Popup(AlertType.ERROR,
					"Excel writing error. Excel sheet my be empty or corrupted.");
		}
	}

}
