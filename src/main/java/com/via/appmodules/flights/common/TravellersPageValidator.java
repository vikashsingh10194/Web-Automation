package com.via.appmodules.flights.common;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.via.pageobjects.common.PageElement;
import com.via.pageobjects.flights.common.FlightBookingDetails;
import com.via.pageobjects.flights.common.FlightDetails;
import com.via.pageobjects.flights.common.TravellersPageElements;
import com.via.utils.CalendarUtils;
import com.via.utils.Constant;
import com.via.utils.CustomAssert;
import com.via.utils.Log;
import com.via.utils.StringUtilities;

public class TravellersPageValidator {

  private static final String FLIGHTS_DISPLAY_CALENDAR_FORMAT = "dd MMM, hh:mm";

  public static boolean validateOnwardJourney(WebDriver driver,
      TravellersPageElements travellersInput, FlightBookingDetails flightBookingDetails) {

    String testId = flightBookingDetails.getTestCaseId();

    String onwardFlightCountString = travellersInput.getOnwardFlightsCount().getText();
    int onwardFlightCount = getFlightsCountFromString(onwardFlightCountString);
    if (onwardFlightCount == -1) {
      return false;
    }

    List<FlightDetails> onwardFlights = flightBookingDetails.getOnwardFlights();
    CustomAssert.assertEquals(driver, testId, onwardFlightCount, onwardFlights.size(),
        "Onward flights count error :: Expected: " + onwardFlights.size() + " Actual: "
            + onwardFlightCount);

    return validateJourney(driver, testId, travellersInput, onwardFlights, 1, onwardFlightCount);
  }

  public static boolean validateReturnJourney(WebDriver driver,
      TravellersPageElements travellersInput, FlightBookingDetails flightBookingDetails) {

    String testId = flightBookingDetails.getTestCaseId();

    WebElement returnFlightCountElement = travellersInput.getReturnFlightsCount();
    String returnFlightCountString = returnFlightCountElement.getText();
    int returnFlightCount = getFlightsCountFromString(returnFlightCountString);
    if (returnFlightCount == -1) {
      return false;
    }

    List<FlightDetails> onwardFlights = flightBookingDetails.getOnwardFlights();
    List<FlightDetails> returnFlights = flightBookingDetails.getReturnFlights();
    CustomAssert.assertEquals(driver, testId, returnFlightCount, returnFlights.size(),
        "Return flights count error :: Expected: " + returnFlights.size() + " Actual: "
            + returnFlightCount);

    return validateJourney(driver, testId, travellersInput, returnFlights,
        onwardFlights.size() + 1, returnFlightCount);
  }

  // Start index and end index represents the index of onward journey or
  // return journey from overall journey flights
  private static boolean validateJourney(WebDriver driver, String testId,
      TravellersPageElements travellersInput, List<FlightDetails> flights, int startIndex,
      int endIndex) {

    // Validates all the flights occurring in the sector
    for (int i = startIndex; i <= endIndex;) {

      PageElement srcDestObject =
          travellersInput.modifyPageElementOnce("srcDest", Integer.toString(i));
      String srcDest = travellersInput.getElementByPageObject(srcDestObject).getText();

      List<String> srcDestList = StringUtilities.split(srcDest, " to ");
      if (srcDestList.size() < 2) {
        Log.error(driver, testId, "Invalid sourceDestination in flightSerial " + i);
        return false;
      }
      String source = srcDestList.get(0);
      String destination = srcDestList.get(1);

      PageElement flightNameObject =
          travellersInput.modifyPageElementOnce("flightName", Integer.toString(i));
      String flightName = travellersInput.getElementByPageObject(flightNameObject).getText();

      PageElement flightNumberObject =
          travellersInput.modifyPageElementOnce("flightNumber", Integer.toString(i));
      String flightNumber = travellersInput.getElementByPageObject(flightNumberObject).getText();
      String flightCode = separateAirportCodeWithHyphen(flightNumber);

      PageElement deaprtureDateTimeObject =
          travellersInput.modifyPageElementOnce("departureDateTime", Integer.toString(i));
      String departureDateTimeString =
          travellersInput.getElementByPageObject(deaprtureDateTimeObject).getText();

      Calendar departureDateTime = null;
      try {
        departureDateTime =
            CalendarUtils.dateStringToCalendarDate(departureDateTimeString,
                FLIGHTS_DISPLAY_CALENDAR_FORMAT);
      } catch (ParseException e) {
        Log.error(driver, testId,
            "Unable to parse departure date time in flightSerial " + i + e.getMessage());
        return false;
      }

      PageElement arrivalDateTimeObject =
          travellersInput.modifyPageElementOnce("arrivalDateTime", Integer.toString(i));
      String arrivalDateTimeString =
          travellersInput.getElementByPageObject(arrivalDateTimeObject).getText();
      Calendar arrivalDateTime = null;
      try {
        arrivalDateTime =
            CalendarUtils.dateStringToCalendarDate(arrivalDateTimeString,
                FLIGHTS_DISPLAY_CALENDAR_FORMAT);
      } catch (ParseException e) {
        Log.error(driver, testId, "Unable to parse arrival date time in flightSerial " + i + e.getMessage());
        return false;
      }

      // Checking if the flight is the last flight of the sector
      // if (i != endIndex) {
      // PageElement layoverObject = travellersInput
      // .modifyPageElementOnce("layover", Integer.toString(i));
      // WebElement layoverElement = travellersInput
      // .getElementByPageObject(layoverObject);
      // String layover = layoverElement.getText();
      // }

      FlightDetails flight = flights.get(i - startIndex);
      String expectedSource = flight.getSourceCity();

      CustomAssert.assertTrue(testId, StringUtils.equalsIgnoreCase(expectedSource, source),
          "Error in validating source city :: Expected: " + expectedSource + " Actual: " + source);

      String expectedDestination = flight.getDestinationCity();

      CustomAssert.assertTrue(testId,
          StringUtils.equalsIgnoreCase(expectedDestination, destination),
          "Error in validating  destination city :: Expected: " + expectedDestination + " Actual: "
              + destination);

      String expectedFlightName = flight.getName();
      CustomAssert.assertTrue(testId, StringUtils.equalsIgnoreCase(expectedFlightName, flightName),
          "Error in validating destination name:: Expected: " + expectedFlightName + " Actual: "
              + flightName);

      String expectedFlightCode = flight.getCode();
      CustomAssert.assertEquals(driver, testId, expectedFlightCode, flightCode,
          "Error in validating  flight code:: Expected: " + expectedFlightCode + " Actual: "
              + flightCode);

      Calendar expectedDepartDate = flight.getDeparture();

      CustomAssert.assertTrue(
          testId,
          CalendarUtils.compareCalenderIgnoringYear(departureDateTime, expectedDepartDate),
          "Error in validating departure date time :: Expected: "
              + CalendarUtils.getFormattedDateTimeExcludingYear(expectedDepartDate) + " Actual: "
              + CalendarUtils.getFormattedDateTimeExcludingYear(departureDateTime));

      Calendar expectedArrivalDate = flight.getArrival();

      CustomAssert.assertTrue(
          testId,
          CalendarUtils.compareCalenderIgnoringYear(arrivalDateTime, expectedArrivalDate),
          "Error in validating departure date time :: Expected: "
              + CalendarUtils.getFormattedDateTimeExcludingYear(expectedArrivalDate) + " Actual: "
              + CalendarUtils.getFormattedDateTimeExcludingYear(arrivalDateTime));

      return true;
    }

    return true;
  }

  private static String separateAirportCodeWithHyphen(String airportCode) {
    String[] airportCodeArray = StringUtils.split(airportCode);
    return StringUtils.join(airportCodeArray, "-");
  }

  // String Format -> #Count Flights
  private static int getFlightsCountFromString(String countString) {
    List<String> countStringList = StringUtilities.split(countString, Constant.WHITESPACE);
    if (countStringList.size() < 2) {
      return -1;
    }
    String count = countStringList.get(0);
    int flightsCount = Integer.parseInt(count);
    return flightsCount;
  }

}
