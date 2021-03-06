package com.via.testcases.common.reseller;

import java.util.Map;

import lombok.AllArgsConstructor;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.via.appmodules.common.HomePageActions;
import com.via.appmodules.holidays.GuestsDetailsInput;
import com.via.appmodules.holidays.HolidaySearchAction;
import com.via.appmodules.holidays.HolidaySearchResults;
import com.via.appmodules.payment.Payment;
import com.via.pageobjects.holidays.HolidayBookingDetails;
import com.via.reseller.appmodules.common.AgentLoginController;
import com.via.testcases.common.TestCaseExcelConstant;
import com.via.utils.Constant.VIA_COUNTRY;
import com.via.utils.Log;
import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;

@AllArgsConstructor
public class HolidayTestFlowExecuter {

  private VIA_COUNTRY countryCode;
  private String testId;
  private WebDriver driver;
  private RepositoryParser repositoryParser;
  private final String TEST_SUITE = "B2B Holidays";

  public void execute(Map<Integer, String> testData) {

    String url = null;
    AgentLoginController agentLoginController =
        new AgentLoginController(driver, repositoryParser, testId, countryCode);

    boolean login = false;

    try {
      Log.startTestCase(testId);

      login = agentLoginController.agentLogin(testData.get(TestCaseExcelConstant.COL_LOGIN));

      if (countryCode == VIA_COUNTRY.PH) {
        url = driver.getCurrentUrl().replaceAll("true", "false");
        driver.get(url);
      }

      if (VIA_COUNTRY.ID == countryCode || VIA_COUNTRY.TH == countryCode) {
        new HomePageActions(driver, repositoryParser, testId).changeLanguageToEnglish();
      }

      url = driver.getCurrentUrl();

      HolidayBookingDetails holidayDetails =
          HolidaySearchAction.execute(driver, repositoryParser, testData);
      boolean enquiryCheck =
          HolidaySearchResults.execute(testId, driver, repositoryParser, holidayDetails);
      if (!enquiryCheck) {
        double paymentFare =
            GuestsDetailsInput.execute(testId, driver, repositoryParser, holidayDetails);
        Payment payment = new Payment(testId, countryCode, driver, repositoryParser);
        payment.execute(driver, paymentFare, testData);
      }
      Log.endTestCase(testId);
    } catch (Exception e) {
      //PageHandler.sendFailureMessageSlack(countryCode, TEST_SUITE, testId, e.getMessage());
      Log.error(e.getMessage());
      Log.endTestCase(testId);
      Assert.fail(testId + " failed");
    } finally {
      if (driver != null) {
        if (login) {
          PageHandler.sleep(testId, 2 * 1000L);
          // driver.navigate().to(url);
          // agentLoginController.signOut();
          // login = false;
        }
        driver.close();
        driver.quit();
      }
    }

  }

}
