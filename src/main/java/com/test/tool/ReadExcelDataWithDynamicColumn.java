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
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
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

	private static String dataToOrgToJSON(List<List<String>> dataTable)
			throws JsonGenerationException, JsonMappingException, IOException {
		JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
		jsonBuilder.add("dist_refid", "");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		jsonBuilder.add("date_updated", dtf.format(now));

		System.out.println(dtf.format(now));
		String ret = "";
		String indented = "";
		List<String> dataRowb = new ArrayList<String>();
		if (dataTable != null) {
			int rowCount = dataTable.size();
			for (int i = 0; i < rowCount; i++) {
				dataRowb = dataTable.get(i);
			}
		}
		for (String temp : dataRowb) {
			System.out.println(temp);
		}
		Set<String> set = new HashSet<String>(dataRowb);

		System.out.println("Set values .....");
		for (String temp : set) {
			System.out.println(temp);
		}

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
		List<String> dataRowb = new ArrayList<String>();
		List<String> headerRow1 = dataTable.get(0);
		int rowCount1 = dataTable.size();
		if (dataTable != null) {
//			int columnCount1=headerRow.size();
			for (int i = 1; i < rowCount1; i++) {
				List<String> dataRow = dataTable.get(i);
				String str = dataRow.get(0) + " AZ0ZA " + dataRow.get(1) + " AZ0ZA " + dataRow.get(2);
				dataRowb.add(str);
//				System.out.println(str);
			}
		}
		System.out.println(".............Data with duplicate values...........");
		for (String temp : dataRowb) {
			System.out.println(temp);
		}
		
		Set<String> set = new HashSet<String>(dataRowb);
		System.out.println(".............Set values...........");
		for (String temp : set) {
			System.out.println(temp);
		}
		String rowHeader=headerRow1.get(0) + " AZ0ZA " + headerRow1.get(1) + " AZ0ZA " + headerRow1.get(2);
//		System.out.println(rowHeader);
		List<String> dataRowb1 = new ArrayList<String>(set);
		dataRowb1.add(0, rowHeader);
		System.out.println(".............List values...........");
		for (String temp : dataRowb1) {
			System.out.println(temp);
		}

//		Pattern patternRev = Pattern.compile("(.*?)(r[0-9]{3,})(\\d+?)");
 //       Matcher matcherRev = patternRev.matcher(dataRowb1.get(0));
		if (dataTable != null) {
			int rowCount = dataTable.size();
			if (rowCount > 1) {
				List<String> headerRow = dataTable.get(0);
				int columnCount = headerRow.size();
				JsonArrayBuilder pbuilder = Json.createArrayBuilder();
				for (int i = 1; i < dataRowb1.size(); i++) {
					JsonObjectBuilder jsonBuilder1 = Json.createObjectBuilder();
//					List<String> dataRow = dataTable.get(i);
					String strr=dataRowb1.get(i);
					String[] sp=strr.split(" AZ0ZA ");
//					System.out.println("Data row " + dataRow);
//					System.out.println("Copied column count : " + columnCount);
					for (int j = 0; j < columnCount; j++) {
						String columnName = headerRow.get(j);
						String columnValue = sp[j];
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
