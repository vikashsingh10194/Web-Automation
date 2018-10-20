package com.via.pageobjects.holidays;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.via.pageobjects.common.PageElement;
import com.via.utils.PageHandler;
import com.via.utils.RandomValues;
import com.via.utils.RepositoryParser;

public class GuestsInput extends PageHandler {

  private final String PAGE_NAME = "guestsInput";
  private RepositoryParser repositoryParser;

  public GuestsInput(WebDriver driver, RepositoryParser repositoryParser, String testId) {
    this.driver = driver;
    this.repositoryParser = repositoryParser;
    this.testId = testId;
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

  public WebElement getElementByPageObject(PageElement pageElement) {
    WebElement element = findElement(pageElement);
    Assert.assertTrue(element != null);
    return element;
  }

  public WebElement getRandomOption(WebElement element) {
    List<WebElement> options = element.findElements(By.tagName("option"));
    int optionsLength = options.size();
    int randomOption = RandomValues.getRandomNumberBetween(1, optionsLength - 1);
    WebElement option = options.get(randomOption);
    return option;
  }

  // Top Box Validation Elements
  public WebElement topPackageName() {
    return findElement(repositoryParser, PAGE_NAME, "topHotelName");
  }

  public WebElement topHotelAddress() {
    return findElement(repositoryParser, PAGE_NAME, "topHotelAddress");
  }

  public WebElement topCheckinCheckout() {
    return findElement(repositoryParser, PAGE_NAME, "topCheckinCheckout");
  }

  public WebElement topRooms() {
    return findElement(repositoryParser, PAGE_NAME, "topRooms");
  }

  public WebElement topAdults() {
    return findElement(repositoryParser, PAGE_NAME, "topAdults");
  }

  public WebElement topChildren() {
    return findElement(repositoryParser, PAGE_NAME, "topChildren");
  }

  public WebElement topTotalPrice() {
    return findElement(repositoryParser, PAGE_NAME, "topTotalPrice");
  }

  // Left Box Validation Elements
  public WebElement leftHotelName() {
    return findElement(repositoryParser, PAGE_NAME, "leftHotelName");
  }

  public WebElement leftHotelAddr() {
    return findElement(repositoryParser, PAGE_NAME, "leftHotelAddr");
  }

  public WebElement leftCityCountry() {
    return findElement(repositoryParser, PAGE_NAME, "leftCityCountry");
  }

  public WebElement leftPackageName() {
    return findElement(repositoryParser, PAGE_NAME, "leftPackageName");
  }

  public WebElement leftInclusions() {
    return findElement(repositoryParser, PAGE_NAME, "LeftInclusions");
  }

  public WebElement leftCheckinCheckout() {
    return findElement(repositoryParser, PAGE_NAME, "leftCheckinCheckout");
  }

  public WebElement leftpackageNights() {
    return findElement(repositoryParser, PAGE_NAME, "rooms");
  }

  public WebElement leftAdults() {
    return findElement(repositoryParser, PAGE_NAME, "adults");
  }

  public WebElement leftChildren() {
    return findElement(repositoryParser, PAGE_NAME, "children");
  }

  // Guests Details

  public WebElement getISDCodeHeader() {
    return findElement(repositoryParser, PAGE_NAME, "ISDcode");
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

  public WebElement getTotalFare() {
    return findElement(repositoryParser, PAGE_NAME, "totalFareAmount");
  }

  public WebElement confirmationButton() {
    return findElement(repositoryParser, PAGE_NAME, "confirmationButton");
  }
}
