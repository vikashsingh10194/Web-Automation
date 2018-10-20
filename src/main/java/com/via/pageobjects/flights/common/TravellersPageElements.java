package com.via.pageobjects.flights.common;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.via.pageobjects.common.PageElement;
import com.via.utils.Constant;
import com.via.utils.CustomAssert;
import com.via.utils.PageHandler;
import com.via.utils.RandomValues;
import com.via.utils.RepositoryParser;

public class TravellersPageElements extends PageHandler {
  private final String PAGE_NAME = "travellersInformation";
  private RepositoryParser repositoryParser;

  public TravellersPageElements(String testId, WebDriver driver, RepositoryParser repositoryParser) {
    super(testId, driver);
    this.repositoryParser = repositoryParser;
  }

  // Gets the pageObject and modifies the string "toModify" to the target
  // string in locator value
  public PageElement modifyPageElementOnce(String objectName, String modification) {
    PageElement element = repositoryParser.getPageObject(PAGE_NAME, objectName);
    PageElement modifiedElement = new PageElement(element);
    String locatorValue = modifiedElement.getLocatorValue();
    String modifiedLocatorValue = StringUtils.replace(locatorValue, "toModify", modification);
    modifiedElement.setLocatorValue(modifiedLocatorValue);
    return modifiedElement;
  }

  // This method does not use any CommonUtils method or any JSON repository
  public WebElement getElementById(String id) {
    WebElement element = driver.findElement(By.id(id));
    return element;
  }

  public WebElement getElementByPageObject(PageElement pageElement) {
    WebElement element = findElement(pageElement);
    Assert.assertTrue(element != null);
    return element;
  }

  public WebElement selectOptions(WebElement element, int targetValue) {
    List<WebElement> titles = element.findElements(By.tagName("option"));
    WebElement targetTitle = null;
    for (WebElement title : titles) {
      String valueAttribute = title.getAttribute("value");
      Integer value = Integer.parseInt(valueAttribute);
      if (Integer.compare(value, targetValue) == 0) {
        targetTitle = title;
      }
    }
    Assert.assertTrue(targetTitle != null);
    return targetTitle;
  }

  public WebElement getRandomOption(WebElement element) {
    List<WebElement> options = element.findElements(By.tagName("option"));
    int optionsLength = options.size();
    int randomOption = RandomValues.getRandomNumberBetween(1, optionsLength - 1);
    WebElement option = options.get(randomOption);
    return option;
  }

  public WebElement getISDCodeHeader() {
    return findElement(repositoryParser, PAGE_NAME, "ISDCode");
  }

  public WebElement getContactMobile() {
    return findElement(repositoryParser, PAGE_NAME, "contactMobile");
  }

  public WebElement getContactEmail() {
    return findElement(repositoryParser, PAGE_NAME, "contactEmail");
  }

  public WebElement getReadTermsLabel() {
    return findElement(repositoryParser, PAGE_NAME, "readTermsLabel");
  }

  public WebElement makePayment() {
    return findElement(repositoryParser, PAGE_NAME, "makePayment");
  }

  public WebElement getLayover() {
    return findElement(repositoryParser, PAGE_NAME, "layover");
  }

  public WebElement getOnwardFlightsCount() {
    return findElement(repositoryParser, PAGE_NAME, "onwardFlightsCount");
  }

  public WebElement getReturnFlightsCount() {
    return findElement(repositoryParser, PAGE_NAME, "returnFlightsCount");
  }

  public WebElement getTotalFare() {
    return findElement(repositoryParser, PAGE_NAME, "totalFareAmount");
  }

  public WebElement getNoInc() {
    WebElement noInc = findElement(repositoryParser, PAGE_NAME, "noInc");
    return noInc;
  }

  public WebElement getDobDiv(String passengerType) {
    return findElement(modifyPageElementOnce("dobDiv", passengerType));
  }

  public WebElement getVisaInfoRadio() {
    WebElement visaInfoRadio = findElement(repositoryParser, PAGE_NAME, "visaInfoRadio");
    return visaInfoRadio;
  }

  private PageElement getPageObject(String objectName, String replace, String replacement) {
    PageElement pageObject = repositoryParser.getPageObject(PAGE_NAME, objectName);
    String locatorValue = StringUtils.replace(pageObject.getLocatorValue(), replace, replacement);
    pageObject.setLocatorValue(locatorValue);
    return pageObject;
  }

  public WebElement getPassengerDiv(String traveller) {
    return findElement(getPageObject("passengerDiv", "passengerType", traveller));
  }

  public WebElement getMoreOptions(WebElement travellerDiv) {
    WebElement options = null;
    PageElement pageObject = repositoryParser.getPageObject(PAGE_NAME, "moreOptionsExpand");
    try {
      options = travellerDiv.findElement(By.xpath(pageObject.getLocatorValue()));
    } catch (Exception e) {
    }
    return options;
  }

  public WebElement getHiddenMoreCheckBox(WebElement travellerDiv) {
    WebElement options = null;
    PageElement pageObject = repositoryParser.getPageObject(PAGE_NAME, "moreOptionsHiddenCheckBox");
    try {
      options = travellerDiv.findElement(By.xpath(pageObject.getLocatorValue()));
    } catch (Exception e) {
    }
    return options;
  }

  public List<WebElement> getMealOptions(WebElement travellerDiv) {
    List<WebElement> optionsList = new ArrayList<WebElement>();
    try {
      optionsList = findElements(travellerDiv, repositoryParser, PAGE_NAME, "mealOption");
    } catch (Exception e) {
      CustomAssert.assertFail(testId, "Meal not available.");
    }
    return optionsList;
  }

  public List<WebElement> getBaggageOptions(WebElement travellerDiv) {
    List<WebElement> optionsList = new ArrayList<WebElement>();
    try {
      optionsList = findElements(travellerDiv, repositoryParser, PAGE_NAME, "baggageOption");
    } catch (Exception e) {
      CustomAssert.assertFail(testId, "Baggage not available.");
    }
    return optionsList;
  }

  public List<WebElement> getMealSectorList(WebElement travellerDiv) {
    List<WebElement> sectorList = new ArrayList<WebElement>();
    try {
      sectorList = findElements(travellerDiv, repositoryParser, PAGE_NAME, "mealSector");
    } catch (Exception e) {
      CustomAssert.assertFail(testId, "Meal Sector not available.");
    }
    return sectorList;
  }

  public List<WebElement> getBaggageSectorList(WebElement travellerDiv) {
   List<WebElement> sectorList = new ArrayList<WebElement>();
    try {
      sectorList = findElements(travellerDiv, repositoryParser, PAGE_NAME, "baggageSector");
    } catch (Exception e) {
      CustomAssert.assertFail(testId, "Baggage Sector not available.");
    }
    return sectorList;
  }

  public WebElement getSeatExpandBtn() {
    return findElement(repositoryParser, PAGE_NAME, "seatExpandBtn");
  }

  public WebElement getFlightSeatBtn(String flightNo) {
    return findElement(getPageObject("flightSeatBtn", "flightNo", flightNo));
  }

  public WebElement getFlightSeatDiv(String flightSector) {

    return findElement(getPageObject("flightSeatMap", "sector",
        StringUtils.replace(flightSector, "-", "_")));
  }

  public List<WebElement> getAvailableSeats(WebElement seatDiv) {
    return findElements(seatDiv, repositoryParser, PAGE_NAME, "availableSeats");
  }

  public List<WebElement> getSelectedSeats(WebElement seatDiv) {
    return findElements(seatDiv, repositoryParser, PAGE_NAME, "selectedSeat");
  }

  public List<WebElement> getSelectedSeatsFare(WebElement seatDiv) {
    return findElements(seatDiv, repositoryParser, PAGE_NAME, "selectedSeatFare");
  }

  public WebElement getProceedWithSeat(WebElement seatDiv) {
    return findElement(seatDiv, repositoryParser, PAGE_NAME, "proceedWithSeat");
  }
}
