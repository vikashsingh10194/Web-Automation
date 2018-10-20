package com.via.appmodules.flights.domestic;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.via.pageobjects.flights.common.FareDetails;
import com.via.pageobjects.flights.common.FlightBookingDetails;
import com.via.pageobjects.flights.common.FlightDetails;
import com.via.pageobjects.flights.domestic.FlightResultContainerElements;
import com.via.utils.CalendarUtils;
import com.via.utils.Constant;
import com.via.utils.Constant.BOOKING_MEDIA;
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
public class FlightSelector {
  private String testId;
  private VIA_COUNTRY countryCode;
  private BOOKING_MEDIA media;
  private WebDriver driver;
  private RepositoryParser repositoryParser;

  private final String DATE_FORMAT = "dd MMM, yyyy HH:mm";

  public final String ONEWAY = "resultSet domestic";
  public final String ROUNDTRIP_ONWARD = "onwardResults";
  public final String ROUNDTRIP_RETURN = "returnResults";
  public final String ROUNDTRIP_ONWARD_BOOK = "onwardFlight";
  public final String ROUNDTRIP_RETURN_BOOK = "returnFlight";

  public FlightBookingDetails flightSelection(FlightResultContainerElements flightSearchElement,
      FlightBookingDetails flightBookingDetails) {

    Log.info(testId, "---------------        Flight Selection        ---------------");
    Log.divider(testId);

    /***
     * get flight no and name in a list which have to select in onward journey
     ***/
    List<String> onwardFlightDetails =
        StringUtilities.getFlightNameAndNo(flightBookingDetails.getOnwardFlightName());

    /***
     * store onward and return flight details in lists, return flight list will be null if journey
     * is oneway.
     ***/
    List<FlightDetails> onwardFlightList = null;
    List<FlightDetails> returnFlightList = null;

    /*** journey type == oneway ***/
    if (flightBookingDetails.getJourneyType() == Journey.ONE_WAY) {

      Log.info(testId, "----------------     ONEWAY FLIGHT DETAILS     ---------------");

      /***
       * get one way journey flight name and no mentioned at excel sheet
       ***/
      String flightDesc = onwardFlightDetails.get(0);
      String flightName = EntityMap.getAirlineCode(flightDesc);
      String flightNo = onwardFlightDetails.get(1);

      /*** get flightDetails menu button and click on it. ***/

      WebElement flightDetailsDiv =
          flightSearchElement.getFlightCompleteDiv(ONEWAY, flightName, flightNo);

      CustomAssert.assertTrue(testId, flightDetailsDiv != null,
          "One way Flight with description as  " + flightDesc + " not found.");

      /***
       * get flight details, baggage information and fare rules of selected flight
       ***/
      onwardFlightList =
          oneWayFlightSelection(flightDetailsDiv, flightBookingDetails, flightSearchElement);

      /*** get fare details one way journey ***/
      if (flightBookingDetails.getOnwardFlightName().equalsIgnoreCase("Garuda")) {
        PageHandler.sleep(testId, 40 * 1000L);
      }
      FareDetails fareDetails =
          getFareDetails(flightDetailsDiv, flightSearchElement, onwardFlightList,
              flightBookingDetails);

      flightBookingDetails.setOnwardFareDetails(fareDetails);
      flightBookingDetails.setTotalFare(fareDetails.getTotalFare());

      /*** click on book button ***/
      clickOnBookButton(flightDetailsDiv, flightSearchElement);
      Log.info(testId, "Book button clicked in oneway journey.");
      Log.divider(testId);
    }
    /*** journey type == round trip ***/
    else if (flightBookingDetails.getJourneyType() == Journey.ROUND_TRIP) {

      Log.info(testId, "--------------       ONWARD FLIGHT DETAILS       -------------");

      /*** get onward flight name and no mentioned in excel sheet ***/
      String flightDesc = onwardFlightDetails.get(0);
      String flightName = EntityMap.getAirlineCode(flightDesc);
      String flightNo = onwardFlightDetails.get(1);

      /***
       * get flight details menu button of onward flight and click on it
       ***/
      WebElement flightDetailsDiv =
          flightSearchElement.getFlightCompleteDiv(ROUNDTRIP_ONWARD, flightName, flightNo);

      CustomAssert.assertTrue(testId, flightDetailsDiv != null,
          "Onward Flight with description as  " + flightDesc + " not found.");

      /***
       * get flight details, baggage information and fare rules of selected flight
       ***/
      // Waiting for some time if airline used is Garuda....
      if (flightBookingDetails.getOnwardFlightName().equalsIgnoreCase("Garuda")
          || flightBookingDetails.getReturnFlightName().equalsIgnoreCase("Garuda")) {
        PageHandler.sleep(testId, 40 * 1000L);
      }
      onwardFlightList =
          oneWayFlightSelection(flightDetailsDiv, flightBookingDetails, flightSearchElement);

      /*** get fare details for onward journey ***/

      FareDetails fareDetails =
          getFareDetails(flightDetailsDiv, flightSearchElement, onwardFlightList,
              flightBookingDetails);
      flightBookingDetails.setOnwardFareDetails(fareDetails);

      /*** store onward selected flight details in map ***/
      Map<String, String> selectedFlightDetails =
          getSelectedFlightDetails(flightDetailsDiv, flightSearchElement, fareDetails);

      clickOnBookButton(flightDetailsDiv, flightSearchElement);

      /*** validate the onward details to the details at book panel ***/
      WebElement selectedDiv = flightSearchElement.getFlightDivAtBookPanel(ROUNDTRIP_ONWARD_BOOK);
      FlightValidator flightValidator = new FlightValidator();
      boolean onwardValidationFlag =
          flightValidator.selectedFlightValidation(testId, countryCode, selectedDiv,
              flightSearchElement, selectedFlightDetails);

      Log.info(testId, "Onward flight Selection Panel Verification :: " + onwardValidationFlag);

      Log.divider(testId);

      Log.info(testId, "---------------      RETURN FLIGHT DETAILS      --------------");

      /***
       * get flight no and name in a list which have to select in return journey
       ***/

      flightDesc = flightBookingDetails.getReturnFlightName();
      List<String> returnFlightDetails = StringUtilities.getFlightNameAndNo(flightDesc);

      /*** fetch flightName and no from list ***/
      flightName = EntityMap.getAirlineCode(returnFlightDetails.get(0));
      flightNo = returnFlightDetails.get(1);

      /*** get flight complete div ***/

      flightDetailsDiv =
          flightSearchElement.getFlightCompleteDiv(ROUNDTRIP_RETURN, flightName, flightNo);

      CustomAssert.assertTrue(testId, flightDetailsDiv != null,
          "Return Flight with description as  " + flightDesc + " not found.");

      /***
       * get flight Details menu button to fetch flight baggage, flight details and fare rules in
       * list.
       ***/
      returnFlightList =
          oneWayFlightSelection(flightDetailsDiv, flightBookingDetails, flightSearchElement);

      /***
       * get return flight fare details and store in the FlightBookingDetails
       ***/
      fareDetails =
          getFareDetails(flightDetailsDiv, flightSearchElement, returnFlightList,
              flightBookingDetails);

      flightBookingDetails.setReturnFareDetails(fareDetails);

      /*** store return flight details in map ***/
      selectedFlightDetails =
          getSelectedFlightDetails(flightDetailsDiv, flightSearchElement, fareDetails);

      clickOnBookButton(flightDetailsDiv, flightSearchElement);

      /*** compare return flight details to the details at book panel ***/

      selectedDiv = flightSearchElement.getFlightDivAtBookPanel(ROUNDTRIP_RETURN_BOOK);

      boolean returnValidationFlag =
          flightValidator.selectedFlightValidation(testId, countryCode, selectedDiv,
              flightSearchElement, selectedFlightDetails);

      Log.info(testId, "Return flight Selection Panel Verification :: " + returnValidationFlag);
      Log.divider(testId);

      Log.info(testId, "-----------   Selection Panel Fare Verification    -----------");

      List<WebElement> grandTotalElements =
          flightSearchElement.getSelectedFlightsGrandTotal(testId);

      Double grandTotal =
          NumberUtility.getAmountFromString(countryCode,
              StringUtils.trimToEmpty(grandTotalElements.get(0).getText()));
      String grandTotalStr = NumberUtility.getRoundedAmount(countryCode, grandTotal);
      double payableGrandTotal = grandTotal;

      if (grandTotalElements.size() == 2) {
        payableGrandTotal =
            NumberUtility.getAmountFromString(countryCode,
                StringUtils.trimToEmpty(grandTotalElements.get(1).getText()));
      }
      /***
       * set validationFlag true if both onward and return book panel details same as details at
       * selection panel
       ***/
      boolean validationFlag = onwardValidationFlag && returnValidationFlag;

      /***
       * check sum of onward and return total fare is either equal to grandTotal or not.
       ***/

      boolean fareValidationFlag = true;

      String totalFare =
          NumberUtility.getRoundedAmount(countryCode, flightBookingDetails.getOnwardFareDetails()
              .getTotalFare() + flightBookingDetails.getReturnFareDetails().getTotalFare());
      if (!StringUtils.equals(totalFare, grandTotalStr)) {
        Log.info(testId, "Grand total amount didn't match. Expected :: " + totalFare
            + " Acutal :: " + grandTotalStr);
        fareValidationFlag = false;
      }

      Log.info(testId, "Grand Total Fare Verification: " + fareValidationFlag);

      validationFlag &= fareValidationFlag;

      /*** if validationFlag is not true then break. ***/

      CustomAssert.assertTrue(testId, validationFlag,
          "Selected flight (Roundtrip Journey) Validated.",
          "Selected flight (Roundtrip Journey) didn't Validate.");

      Log.info(testId, "Payable Grand Total: " + payableGrandTotal);
      flightBookingDetails.setTotalFare(payableGrandTotal);

      WebElement bookButton = flightSearchElement.getBookButton(testId);
      PageHandler.javaScriptExecuterClick(driver, bookButton);
    }

    flightBookingDetails.setOnwardFlights(onwardFlightList);
    flightBookingDetails.setReturnFlights(returnFlightList);

    Log.divider(testId);
    return flightBookingDetails;
  }

  /*** store all the details which has to validate at book panel ***/
  private Map<String, String> getSelectedFlightDetails(WebElement divElement,
      FlightResultContainerElements flightSearchElement, FareDetails fareDetails) {

    /*** flightBooking attribute = > flightValue ***/
    Map<String, String> selectedFlightDetails = new HashMap<String, String>();
    selectedFlightDetails.put("flightName",
        StringUtils.trimToEmpty(flightSearchElement.getFlightName(divElement).getText()));

    selectedFlightDetails.put("flightNo",
        StringUtils.trimToEmpty(flightSearchElement.getFlightNo(divElement).getText()));

    selectedFlightDetails.put("departCity",
        StringUtils.trimToEmpty(flightSearchElement.getDepartCity(divElement).getText()));

    selectedFlightDetails.put("departTime",
        StringUtils.trimToEmpty(flightSearchElement.getDepartureTime(divElement).getText()));

    selectedFlightDetails.put("arrivalCity",
        StringUtils.trimToEmpty(flightSearchElement.getArrivalCity(divElement).getText()));

    selectedFlightDetails.put("arrivalTime",
        StringUtils.trimToEmpty(flightSearchElement.getArrivalTime(divElement).getText()));

    selectedFlightDetails.put("fare", fareDetails.getTotalFare().toString());
    return selectedFlightDetails;
  }

  private void clickOnBookButton(WebElement divElement,
      FlightResultContainerElements flightSearchElement) {
    WebElement bookButton = flightSearchElement.getSelectButton(divElement);
    PageHandler.javaScriptExecuterClick(driver, bookButton);
  }

  /*** get onward fare details of flight ***/
  private FareDetails getFareDetails(WebElement divElement,
      FlightResultContainerElements flightSearchElement, List<FlightDetails> flightList,
      FlightBookingDetails flightBookingDetails) {

    Log.info(testId, "------------------        Fare Details       -----------------");

    FareDetails fareDetails = new FareDetails();

    WebElement fareButton = flightSearchElement.getFareDetailsButton(divElement);

    PageHandler.javaScriptExecuterClick(driver, fareButton);

    WebElement element = flightSearchElement.getAdultTotalFare(divElement);
    double fare = NumberUtility.getAmountFromString(countryCode, element.getText());
    fareDetails.setAdultTotalFare(fare);

    Log.info(testId, "Adult(s) total fare :: " + fare);

    /*** children fare details present if children count > 0 ***/
    if (flightBookingDetails.getChildrenCount() > 0) {
      fare = 0;
      element = flightSearchElement.getChildTotalFare(divElement);
      if (element != null) {
        fare = NumberUtility.getAmountFromString(countryCode, element.getText());
      }
      Log.info(testId, "Children total fare :: " + fare);
      fareDetails.setChildTotalFare(fare);
    }

    /*** infant fare details present if infant count > 0 ***/
    if (flightBookingDetails.getInfantsCount() > 0) {
      fare = 0;
      element = flightSearchElement.getInfantTotalFare(divElement);
      if (element != null) {
        fare = NumberUtility.getAmountFromString(countryCode, element.getText());
      }
      Log.info(testId, "Infant(s) total fare :: " + fare);
      fareDetails.setInfantTotalFare(fare);
    }

    List<WebElement> otherFareType = flightSearchElement.getOtherFareType(divElement);
    List<WebElement> otherFareList = flightSearchElement.getOtherFare(divElement);

    for (int otherFareDetailsCount = 0; otherFareDetailsCount < otherFareList.size(); otherFareDetailsCount++) {
      setFareType(fareDetails, otherFareType.get(otherFareDetailsCount),
          otherFareList.get(otherFareDetailsCount));
    }

    element = flightSearchElement.getTotalFare(divElement);
    fare = NumberUtility.getAmountFromString(countryCode, element.getText());
    fareDetails.setTotalFare(fare);

    Log.info(testId, "Total fare :: " + fare);

    if(countryCode!=Constant.VIA_COUNTRY.IN_CORP){
      fareDetails.verifyTransactionFee(testId, countryCode, media, Journey.ONE_WAY, Flight.DOMESTIC,
          repositoryParser, flightList, flightBookingDetails);

      CustomAssert.assertVerify(testId, true, fareDetails.verifyFare(testId), "Total fare verified.",
          "Total fare not equal to sum of all fares.");
    }

    PageHandler.javaScriptExecuterClick(driver, fareButton);

    Log.divider(testId);
    return fareDetails;
  }

  private void setFareType(FareDetails fareDetails, WebElement fareType, WebElement fareElement) {
    double amount = NumberUtility.getAmountFromString(countryCode, fareElement.getText());
    String type = fareType.getText();
    switch (type) {
      case "Discount":
        fareDetails.setDiscount(amount);
        break;
      case "Taxes":
        fareDetails.setTaxes(amount);
        break;
      case "Transaction Fee":
        fareDetails.setTransactionFee(amount);
        break;
      default:
        fareDetails.incrementOtherFare(amount);
    }

    Log.info(testId, type + " :: " + NumberUtility.getRoundedAmount(countryCode, amount));
  }

  /*** getting details for one direction ***/
  private List<FlightDetails> oneWayFlightSelection(WebElement divElement,
      FlightBookingDetails flightBookingDetails, FlightResultContainerElements flightSearchElement) {

    /*** get the flight baggage details and store in list ***/
    List<FlightDetails> flightDetailsList =
        getFlightBaggageDetails(divElement, flightBookingDetails, flightSearchElement);

    /*** get flight details ***/
    getFlightDetails(divElement, flightDetailsList, flightSearchElement);

    return flightDetailsList;
  }

  private void getFlightDetails(WebElement divElement, List<FlightDetails> flightDetailsList,
      FlightResultContainerElements flightSearchElement) {

    Log.info(testId, "------------------     Flight Information     ----------------");

    WebElement flightButton = flightSearchElement.getFlightDetailsButton(divElement);
    PageHandler.javaScriptExecuterClick(driver, flightButton);

    List<WebElement> flightNameList =
        flightSearchElement.getFlightNameFromFlightDetails(divElement);
    List<WebElement> flightNoList = flightSearchElement.getFlightNoFromFlightDetails(divElement);

    List<WebElement> sourceCityNameList =
        flightSearchElement.getDepartAirportNameFromFlightDetails(divElement);
    List<WebElement> destinationCityNameList =
        flightSearchElement.getArrivalAirportNameFromFlightDetails(divElement);
    List<WebElement> sourceCityCodeList =
        flightSearchElement.getDepartAirPortCodeFromFlightDetails(divElement);
    List<WebElement> destinationCityCodeList =
        flightSearchElement.getArrivalAirportCodeFromFlightDetails(divElement);
    List<WebElement> departTimeList =
        flightSearchElement.getDepartTimeFromFlightDetails(divElement);
    List<WebElement> arrivalTimeList =
        flightSearchElement.getArrivalTimeFromFlightDetails(divElement);
    List<WebElement> departDateList =
        flightSearchElement.getDepartDateFromFlightDetails(divElement);
    List<WebElement> arrivalDateList =
        flightSearchElement.getArrivalDateFromFlightDetails(divElement);

    for (int flightCount = 0; flightCount < flightNameList.size(); flightCount++) {

      FlightDetails flightDetails = flightDetailsList.get(flightCount);
      String content = getText(flightNameList, flightCount);

      Log.info(testId, "Flight Name :: " + content);

      if (!StringUtils.equalsIgnoreCase(content, flightDetails.getName())) {
        Log.error(driver, testId, "Flight name didn't match at flight details");
      }

      content = getText(flightNoList, flightCount);

      Log.info(testId, "Flight No. :: " + content);

      if (!StringUtils.equalsIgnoreCase(content, flightDetails.getCode())) {
        Log.error(driver, testId, "Flight No didn't match at flight details");
      }

      /*** set source city and city code ***/
      String cityName =
          EntityMap.getCityNameFromAirportName(getText(sourceCityNameList, flightCount));

      flightDetails.setSourceCity(cityName);
      String cityCode = getText(sourceCityCodeList, flightCount);
      flightDetails.setSourceCityCode(cityCode);

      Log.info(testId, "Departure City :: " + cityName + " (" + cityCode + ")");

      /*** set departure calendar ***/
      Calendar departCalendar =
          getCalendar(departDateList.get(flightCount), departTimeList.get(flightCount));
      flightDetails.setDeparture(departCalendar);

      Log.info(testId, "Departure time :: " + CalendarUtils.getFormattedDateTime(departCalendar));

      /*** set arrival city and code ***/
      cityName =
          EntityMap.getCityNameFromAirportName(getText(destinationCityNameList, flightCount));

      flightDetails.setDestinationCity(cityName);
      cityCode = getText(destinationCityCodeList, flightCount);
      flightDetails.setDestinationCityCode(cityCode);

      Log.info(testId, "Arrival City :: " + cityName + " (" + cityCode + ")");

      /*** set arrival calendar ***/
      Calendar arrivalCalendar =
          getCalendar(arrivalDateList.get(flightCount), arrivalTimeList.get(flightCount));
      flightDetails.setArrival(arrivalCalendar);

      Log.info(testId, "Arrival time :: " + CalendarUtils.getFormattedDateTime(arrivalCalendar));
      Log.divider(testId);
    }

    PageHandler.javaScriptExecuterClick(driver, flightButton);
  }

  private Calendar getCalendar(WebElement dateElement, WebElement timeElement) {
    String calendarString = dateElement.getText() + " " + timeElement.getText();
    Calendar calendar = null;
    try {
      calendar = CalendarUtils.dateStringToCalendarDate(calendarString, DATE_FORMAT);
    } catch (ParseException e) {
      Log.error("Calendar can't be parsed.");
    }
    return calendar;
  }

  private String getText(List<WebElement> elementList, int index) {
    if (index >= elementList.size()) {
      return StringUtils.EMPTY;
    }
    return StringUtils.trimToEmpty(elementList.get(index).getText());
  }

  /*** get baggage details of flights ***/
  private List<FlightDetails> getFlightBaggageDetails(WebElement divElement,
      FlightBookingDetails flightBookingDetails, FlightResultContainerElements flightSearchElement) {

    Log.info(testId, "-----------------     Baggage Information     ----------------");

    List<FlightDetails> flightDetailsList = new ArrayList<FlightDetails>();

    WebElement baggageButton = flightSearchElement.getBaggageInformationButton(divElement);
    PageHandler.javaScriptExecuterClick(driver, baggageButton);

    /*** get all web element containing flight name ***/
    List<WebElement> flightNames = flightSearchElement.getFlightNameFromBaggage(divElement);

    /*** get all web element containing flight no ***/
    List<WebElement> flightNos = flightSearchElement.getFlightNoFromBaggage(divElement);

    /*** get all web element containing check in baggage ***/
    List<WebElement> checkInBaggageList = flightSearchElement.getCheckInBaggage(divElement);

    /*** get all web element containing cabin baggage ***/
    List<WebElement> cabinBaggageList = flightSearchElement.getCabinBaggage(divElement);

    /***
     * store details in flight details object and add to flight details list
     ***/
    for (int flightCount = 0; flightCount < flightNames.size(); flightCount++) {
      FlightDetails flightDetails = new FlightDetails();
      String content = getText(flightNames, flightCount);

      flightDetails.setName(content);
      Log.info(testId, "Flight Name :: " + content);

      content = getText(flightNos, flightCount);
      flightDetails.setCode(content);
      Log.info(testId, "Flight No :: " + content);

      int adultCheckInBaggage = 0;
      int adultCabinBaggae = 0;
      int childCheckInBaggage = 0;
      int childCabinBaggage = 0;

      if (checkInBaggageList.size() > flightCount) {
        List<String> checkInBaggage = getBaggage(checkInBaggageList.get(flightCount));
        if (checkInBaggage.size() > 0) {
          adultCheckInBaggage = NumberUtils.toInt(checkInBaggage.get(0));
          flightDetails.setAdultCheckInBaggage(adultCheckInBaggage);

          if (checkInBaggage.size() > 1) {
            childCheckInBaggage = NumberUtils.toInt(checkInBaggage.get(1));
            flightDetails.setChildCheckInBaggage(childCheckInBaggage);
          }
        }
      }

      if (cabinBaggageList.size() > flightCount) {
        List<String> cabinBaggage = getBaggage(cabinBaggageList.get(flightCount));
        if (cabinBaggage.size() > 0) {
          adultCabinBaggae = NumberUtils.toInt(cabinBaggage.get(0));
          flightDetails.setAdultCabinBaggage(adultCabinBaggae);
          if (cabinBaggage.size() > 1) {
            childCabinBaggage = NumberUtils.toInt(cabinBaggage.get(1));
            flightDetails.setChildCabinBaggage(childCabinBaggage);
          }
        }
      }

      Log.info(testId, "Adult CheckIn baggage :: " + adultCheckInBaggage + " Kg");
      Log.info(testId, "Adult Cabin Baggage :: " + adultCabinBaggae + " Kg");

      if (flightBookingDetails.getChildrenCount() > 0) {
        Log.info(testId, "Children CheckIn baggage :: " + childCheckInBaggage + " Kg");
        Log.info(testId, "Children Cabin Baggage :: " + childCabinBaggage + " Kg");
      }

      flightDetailsList.add(flightDetails);
      Log.divider(testId);
    }

    /*** click on baggage button to close it ***/
    PageHandler.javaScriptExecuterClick(driver, baggageButton);
    return flightDetailsList;
  }

  private List<String> getBaggage(WebElement webElement) {
    List<String> baggageDetails =
        new ArrayList<String>(StringUtilities.split(webElement.getText().replaceAll("[^0-9 ]", ""),
            Constant.WHITESPACE));
    baggageDetails.removeAll(Arrays.asList("", null));
    return baggageDetails;
  }
}
