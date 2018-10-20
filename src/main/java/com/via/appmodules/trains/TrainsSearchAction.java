package com.via.appmodules.trains;

import java.util.Calendar;
import java.util.Map;

import lombok.AllArgsConstructor;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.via.pageobjects.trains.TrainBookingDetails;
import com.via.pageobjects.trains.TrainSearchElements;
import com.via.testcases.common.TestCaseExcelConstant;
import com.via.utils.CalendarUtils;
import com.via.utils.Constant;
import com.via.utils.Constant.Journey;
import com.via.utils.Constant.TRAIN_TYPE;
import com.via.utils.CustomAssert;
import com.via.utils.Log;
import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;

@AllArgsConstructor
public class TrainsSearchAction {
  private String testId;
  private WebDriver driver;
  private RepositoryParser repositoryParser;

  private final String RAILINK = "railink";
  private final String KAI = "kai";
  private static final String SOURCE_LIST = "ui-id-1";
  private static final String DESTINATION_LIST = "ui-id-2";

  public TrainBookingDetails execute(Map<Integer, String> testData) {
    TrainBookingDetails trainBookingDetails = getTrainBookingDetails(testData);

    TrainSearchElements searchPageElement =
        new TrainSearchElements(testId, driver, repositoryParser);

    searchPageElement.getTrainSearchPage().click();

    PageHandler.waitForDomLoad(testId, driver);

    searchPageElement.getOneWayTab().click();

    if (trainBookingDetails.getJourneyType() == Journey.ROUND_TRIP) {
      searchPageElement.getRoundTripTab().click();
    }

    if (trainBookingDetails.getTrainType() == TRAIN_TYPE.RAILINK) {
      searchPageElement.getTrainTypeRadio(RAILINK).click();
    } else {
      searchPageElement.getTrainTypeRadio(KAI).click();
    }

    String source = trainBookingDetails.getSourceCity();
    WebElement sourceElement = searchPageElement.getSourceCity();
    sourceElement.clear();
    sourceElement.sendKeys(source);
    WebElement sourceAutoComplete = searchPageElement.getStationElement(SOURCE_LIST, source);
    CustomAssert.assertTrue(testId, sourceAutoComplete != null,
        "Error in finding source auto complete.");
    trainBookingDetails.setSourceCode(searchPageElement.getStationCodeElement(sourceAutoComplete)
        .getText());
    sourceAutoComplete.click();

    String destination = trainBookingDetails.getDestinationCity();
    WebElement destinationElement = searchPageElement.getDestinationCity();
    destinationElement.clear();
    destinationElement.sendKeys(destination);
    WebElement destinationAutoComplete =
        searchPageElement.getStationElement(DESTINATION_LIST, destination);
    CustomAssert.assertTrue(destination, destinationAutoComplete != null,
        "Error in finding destination auto complete.");
    trainBookingDetails.setDestinationCode(searchPageElement.getStationCodeElement(
        destinationAutoComplete).getText());
    destinationAutoComplete.click();

    WebElement departureDate =
        CalendarUtils.selectDate(testId, driver, repositoryParser,
            trainBookingDetails.getOnwardDate(), Constant.DEPART_CAL_ID);
    CustomAssert.assertTrue(departureDate != null, "Unable to select departure date");
    departureDate.click();

    // Return Date
    if (trainBookingDetails.getJourneyType() == Journey.ROUND_TRIP) {
      searchPageElement.getReturnCalendar().click();
      WebElement returnDate =
          CalendarUtils.selectDate(testId, driver, repositoryParser,
              trainBookingDetails.getReturnDate(), Constant.RETURN_CAL_ID);
      CustomAssert.assertTrue(testId, returnDate != null, "Unable to select return date");
      returnDate.click();
    }

    // Passenger Count
    setAdultsCount(searchPageElement, trainBookingDetails.getAdultCount());
    setInfantsCount(searchPageElement, trainBookingDetails.getInfantCount());

    // Search Flights Button
    WebElement searchFlight = searchPageElement.searchButton();
    CustomAssert.assertTrue(searchFlight != null, "Unable to find search flight button");
    searchFlight.click();

    printSearchDetailsInLogFile(trainBookingDetails);

    return trainBookingDetails;
  }

  private void printSearchDetailsInLogFile(TrainBookingDetails trainBookingDetails) {
    Log.info(testId, ":::::::::::::::::::      Train Search Home Page      :::::::::::::::::");
    Log.divider(testId);
    Log.info(testId, "Journey type :: " + trainBookingDetails.getJourneyType().toString());
    Log.info(testId, "Train Type :: " + trainBookingDetails.getTrainType().toString());
    Log.info(testId, "Source City :: " + trainBookingDetails.getSourceCity() + " ("
        + trainBookingDetails.getSourceCode() + ")");
    Log.info(testId, "Destination City :: " + trainBookingDetails.getDestinationCity() + " ("
        + trainBookingDetails.getDestinationCode() + ")");

    Log.info(testId,
        "Onward Date :: " + CalendarUtils.getFormattedDate(trainBookingDetails.getOnwardDate()));

    if (trainBookingDetails.getJourneyType() == Journey.ROUND_TRIP) {
      Log.info(testId,
          "Return Date :: " + CalendarUtils.getFormattedDate(trainBookingDetails.getReturnDate()));
    }

    Log.info(testId, "Adult(s) :: " + trainBookingDetails.getAdultCount());
    Log.info(testId, "Infant(s) :: " + trainBookingDetails.getInfantCount());

    Log.divider(testId);

  }

  private static void setAdultsCount(TrainSearchElements searchPageElement, int targetCount) {
    WebElement adultCountMinus = searchPageElement.getAdultCountMinus();
    WebElement adultCount = searchPageElement.getAdultCount();
    WebElement adultCountPlus = searchPageElement.getAdultCountPlus();
    setPassengerCount(adultCountMinus, adultCount, adultCountPlus, targetCount);
  }

  private static void setInfantsCount(TrainSearchElements searchPageElement, int targetCount) {
    WebElement infantCountMinus = searchPageElement.getInfantCountMinus();
    WebElement infantCount = searchPageElement.getInfantCount();
    WebElement infantCountPlus = searchPageElement.getInfantCountPlus();
    setPassengerCount(infantCountMinus, infantCount, infantCountPlus, targetCount);
  }

  private static void setPassengerCount(WebElement decreaseCountElement, WebElement countElement,
      WebElement increaseCountElement, int targetCount) {
    WebElement element = null;
    int currCount = NumberUtils.toInt(countElement.getText());
    int diff = targetCount - currCount;

    while (diff != 0) {
      if (diff > 0) {
        element = increaseCountElement;
        diff--;
      } else {
        element = decreaseCountElement;
        diff++;
      }
      element.click();
    }
  }

  private TrainBookingDetails getTrainBookingDetails(Map<Integer, String> testData) {
    TrainBookingDetails trainBookingDetails = new TrainBookingDetails();

    int onwardAdd =
        NumberUtils.toInt(testData.get(TestCaseExcelConstant.COL_TRAIN_ONWARD_DATE_ADD));
    Calendar calendar = CalendarUtils.getCalendarIncrementedByDays(onwardAdd);

    trainBookingDetails.setOnwardDate(calendar);
    trainBookingDetails.setOnwardTrain(testData.get(TestCaseExcelConstant.COL_TRAIN_ONWARD_TRAIN));

    trainBookingDetails.setJourneyType(Journey.ONE_WAY);
    String returnAdd = testData.get(TestCaseExcelConstant.COL_TRAIN_RETURN_DATE_ADD);
    if (returnAdd != null) {
      trainBookingDetails.setJourneyType(Journey.ROUND_TRIP);
      calendar =
          CalendarUtils.getCalendarIncrementedByDays(onwardAdd + NumberUtils.toInt(returnAdd));
      trainBookingDetails.setReturnDate(calendar);
      trainBookingDetails
          .setReturnTrain(testData.get(TestCaseExcelConstant.COL_TRAIN_RETURN_TRAIN));
    }

    trainBookingDetails.setSourceCity(testData.get(TestCaseExcelConstant.COL_TRAIN_SOURCE));
    trainBookingDetails.setDestinationCity(testData
        .get(TestCaseExcelConstant.COL_TRAIN_DESTINATION));

    trainBookingDetails.setTrainType(TRAIN_TYPE.KAI);

    int infantCount = NumberUtils.toInt(testData.get(TestCaseExcelConstant.COL_TRAIN_INFANT));

    if (StringUtils.equalsIgnoreCase(testData.get(TestCaseExcelConstant.COL_TRAIN_TRAIN_TYPE),
        RAILINK)) {
      trainBookingDetails.setTrainType(TRAIN_TYPE.RAILINK);
      infantCount = 0;
    }

    trainBookingDetails.setAdultCount(NumberUtils.toInt(testData
        .get(TestCaseExcelConstant.COL_TRAIN_ADULT)));
    trainBookingDetails.setInfantCount(infantCount);

    String seatSelection = testData.get(TestCaseExcelConstant.COL_TRAIN_SEAT_SELECTION);
    if (seatSelection == null || StringUtils.equalsIgnoreCase(seatSelection, "no")) {
      trainBookingDetails.setManualSeatSelection(false);
    } else {
      trainBookingDetails.setManualSeatSelection(true);
    }

    String splitBooking = testData.get(TestCaseExcelConstant.COL_TRAIN_SPLIT_BOOKING);

    if (splitBooking != null || StringUtils.equalsIgnoreCase(splitBooking, "no")) {
      trainBookingDetails.setSplitBooking(false);
    } else {
      trainBookingDetails.setSplitBooking(true);
    }

    return trainBookingDetails;
  }
}
