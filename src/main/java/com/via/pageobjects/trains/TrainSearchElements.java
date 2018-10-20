package com.via.pageobjects.trains;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.via.pageobjects.common.PageElement;
import com.via.utils.Log;
import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;

public class TrainSearchElements extends PageHandler {
  private RepositoryParser repositoryParser;
  private final String PAGE_NAME = "trainHome";

  public TrainSearchElements(String testId, WebDriver driver, RepositoryParser repositoryParser) {
    super(testId, driver);
    this.repositoryParser = repositoryParser;
  }

  public WebElement getTrainSearchPage() {
    return findElement(repositoryParser, PAGE_NAME, "train");
  }

  public WebElement getTrainTypeRadio(String type) {
    return findElement(repositoryParser, PAGE_NAME, type);
  }

  public WebElement getSourceCity() {
    return findElement(repositoryParser, PAGE_NAME, "source");
  }

  public WebElement getDestinationCity() {
    return findElement(repositoryParser, PAGE_NAME, "destination");
  }

  public WebElement getOneWayTab() {
    return findElement(repositoryParser, PAGE_NAME, "oneWay");
  }

  public WebElement getRoundTripTab() {
    return findElement(repositoryParser, PAGE_NAME, "roundTrip");
  }

  public WebElement getStationElement(String dropDown, String cityName) {
    PageElement pageObject = repositoryParser.getPageObject(PAGE_NAME, "autoComplete");
    String locatorValue =
        StringUtils.replace(pageObject.getLocatorValue(), "cityName",
            StringUtils.upperCase(cityName));
    locatorValue = StringUtils.replace(locatorValue, "dropDownType", dropDown);
    pageObject.setLocatorValue(locatorValue);

    WebElement autoCompleteElement = null;
    try {
      autoCompleteElement = findElement(pageObject);
    } catch (Exception e) {
    }
    return autoCompleteElement;
  }

  public WebElement getStationCodeElement(WebElement element) {
    WebElement codeElement = null;
    try {
      codeElement = element.findElement(By.className("code"));
    } catch (Exception e) {
      Log.error(driver, testId, "Could not fing the airport code");
      return null;
    }
    return codeElement;
  }

  public WebElement searchButton() {
    return findElement(repositoryParser, PAGE_NAME, "searchTrain");
  }

  public WebElement getReturnCalendar() {
    return findElement(repositoryParser, PAGE_NAME, "returnDate");
  }

  public WebElement getInfantCountMinus() {
    return findElement(repositoryParser, PAGE_NAME, "infantCountMinus");
  }

  public WebElement getInfantCountPlus() {
    return findElement(repositoryParser, PAGE_NAME, "infantCountPlus");
  }

  public WebElement getInfantCount() {
    return findElement(repositoryParser, PAGE_NAME, "infantCount");
  }

  public WebElement getAdultCountMinus() {
    return findElement(repositoryParser, PAGE_NAME, "adultCountMinus");
  }

  public WebElement getAdultCountPlus() {
    return findElement(repositoryParser, PAGE_NAME, "adultCountPlus");
  }

  public WebElement getAdultCount() {
    return findElement(repositoryParser, PAGE_NAME, "adultCount");
  }
}
