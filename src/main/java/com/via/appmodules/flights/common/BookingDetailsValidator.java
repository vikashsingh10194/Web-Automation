package com.via.appmodules.flights.common;

import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.AllArgsConstructor;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.via.pageobjects.flights.common.BookingDetailsElements;
import com.via.pageobjects.flights.common.FlightBookingDetails;
import com.via.pageobjects.flights.common.FlightDetails;
import com.via.pageobjects.flights.common.SSR;
import com.via.pageobjects.flights.common.TravellerDetails;
import com.via.utils.CalendarUtils;
import com.via.utils.Constant;
import com.via.utils.Constant.Flight;
import com.via.utils.Constant.Journey;
import com.via.utils.Constant.VIA_COUNTRY;
import com.via.utils.CustomAssert;
import com.via.utils.EntityMap;
import com.via.utils.Log;
import com.via.utils.NumberUtility;
import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;
import com.via.utils.StringUtilities;

@AllArgsConstructor
public class BookingDetailsValidator {
  private String testId;
  private VIA_COUNTRY countryCode;
  private Flight flightType;
  private WebDriver driver;
  private RepositoryParser repositoryParser;
  private FlightBookingDetails flightBookingDetails;
  private List<TravellerDetails> travellerDetailsList;

  private final String JOURNEY_DATE_FORMAT = "MMM dd, yyyy";
  // Date format for FLIGHT_CAL_FORMAT has been changed in Booking Details web page.
  private final String FLIGHT_CAL_FORMAT = "EEE dd MMM yyyy, HH:mm";
  private final String CHILD = "Child";
  private final String SRC = "Source";
  private final String DEST = "Destination";
  private final String BLANK = "-";
  private final String NO_BAGGAGE = "No Baggage";
  private final String NO_MEAL = "No Meal";

  public void execute() throws ParseException {
    BookingDetailsElements bookingElements =
        new BookingDetailsElements(testId, driver, repositoryParser);

    PageHandler.waitForDomLoad(testId, driver);

    Log.info(testId, "::::::::::::       Booking Details Page       :::::::::::");
    Log.divider(testId);
    validateJourneyDescription(bookingElements);
    validateFlightDetails(bookingElements.getOnwardDiv(), bookingElements,
        flightBookingDetails.getOnwardFlights());

    if (flightBookingDetails.getJourneyType().equals(Journey.ROUND_TRIP)) {
      validateFlightDetails(bookingElements.getReturnDiv(), bookingElements,
          flightBookingDetails.getReturnFlights());
    }

    validatePassengerDetails(bookingElements);
    validatePaymentDetails(bookingElements);

    Log.info(testId, "Booking details validated successfully");
    Log.divider(testId);
  }

  private void validatePaymentDetails(BookingDetailsElements bookingElements) {
    List<WebElement> paymentDetailsList = bookingElements.getPaymentDetails();

    Double total = 0.0;
    int pCount = 1;
    for (; pCount < paymentDetailsList.size() - 1; pCount++) {
      String text = paymentDetailsList.get(pCount).getText();
      Double amount = NumberUtility.getAmountFromString(countryCode, text);
      if (amount == 0) {
        continue;
      }
      if (text.charAt(0) == '-') {
        total -= amount;
      } else {
        total += amount;
      }
    }

    Double totalAmount =
        NumberUtility.getAmountFromString(countryCode, paymentDetailsList.get(pCount).getText());

    boolean diffValidation = Math.abs(totalAmount - total) < 0.1;

    CustomAssert.assertTrue(testId, diffValidation, "Sum of amount not equal to total Amount.");

    diffValidation = Math.abs(totalAmount - flightBookingDetails.getTotalFare()) < 1;

    CustomAssert.assertTrue(testId, diffValidation,
        "Total payable amount not equal to total Amount.");
  }

  private boolean validatePassengerDetails(BookingDetailsElements bookingElements)
      throws ParseException {

    List<WebElement> travellerNameList = bookingElements.getTravellersName();
    List<WebElement> travellersDetailsList = bookingElements.getTravellersDetails();
    List<WebElement> travellersPassportList = bookingElements.getPassportDetails();

    Map<String, Map<String, SSR>> addOnsMap = getAddOnsMap(bookingElements);

    // Checks the total count of passengers
    CustomAssert.assertEquals(driver, testId, travellerNameList.size(),
        travellerDetailsList.size(), "Error in validating passengar count");

    boolean validation = true;

    // Loops through the passenger list
    for (int tCount = 0; tCount < travellerDetailsList.size(); tCount++) {

      TravellerDetails travellerDetails = travellerDetailsList.get(tCount);

      List<String> travellerOtherDetails =
          StringUtilities.split(travellersDetailsList.get(tCount).getText(), "[\\(\\)]");
      String fullName = travellerDetails.getName();
      textValidation("Traveller Name", fullName, travellerNameList.get(tCount).getText());
      textValidation("Traveller Type", travellerDetails.getType().toString(),
          travellerOtherDetails.get(0));

      Calendar dob = travellerDetails.getBirthCalender();
      if (dob != null) {
        dateValidation("DOB", dob, travellerOtherDetails.get(1));
      }
      if (flightType == Flight.INTERNATIONAL) {
        List<String> passportDetails =
            StringUtilities.split(travellersPassportList.get(tCount).getText(), Constant.COMMA);
        if(passportDetails.size()>1 && travellerDetails.getPassportNo()!=null){
          textValidation("Passport No.", travellerDetails.getPassportNo(), passportDetails.get(0));
          textValidation("Nationality", travellerDetails.getCountry(), passportDetails.get(3));
          dateValidation("Passport Expiry Date", travellerDetails.getPassportExpDate(),
              passportDetails.get(1) + ", " + passportDetails.get(2));
        }else if(passportDetails.size() == 0 && travellerDetails.getPassportNo()!=null){
          Log.error(driver, testId, "Passport details are not validated");
        }
        else{
          Log.info(testId, "Nothing is available to verify in passport details");
        }
      }

      Map<String, SSR> ssrDetails = travellerDetails.getSsrDetails();
      CustomAssert.assertTrue(validateSSRDetails(ssrDetails, addOnsMap.get(fullName)),
          "SSR Details not validated for traveller " + fullName);
    }

    return validation;
  }

  private void validateFlightDetails(WebElement flightDiv, BookingDetailsElements bookingElements,
      List<FlightDetails> flightList) throws ParseException {
    List<WebElement> airlineNames = bookingElements.getAirlineName(flightDiv);
    List<WebElement> flightNos = bookingElements.getFlightNo(flightDiv);
    List<WebElement> airportCodes = bookingElements.getAirportCode(flightDiv);
    List<WebElement> cityNames = bookingElements.getCityName(flightDiv);

    List<WebElement> dates = bookingElements.getDates(flightDiv);

    int totalFlights = flightList.size();
    int cityCount = 0;

    for (int flightCount = 0; flightCount < totalFlights; flightCount++) {
      FlightDetails flightDetails = flightList.get(flightCount);
      textValidation("Airline", flightDetails.getName(), airlineNames.get(flightCount).getText());
      textValidation("Flight No", flightDetails.getCode(), flightNos.get(flightCount).getText());
      textValidation("Source City Code", flightDetails.getSourceCityCode(),
          airportCodes.get(cityCount).getText());
      textValidation("Source City Name", flightDetails.getSourceCity(), cityNames.get(cityCount)
          .getText());
      dateTimeExceptYearValidation("Departure Date Time", flightDetails.getDeparture(),
          dates.get(cityCount).getText());
      textValidation("Destination City Code", flightDetails.getDestinationCityCode(), airportCodes
          .get(++cityCount).getText());
      textValidation("Destination City Name", flightDetails.getDestinationCity(),
          cityNames.get(cityCount).getText());
      dateTimeExceptYearValidation("Arrival Date Time", flightDetails.getArrival(),
          dates.get(cityCount).getText());
      cityCount++;
    }
  }

  private boolean validateSSRDetails(Map<String, SSR> expectedSsrDetails,
      Map<String, SSR> actualSsrDetails) {

    if (expectedSsrDetails.size() == 0 && actualSsrDetails == null) {
      return true;
    }
    if (expectedSsrDetails.size() != 0 && actualSsrDetails == null) {
      return false;
    }

    return verifyAddOns(expectedSsrDetails, actualSsrDetails);
  }

  private boolean verifyAddOns(Map<String, SSR> expectedSsrDetails,
      Map<String, SSR> actualSsrDetails) {
    for (Entry<String, SSR> entry : expectedSsrDetails.entrySet()) {
      String flightNo = entry.getKey();
      SSR expectedSSR = entry.getValue();
      SSR actualSSR = actualSsrDetails.get(flightNo);

      if (expectedSSR == null && actualSSR == null) {
        continue;
      }

      if (actualSSR == null) {
        actualSSR = new SSR();
      }

      Double expectedBaggage = 0.0;

      if (StringUtils.isNotBlank(expectedSSR.getBaggage())) {
        expectedBaggage =
            NumberUtility.getAmountFromString(StringUtilities.split(expectedSSR.getBaggage(),
                " at ").get(0));
      }

      Double actualBaggage = NumberUtility.getAmountFromString(actualSSR.getBaggage());

      if (!expectedBaggage.equals(actualBaggage)) {
        Log.error(driver, testId, "Baggage Details Not matched at AddOns.");
        return false;
      }

      String expectedMeal = expectedSSR.getMeal();
      String actualMeal = actualSSR.getMeal();

      if (StringUtils.isNotBlank(actualMeal)) {
        actualMeal = actualMeal.replaceAll(";", "");
      }

      if (!(StringUtils.containsIgnoreCase(expectedMeal, actualMeal)
          || (StringUtils.isBlank(expectedMeal) && StringUtils.isBlank(actualMeal))
          || (StringUtils.isBlank(expectedMeal) && StringUtils.containsIgnoreCase(actualMeal,
              NO_MEAL)) || StringUtils.isBlank(actualMeal)
          && StringUtils.equalsIgnoreCase(expectedMeal, NO_MEAL))) {
        Log.error(driver, testId, "Meal Not matched at AddOns.");
        return false;
      }

      if (!StringUtils.equalsIgnoreCase(expectedSSR.getSeatNo(), actualSSR.getSeatNo())) {
        Log.error(driver, testId, "Seat No Not matched at AddOns.");
        return false;
      }
    }

    return true;
  }

  private Map<String, Map<String, SSR>> getAddOnsMap(BookingDetailsElements bookingElements) {
    Map<String, Map<String, SSR>> addOnsMap = new HashMap<String, Map<String, SSR>>();

    List<WebElement> nameList = bookingElements.getSSRName();
    List<WebElement> mealList = bookingElements.getSSRMeal();
    List<WebElement> baggageList = bookingElements.getSSRBaggage();
    List<WebElement> seatList = bookingElements.getSSRSeat();

    String fltNo = null;

    for (int row = 0; row < nameList.size(); row++) {
      String name = nameList.get(row).getText();
      if (StringUtils.containsIgnoreCase(name, "Flight")) {
        fltNo = StringUtilities.split(name, Constant.COLON).get(1);
        continue;
      }

      String seat = seatList.get(row).getText();
      String baggage = baggageList.get(row).getText();
      String meal = mealList.get(row).getText();
      Map<String, SSR> ssrDetails = addOnsMap.get(name);

      if (ssrDetails == null) {
        ssrDetails = new HashMap<String, SSR>();
      }

      SSR ssr = new SSR();
      if (!StringUtils.equals(seat, BLANK)) {
        ssr.setSeatNo(StringUtils.trimToEmpty(seat));
      }

      if (!StringUtils.equals(baggage, BLANK)) {
        ssr.setBaggage(baggage);
      } else {
        ssr.setBaggage(NO_BAGGAGE);
      }

      if (!StringUtils.equals(meal, BLANK)) {
        ssr.setMeal(meal);
      } else {
        ssr.setMeal(NO_MEAL);
      }

      ssrDetails.put(fltNo, ssr);

      addOnsMap.put(name, ssrDetails);
    }

    return addOnsMap;
  }

  private void dateTimeExceptYearValidation(String calendarType, Calendar expected,
      String actualCalString) throws ParseException {
    Calendar actual = null;
    actual =
        CalendarUtils.dateStringToCalendarDate(StringUtils.trimToEmpty(actualCalString),
            FLIGHT_CAL_FORMAT);

    CustomAssert.assertTrue(testId, CalendarUtils.compareCalenderIgnoringYear(expected, actual),
        calendarType + " didn't match. Expected: " + CalendarUtils.getFormattedDateTime(expected)
            + " Actual: " + CalendarUtils.getFormattedDateTime(actual));
  }

  private void validateJourneyDescription(BookingDetailsElements bookingElements)
      throws ParseException {

    Log.info(testId, "----------    Journey Description Validation   -----------");

    textValidation(SRC, flightBookingDetails.getSourceCity(), bookingElements.getSource().getText());
    textValidation(DEST, flightBookingDetails.getDestinationCity(), bookingElements
        .getDestination().getText());

    Journey expectedJourney = flightBookingDetails.getJourneyType();

    String journeyText = bookingElements.getJourneyType().getText();
    Journey actualJourney =
        StringUtils.equalsIgnoreCase(journeyText, "Round trip") ? Journey.ROUND_TRIP
            : Journey.ONE_WAY;

    CustomAssert.assertEquals(driver, testId, expectedJourney, actualJourney,
        "Journey Type didn't match. Expected: " + expectedJourney + " Actual: " + actualJourney);

    List<String> journeyDates =
        StringUtilities.split(bookingElements.getJourneyDates().getText(), Constant.SLASH);

    dateValidation("Onward Date", flightBookingDetails.getOnwardDate(), journeyDates.get(0));

    if (expectedJourney.equals(Journey.ROUND_TRIP)) {
      dateValidation("Return Date", flightBookingDetails.getReturnDate(), journeyDates.get(1));
    }

    List<String> passengerCount =
        StringUtilities.split(bookingElements.getPassengerCount().getText(), Constant.COMMA);

    int actualAdult =
        NumberUtils.toInt(StringUtilities.split(passengerCount.get(0), Constant.WHITESPACE).get(0));
    int actualChildren = 0;
    int actualInfant = 0;

    if (passengerCount.size() > 1) {
      List<String> details =
          StringUtilities
              .split(StringUtils.trimToEmpty(passengerCount.get(1)), Constant.WHITESPACE);
      if (StringUtils.equalsIgnoreCase(details.get(1), CHILD)) {
        actualChildren = NumberUtils.toInt(details.get(0));
      } else {
        actualInfant = NumberUtils.toInt(details.get(0));
      }

      if (passengerCount.size() > 2) {
        actualInfant =
            NumberUtils.toInt(StringUtilities.split(StringUtils.trimToEmpty(passengerCount.get(2)),
                Constant.WHITESPACE).get(0));
      }
    }
    int expectedCount = flightBookingDetails.getAdultsCount();
    CustomAssert.assertEquals(driver, testId, expectedCount, actualAdult,
        "Adult Count didn't match. Expected: " + expectedCount + " Actual: " + actualAdult);

    expectedCount = flightBookingDetails.getChildrenCount();
    CustomAssert.assertEquals(driver, testId, expectedCount, actualChildren,
        "Children Count didn't match. Expected: " + expectedCount + " Actual: " + actualChildren);

    expectedCount = flightBookingDetails.getInfantsCount();
    CustomAssert.assertEquals(driver, testId, expectedCount, actualInfant,
        "Infant Count didn't match. Expected: " + expectedCount + " Actual: " + actualInfant);

    String expectedBookingId = flightBookingDetails.getBookingId();
    String actualBookingId = bookingElements.getBookingId().getText();

    if (StringUtils.isBlank(expectedBookingId)) {
      expectedBookingId = actualBookingId;
      flightBookingDetails.setBookingId(actualBookingId);
    }

    CustomAssert.assertTrue(testId,
        StringUtils.equalsIgnoreCase(expectedBookingId, actualBookingId),
        "Booking Id didn't match. Expected: " + expectedBookingId + " Actual: " + actualBookingId);

  }

  private void textValidation(String type, String expected, String actual) {
    actual = EntityMap.getCityNameFromAirportName(actual);
    CustomAssert.assertTrue(testId, StringUtils.equalsIgnoreCase(expected, actual), type
        + " didn't Match. Expected: " + expected + " Actual: " + actual);
  }

  private void dateValidation(String type, Calendar expectedDate, String actualDateString)
      throws ParseException {
    Calendar actualDate = null;
    actualDate = CalendarUtils.dateStringToCalendarDate(actualDateString, JOURNEY_DATE_FORMAT);

    CustomAssert.assertTrue(testId, DateUtils.isSameDay(expectedDate, actualDate), type
        + " didn't Match");
  }

}
