package com.via.testcases.common;

import java.util.Map;

import lombok.AllArgsConstructor;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.via.appmodules.common.HomePageActions;
import com.via.appmodules.payment.Payment;
import com.via.appmodules.trains.TrainResultPageHandler;
import com.via.appmodules.trains.TrainsSearchAction;
import com.via.appmodules.trains.TravellersPageHandler;
import com.via.pageobjects.trains.TrainBookingDetails;
import com.via.utils.Constant;
import com.via.utils.Constant.BOOKING_MEDIA;
import com.via.utils.Constant.VIA_COUNTRY;
import com.via.utils.Log;
import com.via.utils.NumberUtility;
import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;

@AllArgsConstructor
public class TrainTestFlowExecuter {
  private VIA_COUNTRY countryCode;
  private String testId;
  private WebDriver driver;
  private BOOKING_MEDIA media;
  private RepositoryParser repositoryParser;
  private final String TEST_SUITE = "B2C Trains";

  public void execute(Map<Integer, String> testData) {
    boolean login = false;
    HomePageActions homePageAction = null;
    String url = driver.getCurrentUrl();
    try {
      Log.startTestCase(testId);
      String loginDetails = testData.get(TestCaseExcelConstant.COL_LOGIN);

      homePageAction = new HomePageActions(driver, repositoryParser, testId);

      if (VIA_COUNTRY.ID == countryCode || VIA_COUNTRY.TH == countryCode) {
        homePageAction.changeLanguageToEnglish();
      }

      login = homePageAction.loginUser(countryCode, loginDetails);

      TrainsSearchAction railSearchAction =
          new TrainsSearchAction(testId, driver, repositoryParser);
      TrainBookingDetails trainBookingDetails = railSearchAction.execute(testData);
      TrainResultPageHandler resultPageHandler =
          new TrainResultPageHandler(testId, driver, repositoryParser);
      resultPageHandler.execute(countryCode, trainBookingDetails);
      TravellersPageHandler travellersPageHandler =
          new TravellersPageHandler(testId, driver, repositoryParser);
      travellersPageHandler.execute(countryCode, media, trainBookingDetails);
      Double payableAmount = trainBookingDetails.getTotalFare();

      Payment payment = new Payment(testId, countryCode, driver, repositoryParser);
      Map<String, String> paymentDetails = payment.execute(driver, payableAmount, testData);
      Payment.verifyTrainConvenience(
          testId,
          countryCode,
          media,
          trainBookingDetails,
          payableAmount,
          NumberUtility.getAmountFromString(countryCode,
              paymentDetails.get(Constant.AMOUNT_WITH_CONV)));

    } catch (Exception e) {
      //PageHandler.sendFailureMessageSlack(countryCode, TEST_SUITE, testId, e.getMessage());
      Log.error(e.getMessage());
      Assert.fail(testId + "failed");
    } finally {
      if (driver != null) {
        if (login) {
          try {
            Thread.sleep(2 * 1000);
          } catch (Exception e) {

          }
          driver.get(url);
          homePageAction.signOut();
          login = false;
        }

        driver.close();
        driver.quit();
        Log.endTestCase(testId);
      }
    }
  }
}
