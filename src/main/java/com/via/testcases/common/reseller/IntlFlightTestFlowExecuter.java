package com.via.testcases.common.reseller;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.via.appmodules.common.HomePageActions;
import com.via.appmodules.flights.common.BookingDetailsValidator;
import com.via.appmodules.flights.common.FlightsSearchAction;
import com.via.appmodules.flights.common.NewTravellersPageController;
import com.via.appmodules.flights.common.TravellersPageController;
import com.via.appmodules.flights.international.FlightSearchResultController;
import com.via.appmodules.payment.Payment;
import com.via.pageobjects.flights.common.FlightBookingDetails;
import com.via.pageobjects.flights.common.TravellerDetails;
import com.via.reseller.appmodules.common.AgentLoginController;
import com.via.reseller.appmodules.common.LoginControllerCorp;
import com.via.testcases.common.TestCaseExcelConstant;
import com.via.utils.Constant.BOOKING_MEDIA;
import com.via.utils.Constant.Flight;
import com.via.utils.Constant.VIA_COUNTRY;
import com.via.utils.Constant;
import com.via.utils.Log;
import com.via.utils.NumberUtility;
import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;

@AllArgsConstructor
public class IntlFlightTestFlowExecuter {

  private VIA_COUNTRY countryCode;
  private String testId;
  private Flight flightType;
  private WebDriver driver;
  private RepositoryParser repositoryParser;
  private final String TEST_SUITE = "B2B Intl Flights";

  public void execute(Map<Integer, String> testData) {
    String url = null;
    boolean login = false;

    Log.startTestCase(testId);
    try {
      if (countryCode == VIA_COUNTRY.IN_CORP) {
        LoginControllerCorp corpLoginController =
            new LoginControllerCorp(driver, repositoryParser, testId, countryCode);
        
        login = corpLoginController.agentLogin(testData.get(TestCaseExcelConstant.COL_LOGIN));
      }else{
        AgentLoginController agentLoginController =
            new AgentLoginController(driver, repositoryParser, testId, countryCode);
        login = agentLoginController.agentLogin(testData.get(TestCaseExcelConstant.COL_LOGIN));
      }
      if (countryCode == VIA_COUNTRY.PH) {
        url = driver.getCurrentUrl().replaceAll("true", "false");
        driver.get(url);
      }
      if (countryCode == VIA_COUNTRY.ID || countryCode == VIA_COUNTRY.TH) {
        new HomePageActions(driver, repositoryParser, testId).changeLanguageToEnglish();
      }
      url = driver.getCurrentUrl();

      FlightBookingDetails flightBookingDetails =
          FlightsSearchAction.execute(countryCode, driver, repositoryParser, testData);
      FlightSearchResultController flightResultController =
          new FlightSearchResultController(testId, countryCode, driver, repositoryParser);
      flightResultController.execute(BOOKING_MEDIA.B2B, flightBookingDetails);
      List<TravellerDetails> travellerList;
      if (countryCode==Constant.VIA_COUNTRY.ID || countryCode==Constant.VIA_COUNTRY.IN_CORP) {
        travellerList =
            NewTravellersPageController.execute(countryCode, flightType, driver, repositoryParser,
                flightBookingDetails);
      }else{
        travellerList =
            TravellersPageController.execute(countryCode, flightType, driver, repositoryParser,
                flightBookingDetails);
      }

      Payment payment = new Payment(testId, countryCode, driver, repositoryParser);

      Map<String, String> paymentDetails =
          payment.execute(driver, flightBookingDetails.getTotalFare(), testData);

      Double totalAmountWithConv =
          NumberUtility.getAmountFromString(countryCode,
              paymentDetails.get(Constant.AMOUNT_WITH_CONV));

      payment.verifyConvenience(driver, BOOKING_MEDIA.B2B, flightType, flightBookingDetails,
          flightBookingDetails.getTotalFare(), totalAmountWithConv);

      flightBookingDetails.setTotalFare(totalAmountWithConv);

      if (StringUtils.equalsIgnoreCase(paymentDetails.get(Constant.BOOKING_VERIFICATION), "true")) {
        BookingDetailsValidator bookingDetailsValidation =
            new BookingDetailsValidator(testId, countryCode, flightType, driver, repositoryParser,
                flightBookingDetails, travellerList);
        bookingDetailsValidation.execute();
      }
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
            Log.info(testId, "Interrupt occured");
          }
          // driver.get(url);
          // agentLoginController.signOut();
          login = false;
        }
        driver.close();
        Log.endTestCase(testId);
      }
    }
  }
}
