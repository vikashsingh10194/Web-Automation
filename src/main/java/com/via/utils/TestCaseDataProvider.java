package com.via.utils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

public class TestCaseDataProvider {

  private static final String SHEET_ROW_COUNT_KEY = "dataSheetRowCount";

  @DataProvider(name = "inputTestData", parallel = true)
  public static Object[][] getExcelDataAsObect(Method testDataExcelFileName, ITestContext context) {
    Object[][] testCaseDetailsAsObject = null;
    Map<String, Map<Integer, String>> testCaseDetails;
    int rowCount;
    String parameterValue;
    try {
      DataProviderParameters parameters =
          testDataExcelFileName.getAnnotation(DataProviderParameters.class);
      String documentPath = parameters.path();
      Map<String, String> xmlParameters = context.getCurrentXmlTest().getAllParameters();
      if (xmlParameters.containsKey(SHEET_ROW_COUNT_KEY)) {
        parameterValue = xmlParameters.get(SHEET_ROW_COUNT_KEY);
        if (parameterValue != "") {
          rowCount = Integer.parseInt(parameterValue);
        } else {
          rowCount = -1;
        }
      } else {
        rowCount = -1;
      }

      String sheetName = parameters.sheetName();
      XSSFSheet excelSheet =
          TestCaseExcelUtils.getExcelDocumentReader(System.getProperty("user.dir") + documentPath,
              sheetName);
      testCaseDetails = TestCaseExcelUtils.getCompleteSheetDetails(excelSheet, rowCount);

      testCaseDetailsAsObject = new Object[testCaseDetails.size()][2];
      int testNo = 0;
      for (Entry<String, Map<Integer, String>> testCase : testCaseDetails.entrySet()) {
        testCaseDetailsAsObject[testNo][0] = testCase.getKey();
        testCaseDetailsAsObject[testNo++][1] = testCase.getValue();
      }
    } catch (IOException e) {
      Log.warn("No data fetched from excel sheet.");
      return testCaseDetailsAsObject;
    }
    return testCaseDetailsAsObject;
  }
}
