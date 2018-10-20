package com.via.appmodules.holidays;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import com.via.pageobjects.common.PageElement;
import com.via.pageobjects.holidays.GuestsInput;
import com.via.pageobjects.holidays.HolidayBookingDetails;
import com.via.pageobjects.holidays.RoomDetails;
import com.via.utils.CalendarUtils;
import com.via.utils.CustomAssert;
import com.via.utils.Log;
import com.via.utils.NumberUtility;
import com.via.utils.PageHandler;
import com.via.utils.RandomValues;
import com.via.utils.RepositoryParser;

public class GuestsDetailsInput {

  private static final String ISD_CODE = "91";
  private static final String CONTACT_MOBILE = "9611577993";
  private static final String CONTACT_MOBILE_COMPLETE = "+91-9611577993";
  private static final String CONTACT_EMAIL = "qa@via.com";

  private static final String BIRTH_DATE = "01";
  private static final String BIRTH_MONTH = "01";
  private static final String BIRTH_YEAR_ADULT = "1990";
  private static final String BIRTH_YEAR_CHILD = "2008";

  public static double execute(String testId, WebDriver driver, RepositoryParser repositoryParser,
      HolidayBookingDetails holidayDetails) {

    GuestsInput guestsInputElements = new GuestsInput(driver, repositoryParser, testId);

    boolean topBoxValidate = validateTopBox(driver, testId, guestsInputElements, holidayDetails);
    if (topBoxValidate) {
      Log.info(testId, "Top Box Validated");
    } else {
      Log.error(driver, testId, "Error in validating top box");
    }

    boolean leftBoxValidate = validateLeftBox(driver, testId, guestsInputElements, holidayDetails);
    if (leftBoxValidate) {
      Log.info(testId, "Left Box Validated");
    } else {
      Log.error(driver, testId, "Error in validating left box");
    }

    setGuestName(testId, driver, guestsInputElements, holidayDetails);
    setContactDetails(testId, guestsInputElements, holidayDetails);

    PageHandler.javaScriptExecuterClick(driver, guestsInputElements.getReadTermsLabel());
    PageHandler.javaScriptExecuterClick(driver, guestsInputElements.makePayment());

    double totalFare = getTotalFare(testId, guestsInputElements);
    Log.info(testId, "total fare is retrieved" + totalFare);
    PageHandler.javaScriptExecuterClick(driver, guestsInputElements.confirmationButton());
    Log.info(testId, "confirmation button is clicked");
    return totalFare;
  }

  private static void setGuestName(String testId, WebDriver driver,
      GuestsInput guestsInputElements, HolidayBookingDetails holidayDetails) {

    List<RoomDetails> guestsDetailsList = holidayDetails.getRoomDetailsList();

    for (int roomIndex = 1; roomIndex <= holidayDetails.getRoomCount(); roomIndex++) {
      String roomIndexString = Integer.toString(roomIndex);

      if (roomIndex != 1) {
        PageElement expandElement =
            guestsInputElements.modifyPageElementOnce("guestExpand", roomIndexString);
        WebElement expandButton = guestsInputElements.getElementByPageObject(expandElement);
        PageHandler.javaScriptExecuterClick(driver, expandButton);
      }

      RoomDetails guestDetails = guestsDetailsList.get(roomIndex - 1);

      for (int guestIndex = 1; guestIndex <= guestDetails.getAdultsCount(); guestIndex++) {
        performGuestDetailsInsertion(guestsInputElements, guestIndex, roomIndexString,
            guestDetails, "adult");
        Log.info(testId, "guest-adult details have been inserted");
      }
      for (int guestIndex = 1; guestIndex <= guestDetails.getChildrenCount(); guestIndex++) {
        performGuestDetailsInsertion(guestsInputElements, guestIndex, roomIndexString,
            guestDetails, "child");
        Log.info(testId, "guest-child details have been inserted");
      }

    }
  }

  private static void performGuestDetailsInsertion(GuestsInput guestsInputElements, int guestIndex,
      String roomIndexString, RoomDetails guestDetails, String guestType) {

    PageElement titleElement =
        guestsInputElements.modifyPageElementOnce("guestTitle", guestType + guestIndex + "Room"
            + roomIndexString);
    WebElement titleHeader = guestsInputElements.getElementByPageObject(titleElement);

    PageElement firstNameElement =
        guestsInputElements.modifyPageElementOnce("guestFirstName", guestType + guestIndex + "Room"
            + roomIndexString);
    WebElement firstNameHeader = guestsInputElements.getElementByPageObject(firstNameElement);

    PageElement surNameElement =
        guestsInputElements.modifyPageElementOnce("guestLastName", guestType + guestIndex + "Room"
            + roomIndexString);
    WebElement surNameHeader = guestsInputElements.getElementByPageObject(surNameElement);

    PageElement dobDayElement =
        guestsInputElements.modifyPageElementOnce("guestDOBDay", guestType + guestIndex + "Room"
            + roomIndexString);
    WebElement dobDayHeader = guestsInputElements.getElementByPageObject(dobDayElement);

    PageElement dobMonthElement =
        guestsInputElements.modifyPageElementOnce("guestDOBMonth", guestType + guestIndex + "Room"
            + roomIndexString);
    WebElement dobMonthHeader = guestsInputElements.getElementByPageObject(dobMonthElement);

    PageElement dobYearElement =
        guestsInputElements.modifyPageElementOnce("guestDOBYear", guestType + guestIndex + "Room"
            + roomIndexString);
    WebElement dobYearHeader = guestsInputElements.getElementByPageObject(dobYearElement);

    WebElement randomTitle = guestsInputElements.getRandomOption(titleHeader);
    String title = randomTitle.getText();
    guestDetails.setGuestTitle(title);
    randomTitle.click();

    String firstName = RandomValues.getRandomName();
    guestDetails.setGuestFirstName(firstName);
    firstNameHeader.sendKeys(firstName);

    String surName = RandomValues.getRandomName();
    guestDetails.setGuestSurName(surName);
    surNameHeader.sendKeys(surName);

    Select dateSelect = new Select(dobDayHeader);
    dateSelect.selectByValue(BIRTH_DATE);

    Select monthSelect = new Select(dobMonthHeader);
    monthSelect.selectByValue(BIRTH_MONTH);
    if (guestType.equals("adult")) {
      Select yearSelect = new Select(dobYearHeader);
      yearSelect.selectByValue(BIRTH_YEAR_ADULT);
    } else {
      Select yearSelect = new Select(dobYearHeader);
      yearSelect.selectByValue(BIRTH_YEAR_CHILD);
    }

  }

  private static void setContactDetails(String testId, GuestsInput guestsInputElements,
      HolidayBookingDetails holidayDetails) {

    WebElement isdCodeHeader = guestsInputElements.getISDCodeHeader();
    Assert.assertTrue(isdCodeHeader != null);
    Select isdSelect = new Select(isdCodeHeader);
    isdSelect.selectByVisibleText(ISD_CODE);

    WebElement contactMobile = guestsInputElements.getContactMobile();
    Assert.assertTrue(contactMobile != null);
    contactMobile.sendKeys(CONTACT_MOBILE);
    holidayDetails.setContactMobile(CONTACT_MOBILE_COMPLETE);

    WebElement contactEmail = guestsInputElements.getContactEmail();
    Assert.assertTrue(contactEmail != null);
    contactEmail.sendKeys(CONTACT_EMAIL);
    holidayDetails.setContactEmail(CONTACT_EMAIL);

  }

  private static boolean validateTopBox(WebDriver driver, String testcaseId,
      GuestsInput guestsInputElements, HolidayBookingDetails holidayDetails) {

    String packageName = guestsInputElements.topPackageName().getText();
    String checkinCheckout = guestsInputElements.topCheckinCheckout().getText();
    String checkin = checkinCheckout.split("/")[0];
    String checkout = checkinCheckout.split("/")[1];
    // String rooms = guestsInputElements.topRooms().getText();
    String adults = guestsInputElements.topAdults().getText();
    String children = guestsInputElements.topChildren().getText();
    // String totalPrice = guestsInputElements.topTotalPrice().getText();

    Calendar checkinDate = null;
    Calendar checkoutDate = null;

    try {
      checkinDate = CalendarUtils.dateStringToCalendarDate(checkin, "dd MMM yyyy");
    } catch (ParseException e) {
      Log.error("Error in parsing check in date");
      return false;
    }

    try {
      checkoutDate = CalendarUtils.dateStringToCalendarDate(checkout, "dd MMM yyyy");
    } catch (ParseException e) {
      Log.error("Error in parsing check out date");
      return false;
    }

    // int roomsCount = Integer.parseInt(rooms);
    int adultsCount = NumberUtils.toInt(adults);
    int childrenCount = NumberUtils.toInt(children);

    // String totalPrice =
    // totalPriceIntegerPart.concat(totalPriceFloatPart);
    // totalPrice = StringUtils.trimToEmpty(totalPrice);
    // float finalPrice =
    // NumberUtility.getFloatAmountFronString(totalPrice);

    CustomAssert.assertEquals(driver, testcaseId, packageName, holidayDetails.getName(),
        "package name did not match");

    boolean checkInValidate = DateUtils.isSameDay(checkinDate, holidayDetails.getCheckInDate());
    // boolean checkOutValidate = DateUtils.isSameDay(checkoutDate,
    // holidayDetails.getCheckOutDate());
    CustomAssert.assertTrue(testcaseId, checkInValidate, "checkin date is not on the same day");
    // Assert.assertTrue(checkOutValidate);

    // CustomAssert.assertEquals(driver, testcaseId, roomsCount,
    // holidayDetails.getRoomCount(), "room count did not match");
    CustomAssert.assertEquals(driver, testcaseId, adultsCount, holidayDetails.getAdultsCount(),
        "adults count did not match");
    CustomAssert.assertEquals(driver, testcaseId, childrenCount, holidayDetails.getChildrenCount(),
        "children count did not match");

    return true;
  }

  private static boolean validateLeftBox(WebDriver driver, String testcaseId,
      GuestsInput guestsInputElements, HolidayBookingDetails holidayDetails) {

    String hotelName = guestsInputElements.leftHotelName().getText();
    String packageName = guestsInputElements.leftPackageName().getText();
    String leftCheckinCheckout = guestsInputElements.leftCheckinCheckout().getText();
    String checkin = leftCheckinCheckout.split("/")[0];
    String checkout = leftCheckinCheckout.split("/")[1];
    String packageDuration = guestsInputElements.leftpackageNights().getText();
    String packageNights = packageDuration.split("Nights")[0].trim();
    String adults = guestsInputElements.leftAdults().getText();
    String children = guestsInputElements.leftChildren().getText();

    Calendar checkinDate = null;
    Calendar checkoutDate = null;

    try {
      checkinDate = CalendarUtils.dateStringToCalendarDate(checkin, "dd MMM yyyy");
    } catch (ParseException e) {
      Log.error("Error in parsing check in date");
      return false;
    }

    try {
      checkoutDate = CalendarUtils.dateStringToCalendarDate(checkout, "dd MMM yyyy");
    } catch (ParseException e) {
      Log.error("Error in parsing check out date");
      return false;
    }

    int nightsCount = NumberUtils.toInt(packageNights);
    int adultsCount = NumberUtils.toInt(adults);
    int childrenCount = NumberUtils.toInt(children);

    CustomAssert.assertEquals(driver, testcaseId, hotelName, holidayDetails.getHotelName(),
        "hotel name did not match");
    CustomAssert.assertEquals(driver, testcaseId, packageName, holidayDetails.getName(),
        "package  name did not match");
    boolean checkInValidate = DateUtils.isSameDay(checkinDate, holidayDetails.getCheckInDate());
    // boolean checkOutValidate = DateUtils.isSameDay(checkoutDate,
    // holidayDetails.getCheckOutDate());
    CustomAssert.assertTrue(testcaseId, checkInValidate, "check in date is not on the same day");

    // CustomAssert.assertEquals(driver, testcaseId, nightsCount,
    // holidayDetails.getNightsCount(),
    // "package nights count did not match");
    CustomAssert.assertEquals(driver, testcaseId, adultsCount, holidayDetails.getAdultsCount(),
        "adult count did not match");
    CustomAssert.assertEquals(driver, testcaseId, childrenCount, holidayDetails.getChildrenCount(),
        "children count did not match ");

    return true;
  }

  private static double getTotalFare(String testId, GuestsInput guestsInputElements) {
    String totalFare = guestsInputElements.getTotalFare().getText();
    double completeFare = NumberUtility.getAmountFromString(totalFare);
    return completeFare;
  }

}
