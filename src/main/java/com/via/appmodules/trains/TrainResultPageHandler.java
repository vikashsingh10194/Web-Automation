package com.via.appmodules.trains;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import lombok.AllArgsConstructor;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.via.pageobjects.trains.TrainBookingDetails;
import com.via.pageobjects.trains.TrainDetails;
import com.via.pageobjects.trains.TrainResultPageElements;
import com.via.utils.CalendarUtils;
import com.via.utils.Constant.Journey;
import com.via.utils.Constant.VIA_COUNTRY;
import com.via.utils.CustomAssert;
import com.via.utils.Log;
import com.via.utils.NumberUtility;
import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;

@AllArgsConstructor
public class TrainResultPageHandler {
  private String testId;
  private WebDriver driver;
  private RepositoryParser repositoryParser;

  private final String CAL_FORMAT = "EEE, MMM dd yyyy";
  private final String ONWARD_JOURNEY = "onward";
  private final String RETURN_JOURNEY = "return";

  public void execute(VIA_COUNTRY countryCode, TrainBookingDetails trainBookingDetails) {

    Log.info(testId, ":::::::::::::::::::::     Train Result Page      :::::::::::::::::::");

    TrainResultPageElements resultPageElement =
        new TrainResultPageElements(testId, driver, repositoryParser);

    // Added to load for page
    PageHandler.waitForPageLoad(testId, driver);

    scrollToBottom(resultPageElement);
    headerValidation(resultPageElement, trainBookingDetails);
    selectTrains(countryCode, resultPageElement, trainBookingDetails);

  }

  private void selectTrains(VIA_COUNTRY countryCode, TrainResultPageElements resultPageElement,
      TrainBookingDetails trainBookingDetails) {

    Log.info(testId, "---------------------    Onward Train Details   ---------------------");

    WebElement onwardDiv =
        resultPageElement.getResultDiv(ONWARD_JOURNEY, trainBookingDetails.getOnwardTrain());
    CustomAssert.assertTrue(testId, onwardDiv != null, "Onward Train not found.");

    TrainDetails trainDetails = getTrainDetails(onwardDiv, resultPageElement);
    trainBookingDetails.setOnwardTrainDetails(trainDetails);

    String price = resultPageElement.getPerAdultFare(onwardDiv).getText().replaceAll("[^0-9.]", "");
    Double onwardFare =
        trainBookingDetails.getAdultCount() * NumberUtility.getAmountFromString(countryCode, price);

    Log.info(testId,
        "Onward Total Fare :: " + NumberUtility.getRoundedAmount(countryCode, onwardFare));

    Log.divider(testId);

    WebElement selectBtn = resultPageElement.getSelectButton(onwardDiv);

    PageHandler.javaScriptExecuterClick(driver, selectBtn);

    if (trainBookingDetails.getJourneyType() == Journey.ONE_WAY) {
      trainBookingDetails.setTotalFare(onwardFare);
      return;
    }

    Double onwardFareAtSPanel =
        verifySelectionPanel(countryCode, ONWARD_JOURNEY, resultPageElement, trainDetails);

    CustomAssert.assertTrue(
        testId,
        onwardFare.equals(onwardFareAtSPanel),
        "Onward Fare didn't match at selection Panel. Expected : "
            + NumberUtility.getRoundedAmount(countryCode, onwardFare) + " Actual : "
            + NumberUtility.getRoundedAmount(countryCode, onwardFareAtSPanel));

    Log.info(testId, "---------------------    Return Train Details   ---------------------");

    WebElement returnDiv =
        resultPageElement.getResultDiv(RETURN_JOURNEY, trainBookingDetails.getReturnTrain());
    CustomAssert.assertTrue(testId, returnDiv != null, "Return Train not found.");
    trainDetails = getTrainDetails(returnDiv, resultPageElement);
    trainBookingDetails.setReturnTrainDetails(trainDetails);

    price = resultPageElement.getPerAdultFare(returnDiv).getText().replaceAll("[^0-9.]", "");
    Double returnFare =
        trainBookingDetails.getAdultCount() * NumberUtility.getAmountFromString(countryCode, price);

    Log.info(testId,
        "Return Total Fare :: " + NumberUtility.getRoundedAmount(countryCode, returnFare));

    selectBtn = resultPageElement.getSelectButton(returnDiv);

    PageHandler.javaScriptExecuterClick(driver, selectBtn);

    Double returnFareAtSPanel =
        verifySelectionPanel(countryCode, RETURN_JOURNEY, resultPageElement, trainDetails);

    CustomAssert.assertTrue(
        testId,
        returnFare.equals(returnFareAtSPanel),
        "Return Fare didn't match at selection Panel. Expected : "
            + NumberUtility.getRoundedAmount(countryCode, returnFare) + " Actual : "
            + NumberUtility.getRoundedAmount(countryCode, returnFareAtSPanel));

    Log.divider(testId);

    String totalFareString =
        resultPageElement.getTotalFareFromSelectionPanel().getText().replaceAll("[^0-9.]", "");

    Double totalFare = NumberUtility.getAmountFromString(countryCode, totalFareString);

    Log.info(testId,
        "Total Fare at Book Panel :: " + NumberUtility.getRoundedAmount(countryCode, totalFare));

    Double diff = totalFare - (onwardFare + returnFare);

    CustomAssert.assertTrue(testId, Math.abs(diff) < 0.1, "Total Fare didn't match at book Panel.");

    trainBookingDetails.setTotalFare(totalFare);

    WebElement bookBtn = resultPageElement.getBookButton();

    PageHandler.javaScriptExecuterClick(driver, bookBtn);

  }

  private Double verifySelectionPanel(VIA_COUNTRY countryCode, String journeyType,
      TrainResultPageElements resultPageElement, TrainDetails trainDetails) {
    Double totalFare = null;
    WebElement selectionDiv = resultPageElement.getSelectionPanel(journeyType);
    String actualText = resultPageElement.getTrainNameAtSelectionPanel(selectionDiv).getText();
    String expectedText = trainDetails.getTrainName();

    CustomAssert.assertTrue(testId, StringUtils.equalsIgnoreCase(actualText, expectedText),
        "Train Name not matched at selection Panel. Expected : " + expectedText + " Actual : "
            + actualText);

    actualText = resultPageElement.getCoachTypeFromSelectionPanel(selectionDiv).getText();
    expectedText = trainDetails.getCoachType();
    CustomAssert.assertTrue(testId, StringUtils.equalsIgnoreCase(actualText, expectedText),
        "Train Class not matched at selection Panel. Expected : " + expectedText + " Actual : "
            + actualText);

    List<WebElement> trainTime = resultPageElement.getTimeListFromSelectionPanel(selectionDiv);

    actualText = trainTime.get(0).getText();
    expectedText = trainDetails.getDepartTime();

    CustomAssert.assertTrue(testId, StringUtils.equalsIgnoreCase(actualText, expectedText),
        "Departure Time not matched at selection Panel. Expected : " + expectedText + " Actual : "
            + actualText);

    actualText = trainTime.get(1).getText();
    expectedText = trainDetails.getArrivalTime();

    CustomAssert.assertTrue(testId, StringUtils.equalsIgnoreCase(actualText, expectedText),
        "Arrival Time not matched at selection Panel. Expected : " + expectedText + " Actual : "
            + actualText);

    String price =
        resultPageElement.getFareAtSelectionPanel(selectionDiv).getText().replaceAll("[^0-9.]", "");

    totalFare = NumberUtility.getAmountFromString(countryCode, price);

    return totalFare;
  }

  private TrainDetails getTrainDetails(WebElement resultDiv,
      TrainResultPageElements resultPageElement) {
    TrainDetails trainDetails = new TrainDetails();
    WebElement trainName = resultPageElement.getTrainName(resultDiv);
    WebElement trainClass = resultPageElement.getTrainClass(resultDiv);
    List<WebElement> timeList = resultPageElement.getTrainTime(resultDiv);
    List<WebElement> stationList = resultPageElement.getTrainStations(resultDiv);

    Log.info(testId, "--------------------         Train Details        -----------------");
    String trainNameString = trainName.getText();
    String coachTypeString = trainClass.getText();
    String departTime = timeList.get(0).getText();
    String arrivalTime = timeList.get(1).getText();
    String departStation = stationList.get(0).getText();
    String arrivalStation = stationList.get(1).getText();

    trainDetails.setTrainName(trainNameString);
    trainDetails.setCoachType(coachTypeString);
    trainDetails.setDepartTime(departTime);
    trainDetails.setArrivalTime(arrivalTime);

    trainDetails.setDepartStation(departStation);
    trainDetails.setArrivalStation(arrivalStation);

    Log.info(testId, "Train Name :: " + trainNameString);
    Log.info(testId, "Coach Type :: " + coachTypeString);
    Log.info(testId, "Source Station :: " + departStation);
    Log.info(testId, "Detiantion Station :: " + arrivalStation);
    Log.info(testId, "Departure Time :: " + departTime);
    Log.info(testId, "Arrival Time :: " + arrivalTime);

    return trainDetails;
  }

  private void scrollToBottom(TrainResultPageElements resultPageElement) {

    List<WebElement> trainCountList = resultPageElement.getTrainCountList();
    int totalResults = NumberUtils.toInt(trainCountList.get(0).getText());
    int scrollFactor = 1;

    if (trainCountList.size() == 2) {
      totalResults += NumberUtils.toInt(trainCountList.get(1).getText());
      scrollFactor = 2;
    }

    int totalScroll = totalResults / (25 * scrollFactor);
    int count = 1;

    JavascriptExecutor js = (JavascriptExecutor) driver;

    while (count <= totalScroll) {
      WebElement element = resultPageElement.getTrainInfoIndexedAt(scrollFactor * count * 25);
      js.executeScript("arguments[0].scrollIntoView(true);", element);
      count++;
    }
  }

  private void headerValidation(TrainResultPageElements resultPageElement,
      TrainBookingDetails trainBookingDetails) {
    boolean validationFlag = true;

    List<WebElement> cityCodeList = resultPageElement.getCityCodeElements();
    List<WebElement> cityNameList = resultPageElement.getCityNameElements();

    String expectedText = trainBookingDetails.getSourceCode();
    String actualText = cityCodeList.get(0).getText();

    if (!StringUtils.equals(expectedText, actualText)) {
      Log.error(driver, testId, "Source City Code didn't match. :: Expected : " + expectedText
          + " Actual : " + actualText);
      validationFlag = false;
    }

    expectedText = trainBookingDetails.getSourceCity();
    actualText = cityNameList.get(0).getText();

    if (!StringUtils.equals(expectedText, actualText)) {
      Log.error(driver, testId, "Source City Name didn't match. :: Expected : " + expectedText
          + " Actual : " + actualText);
      validationFlag = false;
    }

    expectedText = trainBookingDetails.getDestinationCode();
    actualText = cityCodeList.get(1).getText();

    if (!StringUtils.equals(expectedText, actualText)) {
      Log.error(driver, testId, "Destination City Code didn't match. :: Expected : " + expectedText
          + " Actual : " + actualText);
      validationFlag = false;
    }

    expectedText = trainBookingDetails.getDestinationCity();
    actualText = cityNameList.get(1).getText();

    if (!StringUtils.equals(expectedText, actualText)) {
      Log.error(driver, testId, "Destination City Name didn't match. :: Expected : " + expectedText
          + " Actual : " + actualText);
      validationFlag = false;
    }

    validationFlag &=
        verifyDate("Onward", trainBookingDetails.getOnwardDate(), resultPageElement.getOnwardDate());
    if (trainBookingDetails.getJourneyType() == Journey.ROUND_TRIP) {
      validationFlag &=
          verifyDate("Return", trainBookingDetails.getReturnDate(),
              resultPageElement.getReturnDate());
    }

    validationFlag &= verifyPaxCount(resultPageElement, trainBookingDetails);

    CustomAssert.assertTrue(testId, validationFlag, "Header Details Successfully Validated.",
        "Header Validation Failed.");
  }

  private boolean verifyPaxCount(TrainResultPageElements resultPageElement,
      TrainBookingDetails trainBookingDetails) {
    boolean validationFlag = true;

    List<WebElement> paxList = resultPageElement.getPaxDetails();

    int expectedCount = trainBookingDetails.getAdultCount();
    int actualCount = NumberUtils.toInt(paxList.get(0).getText());

    if (expectedCount != actualCount) {
      Log.error(driver, testId, "Adult count didn't match. :: Expected : " + expectedCount + " Actual :"
          + actualCount);
      validationFlag = false;
    }

    expectedCount = trainBookingDetails.getInfantCount();
    actualCount = NumberUtils.toInt(paxList.get(1).getText());

    if (expectedCount != actualCount) {
      Log.error(driver, testId, "Infant count didn't match. :: Expected : " + expectedCount + " Actual :"
          + actualCount);
      validationFlag = false;
    }

    return validationFlag;
  }

  private boolean verifyDate(String dateType, Calendar expectedDate, WebElement actualElement) {
    Calendar actualDate = null;
    try {
      actualDate =
          CalendarUtils.dateStringToCalendarDate(StringUtils.trim(actualElement.getText()),
              CAL_FORMAT);
    } catch (ParseException e) {
      CustomAssert.assertFail(testId, dateType + " Calendar Can't be parsed.");
    }

    if (!DateUtils.isSameDay(actualDate, expectedDate)) {
      Log.info(
          testId,
          dateType + " Date didnt match. :: Expected : "
              + CalendarUtils.getFormattedDate(expectedDate) + " Actual : "
              + CalendarUtils.getFormattedDate(actualDate));
      return false;
    }
    return true;
  }
}
