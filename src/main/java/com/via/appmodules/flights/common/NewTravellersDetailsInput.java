package com.via.appmodules.flights.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

import ch.qos.logback.core.joran.spi.ConsoleTarget;

import com.via.pageobjects.flights.common.FlightDetails;
import com.via.pageobjects.flights.common.HeaderElements;
import com.via.pageobjects.flights.common.FlightBookingDetails;
import com.via.pageobjects.flights.common.NewTravellersPageElements;
import com.via.pageobjects.flights.common.SSR;
import com.via.pageobjects.flights.common.TravellerDetails;
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

public class NewTravellersDetailsInput {

  private static String testId;
  private VIA_COUNTRY countryCode;
  private WebDriver driver;
  private RepositoryParser repositoryParser;
  private FlightBookingDetails flightBookingDetails;

  private final String ISD_CODE = "91";
  private final String CONTACT_MOBILE = "9611577993";
  private final String CONTACT_MOBILE_COMPLETE = "+91-9611577993";
  private final String CONTACT_EMAIL = "qa@via.com";

  private final String ADULT = "adt";
  private final String CHILD = "chd";
  private final String NEW_Traveller_HEADER_PAGE_NAME = "newTravellersHeader";

  boolean baggageSelect = false;
  boolean mealSelect = false;
  boolean seatSelect = false;
  boolean insuranceSelect = false;

  private List<Boolean> meal = new ArrayList<Boolean>();
  private List<Boolean> baggage = new ArrayList<Boolean>();
  private List<Boolean> seat = new ArrayList<Boolean>();
  private Map<String, String> onwardSectorFlightMap;
  private Map<String, String> returnSectorFlightMap;

  private Map<String, SSR> ssrDetails = null;

  public NewTravellersDetailsInput(String testId, VIA_COUNTRY countryCode, WebDriver driver,
      RepositoryParser repositoryParser, FlightBookingDetails flightBookingDetails) {
    this.testId = testId;
    this.driver = driver;
    this.countryCode = countryCode;
    this.repositoryParser = repositoryParser;
    this.flightBookingDetails = flightBookingDetails;
    this.onwardSectorFlightMap = getSectorFlightMap(flightBookingDetails.getOnwardFlights());
    this.returnSectorFlightMap = getSectorFlightMap(flightBookingDetails.getReturnFlights());

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

  public List<TravellerDetails> newExecute(Flight flightType,
      NewTravellersPageElements travellersElements) {

    Calendar onwardCalendar = flightBookingDetails.getOnwardDate();

    RandomValues.setUsedNameList();

    // Setting SSR selection as per excel sheet.
    setSSRSelection(flightBookingDetails);

    // Setting Contacts Details.
    setContactDetails(travellersElements);

    int travellersCount =
        flightBookingDetails.getAdultsCount() + flightBookingDetails.getChildrenCount()
            + flightBookingDetails.getInfantsCount();

    // Initialize TravellerDetails.
    List<TravellerDetails> travellersDetailsList =
        initializeTravellerDetails(travellersCount, flightType, onwardCalendar);

    // Initialize SSR Details.
    initializeSSRDetails(travellersDetailsList);

    // Selecting AddOns and Seats.
    selectAddonsAndSeats(travellersElements, travellersDetailsList);

    PageHandler.javaScriptExecuterClick(driver, travellersElements.step1Proceed());

    // Setting Travelers details.
    setPassengerDetails(travellersDetailsList, flightType, travellersElements, travellersCount,
        onwardCalendar);

    // Added as , if elements will not require for this page, then those elements will not be available. 
    // No need to wait for 10 seconds .
    PageHandler.setImplicitWaitTime(driver, 2 * 1000);

    if (countryCode != Constant.VIA_COUNTRY.IN_CORP) {
      try {
        travellersElements.step2Proceed().click();
      } catch (Exception e) {
      }
    }

    double updatedFare = updateDynamicFareChanges(countryCode, driver);
    if (updatedFare != -1) {
      flightBookingDetails.setTotalFare(updatedFare);
    }

    // Handling Insurance PopUp.
    handleInsurancePopup(travellersElements);

    setTaxExempt();

    // Accepting Visa Terms & Conditions.If it is necessary.
    acceptVisaInfo();

    // Set to previous state.
    PageHandler.setImplicitWaitTime(driver, 10 * 1000);

    // Accepting Payment Terms & Conditions.
    WebElement readTermsLabel = travellersElements.getReadTermsLabel();
    // PageHandler.javaScriptExecuterClick(driver, readTermsLabel);
    readTermsLabel.click();

    PageHandler.sleep(testId, 1 * 1000L);

    // CLicking Final Payment Button.
    WebElement makePayment = travellersElements.makePayment();
    // PageHandler.javaScriptExecuterClick(driver, makePayment);
    makePayment.click();

    return travellersDetailsList;
  }

  private void verifyFare() {
    Double totalFare = flightBookingDetails.getTotalFare();

    Log.info(
        testId,
        "Total Fare after addons Selection : "
            + NumberUtility.getRoundedAmount(countryCode, totalFare));

    Double totalFareAtHeader =
        NumberUtility.getAmountFromString(countryCode, new HeaderElements(testId, driver,
            repositoryParser).getTotalFare(NEW_Traveller_HEADER_PAGE_NAME).getText());

    Double diff = Math.abs(totalFareAtHeader - totalFare);

    CustomAssert.assertTrue(
        testId,
        diff < 0.5,
        "Total fare at Traveller Header not verified. Expected : "
            + NumberUtility.getRoundedAmount(countryCode, totalFare) + " Actual : "
            + NumberUtility.getRoundedAmount(countryCode, totalFareAtHeader));

  }

  private List<TravellerDetails> initializeTravellerDetails(int travellersCount, Flight flightType,
      Calendar onwardCalendar) {
    List<TravellerDetails> travellerDetailsList = new LinkedList<TravellerDetails>();
    for (int pCount = 0; pCount < travellersCount; pCount++) {
      String pIndex = Integer.toString(pCount);

      TravellerDetails travellerDetails = new TravellerDetails();
      if (pCount < flightBookingDetails.getAdultsCount()) {
        travellerDetails.setType(Traveller.ADULT);
      } else if (pCount >= flightBookingDetails.getAdultsCount()
          && pCount < travellersCount - flightBookingDetails.getInfantsCount()) {
        travellerDetails.setType(Traveller.CHILD);
      } else {
        travellerDetails.setType(Traveller.INFANT);
      }

      travellerDetails = initializePassengerName(travellerDetails);

      initializePassengerDOB(flightType, travellerDetails, onwardCalendar, pIndex);

      // In Some Country when we select insurance then We'll have to fill
      // the passport details.
      // Coming byDefault for selected Airlines for Singapore MH,OD,CA,
      // CI, MU, CZ, MF.
      if ((flightType == Flight.INTERNATIONAL && insuranceSelect)
          || (flightType == Flight.INTERNATIONAL && (countryCode == Constant.VIA_COUNTRY.ID
              || countryCode == Constant.VIA_COUNTRY.TH || countryCode == Constant.VIA_COUNTRY.PH
              || countryCode == Constant.VIA_COUNTRY.HK || countryCode == Constant.VIA_COUNTRY.SA
              || countryCode == Constant.VIA_COUNTRY.UAE || countryCode == Constant.VIA_COUNTRY.OM || countryCode == Constant.VIA_COUNTRY.IN_CORP))
          || countryCode == Constant.VIA_COUNTRY.UAE
          && flightBookingDetails.getOnwardFlightName().contains("OmanAir")
          || countryCode == Constant.VIA_COUNTRY.SA) {
        initializePassengerPassportDetails(travellerDetails, onwardCalendar, pIndex);
      }
      travellerDetailsList.add(travellerDetails);
    }
    return travellerDetailsList;
  }

  private static double updateDynamicFareChanges(VIA_COUNTRY countryCode, WebDriver driver) {
    try {
      PageHandler.setImplicitWaitTime(driver, 1 * 1000);
      WebElement repriceContinueButton = driver.findElement(By.id("modalConfirmCTA"));
      WebElement amountElement = driver.findElement(By.className("alertHead"));
      List<String> alertMessage = Arrays.asList(amountElement.getText().split(Constant.WHITESPACE));
      String totalFareString = alertMessage.get(6).substring(0, alertMessage.get(6).length() - 1);
      double totalFare = NumberUtility.getAmountFromString(countryCode, totalFareString);
      repriceContinueButton.click();
      return totalFare;
    } catch (Exception e) {
      return -1;
    }
  }

  private void handleInsurancePopup(NewTravellersPageElements travellersElements) {
    // proceedWithInsurance(travellersElements);
    proceedWithoutInsurance(travellersElements);

  }

  // For the passenger data entered insurance price is S$ 20 and insurance
  // shall only be valid for
  // Traveler1, Traveler2.Insurance is applicable only for those, having
  // source and passport country
  // both are same.

  // private void proceedWithInsurance(NewTravellersPageElements
  // travellersElements) {
  // if (insuranceSelect) {
  // try {
  // PageHandler.sleep(testId, 5 * 1000L);
  // WebElement notifyAlert = driver.findElement(By.className("alertHead"));
  // if (notifyAlert.isDisplayed()) {
  // Log.info(testId, notifyAlert.getText());
  // driver.findElement(By.id("modalConfirmCTA")).click();
  // }
  // } catch (Exception e) {
  // }
  // }
  // }

  // Insurance rules were not applied, proceeding without insurance.(In rare
  // case.)
  private void proceedWithoutInsurance(NewTravellersPageElements travellersElements) {
    if (insuranceSelect) {
      try {
        // PageHandler.sleep(testId, 5 * 1000L);
        WebElement notifyAlert = driver.findElement(By.className("alertHead"));
        if (notifyAlert.isDisplayed()) {
          Log.info(testId, notifyAlert.getText());
          driver.findElement(By.id("modalAlertCTA")).click();
        }
      } catch (Exception e) {
      }
    }
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
        CustomAssert.assertFail(testId, "SSR entry is not valid.");
      }
      boolean flag = StringUtils.equalsIgnoreCase(ssrList.get(0), "Y");
      mealSelect |= flag;
      meal.add(flag);

      flag = StringUtils.equalsIgnoreCase(ssrList.get(1), "Y");
      baggageSelect |= flag;
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

  private void setContactDetails(NewTravellersPageElements travellersElements) {
    String testId = flightBookingDetails.getTestCaseId();

    WebElement isdCodeHeader = travellersElements.getISDCodeHeader();
    CustomAssert.assertTrue(testId, isdCodeHeader != null, "Error in finding ISD Header");
    // isdCodeHeader.clear();
    // isdCodeHeader.sendKeys(ISD_CODE);
    Select isdSelector = new Select(isdCodeHeader);
    isdSelector.selectByValue(ISD_CODE);

    WebElement contactMobile = travellersElements.getContactMobile();
    CustomAssert
        .assertTrue(testId, contactMobile != null, "Error in finding mobile Number element");
    contactMobile.clear();
    contactMobile.sendKeys(CONTACT_MOBILE);
    flightBookingDetails.setMobileNumber(CONTACT_MOBILE_COMPLETE);

    WebElement contactEmail = travellersElements.getContactEmail();
    CustomAssert.assertTrue(testId, contactEmail != null, "Error in finding email element");
    contactEmail.clear();
    contactEmail.sendKeys(CONTACT_EMAIL);
    flightBookingDetails.setEMail(CONTACT_EMAIL);

    // Apply promoCode if any.
    applyPromoCode(travellersElements);

    // Apply Insurance as per excel sheet.
    insuranceSelect = setInsurace(travellersElements);

  }

  private void applyPromoCode(NewTravellersPageElements travellersElements) {
    String testId = flightBookingDetails.getTestCaseId();
    String promoCode = flightBookingDetails.getPromoCode();
    if (promoCode != null) {
      travellersElements.getVoucherElement().sendKeys(promoCode);
      travellersElements.voucherValidate().click();
      double totalFare =
          NumberUtility.getAmountFromString(countryCode, travellersElements.totalFare().getText());
      flightBookingDetails.setTotalFare(totalFare);
      if (travellersElements.alertMessage().isDisplayed()) {
        Log.info(testId, "Wrong promo Code is applied");
        return;
      }

      // Double totalAmountAfterVoucher =
      // NumberUtility.getAmountFromString(countryCode, new
      // HeaderElements(testId, driver,
      // repositoryParser).getTotalFare(NEW_Traveller_HEADER_PAGE_NAME).getText());
      // flightBookingDetails.setTotalFare(totalAmountAfterVoucher);
      // Log.info(
      // testId,
      // "Total Amount after insurance : "
      // + NumberUtility.getRoundedAmount(countryCode,
      // totalAmountAfterVoucher));
      Log.info(testId, "Promo Code is successfully applied");
    }
  }

  private boolean setInsurace(NewTravellersPageElements travellersElements) {
    boolean insuranceSelect = false;
    WebElement insTermsCondition;
    try {
      insTermsCondition = travellersElements.insTermsCondition();
      // If We are passing insurance from excel sheet.
      if (flightBookingDetails.isInsurance()) {
        // Checking whether insurance is selected already or not.
        if (!insTermsCondition.isSelected()) {
          PageHandler.javaScriptExecuterClick(driver, insTermsCondition);
        }
        PageHandler.sleep(testId, 1000L);
        Double totalAmountAfterIns =
            NumberUtility.getAmountFromString(countryCode, new HeaderElements(testId, driver,
                repositoryParser).getTotalFare(NEW_Traveller_HEADER_PAGE_NAME).getText());
        flightBookingDetails.setTotalFare(totalAmountAfterIns);
        Log.info(
            testId,
            "Total Amount after insurance : "
                + NumberUtility.getRoundedAmount(countryCode, totalAmountAfterIns));
        insuranceSelect = true;
      } else {
        if (insTermsCondition.isSelected()) {
          PageHandler.javaScriptExecuterClick(driver, insTermsCondition);
          PageHandler.sleep(testId, 1000L);
          Double totalAmountWithoutIns =
              NumberUtility.getAmountFromString(countryCode, new HeaderElements(testId, driver,
                  repositoryParser).getTotalFare(NEW_Traveller_HEADER_PAGE_NAME).getText());
          flightBookingDetails.setTotalFare(totalAmountWithoutIns);
          Log.info(
              testId,
              "Total Amount Without insurance : "
                  + NumberUtility.getRoundedAmount(countryCode, totalAmountWithoutIns));
          insuranceSelect = false;
        }
      }
    } catch (Exception e) {
      Log.info(testId, "Insurance is not available");
      insuranceSelect = false;
    }
    return insuranceSelect;
  }

  private void setPassengerDetails(List<TravellerDetails> travellersDetailsList, Flight flightType,
      NewTravellersPageElements travellersElements, int travellersCount, Calendar onwardCalendar) {
    for (int pCount = 0; pCount < travellersCount; pCount++) {
      String pIndex = Integer.toString(pCount);
      String traveller = travellersElements.getPassengerType(pIndex);

      TravellerDetails travellerDetails = travellersDetailsList.get(pCount);
      setPassengerName(travellerDetails, travellersElements, pIndex);

      setPassengerDOB(flightType, travellersElements, travellerDetails, onwardCalendar, pIndex);

      // In Some Country when we select insurance then We'll have to fill
      // the passport details.
      // Coming byDefault for selected Airlines for Singapore MH,OD,CA,
      // CI, MU, CZ, MF.
      if ((flightType == Flight.INTERNATIONAL && insuranceSelect)
          || (flightType == Flight.INTERNATIONAL && (countryCode == Constant.VIA_COUNTRY.ID
              || countryCode == Constant.VIA_COUNTRY.TH || countryCode == Constant.VIA_COUNTRY.PH
              || countryCode == Constant.VIA_COUNTRY.HK || countryCode == Constant.VIA_COUNTRY.SA
              || countryCode == Constant.VIA_COUNTRY.UAE || countryCode == Constant.VIA_COUNTRY.OM || countryCode == Constant.VIA_COUNTRY.IN_CORP))
          || countryCode == Constant.VIA_COUNTRY.UAE
          && flightBookingDetails.getOnwardFlightName().contains("OmanAir")
          || countryCode == Constant.VIA_COUNTRY.SA) {
        try {
          setPassengerPassportDetails(travellersElements, traveller, travellerDetails,
              onwardCalendar, pIndex);
        } catch (Exception e) {
        }
      }
    }
  }

  private void setPassengerName(TravellerDetails travellerDetails,
      NewTravellersPageElements TravellersElements, String pIndex) {

    WebElement titleHeader = TravellersElements.getTitle(pIndex);
    WebElement firstNameElement = TravellersElements.getFirstName(pIndex);
    WebElement surnameElement = TravellersElements.getSirname(pIndex);

    // WebElement randomTitle =
    // TravellersElements.getRandomOption(titleHeader);
    List<WebElement> titles = TravellersElements.getOptions(titleHeader);
    for (WebElement title : titles) {
      if (title.getText().equals(travellerDetails.getTitle())) {
        title.click();
      }
    }

    firstNameElement.sendKeys(travellerDetails.getFirstName());

    surnameElement.sendKeys(travellerDetails.getSurName());
  }

  private TravellerDetails initializePassengerName(TravellerDetails travellerDetails) {
    String title = RandomValues.getRandomTitle(travellerDetails);
    travellerDetails.setTitle(title);
    List<String> name =
        StringUtilities.split(RandomValues.getRandomName(countryCode), Constant.WHITESPACE);
    String firstName = name.get(0);
    travellerDetails.setFirstName(firstName);
    String surName = name.get(1);
    travellerDetails.setSurName(surName);
    return travellerDetails;
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

  private void initializePassengerDOB(Flight flightType, TravellerDetails travellerDetails,
      Calendar onwardCalendar, String pIndex) {
    Traveller passengerType = travellerDetails.getType();

    // DOB is not require if Sector is Domestic and Traveler type is adult
    // and insurance is not
    // selected.
    if ((flightType == Flight.DOMESTIC && !insuranceSelect && passengerType == Traveller.ADULT
        && countryCode != VIA_COUNTRY.SA && flightBookingDetails.getChildrenCount() == 0 && !flightBookingDetails
        .getOnwardFlightName().contains("AirAsia"))
        || (countryCode == VIA_COUNTRY.ID && flightType == Flight.DOMESTIC && !insuranceSelect
            && passengerType == Traveller.ADULT && !flightBookingDetails.getOnwardFlightName()
            .contains("AirAsia"))) {
      return;
    }
    // Otherwise DOB is requires in any case .
    Calendar calendar = CalendarUtils.initializeRandomDOB(passengerType, onwardCalendar);
    travellerDetails.setBirthCalender(calendar);
  }

  private void setPassengerDOB(Flight flightType, NewTravellersPageElements travellersElements,
      TravellerDetails travellerDetails, Calendar onwardCalendar, String pIndex) {
    String passengerType = travellersElements.getPassengerType(pIndex);

    // DOB is not require if Sector is Domestic and Traveler type is adult
    // and insurance is not
    // selected.
    if ((flightType == Flight.DOMESTIC && !insuranceSelect
        && StringUtils.equals(passengerType, "adt") && countryCode != VIA_COUNTRY.SA
        && flightBookingDetails.getChildrenCount() == 0 && !flightBookingDetails
        .getOnwardFlightName().contains("AirAsia"))
        || (countryCode == VIA_COUNTRY.ID && flightType == Flight.DOMESTIC && !insuranceSelect
            && StringUtils.equals(passengerType, "adt") && !flightBookingDetails
            .getOnwardFlightName().contains("AirAsia"))) {
      return;
    }

    // Otherwise DOB is requires in any case .
    Calendar calendar = travellerDetails.getBirthCalender();

    Select dateSelect = new Select(travellersElements.getDate(pIndex));
    dateSelect.selectByValue(new SimpleDateFormat("dd").format(calendar.getTime()));

    Select monthSelect = new Select(travellersElements.getMonth(pIndex));
    monthSelect.selectByValue(new SimpleDateFormat("MM").format(calendar.getTime()));

    Select yearSelect = new Select(travellersElements.getYear(pIndex));
    yearSelect.selectByValue(Integer.toString(calendar.get(Calendar.YEAR)));
  }

  private void initializePassengerPassportDetails(TravellerDetails travellerDetails,
      Calendar onwardCalendar, String pIndex) {
    String countryName = EntityMap.getCountryName(countryCode);
    travellerDetails.setCountry(countryName);

    String passportNo = RandomValues.getRandomAlphaNumericString();
    travellerDetails.setPassportNo(passportNo);

    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.YEAR, 5);

    travellerDetails.setPassportExpDate(calendar);
  }

  private void setPassengerPassportDetails(NewTravellersPageElements travellersElements,
      String Traveller, TravellerDetails travellerDetails, Calendar onwardCalendar, String pIndex) {
    String countryName = travellerDetails.getCountry();
    WebElement nationalty = travellersElements.passportNationalty(pIndex);
    nationalty.clear();
    nationalty.sendKeys(countryName);

    WebElement nationaltyAutoComplete =
        travellersElements.nationaltyAutoComplete(pIndex, countryName);
    nationaltyAutoComplete.click();

    String passportNo = travellerDetails.getPassportNo();
    travellersElements.passportNumber(pIndex).sendKeys(passportNo);

    Calendar calendar = travellerDetails.getPassportExpDate();

    Select dateSelect = new Select(travellersElements.pExpiryDate(pIndex));
    dateSelect.selectByValue(new SimpleDateFormat("dd").format(calendar.getTime()));

    Select monthSelect = new Select(travellersElements.pExpiryMonth(pIndex));
    monthSelect.selectByValue(new SimpleDateFormat("MM").format(calendar.getTime()));

    Select yearSelect = new Select(travellersElements.pExpiryYear(pIndex));
    yearSelect.selectByValue(Integer.toString(calendar.get(Calendar.YEAR)));

  }

  private void selectAddonsAndSeats(NewTravellersPageElements travellersElements,
      List<TravellerDetails> travellersDetailsList) {
    // Traveler count eligible for addons selection
    int travelerCount =
        flightBookingDetails.getAdultsCount() + flightBookingDetails.getChildrenCount();
    try {
      if (StringUtils.equals(driver.findElement(By.id("addons-btn")).getText(),
          "Sorry, No Add-on information could be retrieved.")) {
        Log.info(testId, "Sorry, No Add-on information could be retrieved.");
        return;
      }
      if (!driver.findElement(By.xpath(".//*[@id='addon-summary']")).isDisplayed()) {
        travellersElements.selectAddons().click();
      }
      if (mealSelect) {
        setMeal(travellersElements, travellersDetailsList, travelerCount);
        PageHandler.sleep(testId, 1 * 1000L);
      }
      if (baggageSelect) {
        setBaggage(travellersElements, travellersDetailsList, travelerCount);
        PageHandler.sleep(testId, 1 * 1000L);
      }
      if (seatSelect) {
        seatSelection(travellersElements, travellersDetailsList, travelerCount);
        PageHandler.sleep(testId, 1 * 1000L);
      }
      verifyFare();
    } catch (Exception e) {
      Log.info(testId, "Sorry No Addons found");
    }
  }

  private void initializeSSRDetails(List<TravellerDetails> travellersDetailsList) {
    for (int index = 0; index < travellersDetailsList.size(); index++) {
      travellersDetailsList.get(index).setSsrDetails(new HashMap<String, SSR>());
    }
  }

  private void seatSelection(NewTravellersPageElements travellersElements,
      List<TravellerDetails> travellerDetailsList, int travelerCount) {

    String onwardSection = Integer.toString(1);
    String returnSection = Integer.toString(2);
    Double totalFare = flightBookingDetails.getTotalFare();
    try {
      WebElement seatButton = travellersElements.seat();
      PageHandler.javaScriptExecuterClick(driver, seatButton);

      if (seat.get(0)) {
        totalFare +=
            setSeatDetails(onwardSectorFlightMap, travellersElements, onwardSection,
                travellerDetailsList, travelerCount);
        PageHandler.sleep(testId, 1 * 1000L);
      }
      if (flightBookingDetails.getJourneyType() == Journey.ROUND_TRIP && seat.get(1)) {
        totalFare +=
            setSeatDetails(returnSectorFlightMap, travellersElements, returnSection,
                travellerDetailsList, travelerCount);
        PageHandler.sleep(testId, 1 * 1000L);
      }
      travellersElements.addonContinue().click();
    } catch (Exception e) {
      Log.info(testId, "No Seats available");
    }
    flightBookingDetails.setTotalFare(totalFare);
  }

  private Double setSeatDetails(Map<String, String> sectorFlightMap,
      NewTravellersPageElements travellersElements, String section,
      List<TravellerDetails> travellerDetailsList, int travelerCount) {

    Double totalSeatFare = 0.0;
    for (Entry<String, String> entry : sectorFlightMap.entrySet()) {
      String sector = entry.getKey();
      String flightNo = entry.getValue();
      String[] toModify = new String[2];

      WebElement seatMapBtn = travellersElements.flightSeatBtn(sector, section);
      PageHandler.javaScriptExecuterClick(driver, seatMapBtn);

      // Kishor-10*1000
      PageHandler.sleep(testId, 2 * 1000L);

      WebElement seatDiv = travellersElements.getFlightSeatDiv(sector, countryCode);
      List<WebElement> availableSeats = travellersElements.getAvailableSeats(seatDiv);

      for (int seatCount = 0; seatCount < travelerCount && seatCount < availableSeats.size(); seatCount++) {
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

        toModify[0] = StringUtils.replace(sector, "-", "_");
        toModify[1] = Integer.toString(seatCount);

        String selectedSeatNo = travellersElements.selectedSeat(countryCode, toModify).getText();

        Double selectedFare =
            NumberUtility.getAmountFromString(countryCode,
                travellersElements.getSelectedSeatFare(countryCode, toModify).getText());

        CustomAssert.assertTrue(testId, StringUtils.equals(seatNo, selectedSeatNo),
            "Selected seat Fare didn't match at selection panel.");

        ssr.setSeatNo(seatNo);
        ssr.setSeatPrice(selectedFare);

        Log.info(testId, travellerDetails.getName() + " Flight No : " + flightNo + " Seat No : "
            + seatNo + " Fare : " + NumberUtility.getRoundedAmount(countryCode, selectedFare));

        totalSeatFare += selectedFare;
      }
      WebElement proceedSeatBtn = travellersElements.getProceedWithSeat(seatDiv);
      PageHandler.javaScriptExecuterClick(driver, proceedSeatBtn);
      PageHandler.sleep(testId, 2 * 1000L);
    }

    return totalSeatFare;
  }

  private void setBaggage(NewTravellersPageElements travellersElements,
      List<TravellerDetails> travellerDetailsList, int travelerCount) {

    Double totalFare = flightBookingDetails.getTotalFare();
    String onwardSection = Integer.toString(1);
    String returnSection = Integer.toString(2);
    try {
      PageHandler.javaScriptExecuterClick(driver, travellersElements.baggage());
      totalFare +=
          selectOneWayBaggage(baggage.get(0), travellersElements, onwardSection,
              onwardSectorFlightMap, travellerDetailsList, travelerCount);

      if (flightBookingDetails.getJourneyType() == Journey.ROUND_TRIP) {
        totalFare +=
            selectOneWayBaggage(baggage.get(1), travellersElements, returnSection,
                returnSectorFlightMap, travellerDetailsList, travelerCount);
      }
      travellersElements.addonContinue().click();
    } catch (Exception e) {
      Log.info(testId, "No Baggage available");;
    }
    flightBookingDetails.setTotalFare(totalFare);
  }

  private void setMeal(NewTravellersPageElements travellersElements,
      List<TravellerDetails> travellerDetailsList, int travelerCount) {

    Double totalFare = flightBookingDetails.getTotalFare();
    String onwardSection = Integer.toString(1);
    String returnSection = Integer.toString(2);
    try {
      PageHandler.javaScriptExecuterClick(driver, travellersElements.meal());
      totalFare +=
          selectOneWayMeal(meal.get(0), travellersElements, onwardSection, onwardSectorFlightMap,
              travellerDetailsList, travelerCount);

      if (flightBookingDetails.getJourneyType() == Journey.ROUND_TRIP) {
        totalFare +=
            selectOneWayMeal(meal.get(1), travellersElements, returnSection, returnSectorFlightMap,
                travellerDetailsList, travelerCount);
      }
      travellersElements.addonContinue().click();
    } catch (Exception e) {
      Log.info(testId, "No Meal available");
    }
    flightBookingDetails.setTotalFare(totalFare);
  }

  private Double selectOneWayMeal(boolean mealFlag, NewTravellersPageElements travellersElements,
      String section, Map<String, String> sectorFlightMap,
      List<TravellerDetails> travellerDetailsList, int travelerCount) {

    Double totalMealPrice = 0.0;
    String name = null;
    int travelersIndex = 0;
    List<WebElement> mealSectors = null;
    List<WebElement> mealOptions = null;

    if (section.equals("1")) {
      mealSectors = travellersElements.onwardMealSectors();
      mealOptions = travellersElements.onwardMealOptions();
    } else {
      mealSectors = travellersElements.returnMealSectors();
      mealOptions = travellersElements.returnMealOptions();
    }

    // For Connected flights.
    /*
     * If meal options are available for Connected flights Then We will select option from top to
     * bottom,firstly we'll select onward/return option for first traveler(For all connected
     * flights), and then for 2nd and then for 3rd and so on..
     * 
     * For example if two connected flights are present and there are 4 travelers Then, Traveler0 =
     * meal0; Traveler0 = meal1; Traveler1 = meal2; Traveler1 = meal3; Traveler2 = meal4; Traveler2
     * = meal5; Traveler3 = meal6; Traveler3 = meal7;
     * 
     * As we need same travelersIndex for even, and odd position for available baggage options.
     * That's why, for even option we are using travelersIndex and increasing travelersIndex, and
     * for odd option we are decreasing travelersIndex and then increasing travelersIndex.
     */
    for (int index = 0; index < mealOptions.size(); index++) {
      // For Connected flights.(we'll use travelersIndex and then will
      // increase it)
      if (index % 2 == 0 && mealOptions.size() != travelerCount) {
        ssrDetails = travellerDetailsList.get(travelersIndex).getSsrDetails();
        name = travellerDetailsList.get(travelersIndex++).getName();
      }
      // For Connected flights.(Firstly we'll decrease travelersIndex and
      // then will increase it)
      else if (index % 2 != 0 && mealOptions.size() != travelerCount) {
        ssrDetails = travellerDetailsList.get(--travelersIndex).getSsrDetails();
        name = travellerDetailsList.get(travelersIndex++).getName();
      } else {
        ssrDetails = travellerDetailsList.get(index).getSsrDetails();
        name = travellerDetailsList.get(index).getName();
      }

      String meal = null;
      if (mealFlag) {
        meal = PageHandler.selectRandomOption(mealOptions.get(index));
      } else {
        meal = PageHandler.getSelectedOptionText(mealOptions.get(index));
      }
      int idx = StringUtils.lastIndexOf(meal, "(");
      Double mealPrice =
          NumberUtility.getAmountFromString(countryCode, StringUtils.substring(meal, idx));

      // Getting SectorString as "DXB - MAA"
      String sectorString = mealSectors.get(index).getText().replace(" ", "");
      List<String> sectorList = null;

      /*
       * If available baggage is same for all available connecting (Multiple) sector as
       * "DXB - MAA, MAA - BOM" Then Store each sector in List of string and set SSR for each
       * sector.
       */
      if (StringUtils.contains(sectorString, ",")) {
        sectorList = Arrays.asList(sectorString.split(","));
      }
      String flightNo = null;

      // If Sector List is not null,Then set SSR for each sector.
      if (sectorList != null) {
        for (String sector : sectorList) {
          if (StringUtils.isEmpty(sectorFlightMap.get(sector))) {
            continue;
          }
          flightNo = sectorFlightMap.get(sector);
          setMealAddons(flightNo, meal, mealPrice);
          Log.info(testId, name + " " + sector + " Meal : " + meal);
        }
      }// Otherwise set SSR for available (single) sector.
      else {
        if (StringUtils.isEmpty(sectorFlightMap.get(sectorString))) {
          continue;
        }
        flightNo = sectorFlightMap.get(sectorString);
        setMealAddons(flightNo, meal, mealPrice);
        Log.info(testId, name + " " + sectorString + " Meal : " + meal);
      }
      totalMealPrice += mealPrice;
    }
    return totalMealPrice;
  }

  // Setting SSR for every sector.
  private void setMealAddons(String flightNo, String addOns, double addOnsPrice) {
    SSR ssr = ssrDetails.get(flightNo);
    if (ssr == null) {
      ssr = new SSR();
    }
    ssr.setMeal(addOns);
    ssr.setMealPrice(addOnsPrice);
    ssrDetails.put(flightNo, ssr);
  }

  private Double selectOneWayBaggage(boolean baggageFlag,
      NewTravellersPageElements travellersElements, String section,
      Map<String, String> sectorFlightMap, List<TravellerDetails> travellerDetailsList,
      int travelerCount) {

    Double totalBaggagePrice = 0.0;
    String name = null;
    int travelersIndex = 0;
    List<WebElement> baggageSectors;
    List<WebElement> baggageOptions;
    if (section.equals("1")) {
      baggageSectors = travellersElements.onwardBaggageSector();
      baggageOptions = travellersElements.onwardBaggageOptions();
    } else {
      baggageSectors = travellersElements.returnBaggageSector();
      baggageOptions = travellersElements.returnBaggageOptions();
    }

    // For Connected flights.
    /*
     * If baggage options are available for Connected flights Then We will select option from top to
     * bottom,firstly we'll select onward/return option for first traveler(For all connected
     * flights), and then for 2nd and then for 3rd and so on.. For example if two connected flights
     * are present and there are 4 travelers Then, Traveler0 = baggage0; Traveler0 = baggage1;
     * Traveler1 = baggage2; Traveler1 = baggage3; Traveler2 = baggage4; Traveler2 = baggage5;
     * Traveler3 = baggage6; Traveler3 = baggage7;
     * 
     * As we need same travelersIndex for even, and odd position for available baggage options.
     * That's why, for even option we are using travelersIndex and increasing travelersIndex, and
     * for odd option we are decreasing travelersIndex and then increasing travelersIndex.
     */

    for (int index = 0; index < baggageOptions.size(); index++) {
      // For Connected flights.(we'll use travelersIndex and then will
      // increase it)
      if (index % 2 == 0 && baggageOptions.size() != travelerCount) {
        ssrDetails = travellerDetailsList.get(travelersIndex).getSsrDetails();
        name = travellerDetailsList.get(travelersIndex++).getName();
      }
      // For Connected flights.(Firstly we'll decrease travelersIndex and
      // then will increase it)
      else if (index % 2 != 0 && baggageOptions.size() != travelerCount) {
        ssrDetails = travellerDetailsList.get(--travelersIndex).getSsrDetails();
        name = travellerDetailsList.get(travelersIndex++).getName();
      }
      // For Direct flights.
      else {
        ssrDetails = travellerDetailsList.get(index).getSsrDetails();
        name = travellerDetailsList.get(index).getName();
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

      // Getting SectorString as "DXB - MAA"
      String sectorString = baggageSectors.get(index).getText().replace(" ", "");
      List<String> sectorList = null;

      /*
       * If available baggage is same for all available connecting (Multiple) sector as
       * "DXB - MAA, MAA - BOM" Then Store each sector in List of string and set SSR for each
       * sector.
       */
      if (StringUtils.contains(sectorString, ",")) {
        sectorList = Arrays.asList(sectorString.split(","));
      }
      String flightNo = null;

      // If Sector List is not null,Then set SSR for each sector.
      if (sectorList != null) {
        for (String sector : sectorList) {
          if (StringUtils.isEmpty(sectorFlightMap.get(sector))) {
            continue;
          }
          flightNo = sectorFlightMap.get(sector);
          setSSR(flightNo, baggage, baggagePrice);
          Log.info(testId, name + " " + sector + " Baggage : " + baggage);
        }
      }// Otherwise set SSR for available single sector.
      else {
        if (StringUtils.isEmpty(sectorFlightMap.get(sectorString))) {
          continue;
        }
        flightNo = sectorFlightMap.get(sectorString);
        setSSR(flightNo, baggage, baggagePrice);
        Log.info(testId, name + " " + sectorString + " Baggage : " + baggage);
      }
      totalBaggagePrice += baggagePrice;
    }

    return totalBaggagePrice;
  }

  // Setting SSR for every sector.
  private void setSSR(String flightNo, String baggage, double baggagePrice) {
    SSR ssr = ssrDetails.get(flightNo);
    if (ssr == null) {
      ssr = new SSR();
    }
    ssr.setBaggage(baggage);
    ssr.setBaggagePrice(baggagePrice);
    ssrDetails.put(flightNo, ssr);
  }

  private void setTaxExempt() {
    try {
      WebElement taxExemptElement = driver.findElement(By.id("phTaxNo"));
      // PageHandler.javaScriptExecuterClick(driver, taxExemptElement);
      taxExemptElement.click();
      Log.info(testId, "Tax Exempt :: No");
    } catch (Exception e) {
    }
  }

  private void acceptVisaInfo() {
    try {
      WebElement visaInfoRadio = driver.findElement(By.id("visaInfoYes_label"));
      // PageHandler.javaScriptExecuterClick(driver, visaInfoRadio);
      visaInfoRadio.click();
    } catch (Exception e) {

    }
  }
}
