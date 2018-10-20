package com.via.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.via.testcases.common.TestCaseExcelConstant;

public class TestCaseExcelUtils {
  public static XSSFSheet getExcelDocumentReader(String documentPath, String sheetName)
      throws IOException {
    FileInputStream file = new FileInputStream(documentPath);
    XSSFWorkbook excelWorkBook = new XSSFWorkbook(file);
    XSSFSheet excelWorkSheet = excelWorkBook.getSheet(sheetName);
    return excelWorkSheet;
  }

  private static XSSFCell getCell(XSSFSheet excelSheet, int rowNo, int colNo) {
    XSSFRow row = excelSheet.getRow(rowNo);
    if (row == null) {
      return null;
    }

    return row.getCell(colNo);
  }

  private static int getRowNoOfTestName(XSSFSheet excelSheet, String testName) {
    int totalRows = excelSheet.getLastRowNum();
    for (int i = 0; i <= totalRows; i++) {
      XSSFCell cell = getCell(excelSheet, i, TestCaseExcelConstant.COL_TEST_CASE_ID);
      if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
        continue;
      }

      String testCaseId = StringUtils.trimToEmpty(cell.getStringCellValue());

      if (StringUtils.equalsIgnoreCase(testCaseId, testName)) {
        return i;
      }
    }
    return -1;
  }

  public static Map<Integer, String> getDetailsByTestName(XSSFSheet excelSheet, String testName) {
    int rowNo = getRowNoOfTestName(excelSheet, testName);
    return getDetailsOfRow(excelSheet, rowNo);
  }

  public static Map<Integer, String> getDetailsOfRow(XSSFSheet excelSheet, int rowNo) {
    // colNumber -> colValue
    Map<Integer, String> testCaseDetails = new HashMap<Integer, String>();
    if (excelSheet == null || rowNo <= 0) {
      return testCaseDetails;
    }

    XSSFRow sheetRow = excelSheet.getRow(rowNo);
    if (sheetRow == null) {
      return testCaseDetails;
    }

    int totalColumns = sheetRow.getLastCellNum();
    for (int i = 0; i < totalColumns; i++) {
      Cell cell = sheetRow.getCell(i);
      if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
        continue;
      }
      cell.setCellType(Cell.CELL_TYPE_STRING);
      String content = StringUtils.trimToEmpty(cell.getStringCellValue().trim());

      if (StringUtils.isNotBlank(content)) {
        testCaseDetails.put(i, content);
      }
    }
    return testCaseDetails;
  }

  public static Map<String, Map<Integer, String>> getCompleteSheetDetails(XSSFSheet excelSheet,
      int rowCount) {
    // testCaseId -> Map(colNumber -> colValue)
    Map<String, Map<Integer, String>> completeSheetDetails =
        new LinkedHashMap<String, Map<Integer, String>>();

    if (excelSheet == null) {
      return completeSheetDetails;
    }

    int totalRows = excelSheet.getLastRowNum();
    int headerIndex = 0;

    // finding header row.
    for (; headerIndex <= totalRows; headerIndex++) {
      Cell cell = excelSheet.getRow(headerIndex).getCell(0);
      if (cell != null) {
        break;
      }
    }

    int testCount = 0;
    for (int rowNo = headerIndex + 1; rowNo <= totalRows; rowNo++) {
      if (testCount == rowCount) {
        break;
      }
      XSSFRow row = excelSheet.getRow(rowNo);
      if (row == null) {
        continue;
      }

      String scriptRunStatus =
          row.getCell(TestCaseExcelConstant.COL_SCRIPT_RUN).getStringCellValue();
      if (scriptRunStatus.equalsIgnoreCase("NO")) {
        continue;
      }

      String testCaseId = row.getCell(TestCaseExcelConstant.COL_TEST_CASE_ID).getStringCellValue();

      Map<Integer, String> rowContentMap = completeSheetDetails.get(testCaseId);
      if (rowContentMap == null) {
        rowContentMap = getDetailsOfRow(excelSheet, rowNo);
        if (!rowContentMap.isEmpty()) {
          completeSheetDetails.put(testCaseId, rowContentMap);
          testCount++;
        }
      }
    }
    return completeSheetDetails;
  }
}
