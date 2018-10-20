package com.via.pageobjects.holidays;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.via.pageobjects.common.PageElement;
import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;

public class HolidaySearchResultElements extends PageHandler {
  private final String PAGE_NAME = "holidaySearchResults";
  private RepositoryParser repositoryParser;

  public HolidaySearchResultElements(String testId, WebDriver driver,
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

  public WebElement getElementByPageObject(PageElement pageElement) {
    WebElement element = findElement(pageElement);
    Assert.assertTrue(element != null);
    return element;
  }

  public WebElement destinationCity() {
    return findElement(repositoryParser, PAGE_NAME, "destinationCity");
  }

  public WebElement destinationCountry() {
    return findElement(repositoryParser, PAGE_NAME, "destinationCountry");
  }

  public WebElement checkInOutDate() {
    return findElement(repositoryParser, PAGE_NAME, "checkInOutDate");
  }

  public WebElement roomCount() {
    return findElement(repositoryParser, PAGE_NAME, "roomCount");
  }

  public WebElement adultCount() {
    return findElement(repositoryParser, PAGE_NAME, "adultCount");
  }

  public WebElement childCount() {
    return findElement(repositoryParser, PAGE_NAME, "childCount");
  }

  public WebElement bookPackage() {
    return findElement(repositoryParser, PAGE_NAME, "bookPackage");
  }

  public WebElement calendarMonth() {
    return findElement(repositoryParser, PAGE_NAME, "calendarMonth");
  }

  public WebElement calendarYear() {
    return findElement(repositoryParser, PAGE_NAME, "calendarYear");
  }

  public WebElement calendarDate() {
    return findElement(repositoryParser, PAGE_NAME, "calendarDate");
  }

  public WebElement calendarNavigation() {
    return findElement(repositoryParser, PAGE_NAME, "calendarNavigation");
  }

  public WebElement packageCalenderBook() {
    return findElement(repositoryParser, PAGE_NAME, "packageCalenderBookButton");
  }

  public WebElement packageName() {
    return findElement(repositoryParser, PAGE_NAME, "packageName");
  }

  public WebElement hotelElement() {
    return findElement(repositoryParser, PAGE_NAME, "getHotelText");
  }

  public WebElement hotelName() {
    return findElement(repositoryParser, PAGE_NAME, "getHotelName");
  }

  public WebElement getTotalholidayCount() {
    return findElement(repositoryParser, PAGE_NAME, "totalHolidayCount");
  }

  public WebElement getHolidayResultDiv(int count) {
    PageElement pageObject = repositoryParser.getPageObject(PAGE_NAME, "resultDiv");
    String locatorValue = pageObject.getLocatorValue();
    locatorValue = StringUtils.replace(locatorValue, "count", Integer.toString(count));
    pageObject.setLocatorValue(locatorValue);
    WebDriverWait wait = new WebDriverWait(driver, 20);
    return wait.until(ExpectedConditions.visibilityOf(findElement(pageObject)));
  }

  public WebElement sendEnquiry() {
    return findElement(repositoryParser, PAGE_NAME, "sendEnquiry");
  }

  public WebElement cancelEnquiry() {
    return findElement(repositoryParser, PAGE_NAME, "cancelSendEnquiry");
  }

  public WebElement enquiryFirstName() {
    return findElement(repositoryParser, PAGE_NAME, "sendEnquiryFirstName");
  }

  public WebElement enquiryLastName() {
    return findElement(repositoryParser, PAGE_NAME, "sendEnquiryLastName");
  }

  public WebElement enquiryEmail() {
    return findElement(repositoryParser, PAGE_NAME, "sendEnquiryEmail");
  }

  public WebElement enquiryMobileno() {
    return findElement(repositoryParser, PAGE_NAME, "sendEnquiryMobileno");
  }

  public WebElement enquiryCalendar() {
    return findElement(repositoryParser, PAGE_NAME, "calendarSelect");
  }

  public WebElement submitSendEnquiry() {
    return findElement(repositoryParser, PAGE_NAME, "submitSendEnquiry");
  }

  public WebElement cancelSendEnquiry() {
    return findElement(repositoryParser, PAGE_NAME, "cancelSendEnquiry");
  }

  public WebElement alertMsg() {
    return findElement(repositoryParser, PAGE_NAME, "alertMsg");
  }
}
