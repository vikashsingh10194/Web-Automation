package com.via.appmodules.flights.common;

import java.util.Calendar;
import java.util.Map;

import jxl.biff.CountryCode;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.via.pageobjects.flights.common.FlightBookingDetails;
import com.via.pageobjects.flights.common.FlightsSearch;
import com.via.testcases.common.TestCaseExcelConstant;
import com.via.utils.CalendarUtils;
import com.via.utils.Constant;
import com.via.utils.Constant.Journey;
import com.via.utils.Constant.VIA_COUNTRY;
import com.via.utils.CustomAssert;
import com.via.utils.EntityMap;
import com.via.utils.Log;
import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;

public class FlightsSearchAction {

  //WHEN WE WILL LOGIN........
  private static final String SOURCE_LIST = "ui-id-1";
  private static final String DESTINATION_LIST = "ui-id-2";
  
  /*
   * WHEN WE WILL NOT LOGIN......
   *  private static final String SOURCE_LIST = "ui-id-2";
   *  private static final String DESTINATION_LIST = "ui-id-3";
   */

  public static FlightBookingDetails execute(VIA_COUNTRY countryCode, WebDriver driver, RepositoryParser repositoryParser,
      Map<Integer, String> flightTestCase) {
    String testId = flightTestCase.get(TestCaseExcelConstant.COL_TEST_CASE_ID);
    FlightBookingDetails flightBookingDetails = setFlightBookingDetailsObject(flightTestCase);

    FlightsSearch flightsPage = new FlightsSearch(testId, driver, repositoryParser);

    // Contains the mapping of city name with the name to be appeared in
    // autoSearch box

    try{
      PageHandler.sleep(testId, 2 * 1000L);
      PageHandler.javaScriptExecuterClick(driver, driver.findElement(By.linkText("Skip")));
      PageHandler.sleep(testId, 2 * 1000L);
    }catch(Exception e){     
    }
    
    if (flightBookingDetails.getJourneyType() == Journey.ONE_WAY) {
      flightsPage.oneWay().click();
    } else if (flightBookingDetails.getJourneyType() == Journey.ROUND_TRIP) {
      flightsPage.roundTrip().click();
    }

    // Source
    String source = flightBookingDetails.getSourceCity();
    WebElement sourceElement = flightsPage.source();
    sourceElement.clear();
    sourceElement.sendKeys(source);
    String sourceComplete = EntityMap.getAirportCityName(countryCode, source);
    WebElement sourceAutoComplete = flightsPage.getAirportElement(SOURCE_LIST, sourceComplete);
    CustomAssert.assertTrue(sourceAutoComplete != null, "Error in finding source auto complete");

    // Sets the complete city name and airport code in the POJO
    flightBookingDetails.setSourceCityDetailed(sourceComplete);
    WebElement sourceCodeElement = flightsPage.getAirportCodeElement(sourceAutoComplete);
    flightBookingDetails.setSourceCityCode(getAirportCode(sourceCodeElement));
    sourceAutoComplete.click();

    // Destination
    String destination = flightBookingDetails.getDestinationCity();
    WebElement destinationElement = flightsPage.destination();
    destinationElement.clear();
    destinationElement.sendKeys(destination);
    String destinationComplete = EntityMap.getAirportCityName(countryCode, destination);
    WebElement destinationAutoComplete =
        flightsPage.getAirportElement(DESTINATION_LIST, destinationComplete);
    CustomAssert.assertTrue(destinationAutoComplete != null,
        "Error in finding destination auto complete");

    flightBookingDetails.setDestinationCityDetailed(destinationComplete);
    WebElement destinationCodeElement = flightsPage.getAirportCodeElement(destinationAutoComplete);
    flightBookingDetails.setDestinationCityCode(getAirportCode(destinationCodeElement));
    destinationAutoComplete.click();

    // Departure Date
    WebElement departureDate =
        CalendarUtils.selectDate(testId, driver, repositoryParser,
            flightBookingDetails.getOnwardDate(), Constant.DEPART_CAL_ID);
    CustomAssert.assertTrue(departureDate != null, "Unable to select departure date");
    departureDate.click();

    // Return Date
    if (flightBookingDetails.getJourneyType() == Journey.ROUND_TRIP) {
      WebElement returnDate =
          CalendarUtils.selectDate(testId, driver, repositoryParser,
              flightBookingDetails.getReturnDate(), Constant.RETURN_CAL_ID);
      CustomAssert.assertTrue(returnDate != null, "Unable to select return date");
      returnDate.click();
    }

    // Passenger Count
    setAdultsCount(testId, flightsPage, flightBookingDetails.getAdultsCount());
    setChildrenCount(testId, flightsPage, flightBookingDetails.getChildrenCount());
    setInfantsCount(testId, flightsPage, flightBookingDetails.getInfantsCount());

    // Search Flights Button
    WebElement searchFlight = flightsPage.searchFlight();
    CustomAssert.assertTrue(searchFlight != null, "Unable to find search flight button");
    searchFlight.click();

    printSearchDetailsInLogFile(flightBookingDetails);

    return flightBookingDetails;
  }

  private static void printSearchDetailsInLogFile(FlightBookingDetails flightBookingDetails) {

    String testId = flightBookingDetails.getTestCaseId();

    Log.info(testId, ":::::::::::::::::         Flight Home Page         :::::::::::::::");
    Log.divider(testId);

    Log.info(testId, "Source City :: " + flightBookingDetails.getSourceCity() + " ("
        + flightBookingDetails.getSourceCityCode() + ")");
    Log.info(testId, "Destination City :: " + flightBookingDetails.getDestinationCity() + " ("
        + flightBookingDetails.getDestinationCityCode() + ")");
    Log.info(
        testId,
        "Onward Date :: "
            + StringUtils.substring(
                CalendarUtils.getFormattedDateTime(flightBookingDetails.getOnwardDate()), 0, 17));
    if (flightBookingDetails.getJourneyType() == Journey.ROUND_TRIP) {
      Log.info(
          testId,
          "Return Date :: "
              + StringUtils.substring(
                  CalendarUtils.getFormattedDateTime(flightBookingDetails.getReturnDate()), 0, 17));
    }

    Log.info(testId, "Adult(s) :: " + flightBookingDetails.getAdultsCount());
    Log.info(testId, "Children :: " + flightBookingDetails.getChildrenCount());
    Log.info(testId, "Infant(s) :: " + flightBookingDetails.getInfantsCount());

    Log.divider(testId);
  }

  // This method sets the details in Flight Object from the map of Excel sheet
  private static FlightBookingDetails setFlightBookingDetailsObject(
      Map<Integer, String> flightTestCase) {

    FlightBookingDetails flightBookingDetails = new FlightBookingDetails();
    String testId = flightTestCase.get(TestCaseExcelConstant.COL_TEST_CASE_ID);
    flightBookingDetails.setTestCaseId(testId);
    
    
    //Abhishek.Added for promocode..
    String promoCode = flightTestCase.get(TestCaseExcelConstant.COL_PROMO_CODE);
    flightBookingDetails.setPromoCode(promoCode);
    
    String source = flightTestCase.get(TestCaseExcelConstant.COL_SOURCE);
    CustomAssert.assertTrue(testId, source != null, "Error in reading the source city");
    flightBookingDetails.setSourceCity(source);

    String destination = flightTestCase.get(TestCaseExcelConstant.COL_DESTINATION);
    CustomAssert.assertTrue(testId, destination != null, "Error in reading the destination city");
    flightBookingDetails.setDestinationCity(destination);

    /*** setting pax count ***/

    int adults = NumberUtils.toInt(flightTestCase.get(TestCaseExcelConstant.COL_ADULT));
    CustomAssert.assertTrue(adults != 0, "Error in reading the adults count");

    flightBookingDetails.setAdultsCount(adults);
    flightBookingDetails.setChildrenCount(NumberUtils.toInt(flightTestCase
        .get(TestCaseExcelConstant.COL_CHILD)));
    flightBookingDetails.setInfantsCount(NumberUtils.toInt(flightTestCase
        .get(TestCaseExcelConstant.COL_INFANT)));

    String onwardAdd = flightTestCase.get(TestCaseExcelConstant.COL_ONWARD_DATE_ADD);
    CustomAssert.assertTrue(onwardAdd != null, "Error in reading onward date");

    /*** set journey calendar date ***/
    int onwardIncrement = Integer.parseInt(onwardAdd);
    Calendar onwardDate = CalendarUtils.getCalendarIncrementedByDays(onwardIncrement);
    flightBookingDetails.setOnwardDate(onwardDate);
    flightBookingDetails.setJourneyType(Journey.ONE_WAY);

    String returnAdd = flightTestCase.get(TestCaseExcelConstant.COL_RETURN_DATE_ADD);
    if (returnAdd != null) {
      flightBookingDetails.setJourneyType(Journey.ROUND_TRIP);
      int returnIncrement = onwardIncrement + Integer.parseInt(returnAdd);
      Calendar returnDate = CalendarUtils.getCalendarIncrementedByDays(returnIncrement);
      flightBookingDetails.setReturnDate(returnDate);
    }

    String onwardFlightName = flightTestCase.get(TestCaseExcelConstant.COL_ONWARD_FLIGHT);
    CustomAssert.assertTrue(onwardFlightName != null, "Error in reading onward flight name");
    flightBookingDetails.setOnwardFlightName(onwardFlightName);

    String returnFlightName = flightTestCase.get(TestCaseExcelConstant.COL_RETURN_FLIGHT);
    CustomAssert.assertTrue(onwardFlightName != null, "Error in reading return flight name");
    flightBookingDetails.setReturnFlightName(returnFlightName);

    flightBookingDetails.setOnwardSSR(flightTestCase.get(TestCaseExcelConstant.COL_ONWARD_SSR));
    flightBookingDetails.setReturnSSR(flightTestCase.get(TestCaseExcelConstant.COL_RETURN_SSR));

    flightBookingDetails.setInsurance(StringUtils.equalsIgnoreCase(
        flightTestCase.get(TestCaseExcelConstant.COL_INSURANCE), "yes"));

    return flightBookingDetails;
  }

  private static void setAdultsCount(String testId, FlightsSearch flightsPage, int targetCount) {
    WebElement adultCountMinus = flightsPage.getAdultCountMinus();
    WebElement adultCount = flightsPage.getAdultCount();
    WebElement adultCountPlus = flightsPage.getAdultCountPlus();
    setPassengerCount(adultCountMinus, adultCount, adultCountPlus, targetCount);
  }

  private static void setChildrenCount(String testId, FlightsSearch flightsPage, int targetCount) {
    WebElement childrenCountMinus = flightsPage.getChildrenCountMinus();
    WebElement childrenCount = flightsPage.getChildrenCount();
    WebElement childrenCountPlus = flightsPage.getChildrenCountPlus();
    setPassengerCount(childrenCountMinus, childrenCount, childrenCountPlus, targetCount);
  }

  private static void setInfantsCount(String testId, FlightsSearch flightsPage, int targetCount) {
    WebElement infantCountMinus = flightsPage.getInfantCountMinus();
    WebElement infantCount = flightsPage.getInfantCount();
    WebElement infantCountPlus = flightsPage.getInfantCountPlus();
    setPassengerCount(infantCountMinus, infantCount, infantCountPlus, targetCount);
  }

  private static void setPassengerCount(WebElement decreaseCountElement, WebElement countElement,
      WebElement increaseCountElement, int targetCount) {
    WebElement element = null;
    int currCount = Integer.parseInt(countElement.getText());
    int diff = targetCount - currCount;

    while (diff != 0) {
      if (diff > 0) {
        element = increaseCountElement;
        diff--;
      } else {
        element = decreaseCountElement;
        diff++;
      }
      element.click();
    }
  }

  private static String getAirportCode(WebElement airportCodeElement) {
    if (airportCodeElement == null) {
      return null;
    }
    String codeComplete = airportCodeElement.getText();
    if (codeComplete == null || codeComplete.length() < 3) {
      return null;
    }
    String code = codeComplete.substring(1, codeComplete.length() - 1);
    return code;
  }
}
