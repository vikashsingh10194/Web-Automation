package com.via.testcases.common;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.via.appmodules.common.HomePageActions;
import com.via.appmodules.flights.common.BookingDetailsValidator;
import com.via.appmodules.flights.common.FlightsSearchAction;
import com.via.appmodules.flights.common.NewTravellersPageController;
import com.via.appmodules.flights.common.TravellersPageController;
import com.via.appmodules.flights.international.FlightSearchResultController;
import com.via.appmodules.payment.Payment;
import com.via.pageobjects.flights.common.FlightBookingDetails;
import com.via.pageobjects.flights.common.FlightsSearch;
import com.via.pageobjects.flights.common.TravellerDetails;
import com.via.utils.Constant;
import com.via.utils.Constant.BOOKING_MEDIA;
import com.via.utils.Constant.Flight;
import com.via.utils.Constant.VIA_COUNTRY;
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

  private final String TEST_SUITE = "B2C Intl Flights";

  public void execute(Map<Integer, String> testData) {
    HomePageActions homePageAction = null;
    String url = driver.getCurrentUrl();
    boolean login = false;
    try {
      Log.startTestCase(testId);
      String loginDetails = testData.get(TestCaseExcelConstant.COL_LOGIN);

      homePageAction = new HomePageActions(driver, repositoryParser, testId);

      if (VIA_COUNTRY.ID == countryCode || VIA_COUNTRY.TH == countryCode) {
        homePageAction.changeLanguageToEnglish();
      }

      login = homePageAction.loginUser(countryCode, loginDetails);
      // In case of UAE on home page,it opens popup box automatically..
      if (countryCode == VIA_COUNTRY.UAE) {
        FlightsSearch flightsPage = new FlightsSearch(testId, driver, repositoryParser);
        WebElement element = flightsPage.getFlightsLink();
        PageHandler.explicitWait(driver, element);
        element.click();
      }

      FlightBookingDetails flightBookingDetails =
          FlightsSearchAction.execute(countryCode, driver, repositoryParser, testData);
      FlightSearchResultController flightResultController =
          new FlightSearchResultController(testId, countryCode, driver, repositoryParser);
      flightResultController.execute(BOOKING_MEDIA.B2C, flightBookingDetails);
//      List<TravellerDetails> travellerList =
//          TravellersPageController.execute(countryCode, flightType, driver, repositoryParser,
//              flightBookingDetails);
      
      
      //For New TDP Page.
      List<TravellerDetails> travellerList =
        NewTravellersPageController.execute(countryCode, flightType, driver, repositoryParser,
            flightBookingDetails);
      
      
      Payment payment = new Payment(testId, countryCode, driver, repositoryParser);

      Map<String, String> paymentDetails =
          payment.execute(driver, flightBookingDetails.getTotalFare(), testData);

      Double totalAmountWithConv =
          NumberUtility.getAmountFromString(countryCode,
              paymentDetails.get(Constant.AMOUNT_WITH_CONV));

      payment.verifyConvenience(driver, BOOKING_MEDIA.B2C, flightType, flightBookingDetails,
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
      Log.endTestCase(testId);
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
