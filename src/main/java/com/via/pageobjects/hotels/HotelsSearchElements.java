package com.via.pageobjects.hotels;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.via.pageobjects.common.PageElement;
import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;

public class HotelsSearchElements extends PageHandler {

  private final String PAGE_NAME = "hotelsSearchPage";
  private RepositoryParser repositoryParser;

  public HotelsSearchElements(WebDriver driver, RepositoryParser repositoryParser, String testId) {
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

  public WebElement getElementByPageObject(PageElement pageElement) {
    WebElement element = findElement(pageElement);
    Assert.assertTrue(element != null);
    return element;
  }

  public WebElement getHotelsSeachBox() {
    return findElement(repositoryParser, PAGE_NAME, "hotels");
  }

  public WebElement destination() {
    return findElement(repositoryParser, PAGE_NAME, "destinationInput");
  }

  public WebElement destinationAutoComplete(String autoSuggestion, String targetDestination) {

    PageElement pageObject = repositoryParser.getPageObject(PAGE_NAME, "destinationAutoComplete");

    String locatorValue =
        StringUtils.replace(pageObject.getLocatorValue(), "replaceText", targetDestination);
    locatorValue = StringUtils.replace(locatorValue, "autoSuggestion", autoSuggestion);
    pageObject.setLocatorValue(locatorValue);
    WebElement autoSuggestionElement = null;
    try {
      autoSuggestionElement = findElement(pageObject);
    } catch (Exception e) {
    }
    return autoSuggestionElement;
  }

  public WebElement checkIn() {
    return findElement(repositoryParser, PAGE_NAME, "checkInInput");
  }

  public WebElement checkOut() {
    return findElement(repositoryParser, PAGE_NAME, "checkOutInput");
  }

  public WebElement guestInput() {
    return findElement(repositoryParser, PAGE_NAME, "guestInput");
  }

  public WebElement addRoom() {
    return findElement(repositoryParser, PAGE_NAME, "addRoom");
  }

  public WebElement doneRoomDetails() {
    return findElement(repositoryParser, PAGE_NAME, "doneRoomDetails");
  }

  public WebElement searchHotels() {
    return findElement(repositoryParser, PAGE_NAME, "searchHotels");
  }

}
