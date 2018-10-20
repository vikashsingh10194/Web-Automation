package com.via.pageobjects.flights.common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;

public class HeaderElements extends PageHandler {
  private RepositoryParser repositoryParser;

  public HeaderElements(String testId, WebDriver driver, RepositoryParser repositoryParser) {
    super(testId, driver);
    this.repositoryParser = repositoryParser;
  }

  public WebElement getLoadPercent(String pageName) {
    return findElement(repositoryParser, pageName, "loadPercent");
  }

  public WebElement getSourceDetails(String pageName) {
    return findElement(repositoryParser, pageName, "sourceCity");
  }

  public WebElement getDestinationDetails(String pageName) {
    return findElement(repositoryParser, pageName, "destinationCity");
  }

  public WebElement getOnwardDate(String pageName) {
    return findElement(repositoryParser, pageName, "onwardDate");
  }

  public WebElement getReturnDate(String pageName) {
    return findElement(repositoryParser, pageName, "returnDate");
  }

  public WebElement getAdultsCount(String pageName) {
    return findElement(repositoryParser, pageName, "adultCount");
  }

  public WebElement getChildrenCount(String pageName) {
    return findElement(repositoryParser, pageName, "childrenCount");
  }

  public WebElement getInfantsCount(String pageName) {
    return findElement(repositoryParser, pageName, "infantCount");
  }

  public WebElement getOnwardSrcCityCode(String pageName) {
    return findElement(repositoryParser, pageName, "onwardSourceCityCode");
  }

  public WebElement getOnwardDestCityCode(String pageName) {
    return findElement(repositoryParser, pageName, "onwardDestinationCode");
  }

  public WebElement getOnwardReturnDate(String pageName) {
    return findElement(repositoryParser, pageName, "onwardReturnDate");
  }

  public WebElement getTotalFare(String pageName) {
    return findElement(repositoryParser, pageName, "totalFareAmount");
  }

  public WebElement expandFareDetails(String pageName) {
    return findElement(repositoryParser, pageName, "expandFareDetails");
  }

}
