package com.via.testcases.thailand;

import java.util.Map;

import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.via.testcases.common.IntlFlightTestFlowExecuter;
import com.via.utils.Constant;
import com.via.utils.Constant.Flight;
import com.via.utils.Constant.VIA_COUNTRY;
import com.via.utils.DataProviderParameters;
import com.via.utils.Log;
import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;
import com.via.utils.TestCaseDataProvider;

public class IntlFlightsTestCase {

  private WebDriver driver;
  private RepositoryParser repositoryParser;

  @BeforeClass
  public void dataFetch() {
    repositoryParser = new RepositoryParser();
    repositoryParser.pageElementsLoader(Constant.FLIGHTS_COMMON_LOCATOR_REPOSITORY);
    repositoryParser.pageElementsLoader(Constant.FLIGHTS_INTL_LOCATOR_REPOSITORY);
    repositoryParser.pageElementsLoader(Constant.COMMON_LOCATOR_REPOSITORY);
    repositoryParser.pageElementsLoader(Constant.BOOKING_DETAILS_LOCATOR_REPOSITORY);
    repositoryParser.transactionRuleLoader(VIA_COUNTRY.TH, Constant.TH_TRANSACTION_RULE_JSON);
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
  @DataProviderParameters(path = Constant.TH_FLIGHTS_TESTDATA,
      sheetName = Constant.FLIGHTS_INTL_SHEETNAME, parallel = true)
  public void flightsSearchValidation(String testId, Map<Integer, String> testData) {
    IntlFlightTestFlowExecuter flightTestExecuter =
        new IntlFlightTestFlowExecuter(VIA_COUNTRY.TH, testId, Flight.INTERNATIONAL, driver,
            repositoryParser);
    flightTestExecuter.execute(testData);
  }
}
