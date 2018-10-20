package com.via.pageobjects.holidays;

import java.util.concurrent.TimeUnit;

import lombok.AllArgsConstructor;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.via.pageobjects.common.PageElement;
import com.via.utils.Constant;
import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;

@AllArgsConstructor
public class HolidaysSearch extends PageHandler {

  private RepositoryParser repositoryParser;

  private final String PAGE_NAME = "holidaysHome";


  public HolidaysSearch(String testId, WebDriver driver, RepositoryParser repositoryParser) {
    super(testId, driver);
    this.repositoryParser = repositoryParser;
  }

  public WebElement holidaysTab() {
    WebElement element = findElement(repositoryParser, PAGE_NAME, "holidaysTab");
    return element;
  }

  public WebElement destination() {
    WebElement element = findElement(repositoryParser, PAGE_NAME, "destination");
    return element;
  }

  public WebElement destinationAutoComplete(String targetDestination) {
    PageElement pageObject = repositoryParser.getPageObject(PAGE_NAME, "destinationCity");
    String locatorValue =
        StringUtils.replace(pageObject.getLocatorValue(), "destination", targetDestination);
    pageObject.setLocatorValue(locatorValue);

    return findElement(pageObject);
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

  public WebElement calendarTextBox() {
    WebElement element = findElement(repositoryParser, PAGE_NAME, "calender");
    return element;
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

  public WebElement searchHoliday() {
    WebElement element = findElement(repositoryParser, PAGE_NAME, "searchHoliday");
    return element;
  }

  public WebElement roomDropdown() {
    return findElement(repositoryParser, PAGE_NAME, "roomDropdown");
  }

  public WebElement addRoom() {
    return findElement(repositoryParser, PAGE_NAME, "addRoom");
  }

  public WebElement doneRoomDetails() {
    return findElement(repositoryParser, PAGE_NAME, "done");
  }

  public WebElement roomSelector() {
    return findElement(repositoryParser, PAGE_NAME, "roomSelector");
  }

  public WebElement getBookingTab(String bookingTab) {
    driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
    WebElement element = driver.findElement(By.className(bookingTab));
    driver.manage().timeouts().implicitlyWait(Constant.IMPLICIT_WAIT_TIME, TimeUnit.SECONDS);
    return element;
  }

}
