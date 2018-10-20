package com.via.appmodules.flights.common;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;

import com.via.pageobjects.flights.common.FlightBookingDetails;
import com.via.pageobjects.flights.common.FlightDetails;
import com.via.pageobjects.flights.common.NewTravellersPageElements;
import com.via.utils.CalendarUtils;
import com.via.utils.Constant;
import com.via.utils.CustomAssert;
import com.via.utils.EntityMap;
import com.via.utils.Log;
import com.via.utils.StringUtilities;

public class NewTravellersPageValidator {

  private static final String NEW_FLIGHTS_DISPLAY_CALENDAR_FORMAT = "dd MMM yyyy, hh:mm";
  private WebDriver driver ;
  
  NewTravellersPageValidator(WebDriver driver){
    this.driver = driver;
  }
  /*
   * Validates onward journey.
   */
  public boolean newValidateOnwardJourney(NewTravellersPageElements travellersInput,
      FlightBookingDetails flightBookingDetails) {
    String testId = flightBookingDetails.getTestCaseId();
    String onwardSection = Integer.toString(1);

    String onwardFlightStopsCountString = travellersInput.getOnwardFlightsStopsAndCount().getText();
    List<String> flightStopsAndCount =
        Arrays.asList(onwardFlightStopsCountString.split(Constant.COMMA));
    String onwardFlightStopsString = flightStopsAndCount.get(0);
    String onwardFlightCountString = flightStopsAndCount.get(1);
    int onwardFlightStops = getDigitCountFromString(onwardFlightStopsString);
    int onwardFlightCount = getDigitCountFromString(onwardFlightCountString);
    if (onwardFlightCount == -1) {
      return false;
    }

    List<FlightDetails> onwardFlights = flightBookingDetails.getOnwardFlights();
    CustomAssert.assertEquals(driver, testId, onwardFlightCount, onwardFlights.size(),
        "Onward flights count error :: Expected: " + onwardFlights.size() + " Actual: "
            + onwardFlightCount);

    // OnwardStops are not set up by flightBookingDetails
    // List<FlightDetails> onwardStops = flightBookingDetails.getOnwardStops();
    // CustomAssert.assertEquals(driver, testId, onwardFlightStops, onwardStops.size(),
    // "Onward flights count error :: Expected: " + onwardStops.size() + " Actual: "
    // + onwardFlightStops);

    return validateJourney(testId, travellersInput, onwardFlights, onwardSection, onwardFlightCount);
  }

  /*
   * Validates return journey.
   */
  public boolean newValidateReturnJourney(NewTravellersPageElements travellersInput,
      FlightBookingDetails flightBookingDetails) {

    String testId = flightBookingDetails.getTestCaseId();
    String returnSection = Integer.toString(2);

    String returnFlightStopsCountString = travellersInput.getReturnFlightsStopsAndCount().getText();
    List<String> flightStopsAndCount =
        Arrays.asList(returnFlightStopsCountString.split(Constant.COMMA));
    String returnFlightStopsString = flightStopsAndCount.get(0);
    String returnFlightCountString = flightStopsAndCount.get(1);
    int returnFlightStops = getDigitCountFromString(returnFlightStopsString);
    int returnFlightCount = getDigitCountFromString(returnFlightCountString);
    if (returnFlightCount == -1) {
      return false;
    }

    List<FlightDetails> onwardFlights = flightBookingDetails.getOnwardFlights();
    List<FlightDetails> returnFlights = flightBookingDetails.getReturnFlights();
    CustomAssert.assertEquals(driver, testId, returnFlightCount, returnFlights.size(),
        "Onward flights count error :: Expected: " + returnFlights.size() + " Actual: "
            + returnFlightCount);

    // ReturnStops are not set up by flightBookingDetails
    // List<FlightDetails> returnStops = flightBookingDetails.getOnwardStops();
    // CustomAssert.assertEquals(driver, testId, returnFlightStops, returnStops.size(),
    // "Onward flights count error :: Expected: " + returnStops.size() + " Actual: "
    // + returnFlightStops);


    return validateJourney(testId, travellersInput, returnFlights, returnSection, returnFlightCount);
  }

  // Start index and end index represents the index of onward journey or
  // return journey from overall journey flights
  private boolean validateJourney(String testId, NewTravellersPageElements travellersInput,
      List<FlightDetails> flights, String section, int flightCount) {
    int flightIndex = 0;
    String[] toModify = new String[2];
    // Validates all the flights occurring in the sector
    for (int flightSection = 2; flightSection <= 2 * flightCount; flightSection = flightSection * 2) {
      if (StringUtils.equals(section, "1")) {
        toModify[0] = section;
        toModify[1] = Integer.toString(flightSection);
      } else {
        toModify[0] = section;
        toModify[1] = Integer.toString(flightSection);
      }
      String src = EntityMap.getCityNameFromAirportName(travellersInput.src(toModify));
      String dest = EntityMap.getCityNameFromAirportName(travellersInput.dest(toModify));
      String flightName = travellersInput.flightName(toModify);
      String flightCode = travellersInput.flightCode(toModify);
      String departureTime = travellersInput.departureTime(toModify);
      String arrivalTime = travellersInput.arrivalTime(toModify);
      String departureDate = travellersInput.departureDate(toModify);
      String arrivalDate = travellersInput.arrivalDate(toModify);
      Calendar arrivalDateTime = null;
      try {
        arrivalDateTime =
            CalendarUtils.dateStringToCalendarDate(arrivalDate + ", " + arrivalTime,
                NEW_FLIGHTS_DISPLAY_CALENDAR_FORMAT);
      } catch (ParseException e) {
        Log.error(driver, testId, "Unable to parse arrival date time in flightSerial " + e.getMessage());
        return false;
      }

      Calendar departureDateTime = null;
      try {
        departureDateTime =
            CalendarUtils.dateStringToCalendarDate(departureDate + ", " + departureTime,
                NEW_FLIGHTS_DISPLAY_CALENDAR_FORMAT);
      } catch (ParseException e) {
        Log.error(driver, testId, "Unable to parse arrival date time in flightSerial " + e.getMessage());
        return false;
      }


      FlightDetails flight = flights.get(flightIndex++);

      String expectedSource = flight.getSourceCity();
      CustomAssert.assertTrue(testId, StringUtils.equalsIgnoreCase(expectedSource, src),
          "Error in validating source city :: Expected: " + expectedSource + " Actual: " + src);

      String expectedDest = flight.getDestinationCity();
      CustomAssert.assertTrue(testId, StringUtils.equalsIgnoreCase(expectedDest, dest),
          "Error in validating source city :: Expected: " + expectedDest + " Actual: " + dest);

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
    }
    return true;
  }

  // String Format -> #Count Flights
  private static int getDigitCountFromString(String countString) {
    List<String> countStringList = StringUtilities.split(countString, Constant.WHITESPACE);
    if (countStringList.size() < 2) {
      return -1;
    }
    String count = countStringList.get(0);
    int flightsCount = Integer.parseInt(count);
    return flightsCount;
  }

}
