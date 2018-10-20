package com.via.pageobjects.hotels;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.via.pageobjects.common.PageElement;
import com.via.utils.PageHandler;
import com.via.utils.CustomAssert;
import com.via.utils.RepositoryParser;

public class HotelSearchResultElements extends PageHandler {

  private final String PAGE_NAME = "hotelsSearchResults";
  private RepositoryParser repositoryParser;

  public HotelSearchResultElements(String testId, WebDriver driver,
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
    CustomAssert.assertTrue(testId, element != null,
        "Element not found corresponding to the page lement " + pageElement.getName());
    return element;
  }

  public List<WebElement> getElementsByPageObject(PageElement pageElement) {
    List<WebElement> elements = findElements(pageElement);
    CustomAssert.assertTrue(testId, elements != null,
        "Elements not found corresponding to the page lement " + pageElement.getName());
    return elements;
  }

  // Hotels Search Box Validation Elements
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

  // Hotels Select Elements
  public WebElement allHotelCount() {
    return findElement(repositoryParser, PAGE_NAME, "allHotelCount");
  }

  public WebElement hotelResults() {
    return findElement(repositoryParser, PAGE_NAME, "hotelResults");
  }

  public WebElement selectRooms() {
    return findElement(repositoryParser, PAGE_NAME, "selectRooms");
  }

  public WebElement hotelName() {
    return findElement(repositoryParser, PAGE_NAME, "hotelName");
  }

  public WebElement hotelAddress() {
    return findElement(repositoryParser, PAGE_NAME, "hotelAddress");
  }

  // Search by hotel name results elements
  public WebElement nameSearchHotelName() {
    return findElement(repositoryParser, PAGE_NAME, "nameSearchHotelName");
  }

  public WebElement nameSearchHotelAddress() {
    return findElement(repositoryParser, PAGE_NAME, "nameSearchHotelAddress");
  }

  public WebElement nameSearchBookRoom() {
    return findElement(repositoryParser, PAGE_NAME, "nameSearchBookRoom");
  }

  public WebElement nameSearchRoomType() {
    return findElement(repositoryParser, PAGE_NAME, "nameSearchRoomType");
  }

  public WebElement nameSearchRoomPrice() {
    return findElement(repositoryParser, PAGE_NAME, "nameSearchRoomPrice");
  }
}
