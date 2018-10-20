package com.via.appmodules.holidays;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.via.pageobjects.common.PageElement;
import com.via.pageobjects.holidays.HolidayBookingDetails;
import com.via.pageobjects.holidays.HolidaysSearch;
import com.via.pageobjects.holidays.RoomDetails;
import com.via.testcases.common.TestCaseExcelConstant;
import com.via.utils.CalendarUtils;
import com.via.utils.Constant;
import com.via.utils.CustomAssert;
import com.via.utils.EntityMap;
import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;
import com.via.utils.StringUtilities;

public class HolidaySearchAction {

  public static HolidayBookingDetails execute(WebDriver driver, RepositoryParser repositoryParser,
      Map<Integer, String> holidayTestCase) {

    String testId = holidayTestCase.get(TestCaseExcelConstant.COL_TEST_CASE_ID);
    HolidayBookingDetails holidayBookingDetails =
        setHolidayBookingDetailsObject(repositoryParser, holidayTestCase);

    HolidaysSearch holidaysPage = new HolidaysSearch(testId, driver, repositoryParser);

    WebElement holidaysTab = holidaysPage.holidaysTab();
    holidaysTab.click();

    PageHandler.waitForDomLoad(testId, driver);

    setBookingTab(holidaysPage, holidayBookingDetails);

    String destination = holidayBookingDetails.getDestination();
    holidaysPage.destination().sendKeys(destination);
    WebElement destinationAutoComplete =
        holidaysPage.destinationAutoComplete(EntityMap.getHolidayCity(destination));
    CustomAssert.assertTrue(destinationAutoComplete != null,
        "Error in finding destination auto complete");
    destinationAutoComplete.click();
    // Departure Date

    // holidaysPage.calendarTextBox().click();

    WebElement departureDate =
        CalendarUtils.selectDate(testId, driver, repositoryParser,
            holidayBookingDetails.getCheckInDate(), Constant.DEPART_CAL_ID);
    CustomAssert.assertTrue(departureDate != null, "Unable to select departure date");
    departureDate.click();

    holidaysPage.roomSelector().click();
    setRoomsAndGuests(holidaysPage, holidayBookingDetails);
    holidaysPage.searchHoliday().click();

    return holidayBookingDetails;

  }

  private static void setBookingTab(HolidaysSearch holidaysPage,
      HolidayBookingDetails holidayBookingDetails) {
    List<String> bookingType =
        new ArrayList<String>(StringUtilities.split(holidayBookingDetails.getBookingType(),
            Constant.UNDERSCORE));

    String bookingTab = "domestic";
    if (bookingType.size() == 2) {
      if (StringUtils.equalsIgnoreCase(bookingType.get(0), "Intl")) {
        bookingTab = "international";
      }
    }
    try {
      WebElement tabElement = holidaysPage.getBookingTab(bookingTab);
      tabElement.click();
    } catch (Exception e) {

    }

  }

  private static HolidayBookingDetails setHolidayBookingDetailsObject(
      RepositoryParser repositoryParser, Map<Integer, String> holidayTestCase) {
    HolidayBookingDetails holidayBookingDetails = new HolidayBookingDetails();
    String testId = holidayTestCase.get(TestCaseExcelConstant.COL_TEST_CASE_ID);
    holidayBookingDetails.setTestCaseId(testId);

    String bookingType = holidayTestCase.get(TestCaseExcelConstant.COL_HOLIDAY_BOOKINGTYPE);
    CustomAssert.assertTrue(testId, bookingType != null, "Error in reading the booking type");
    holidayBookingDetails.setBookingType(bookingType);

    String destinationType =
        holidayTestCase.get(TestCaseExcelConstant.COL_HOLIDAY_DESTINATION_TYPE);
    CustomAssert.assertTrue(testId, destinationType != null,
        "Error in reading the destination type");
    holidayBookingDetails.setDestination(destinationType);

    String destination = holidayTestCase.get(TestCaseExcelConstant.COL_HOLIDAY_DESTINATION);
    CustomAssert.assertTrue(testId, destination != null, "Error in reading the destination city");
    holidayBookingDetails.setDestination(destination);

    String onwardAdd = holidayTestCase.get(TestCaseExcelConstant.COL_HOLIDAY_CHECKINDATE);
    CustomAssert.assertTrue(onwardAdd != null, "Error in reading onward date");

    int onwardIncrement = NumberUtils.toInt(onwardAdd);
    Calendar checkInDate = CalendarUtils.getCalendarIncrementedByDays(onwardIncrement);
    holidayBookingDetails.setCheckInDate(checkInDate);

    String roomsString = holidayTestCase.get(TestCaseExcelConstant.COL_HOLIDAY_ROOMCOUNT);
    int rooms = Integer.parseInt(roomsString);
    holidayBookingDetails.setRoomCount(rooms);

    List<RoomDetails> guestDetailsList = new ArrayList<RoomDetails>(rooms);

    String guestsString = holidayTestCase.get(TestCaseExcelConstant.COL_HOLIDAY_GUESTCOUNT);

    setAdultsInObject(holidayBookingDetails, guestDetailsList, guestsString);

    holidayBookingDetails.setRoomDetailsList(guestDetailsList);

    String extraBedReq = holidayTestCase.get(TestCaseExcelConstant.COL_HOLIDAY_BEDREQUIREMENT);
    if (extraBedReq == null) {
      holidayBookingDetails.setBedRequirement(false);
    } else {
      holidayBookingDetails.setBedRequirement(true);
    }

    return holidayBookingDetails;
  }

  private static void setAdultsInObject(HolidayBookingDetails holidayDetails,
      List<RoomDetails> guestDetailsList, String guestsString) {

    List<String> guestsList = StringUtilities.split(guestsString, "|");
    for (String guests : guestsList) {
      RoomDetails roomGuestDetails = new RoomDetails();

      List<String> guestsCountList = StringUtilities.split(guests, "_");
      int guestsCountListSize = guestsCountList.size();

      int adultsCount = Integer.parseInt(guestsCountList.get(0));
      holidayDetails.increaseAdultsCount(adultsCount);
      roomGuestDetails.setAdultsCount(adultsCount);
      if (guestsCountListSize > 1) {
        int childrenCount = Integer.parseInt(guestsCountList.get(1));
        holidayDetails.increaseChildrenCount(childrenCount);
        roomGuestDetails.setChildrenCount(childrenCount);
      }

      guestDetailsList.add(roomGuestDetails);
    }
  }

  private static void setRoomsAndGuests(HolidaysSearch holidaysSearch,
      HolidayBookingDetails holidayDetails) {

    List<RoomDetails> roomGuestDetailsList = holidayDetails.getRoomDetailsList();

    int totalrooms = holidayDetails.getRoomCount();
    for (int roomIndex = 1; roomIndex <= totalrooms; roomIndex++) {
      resetRoom(holidaysSearch, roomIndex);
      RoomDetails guestDetails = roomGuestDetailsList.get(roomIndex - 1);
      String roomId = "eachRoom roomNummber-" + roomIndex;
      setRoom(holidaysSearch, roomId, guestDetails);
      if (roomIndex != totalrooms) {
        WebElement addRoom = holidaysSearch.addRoom();
        addRoom.click();
      }
    }
    WebElement closeRoomsBox = holidaysSearch.doneRoomDetails();
    closeRoomsBox.click();
  }

  private static void resetRoom(HolidaysSearch holidaysSearch, int roomIndex) {
    String roomId = "eachRoom roomNummber-" + roomIndex;
    int adultsCount = getAdultsCount(holidaysSearch, roomId);
    int childrenCount = getChildrenCount(holidaysSearch, roomId);

    while (adultsCount != 1) {
      decreaseAdult(holidaysSearch, roomId);
      adultsCount--;
    }
    while (childrenCount != 0) {
      decreaseChild(holidaysSearch, roomId);
      childrenCount--;
    }
  }

  private static void setRoom(HolidaysSearch holidaysSearch, String roomId, RoomDetails guestDetails) {

    int adults = guestDetails.getAdultsCount();
    int children = guestDetails.getChildrenCount();

    for (int adultIndex = 1; adultIndex < adults; adultIndex++) {
      increaseAdult(holidaysSearch, roomId);
    }

    for (int childIndex = 1; childIndex <= children; childIndex++) {
      increaseChildren(holidaysSearch, roomId);
    }
  }

  private static void increaseAdult(HolidaysSearch holidaysSearch, String roomId) {
    PageElement adultsPlus = holidaysSearch.modifyPageElementOnce("increaseAdult", roomId);
    WebElement increaseAdultsElement = holidaysSearch.getElementByPageObject(adultsPlus);
    increaseAdultsElement.click();
  }

  private static int getAdultsCount(HolidaysSearch holidaysSearch, String roomId) {
    PageElement adults = holidaysSearch.modifyPageElementOnce("adultCount", roomId);
    WebElement adultsElement = holidaysSearch.getElementByPageObject(adults);
    String adultsCount = adultsElement.getText();
    return Integer.parseInt(adultsCount);
  }

  private static void decreaseAdult(HolidaysSearch holidaysSearch, String roomId) {
    PageElement adultsMinus = holidaysSearch.modifyPageElementOnce("decreaseAdult", roomId);
    WebElement decreaseAdultsElement = holidaysSearch.getElementByPageObject(adultsMinus);
    decreaseAdultsElement.click();
  }

  private static void increaseChildren(HolidaysSearch holidaysSearch, String roomId) {
    PageElement childrenPlus = holidaysSearch.modifyPageElementOnce("increaseChild", roomId);
    WebElement increaseChildElement = holidaysSearch.getElementByPageObject(childrenPlus);
    increaseChildElement.click();
  }

  private static int getChildrenCount(HolidaysSearch holidaysSearch, String roomId) {
    PageElement children = holidaysSearch.modifyPageElementOnce("childCount", roomId);
    WebElement childrenElement = holidaysSearch.getElementByPageObject(children);
    String childrenCount = childrenElement.getText();
    return Integer.parseInt(childrenCount);
  }

  private static void decreaseChild(HolidaysSearch holidaysSearch, String roomId) {
    PageElement childMinus = holidaysSearch.modifyPageElementOnce("decreaseChild", roomId);
    WebElement decreaseChildElement = holidaysSearch.getElementByPageObject(childMinus);
    decreaseChildElement.click();
  }
}
