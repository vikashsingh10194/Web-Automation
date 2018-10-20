package com.via.pageobjects.hotels;

import java.util.List;

import lombok.AllArgsConstructor;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.via.pageobjects.common.PageElement;
import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;

@AllArgsConstructor
public class HotelsSearch extends PageHandler {

  private final String PAGE_NAME = "hotelsSearchPage";
  private RepositoryParser repositoryParser;


  public HotelsSearch(String testId, WebDriver driver, RepositoryParser repositoryParser) {
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

  public WebElement destinationAutoComplete(String targetDestination) {
    WebElement dropdown = findElement(repositoryParser, PAGE_NAME, "destinationAutoComplete");
    List<WebElement> destinations = dropdown.findElements(By.className("ui-menu-item"));
    for (WebElement destination : destinations) {
      WebElement destinationNameElement = destination.findElement(By.className("searchName"));
      String destinationName = destinationNameElement.getText();
      if (StringUtils.equals(destinationName, targetDestination)) {
        return destination;
      }
    }
    return null;
  }

  public WebElement roomDropdown() {
    return findElement(repositoryParser, PAGE_NAME, "roomDropdown");
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
