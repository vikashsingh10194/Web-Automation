package com.via.testcases.saudi;

import java.util.Map;

import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.via.testcases.common.reseller.DomFlightTestFlowExecuter;
import com.via.utils.Constant;
import com.via.utils.Constant.Flight;
import com.via.utils.Constant.VIA_COUNTRY;
import com.via.utils.DataProviderParameters;
import com.via.utils.EntityMap;
import com.via.utils.Log;
import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;
import com.via.utils.TestCaseDataProvider;

public class DomFlightsTestCaseReseller {

  private WebDriver driver;
  private RepositoryParser repositoryParser;

  @BeforeClass
  public void datafetch() {
    repositoryParser = new RepositoryParser();
    repositoryParser.pageElementsLoader(Constant.RESELLER_COMMON_LOCATOR_REPOSITORY);
    repositoryParser.pageElementsLoader(Constant.FLIGHTS_COMMON_LOCATOR_REPOSITORY);
    repositoryParser.pageElementsLoader(Constant.COMMON_LOCATOR_REPOSITORY);
    repositoryParser.pageElementsLoader(Constant.FLIGHTS_DOM_LOCATOR_REPOSITORY);
    repositoryParser.pageElementsLoader(Constant.BOOKING_DETAILS_LOCATOR_REPOSITORY);
    repositoryParser.transactionRuleLoader(VIA_COUNTRY.SA, Constant.SA_TRANSACTION_RULE_JSON);
    EntityMap.replaceAirportCity("JAKARTA", "Jakarta - Indonesia");
    repositoryParser.loadConfigProperties();
  }

  @BeforeMethod
  @Parameters({"browser", "Url"})
  public void beforeMethod(String browser, String url) throws Exception {
    String urlToRun = repositoryParser.getPropertyValue(url);
    try {
      DOMConfigurator.configure("log4j.xml");
      PageHandler commonUtils = new PageHandler();
      driver = commonUtils.openBrowser(browser, urlToRun);
    } catch (Exception e) {
      Log.error("Browser could not be opened");
    } finally {
      Assert.assertTrue(driver != null);
    }
  }

  @Test(dataProvider = "inputTestData", dataProviderClass = TestCaseDataProvider.class)
  @DataProviderParameters(path = Constant.SA_FLIGHTS_TESTDATA,
      sheetName = Constant.FLIGHTS_DOM_RESELLER_SHEETNAME, parallel=true)
  public void flightsSearchValidation(String testcaseId, Map<Integer, String> testData) {
    executeTest(driver, testcaseId, testData);
  }

  private void executeTest(WebDriver driver, String testId, Map<Integer, String> testData) {
    DomFlightTestFlowExecuter flightTestExecuter =
        new DomFlightTestFlowExecuter(VIA_COUNTRY.SA, testId, Flight.DOMESTIC, driver,
            repositoryParser);
    flightTestExecuter.execute(testData);
  }
}
