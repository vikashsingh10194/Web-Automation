package com.via.appmodules.flights.domestic;

import java.util.List;

import lombok.AllArgsConstructor;

import org.apache.commons.lang.math.NumberUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.via.appmodules.flights.common.FlightHeaderValidator;
import com.via.pageobjects.flights.common.FlightBookingDetails;
import com.via.pageobjects.flights.domestic.FlightResultContainerElements;
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
        "Search Result Header details validated.", "Search Result Header Validation failed.");
    Log.divider(testId);

    FlightResultContainerElements flightSearchElements =
        new FlightResultContainerElements(testId, driver, repositoryParser);

    // Added to wait for page load...
    PageHandler.waitForPageLoad(testId, driver);
    scrollToBottom(flightSearchElements);

    /*** Selection of flight ***/
    FlightSelector flightSelector =
        new FlightSelector(testId, countryCode, media, driver, repositoryParser);
    flightBookingDetails =
        flightSelector.flightSelection(flightSearchElements, flightBookingDetails);
    Log.divider(testId);
    return flightBookingDetails;
  }

  public void scrollToBottom(FlightResultContainerElements flightSearchElement) {
    /*** get flight count elements for one way or round trip journey. ***/

    int totalResult = 0;
    try {
      List<WebElement> totalFlightCountList = flightSearchElement.getTotalFlightCount();
      /*** get flight count from first flight count element ***/
      totalResult = NumberUtils.toInt(totalFlightCountList.get(0).getText());

      /** if journey is round trip ***/
      if (totalFlightCountList.size() == 2) {
        /*** store return flight count. ***/
        totalResult += NumberUtils.toInt(totalFlightCountList.get(1).getText());
      }
    } catch (Exception e) {
      CustomAssert.assertFail(testId, "No flight found for this sector");
    }

    /***
     * if loaded flight count same as the current flight count then return true since complete dom
     * is loaded
     ***/
    JavascriptExecutor js = (JavascriptExecutor) driver;
    int scrollCount = totalResult / 30;
    int count = 1;

    /*** load dom with 25 new flights in one scroll ***/
    int scrollIndex = 0;
    try {
      while (count <= scrollCount) {
        scrollIndex += 20;
        WebElement scrolledElement = flightSearchElement.getFlightResultDiv(scrollIndex);
        js.executeScript("arguments[0].scrollIntoView(true);", scrolledElement);

        // Method jSExecuterScrolldown() automated in PageHandler....
        // PageHandler.jSExecuterScrolldown(driver, scrolledElement);
        for (int i = 0; i < 2; i++) {
          scrollIndex += 5;
          if (scrollIndex <= totalResult) {
            scrolledElement = flightSearchElement.getFlightResultDiv(scrollIndex);
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
