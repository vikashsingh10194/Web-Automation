package com.via.testcases.indonesia;

import java.util.Map;

import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.via.testcases.common.reseller.ERechargeTestFlowExecuter;
import com.via.utils.Constant;
import com.via.utils.Constant.VIA_COUNTRY;
import com.via.utils.DataProviderParameters;
import com.via.utils.Log;
import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;
import com.via.utils.TestCaseDataProvider;

public class ERechargeTestCaseReseller {

  private WebDriver driver;
  private RepositoryParser repositoryParser;

  @BeforeClass
  public void datafetch() {
    repositoryParser = new RepositoryParser();
    repositoryParser.pageElementsLoader(Constant.HOLIDAYS_LOCATOR_REPOSITORY);
    repositoryParser.pageElementsLoader(Constant.COMMON_LOCATOR_REPOSITORY);
    repositoryParser.pageElementsLoader(Constant.RESELLER_COMMON_LOCATOR_REPOSITORY);
    repositoryParser.pageElementsLoader(Constant.ERECHARGE_LOCATOR_REPOSITORY);
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
  @DataProviderParameters(path = Constant.ID_ERECHARGE_TESTDATA,
      sheetName = Constant.ERECHARGE_RESELLER_SHEETNAME,parallel=true)
  public void hotelsSearchValidation(String testId, Map<Integer, String> testData) {
    ERechargeTestFlowExecuter rechargeExecuter =
        new ERechargeTestFlowExecuter(VIA_COUNTRY.ID, testId, driver, repositoryParser);
    rechargeExecuter.execute(testData);
  }

}
