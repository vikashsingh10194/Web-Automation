package com.via.testcases.common;

import java.util.Map;

import lombok.AllArgsConstructor;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.via.appmodules.common.HomePageActions;
import com.via.appmodules.hotels.GuestsDetailsInput;
import com.via.appmodules.hotels.HotelSearchResults;
import com.via.appmodules.hotels.HotelsSearchAction;
import com.via.pageobjects.hotels.HotelDetails;
import com.via.reseller.appmodules.common.LoginControllerCorp;
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
  private final String TEST_SUITE = "B2C Hotels";

  public void execute(Map<Integer, String> testData) {
    HomePageActions homePageAction = null;
    String url = driver.getCurrentUrl();
    String loginDetails = testData.get(TestCaseExcelConstant.COL_LOGIN);
    boolean login = false;
    try {
      Log.startTestCase(testId);
      if (countryCode == VIA_COUNTRY.IN_CORP) {
          LoginControllerCorp corpLoginController =
              new LoginControllerCorp(driver, repositoryParser, testId, countryCode);
          
          login = corpLoginController.agentLogin(loginDetails);
      }
      else{
      homePageAction = new HomePageActions(driver, repositoryParser, testId);

      if (VIA_COUNTRY.ID == countryCode || VIA_COUNTRY.TH == countryCode) {
        homePageAction.changeLanguageToEnglish();
      }

      login = homePageAction.loginUser(countryCode, loginDetails);
      }
      HotelsSearchAction hotelSearchAction =
          new HotelsSearchAction(testId, driver, repositoryParser);
      HotelDetails hotelDetails = hotelSearchAction.execute(testData);
      HotelSearchResults hotelResults =
          new HotelSearchResults(testId, driver, countryCode, repositoryParser, hotelDetails);
      hotelResults.execute();
      GuestsDetailsInput guestDetailsInput =
          new GuestsDetailsInput(testId, driver, countryCode, repositoryParser, hotelDetails);
      Double totalFare = guestDetailsInput.execute();
      // Double amountWithConv = Payment.execute(driver, countryCode,
      // repositoryParser, totalFare, testData);
      Log.endTestCase(testId);
    } catch (Exception e) {
      //PageHandler.sendFailureMessageSlack(countryCode, TEST_SUITE, testId, e.getMessage());
      // System.out.println(driver.getPageSource());
      Log.error(e.getMessage());
      Log.endTestCase(testId);
      Assert.fail(testId + "failed");
    } finally {
      if (driver != null) {
        if (login) {
          PageHandler.sleep(testId, 3 * 1000L);
          driver.get(url);
          homePageAction.signOut();
          login = false;
        }
        driver.close();
        driver.quit();
      }
    }
  }
}
