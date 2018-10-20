package com.via.pageobjects.trains;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.via.pageobjects.common.PageElement;
import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;

public class TravellersPageElements extends PageHandler {
  private RepositoryParser repositoryParser;
  private final String HEADER_PAGE_NAME = "travellersHeader";
  private final String JOURNEY_DETAILS_PAGE_NAME = "journeyDetails";
  private final String TRAVELLER_DETAILS_PAGE_NAME = "travellersDetails";
  private final String SEAT_SELECTION_PAGE_NAME = "seatSelectionPanel";
  private final String JOURNEY = "journey";
  private final String TRAVELLER_VERIFICATION_PAGE_NAME = "confirmationPanel";

  public TravellersPageElements(String testId, WebDriver driver, RepositoryParser repositoryParser) {
    super(testId, driver);
    this.repositoryParser = repositoryParser;
  }

  public List<WebElement> getCityCode() {
    return findElements(repositoryParser, HEADER_PAGE_NAME, "cityCode");
  }

  public WebElement getJourneyDate() {
    return findElement(repositoryParser, HEADER_PAGE_NAME, "journeyDate");
  }

  public WebElement getPaxDetails() {
    return findElement(repositoryParser, HEADER_PAGE_NAME, "paxDetails");
  }

  public WebElement totalFareAtHeader() {
    return findElement(repositoryParser, HEADER_PAGE_NAME, "totalFare");
  }

  public WebElement expandsFareDetails() {
    return findElement(repositoryParser, HEADER_PAGE_NAME, "expandFareDetails");
  }

  public WebElement getJourneyDiv(int index) {
    PageElement pageObject =
        repositoryParser.getPageObject(JOURNEY_DETAILS_PAGE_NAME, "journeyDiv");
    String locatorValue =
        StringUtils.replace(pageObject.getLocatorValue(), "count", Integer.toString(index));
    pageObject.setLocatorValue(locatorValue);
    return findElement(pageObject);
  }

  public WebElement getCityName(WebElement resultDiv) {
    return findElement(resultDiv, repositoryParser, JOURNEY_DETAILS_PAGE_NAME, "journeyCity");
  }

  public WebElement getTrainName(WebElement resultDiv) {
    return findElement(resultDiv, repositoryParser, JOURNEY_DETAILS_PAGE_NAME, "trainName");
  }

  public WebElement getTrainCoach(WebElement resultDiv) {
    return findElement(resultDiv, repositoryParser, JOURNEY_DETAILS_PAGE_NAME, "trainCoach");
  }

  public List<WebElement> getTrainTimeDetails(WebElement resultDiv) {
    return findElements(resultDiv, repositoryParser, JOURNEY_DETAILS_PAGE_NAME, "trainTime");
  }

  private PageElement getSpecificPageObject(PageElement pageObject, String traveller) {
    String locatorValue = StringUtils.replace(pageObject.getLocatorValue(), "traveller", traveller);
    pageObject.setLocatorValue(locatorValue);
    return pageObject;
  }

  public WebElement getTravellerName(String traveller) {
    PageElement pageObject =
        repositoryParser.getPageObject(TRAVELLER_DETAILS_PAGE_NAME, "travellerName");
    return findElement(getSpecificPageObject(pageObject, traveller));
  }

  public WebElement getTravellerMobileNo(String traveller) {
    PageElement pageObject =
        repositoryParser.getPageObject(TRAVELLER_DETAILS_PAGE_NAME, "travellerMobNo");
    return findElement(getSpecificPageObject(pageObject, traveller));
  }

  public WebElement getTravellerIdType(String traveller) {
    PageElement pageObject =
        repositoryParser.getPageObject(TRAVELLER_DETAILS_PAGE_NAME, "travellerIdType");
    return findElement(getSpecificPageObject(pageObject, traveller));
  }

  public WebElement getTravellerIdNo(String traveller) {
    PageElement pageObject =
        repositoryParser.getPageObject(TRAVELLER_DETAILS_PAGE_NAME, "travellerIdNo");
    return findElement(getSpecificPageObject(pageObject, traveller));
  }

  public WebElement getExpandButton(String traveller) {
    PageElement pageObject =
        repositoryParser.getPageObject(TRAVELLER_DETAILS_PAGE_NAME, "travellersExpandBtn");
    return findElement(getSpecificPageObject(pageObject, traveller));
  }

  public WebElement getTravellerDOBDay(String traveller) {
    PageElement pageObject =
        repositoryParser.getPageObject(TRAVELLER_DETAILS_PAGE_NAME, "travellerDOBday");
    return findElement(getSpecificPageObject(pageObject, traveller));
  }

  public WebElement getTravellerDOBMonth(String traveller) {
    PageElement pageObject =
        repositoryParser.getPageObject(TRAVELLER_DETAILS_PAGE_NAME, "travellerDOBMonth");
    return findElement(getSpecificPageObject(pageObject, traveller));
  }

  public WebElement getTravellerDOBYear(String traveller) {
    PageElement pageObject =
        repositoryParser.getPageObject(TRAVELLER_DETAILS_PAGE_NAME, "travellerDOBYear");
    return findElement(getSpecificPageObject(pageObject, traveller));
  }

  public WebElement getNoSplit() {
    return findElement(repositoryParser, TRAVELLER_DETAILS_PAGE_NAME, "splitBookingNo");
  }

  public WebElement getSplit() {
    return findElement(repositoryParser, TRAVELLER_DETAILS_PAGE_NAME, "splitBookingYes");
  }

  public WebElement getAutoAllocation() {
    return findElement(repositoryParser, TRAVELLER_DETAILS_PAGE_NAME, "autoAllocation");
  }

  public WebElement getManualAllocation() {
    return findElement(repositoryParser, TRAVELLER_DETAILS_PAGE_NAME, "manualAllocation");
  }

  public List<WebElement> getSeatSelectionBtn() {
    return findElements(repositoryParser, TRAVELLER_DETAILS_PAGE_NAME, "seatSelection");
  }

  private PageElement getSpecificPageObject(PageElement pageObject, String text, String replacement) {
    String locatorValue = StringUtils.replace(pageObject.getLocatorValue(), text, replacement);
    pageObject.setLocatorValue(locatorValue);
    return pageObject;
  }

  public WebElement getSeatDiv(String journeyType) {
    PageElement pageObject =
        repositoryParser.getPageObject(SEAT_SELECTION_PAGE_NAME, "seatMapPanel");
    return findElement(getSpecificPageObject(pageObject, JOURNEY, journeyType));
  }

  public WebElement getJourneyCity(WebElement journeySeatDiv) {
    return findElement(journeySeatDiv, repositoryParser, SEAT_SELECTION_PAGE_NAME, "cityCode");
  }

  public WebElement getDate(WebElement journeySeatDiv) {
    return findElement(journeySeatDiv, repositoryParser, SEAT_SELECTION_PAGE_NAME, "date");
  }

  public List<WebElement> getTime(WebElement journeySeatDiv) {
    return findElements(journeySeatDiv, repositoryParser, SEAT_SELECTION_PAGE_NAME, "time");
  }

  public List<WebElement> getTrainNameAtSeatMap(WebElement journeySeatDiv) {
    return findElements(journeySeatDiv, repositoryParser, SEAT_SELECTION_PAGE_NAME, "trainInfo");
  }

  public WebElement getAdultCount(WebElement journeySeatDiv) {
    return findElement(journeySeatDiv, repositoryParser, SEAT_SELECTION_PAGE_NAME, "paxInfo");
  }

  public List<WebElement> getAllCoaches(String journeyType) {
    PageElement pageObject =
        repositoryParser.getPageObject(SEAT_SELECTION_PAGE_NAME, "coachButton");
    return findElements(getSpecificPageObject(pageObject, JOURNEY, journeyType));
  }

  public List<WebElement> getAvailableSeats(WebElement journeySeatDiv, int count) {
    PageElement pageObject =
        repositoryParser.getPageObject(SEAT_SELECTION_PAGE_NAME, "availableSeats");
    return findElements(journeySeatDiv,
        getSpecificPageObject(pageObject, "count", Integer.toString(count)));
  }

  public WebElement getProceedWithSeat(WebElement journeySeatDiv) {
    return findElement(journeySeatDiv, repositoryParser, SEAT_SELECTION_PAGE_NAME, "proceedSeatBtn");
  }

  public List<WebElement> getSelectedSeatDetails() {
    return findElements(repositoryParser, SEAT_SELECTION_PAGE_NAME, "selectedSeat");
  }

  public List<WebElement> getCompleteFareDetails() {
    return findElements(repositoryParser, TRAVELLER_DETAILS_PAGE_NAME, "fareDetails");
  }

  public WebElement getISDCode() {
    return findElement(repositoryParser, TRAVELLER_DETAILS_PAGE_NAME, "ISDCode");
  }

  public WebElement getContactNo() {
    return findElement(repositoryParser, TRAVELLER_DETAILS_PAGE_NAME, "contactNo");
  }

  public WebElement getEmail() {
    return findElement(repositoryParser, TRAVELLER_DETAILS_PAGE_NAME, "contactEmail");
  }

  public WebElement getTermsCheckBox() {
    return findElement(repositoryParser, TRAVELLER_DETAILS_PAGE_NAME, "termsCheckBox");
  }

  public WebElement getMakePaymentBtn() {
    return findElement(repositoryParser, TRAVELLER_DETAILS_PAGE_NAME, "makePayment");
  }

  public List<WebElement> getTravellerDetails() {
    return findElements(repositoryParser, TRAVELLER_VERIFICATION_PAGE_NAME, "travellerDetails");
  }

  public List<WebElement> getSeatDetails() {
    return findElements(repositoryParser, TRAVELLER_VERIFICATION_PAGE_NAME, "seatDetails");
  }

  public List<WebElement> getTravellerName() {
    return findElements(repositoryParser, TRAVELLER_VERIFICATION_PAGE_NAME, "travellerName");
  }

  public WebElement getTravellerType(WebElement travellerDetails) {
    return findElement(travellerDetails, repositoryParser, TRAVELLER_VERIFICATION_PAGE_NAME,
        "travellerType");
  }

  public List<WebElement> getKeys(WebElement travellerDetails) {
    return findElements(travellerDetails, repositoryParser, TRAVELLER_VERIFICATION_PAGE_NAME,
        "detailsKey");
  }

  public List<WebElement> getValues(WebElement travellerDetails) {
    return findElements(travellerDetails, repositoryParser, TRAVELLER_VERIFICATION_PAGE_NAME,
        "detailsValue");
  }

  public WebElement getConfirmEmail() {
    return findElement(repositoryParser, TRAVELLER_VERIFICATION_PAGE_NAME, "contactEmail");
  }

  public WebElement getConfirmContactNo() {
    return findElement(repositoryParser, TRAVELLER_VERIFICATION_PAGE_NAME, "contactNo");
  }

  public WebElement getProceedPaymentBtn() {
    return findElement(repositoryParser, TRAVELLER_VERIFICATION_PAGE_NAME, "confirmPaymentBtn");
  }
}
