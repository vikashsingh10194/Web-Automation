package com.via.pageobjects.trains;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.via.pageobjects.common.PageElement;
import com.via.utils.Constant;
import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;
import com.via.utils.StringUtilities;

public class TrainResultPageElements extends PageHandler {
  private RepositoryParser repositoryParser;
  private String HEADER_PAGE_NAME = "trainResultHeader";
  private String CONTAINER_PAGE_NAME = "trainResultContainer";
  private String SELECTION_PANEL_PAGE_NAME = "selectionPanel";

  public TrainResultPageElements(String testId, WebDriver driver, RepositoryParser repositoryParser) {
    super(testId, driver);
    this.repositoryParser = repositoryParser;
  }

  public List<WebElement> getCityCodeElements() {
    return findElements(repositoryParser, HEADER_PAGE_NAME, "cityCode");
  }

  public List<WebElement> getCityNameElements() {
    return findElements(repositoryParser, HEADER_PAGE_NAME, "cityName");
  }

  public WebElement getOnwardDate() {
    return findElement(repositoryParser, HEADER_PAGE_NAME, "onwardDate");
  }

  public WebElement getReturnDate() {
    return findElement(repositoryParser, HEADER_PAGE_NAME, "returnDate");
  }

  public List<WebElement> getPaxDetails() {
    return findElements(repositoryParser, HEADER_PAGE_NAME, "paxDetails");
  }

  public WebElement getModifyButton() {
    return findElement(repositoryParser, HEADER_PAGE_NAME, "modifyButton");
  }

  public List<WebElement> getTrainCountList() {
    return findElements(repositoryParser, CONTAINER_PAGE_NAME, "trainCount");
  }

  public WebElement getTrainInfoIndexedAt(int index) {
    PageElement pageObject = repositoryParser.getPageObject(CONTAINER_PAGE_NAME, "trainInfo");
    String locatorValue =
        StringUtils.replace(pageObject.getLocatorValue(), "count", Integer.toString(index));
    pageObject.setLocatorValue(locatorValue);
    return findElement(pageObject);
  }

  public WebElement getResultDiv(String journeyType, String trainDetails) {

    PageElement pageObject = repositoryParser.getPageObject(CONTAINER_PAGE_NAME, "resultDiv");

    List<String> trainDetailsList = StringUtilities.split(trainDetails, Constant.UNDERSCORE);
    String trainName = StringUtils.upperCase(trainDetailsList.get(0));

    String locatorValue = StringUtils.replace(pageObject.getLocatorValue(), "journey", journeyType);
    locatorValue = StringUtils.replace(locatorValue, "trainName", trainName);

    String coachName = "Business (coach)";
    if (StringUtils.equalsIgnoreCase(trainDetailsList.get(1), "E")) {
      coachName = "Executive (coach)";
    }

    coachName =
        StringUtils.replace(coachName, "coach", StringUtils.upperCase(trainDetailsList.get(2)));

    locatorValue = StringUtils.replace(locatorValue, "coachType", coachName);
    pageObject.setLocatorValue(locatorValue);
    WebElement resultDiv = null;
    try {
      resultDiv = findElement(pageObject);
    } catch (Exception e) {

    }
    return resultDiv;
  }

  public List<WebElement> getTrainTime(WebElement resultDiv) {
    return findElements(resultDiv, repositoryParser, CONTAINER_PAGE_NAME, "depArrTime");
  }

  public List<WebElement> getTrainStations(WebElement resultDiv) {
    return findElements(resultDiv, repositoryParser, CONTAINER_PAGE_NAME, "depArrStations");
  }

  public WebElement getTrainName(WebElement resultDiv) {
    return findElement(resultDiv, repositoryParser, CONTAINER_PAGE_NAME, "trainName");
  }

  public WebElement getTrainClass(WebElement resultDiv) {
    return findElement(resultDiv, repositoryParser, CONTAINER_PAGE_NAME, "class");
  }

  public WebElement getPerAdultFare(WebElement resultDiv) {
    return findElement(resultDiv, repositoryParser, CONTAINER_PAGE_NAME, "perAdultFare");
  }

  public WebElement getSelectButton(WebElement resultDiv) {
    return findElement(resultDiv, repositoryParser, CONTAINER_PAGE_NAME, "selectBtn");
  }

  public WebElement getSelectionPanel(String journeyType) {
    PageElement pageObject =
        repositoryParser.getPageObject(SELECTION_PANEL_PAGE_NAME, "selectionDiv");
    String locatorValue = StringUtils.replace(pageObject.getLocatorValue(), "journey", journeyType);
    pageObject.setLocatorValue(locatorValue);
    return findElement(pageObject);
  }

  public WebElement getTrainNameAtSelectionPanel(WebElement selectionDiv) {
    return findElement(selectionDiv, repositoryParser, SELECTION_PANEL_PAGE_NAME, "trainName");
  }

  public WebElement getCoachTypeFromSelectionPanel(WebElement selectionDiv) {
    return findElement(selectionDiv, repositoryParser, SELECTION_PANEL_PAGE_NAME, "coachType");
  }

  public List<WebElement> getTimeListFromSelectionPanel(WebElement selectionDiv) {
    return findElements(selectionDiv, repositoryParser, SELECTION_PANEL_PAGE_NAME, "deptArrTime");
  }

  public WebElement getFareAtSelectionPanel(WebElement selectionDiv) {
    return findElement(selectionDiv, repositoryParser, SELECTION_PANEL_PAGE_NAME, "fare");
  }

  public WebElement getTotalFareFromSelectionPanel() {
    return findElement(repositoryParser, SELECTION_PANEL_PAGE_NAME, "totalFare");
  }

  public WebElement getBookButton() {
    return findElement(repositoryParser, SELECTION_PANEL_PAGE_NAME, "bookBtn");
  }

}
