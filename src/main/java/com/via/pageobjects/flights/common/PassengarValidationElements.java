package com.via.pageobjects.flights.common;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.via.pageobjects.common.PageElement;
import com.via.utils.Constant;
import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;

public class PassengarValidationElements extends PageHandler {

  private final String PAGE_NAME = "travellersDetailsValidation";
  private RepositoryParser repositoryParser;

  public PassengarValidationElements(String testId, WebDriver driver,
      RepositoryParser repositoryParser) {
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

  public WebElement getElementByPageObject(String testId, PageElement pageElement) {
    WebElement element = findElement(pageElement);
    Assert.assertTrue(element != null);
    return element;
  }

  public WebElement getPassengerEmail() {
    return findElement(repositoryParser, PAGE_NAME, "passengerEmail");
  }

  public WebElement getPassengerContact() {
    return findElement(repositoryParser, PAGE_NAME, "passengerMobile");
  }

  public List<WebElement> getPassengers() {
    return findElements(repositoryParser, PAGE_NAME, "passengers");
  }

  public WebElement confirmPaymentButton() {
    return findElement(repositoryParser, PAGE_NAME, "confirmPaymentButton");
  }

  public List<WebElement> getTravellersName() {
    return findElements(repositoryParser, PAGE_NAME, "passengerName");
  }

  public List<WebElement> getTravellersType() {
    return findElements(repositoryParser, PAGE_NAME, "passengerType");
  }

  public List<WebElement> getTravellersDOB() {
    List<WebElement> dobList = findElements(repositoryParser, PAGE_NAME, "passengerDOB");
    return dobList;
  }

  public List<WebElement> getTravellersNationality() {
    List<WebElement> nationList = findElements(repositoryParser, PAGE_NAME, "nationality");
    return nationList;
  }

  public List<WebElement> getPassportNos() {
    List<WebElement> passportList = findElements(repositoryParser, PAGE_NAME, "passportNo");
    return passportList;
  }

  public List<WebElement> getExpiryDateList() {
    List<WebElement> expDateList = findElements(repositoryParser, PAGE_NAME, "expiryDate");
    return expDateList;
  }

  public List<WebElement> getAddOnsRows() {
   List<WebElement> addOnsRows = new ArrayList<WebElement>();
    try {
      addOnsRows = findElements(repositoryParser, PAGE_NAME, "addonsRows");
    } catch (Exception e) {
    }
    return addOnsRows;
  }

  public WebElement getFlightNo(WebElement rowElement) {
    return findElement(rowElement, repositoryParser, PAGE_NAME, "flightNo");
  }

  public WebElement getName(WebElement rowElement) {
    return findElement(rowElement, repositoryParser, PAGE_NAME, "name");
  }

  public WebElement getSeat(WebElement rowElement) {
    return findElement(rowElement, repositoryParser, PAGE_NAME, "seatNo");
  }

  public WebElement getMeal(WebElement rowElement) {
    return findElement(rowElement, repositoryParser, PAGE_NAME, "meal");
  }

  public WebElement getBaggage(WebElement rowElement) {
    return findElement(rowElement, repositoryParser, PAGE_NAME, "baggage");
  }
}
