package com.via.appmodules.flights.domestic;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;

import com.via.pageobjects.flights.domestic.FlightResultContainerElements;
import com.via.utils.Constant.VIA_COUNTRY;
import com.via.utils.Log;
import com.via.utils.NumberUtility;
import com.via.utils.PageHandler;

public class FlightValidator extends PageHandler {

  /*** validate flight details of selection panel to the details at book panel ***/
  public boolean selectedFlightValidation(String testId, VIA_COUNTRY countryCode,
      WebElement selectedDiv, FlightResultContainerElements flightSearchElement,
      Map<String, String> flightDetails) {

    boolean flag = true;

    String flightName = flightDetails.get("flightName");
    String selectedFlightText =
        StringUtils.trimToEmpty(flightSearchElement.getSelectedFlightName(selectedDiv).getText());
    if (!StringUtils.equalsIgnoreCase(flightName, selectedFlightText)) {
      Log.error(driver, testId, "Selected flight Name didn't match :: Expected: " + flightName
          + " Actual: " + selectedFlightText);
      flag = false;
    }

    selectedFlightText =
        StringUtils.trimToEmpty(flightSearchElement.getSelectedFlightNo(selectedDiv).getText());
    String flightNo = flightDetails.get("flightNo");
    if (!StringUtils.equalsIgnoreCase(flightNo, selectedFlightText)) {
      Log.error(driver, testId, "Selected flight No didn't match :: Expected: " + flightNo + " Actual: "
          + selectedFlightText);
      flag = false;
    }

    String arrivalCity = flightDetails.get("arrivalCity");
    selectedFlightText =
        StringUtils.trimToEmpty(flightSearchElement.getSelectedFlightArrivalCity(selectedDiv)
            .getText());
    if (!StringUtils.equalsIgnoreCase(arrivalCity, selectedFlightText)) {
      Log.error(driver, testId, "Selected flight arrival city didn't match :: Expected: " + arrivalCity
          + " Acutal: " + selectedFlightText);
      flag = false;
    }

    String departCity = flightDetails.get("departCity");
    selectedFlightText =
        StringUtils.trimToEmpty(flightSearchElement.getSelectedFlightDepartCity(selectedDiv)
            .getText());
    if (!StringUtils.equalsIgnoreCase(departCity, selectedFlightText)) {
      Log.error(driver, testId, "Selected flight departure city didn't match :: Expected: " + departCity
          + " Actual: " + selectedFlightText);
      flag = false;
    }

    String arrivalTime = flightDetails.get("arrivalTime");
    selectedFlightText =
        StringUtils.trimToEmpty(flightSearchElement.getSelectedFlightArrivalTime(selectedDiv)
            .getText());
    if (!StringUtils.equalsIgnoreCase(arrivalTime, selectedFlightText)) {
      Log.error(driver, testId, "Selected flight arrival time didn't match :: Expected: " + arrivalTime
          + " Actual: " + selectedFlightText);
      flag = false;
    }

    String departTime = flightDetails.get("departTime");
    selectedFlightText =
        StringUtils.trimToEmpty(flightSearchElement.getSelectedFlightDepartTime(selectedDiv)
            .getText());
    if (!StringUtils.equalsIgnoreCase(departTime, selectedFlightText)) {
      Log.error(driver, testId, "Selected flight arrival city didn't match :: Expected: " + departTime
          + " Actual: " + selectedFlightText);
      flag = false;
    }

    String fare = flightDetails.get("fare");
    Double selectedFare =
        NumberUtility.getAmountFromString(countryCode, StringUtils.trimToEmpty(flightSearchElement
            .getSelectedFlightFare(selectedDiv).getText()));
    String selectedFareText = selectedFare.toString();
    if (!StringUtils.equalsIgnoreCase(flightDetails.get("fare"), selectedFareText)) {
      Log.error(driver, testId, "Selected flight fare didn't match :: Expected: " + fare + " Actual: "
          + selectedFareText);
      flag = false;
    }

    return flag;
  }
}
