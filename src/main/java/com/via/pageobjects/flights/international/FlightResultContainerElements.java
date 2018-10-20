package com.via.pageobjects.flights.international;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.via.pageobjects.common.PageElement;
import com.via.utils.Constant;
import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;

public class FlightResultContainerElements extends PageHandler {
  private RepositoryParser repositoryParser;

  public FlightResultContainerElements(WebDriver driver, RepositoryParser repositoryParser,
      String testId) {
    super(testId, driver);
    this.repositoryParser = repositoryParser;
  }

  private final String PAGE_NAME = "flightResultContainer";

  public WebElement getOnwardDivElement(String flightName, String flightNo) {
    PageElement pageObject = repositoryParser.getPageObject(PAGE_NAME, "flightOnwardDiv");
    String locatorValue =
        StringUtils.replace(pageObject.getLocatorValue(), "flightName", flightName);
    locatorValue = StringUtils.replace(locatorValue, "flightNo", flightNo);
    pageObject.setLocatorValue(locatorValue);
    return findElement(pageObject);
  }

  public WebElement getReturnDivElement(String onwardFlightName, String returnFlightName,
      String divNo) {
    PageElement pageObject = repositoryParser.getPageObject(PAGE_NAME, "flightReturnDiv");
    String locatorValue =
        StringUtils.replace(pageObject.getLocatorValue(), "onwardFlight", onwardFlightName);
    locatorValue = StringUtils.replace(locatorValue, "returnFlight", returnFlightName);
    locatorValue = StringUtils.replace(locatorValue, "divNo", divNo);
    pageObject.setLocatorValue(locatorValue);
    return findElement(pageObject);
  }

  public WebElement getFlightDetailsContainer(WebElement divElement) {
    return findElement(divElement, repositoryParser, PAGE_NAME, "flightDetailsContainer");
  }

  public WebElement getTotalFlightCount() {
    return findElement(repositoryParser, PAGE_NAME, "flightCount");
  }

  public WebElement getFlightDiv(int count) {
    PageElement pageObject = repositoryParser.getPageObject(PAGE_NAME, "flightDiv");
    String locatorValue =
        StringUtils.replace(pageObject.getLocatorValue(), "count", Integer.toString(count));
    pageObject.setLocatorValue(locatorValue);
    return findElement(pageObject);
  }

  public WebElement getFlightDetailsTab(WebElement divElement) {
    return findElement(divElement, repositoryParser, PAGE_NAME, "flightDetailsTab");
  }

  public List<WebElement> getStopsDetails(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "flightStops");
  }

  public List<WebElement> getDepartAirportNameList(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "flightDepartAirportName");
  }

  public List<WebElement> getDepartAirportCodeList(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "flightDepartAirportCode");
  }

  public List<WebElement> getArrivalAirportCodeList(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "flightArrivalAirportCode");
  }

  public List<WebElement> getArrivalAirportNameList(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "flightArrivalAirportName");
  }

  public List<WebElement> getDepartCountryList(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "flightDepartCountry");
  }

  public List<WebElement> getArrivalCountryList(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "flightArrivalCountry");
  }

  public List<WebElement> getFlightNameList(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "flightName");
  }

  public List<WebElement> getFlightNoList(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "flightNo");
  }

  public List<WebElement> getFlightDepartDate(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "flightDepartDate");
  }

  public List<WebElement> getFlightDepartTime(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "flightDepartTime");
  }

  public List<WebElement> getFlightDepartTerminal(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "flightDepartTerminal");
  }

  public List<WebElement> getFlightArrivalDate(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "flightArrivalDate");
  }

  public List<WebElement> getFlightArrivalTime(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "flightArrivalTime");
  }

  public List<WebElement> getFlightArrivalTerminal(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "flightArrivalTerminal");
  }

  public List<WebElement> getCheckInBaggageList(WebElement divElement) {
    List<WebElement> baggage =
        findElements(divElement, repositoryParser, PAGE_NAME, "checkInBaggage");
    return baggage;
  }

  public List<WebElement> getCabinBaggageList(WebElement divElement) {
    List<WebElement> baggage =
        findElements(divElement, repositoryParser, PAGE_NAME, "cabinBaggage");
    return baggage;
  }

  public List<WebElement> getFlightNameListFromBaggage(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "baggageFlightName");
  }

  public List<WebElement> getFlightNoListFromBaggage(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "baggageFlightNo");
  }

  public WebElement getBaggageInfoTab(WebElement divElement) {
    return findElement(divElement, repositoryParser, PAGE_NAME, "baggageDetailsTab");
  }

  public WebElement getAdultTotalFare(WebElement divElement) {
    return findElement(divElement, repositoryParser, PAGE_NAME, "adultTotalFare");
  }

  public WebElement getChildTotalFare(WebElement divElement) {
    return findElement(divElement, repositoryParser, PAGE_NAME, "childTotalFare");
  }

  public WebElement getInfantTotalFare(WebElement divElement) {
    return findElement(divElement, repositoryParser, PAGE_NAME, "infantTotalFare");
  }

  public List<WebElement> getOtherFares(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "otherTotalFare");
  }

  public List<WebElement> getOtherFareType(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "otherFareType");
  }

  public WebElement getTotalFare(WebElement divElement) {
    return findElement(divElement, repositoryParser, PAGE_NAME, "totalFare");
  }

  public WebElement getTotalPrice(WebElement divElement) {
    return findElement(divElement, repositoryParser, PAGE_NAME, "totalPrice");
  }

  public WebElement getBookButton(WebElement divElement) {
    return findElement(divElement, repositoryParser, PAGE_NAME, "bookButton");
  }

  public WebElement getFareDetailsTab(WebElement divElement) {
    return findElement(divElement, repositoryParser, PAGE_NAME, "fareDetailsTab");
  }

  public int getTotalOnwardFlight(WebElement divElement) {
    List<WebElement> onwardSegment =
        findElements(divElement, repositoryParser, PAGE_NAME, "onwardFlightCount");
    return onwardSegment.size();
  }

  public WebElement getCompanyText() {
    return findElement(repositoryParser, "home", "company");
  }

  public WebElement getResultDiv(int index) {
    PageElement pageObject = repositoryParser.getPageObject(PAGE_NAME, "resultDiv");
    String locatorValue =
        StringUtils.replace(pageObject.getLocatorValue(), "count", Integer.toString(index));
    pageObject.setLocatorValue(locatorValue);
    // return findElement(pageObject);
    WebElement element = findElement(pageObject);
    PageHandler.jSExecuterScrolldown(driver, element);
    return element;
  }

}
