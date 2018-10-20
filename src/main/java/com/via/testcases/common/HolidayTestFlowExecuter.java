package com.via.testcases.common;

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
  private final String TEST_SUITE = "B2C Holidays";

  public void execute(Map<Integer, String> testData) {
    HomePageActions homePageAction = new HomePageActions(driver, repositoryParser, testId);
    String url = driver.getCurrentUrl();
    boolean login = false;
    try {
      Log.startTestCase(testId);
      String loginDetails = testData.get(TestCaseExcelConstant.COL_LOGIN);

      if (VIA_COUNTRY.ID == countryCode || VIA_COUNTRY.TH == countryCode) {
        homePageAction.changeLanguageToEnglish();
      }

      login = homePageAction.loginUser(countryCode, loginDetails);
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
      Assert.fail(testId + " failed");
    } finally {
      if (driver != null) {
        if (login) {
          try {
            Thread.sleep(2 * 1000);
          } catch (Exception e) {

          }
          driver.navigate().to(url);
          homePageAction.signOut();
          login = false;
        }
        driver.close();
        driver.quit();
      }
      Log.endTestCase(testId);
    }

  }

}
