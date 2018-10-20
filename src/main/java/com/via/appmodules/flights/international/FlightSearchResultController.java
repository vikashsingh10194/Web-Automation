package com.via.appmodules.flights.international;


import lombok.AllArgsConstructor;

import org.apache.commons.lang.math.NumberUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.via.appmodules.flights.common.FlightHeaderValidator;
import com.via.pageobjects.flights.common.FlightBookingDetails;
import com.via.pageobjects.flights.international.FlightResultContainerElements;
import com.via.utils.Constant.BOOKING_MEDIA;
import com.via.utils.Constant.VIA_COUNTRY;
import com.via.utils.CustomAssert;
import com.via.utils.Log;
import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;

@AllArgsConstructor
public class FlightSearchResultController {

  private String testId;
  private VIA_COUNTRY countryCode;
  private WebDriver driver;
  private RepositoryParser repositoryParser;

  public FlightBookingDetails execute(BOOKING_MEDIA media, FlightBookingDetails flightBookingDetails) {

    Log.info(testId, ":::::::::::::::::     Search Result Page      :::::::::::::::::::");
    Log.divider(testId);
    /*** validate the flight header. ***/
    boolean flightHeaderValidation =
        FlightHeaderValidator.validateFlightsOneLinerSnapshot(driver, repositoryParser,
            flightBookingDetails);

    /*** if header validation failed further execution stops ***/
    CustomAssert.assertTrue(testId, flightHeaderValidation,
        "Search Page Header details validated.", "Search Page Header details Validation failed.");
    Log.divider(testId);

    FlightResultContainerElements flightResultElements =
        new FlightResultContainerElements(driver, repositoryParser, testId);
    // Added to wait for page load....
    PageHandler.waitForPageLoad(testId, driver);
    scrollToBottom(flightResultElements);
    FlightSelector flightSelector =
        new FlightSelector(testId, countryCode, media, driver, repositoryParser);
    flightSelector.selectFlight(flightResultElements, flightBookingDetails);
    return flightBookingDetails;
  }

  private void scrollToBottom(FlightResultContainerElements flightResultElements) {
    int totalResult = 0;
    try {
      WebElement totalResultDiv = flightResultElements.getTotalFlightCount();
      totalResult = NumberUtils.toInt(totalResultDiv.getText());
    } catch (Exception e) {
      CustomAssert.assertFail(testId, "No flight loaded for this sector.");
    }

    int totalScroll = totalResult / 30;
    int count = 1;
    JavascriptExecutor js = (JavascriptExecutor) driver;
    int scrollIndex = 0;
    // WebElement scrolledElement =null;
    try {
      while (count <= totalScroll) {
        scrollIndex += 20;
        WebElement scrolledElement = flightResultElements.getResultDiv(scrollIndex);
        js.executeScript("arguments[0].scrollIntoView(true);", scrolledElement);

        // Method jSExecuterScrolldown() automated in PageHandler....
        // PageHandler.jSExecuterScrolldown(driver, scrolledElement);

        for (int i = 0; i < 2; i++) {
          scrollIndex += 5;
          if (scrollIndex <= totalResult) {
            scrolledElement = flightResultElements.getResultDiv(scrollIndex);
            js.executeScript("arguments[0].scrollIntoView(true);", scrolledElement);
            // PageHandler.jSExecuterScrolldown(driver, scrolledElement);
          }
        }

        count++;
      }
    } catch (Exception e) {
    }

    PageHandler.sleep(testId, 2 * 1000L);
  }
}
