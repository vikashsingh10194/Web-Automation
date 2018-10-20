package com.via.appmodules.hotels;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.via.pageobjects.hotels.GuestsInputElements;
import com.via.pageobjects.hotels.HotelDetails;
import com.via.pageobjects.hotels.RoomDetails;
import com.via.utils.CalendarUtils;
import com.via.utils.Constant;
import com.via.utils.Constant.VIA_COUNTRY;
import com.via.utils.CustomAssert;
import com.via.utils.Log;
import com.via.utils.NumberUtility;
import com.via.utils.PageHandler;
import com.via.utils.RandomValues;
import com.via.utils.RepositoryParser;
import com.via.utils.StringUtilities;

public class GuestsDetailsInput {

  private final String CALENDAR_FORMAT = "dd MMM, yyyy";

  private String testId;
  private WebDriver driver;
  private VIA_COUNTRY countryCode;
  private HotelDetails hotelDetails;
  private GuestsInputElements guestsInputElements;

  public GuestsDetailsInput(String testId, WebDriver driver, VIA_COUNTRY countryCode,
      RepositoryParser repositoryParser, HotelDetails hotelDetails) {
    this.testId = testId;
    this.driver = driver;
    this.countryCode = countryCode;
    this.hotelDetails = hotelDetails;
    guestsInputElements = new GuestsInputElements(testId, driver, repositoryParser);
  }

  public Double execute() throws ParseException {
    Log.info(testId, "::::::::::::::::        Guest Details Page       ::::::::::::::::");
    Log.divider(testId);

    validateHotelSummary();

    validateBookingSummary();

    Double totalFare = getTotalFare(countryCode);
    Log.info(testId,
        "Total Fare at GDP :: " + NumberUtility.getRoundedAmount(countryCode, totalFare));

    setGuestName();

    setContactDetails();

    PageHandler.javaScriptExecuterClick(driver, guestsInputElements.getReadTermsLabel());

    PageHandler.javaScriptExecuterClick(driver, guestsInputElements.makePayment());

    verifyGuestDetailsAndProceed();

    return totalFare;
  }

  private void setGuestName() {

    List<RoomDetails> roomsDetailsList = hotelDetails.getRoomDetailsList();

    RandomValues.setUsedNameList();

    for (int roomIndex = 0; roomIndex < hotelDetails.getRoomsCount(); roomIndex++) {
      String roomIndexString = Integer.toString(roomIndex);

      // If the room is not the first room, expand the room for entering
      // details
      if (roomIndex != 0) {
        WebElement expandButton =
            guestsInputElements.getElementByPageObject(guestsInputElements.modifyPageElementOnce(
                "guestExpand", Integer.toString(roomIndex + 1)));
        expandButton.click();
      }

      // Room details
      RoomDetails roomDetails = roomsDetailsList.get(roomIndex);
      int adults = roomDetails.getAdultsCount();
      int children = roomDetails.getChildrenCount();

      // Sets all adults of a room
      // Entry for single adult is found for a room in all flow ids except
      // in ids 7 and 13, where details of all children and adults id
      // asked
      for (int adultIndex = 0; adultIndex < adults; adultIndex++) {
        if (adultIndex == 0 || hotelDetails.getBookingFlowId() == 13
            || hotelDetails.getBookingFlowId() == 7) {
          setAdultDetails(roomDetails, roomIndexString, Integer.toString(adultIndex));
        }
      }

      for (int childIndex = 0; childIndex < children; childIndex++) {
        if (hotelDetails.getBookingFlowId() == 13 || hotelDetails.getBookingFlowId() == 7) {
          setChildDetails(roomDetails, roomIndexString, Integer.toString(childIndex));
        }
      }

    }
  }

  // Sets the adult entry corresponding to its roomIndex and adultIndex
  private void setAdultDetails(RoomDetails roomDetails, String roomIndex, String adultIndex) {

    WebElement titleHeader =
        guestsInputElements.getElementByPageObject(guestsInputElements.modifyPageElementTwice(
            "adultTitle", roomIndex, adultIndex));
    WebElement firstNameHeader =
        guestsInputElements.getElementByPageObject(guestsInputElements.modifyPageElementTwice(
            "adultFirstName", roomIndex, adultIndex));
    WebElement surNameHeader =
        guestsInputElements.getElementByPageObject(guestsInputElements.modifyPageElementTwice(
            "adultLastName", roomIndex, adultIndex));

    WebElement randomTitle = guestsInputElements.getRandomOption(titleHeader);
    String title = randomTitle.getText();
    roomDetails.setGuestTitle(title);
    randomTitle.click();

    List<String> name =
        StringUtilities.split(RandomValues.getRandomName(countryCode), Constant.WHITESPACE);

    String firstName = name.get(0);
    roomDetails.setGuestFirstName(firstName);
    firstNameHeader.sendKeys(firstName);

    String surName = name.get(1);
    roomDetails.setGuestSurName(surName);
    surNameHeader.sendKeys(surName);
  }

  // Sets the child entry corresponding to its roomIndex and childIndex
  private void setChildDetails(RoomDetails roomDetails, String roomIndex, String childIndex) {

    WebElement titleHeader =
        guestsInputElements.getElementByPageObject(guestsInputElements.modifyPageElementTwice(
            "childTitle", roomIndex, childIndex));
    WebElement firstNameHeader =
        guestsInputElements.getElementByPageObject(guestsInputElements.modifyPageElementTwice(
            "childFirstName", roomIndex, childIndex));
    WebElement surNameHeader =
        guestsInputElements.getElementByPageObject(guestsInputElements.modifyPageElementTwice(
            "childLastName", roomIndex, childIndex));

    WebElement randomTitle = guestsInputElements.getRandomOption(titleHeader);
    String title = randomTitle.getText();
    roomDetails.setGuestTitle(title);
    randomTitle.click();

    String firstName = RandomValues.getRandomName();
    roomDetails.setGuestFirstName(firstName);
    firstNameHeader.sendKeys(firstName);

    String surName = RandomValues.getRandomName();
    roomDetails.setGuestSurName(surName);
    surNameHeader.sendKeys(surName);
  }

  // Setting the conatct details isd, mobile and email
  private void setContactDetails() {

    WebElement isdCodeHeader = guestsInputElements.getISDCodeHeader();
    Select isdSelect = new Select(isdCodeHeader);
    isdSelect.selectByVisibleText(Constant.ISD_CODE);

    WebElement contactMobile = guestsInputElements.getContactMobile();
    contactMobile.sendKeys(Constant.CONTACT_MOBILE);
    hotelDetails.setContactMobile(Constant.CONTACT_MOBILE_COMPLETE);

    WebElement contactEmail = guestsInputElements.getContactEmail();
    contactEmail.sendKeys(Constant.CONTACT_EMAIL);
    hotelDetails.setContactEmail(Constant.CONTACT_EMAIL);
  }

  private void validateHotelSummary() throws ParseException {

    Log.info(testId, "----------    Guest Details Page Header Validation     ----------");
    // Fetching details from web page
    String hotelName = guestsInputElements.topHotelName().getText();
    String hotelAddr = guestsInputElements.topHotelAddress().getText();
    String checkin = guestsInputElements.topCheckin().getText();
    String checkout = guestsInputElements.topCheckout().getText();
    String rooms = guestsInputElements.topRooms().getText();
    String adults = guestsInputElements.topAdults().getText();
    String children = guestsInputElements.topChildren().getText();
    // String totalPrice = guestsInputElements.topTotalPrice().getText();

    // Parsing the details
    Calendar checkinDate = CalendarUtils.dateStringToCalendarDate(checkin, CALENDAR_FORMAT);
    Calendar checkoutDate = CalendarUtils.dateStringToCalendarDate(checkout, CALENDAR_FORMAT);
    int roomsCount = Integer.parseInt(rooms);
    int adultsCount = Integer.parseInt(adults);
    int childrenCount = Integer.parseInt(children);


    // validating details

    String expected = hotelDetails.getName();
    CustomAssert.assertTrue(testId, StringUtils.equalsIgnoreCase(expected, hotelName),
        "Error in validating hotel name. Expected : " + expected + " Actual : " + hotelName);

    expected = hotelDetails.getAddress();
    CustomAssert.assertTrue(testId, StringUtils.containsIgnoreCase(expected, hotelAddr),
        "Error in validating hotel address. Expected : " + expected + " Actual : " + hotelAddr);

    Calendar expectedDate = hotelDetails.getCheckInDate();
    CustomAssert.assertTrue(
        testId,
        DateUtils.isSameDay(checkinDate, expectedDate),
        "Error in validating checkin date. Expected : "
            + CalendarUtils.getFormattedDate(expectedDate) + " Actual : "
            + CalendarUtils.getFormattedDate(checkinDate));

    expectedDate = hotelDetails.getCheckOutDate();
    CustomAssert.assertTrue(
        testId,
        DateUtils.isSameDay(checkoutDate, expectedDate),
        "Error in validating checkout date. Expected : "
            + CalendarUtils.getFormattedDate(expectedDate) + " Actual : "
            + CalendarUtils.getFormattedDate(checkoutDate));

    int expectedValue = hotelDetails.getRoomsCount();
    CustomAssert.assertEquals(driver, testId, expectedValue, roomsCount,
        "Error in validating rooms count. Expected : " + expectedValue + " Actual : " + roomsCount);

    expectedValue = hotelDetails.getAdultsCount();
    CustomAssert.assertEquals(driver, testId, expectedValue, adultsCount,
        "Error in validating adults count. Expected : " + expectedValue + " Actual : "
            + adultsCount);

    expectedValue = hotelDetails.getChildrenCount();
    CustomAssert.assertEquals(driver, testId, expectedValue, childrenCount,
        "Error in validating children count. Expected : " + expectedValue + " Actual : "
            + childrenCount);

    Log.info(testId, "Guest Details Page Header Validated.");
    Log.divider(testId);
  }

  private void validateBookingSummary() throws ParseException {
    Log.info(testId, "------------       Booking Details Validation       -------------");

    // Fetching the details from web page
    String hotelName = guestsInputElements.leftHotelName().getText();
    String hotelAddr = guestsInputElements.leftHotelAddr().getText();
    String cityCountry = guestsInputElements.leftCityCountry().getText();
    // String roomName = guestsInputElements.leftRoomName().getText();
    // String inclusions = guestsInputElements.leftInclusions().getText();
    String checkin = guestsInputElements.leftCheckin().getText();
    String checkout = guestsInputElements.leftCheckout().getText();
    String rooms = guestsInputElements.leftRooms().getText();
    String adults = guestsInputElements.leftAdults().getText();
    String children = guestsInputElements.leftChildren().getText();

    // Parsing the details
    List<String> cityCountrySplitList = StringUtilities.split(cityCountry, Constant.COMMA);
    CustomAssert.assertTrue(testId, cityCountrySplitList.size() >= 2,
        "Error in parsing cityCountryString");
    String city = cityCountrySplitList.get(0);
    String country = cityCountrySplitList.get(1);

    Calendar checkinDate = CalendarUtils.dateStringToCalendarDate(checkin, CALENDAR_FORMAT);
    Calendar checkoutDate = CalendarUtils.dateStringToCalendarDate(checkout, CALENDAR_FORMAT);

    int roomsCount = Integer.parseInt(rooms);
    int adultsCount = Integer.parseInt(adults);
    int childrenCount = Integer.parseInt(children);

    // Validating the details
    String expected = hotelDetails.getName();
    CustomAssert.assertTrue(testId, StringUtils.equalsIgnoreCase(expected, hotelName),
        "Error in validating hotel name. Expected : " + expected + " Actual : " + hotelName);

    expected = hotelDetails.getAddress();
    CustomAssert.assertTrue(testId, StringUtils.containsIgnoreCase(expected, hotelAddr),
        "Error in validating hotel address. Expected : " + expected + " Actual : " + hotelAddr);

    expected = hotelDetails.getDestinationCity();
    CustomAssert.assertTrue(testId, StringUtils.equalsIgnoreCase(expected, city),
        "Error in validating destination city. Expected : " + expected + " Actual : " + city);

    expected = hotelDetails.getDestinationCountry();
    CustomAssert.assertTrue(testId, StringUtils.equalsIgnoreCase(expected, country),
        "Error in validating destination country. Expected : " + expected + " Actual : " + country);

    Calendar expectedDate = hotelDetails.getCheckInDate();
    CustomAssert.assertTrue(
        testId,
        DateUtils.isSameDay(checkinDate, expectedDate),
        "Error in validating checkin date. Expected : "
            + CalendarUtils.getFormattedDate(expectedDate) + " Actual : "
            + CalendarUtils.getFormattedDate(checkinDate));

    expectedDate = hotelDetails.getCheckOutDate();
    CustomAssert.assertTrue(
        testId,
        DateUtils.isSameDay(checkoutDate, expectedDate),
        "Error in validating checkout date. Expected : "
            + CalendarUtils.getFormattedDate(expectedDate) + " Actual : "
            + CalendarUtils.getFormattedDate(checkoutDate));

    int expectedValue = hotelDetails.getRoomsCount();
    CustomAssert.assertEquals(driver, testId, expectedValue, roomsCount,
        "Error in validating rooms count. Expected : " + expectedValue + " Actual : " + roomsCount);

    expectedValue = hotelDetails.getAdultsCount();
    CustomAssert.assertEquals(driver, testId, expectedValue, adultsCount,
        "Error in validating adults count. Expected : " + expectedValue + " Actual : "
            + adultsCount);

    expectedValue = hotelDetails.getChildrenCount();
    CustomAssert.assertEquals(driver, testId, expectedValue, childrenCount,
        "Error in validating children count. Expected : " + expectedValue + " Actual : "
            + childrenCount);

    Log.info(testId, "Booking details Validated.");
    Log.divider(testId);
  }

  // Retrieving the total fare to be forwarded to next page
  private Double getTotalFare(VIA_COUNTRY countryCode) {
    return NumberUtility.getAmountFromString(countryCode, guestsInputElements.topTotalPrice()
        .getText().replaceAll("[^0-9.]", ""));
  }

  // Verify guest details in the popup
  private void verifyGuestDetailsAndProceed() {

    Log.info(testId, "----------------    Guest Details Verification    ---------------");

    if (hotelDetails.getBookingFlowId() != 7 && hotelDetails.getBookingFlowId() != 13) {
      verifyGuestName();
    }

    verifyContactDetails();

    PageHandler.javaScriptExecuterClick(driver, guestsInputElements.confirmProceedPayBtn());
    Log.divider(testId);
  }

  // Verify guest name in the guests details validation page
  private void verifyGuestName() {

    int rooms = hotelDetails.getRoomsCount();
    List<RoomDetails> roomDetailsList = hotelDetails.getRoomDetailsList();

    for (int roomIndex = 1; roomIndex <= rooms; roomIndex++) {
      RoomDetails roomDetails = roomDetailsList.get(roomIndex - 1);

      String roomIndexString = Integer.toString(roomIndex);
      String guestName =
          guestsInputElements.getElementByPageObject(
              guestsInputElements.modifyPageElementOnce("guestName", roomIndexString)).getText();
      // String guestType = guestsInputElements.getElementByPageObject(
      // guestsInputElements.modifyPageElementOnce("guestType",
      // roomIndexString)).getText();
      String targetGuestName =
          roomDetails.getGuestTitle().concat(" ").concat(roomDetails.getGuestFirstName())
              .concat(" ").concat(roomDetails.getGuestSurName());
      CustomAssert.assertEquals(driver, testId, targetGuestName, guestName,
          "Error in validating guest name in guest validation page of room " + roomIndex);
    }

    Log.info(testId, "Guest Name Verified.");
  }

  // Verify contact details in the guests details validation page
  private void verifyContactDetails() {
    String email = guestsInputElements.confirmContactEmail().getText();
    String mobileNo = guestsInputElements.confirmContactPhone().getText();

    CustomAssert.assertEquals(driver, testId, hotelDetails.getContactEmail(), email,
        "Error in validating email in guest validation page");
    CustomAssert.assertEquals(driver, testId, hotelDetails.getContactMobile(), mobileNo,
        "Error in validating mobile number in guest validation page");

    Log.info(testId, "Guest Contact Details Verified.");
  }
}
