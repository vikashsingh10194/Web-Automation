package com.via.appmodules.flights.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.via.pageobjects.common.PageElement;
import com.via.pageobjects.flights.common.FlightBookingDetails;
import com.via.pageobjects.flights.common.FlightDetails;
import com.via.pageobjects.flights.common.HeaderElements;
import com.via.pageobjects.flights.common.SSR;
import com.via.pageobjects.flights.common.TravellerDetails;
import com.via.pageobjects.flights.common.TravellersPageElements;
import com.via.utils.CalendarUtils;
import com.via.utils.Constant;
import com.via.utils.Constant.Flight;
import com.via.utils.Constant.Journey;
import com.via.utils.Constant.Traveller;
import com.via.utils.Constant.VIA_COUNTRY;
import com.via.utils.CustomAssert;
import com.via.utils.EntityMap;
import com.via.utils.Log;
import com.via.utils.NumberUtility;
import com.via.utils.PageHandler;
import com.via.utils.RandomValues;
import com.via.utils.RepositoryParser;
import com.via.utils.StringUtilities;

public class TravellersDetailsInput {

  private String testId;
  private VIA_COUNTRY countryCode;
  private WebDriver driver;
  private RepositoryParser repositoryParser;
  private FlightBookingDetails flightBookingDetails;

  private final String ISD_CODE = "91";
  private final String CONTACT_MOBILE = "9611577993";
  private final String CONTACT_MOBILE_COMPLETE = "+91-9611577993";
  private final String CONTACT_EMAIL = "qa@via.com";

  private final String ADULT = "adult";
  private final String CHILD = "child";
  private final String INFANT = "infant";
  private final String Traveller_HEADER_PAGE_NAME = "travellersHeader";

  boolean mealBaggageSelect = false;
  boolean seatSelect = false;

  private List<Boolean> meal = new ArrayList<Boolean>();
  private List<Boolean> baggage = new ArrayList<Boolean>();
  private List<Boolean> seat = new ArrayList<Boolean>();
  private Map<String, String> onwardSectorFlightMap;
  private Map<String, String> returnSectorFlightMap;

  public TravellersDetailsInput(String testId, VIA_COUNTRY countryCode, WebDriver driver,
      RepositoryParser repositoryParser, FlightBookingDetails flightBookingDetails) {
    this.testId = testId;
    this.driver = driver;
    this.countryCode = countryCode;
    this.repositoryParser = repositoryParser;
    this.flightBookingDetails = flightBookingDetails;
    this.onwardSectorFlightMap = getSectorFlightMap(flightBookingDetails.getOnwardFlights());
    this.returnSectorFlightMap = getSectorFlightMap(flightBookingDetails.getReturnFlights());

  }

  public List<TravellerDetails> execute(Flight flightType, TravellersPageElements travellersElements) {

    Calendar onwardCalendar = flightBookingDetails.getOnwardDate();

    RandomValues.setUsedNameList();

    setSSRSelection(flightBookingDetails);

    List<TravellerDetails> travellerDetailsList = new LinkedList<TravellerDetails>();

    List<TravellerDetails> adultDetailsList =
        setPassengerDetails(flightType, travellersElements, ADULT,
            flightBookingDetails.getAdultsCount(), onwardCalendar);
    travellerDetailsList.addAll(adultDetailsList);

    List<TravellerDetails> childrenDetailsList =
        setPassengerDetails(flightType, travellersElements, CHILD,
            flightBookingDetails.getChildrenCount(), onwardCalendar);
    travellerDetailsList.addAll(childrenDetailsList);

    List<TravellerDetails> infantDetailsList =
        setPassengerDetails(flightType, travellersElements, INFANT,
            flightBookingDetails.getInfantsCount(), onwardCalendar);
    travellerDetailsList.addAll(infantDetailsList);

    if (seatSelect) {
      seatSelection(travellersElements, travellerDetailsList);
    }

    if (mealBaggageSelect || seatSelect) {
    	//Kishor-from 3 to 1
      PageHandler.sleep(testId, 1 * 1000L);
    }

    Double totalFare = flightBookingDetails.getTotalFare();

    Log.info(
        testId,
        "Total Fare after addons Selection : "
            + NumberUtility.getRoundedAmount(countryCode, totalFare));

    Double totalFareAtHeader =
        NumberUtility.getAmountFromString(countryCode, new HeaderElements(testId, driver,
            repositoryParser).getTotalFare(Traveller_HEADER_PAGE_NAME).getText());

    Double diff = Math.abs(totalFareAtHeader - totalFare);

    CustomAssert.assertTrue(
        testId,
        diff < 0.1,
        "Total fare at Traveller Header not verified. Expected : "
            + NumberUtility.getRoundedAmount(countryCode, totalFare) + " Actual : "
            + NumberUtility.getRoundedAmount(countryCode, totalFareAtHeader));

    setTaxExempt();
    setContactDetails(travellersElements);

    setInsurace(travellersElements, travellerDetailsList);

    acceptVisaInfo();

    WebElement readTermsLabel = travellersElements.getReadTermsLabel();
    PageHandler.javaScriptExecuterClick(driver, readTermsLabel);

    WebElement makePayment = travellersElements.makePayment();
    PageHandler.javaScriptExecuterClick(driver, makePayment);

    if (flightBookingDetails.isInsurance()) {
      //Kishor-From 2 to 1
    	PageHandler.sleep(testId, 1000L);
      Double totalAmountAfterIns =
          NumberUtility.getAmountFromString(countryCode, new HeaderElements(testId, driver,
              repositoryParser).getTotalFare(Traveller_HEADER_PAGE_NAME).getText());
      flightBookingDetails.setTotalFare(totalAmountAfterIns);
      Log.info(
          testId,
          "Total Amount after insurance : "
              + NumberUtility.getRoundedAmount(countryCode, totalAmountAfterIns));
    }

    return travellerDetailsList;
  }

  private void seatSelection(TravellersPageElements travellersElements,
      List<TravellerDetails> travellerDetailsList) {
    WebElement seatExpand = travellersElements.getSeatExpandBtn();
    PageHandler.javaScriptExecuterClick(driver, seatExpand);
    Double totalSeatFare = 0.0;
    if (seat.get(0)) {
      totalSeatFare +=
          setSeatDetails(onwardSectorFlightMap, travellersElements, travellerDetailsList);
    }
    if (flightBookingDetails.getJourneyType() == Journey.ROUND_TRIP && seat.get(1)) {
      totalSeatFare +=
          setSeatDetails(returnSectorFlightMap, travellersElements, travellerDetailsList);
    }

    Double totalFare = flightBookingDetails.getTotalFare() + totalSeatFare;

    flightBookingDetails.setTotalFare(totalFare);
  }

  private Double setSeatDetails(Map<String, String> sectorFlightMap,
      TravellersPageElements travellersElements, List<TravellerDetails> travellerDetailsList) {

    Double totalSeatFare = 0.0;

    for (Entry<String, String> entry : sectorFlightMap.entrySet()) {
      String sector = entry.getKey();
      String flightNo = entry.getValue();

      WebElement seatMapBtn = travellersElements.getFlightSeatBtn(flightNo);
      PageHandler.javaScriptExecuterClick(driver, seatMapBtn);
      //Kishor from 2 to 1
      PageHandler.sleep(testId, 1000L);

      WebElement seatDiv = travellersElements.getFlightSeatDiv(sector);
      List<WebElement> availableSeats = travellersElements.getAvailableSeats(seatDiv);

      int totalSeats =
          flightBookingDetails.getAdultsCount() + flightBookingDetails.getChildrenCount();
      for (int seatCount = 0; seatCount < totalSeats && seatCount < availableSeats.size(); seatCount++) {
        TravellerDetails travellerDetails = travellerDetailsList.get(seatCount);
        Map<String, SSR> ssrDetails = travellerDetails.getSsrDetails();

        SSR ssr = ssrDetails.get(flightNo);
        if (ssr == null) {
          ssr = new SSR();
          ssrDetails.put(flightNo, ssr);
        }

        WebElement seatBtn = availableSeats.get(seatCount);
        String seatAttr = seatBtn.getAttribute("class");

        if (StringUtils.containsIgnoreCase(seatAttr, "selected")) {
          PageHandler.javaScriptExecuterClick(driver, seatBtn);
        }

        String seatNo = seatBtn.getText();
        PageHandler.javaScriptExecuterClick(driver, seatBtn);

        Double seatFare =
            NumberUtility.getAmountFromString(countryCode, seatBtn.getAttribute("data-fee"));

        String selectedSeatNo =
            travellersElements.getSelectedSeats(seatDiv).get(seatCount).getText();
        Double selectedFare =
            NumberUtility.getAmountFromString(countryCode,
                travellersElements.getSelectedSeatsFare(seatDiv).get(seatCount).getText());

        CustomAssert.assertTrue(testId, seatFare.equals(selectedFare),
            "Selected seat no didn't match at selection panel.");
        CustomAssert.assertTrue(testId, StringUtils.equals(seatNo, selectedSeatNo),
            "Selected seat Fare didn't match at selection panel.");

        ssr.setSeatNo(seatNo);
        ssr.setSeatPrice(seatFare);

        Log.info(testId, travellerDetails.getName() + " Flight No : " + flightNo + " Seat No : "
            + seatNo + " Fare : " + NumberUtility.getRoundedAmount(countryCode, seatFare));

        totalSeatFare += seatFare;
      }
      WebElement proceedSeatBtn = travellersElements.getProceedWithSeat(seatDiv);
      PageHandler.javaScriptExecuterClick(driver, proceedSeatBtn);
    }

    return totalSeatFare;
  }

  private void setSSRSelection(FlightBookingDetails flightBookingDetails) {
    String ssr = flightBookingDetails.getOnwardSSR();
    setOnewaySSRSelection(ssr);
    ssr = flightBookingDetails.getReturnSSR();
    setOnewaySSRSelection(ssr);
  }

  private void setOnewaySSRSelection(String ssr) {
    if (StringUtils.isNotBlank(ssr)) {
      List<String> ssrList = StringUtilities.split(ssr, Constant.PIPE);
      if (ssrList.size() != 3) {
        CustomAssert.assertFail(testId, "Onward SSR entry is not valid.");
      }
      boolean flag = StringUtils.equalsIgnoreCase(ssrList.get(0), "Y");
      mealBaggageSelect |= flag;
      meal.add(flag);

      flag = StringUtils.equalsIgnoreCase(ssrList.get(1), "Y");
      mealBaggageSelect |= flag;
      baggage.add(flag);

      flag = StringUtils.equalsIgnoreCase(ssrList.get(2), "Y");
      seatSelect |= flag;
      seat.add(flag);

    } else {
      meal.add(false);
      baggage.add(false);
      seat.add(false);
    }
  }

  private List<TravellerDetails> setPassengerDetails(Flight flightType,
      TravellersPageElements travellersElements, String travellerType, int travellersCount,
      Calendar onwardCalendar) {
    Double addOnsPrice = 0.0;
    List<TravellerDetails> travellerDetailsList = new LinkedList<TravellerDetails>();
    for (int pCount = 1; pCount <= travellersCount; pCount++) {
      String traveller = travellerType + pCount;
      TravellerDetails travellerDetails = setPassengerName(travellersElements, traveller);

      setPassengerDOB(travellersElements, traveller, travellerDetails, onwardCalendar);

      if (flightType == Flight.INTERNATIONAL) {
        setPassengerPassportDetails(travellersElements, traveller, travellerDetails, onwardCalendar);
      }

      travellerDetails.setSsrDetails(new HashMap<String, SSR>());
      addOnsPrice += setMealBaggage(travellersElements, traveller, travellerDetails);
      travellerDetails.setType(setTravellerType(travellerType));
      travellerDetailsList.add(travellerDetails);
    }

    Double totalFare = flightBookingDetails.getTotalFare() + addOnsPrice;

    flightBookingDetails.setTotalFare(totalFare);

    return travellerDetailsList;
  }

  private Traveller setTravellerType(String travellerType) {
    if (StringUtils.equals(travellerType, ADULT)) {
      return Traveller.ADULT;
    }
    if (StringUtils.equals(travellerType, CHILD)) {
      return Traveller.CHILD;
    }
    return Traveller.INFANT;
  }

  private Double setMealBaggage(TravellersPageElements travellersElements, String traveller,
      TravellerDetails travellerDetails) {

    Double addOnsPrice = 0.0;

    if (!mealBaggageSelect || StringUtils.containsIgnoreCase(traveller, "Infant")) {
      return addOnsPrice;
    }

    String name = travellerDetails.getName();

    WebElement travellerDiv = travellersElements.getPassengerDiv(traveller);
    WebElement expandMoreOption = travellersElements.getMoreOptions(travellerDiv);

    if (expandMoreOption == null) {
      Log.error(driver, testId, "No option available for meal and baggage addons.");
      return addOnsPrice;
    }

    boolean selected = travellersElements.getHiddenMoreCheckBox(travellerDiv).isSelected();

    if (!selected) {
      PageHandler.javaScriptExecuterClick(driver, expandMoreOption);
    }

    Map<String, SSR> ssrDetails = travellerDetails.getSsrDetails();

    List<WebElement> mealOptions = travellersElements.getMealOptions(travellerDiv);
    List<WebElement> mealSectorList = travellersElements.getMealSectorList(travellerDiv);

    addOnsPrice +=
        selectOneWayMeal(meal.get(0), name, mealSectorList, mealOptions, onwardSectorFlightMap,
            ssrDetails);

    List<WebElement> baggageOptions = travellersElements.getBaggageOptions(travellerDiv);
    List<WebElement> baggageSectorList = travellersElements.getBaggageSectorList(travellerDiv);

    addOnsPrice +=
        selectOneWayBaggage(baggage.get(0), name, baggageSectorList, baggageOptions,
            onwardSectorFlightMap, ssrDetails);

    if (flightBookingDetails.getJourneyType() == Journey.ROUND_TRIP) {
      addOnsPrice +=
          selectOneWayMeal(meal.get(1), name, mealSectorList, mealOptions, returnSectorFlightMap,
              ssrDetails);
      addOnsPrice +=
          selectOneWayBaggage(baggage.get(1), name, baggageSectorList, baggageOptions,
              returnSectorFlightMap, ssrDetails);
    }
    return addOnsPrice;
  }

  private Map<String, String> getSectorFlightMap(List<FlightDetails> flightList) {

    Map<String, String> sectorFlightMap = new HashMap<String, String>();

    if (flightList == null) {
      return sectorFlightMap;
    }

    for (int flightCount = 0; flightCount < flightList.size(); flightCount++) {
      FlightDetails flightDetails = flightList.get(flightCount);
      String sector =
          flightDetails.getSourceCityCode() + "-" + flightDetails.getDestinationCityCode();
      sectorFlightMap.put(sector, flightDetails.getCode());
    }

    return sectorFlightMap;
  }

  private Double selectOneWayMeal(boolean mealFlag, String name, List<WebElement> sectorList,
      List<WebElement> mealOptions, Map<String, String> sectorFlightMap, Map<String, SSR> ssrDetails) {

    Double totalMealPrice = 0.0;

    for (int index = 0; index < sectorList.size(); index++) {
      String sector = sectorList.get(index).getText();
      String flightNo = sectorFlightMap.get(sector);
      if (StringUtils.isNotBlank(flightNo)) {
        SSR ssr = new SSR();
        String meal = null;
        if (mealFlag) {
          meal = PageHandler.selectRandomOption(mealOptions.get(index));
        } else {
          meal = PageHandler.getSelectedOptionText(mealOptions.get(index));
        }

        ssr.setMeal(meal);
        int idx = StringUtils.lastIndexOf(meal, "(");
        Double mealPrice =
            NumberUtility.getAmountFromString(countryCode, StringUtils.substring(meal, idx));
        totalMealPrice += mealPrice;
        ssr.setMealPrice(mealPrice);
        ssrDetails.put(flightNo, ssr);

        Log.info(testId, name + " " + sector + " Meal : " + meal);
      }
    }

    return totalMealPrice;
  }

  private Double selectOneWayBaggage(boolean baggageFlag, String name, List<WebElement> sectorList,
      List<WebElement> baggageOptions, Map<String, String> sectorFlightMap,
      Map<String, SSR> ssrDetails) {

    Double totalBaggagePrice = 0.0;

    for (int index = 0; index < sectorList.size(); index++) {
      String sectorString = sectorList.get(index).getText();

      List<String> sectors = StringUtilities.split(sectorString, Constant.COMMA);

      if (StringUtils.isEmpty(sectorFlightMap.get(sectors.get(0)))) {
        continue;
      }

      String baggage = null;
      if (!baggageFlag) {
        baggage = PageHandler.getSelectedOptionText(baggageOptions.get(index));
      } else {
        baggage = PageHandler.selectRandomOption(baggageOptions.get(index));
      }

      int idx = StringUtils.indexOfIgnoreCase(baggage, "KG");
      Double baggagePrice =
          NumberUtility.getAmountFromString(countryCode, StringUtils.substring(baggage, idx));

      Log.info(testId, name + " " + sectorString + " Baggage : " + baggage);

      for (int secIndex = 0; secIndex < sectors.size(); secIndex++) {
        String sector = sectors.get(secIndex);
        String flightNo = sectorFlightMap.get(sector);

        SSR ssr = ssrDetails.get(flightNo);
        if (ssr == null) {
          ssr = new SSR();
        }
        ssr.setBaggage(baggage);
        ssr.setBaggagePrice(baggagePrice);
        ssrDetails.put(flightNo, ssr);
      }
      totalBaggagePrice += baggagePrice;
    }

    return totalBaggagePrice;
  }

  private void setPassengerPassportDetails(TravellersPageElements TravellersElements,
      String Traveller, TravellerDetails TravellerDetails, Calendar onwardCalendar) {

    PageElement expandButtonElement =
        TravellersElements.modifyPageElementOnce("passportExpand", Traveller);
    WebElement expandButton = TravellersElements.getElementByPageObject(expandButtonElement);
    if (StringUtils.equalsIgnoreCase(expandButton.getAttribute("data-mandatory"), "false")) {
      PageHandler.javaScriptExecuterClick(driver, expandButton);
    }

    String countryName = EntityMap.getCountryName(countryCode);

    Select countrySelect = new Select(TravellersElements.getElementById(Traveller + "PassportNAT"));
    countrySelect.selectByVisibleText(countryName);
    TravellerDetails.setCountry(countryName);

    String passportNo = RandomValues.getRandomAlphaNumericString();
    TravellersElements.getElementById(Traveller + "PassportNUM").sendKeys(passportNo);
    TravellerDetails.setPassportNo(passportNo);

    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.YEAR, 5);

    Select dateSelect = new Select(TravellersElements.getElementById(Traveller + "Pday"));
    dateSelect.selectByValue(new SimpleDateFormat("dd").format(calendar.getTime()));

    Select monthSelect = new Select(TravellersElements.getElementById(Traveller + "Pmonth"));
    monthSelect.selectByValue(new SimpleDateFormat("MM").format(calendar.getTime()));

    Select yearSelect = new Select(TravellersElements.getElementById(Traveller + "Pyear"));
    yearSelect.selectByValue(Integer.toString(calendar.get(Calendar.YEAR)));

    TravellerDetails.setPassportExpDate(calendar);
  }

  // Takes the passenger details randomly and pushes the details in traveler
  // details POJO
  private TravellerDetails setPassengerName(TravellersPageElements TravellersElements,
      String passengerType) {

    TravellerDetails TravellerDetails = new TravellerDetails();
    // Clicks the expand button for target passenger, if not opened
    if (!StringUtils.equals(passengerType, "adult1")) {
      PageElement expandButtonElement =
          TravellersElements.modifyPageElementOnce("passengerExpand", passengerType);
      WebElement expandButton = TravellersElements.getElementByPageObject(expandButtonElement);
      PageHandler.javaScriptExecuterClick(driver, expandButton);
    }

    WebElement titleHeader = TravellersElements.getElementById(passengerType + "Title");
    WebElement firstNameElement = TravellersElements.getElementById(passengerType + "FirstName");
    WebElement surnameElement = TravellersElements.getElementById(passengerType + "Surname");

    WebElement randomTitle = TravellersElements.getRandomOption(titleHeader);
    String title = randomTitle.getText();
    TravellerDetails.setTitle(title);
    randomTitle.click();

    List<String> name =
        StringUtilities.split(RandomValues.getRandomName(countryCode), Constant.WHITESPACE);
    String firstName = name.get(0);
    TravellerDetails.setFirstName(firstName);
    firstNameElement.sendKeys(firstName);

    String surName = name.get(1);
    TravellerDetails.setSurName(surName);
    surnameElement.sendKeys(surName);

    return TravellerDetails;
  }

  private void setPassengerDOB(TravellersPageElements travellersElements, String passengerType,
      TravellerDetails travellerDetails, Calendar onwardCalendar) {

    WebElement expandBtn =
        travellersElements.findElement(travellersElements.modifyPageElementOnce("expandTab",
            passengerType));

    if (StringUtils.equalsIgnoreCase(expandBtn.getAttribute("data-collapseval"), "1")) {
      PageHandler.javaScriptExecuterClick(driver, expandBtn);
    }

    if (StringUtils.equals(travellersElements.getDobDiv(passengerType).getAttribute("style"),
        "display: none;")) {
      return;
    }

    Calendar calendar = CalendarUtils.getRandomDOB_OLD(passengerType, onwardCalendar);

    Select dateSelect = new Select(travellersElements.getElementById(passengerType + "DOBday"));
    dateSelect.selectByValue(new SimpleDateFormat("dd").format(calendar.getTime()));

    Select monthSelect = new Select(travellersElements.getElementById(passengerType + "DOBmonth"));
    monthSelect.selectByValue(new SimpleDateFormat("MM").format(calendar.getTime()));

    Select yearSelect = new Select(travellersElements.getElementById(passengerType + "DOByear"));
    yearSelect.selectByValue(Integer.toString(calendar.get(Calendar.YEAR)));

    travellerDetails.setBirthCalender(calendar);
  }

  private void setContactDetails(TravellersPageElements TravellersElements) {
    String testId = flightBookingDetails.getTestCaseId();

    WebElement isdCodeHeader = TravellersElements.getISDCodeHeader();
    CustomAssert.assertTrue(testId, isdCodeHeader != null, "Error in finding ISD Header");
    Select isdSelect = new Select(isdCodeHeader);
    isdSelect.selectByVisibleText(ISD_CODE);

    WebElement contactMobile = TravellersElements.getContactMobile();
    CustomAssert
        .assertTrue(testId, contactMobile != null, "Error in finding mobile Number element");
    contactMobile.sendKeys(CONTACT_MOBILE);
    flightBookingDetails.setMobileNumber(CONTACT_MOBILE_COMPLETE);

    WebElement contactEmail = TravellersElements.getContactEmail();
    CustomAssert.assertTrue(testId, contactEmail != null, "Error in finding email element");
    contactEmail.sendKeys(CONTACT_EMAIL);
    flightBookingDetails.setEMail(CONTACT_EMAIL);

  }

  private void setTaxExempt() {
    try {
      WebElement taxExemptElement = driver.findElement(By.id("phTaxNo"));
      PageHandler.javaScriptExecuterClick(driver, taxExemptElement);
      Log.info(testId, "Tax Exempt :: No");
    } catch (Exception e) {
    }
}

  private void setInsurace(TravellersPageElements travellersElements,
      List<TravellerDetails> travellerDetailsList) {
    boolean insurance = flightBookingDetails.isInsurance();

    WebElement noIns = null;
    WebElement yesIns = null;
    try {
      noIns = driver.findElement(By.xpath("//input[contains(@id, 'noInsr')]"));
    } catch (Exception e) {
    }

    try {
      yesIns = driver.findElement(By.xpath("//input[contains(@id, 'yesInsr')]"));
    } catch (Exception e) {
    }

    if (insurance && noIns == null && yesIns == null) {
      Log.error(driver, testId, "Insurance not available.");
      return;
    }

    if (!insurance) {
      if (noIns != null && !noIns.isSelected()) {
        PageHandler.javaScriptExecuterClick(driver, noIns);
      }

      else if (yesIns != null && yesIns.isSelected()) {
        PageHandler.javaScriptExecuterClick(driver, yesIns);
      }

      return;
    }

    if (!yesIns.isSelected()) {
      PageHandler.javaScriptExecuterClick(driver, yesIns);
    }

    Traveller type = Traveller.ADULT;
    int index = 1;

    Calendar onwardCalendar = flightBookingDetails.getOnwardDate();

    for (TravellerDetails travellerDetails : travellerDetailsList) {
      Traveller actualType = travellerDetails.getType();
      if (!type.equals(actualType)) {
        index = 1;
        type = actualType;
      }

      String passengerType = StringUtils.lowerCase(type.toString()) + index;
      index++;

      setPassengerDOB(travellersElements, passengerType, travellerDetails, onwardCalendar);
    }

    WebElement insTermsCondition =
        driver.findElement(By.xpath(".//input[@name='agree_insurance_terms']"));

    if (!insTermsCondition.isSelected()) {
      PageHandler.javaScriptExecuterClick(driver, insTermsCondition);
    }
  }

  private void acceptVisaInfo() {
    try {
      WebElement visaInfoRadio = driver.findElement(By.id("visaInfoYes_label"));
      PageHandler.javaScriptExecuterClick(driver, visaInfoRadio);
    } catch (Exception e) {

    }
 }
}
