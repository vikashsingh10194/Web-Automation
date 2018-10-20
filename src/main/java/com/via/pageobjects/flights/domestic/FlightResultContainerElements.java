package com.via.pageobjects.flights.domestic;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.via.pageobjects.common.PageElement;
import com.via.utils.Constant;
import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;

public class FlightResultContainerElements extends PageHandler {
  private final String PAGE_NAME = "flightResultContainer";
  private RepositoryParser repositoryParser;

  public FlightResultContainerElements(String testId, WebDriver driver,
      RepositoryParser repositoryParser) {
    super(testId, driver);
    this.repositoryParser = repositoryParser;
  }

  public WebElement getAirline(String journeyType, String flightName) {
    PageElement pageElement = repositoryParser.getPageObject(PAGE_NAME, "airlines");
    String locatorValue = pageElement.getLocatorValue();
    locatorValue = StringUtils.replace(locatorValue, "flightResults", journeyType);
    locatorValue = StringUtils.replace(locatorValue, "flightName", flightName);
    pageElement.setLocatorValue(locatorValue);
    WebElement element = findElement(pageElement);
    return element;
  }

  private PageElement getPageElementSpecificToFlightNameAndFlightNo(PageElement pageElement,
      String journeyType, String flightName, String flightNo) {
    String locatorValue = pageElement.getLocatorValue();
    locatorValue = StringUtils.replace(locatorValue, "flightResults", journeyType);
    locatorValue = StringUtils.replace(locatorValue, "flightName", flightName);
    locatorValue = StringUtils.replace(locatorValue, "flightNo", flightNo);
    pageElement.setLocatorValue(locatorValue);
    return pageElement;
  }

  private PageElement getSpecificPageElement(String elementName, String journeyType,
      String flightName, String flightNo) {
    PageElement pageObject = repositoryParser.getPageObject(PAGE_NAME, elementName);
    pageObject =
        getPageElementSpecificToFlightNameAndFlightNo(pageObject, journeyType, flightName, flightNo);
    return pageObject;
  }

  public WebElement getFlightCompleteDiv(String journeyType, String flightName, String flightNo) {
    try {
      PageElement pageObject =
          getSpecificPageElement("resultDiv", journeyType, flightName, flightNo);
      // return findElement(pageObject);
      WebDriverWait wait = new WebDriverWait(driver, 25);
      return wait.until(ExpectedConditions.visibilityOf(findElement(pageObject)));
    } catch (Exception e) {
      return null;
    }
  }

  public WebElement getFlightNo(WebElement divElement) {
    return findElement(divElement, repositoryParser, PAGE_NAME, "flightNo");
  }

  public WebElement getFlightName(WebElement divElement) {
    return findElement(divElement, repositoryParser, PAGE_NAME, "flightName");
  }

  public WebElement getDepartureTime(WebElement divElement) {
    return findElement(divElement, repositoryParser, PAGE_NAME, "departTime");
  }

  public WebElement getArrivalTime(WebElement divElement) {
    return findElement(divElement, repositoryParser, PAGE_NAME, "arrivalTime");
  }

  public WebElement getDuration(WebElement divElement) {
    return findElement(divElement, repositoryParser, PAGE_NAME, "duration");
  }

  public WebElement getFare(WebElement divElement) {
    List<WebElement> fares = findElements(divElement, repositoryParser, PAGE_NAME, "fare");
    if (fares.size() == 1) {
      return fares.get(0);
    }
    return fares.get(1);
  }

  public WebElement getDepartCity(WebElement divElement) {
    return findElement(divElement, repositoryParser, PAGE_NAME, "departCity");
  }

  public WebElement getArrivalCity(WebElement divElement) {
    return findElement(divElement, repositoryParser, PAGE_NAME, "arrivalCity");
  }

  public WebElement getFlightStops(String journeyType, String flightName, String flightNo) {
    PageElement pageObject = getSpecificPageElement("stops", journeyType, flightName, flightNo);
    return findElement(pageObject);
  }

  public List<WebElement> getTotalFlightCount() {

    List<WebElement> elements = findElements(repositoryParser, PAGE_NAME, "totalFlightCount");
    return elements;
  }

  public WebElement getEndPageMarker() {
    PageElement pageObject = repositoryParser.getPageObject(PAGE_NAME, "endPageMarker");
    return findElement(pageObject);
  }

  public WebElement getBaggageInformationButton(WebElement divElement) {
    WebDriverWait wait = new WebDriverWait(driver, 20);
    return wait.until(ExpectedConditions.elementToBeClickable(findElement(divElement,
        repositoryParser, PAGE_NAME, "baggage")));
  }

  public WebElement getFlightDetailsButton(WebElement divElement) {
    WebDriverWait wait = new WebDriverWait(driver, 20);
    return wait.until(ExpectedConditions.elementToBeClickable(findElement(divElement,
        repositoryParser, PAGE_NAME, "flightDetails")));
  }

  public WebElement getFareRulesButton(WebElement divElement) {
    WebDriverWait wait = new WebDriverWait(driver, 20);
    return wait.until(ExpectedConditions.elementToBeClickable(findElement(divElement,
        repositoryParser, PAGE_NAME, "fareRules")));
  }

  public WebElement getFareDetailsButton(WebElement divElement) {
    WebDriverWait wait = new WebDriverWait(driver, 20);
    return wait.until(ExpectedConditions.elementToBeClickable(findElement(divElement,
        repositoryParser, PAGE_NAME, "fareDetails")));
  }

  public WebElement getFlightFromToDetailsFromBaggage(WebElement divElement) {
    return findElement(divElement, repositoryParser, PAGE_NAME, "baggageFromToDeatils");
  }

  public List<WebElement> getFlightNoFromBaggage(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "baggageFlightNo");
  }

  public List<WebElement> getFlightNameFromBaggage(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "baggageAirLineName");
  }

  public List<WebElement> getAdultCheckInBaggage(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "adultCheckInBaggage");
  }

  public List<WebElement> getAdultCabinBaggage(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "adultCabinBaggage");
  }

  public List<WebElement> getCheckInBaggage(WebElement divElement) {
    driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
    List<WebElement> baggage =
        findElements(divElement, repositoryParser, PAGE_NAME, "checkInBaggage");
    driver.manage().timeouts().implicitlyWait(Constant.IMPLICIT_WAIT_TIME, TimeUnit.SECONDS);
    return baggage;
  }

  public List<WebElement> getCabinBaggage(WebElement divElement) {
    List<WebElement> baggage =
        findElements(divElement, repositoryParser, PAGE_NAME, "cabinBaggage");
    return baggage;
  }

  public List<WebElement> getChildCheckInBaggage(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "childCheckInBaggage");
  }

  public List<WebElement> getChildCabinBaggage(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "childCabinBaggage");
  }

  public List<WebElement> getFlightFromToDetails(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "flightFromToDetails");
  }

  public WebElement getFlightDepartureDateDetails(WebElement divElement) {
    return findElement(divElement, repositoryParser, PAGE_NAME, "flightDepartDate");
  }

  public WebElement getFlightStopsFromFlightDetails(WebElement divElement) {
    return findElement(divElement, repositoryParser, PAGE_NAME, "flightStops");
  }

  public List<WebElement> getDepartAirPortCodeFromFlightDetails(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "flightDepartAirPortCode");
  }

  public List<WebElement> getDepartAirportNameFromFlightDetails(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "flightDepartAirPortName");
  }

  public List<WebElement> getDepartAirportCountryFromFlightDetails(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "flightDepartCountry");
  }

  public List<WebElement> getDepartTimeFromFlightDetails(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "flightDepartTime");
  }

  public List<WebElement> getDepartDateFromFlightDetails(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "flightDepartDate");
  }

  public List<WebElement> getArrivalDateFromFlightDetails(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "flightArrivalDate");
  }

  public List<WebElement> getArrivalAirportCodeFromFlightDetails(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "flightArrivalAirPortCode");
  }

  public List<WebElement> getArrivalAirportNameFromFlightDetails(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "flightArrivalAirPortName");
  }

  public List<WebElement> getArrivalAirportCountryFromFlightDetails(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "flightArrivalCountry");
  }

  public List<WebElement> getArrivalTimeFromFlightDetails(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "flightArrivalTime");
  }

  public List<WebElement> getFlightNameFromFlightDetails(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "flightNameFromFlightDetails");
  }

  public List<WebElement> getFlightNoFromFlightDetails(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "flightNoFromFlightDetails");
  }

  public List<WebElement> getFlightDurationFromFlightDetails(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "flightDurationFromFlightDetails");
  }

  public List<WebElement> getFlightLayoverFromFlightDetails(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "flightLayOverFromFlightDetails");
  }

  public List<WebElement> getFareRulessTab(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "fareTab");
  }

  public WebElement getFareRefundable(WebElement divElement) {
    return findElement(divElement, repositoryParser, PAGE_NAME, "flightFareRefundable");
  }

  public WebElement getAdultFare(WebElement divElement) {
    return findElement(divElement, repositoryParser, PAGE_NAME, "adultFare");
  }

  public WebElement getAdultTotalFare(WebElement divElement) {
    return findElement(divElement, repositoryParser, PAGE_NAME, "adultTotalFare");
  }

  public WebElement getChildFare(WebElement divElement) {
    return findElement(divElement, repositoryParser, PAGE_NAME, "childFare");
  }

  public WebElement getChildTotalFare(WebElement divElement) {
    return findElement(divElement, repositoryParser, PAGE_NAME, "childTotalFare");
  }

  public WebElement getInfantFare(WebElement divElement) {
    return findElement(divElement, repositoryParser, PAGE_NAME, "infantFare");
  }

  public WebElement getInfantTotalFare(WebElement divElement) {
    return findElement(divElement, repositoryParser, PAGE_NAME, "infantTotalFare");
  }

  public List<WebElement> getOtherFare(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "otherFare");
  }

  public List<WebElement> getOtherFareType(WebElement divElement) {
    return findElements(divElement, repositoryParser, PAGE_NAME, "otherFareType");
  }

  public WebElement getTotalOtherFare(WebElement divElement) {
    return findElement(divElement, repositoryParser, PAGE_NAME, "totalOtherFare");
  }

  public WebElement getTotalFare(WebElement divElement) {
    return findElement(divElement, repositoryParser, PAGE_NAME, "totalFare");
  }

  public WebElement getSelectButton(WebElement divElement) {
    WebDriverWait wait = new WebDriverWait(driver, 20);
    return wait.until(ExpectedConditions.elementToBeClickable(findElement(divElement,
        repositoryParser, PAGE_NAME, "select")));
  }

  public WebElement getFlightDivAtBookPanel(String flightType) {
    PageElement pageObject = getPageElementSpecificToFlightType("selectedFlightDiv", flightType);
    return findElement(pageObject);
  }

  public WebElement getSelectedFlightName(WebElement flightType) {
    return findElement(flightType, repositoryParser, PAGE_NAME, "selectedFlightName");
  }

  public WebElement getSelectedFlightNo(WebElement flightType) {
    return findElement(flightType, repositoryParser, PAGE_NAME, "selectedFlightCode");
  }

  public WebElement getSelectedFlightDepartTime(WebElement flightType) {
    return findElement(flightType, repositoryParser, PAGE_NAME, "selectedFlightDepartTime");
  }

  public WebElement getSelectedFlightDepartCity(WebElement flightType) {
    return findElement(flightType, repositoryParser, PAGE_NAME, "selectedFlightDepartCity");
  }

  public WebElement getSelectedFlightArrivalCity(WebElement flightType) {
    return findElement(flightType, repositoryParser, PAGE_NAME, "selectedFlightArrivalCity");
  }

  public List<WebElement> getSelectedFlightsGrandTotal(String testId) {
    return findElements(repositoryParser, PAGE_NAME, "grandTotalFare");
  }

  public WebElement getSelectedFlightFare(WebElement flightType) {
    return findElement(flightType, repositoryParser, PAGE_NAME, "selectedFlightFare");
  }

  public WebElement getSelectedFlightArrivalTime(WebElement flightType) {
    return findElement(flightType, repositoryParser, PAGE_NAME, "selectedFlightArrivalTime");
  }

  public WebElement getBookButton(String testId) {
    WebDriverWait wait = new WebDriverWait(driver, 20);
    return wait.until(ExpectedConditions.elementToBeClickable(findElement(repositoryParser,
        PAGE_NAME, "bookFlights")));
  }

  private PageElement getPageElementSpecificToFlightType(String objectName, String flightType) {
    PageElement pageObject = repositoryParser.getPageObject(PAGE_NAME, objectName);
    pageObject.setLocatorValue(StringUtils.replace(pageObject.getLocatorValue(), "flightType",
        flightType));
    return pageObject;
  }

  public WebElement getFlightResultDiv(int count) {
    PageElement pageObject = repositoryParser.getPageObject(PAGE_NAME, "flightResultsDiv");
    String locatorValue = pageObject.getLocatorValue();
    locatorValue = StringUtils.replace(locatorValue, "count", Integer.toString(count));
    pageObject.setLocatorValue(locatorValue);
    // WebDriverWait wait = new WebDriverWait(driver, 20);
    // return wait.until(ExpectedConditions.visibilityOf(findElement(pageObject)));
    WebElement element = findElement(pageObject);
    PageHandler.jSExecuterScrolldown(driver, element);
    return element;
  }

  public WebElement getCompanyText() {
    return findElement(repositoryParser, "home", "company");
  }

}
