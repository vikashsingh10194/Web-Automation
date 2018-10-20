package com.via.appmodules.flights.common;

import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.AllArgsConstructor;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.via.pageobjects.flights.common.FlightBookingDetails;
import com.via.pageobjects.flights.common.PassengarValidationElements;
import com.via.pageobjects.flights.common.SSR;
import com.via.pageobjects.flights.common.TravellerDetails;
import com.via.utils.CalendarUtils;
import com.via.utils.Constant.Flight;
import com.via.utils.Constant.VIA_COUNTRY;
import com.via.utils.CustomAssert;
import com.via.utils.Log;
import com.via.utils.NumberUtility;
import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;
import com.via.utils.StringUtilities;

@AllArgsConstructor
public class PassengerValidation {

  private String testId;
  private VIA_COUNTRY countryCode;
  private WebDriver driver;
  private RepositoryParser repositoryParser;

  private final String NO_BAGGAGE = "No Baggage";
  private final String NO_MEAL = "No Meal";

  public void validatePassengers(Flight flightType, FlightBookingDetails flightBookingDetails,
      List<TravellerDetails> travellerDetailsList) {

    PassengarValidationElements passengerValidationObjects =
        new PassengarValidationElements(testId, driver, repositoryParser);

    boolean passengerValidation =
        validatePassengerDetails(flightType, passengerValidationObjects, travellerDetailsList);
    CustomAssert.assertTrue(testId, passengerValidation, "Travellers Details Validated.",
        "Error in validating travellers details.");

    boolean contactValidation =
        validateContactDetails(driver, passengerValidationObjects, flightBookingDetails);
    CustomAssert.assertTrue(testId, contactValidation, "Travellers Contact Details Validated.",
        "Error in validating travellers Contact Details.");

    PageHandler.javaScriptExecuterClick(driver, passengerValidationObjects.confirmPaymentButton());
  }

  private boolean validatePassengerDetails(Flight flightType,
      PassengarValidationElements passengerValidationObjects,
      List<TravellerDetails> travellerDetailsList) {

    List<WebElement> travellerNameList = passengerValidationObjects.getTravellersName();
    List<WebElement> travellersTypeList = passengerValidationObjects.getTravellersType();
    List<WebElement> travellersDOBList = passengerValidationObjects.getTravellersDOB();
    List<WebElement> nationalityList = passengerValidationObjects.getTravellersNationality();
    List<WebElement> passportNoList = passengerValidationObjects.getPassportNos();
    List<WebElement> expiryDateList = passengerValidationObjects.getExpiryDateList();

    Map<String, Map<String, SSR>> addOnsMap = getAddOnsMap(passengerValidationObjects);

    int calendarCount = 0;
    // Checks the total count of passengers
    CustomAssert.assertEquals(driver, testId, travellerNameList.size(),
        travellerDetailsList.size(), "Error in validating passengar count");

    boolean validation = true;

    // Loops through the passenger list
    for (int tCount = 0; tCount < travellerDetailsList.size(); tCount++) {

      TravellerDetails travellerDetails = travellerDetailsList.get(tCount);

      validation &=
          validateData(testId, "Traveller Type", travellersTypeList.get(tCount), travellerDetails
              .getType().toString());

      String fullName = travellerDetails.getName();

      validation &= validateData(testId, "Traveller Name", travellerNameList.get(tCount), fullName);

      Calendar dob = travellerDetails.getBirthCalender();
      if (dob != null) {
        validation &= validateDate(testId, "DOB", travellersDOBList.get(calendarCount++), dob);
      }

      if (flightType == Flight.INTERNATIONAL) {

        validation &=
            validateData(testId, "Nationality", nationalityList.get(tCount),
                travellerDetails.getCountry());

        validation &=
            validateData(testId, "Passport No.", passportNoList.get(tCount),
                travellerDetails.getPassportNo());

        validation &=
            validateDate(testId, "Passport Expiry Date", expiryDateList.get(tCount),
                travellerDetails.getPassportExpDate());
      }

      Map<String, SSR> ssrDetails = travellerDetails.getSsrDetails();
      CustomAssert.assertTrue(testId, validateSSRDetails(ssrDetails, addOnsMap.get(fullName)),
          "SSR Details not validated for traveller " + fullName);
    }

    return validation;
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

      boolean diffFlag =
          Math.abs(expectedSSR.getBaggagePrice() - actualSSR.getBaggagePrice()) < 0.1;
      if (!diffFlag) {
        Log.error(driver, testId, "Baggage Fare Not matched at AddOns.");
        return false;
      }

      String expectedBaggage = expectedSSR.getBaggage();
      String actualBaggage = actualSSR.getBaggage();

      if (!(StringUtils.equalsIgnoreCase(expectedBaggage, actualBaggage)
          || (StringUtils.isBlank(expectedBaggage) && StringUtils.equalsIgnoreCase(actualBaggage,
              NO_BAGGAGE)) || StringUtils.isBlank(actualBaggage)
          && StringUtils.equalsIgnoreCase(expectedBaggage, NO_BAGGAGE))) {

        Log.error(driver, testId, "Baggage Details Not matched at AddOns.");
        return false;
      }

      String expectedMeal = expectedSSR.getMeal();
      String actualMeal = actualSSR.getMeal();

      if (!(StringUtils.equalsIgnoreCase(expectedMeal, actualMeal)
          || (StringUtils.isBlank(expectedMeal) && StringUtils
              .equalsIgnoreCase(actualMeal, NO_MEAL)) || StringUtils.isBlank(actualMeal)
          && StringUtils.equalsIgnoreCase(expectedMeal, NO_MEAL))) {

        Log.error(driver, testId, "Meal Not matched at AddOns.");
        return false;
      }

      diffFlag = Math.abs(expectedSSR.getMealPrice() - actualSSR.getMealPrice()) < 0.1;

      if (!diffFlag) {
        Log.error(driver, testId, "Meal Fare Not matched at AddOns.");
        return false;
      }

      if (!StringUtils.equalsIgnoreCase(expectedSSR.getSeatNo(), actualSSR.getSeatNo())) {
        Log.error(driver, testId, "Seat No Not matched at AddOns.");
        return false;
      }

      diffFlag = Math.abs(expectedSSR.getSeatPrice() - actualSSR.getSeatPrice()) < 0.1;
      if (!diffFlag) {
        Log.error(driver, testId, "Seat Fare Not matched at AddOns.");
        return false;
      }
    }

    return true;
  }

  private Map<String, Map<String, SSR>> getAddOnsMap(
      PassengarValidationElements passengerValidationObjects) {
    Map<String, Map<String, SSR>> addOnsMap = new HashMap<String, Map<String, SSR>>();

    List<WebElement> addOnsRows = passengerValidationObjects.getAddOnsRows();
    String fltNo = null;

    String blank = "--";

    for (int row = 0; row < addOnsRows.size(); row++) {
      WebElement rowElement = addOnsRows.get(row);
      String classAttr = rowElement.getAttribute("class");
      if (StringUtils.contains(classAttr, "headerRow")) {
        fltNo = passengerValidationObjects.getFlightNo(rowElement).getText();
        continue;
      }

      String name = passengerValidationObjects.getName(rowElement).getText();
      String seat = passengerValidationObjects.getSeat(rowElement).getText();
      String baggage = passengerValidationObjects.getBaggage(rowElement).getText();
      String meal = passengerValidationObjects.getMeal(rowElement).getText();
      Map<String, SSR> ssrDetails = addOnsMap.get(name);

      if (ssrDetails == null) {
        ssrDetails = new HashMap<String, SSR>();
      }

      SSR ssr = new SSR();
      if (!StringUtils.equals(seat, blank)) {
        List<String> seatList = StringUtilities.split(seat, " at ");
        ssr.setSeatNo(StringUtils.trimToEmpty(seatList.get(0)));
        ssr.setSeatPrice(NumberUtility.getAmountFromString(countryCode, seatList.get(1)));
      }

      if (!StringUtils.equals(baggage, blank)) {
        ssr.setBaggage(baggage);
        int idx = StringUtils.indexOfIgnoreCase(baggage, "KG");
        Double baggagePrice =
            NumberUtility.getAmountFromString(countryCode, StringUtils.substring(baggage, idx));
        ssr.setBaggagePrice(baggagePrice);
      } else {
        ssr.setBaggage(NO_BAGGAGE);
        ssr.setBaggagePrice(0.0);
      }

      if (!StringUtils.equals(meal, blank)) {
        ssr.setMeal(meal);
        int idx = StringUtils.lastIndexOf(meal, "(");
        ssr.setMealPrice(NumberUtility.getAmountFromString(countryCode,
            StringUtils.substring(meal, idx)));
      } else {
        ssr.setMeal(NO_MEAL);
        ssr.setMealPrice(0.0);
      }
      ssrDetails.put(fltNo, ssr);

      addOnsMap.put(name, ssrDetails);
    }

    return addOnsMap;
  }

  private boolean validateDate(String testId, String calendarType, WebElement dateElement,
      Calendar birthCalendar) {

    Calendar calendar = null;
    try {
      calendar = CalendarUtils.dateStringToCalendarDate(dateElement.getText(), "EEE, dd MMM ''yy");
    } catch (ParseException e) {
      Log.error(driver, testId, "Unable to parse travellers " + calendarType + e.getMessage());
    }

    if (!DateUtils.isSameDay(calendar, birthCalendar)) {
      Log.info(
          testId,
          "Error validating " + calendarType + " :: Expected: "
              + CalendarUtils.getFormattedDate(birthCalendar) + " Actual: "
              + CalendarUtils.getFormattedDate(calendar));
    }

    return true;
  }

  private static boolean validateData(String testId, String dataType, WebElement travellerElement,
      String givenText) {
    String text = travellerElement.getText();

    if (!StringUtils.equalsIgnoreCase(text, givenText)) {
      Log.info(testId, dataType + " not matched. Expected: " + givenText + " Actual: " + text);
      return false;
    }
    return true;
  }

  private static boolean validateContactDetails(WebDriver driver,
      PassengarValidationElements passengerValidationObjects,
      FlightBookingDetails flightBookingDetails) {
    String testId = flightBookingDetails.getTestCaseId();
    WebElement emailElement = passengerValidationObjects.getPassengerEmail();
    String email = emailElement.getText();
    CustomAssert.assertEquals(driver, testId, email, flightBookingDetails.getEMail(),
        "Error in validating  email");

    WebElement mobileElement = passengerValidationObjects.getPassengerContact();
    String mobile = mobileElement.getText();
    CustomAssert.assertEquals(driver, testId, mobile, flightBookingDetails.getMobileNumber(),
        "Error in validating mobile Number");

    return true;
  }
}
