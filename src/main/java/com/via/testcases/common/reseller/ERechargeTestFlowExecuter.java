package com.via.testcases.common.reseller;

import java.util.Map;

import lombok.AllArgsConstructor;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.via.appmodules.common.HomePageActions;
import com.via.appmodules.erecharge.ERechargeHandler;
import com.via.reseller.appmodules.common.AgentLoginController;
import com.via.testcases.common.TestCaseExcelConstant;
import com.via.utils.Constant.VIA_COUNTRY;
import com.via.utils.Log;
import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;

@AllArgsConstructor
public class ERechargeTestFlowExecuter {
  private VIA_COUNTRY countryCode;
  private String testId;
  private WebDriver driver;
  private RepositoryParser repositoryParser;
  private final String TEST_SUITE = "B2B ERecharge";

  public void execute(Map<Integer, String> testData) {
    boolean login = false;
    String url = null;
    AgentLoginController agentLoginController =
        new AgentLoginController(driver, repositoryParser, testId, countryCode);
    try {
      Log.startTestCase(testId);
      login = agentLoginController.agentLogin(testData.get(TestCaseExcelConstant.COL_LOGIN));
      if (countryCode == VIA_COUNTRY.PH) {
        url = driver.getCurrentUrl().replaceAll("true", "false");
        driver.get(url);
      }

      if (countryCode == VIA_COUNTRY.ID || countryCode == VIA_COUNTRY.TH) {
        new HomePageActions(driver, repositoryParser, testId).changeLanguageToEnglish();
      }

      url = driver.getCurrentUrl();

      ERechargeHandler rechargeHandler =
          new ERechargeHandler(testId, countryCode, driver, repositoryParser);
      rechargeHandler.execute(testData);
    } catch (Exception e) {
      //PageHandler.sendFailureMessageSlack(countryCode, TEST_SUITE, testId, e.getMessage());
      Log.error(e.getMessage());
      Assert.fail(testId + "failed");
    } finally {
      if (driver != null) {
        if (login) {
          PageHandler.sleep(testId, 2 * 1000L);
          // driver.get(url);
          // agentLoginController.signOut();
          login = false;
        }
        Log.endTestCase(testId);
        driver.close();
        driver.quit();
      }
    }
  }
}
