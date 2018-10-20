package com.via.appmodules.flights.common;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.via.pageobjects.flights.common.FlightBookingDetails;
import com.via.pageobjects.flights.common.HeaderElements;
import com.via.pageobjects.flights.common.FlightBookingDetails;
import com.via.pageobjects.flights.common.NewHeaderElements;
import com.via.utils.CalendarUtils;
import com.via.utils.Constant;
import com.via.utils.Constant.Journey;
import com.via.utils.Constant.VIA_COUNTRY;
import com.via.utils.CustomAssert;
import com.via.utils.Log;
import com.via.utils.NumberUtility;
import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;

public class NewFlightHeaderValidator {
  private static final String FLIGHT_HEADER_PAGE_NAME = "flightResultHeader";
  private static final String NEW_TRAVELLER_HEADER_PAGE_NAME = "newTravellersHeader";
  private static final String NEW_ONELINER_SNAPSHOT_CALENDAR_FORMAT = "dd MMM yy";
  private static final String FLIGHT_DATE_FORMAT = "EEE, MMM dd yyyy";

  public static boolean validateFlightsOneLinerSnapshot(WebDriver driver,
      RepositoryParser repositoryParser, FlightBookingDetails flightBookingDetails) {
    boolean flag = true;

    String testId = flightBookingDetails.getTestCaseId();

    Log.info(testId, "-----------        Search Page Header Validation      ------------");
    HeaderElements headerElements = new HeaderElements(testId, driver, repositoryParser);

    PageHandler.waitForPageLoad(testId, driver);

    String[] sourceDetails =
        headerElements.getSourceDetails(FLIGHT_HEADER_PAGE_NAME).getText()
            .split(Constant.WHITESPACE, 2);

    /*** validating source city and source city code ***/
    String city = sourceDetails[1];
    String searchedCity = flightBookingDetails.getSourceCity();
    if (!StringUtils.equalsIgnoreCase(city, searchedCity)) {
      Log.error(driver, testId, "Source City didn't Match :: Expected: " + searchedCity + " Actual: "
          + city);
      flag = false;
    }

    String cityCode = sourceDetails[0];
    String searchedCityCode = flightBookingDetails.getSourceCityCode();
    if (!StringUtils.equalsIgnoreCase(cityCode, searchedCityCode)) {
      Log.error(driver, testId, "Source City Code didn't Match :: Expected: " + searchedCityCode
          + " Actual: " + cityCode);
      flag = false;
    }

    /*** validating destination city and code ***/
    String[] destinationDetails =
        headerElements.getDestinationDetails(FLIGHT_HEADER_PAGE_NAME).getText()
            .split(Constant.WHITESPACE, 2);
    city = StringUtils.trimToEmpty(destinationDetails[1]);
    searchedCity = flightBookingDetails.getDestinationCity();
    if (!StringUtils.equalsIgnoreCase(city, searchedCity)) {
      Log.error(driver, testId, "Destination city didn't Match :: Expected: " + searchedCity + " Actual: "
          + city);
      flag = false;
    }

    cityCode = destinationDetails[0];
    searchedCityCode = flightBookingDetails.getDestinationCityCode();
    if (!StringUtils.equalsIgnoreCase(cityCode, searchedCityCode)) {
      Log.error(driver, testId, "Destination City Code didn't Match :: Expected: " + searchedCityCode
          + " Actual: " + cityCode);
      flag = false;
    }

    Calendar resultOnwardDate = null;
    try {
      resultOnwardDate =
          CalendarUtils.dateStringToCalendarDate(StringUtils.trimToEmpty(headerElements
              .getOnwardDate(FLIGHT_HEADER_PAGE_NAME).getText()), FLIGHT_DATE_FORMAT);
    } catch (ParseException e) {
      Log.warn(testId, "Onward date can't be parsed.");
      flag = false;
    }

    Calendar searchedOnwardDate = flightBookingDetails.getOnwardDate();

    if (!DateUtils.isSameDay(resultOnwardDate, searchedOnwardDate)) {
      Log.error(driver, 
          testId,
          "Onward date didn't match :: Expected: "
              + CalendarUtils.getFormattedDateTime(searchedOnwardDate) + " Actual: "
              + CalendarUtils.getFormattedDateTime(resultOnwardDate));
      flag = false;
    }

    int adultCount =
        NumberUtils.toInt(StringUtils.trimToEmpty(headerElements.getAdultsCount(
            FLIGHT_HEADER_PAGE_NAME).getText()));
    int searchedAdultCount = flightBookingDetails.getAdultsCount();
    if (adultCount != searchedAdultCount) {
      Log.error(driver, testId, "Adult count didn't match :: Expected: " + searchedAdultCount + " Actual: "
          + adultCount);
      flag = false;
    }

    int childCount =
        NumberUtils.toInt(StringUtils.trimToEmpty(headerElements.getChildrenCount(
            FLIGHT_HEADER_PAGE_NAME).getText()));
    int searchedChildCount = flightBookingDetails.getAdultsCount();
    if (adultCount != searchedAdultCount) {
      Log.error("Children count didn't match :: Expected: " + searchedChildCount + " Actual: "
          + childCount);
      flag = false;
    }

    int infantCount =
        NumberUtils.toInt(StringUtils.trimToEmpty(headerElements.getInfantsCount(
            FLIGHT_HEADER_PAGE_NAME).getText()));
    int searchedInfantCount = flightBookingDetails.getAdultsCount();
    if (adultCount != searchedAdultCount) {
      Log.error(driver, testId, "Infant count didn't match :: Expected: " + searchedInfantCount
          + " Actual: " + infantCount);
      flag = false;
    }

    if (flightBookingDetails.getJourneyType() == Journey.ROUND_TRIP) {
      Calendar resultReturnDate = null;
      try {
        resultReturnDate =
            CalendarUtils.dateStringToCalendarDate(StringUtils.trimToEmpty(headerElements
                .getReturnDate(FLIGHT_HEADER_PAGE_NAME).getText()), FLIGHT_DATE_FORMAT);
      } catch (ParseException e) {
        Log.error(driver, testId, "Return date can't be parsed.");
        flag = false;
      }

      Calendar searchedReturnDate = flightBookingDetails.getReturnDate();
      if (!DateUtils.isSameDay(resultReturnDate, searchedReturnDate)) {
        Log.error(driver, 
            testId,
            "Return date didn't match :: Expected: "
                + CalendarUtils.getFormattedDateTime(searchedReturnDate) + " Actual: "
                + CalendarUtils.getFormattedDateTime(resultReturnDate));
        flag = false;
      }
    }

    return flag;
  }

  /*
   * Validates TDP Header.
   */
  public static boolean newValidateTravellersOneLinerSnapshot(VIA_COUNTRY countryCode,
      WebDriver driver, RepositoryParser repositoryParser, FlightBookingDetails flightBookingDetails) {

    String testId = flightBookingDetails.getTestCaseId();

    NewHeaderElements headerElements = new NewHeaderElements(testId, driver, repositoryParser);

    // Validates source city code
    List<WebElement> onwardSrcDestCityCode =
        headerElements.getOnwardSrcDestCityCode(NEW_TRAVELLER_HEADER_PAGE_NAME);
    String onwardSrcCityCode = onwardSrcDestCityCode.get(0).getText();
    String actualSourceCityCode = flightBookingDetails.getSourceCityCode();
    CustomAssert.assertEquals(driver, testId, onwardSrcCityCode, actualSourceCityCode,
        "Error in validating src city code:: Expected: " + actualSourceCityCode + " Acutal: "
            + onwardSrcCityCode);

    // Validated destination city code
    String onwardDestCityCode = onwardSrcDestCityCode.get(1).getText();
    String acutalDestCityCode = flightBookingDetails.getDestinationCityCode();
    CustomAssert.assertEquals(driver, testId, onwardDestCityCode, onwardDestCityCode,
        "Error in validating dest city code :: Expected: " + acutalDestCityCode + " Actual: "
            + onwardDestCityCode);

    Calendar onwardJourneyDate = null;
    Calendar returnJourneyDate = null;

    List<WebElement> flightHeaderShortSnap =
        headerElements.flightHeaderShortSnap(NEW_TRAVELLER_HEADER_PAGE_NAME);

    // Validates onward journey date
    String onwardDate = flightHeaderShortSnap.get(0).getText();
    try {
      onwardJourneyDate =
          CalendarUtils.dateStringToCalendarDate(onwardDate, NEW_ONELINER_SNAPSHOT_CALENDAR_FORMAT);
    } catch (ParseException e) {
      Log.error(driver, testId, "Error parsing onward journey date " + e.getMessage());
      return false;
    }
    Calendar expectedOnwardDate = flightBookingDetails.getOnwardDate();
    CustomAssert.assertTrue(
        testId,
        DateUtils.isSameDay(onwardJourneyDate, expectedOnwardDate),
        "Error in validating onward journey date :: Expected: "
            + CalendarUtils.getFormattedDate(expectedOnwardDate) + " Actual: "
            + CalendarUtils.getFormattedDate(onwardJourneyDate));

    // Validates return date
    if (flightBookingDetails.getJourneyType() == Journey.ROUND_TRIP) {
      String returnDate = flightHeaderShortSnap.get(1).getText();
      try {
        returnJourneyDate =
            CalendarUtils.dateStringToCalendarDate(returnDate,
                NEW_ONELINER_SNAPSHOT_CALENDAR_FORMAT);
      } catch (ParseException e) {
        Log.error(driver, testId, "Error parsing return journey date " + e.getMessage());
        return false;
      }
      expectedOnwardDate = flightBookingDetails.getReturnDate();
      CustomAssert.assertTrue(
          testId,
          DateUtils.isSameDay(returnJourneyDate, expectedOnwardDate),
          "Error in validating return journey date :: Expected: "
              + CalendarUtils.getFormattedDate(expectedOnwardDate) + " Actual: "
              + CalendarUtils.getFormattedDate(returnJourneyDate));

    }

    // Validate Passenger Counts.
    List<String> travellersCount;
    if (flightBookingDetails.getJourneyType() == Journey.ROUND_TRIP) {
      travellersCount =
          Arrays.asList(flightHeaderShortSnap.get(2).getText().replace(" ", "").split(Constant.COMMA));
    }else{
      travellersCount =
          Arrays.asList(flightHeaderShortSnap.get(1).getText().replace(" ", "").split(Constant.COMMA));
    }
    
    int adults = NumberUtils.toInt("" + travellersCount.get(0).charAt(0));
    int expectedAdults = flightBookingDetails.getAdultsCount();
    CustomAssert.assertEquals(driver, testId, adults, expectedAdults,
        "Error in validating adults count :: Expected: " + expectedAdults + " Actual: " + adults);

    int children = NumberUtils.toInt("" + travellersCount.get(1).charAt(0));
    int expectedChildren = flightBookingDetails.getChildrenCount();
    CustomAssert.assertEquals(driver, testId, children, expectedChildren,
        "Error in validating children count :: Expected: " + expectedChildren + " Acutal: "
            + children);

    int infants = Integer.parseInt("" + travellersCount.get(2).charAt(0));
    int expectedInfants = flightBookingDetails.getInfantsCount();
    CustomAssert
        .assertEquals(testId, infants, expectedInfants,
            "Error in validating infants count :: Expected: " + expectedInfants + " Actual: "
                + infants);

    // Validates Total fare.
    double completeFare = 0.0;
    
    //If insurance is selected already,Then total fare is now with insurance charge.
    try {
      WebElement insTermsCondition = headerElements.insTermsCondition(NEW_TRAVELLER_HEADER_PAGE_NAME);
      if (insTermsCondition.isSelected()) {
        PageHandler.javaScriptExecuterClick(driver, insTermsCondition);
        if (flightBookingDetails.getJourneyType() == Journey.ROUND_TRIP) {
          completeFare = NumberUtility.getAmountFromString(countryCode, flightHeaderShortSnap.get(3).getText());
        }else{
          completeFare = NumberUtility.getAmountFromString(countryCode, flightHeaderShortSnap.get(2).getText());
        }
        
      }else{
        if (flightBookingDetails.getJourneyType() == Journey.ROUND_TRIP) {
          completeFare = NumberUtility.getAmountFromString(countryCode, flightHeaderShortSnap.get(3).getText());
        }else{
          completeFare = NumberUtility.getAmountFromString(countryCode, flightHeaderShortSnap.get(2).getText());
        }
      }
    } catch (Exception e) {
      Log.info(testId,"Insurance is not available for this sector");
      if (flightBookingDetails.getJourneyType() == Journey.ROUND_TRIP) {
        completeFare = NumberUtility.getAmountFromString(countryCode, flightHeaderShortSnap.get(3).getText());
      }else{
        completeFare = NumberUtility.getAmountFromString(countryCode, flightHeaderShortSnap.get(2).getText());
      }
    }
    double totalFare = flightBookingDetails.getTotalFare();
    CustomAssert.assertEquals(driver, testId, completeFare, totalFare,
        "Error in validating total fare :: Expected: " + totalFare + " Acutal: " + completeFare);

    Log.info(testId,
        "Total Fare at TDP Header: " + NumberUtility.getRoundedAmount(countryCode, totalFare));

    return true;
  }
}
