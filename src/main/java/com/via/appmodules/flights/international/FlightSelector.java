package com.via.appmodules.flights.international;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import lombok.AllArgsConstructor;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.via.pageobjects.flights.common.FareDetails;
import com.via.pageobjects.flights.common.FlightBookingDetails;
import com.via.pageobjects.flights.common.FlightDetails;
import com.via.pageobjects.flights.international.FlightResultContainerElements;
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

  private final String DATE_FORMAT = "dd MMM, yyyy HH:mm"; // 10 Sep,

  // 2016
  // 19:30

  public void selectFlight(FlightResultContainerElements flightResultElements,
      FlightBookingDetails flightBookingDetails) {

    Log.info(testId, "---------------        Flight Selection        ---------------");
    Log.divider(testId);

    WebElement divElement = null;
    Journey journey;

    if (flightBookingDetails.getJourneyType() == Journey.ONE_WAY) {
      List<String> onwardFlightDetails =
          StringUtilities.getFlightNameAndNo(flightBookingDetails.getOnwardFlightName());
      String flightDesc = onwardFlightDetails.get(0);
      String flightName = EntityMap.getAirlineCode(onwardFlightDetails.get(0));
      String flightNo = onwardFlightDetails.get(1);
      try {
        if (flightBookingDetails.getOnwardFlightName().equalsIgnoreCase("Garuda")) {
          Thread.sleep(30 * 1000);
        }
        divElement = flightResultElements.getOnwardDivElement(flightName, flightNo);
      } catch (Exception e) {
        CustomAssert.assertFail(testId, "One way Flight with description as " + flightDesc
            + " Not found.");
      }
      journey = Journey.ONE_WAY;
    } else {
      String onwardFlightDesc = flightBookingDetails.getOnwardFlightName();
      List<String> flightDetails = StringUtilities.getFlightNameAndNo(onwardFlightDesc);
      String onwardFlightName = EntityMap.getAirlineCode(flightDetails.get(0));
      String returnFlightDesc = flightBookingDetails.getReturnFlightName();
      flightDetails = StringUtilities.getFlightNameAndNo(returnFlightDesc);
      String returnFlightName = EntityMap.getAirlineCode(flightDetails.get(0));
      String divNo = flightDetails.get(1);
      try {
        if (flightBookingDetails.getOnwardFlightName().equalsIgnoreCase("Garuda")
            || flightBookingDetails.getReturnFlightName().equalsIgnoreCase("Garuda")) {
          Thread.sleep(30 * 1000);
        }
        divElement =
            flightResultElements.getReturnDivElement(onwardFlightName, returnFlightName, divNo);
      } catch (Exception e) {
        CustomAssert.assertFail(testId, "Round trip Flight with combination (onward : "
            + onwardFlightDesc + " return : " + returnFlightDesc + ") not found.");
      }
      journey = Journey.ROUND_TRIP;
    }

    List<FlightDetails> onwardFlightList = new ArrayList<FlightDetails>();
    List<FlightDetails> returnFlightList = new ArrayList<FlightDetails>();

    flightBookingDetails.setOnwardFlights(onwardFlightList);
    flightBookingDetails.setReturnFlights(returnFlightList);

    // WebElement detailsContainer =
    // flightResultElements.getFlightDetailsContainer(divElement);
    // CommonUtils.javaScriptExecuterClick(driver, detailsContainer);
    setFlightDetails(divElement, flightResultElements, flightBookingDetails);
    setBaggageInformation(divElement, flightResultElements, flightBookingDetails);
    setFareDetails(journey, divElement, flightResultElements, flightBookingDetails);

    // CommonUtils.javaScriptExecuterClick(driver, detailsContainer);

    WebElement bookButton = flightResultElements.getBookButton(divElement);
    PageHandler.javaScriptExecuterClick(driver, bookButton);

  }

  private void setFareDetails(Journey journey, WebElement divElement,
      FlightResultContainerElements flightResultElements, FlightBookingDetails flightBookingDetails) {

    WebElement fareDetailsTab = flightResultElements.getFareDetailsTab(divElement);
    PageHandler.javaScriptExecuterClick(driver, fareDetailsTab);

    Log.info(testId, "------------------        Fare Details       -----------------");

    FareDetails fareDetails = new FareDetails();

    double adultTotalFare =
        NumberUtility.getAmountFromString(countryCode,
            flightResultElements.getAdultTotalFare(divElement).getText());
    fareDetails.setAdultTotalFare(adultTotalFare);

    Log.info(testId,
        "Adults Total Fare :: " + NumberUtility.getRoundedAmount(countryCode, adultTotalFare));

    if (flightBookingDetails.getChildrenCount() > 0) {
      double childTotalFare =
          NumberUtility.getAmountFromString(countryCode,
              flightResultElements.getChildTotalFare(divElement).getText());
      fareDetails.setChildTotalFare(childTotalFare);
      Log.info(testId,
          "Children Total Fare :: " + NumberUtility.getRoundedAmount(countryCode, childTotalFare));
    }

    if (flightBookingDetails.getInfantsCount() > 0) {
      double infantTotalFare =
          NumberUtility.getAmountFromString(countryCode,
              flightResultElements.getInfantTotalFare(divElement).getText());
      fareDetails.setInfantTotalFare(infantTotalFare);
      Log.info(testId,
          "Infants Total Fare :: " + NumberUtility.getRoundedAmount(countryCode, infantTotalFare));
    }

    List<WebElement> otherFareType = flightResultElements.getOtherFareType(divElement);
    List<WebElement> otherFareList = flightResultElements.getOtherFares(divElement);

    for (int otherFareDetailsCount = 0; otherFareDetailsCount < otherFareList.size(); otherFareDetailsCount++) {
      setFareType(fareDetails, otherFareType.get(otherFareDetailsCount),
          otherFareList.get(otherFareDetailsCount));
    }

    double totalFare =
        NumberUtility.getAmountFromString(countryCode, flightResultElements
            .getTotalFare(divElement).getText());

    fareDetails.setTotalFare(totalFare);

    flightBookingDetails.setOnwardFareDetails(fareDetails);

    // double totalPrice =
    // NumberUtility.getAmountFromString(flightResultElements.getTotalPrice(divElement).getText());

    flightBookingDetails.setTotalFare(totalFare);

    // if (totalPrice != totalFare) {
    // Log.info(
    // testId,
    // "Total price didn't match at book panel and fare details panel. :: Expected: "
    // + NumberUtility.getRoundedAmount(totalPrice) + " Actual: "
    // + NumberUtility.getRoundedAmount(totalFare));
    // }
    //

    Log.info(testId, "Total Fare :: " + NumberUtility.getRoundedAmount(countryCode, totalFare));

    List<FlightDetails> flightList = new ArrayList<FlightDetails>();

    flightList.addAll(flightBookingDetails.getOnwardFlights());
    flightList.addAll(flightBookingDetails.getReturnFlights());

    if (countryCode!=Constant.VIA_COUNTRY.IN_CORP) {
      fareDetails.verifyTransactionFee(testId, countryCode, media, journey, Flight.INTERNATIONAL,
          repositoryParser, flightList, flightBookingDetails);
      CustomAssert.assertVerify(testId, true, fareDetails.verifyFare(testId),
          "Total fare verified.", "Total fare not equal to sum of all fares.");
    }
    Log.divider(testId);
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
        break;
    }

    Log.info(testId, type + " :: " + NumberUtility.getRoundedAmount(countryCode, amount));
  }

  private void setBaggageInformation(WebElement divElement,
      FlightResultContainerElements flightResultElements, FlightBookingDetails flightBookingDetails) {

    WebElement baggageDetailsTab = flightResultElements.getBaggageInfoTab(divElement);
    PageHandler.javaScriptExecuterClick(driver, baggageDetailsTab);

    List<WebElement> checkInBaggageList = flightResultElements.getCheckInBaggageList(divElement);
    List<WebElement> cabinBaggageList = flightResultElements.getCabinBaggageList(divElement);
    List<WebElement> flightNameList = flightResultElements.getFlightNameListFromBaggage(divElement);
    List<WebElement> flightNoList = flightResultElements.getFlightNoListFromBaggage(divElement);

    int listIterCount = 0;
    List<FlightDetails> flightDetailsList = flightBookingDetails.getOnwardFlights();
    int loopIterCount = 0;

    Log.info(testId, "--------------     Onward Baggage information   ---------------");
    Log.divider(testId);

    while (true) {
      for (int flightCount = 0; flightCount < flightDetailsList.size(); flightCount++) {
        FlightDetails flightDetails = flightDetailsList.get(flightCount);

        /*** verify flight name of flight details and baggage information ****/
        String flightNameInBaggageInfo = flightNameList.get(listIterCount).getText();
        String flightNameInFlightDetails = flightDetails.getName();
        if (!StringUtils.equalsIgnoreCase(flightNameInBaggageInfo, flightNameInFlightDetails)) {
          Log.info(testId,
              "Flight Name didn't match at flight details and baggage info. :: Expected: "
                  + flightNameInFlightDetails + "Actual: " + flightNameInBaggageInfo);
        }

        /*** verify flight No of flight details and baggage information ****/
        String flightNoInBaggageInfo = flightNoList.get(listIterCount).getText();
        String flightNoInFlightDetails = flightDetails.getCode();

        if (!StringUtils.equalsIgnoreCase(flightNoInBaggageInfo, flightNoInFlightDetails)) {
          Log.info(testId,
              "Flight No didn't match at flight details and baggage info. :: Expected: "
                  + flightNoInFlightDetails + "Actual: " + flightNoInBaggageInfo);
        }

        Log.info(testId, "Flight Name :: " + flightNameInBaggageInfo);
        Log.info(testId, "Flight No :: " + flightNoInBaggageInfo);

        /*** get baggage information ****/

        int adultCheckInBaggage = 0;
        int adultCabinBaggae = 0;
        int childCheckInBaggage = 0;
        int childCabinBaggage = 0;

        if (checkInBaggageList.size() > listIterCount) {
          List<String> checkInBaggage = getBaggage(checkInBaggageList.get(listIterCount));
          if (checkInBaggage.size() > 0) {
            adultCheckInBaggage = NumberUtils.toInt(checkInBaggage.get(0));
            flightDetails.setAdultCheckInBaggage(adultCheckInBaggage);

            if (checkInBaggage.size() > 1) {
              childCheckInBaggage = NumberUtils.toInt(checkInBaggage.get(1));
              flightDetails.setChildCheckInBaggage(childCheckInBaggage);
            }
          }
        }

        if (cabinBaggageList.size() > listIterCount) {
          List<String> cabinBaggage = getBaggage(cabinBaggageList.get(listIterCount));
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
        listIterCount++;
      }

      loopIterCount++;
      flightDetailsList = flightBookingDetails.getReturnFlights();
      if (flightDetailsList.size() == 0 || loopIterCount == 2) {
        break;
      }
      Log.divider(testId);
      Log.info(testId, "----------------    Return Baggage Details    ---------------");
    }
    Log.divider(testId);
  }

  private List<String> getBaggage(WebElement webElement) {
    List<String> baggageDetails =
        new ArrayList<String>(StringUtilities.split(webElement.getText().replaceAll("[^0-9 ]", ""),
            Constant.WHITESPACE));
    baggageDetails.removeAll(Arrays.asList("", null));
    return baggageDetails;
  }

  private void setFlightDetails(WebElement divElement,
      FlightResultContainerElements flightResultElements, FlightBookingDetails flightBookingDetails) {

    Log.info(testId, "------------------     Flight Information     ----------------");

    WebElement flightDetailsTab = flightResultElements.getFlightDetailsTab(divElement);
    PageHandler.javaScriptExecuterClick(driver, flightDetailsTab);

    List<WebElement> departureCodeList = flightResultElements.getDepartAirportCodeList(divElement);
    List<WebElement> arrivalCodeList = flightResultElements.getArrivalAirportCodeList(divElement);
    List<WebElement> departureNameList = flightResultElements.getDepartAirportNameList(divElement);
    List<WebElement> arrivalNameList = flightResultElements.getArrivalAirportNameList(divElement);
    List<WebElement> flightNameList = flightResultElements.getFlightNameList(divElement);
    List<WebElement> flightNoList = flightResultElements.getFlightNoList(divElement);
    List<WebElement> departDateList = flightResultElements.getFlightDepartDate(divElement);
    List<WebElement> departTimeList = flightResultElements.getFlightDepartTime(divElement);
    List<WebElement> arrivalDateList = flightResultElements.getFlightArrivalDate(divElement);
    List<WebElement> arrivalTimeList = flightResultElements.getFlightArrivalTime(divElement);

    List<FlightDetails> flightDetailsList = flightBookingDetails.getOnwardFlights();

    int listIterCount = 0;

    int totalOnwardFlight = departureCodeList.size();

    if (flightBookingDetails.getJourneyType() == Journey.ROUND_TRIP) {
      totalOnwardFlight = flightResultElements.getTotalOnwardFlight(divElement);
    }

    Log.info(testId, "-----------------    Onward flight Details    ----------------");
    for (int flightCount = 0; flightCount < departureCodeList.size(); flightCount++) {

      if (flightCount == totalOnwardFlight) {
        Log.divider(testId);
        Log.info(testId, "-----------------    Return flight Details    ----------------");
        flightDetailsList = flightBookingDetails.getReturnFlights();
      }

      FlightDetails flightDetails = new FlightDetails();
      String departureCode = departureCodeList.get(listIterCount).getText();
      String arrivalCode = arrivalCodeList.get(listIterCount).getText();
      String departCityName = departureNameList.get(listIterCount).getText();
      String arrivalCityName = arrivalNameList.get(listIterCount).getText();

      departCityName = EntityMap.getCityNameFromAirportName(departCityName);

      arrivalCityName = EntityMap.getCityNameFromAirportName(arrivalCityName);

      /*** set source details ***/
      flightDetails.setSourceCity(departCityName);
      flightDetails.setSourceCityCode(departureCode);

      /*** set destination details ***/
      flightDetails.setDestinationCityCode(arrivalCode);
      flightDetails.setDestinationCity(arrivalCityName);

      /*** set flight details ***/
      String flightName = flightNameList.get(listIterCount).getText();
      String flightNo = flightNoList.get(listIterCount).getText();
      flightDetails.setName(flightName);
      flightDetails.setCode(flightNo);

      Calendar departCalendar =
          getCalendar(departDateList.get(listIterCount), departTimeList.get(listIterCount));
      Calendar arrivalCalendar =
          getCalendar(arrivalDateList.get(listIterCount), arrivalTimeList.get(listIterCount));

      flightDetails.setDeparture(departCalendar);
      flightDetails.setArrival(arrivalCalendar);

      /**** log the report in log file ****/
      /**** log the report in log file ****/
      Log.info(testId, "Departure City :: " + departCityName + " (" + departureCode + ")");
      Log.info(testId, "Departure time :: " + CalendarUtils.getFormattedDateTime(departCalendar));
      Log.info(testId, "Flight Name :: " + flightName);
      Log.info(testId, "Flight No :: " + flightNo);
      Log.info(testId, "Arrival City :: " + arrivalCityName + " (" + arrivalCode + ")");
      Log.info(testId, "Arrival time :: " + CalendarUtils.getFormattedDateTime(arrivalCalendar));

      flightDetailsList.add(flightDetails);

      listIterCount++;
    }
    Log.divider(testId);
    PageHandler.javaScriptExecuterClick(driver, flightDetailsTab);
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
}
