package com.via.appmodules.flights.common;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.via.pageobjects.flights.common.FlightBookingDetails;
import com.via.pageobjects.flights.common.TravellerDetails;
import com.via.pageobjects.flights.common.TravellersPageElements;
import com.via.utils.Constant;
import com.via.utils.Constant.Flight;
import com.via.utils.Constant.Journey;
import com.via.utils.Constant.VIA_COUNTRY;
import com.via.utils.CustomAssert;
import com.via.utils.Log;
import com.via.utils.NumberUtility;
import com.via.utils.RepositoryParser;

public class TravellersPageController {

  public static List<TravellerDetails> execute(VIA_COUNTRY countryCode, Flight flightType,
      WebDriver driver, RepositoryParser repositoryParser, FlightBookingDetails flightBookingDetails) {

    String testId = flightBookingDetails.getTestCaseId();

    Log.info(testId, "::::::::::::::        Travellers Details Page       ::::::::::::::");
    Log.divider(testId);

    double updatedFare = updateDynamicFareChanges(countryCode, driver);
    if (updatedFare != -1) {
      flightBookingDetails.setTotalFare(updatedFare);
    }

    Log.info(
        testId,
        "Payable Amount at TDP: "
            + NumberUtility.getRoundedAmount(countryCode, flightBookingDetails.getTotalFare()));

    Log.divider(testId);
    Log.info(testId, "---------    Travellers Page Header Validation    ---------");

    boolean oneLinerSnapshotValidator =
        FlightHeaderValidator.validateTravellersOneLinerSnapshot(countryCode, driver,
            repositoryParser, flightBookingDetails);
    CustomAssert.assertTrue(testId, oneLinerSnapshotValidator,
        "Travellers Details Page Header Validated.",
        "Error in validating Travellers Details Page Header.");
    Log.divider(testId);

    TravellersPageElements travellersElements =
        new TravellersPageElements(testId, driver, repositoryParser);

    // Validates the onward journey details appearing on left part of page

    if (flightBookingDetails.getJourneyType() == Journey.ONE_WAY) {
      boolean onwardJourneyValidator =
          TravellersPageValidator.validateOnwardJourney(driver,travellersElements, flightBookingDetails);
      Log.info(testId, "------------    One Way Flight Details Validation    ---------");

      CustomAssert.assertTrue(testId, onwardJourneyValidator, "One Way Journey Details Validated",
          "Error in validating one way Journey Details");
    }

    // Validates the return journey details appearing on left part of page
    // in case of round trip
    if (flightBookingDetails.getJourneyType() == Journey.ROUND_TRIP) {
      Log.info(testId, "-----------    Onward Flight Details Validation   ------------");

      boolean onwardJourneyValidator =
          TravellersPageValidator.validateOnwardJourney(driver,travellersElements, flightBookingDetails);
      CustomAssert.assertTrue(testId, onwardJourneyValidator, "Onward Journey Details Validated",
          "Error in validating onward Journey Details");
      Log.divider(testId);

      Log.info(testId, "-----------     Return Flight Details Validation    ----------");

      boolean returnJourneyValidator =
          TravellersPageValidator.validateReturnJourney(driver,travellersElements, flightBookingDetails);
      CustomAssert.assertTrue(testId, returnJourneyValidator, "Return Journey details Validated.",
          "Error in validating return Journey details.");
    }

    Log.divider(testId);

    TravellersDetailsInput travellersDetailsInput =
        new TravellersDetailsInput(testId, countryCode, driver, repositoryParser,
            flightBookingDetails);

    List<TravellerDetails> travellerDetailsList =
        travellersDetailsInput.execute(flightType, travellersElements);

    // Validates the passenger details after clicking make payment button
    Log.info(testId, "-------------     Passenger Details Validation     -----------");

    PassengerValidation passengerValidation =
        new PassengerValidation(testId, countryCode, driver, repositoryParser);
    passengerValidation.validatePassengers(flightType, flightBookingDetails, travellerDetailsList);

    Log.divider(testId);

    return travellerDetailsList;
  }

  // Used to update the dynamic fare changes while switching through the pages
  private static double updateDynamicFareChanges(VIA_COUNTRY countryCode, WebDriver driver) {
    try {
      WebElement repriceContinueButton = driver.findElement(By.id("repiceContBook"));
      WebElement amountElement = driver.findElement(By.xpath("//div[@class='newPrice']"));
      String amount = amountElement.getText().replaceAll("[^0-9.]", "");
      double totalFare = NumberUtility.getAmountFromString(countryCode, amount);
      repriceContinueButton.click();
      return totalFare;
    } catch (Exception e) {
      return -1;
    }
  }
}
