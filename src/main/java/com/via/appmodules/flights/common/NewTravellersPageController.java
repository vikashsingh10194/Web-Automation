package com.via.appmodules.flights.common;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.via.pageobjects.flights.common.FlightBookingDetails;
import com.via.pageobjects.flights.common.NewTravellersPageElements;
import com.via.pageobjects.flights.common.TravellerDetails;
import com.via.utils.Constant;
import com.via.utils.Constant.Flight;
import com.via.utils.Constant.Journey;
import com.via.utils.Constant.VIA_COUNTRY;
import com.via.utils.CustomAssert;
import com.via.utils.Log;
import com.via.utils.NumberUtility;
import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;

public class NewTravellersPageController {

  public static List<TravellerDetails> execute(VIA_COUNTRY countryCode, Flight flightType,
      WebDriver driver, RepositoryParser repositoryParser, FlightBookingDetails flightBookingDetails) {

    String testId = flightBookingDetails.getTestCaseId();

    Log.info(testId, "::::::::::::::        Travellers Details Page       ::::::::::::::");
    Log.divider(testId);

    PageHandler.waitForDomLoad(testId, driver);
    /*
     * Handle Dynamic changes when loading TDP page.
     */
    double updatedFare = updateDynamicFareChanges(countryCode, driver);
    if (updatedFare != -1 && updatedFare!=0) {
      flightBookingDetails.setTotalFare(updatedFare);
    }

    Log.info(
        testId,
        "Payable Amount at TDP: "
            + NumberUtility.getRoundedAmount(countryCode, flightBookingDetails.getTotalFare()));

    Log.divider(testId);
    Log.info(testId, "---------    Travellers Page Header Validation    ---------");

    /*
     * Validate TDP header.For example source, destination, date, etc.
     */
    boolean oneLinerSnapshotValidator =
        NewFlightHeaderValidator.newValidateTravellersOneLinerSnapshot(countryCode, driver,
            repositoryParser, flightBookingDetails);
    CustomAssert.assertTrue(testId, oneLinerSnapshotValidator,
        "Travellers Details Page Header Validated.",
        "Error in validating Travellers Details Page Header.");
    Log.divider(testId);


    /*
     * Initialize all TDP Page elements.
     */
    NewTravellersPageElements travellersElements =
        new NewTravellersPageElements(testId, driver, repositoryParser);

    /*
     * Validates onward journey.Appearing on top of TDP.
     */
    NewTravellersPageValidator newTravellersPageValidator = new NewTravellersPageValidator(driver);
    
    if (flightBookingDetails.getJourneyType() == Journey.ONE_WAY) {
      Log.info(testId, "------------    One Way Flight Details Validation    ---------");
      travellersElements.expandFlightsDetails().click();
     PageHandler.sleep(testId, 1000L);
      //Validates onward journey.
      boolean onwardJourneyValidator =
          newTravellersPageValidator.newValidateOnwardJourney(travellersElements,
              flightBookingDetails);
      
      travellersElements.collapseFlightsDetails().click();
      CustomAssert.assertTrue(testId, onwardJourneyValidator, "One Way Journey Details Validated",
          "Error in validating one way Journey Details");
    }

    /*
     * Validates onward and return journey in case of Round trip.Appearing on top of TDP.
     */
    if (flightBookingDetails.getJourneyType() == Journey.ROUND_TRIP) {
      travellersElements.expandFlightsDetails().click();
      PageHandler.sleep(testId, 2 * 1000L);
      //Onward journey validation.
      Log.info(testId, "-----------    Onward Flight Details Validation   ------------");      
      boolean onwardJourneyValidator =
          newTravellersPageValidator.newValidateOnwardJourney(travellersElements,
              flightBookingDetails);
      CustomAssert.assertTrue(testId, onwardJourneyValidator, "Onward Journey Details Validated",
          "Error in validating onward Journey Details");
      Log.divider(testId);
      
      //Return journey validation.
      Log.info(testId, "-----------     Return Flight Details Validation    ----------");     
      boolean returnJourneyValidator =
          newTravellersPageValidator.newValidateReturnJourney(travellersElements,
              flightBookingDetails);
      CustomAssert.assertTrue(testId, returnJourneyValidator, "Return Journey details Validated.",
          "Error in validating return Journey details.");

      //travellersElements.collapseFlightsDetails().click();
      PageHandler.javaScriptExecuterClick(driver, travellersElements.collapseFlightsDetails());
    }

    Log.divider(testId);

    /*
     * Initialize TravellersDetailsInput object.
     */
    NewTravellersDetailsInput travellersDetailsInput =
        new NewTravellersDetailsInput(testId, countryCode, driver, repositoryParser,
            flightBookingDetails);

    /*
     * Execute Travelers Details Input and Creating List of TravellerDetails(Depends on No. of travelers).
     */
    List<TravellerDetails> travellerDetailsList =
        travellersDetailsInput.newExecute(flightType, travellersElements);

    Log.divider(testId);

    return travellerDetailsList;
  }

 /*
  * Used to update the dynamic fare changes while switching through the pages
  */
  private static double updateDynamicFareChanges(VIA_COUNTRY countryCode, WebDriver driver) {
    double totalFare =0;
    try {
      PageHandler.setImplicitWaitTime(driver, 1 * 1000);
      WebElement repriceContinueButton = driver.findElement(By.id("modalConfirmCTA"));
      WebElement amountElement = driver.findElement(By.className("alertHead"));
      List<String> alertMessage = Arrays.asList(amountElement.getText().split(Constant.WHITESPACE));
      String totalFareString = alertMessage.get(6).substring(0, alertMessage.get(6).length() - 1);
      totalFare = NumberUtility.getAmountFromString(countryCode, totalFareString);
      repriceContinueButton.click();
    } catch (Exception e) {
      return -1;
    }
    PageHandler.setImplicitWaitTime(driver, 10 * 000);
    return totalFare;
  }
}
