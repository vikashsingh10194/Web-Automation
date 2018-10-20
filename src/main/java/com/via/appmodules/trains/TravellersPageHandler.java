package com.via.appmodules.trains;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.via.pageobjects.trains.TrainBookingDetails;
import com.via.pageobjects.trains.TrainDetails;
import com.via.pageobjects.trains.TravellerDetails;
import com.via.pageobjects.trains.TravellersPageElements;
import com.via.utils.CalendarUtils;
import com.via.utils.Constant;
import com.via.utils.Constant.BOOKING_MEDIA;
import com.via.utils.Constant.Journey;
import com.via.utils.Constant.VIA_COUNTRY;
import com.via.utils.CustomAssert;
import com.via.utils.Log;
import com.via.utils.NumberUtility;
import com.via.utils.PageHandler;
import com.via.utils.RandomValues;
import com.via.utils.RepositoryParser;
import com.via.utils.StringUtilities;

@AllArgsConstructor
public class TravellersPageHandler {
  private String testId;
  private WebDriver driver;
  private RepositoryParser repositoryParser;
  private final String HEADER_CAL_FORMAT = "dd MMM yyyy";
  private final String SEAT_MAP_CAL_FORMAT = "MMM dd, yyyy";
  private final String CONFIRMATION_SEAT_CAL_FORMAT = "EEE, dd MMM ''yy";
  private final String ONWARD = "onward";
  private final String RETURN = "return";

  private final String ISD_CODE = "91";
  private final String CONTACT_MOBILE = "9611577993";
  private final String CONTACT_EMAIL = "qa@via.com";

  private final String ONWARD_SEAT = "Onward Seat";
  private final String RETURN_SEAT = "Return Seat";

  public void execute(VIA_COUNTRY countryCode, BOOKING_MEDIA media,
      TrainBookingDetails trainBookingDetails) {
    Log.info(testId,
        ":::::::::::::::::::         Traveller Details Page         ::::::::::::::::::::");
    TravellersPageElements travellerElements =
        new TravellersPageElements(testId, driver, repositoryParser);
    validateHeaderDetails(travellerElements, trainBookingDetails);
    validateJourneyDetails(travellerElements, trainBookingDetails);

    List<TravellerDetails> travellerDetailsList =
        fillTravellersDetails(countryCode, travellerElements, trainBookingDetails);
    setContactDetails(travellerElements, trainBookingDetails);

    seatSelection(travellerElements, media, trainBookingDetails, travellerDetailsList);

    verifyFareDetails(countryCode, media, travellerElements, trainBookingDetails);

    PageHandler.javaScriptExecuterClick(driver, travellerElements.getTermsCheckBox());
    PageHandler.javaScriptExecuterClick(driver, travellerElements.getMakePaymentBtn());

    verifyDetails(travellerElements, trainBookingDetails, travellerDetailsList);

    PageHandler.javaScriptExecuterClick(driver, travellerElements.getProceedPaymentBtn());
  }

  private void verifyDetails(TravellersPageElements travellerElements,
      TrainBookingDetails trainBookingDetails, List<TravellerDetails> travellerDetailsList) {
    verifyTravellersDetails(travellerElements, travellerDetailsList);
    verifyContactDetails(travellerElements, trainBookingDetails);
  }

  private void verifyContactDetails(TravellersPageElements travellerElements,
      TrainBookingDetails trainBookingDetails) {
    String actualEmail = travellerElements.getConfirmEmail().getText();
    String actualContactNo = travellerElements.getConfirmContactNo().getText();
    String expectedEmail = trainBookingDetails.getContactEmail();
    String expectedContactNo =
        "+" + trainBookingDetails.getIsdCode() + "-" + trainBookingDetails.getContactNo();

    CustomAssert.assertTrue(testId, StringUtils.equalsIgnoreCase(actualEmail, expectedEmail),
        "Contact Email didn't match. Expected : " + expectedEmail + " Actual : " + actualEmail);
    CustomAssert
        .assertTrue(testId, StringUtils.equalsIgnoreCase(actualContactNo, expectedContactNo),
            "Contact No didn't match. Expected : " + expectedContactNo + " Actual : "
                + actualContactNo);

  }

  private void verifyTravellersDetails(TravellersPageElements travellerElements,
      List<TravellerDetails> travellerDetailsList) {

    List<WebElement> nameList = travellerElements.getTravellerName();
    List<WebElement> travellerDetailsElements = travellerElements.getTravellerDetails();
    List<WebElement> seatMapList = travellerElements.getSeatDetails();

    int actualTotalTravellers = travellerDetailsElements.size();
    int expectedTotalTravellers = travellerDetailsList.size();

    CustomAssert.assertTrue(testId, actualTotalTravellers == expectedTotalTravellers,
        "Total Travellers Details not matched. Expected : " + expectedTotalTravellers
            + " Actual : " + actualTotalTravellers);

    for (int count = 0; count < actualTotalTravellers; count++) {
      TravellerDetails travellerDetails = travellerDetailsList.get(count);

      WebElement detailsElement = travellerDetailsElements.get(count);
      WebElement seatDetails = seatMapList.get(count);
      List<WebElement> keys = travellerElements.getKeys(detailsElement);
      List<WebElement> values = travellerElements.getValues(detailsElement);

      String actualName = nameList.get(count).getText();
      String expectedName = travellerDetails.getName();

      CustomAssert.assertTrue(testId, StringUtils.equalsIgnoreCase(expectedName, actualName),
          "Traveller Name didn't match. Expected : " + expectedName + " Actual : " + actualName);

      String expectedType = travellerDetails.getType();
      String actualType = travellerElements.getTravellerType(detailsElement).getText();
      CustomAssert.assertTrue(testId, StringUtils.equalsIgnoreCase(expectedType, actualType),
          "Traveller Type didn't match. Expected : " + expectedType + " Actual : " + actualType);
      if (StringUtils.equalsIgnoreCase("adult", expectedType)) {
        String actualIdType = keys.get(0).getText();
        String expectedIdType = travellerDetails.getIdType();

        CustomAssert.assertTrue(testId, StringUtils.equalsIgnoreCase(expectedIdType, actualIdType),
            "Traveller ID type didn't match. Expected : " + expectedIdType + " Actual : "
                + actualIdType);

        String actualIdNo = values.get(0).getText();
        String expectedIdNo = travellerDetails.getIdNo();

        CustomAssert.assertTrue(testId, StringUtils.equalsIgnoreCase(expectedIdNo, actualIdNo),
            "Traveller ID No didn't match. Expected : " + expectedIdNo + " Actual : " + actualIdNo);

        String actualContactNo = values.get(1).getText();
        String expectedContactNo = travellerDetails.getContactNo();

        CustomAssert.assertTrue(testId,
            StringUtils.equalsIgnoreCase(expectedContactNo, actualContactNo),
            "Traveller ID No didn't match. Expected : " + expectedContactNo + " Actual : "
                + actualContactNo);

        Map<String, String> travellerSeatList = travellerDetails.getSeatDetails();

        keys = travellerElements.getKeys(seatDetails);
        values = travellerElements.getValues(seatDetails);

        int totalSeat = travellerSeatList.size();

        for (int seatCount = 0; seatCount < totalSeat; seatCount++) {
          String expectedSeat = travellerSeatList.get(keys.get(seatCount).getText());
          String actualSeat = values.get(seatCount).getText();
          CustomAssert.assertTrue(testId, StringUtils.equalsIgnoreCase(expectedSeat, actualSeat),
              "Traveller Seat No didn't match. Expected : " + expectedSeat + " Actual : "
                  + actualSeat);
        }

      } else {
        Calendar actualDob = null;
        try {
          actualDob =
              CalendarUtils.dateStringToCalendarDate(values.get(0).getText(),
                  CONFIRMATION_SEAT_CAL_FORMAT);
        } catch (ParseException e) {
          CustomAssert.assertFail(testId, "Dob Calendar can't be parsed.");
        }
        Calendar expectedDate = travellerDetails.getDob();
        CustomAssert.assertTrue(
            testId,
            DateUtils.isSameDay(expectedDate, actualDob),
            "Traveller Seat No didn't match. Expected : "
                + CalendarUtils.getDateInFormat(expectedDate, CONFIRMATION_SEAT_CAL_FORMAT)
                + " Actual : "
                + CalendarUtils.getDateInFormat(actualDob, CONFIRMATION_SEAT_CAL_FORMAT));
      }
    }
  }

  private void setContactDetails(TravellersPageElements travellerElements,
      TrainBookingDetails trainBookingDetails) {
    PageHandler.selectFromDropDown(travellerElements.getISDCode(), ISD_CODE);
    trainBookingDetails.setIsdCode(ISD_CODE);
    WebElement contactNo = travellerElements.getContactNo();
    contactNo.clear();
    contactNo.sendKeys(CONTACT_MOBILE);
    trainBookingDetails.setContactNo(CONTACT_MOBILE);
    WebElement contactEmail = travellerElements.getEmail();
    contactEmail.clear();
    contactEmail.sendKeys(CONTACT_EMAIL);
    trainBookingDetails.setContactEmail(CONTACT_EMAIL);
  }

  private void verifyFareDetails(VIA_COUNTRY countryCode, BOOKING_MEDIA media,
      TravellersPageElements travellerElements, TrainBookingDetails trainBookingDetails) {

    Log.info(testId, "----------------     Fare Details Verification      ----------------");

    sleep(2 * 1000);

    List<WebElement> fareDetails = travellerElements.getCompleteFareDetails();

    Double actualBaseFare =
        NumberUtility.getAmountFromString(countryCode, fareDetails.get(2).getText());
    Double expectedBaseFare = trainBookingDetails.getTotalFare();
    boolean validationFlag = true;

    boolean flag = expectedBaseFare.equals(actualBaseFare);
    validationFlag &= flag;

    CustomAssert.assertVerify(testId, flag, "Total Base Fare didn't match. Expected : "
        + expectedBaseFare + " Actual : " + actualBaseFare);

    Double convFare = NumberUtility.getAmountFromString(countryCode, fareDetails.get(3).getText());
    Double discount = NumberUtility.getAmountFromString(countryCode, fareDetails.get(4).getText());
    Double totalFare = NumberUtility.getAmountFromString(countryCode, fareDetails.get(5).getText());

    Log.info(testId, "Total Base Fare : " + actualBaseFare);
    Log.info(testId, "Convenience Fare : " + convFare);
    Log.info(testId, "Discount : " + discount);
    Log.info(testId, "Total Fare : " + totalFare);

    flag = totalFare.equals(actualBaseFare + convFare - discount);
    validationFlag &= flag;
    CustomAssert.assertVerify(testId, flag, "Total Fare not equals to sum of all fares.");

    CustomAssert.assertTrue(testId, validationFlag, "Fare Details Verified.",
        "Fare Details not Verified.");

    trainBookingDetails.setTotalFare(totalFare);

    Log.divider(testId);
  }

  private void seatSelection(TravellersPageElements travellerElements, BOOKING_MEDIA media,
      TrainBookingDetails trainBookingDetails, List<TravellerDetails> travellerDetailsList) {
    if (media == BOOKING_MEDIA.B2B) {

      if (trainBookingDetails.getSplitBooking() == null
          || trainBookingDetails.getSplitBooking() == false) {
        PageHandler.javaScriptExecuterClick(driver, travellerElements.getNoSplit());
      } else {
        PageHandler.javaScriptExecuterClick(driver, travellerElements.getSplit());
      }
    }

    allocateSeat(travellerElements, trainBookingDetails, travellerDetailsList);
  }

  private void allocateSeat(TravellersPageElements travellerElements,
      TrainBookingDetails trainBookingDetails, List<TravellerDetails> travellerDetailsList) {
    if (trainBookingDetails.getManualSeatSelection() == null
        || trainBookingDetails.getManualSeatSelection() == false) {
      PageHandler.javaScriptExecuterClick(driver, travellerElements.getAutoAllocation());
      List<WebElement> selectedSeat = travellerElements.getSelectedSeatDetails();
      List<String> onwardSeats =
          StringUtilities.split(selectedSeat.get(0).getText(), Constant.COMMA);

      if (trainBookingDetails.getJourneyType() == Journey.ONE_WAY) {
        for (int count = 0; count < onwardSeats.size(); count++) {
          Map<String, String> seatDetails = travellerDetailsList.get(count).getSeatDetails();
          seatDetails.put(ONWARD_SEAT, onwardSeats.get(count));
        }
      } else {
        List<String> returnSeats =
            StringUtilities.split(selectedSeat.get(1).getText(), Constant.COMMA);
        for (int count = 0; count < onwardSeats.size(); count++) {
          Map<String, String> seatDetails = travellerDetailsList.get(count).getSeatDetails();
          seatDetails.put(ONWARD_SEAT, onwardSeats.get(count));
          seatDetails.put(RETURN_SEAT, returnSeats.get(count));
        }
      }
      return;
    }

    PageHandler.javaScriptExecuterClick(driver, travellerElements.getManualAllocation());

    sleep(2 * 1000);

    List<WebElement> seatSelectionBtn = travellerElements.getSeatSelectionBtn();
    PageHandler.javaScriptExecuterClick(driver, seatSelectionBtn.get(0));
    selectSeat(ONWARD, travellerElements, trainBookingDetails, travellerDetailsList);

    if (trainBookingDetails.getJourneyType() == Journey.ROUND_TRIP) {
      PageHandler.javaScriptExecuterClick(driver, seatSelectionBtn.get(1));
      selectSeat(RETURN, travellerElements, trainBookingDetails, travellerDetailsList);
    }

  }

  private void selectSeat(String journeyType, TravellersPageElements travellersElements,
      TrainBookingDetails trainBookingDetails, List<TravellerDetails> travellerDetailsList) {

    String seatType = ONWARD_SEAT;
    if (StringUtils.equalsIgnoreCase(journeyType, RETURN)) {
      seatType = RETURN_SEAT;
    }

    WebElement journeySeatDiv = travellersElements.getSeatDiv(journeyType);
    boolean validationFlag =
        validateJourneyDetails(journeyType, journeySeatDiv, travellersElements, trainBookingDetails);
    CustomAssert.assertTrue(testId, validationFlag, journeyType
        + " details validated at seat map panel.");

    List<WebElement> coachList = travellersElements.getAllCoaches(journeyType);
    int totalCoach = coachList.size();
    int count = 0;
    int totalAdults = trainBookingDetails.getAdultCount();
    int adultCount = 1;

    boolean flag = true;

    while (flag) {
      PageHandler.javaScriptExecuterClick(driver, coachList.get(count));

      sleep(200);

      List<WebElement> availableSeats =
          travellersElements.getAvailableSeats(journeySeatDiv, count + 1);
      for (int availCount = 0; availCount < availableSeats.size() && flag; availCount++) {
        WebElement element = availableSeats.get(availCount);

        String attribute = element.getAttribute("class");
        if (StringUtils.containsIgnoreCase(attribute, "status-selected")) {
          PageHandler.javaScriptExecuterClick(driver, element);
        }

        String seat =
            element.getAttribute("data-wagon") + " : "
                + element.findElement(By.xpath(".//div")).getText();

        PageHandler.javaScriptExecuterClick(driver, element);

        travellerDetailsList.get(totalAdults - adultCount).getSeatDetails().put(seatType, seat);
        adultCount++;

        if (adultCount > totalAdults) {
          flag = false;
        }
      }

      count++;
      if (count >= totalCoach) {
        flag = false;
      }
    }

    PageHandler.javaScriptExecuterClick(driver,
        travellersElements.getProceedWithSeat(journeySeatDiv));
    sleep(2 * 1000);
  }

  private boolean validateJourneyDetails(String journeyType, WebElement journeySeatDiv,
      TravellersPageElements travellersElements, TrainBookingDetails trainBookingDetails) {

    sleep(2 * 1000);

    String expectedSource;
    String expectedDestination;
    Calendar expectedDate;
    TrainDetails trainDetails;

    if (StringUtils.equals(journeyType, ONWARD)) {
      expectedSource = trainBookingDetails.getSourceCode();
      expectedDestination = trainBookingDetails.getDestinationCode();
      expectedDate = trainBookingDetails.getOnwardDate();
      trainDetails = trainBookingDetails.getOnwardTrainDetails();
    } else {
      expectedSource = trainBookingDetails.getDestinationCode();
      expectedDestination = trainBookingDetails.getSourceCode();
      expectedDate = trainBookingDetails.getReturnDate();
      trainDetails = trainBookingDetails.getReturnTrainDetails();
    }

    List<String> cityCode =
        StringUtilities.split(travellersElements.getJourneyCity(journeySeatDiv).getText(), " to ");

    String actualSource = cityCode.get(0);
    String actualDestination = cityCode.get(1);

    boolean validationFlag = true;
    boolean flag = StringUtils.equalsIgnoreCase(actualSource, expectedSource);
    validationFlag &= flag;

    CustomAssert.assertVerify(testId, flag, "Source City code didn't match. Expected : "
        + expectedSource + " Actual: " + actualSource);

    flag = StringUtils.equalsIgnoreCase(actualDestination, expectedDestination);
    validationFlag &= flag;

    CustomAssert.assertVerify(testId, flag, "Destination City code didn't match. Expected : "
        + expectedDestination + " Actual: " + actualDestination);

    Calendar actualDate = null;

    try {
      actualDate =
          CalendarUtils.dateStringToCalendarDate(travellersElements.getDate(journeySeatDiv)
              .getText(), SEAT_MAP_CAL_FORMAT);
    } catch (ParseException e) {
      Log.info(testId, journeyType + " calendar can't be parsed at seat map.");
      return false;
    }

    flag = DateUtils.isSameDay(expectedDate, actualDate);

    validationFlag &= flag;

    CustomAssert.assertVerify(testId, flag, "Journey Date didn't match. Expected : "
        + CalendarUtils.getDateInFormat(expectedDate, SEAT_MAP_CAL_FORMAT) + " Actual: "
        + CalendarUtils.getDateInFormat(actualDate, SEAT_MAP_CAL_FORMAT));

    List<WebElement> train = travellersElements.getTrainNameAtSeatMap(journeySeatDiv);
    String actual = train.get(0).getText();
    String expected = trainDetails.getTrainName();

    CustomAssert.assertVerify(testId, flag, "Train Name didn't match. Expected : " + expected
        + " Actual: " + actual);

    actual = train.get(1).getText();
    expected = trainDetails.getCoachType();

    CustomAssert.assertVerify(testId, flag, "Train Coach didn't match. Expected : " + expected
        + " Actual: " + actual);

    String expectedTime = trainDetails.getDepartTime();
    List<WebElement> timeList = travellersElements.getTime(journeySeatDiv);

    String actualTime = timeList.get(0).getText();

    flag = StringUtils.equalsIgnoreCase(expectedTime, actualTime);
    validationFlag &= flag;

    CustomAssert.assertVerify(testId, flag, "Departure time didn't match. Expected : "
        + expectedTime + " Actual: " + actualTime);

    expectedTime = trainDetails.getArrivalTime();
    actualTime = timeList.get(1).getText();

    flag = StringUtils.equalsIgnoreCase(expectedTime, actualTime);
    validationFlag &= flag;

    CustomAssert.assertVerify(testId, flag, "Arrival time didn't match. Expected : " + expectedTime
        + " Actual: " + actualTime);

    int actualAdult = NumberUtils.toInt(travellersElements.getAdultCount(journeySeatDiv).getText());
    int expectedAdult = trainBookingDetails.getAdultCount();

    flag = (expectedAdult == actualAdult);
    validationFlag &= flag;

    CustomAssert.assertVerify(testId, flag, "Adult count didn't match. Expected : " + expectedAdult
        + " Actual: " + actualAdult);

    return validationFlag;
  }

  private List<TravellerDetails> fillTravellersDetails(VIA_COUNTRY countryCode,
      TravellersPageElements travellerElements, TrainBookingDetails trainBookingDetails) {
    RandomValues.setUsedNameList();
    List<TravellerDetails> travellerList =
        setAdultDetails(countryCode, travellerElements, trainBookingDetails.getAdultCount());
    travellerList.addAll(setInfantDetails(countryCode, travellerElements,
        trainBookingDetails.getInfantCount(), trainBookingDetails.getOnwardDate()));
    return travellerList;
  }

  private List<TravellerDetails> setInfantDetails(VIA_COUNTRY countryCode,
      TravellersPageElements travellerElements, int infantCount, Calendar onwardDate) {
    List<TravellerDetails> travellersList = new ArrayList<TravellerDetails>();
    for (int count = 1; count <= infantCount; count++) {
      TravellerDetails travellerDetails = new TravellerDetails();

      travellerDetails.setType("Infant");
      String traveller = "infant" + count;
      PageHandler.javaScriptExecuterClick(driver, travellerElements.getExpandButton(traveller));
      String name = RandomValues.getRandomName(countryCode);
      travellerElements.getTravellerName(traveller).sendKeys(name);
      travellerDetails.setName(name);

      Calendar dob = CalendarUtils.getRandomDOB_OLD("infant", onwardDate);

      PageHandler.selectFromDropDown(travellerElements.getTravellerDOBDay(traveller),
          new SimpleDateFormat("dd").format(dob.getTime()));

      PageHandler.selectFromDropDown(travellerElements.getTravellerDOBMonth(traveller),
          new SimpleDateFormat("MMM").format(dob.getTime()));

      PageHandler.selectFromDropDown(travellerElements.getTravellerDOBYear(traveller),
          new SimpleDateFormat("yyyy").format(dob.getTime()));
      travellerDetails.setDob(dob);

      travellersList.add(travellerDetails);
    }
    return travellersList;
  }

  private List<TravellerDetails> setAdultDetails(VIA_COUNTRY countryCode,
      TravellersPageElements travellerElements, int adultCount) {
    List<TravellerDetails> travellersList = new ArrayList<TravellerDetails>();
    for (int count = 1; count <= adultCount; count++) {

      TravellerDetails travellerDetails = new TravellerDetails();
      travellerDetails.setSeatDetails(new HashMap<String, String>());
      travellerDetails.setType("Adult");

      String traveller = "adult" + count;
      if (!StringUtils.equals(traveller, "adult1")) {
        PageHandler.javaScriptExecuterClick(driver, travellerElements.getExpandButton(traveller));
      }

      String name = RandomValues.getRandomName(countryCode);
      travellerElements.getTravellerName(traveller).sendKeys(name);
      travellerDetails.setName(name);

      Long startContactNo = 6000000000L;
      Long endContactNo = 7000000000L;

      String contactNo =
          Long.toString(RandomValues.getRandomNumberBetween(startContactNo, endContactNo));

      travellerElements.getTravellerMobileNo(traveller).sendKeys(contactNo);
      travellerDetails.setContactNo(contactNo);

      String idType = "PASSPORT";
      PageHandler.selectFromDropDown(travellerElements.getTravellerIdType(traveller), idType);
      travellerDetails.setIdType(idType);

      String idNo = RandomValues.getRandomAlphaNumericString();
      travellerElements.getTravellerIdNo(traveller).sendKeys(idNo);
      travellerDetails.setIdNo(idNo);

      travellersList.add(travellerDetails);
    }
    return travellersList;
  }

  private void validateJourneyDetails(TravellersPageElements travellerElements,
      TrainBookingDetails trainBookingDetails) {

    Log.info(testId,
        "---------------           Journey Details Validation             -------------");
    int onwardJourney = 1;
    int returnJourney = 2;
    boolean validationFlag =
        validateOneWayJourney(onwardJourney, travellerElements,
            trainBookingDetails.getOnwardTrainDetails());

    CustomAssert.assertTrue(testId, validationFlag, "Onward Journey Details Validated.",
        "Onward Journey details validation failed.");

    if (trainBookingDetails.getJourneyType() == Journey.ROUND_TRIP) {
      validationFlag =
          validateOneWayJourney(returnJourney, travellerElements,
              trainBookingDetails.getReturnTrainDetails());

      CustomAssert.assertTrue(testId, validationFlag, "Return Journey Details Validated.",
          "Return Journey details validation failed.");
    }

    Log.divider(testId);

  }

  private boolean validateOneWayJourney(int journeyType, TravellersPageElements travellerElements,
      TrainDetails trainDetails) {
    boolean validationFlag = true;
    boolean flag = true;

    WebElement journeyDiv = travellerElements.getJourneyDiv(journeyType);

    List<String> cityList =
        StringUtilities.split(travellerElements.getCityName(journeyDiv).getText(), " to ");
    String expectedTrainName = travellerElements.getTrainName(journeyDiv).getText();
    String expectedTrainCoach =
        travellerElements.getTrainCoach(journeyDiv).getText().replace("Class: ", "");
    List<WebElement> trainTimeList = travellerElements.getTrainTimeDetails(journeyDiv);

    String expectedText = trainDetails.getDepartStation();
    String actualText = cityList.get(0);

    flag = StringUtils.equalsIgnoreCase(expectedText, actualText);

    CustomAssert.assertVerify(testId, flag, "Departure city didn't match. Expected: "
        + expectedText + " Actual: " + actualText);
    validationFlag &= flag;

    expectedText = trainDetails.getArrivalStation();
    actualText = cityList.get(1);

    flag = StringUtils.equalsIgnoreCase(expectedText, actualText);

    CustomAssert.assertVerify(testId, flag, "Arrival city didn't match. Expected: " + expectedText
        + " Actual: " + actualText);
    validationFlag &= flag;

    String actualTrainName = trainDetails.getTrainName();
    flag = StringUtils.equalsIgnoreCase(expectedTrainName, actualTrainName);

    CustomAssert.assertVerify(testId, flag, "Train Name didn't match. Expected: "
        + expectedTrainName + " Actual: " + actualTrainName);
    validationFlag &= flag;

    String actualTrainCoach = trainDetails.getCoachType();
    flag = StringUtils.equalsIgnoreCase(expectedTrainCoach, actualTrainCoach);

    CustomAssert.assertVerify(testId, flag, "Train Coach didn't match. Expected: "
        + expectedTrainCoach + " Actual: " + actualTrainCoach);
    validationFlag &= flag;

    String actualTime = trainTimeList.get(0).getText();
    String expectedTime = trainDetails.getDepartTime();

    flag = StringUtils.equals(actualTime, expectedTime);

    CustomAssert.assertVerify(testId, flag, "Departure time didn't match. Expected: "
        + expectedTime + " Actual: " + actualTime);
    validationFlag &= flag;

    actualTime = trainTimeList.get(1).getText();
    expectedTime = trainDetails.getArrivalTime();

    flag = StringUtils.equals(actualTime, expectedTime);

    CustomAssert.assertVerify(testId, flag, "Arrival time didn't match. Expected: " + expectedTime
        + " Actual: " + actualTime);
    validationFlag &= flag;

    return validationFlag;
  }

  private void validateHeaderDetails(TravellersPageElements travellerElements,
      TrainBookingDetails trainBookingDetails) {
    Log.info(testId,
        "---------------         Travellers Page Header Validation         -------------");

    boolean validationFlag = true;

    List<WebElement> cityCode = travellerElements.getCityCode();
    List<String> journeyDate =
        StringUtilities.split(travellerElements.getJourneyDate().getText(), Constant.SLASH);
    List<String> paxDetails =
        StringUtilities.split(travellerElements.getPaxDetails().getText(), Constant.WHITESPACE);

    String expectedText = trainBookingDetails.getSourceCode();
    String actualText = cityCode.get(0).getText();

    boolean flag = StringUtils.equalsIgnoreCase(expectedText, actualText);
    validationFlag &= flag;
    CustomAssert.assertVerify(testId, flag, "Source city Code didn't match. Expected : "
        + expectedText + " Actual : " + actualText);

    expectedText = trainBookingDetails.getDestinationCode();
    actualText = cityCode.get(1).getText();

    flag = StringUtils.equalsIgnoreCase(expectedText, actualText);
    validationFlag &= flag;

    CustomAssert.assertVerify(testId, flag, "Source city Code didn't match. Expected : "
        + expectedText + " Actual : " + actualText);

    Calendar actualDate = null;

    try {
      actualDate = CalendarUtils.dateStringToCalendarDate(journeyDate.get(0), HEADER_CAL_FORMAT);
    } catch (ParseException e) {
      CustomAssert.assertFail(testId, "Onward Calendar Can't be parsed.");
    }

    Calendar expectedDate = trainBookingDetails.getOnwardDate();

    flag = DateUtils.isSameDay(actualDate, expectedDate);
    validationFlag &= flag;

    CustomAssert.assertVerify(expectedText, flag, "Onward Date didn't match. Expected : "
        + CalendarUtils.calendarDateToDateString(expectedDate, HEADER_CAL_FORMAT) + " Actual: "
        + journeyDate.get(0));

    flag = (trainBookingDetails.getJourneyType() == Journey.ROUND_TRIP && journeyDate.size() == 2);
    if (flag) {
      try {
        actualDate = CalendarUtils.dateStringToCalendarDate(journeyDate.get(1), HEADER_CAL_FORMAT);
      } catch (ParseException e) {
        CustomAssert.assertFail(testId, "Return Calendar Can't be parsed.");
      }
      expectedDate = trainBookingDetails.getReturnDate();
      CustomAssert.assertVerify(expectedText, flag, "Onward Date didn't match. Expected : "
          + CalendarUtils.calendarDateToDateString(expectedDate, HEADER_CAL_FORMAT) + " Actual: "
          + journeyDate.get(1));
    }

    int actualAdultCount = 0;
    int actualInfantCount = 0;

    if (paxDetails.size() > 1 && StringUtils.equalsIgnoreCase("adults", paxDetails.get(0))) {
      actualAdultCount = NumberUtils.toInt(paxDetails.get(1));
    }
    if (paxDetails.size() > 2 && StringUtils.equalsIgnoreCase("infants", paxDetails.get(2))) {
      actualInfantCount = NumberUtils.toInt(paxDetails.get(3));
    }

    int expectedAdultCount = trainBookingDetails.getAdultCount();
    int expectedInfantCount = trainBookingDetails.getInfantCount();

    flag = (expectedAdultCount == actualAdultCount);
    validationFlag &= flag;

    CustomAssert.assertVerify(testId, flag, "Adult count didn't match. Expected : "
        + expectedAdultCount + " Actual : " + actualAdultCount);

    flag = (expectedInfantCount == actualInfantCount);
    validationFlag &= flag;

    CustomAssert.assertVerify(testId, flag, "Infant count didn't match. Expected : "
        + expectedInfantCount + " Actual : " + actualInfantCount);

    CustomAssert.assertTrue(testId, validationFlag, "Travellers Page Header Details Validated.",
        "Travellers Page Header Validation Failed.");

  }

  private void sleep(int milliseconds) {
    try {
      Thread.sleep(milliseconds);
    } catch (Exception e) {
      CustomAssert.assertFail(testId, "Interrupt occured.");
    }
  }
}
