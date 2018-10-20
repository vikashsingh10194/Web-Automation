package com.via.pageobjects.flights.common;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;

public class BookingDetailsElements extends PageHandler {
  private RepositoryParser repositoryParser;
  private final String PAGE_NAME = "bookingDetails";

  public BookingDetailsElements(String testId, WebDriver driver, RepositoryParser repositoryParser) {
    super(testId, driver);
    this.repositoryParser = repositoryParser;
  }

  public WebElement getSource() {
    return findElement(repositoryParser, PAGE_NAME, "source");
  }

  public WebElement getDestination() {
    return findElement(repositoryParser, PAGE_NAME, "destination");
  }

  public WebElement getJourneyType() {
    return findElement(repositoryParser, PAGE_NAME, "journeyType");
  }

  public WebElement getBookingId() {
    return findElement(repositoryParser, PAGE_NAME, "bookingId");
  }

  public WebElement getJourneyDates() {
    return findElement(repositoryParser, PAGE_NAME, "journeyDates");
  }

  public WebElement getPassengerCount() {
    return findElement(repositoryParser, PAGE_NAME, "pessagnerCount");
  }

  public WebElement getBookingDate() {
    return findElement(repositoryParser, PAGE_NAME, "bookingDate");
  }

  public WebElement getOnwardDiv() {
    return findElement(repositoryParser, PAGE_NAME, "onwardDiv");
  }

  public WebElement getReturnDiv() {
    return findElement(repositoryParser, PAGE_NAME, "returnDiv");
  }

  public List<WebElement> getAirlineName(WebElement journeyDiv) {
    return findElements(journeyDiv, repositoryParser, PAGE_NAME, "airlineName");
  }

  public List<WebElement> getFlightNo(WebElement journeyDiv) {
    return findElements(journeyDiv, repositoryParser, PAGE_NAME, "flightNo");
  }

  public List<WebElement> getAirportCode(WebElement journeyDiv) {
    return findElements(journeyDiv, repositoryParser, PAGE_NAME, "airportCode");
  }

  public List<WebElement> getCityName(WebElement journeyDiv) {
    return findElements(journeyDiv, repositoryParser, PAGE_NAME, "cityName");
  }

  public List<WebElement> getDates(WebElement journeyDiv) {
    return findElements(journeyDiv, repositoryParser, PAGE_NAME, "dates");
  }

  public List<WebElement> getTravellersName() {
    return findElements(repositoryParser, PAGE_NAME, "travellerName");
  }

  public List<WebElement> getTravellersDetails() {
    return findElements(repositoryParser, PAGE_NAME, "travellerDetails");
  }

  public List<WebElement> getPassportDetails() {
    return findElements(repositoryParser, PAGE_NAME, "passportDetails");
  }

  public List<WebElement> getTravellersPnr() {
    return findElements(repositoryParser, PAGE_NAME, "pnr");
  }

  public List<WebElement> getFlier() {
    return findElements(repositoryParser, PAGE_NAME, "flierNo");
  }

  public List<WebElement> getEticket() {
    return findElements(repositoryParser, PAGE_NAME, "eticket");
  }

  public List<WebElement> getInsurance() {
    return findElements(repositoryParser, PAGE_NAME, "insurance");
  }

  public List<WebElement> getStatus() {
    return findElements(repositoryParser, PAGE_NAME, "status");
  }

  public List<WebElement> getSSRName() {
    List<WebElement> nameList = findElements(repositoryParser, PAGE_NAME, "ssrName");
    return nameList;
  }

  public List<WebElement> getSSRMeal() {
    List<WebElement> mealList = findElements(repositoryParser, PAGE_NAME, "ssrMeal");
    return mealList;
  }

  public List<WebElement> getSSRSeat() {
    List<WebElement> seatList = findElements(repositoryParser, PAGE_NAME, "ssrSeat");
    return seatList;
  }

  public List<WebElement> getSSRBaggage() {
    List<WebElement> baggageList = findElements(repositoryParser, PAGE_NAME, "ssrBaggage");
    return baggageList;
  }

  public List<WebElement> getPaymentDetails() {
    return findElements(repositoryParser, PAGE_NAME, "payment");
  }

  public WebElement getCancelBtn() {
    WebDriverWait wait = new WebDriverWait(driver, 20);
    return wait.until(ExpectedConditions.elementToBeClickable(findElement(repositoryParser,
        PAGE_NAME, "cancelBtn")));
  }
}
