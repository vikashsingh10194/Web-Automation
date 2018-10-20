package com.via.pageobjects.flights.common;

import lombok.AllArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.via.pageobjects.common.PageElement;
import com.via.utils.Log;
import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;

@AllArgsConstructor
public class FlightsSearch extends PageHandler {

  private RepositoryParser repositoryParser;

  private final String PAGE_NAME = "flightHome";

  public FlightsSearch(String testId, WebDriver driver, RepositoryParser repositoryParser) {
    super(testId, driver);
    this.repositoryParser = repositoryParser;
  }

  // getFlights Link Element for UAE
  public WebElement getFlightsLink() {
    WebElement element = findElement(repositoryParser, PAGE_NAME, "flights");
    return element;
  }

  public WebElement oneWay() {
    WebElement element = findElement(repositoryParser, PAGE_NAME, "oneWay");
    return element;
  }

  public WebElement roundTrip() {
    WebElement element = findElement(repositoryParser, PAGE_NAME, "roundTrip");
    return element;
  }

  public WebElement multiCity() {
    WebElement element = findElement(repositoryParser, PAGE_NAME, "multiCity");
    return element;
  }

  public WebElement source() {
    WebElement element = findElement(repositoryParser, PAGE_NAME, "source");
    return element;
  }

  public WebElement destination() {
    WebElement element = findElement(repositoryParser, PAGE_NAME, "destination");
    return element;
  }

  public WebElement getAirportElement(String dropDown, String cityName) {
    PageElement pageObject = repositoryParser.getPageObject(PAGE_NAME, "autoComplete");
    String locatorValue = StringUtils.replace(pageObject.getLocatorValue(), "cityName", cityName);
    locatorValue = StringUtils.replace(locatorValue, "dropDownType", dropDown);
    pageObject.setLocatorValue(locatorValue);

    WebElement autoCompleteElement = null;
    try {
      autoCompleteElement = findElement(pageObject);
    } catch (Exception e) {
    }
    return autoCompleteElement;
  }

  public WebElement getAirportCodeElement(WebElement element) {
    WebElement codeElement = null;
    try {
      codeElement = element.findElement(By.className("code"));
    } catch (Exception e) {
      Log.error(driver, testId, "Could not fing the airport code");
      return null;
    }
    return codeElement;
  }

  public WebElement getAdultCountMinus() {
    WebElement element = findElement(repositoryParser, PAGE_NAME, "adultCountMinus");
    return element;
  }

  public WebElement getAdultCount() {
    WebElement element = findElement(repositoryParser, PAGE_NAME, "adultCount");
    return element;
  }

  public WebElement getAdultCountPlus() {
    WebElement element = findElement(repositoryParser, PAGE_NAME, "adultCountPlus");
    return element;
  }

  public WebElement getChildrenCountMinus() {
    WebElement element = findElement(repositoryParser, PAGE_NAME, "childrenCountMinus");
    return element;
  }

  public WebElement getChildrenCount() {
    WebElement element = findElement(repositoryParser, PAGE_NAME, "childrenCount");
    return element;
  }

  public WebElement getChildrenCountPlus() {
    WebElement element = findElement(repositoryParser, PAGE_NAME, "childrenCountPlus");
    return element;
  }

  public WebElement getInfantCountMinus() {
    WebElement element = findElement(repositoryParser, PAGE_NAME, "infantCountMinus");
    return element;
  }

  public WebElement getInfantCount() {
    WebElement element = findElement(repositoryParser, PAGE_NAME, "infantCount");
    return element;
  }

  public WebElement getInfantCountPlus() {
    WebElement element = findElement(repositoryParser, PAGE_NAME, "infantCountPlus");
    return element;
  }

  public WebElement moreOptionSearchBox() {
    WebElement element = findElement(repositoryParser, PAGE_NAME, "moreOptionSearchBox");
    return element;
  }

  public WebElement searchFlight() {
    WebElement element = findElement(repositoryParser, PAGE_NAME, "searchFlight");
    return element;
  }
}
