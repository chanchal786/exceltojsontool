package com.test.tool;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/*
 * @Chanchal CSK
 * 
 * 
 */

//https://www.journaldev.com/2315/java-json-example
//http://www.appsdeveloperblog.com/java-into-json-json-into-java-all-possible-examples/
public class ReadExcelDataWithDynamicColumn {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.println("Enter file path e.g  .xlsx file");
		String excelFilePath = input.nextLine();
		CopyExcelSheet ces = new CopyExcelSheet();
		// String excelFilePath = "C:/Users/MahiWay/Desktop/Tool/Test1.xlsx";
		String copySheetName = "Sheet1";
		List<List<String>> selectedRowDataList = ces.getExcelData(excelFilePath, copySheetName);
		ces.createExcelSheetWithData(excelFilePath, selectedRowDataList);

		creteJSONFileFromExcel(excelFilePath);
	}

	private static String dataToJSON(List<List<String>> dataTable)
			throws JsonGenerationException, JsonMappingException, IOException {
		JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
		jsonBuilder.add("dist_refid", "");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		jsonBuilder.add("date_updated", dtf.format(now));

		System.out.println(dtf.format(now));
		String ret = "";
		String indented = "";
		if (dataTable != null) {
			int rowCount = dataTable.size();
			if (rowCount > 1) {
				List<String> headerRow = dataTable.get(0);
				int columnCount = headerRow.size();
				JsonArrayBuilder pbuilder = Json.createArrayBuilder();
				for (int i = 1; i < rowCount; i++) {
					JsonObjectBuilder jsonBuilder1 = Json.createObjectBuilder();
					List<String> dataRow = dataTable.get(i);
					for (int j = 0; j < columnCount; j++) {
						String columnName = headerRow.get(j);
						String columnValue = dataRow.get(j);
						jsonBuilder1.add(columnName, columnValue);
					}
					pbuilder.add(jsonBuilder1);
				}
				jsonBuilder.add("whitelisted_ids", pbuilder);
				JsonObject empObj = jsonBuilder.build();
				StringWriter strWtr = new StringWriter();
				JsonWriter jsonWtr = Json.createWriter(strWtr);
				jsonWtr.writeObject(empObj);
				jsonWtr.close();
				ret = strWtr.toString().replace("\\", "").replace("\"[", "[").replace("]\"", "]");
				System.out.println(ret);
				ObjectMapper mapper = new ObjectMapper();
				Object json = mapper.readValue(ret, Object.class);
				indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
//				System.out.println(indented);
			}
		}
		return indented;
	}

	private static void creteJSONFileFromExcel(String filePath) {
		try {
			FileInputStream fInputStream = new FileInputStream(filePath.trim());

			Workbook excelWorkBook = new XSSFWorkbook(fInputStream);
			int totalSheetNumber = excelWorkBook.getNumberOfSheets();
			// for (int i = 0; i < totalSheetNumber; i++) {
//				Sheet sheet = excelWorkBook.getSheetAt(i);
			Sheet sheet = excelWorkBook.getSheetAt(1);
			String sheetName = sheet.getSheetName();

			if (sheetName != null && sheetName.length() > 0) {
				List<List<String>> sheetDataTable = getSheetDataList(sheet);
				String jsonString = dataToJSON(sheetDataTable);

				System.out.println(filePath);
				Pattern patternFilepath = Pattern.compile("(.*/)(.*)(.xlsx)");
				Matcher matcherFilepath = patternFilepath.matcher(filePath);
				String jsonFilePath = "";
				if (matcherFilepath.find()) {
					jsonFilePath = matcherFilepath.group(1);
				}
				System.out.println(jsonFilePath);
				String jsonFileName = jsonFilePath + sheet.getSheetName() + ".json";
				writeStringToFile(jsonString, jsonFileName);
			}
			// }
			excelWorkBook.close();
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}

	private static List<List<String>> getSheetDataList(Sheet sheet) {
		List<List<String>> ret = new ArrayList<List<String>>();
		int firstRowNum = sheet.getFirstRowNum();
		int lastRowNum = sheet.getLastRowNum();
		if (lastRowNum > 0) {
			for (int i = firstRowNum; i < lastRowNum + 1; i++) {
				Row row = sheet.getRow(i);
				int firstCellNum = row.getFirstCellNum();
				int lastCellNum = row.getLastCellNum();
				List<String> rowDataList = new ArrayList<String>();
				for (int j = firstCellNum; j < lastCellNum; j++) {
					Cell cell = row.getCell(j);
					int cellType = cell.getCellType().getCode();
					if (cellType == CellType.NUMERIC.getCode()) {
						double numberValue = cell.getNumericCellValue();
						String stringCellValue = BigDecimal.valueOf(numberValue).toPlainString();
						rowDataList.add(stringCellValue);
					} else if (cellType == CellType.STRING.getCode()) {
						String cellValue = cell.getStringCellValue();
						rowDataList.add(cellValue);
					}
				}
				ret.add(rowDataList);
			}
		}
		return ret;
	}

	private static void writeStringToFile(String data, String jsonFilePath) {
		try {
			// String filePath = "C:/Users/MahiWay/Desktop/Tool/" + fileName;
			File file = new File(jsonFilePath);
			FileWriter fw = new FileWriter(file);
			BufferedWriter buffWriter = new BufferedWriter(fw);
			buffWriter.write(data);
			buffWriter.flush();
			buffWriter.close();
			System.out.println(jsonFilePath + " has been created.");
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
	}
}
