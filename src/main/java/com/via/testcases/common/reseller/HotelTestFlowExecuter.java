package com.via.testcases.common.reseller;

import java.util.Map;

import lombok.AllArgsConstructor;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.via.appmodules.common.HomePageActions;
import com.via.appmodules.hotels.GuestsDetailsInput;
import com.via.appmodules.hotels.HotelSearchResults;
import com.via.appmodules.hotels.HotelsSearchAction;
import com.via.pageobjects.hotels.HotelDetails;
import com.via.reseller.appmodules.common.AgentLoginController;
import com.via.testcases.common.TestCaseExcelConstant;
import com.via.utils.Constant.VIA_COUNTRY;
import com.via.utils.Log;
import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;

@AllArgsConstructor
public class HotelTestFlowExecuter {
  private VIA_COUNTRY countryCode;
  private String testId;
  private WebDriver driver;
  private RepositoryParser repositoryParser;
  private final String TEST_SUITE = "B2B Hotels";

  public void execute(Map<Integer, String> testData) {
    Log.startTestCase(testId);
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

      HotelsSearchAction hotelSearchAction =
          new HotelsSearchAction(testId, driver, repositoryParser);
      HotelDetails hotelDetails = hotelSearchAction.execute(testData);
      HotelSearchResults hotelResults =
          new HotelSearchResults(testId, driver, countryCode, repositoryParser, hotelDetails);
      hotelResults.execute();
      GuestsDetailsInput guestDetailsInput =
          new GuestsDetailsInput(testId, driver, countryCode, repositoryParser, hotelDetails);
      guestDetailsInput.execute();

      Log.endTestCase(testId);
    } catch (Exception e) {
      //PageHandler.sendFailureMessageSlack(countryCode, TEST_SUITE, testId, e.getMessage());
      Log.error(e.getMessage());
      Log.endTestCase(testId);
      Assert.fail(testId + "failed");
    } finally {
      if (driver != null) {
        if (login) {
          PageHandler.sleep(testId, 3 * 1000L);
          // driver.get(url);
          // agentLoginController.signOut();
          login = false;
        }
        driver.close();
        driver.quit();
      }
    }
  }
}
