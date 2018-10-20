package com.via.pageobjects.flights.common;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.via.pageobjects.common.PageElement;
import com.via.utils.Constant;
import com.via.utils.CustomAssert;
import com.via.utils.PageHandler;
import com.via.utils.RandomValues;
import com.via.utils.RepositoryParser;
import com.via.utils.Constant.VIA_COUNTRY;

public class NewTravellersPageElements extends PageHandler {
  private final String PAGE_NAME = "newTravellersInformation";

  private RepositoryParser repositoryParser;
  WebDriverWait wait;

  public NewTravellersPageElements(String testId, WebDriver driver,
      RepositoryParser repositoryParser) {
    super(testId, driver);
    this.repositoryParser = repositoryParser;
    wait = new WebDriverWait(driver, 10 * 1000);
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

  // This method does not use any CommonUtils method or any JSON repository
  public WebElement getElementById(String id) {
    WebElement element = driver.findElement(By.id(id));
    return element;
  }

  public WebElement getElementByPageObject(PageElement pageElement) {
    WebElement element = findElement(pageElement);
    Assert.assertTrue(element != null);
    return element;
  }

  public WebElement selectOptions(WebElement element, int targetValue) {
    List<WebElement> titles = element.findElements(By.tagName("option"));
    WebElement targetTitle = null;
    for (WebElement title : titles) {
      String valueAttribute = title.getAttribute("value");
      Integer value = Integer.parseInt(valueAttribute);
      if (Integer.compare(value, targetValue) == 0) {
        targetTitle = title;
      }
    }
    Assert.assertTrue(targetTitle != null);
    return targetTitle;
  }

  public WebElement getRandomOption(WebElement element) {
    List<WebElement> options = element.findElements(By.tagName("option"));
    int optionsLength = options.size();
    int randomOption = RandomValues.getRandomNumberBetween(1, optionsLength - 1);
    WebElement option = options.get(randomOption);
    return option;
  }

  public List<WebElement> getOptions(WebElement element) {
    List<WebElement> options = element.findElements(By.tagName("option"));
    return options;
  }
  
  public WebElement getISDCodeHeader() {
    return findElement(repositoryParser, PAGE_NAME, "ISDCode");
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

  public WebElement getLayover() {
    return findElement(repositoryParser, PAGE_NAME, "layover");
  }

  public WebElement getOnwardFlightsStopsAndCount() {
    return findElement(repositoryParser, PAGE_NAME, "onwardFlightsStopsAndCount");
  }

  public WebElement getReturnFlightsStopsAndCount() {
    return findElement(repositoryParser, PAGE_NAME, "returnFlightsStopsAndCount");
  }

  public WebElement getReturnFlightsCount() {
    return findElement(repositoryParser, PAGE_NAME, "returnFlightsCount");
  }

  public WebElement getTotalFare() {
    return findElement(repositoryParser, PAGE_NAME, "totalFareAmount");
  }

  public WebElement getNoInc() {
    WebElement noInc = findElement(repositoryParser, PAGE_NAME, "noInc");
    return noInc;
  }

  public WebElement getDobDiv(String passengerType) {
    return findElement(modifyPageElementOnce("dobDiv", passengerType));
  }

  public WebElement getVisaInfoRadio() {
    WebElement visaInfoRadio = findElement(repositoryParser, PAGE_NAME, "visaInfoRadio");
    return visaInfoRadio;
  }

  private PageElement getPageObject(String objectName, String replace, String replacement) {
    PageElement pageObject = repositoryParser.getPageObject(PAGE_NAME, objectName);
    String locatorValue = StringUtils.replace(pageObject.getLocatorValue(), replace, replacement);
    pageObject.setLocatorValue(locatorValue);
    return pageObject;
  }

  public WebElement getPassengerDiv(String traveller) {
    return findElement(getPageObject("passengerDiv", "passengerType", traveller));
  }

  public WebElement getMoreOptions(WebElement travellerDiv) {
    WebElement options = null;
    PageElement pageObject = repositoryParser.getPageObject(PAGE_NAME, "moreOptionsExpand");
    try {
      options = travellerDiv.findElement(By.xpath(pageObject.getLocatorValue()));
    } catch (Exception e) {
    }
    return options;
  }

  public WebElement getHiddenMoreCheckBox(WebElement travellerDiv) {
    WebElement options = null;
    PageElement pageObject = repositoryParser.getPageObject(PAGE_NAME, "moreOptionsHiddenCheckBox");
    try {
      options = travellerDiv.findElement(By.xpath(pageObject.getLocatorValue()));
    } catch (Exception e) {
    }
    return options;
  }

  public List<WebElement> getMealOptions(WebElement travellerDiv) {
    List<WebElement> optionsList = new ArrayList<WebElement>();
    try {
      optionsList = findElements(travellerDiv, repositoryParser, PAGE_NAME, "mealOption");
    } catch (Exception e) {
      CustomAssert.assertFail(testId, "Meal not available.");
    }
    return optionsList;
  }

  public List<WebElement> getBaggageOptions(WebElement travellerDiv) {
   List<WebElement> optionsList = new ArrayList<WebElement>();
    try {
      optionsList = findElements(travellerDiv, repositoryParser, PAGE_NAME, "baggageOption");
    } catch (Exception e) {
      CustomAssert.assertFail(testId, "Baggage not available.");
    }
    return optionsList;
  }

  public List<WebElement> getMealSectorList(WebElement travellerDiv) {
    List<WebElement> sectorList = new ArrayList<WebElement>();
    try {
      sectorList = findElements(travellerDiv, repositoryParser, PAGE_NAME, "mealSector");
    } catch (Exception e) {
      CustomAssert.assertFail(testId, "Meal Sector not available.");
    }
    return sectorList;
  }

  public List<WebElement> getBaggageSectorList(WebElement travellerDiv) {
    List<WebElement> sectorList = new ArrayList<WebElement>();
    try {
      sectorList = findElements(travellerDiv, repositoryParser, PAGE_NAME, "baggageSector");
    } catch (Exception e) {
      CustomAssert.assertFail(testId, "Baggage Sector not available.");
    }
    return sectorList;
  }

  public WebElement getSeatExpandBtn() {
    return findElement(repositoryParser, PAGE_NAME, "seatExpandBtn");
  }

  public WebElement getFlightSeatBtn(String flightNo) {
    return findElement(getPageObject("flightSeatBtn", "flightNo", flightNo));
  }

  public WebElement getFlightSeatDiv(String flightSector, VIA_COUNTRY country) {
    //return findElement(repositoryParser, PAGE_NAME, "flightSeatMap");
    if(country == Constant.VIA_COUNTRY.IN_CORP){
      return wait.until(ExpectedConditions.visibilityOf(findElement(getPageObject("corpFlightSeatMap",
          "sector", StringUtils.replace(flightSector, "-", "_")))));
    }
    return wait.until(ExpectedConditions.visibilityOf(findElement(getPageObject("flightSeatMap",
        "sector", StringUtils.replace(flightSector, "-", "_")))));
  }

  public List<WebElement> getAvailableSeats(WebElement seatDiv) {
    return findElements(seatDiv,
        repositoryParser, PAGE_NAME, "availableSeats");
  }

  public WebElement selectedSeat(VIA_COUNTRY country, String... toModify) {
    PageElement element = null;
    if(country == Constant.VIA_COUNTRY.IN_CORP){
      element =
          PageHandler.modifyPageElement(repositoryParser, PAGE_NAME, "corpSelectedSeat", toModify);
    }else{
      element =
          PageHandler.modifyPageElement(repositoryParser, PAGE_NAME, "selectedSeat", toModify);
    }
    
    return findElement(element);
  }

  public WebElement getSelectedSeatFare(VIA_COUNTRY country, String... toModify) {
    PageElement element = null;
  if(country == Constant.VIA_COUNTRY.IN_CORP){
    element =
        PageHandler.modifyPageElement(repositoryParser, PAGE_NAME, "corpSelectedSeatFare", toModify);
  }else{
    element =
        PageHandler.modifyPageElement(repositoryParser, PAGE_NAME, "selectedSeatFare", toModify);
  }
  
  return findElement(element);
  }

  public WebElement getProceedWithSeat(WebElement seatDiv) {
    return findElement(seatDiv, repositoryParser, PAGE_NAME, "proceedWithSeat");
  }


  // Added for new TDP ..20/03/2017

  public WebElement getTitle(String pIndex) {
    PageElement title = modifyPageElementOnce("title", pIndex);
    return findElement(title);
  }

  public WebElement getFirstName(String pIndex) {
    PageElement name = modifyPageElementOnce("firstName", pIndex);
    return findElement(name);
  }

  public WebElement getSirname(String pIndex) {
    PageElement sirName = modifyPageElementOnce("sirName", pIndex);
    return findElement(sirName);
  }

  public String getPassengerType(String pIndex) {
    PageElement passengerType = modifyPageElementOnce("passengerType", pIndex);
    return findElement(passengerType).getAttribute("data-type");
  }

  public WebElement getDate(String pIndex) {
    PageElement date = modifyPageElementOnce("date", pIndex);
    return findElement(date);
  }

  public WebElement getMonth(String pIndex) {
    PageElement month = modifyPageElementOnce("month", pIndex);
    return findElement(month);
  }

  public WebElement getYear(String pIndex) {
    PageElement year = modifyPageElementOnce("year", pIndex);
    return findElement(year);
  }

  public WebElement onwardReturnFlightDetails(String... toModify) {
    PageElement element =
        PageHandler.modifyPageElement(repositoryParser, PAGE_NAME, "onwardReturnFlightDetails",
            toModify);
    return findElement(element);
  }

  public List<WebElement> returnFlightDetails() {
    return findElements(repositoryParser, PAGE_NAME, "returnFlightDetails");
  }

  public String src(String... toModify) {
    PageElement element =
        PageHandler.modifyPageElement(repositoryParser, PAGE_NAME, "src", toModify);
    return findElement(element).getText();
  }

  public String dest(String... toModify) {
    PageElement element =
        PageHandler.modifyPageElement(repositoryParser, PAGE_NAME, "dest", toModify);
    return findElement(element).getText();
  }

  public String flightName(String... toModify) {
    PageElement element =
        PageHandler.modifyPageElement(repositoryParser, PAGE_NAME, "flightName", toModify);
    return findElement(element).getText();
  }

  public String flightCode(String... toModify) {
    PageElement element =
        PageHandler.modifyPageElement(repositoryParser, PAGE_NAME, "flightCode", toModify);
    return findElement(element).getText();
  }

  public String departureTime(String... toModify) {
    PageElement element =
        PageHandler.modifyPageElement(repositoryParser, PAGE_NAME, "departureTime", toModify);
    return findElement(element).getText();
  }

  public String arrivalTime(String... toModify) {
    PageElement element =
        PageHandler.modifyPageElement(repositoryParser, PAGE_NAME, "arrivalTime", toModify);
    return findElement(element).getText();
  }

  public String departureDate(String... toModify) {
    PageElement element =
        PageHandler.modifyPageElement(repositoryParser, PAGE_NAME, "departureDate", toModify);
    return findElement(element).getText();
  }

  public String arrivalDate(String... toModify) {
    PageElement element =
        PageHandler.modifyPageElement(repositoryParser, PAGE_NAME, "arrivalDate", toModify);
    return findElement(element).getText();
  }

  public WebElement selectAddons() {
    WebElement element =
        wait.until(ExpectedConditions.visibilityOf(findElement(repositoryParser, PAGE_NAME,
            "selectAddons")));
    return element;
  }

  public List<WebElement> onwardMealSectors() {
    return findElements(repositoryParser, PAGE_NAME, "onwardMealSectors");
  }

  public List<WebElement> returnMealSectors() {
    return findElements(repositoryParser, PAGE_NAME, "returnMealSectors");
  }

  public List<WebElement> onwardMealOptions() {
    return findElements(repositoryParser, PAGE_NAME, "onwardMealOptions");
  }

  public List<WebElement> returnMealOptions() {
    return findElements(repositoryParser, PAGE_NAME, "returnMealOptions");
  }
  
  public List<WebElement> onwardBaggageSector() {
    return findElements(repositoryParser, PAGE_NAME, "onwardBaggageSector");
  }

  public List<WebElement> returnBaggageSector() {
    return findElements(repositoryParser, PAGE_NAME, "returnBaggageSector");
  }

  public List<WebElement> onwardBaggageOptions() {
    return findElements(repositoryParser, PAGE_NAME, "onwardBaggageOptions");
  }


  public List<WebElement> returnBaggageOptions() {
    return findElements(repositoryParser, PAGE_NAME, "returnBaggageOptions");
  }
  public WebElement meal() {
    return findElement(repositoryParser, PAGE_NAME,
        "mealsButton");
  }

  public WebElement baggage() {
    return findElement(repositoryParser, PAGE_NAME,
        "baggageButton");
  }

  public WebElement seat() {
    return findElement(repositoryParser, PAGE_NAME,
        "seatsButton");
  }

  public WebElement flightSeatBtn(String... toModify) {
    PageElement flightSeatBtn =
        PageHandler.modifyPageElement(repositoryParser, PAGE_NAME, "flightSeatBtn", toModify);
    return findElement(flightSeatBtn);
  }

  public WebElement addonContinue() {
    return findElement(repositoryParser, PAGE_NAME, "addonContinue");
  }

  public WebElement expandFlightsDetails() {
    return findElement(repositoryParser, PAGE_NAME, "expandFlightsDetails");
  }

  public WebElement collapseFlightsDetails() {
    return findElement(repositoryParser, PAGE_NAME, "collapseFlightsDetails");
  }

  public List<WebElement> proceed() {
    return findElements(repositoryParser, PAGE_NAME, "proceed");
  }

  public WebElement passportNationalty(String pIndex) {
    PageElement date = modifyPageElementOnce("passportNationalty", pIndex);
    return findElement(date);
  }

  public WebElement passportNumber(String pIndex) {
    PageElement date = modifyPageElementOnce("passportNumber", pIndex);
    return findElement(date);
  }

  public WebElement pExpiryDate(String pIndex) {
    PageElement date = modifyPageElementOnce("pExpiryDate", pIndex);
    return findElement(date);
  }

  public WebElement pExpiryMonth(String pIndex) {
    PageElement date = modifyPageElementOnce("pExpiryMonth", pIndex);
    return findElement(date);
  }

  public WebElement pExpiryYear(String pIndex) {
    PageElement date = modifyPageElementOnce("pExpiryYear", pIndex);
    return findElement(date);
  }

  public List<WebElement> addonsSummary() {
    return findElements(repositoryParser, PAGE_NAME, "addonsSummary");
  }

  public WebElement insTermsCondition() {
    return findElement(repositoryParser, PAGE_NAME, "insTermsCondition");
  }

  public WebElement getVoucherElement() {
    return findElement(repositoryParser, PAGE_NAME, "voucher");
  }

  public WebElement alertMessage() {
    return findElement(repositoryParser, PAGE_NAME, "alertMessage");
  }

  public WebElement voucherValidate() {
    return findElement(repositoryParser, PAGE_NAME, "voucherValidate");
  }

  public WebElement totalFare() {
    return findElement(repositoryParser, PAGE_NAME, "totalPrice");
  }

  public WebElement nationaltyAutoComplete(String... countryName) {
    PageElement element =
        PageHandler.modifyPageElement(repositoryParser, PAGE_NAME, "nationaltyAutoComplete",
            countryName);
    return findElement(element);
  }

  public WebElement notifyInsurance() {
    return findElement(repositoryParser, PAGE_NAME, "notifyInsurance");
  }

  public WebElement continueElement() {
    return findElement(repositoryParser, PAGE_NAME, "continue");
  }

  public WebElement step1Proceed() {
    return findElement(repositoryParser, PAGE_NAME, "step1Proceed");
  }

  public WebElement step2Proceed() {
    return findElement(repositoryParser, PAGE_NAME, "step2Proceed");
  }
}
