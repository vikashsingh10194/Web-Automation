package com.via.testcases.indonesia;

import java.util.Map;

import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.via.testcases.common.reseller.TrainTestFlowExecuter;
import com.via.utils.Constant;
import com.via.utils.Constant.BOOKING_MEDIA;
import com.via.utils.Constant.VIA_COUNTRY;
import com.via.utils.DataProviderParameters;
import com.via.utils.Log;
import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;
import com.via.utils.TestCaseDataProvider;

public class TrainsTestCaseReseller {

  private WebDriver driver;
  private RepositoryParser repositoryParser;

  @BeforeClass
  public void datafetch() {
    repositoryParser = new RepositoryParser();
    repositoryParser.pageElementsLoader(Constant.TRAINS_LOCATOR_REPOSITORY);
    repositoryParser.pageElementsLoader(Constant.COMMON_LOCATOR_REPOSITORY);
    repositoryParser.pageElementsLoader(Constant.RESELLER_COMMON_LOCATOR_REPOSITORY);
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
  @DataProviderParameters(path = Constant.ID_TRAINS_TESTDATA,
      sheetName = Constant.TRAINS_RESELLER_SHEETNAME, parallel = true)
  public void hotelsSearchValidation(String testId, Map<Integer, String> testData) {
    TrainTestFlowExecuter trainTestExecuter =
        new TrainTestFlowExecuter(VIA_COUNTRY.ID, testId, BOOKING_MEDIA.B2B, driver,
            repositoryParser);
    trainTestExecuter.execute(testData);

  }
}
