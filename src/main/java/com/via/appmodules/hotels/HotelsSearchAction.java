package com.via.appmodules.hotels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.via.pageobjects.hotels.HotelDetails;
import com.via.pageobjects.hotels.HotelsSearchElements;
import com.via.pageobjects.hotels.RoomDetails;
import com.via.testcases.common.TestCaseExcelConstant;
import com.via.utils.CalendarUtils;
import com.via.utils.Constant;
import com.via.utils.Constant.HOTEL_FLOW;
import com.via.utils.CustomAssert;
import com.via.utils.EntityMap;
import com.via.utils.Log;
import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;
import com.via.utils.StringUtilities;

public class HotelsSearchAction {

  private String testId;
  private WebDriver driver;
  private RepositoryParser repositoryParser;
  private HotelsSearchElements hotelsSearchElements;
  private static final String autoSuggestion = "ui-id-1";

  public HotelsSearchAction(String testId, WebDriver driver, RepositoryParser repositoryParser) {
    this.testId = testId;
    this.driver = driver;
    this.repositoryParser = repositoryParser;
    hotelsSearchElements = new HotelsSearchElements(driver, repositoryParser, testId);
  }

  public HotelDetails execute(Map<Integer, String> hotelTestCase) {

    Log.info(testId, ":::::::::::::::::         Hotel Search Page       ::::::::::::::::");
    Log.divider(testId);
    // Gets the details from excel and sets them in POJO
    HotelDetails hotelDetails = setHotelDetailsObject(hotelTestCase);

    // Clicks the hotels tab to open hotel search box
    hotelsSearchElements.getHotelsSeachBox().click();

    PageHandler.waitForDomLoad(testId, driver);

    String completeDestination = "";
    String destinationKeys = "";

    WebElement destination = hotelsSearchElements.destination();

    // Sets the destination
    if (hotelDetails.getFlowType() == HOTEL_FLOW.CITY_SEARCH) {
      destinationKeys = hotelDetails.getDestinationCity();
      completeDestination = EntityMap.getHotelCityName(destinationKeys);

    } else {
      destinationKeys = hotelDetails.getName();
      completeDestination = EntityMap.getHotelName(destinationKeys);
    }

    int length = destinationKeys.length();
    Long waitTimePerKey = 1000L / length;

    for (int index = 0; index < length; index++) {
      destination.sendKeys("" + destinationKeys.charAt(index));
      PageHandler.sleep(testId, waitTimePerKey);
    }

    PageHandler.sleep(testId, 1 * 1000L);

    Log.info(testId, "Search By :: " + hotelDetails.getFlowType().toString());

    Log.info(testId, "Complete destination :: " + completeDestination);

    // hotelsSearchElements.destinationAutoComplete(autoSuggestion,completeDestination);
    WebElement element =
        hotelsSearchElements.destinationAutoComplete(autoSuggestion, completeDestination);
    // element.click();
    PageHandler.javaScriptExecuterClick(driver, element);
    // Sets the hotel country in POJO
    setHotelCountry(hotelDetails, completeDestination);

    // Sets the checkin date in search box
    hotelsSearchElements.checkIn().click();
    Calendar date = hotelDetails.getCheckInDate();
    CalendarUtils.selectDate(testId, driver, repositoryParser, date, Constant.DEPART_CAL_ID)
        .click();

    Log.info(testId, "CheckIn :: " + CalendarUtils.getFormattedDate(date));

    // Sets the checkout date in search box
    hotelsSearchElements.checkOut().click();
    date = hotelDetails.getCheckOutDate();
    CalendarUtils.selectDate(testId, driver, repositoryParser, date, Constant.RETURN_CAL_ID)
        .click();

    Log.info(testId, "CheckOut :: " + CalendarUtils.getFormattedDate(date));

    // Sets the guest details in search box
    hotelsSearchElements.guestInput().click();
    setRoomsAndGuests(hotelDetails);

    // Clicks the search hotels button
    hotelsSearchElements.searchHotels().click();

    return hotelDetails;
  }

  private HotelDetails setHotelDetailsObject(Map<Integer, String> hotelTestCase) {
    HotelDetails hotelDetails = new HotelDetails();

    String city = hotelTestCase.get(TestCaseExcelConstant.COL_HOTEL_CITY);
    if (city != null) {
      hotelDetails.setDestinationCity(city);
      hotelDetails.setFlowType(HOTEL_FLOW.CITY_SEARCH);
    } else {
      String hotelName = hotelTestCase.get(TestCaseExcelConstant.COL_HOTEL_NAME);
      hotelDetails.setName(hotelName);
      hotelDetails.setFlowType(HOTEL_FLOW.NAME_SEARCH);
    }

    int checkin = NumberUtils.toInt(hotelTestCase.get(TestCaseExcelConstant.COL_HOTEL_CHECKIN));
    Calendar checkinCalendar = CalendarUtils.getCalendarIncrementedByDays(checkin);
    hotelDetails.setCheckInDate(checkinCalendar);

    int checkout =
        checkin + NumberUtils.toInt(hotelTestCase.get(TestCaseExcelConstant.COL_HOTEL_CHECKOUT));
    Calendar checkoutCalendar = CalendarUtils.getCalendarIncrementedByDays(checkout);
    hotelDetails.setCheckOutDate(checkoutCalendar);

    int rooms = NumberUtils.toInt(hotelTestCase.get(TestCaseExcelConstant.COL_HOTEL_ROOMS));
    hotelDetails.setRoomsCount(rooms);

    // Creates a list of hotel rooms using the above fetched rooms count
    List<RoomDetails> roomDetailsList = new ArrayList<RoomDetails>(rooms);

    String guestsString = hotelTestCase.get(TestCaseExcelConstant.COL_HOTEL_GUESTS);
    String childrenAgeString = hotelTestCase.get(TestCaseExcelConstant.COL_HOTEL_AGE);

    // Sets the adults and children in the hotel rooms list
    setGuestsCountInObject(hotelDetails, roomDetailsList, guestsString);
    setChildrenAgeInObject(roomDetailsList, childrenAgeString);

    hotelDetails.setRoomDetailsList(roomDetailsList);

    // Gets booking flow id in case of city search
    String bookingFlow = hotelTestCase.get(TestCaseExcelConstant.COL_HOTEL_FLOW);
    if (bookingFlow != null) {
      String bookingFlowId = repositoryParser.getPropertyValue(bookingFlow);
      int bookingFlowID = NumberUtils.toInt(bookingFlowId);
      hotelDetails.setBookingFlowId(bookingFlowID);
    }

    return hotelDetails;
  }

  // guestString -> 2_1 | 3
  // There are 2 rooms with first room with 2 adults & 1 child and second room
  // with 3 adults
  private void setGuestsCountInObject(HotelDetails hotelDetails, List<RoomDetails> roomDetailsList,
      String guestsString) {

    List<String> guestsList = StringUtilities.split(guestsString, Constant.PIPE);
    // Interate for each room
    for (String guests : guestsList) {
      RoomDetails roomDetails = new RoomDetails();

      List<String> guestsCountList = StringUtilities.split(guests, Constant.UNDERSCORE);
      int guestsCountListSize = guestsCountList.size();

      // Sets the adults
      int adultsCount = NumberUtils.toInt(guestsCountList.get(0));
      hotelDetails.increaseAdultsCount(adultsCount);
      roomDetails.setAdultsCount(adultsCount);

      // Sets the children
      if (guestsCountListSize > 1) {
        int childrenCount = NumberUtils.toInt(guestsCountList.get(1));
        hotelDetails.increaseChildrenCount(childrenCount);
        roomDetails.setChildrenCount(childrenCount);
      }

      roomDetailsList.add(roomDetails);
    }
  }

  // childAgeString -> 2_6_1
  // Repesents 3 child with ages 2, 6 and 1 serially
  private void setChildrenAgeInObject(List<RoomDetails> roomDetailsList, String childrenAgeString) {

    int totalRooms = roomDetailsList.size();
    List<String> childrenAgeList = StringUtilities.split(childrenAgeString, Constant.UNDERSCORE);
    int childrenAgeIndex = 0;

    // Fore each room
    for (int roomIndex = 0; roomIndex < totalRooms; roomIndex++) {

      RoomDetails roomDetails = roomDetailsList.get(roomIndex);
      int childrenCount = roomDetails.getChildrenCount();

      // Gets children for each room and stores in list and sets in POJO
      List<Integer> childrenAgeListRoom = new ArrayList<Integer>(childrenCount);
      for (int childrenIndex = 0; childrenIndex < childrenCount; childrenIndex++) {
        childrenAgeListRoom.add(NumberUtils.toInt(childrenAgeList.get(childrenAgeIndex)));
        childrenAgeIndex++;
      }
      roomDetails.setChildAge(childrenAgeListRoom);
    }
  }

  // Gets country name from complete destination name and sets it in POJO
  private void setHotelCountry(HotelDetails hotelDetails, String completeDestination) {

    List<String> addressSplittedList = StringUtilities.split(completeDestination, Constant.COMMA);
    int listSize = addressSplittedList.size();
    CustomAssert.assertTrue(testId, listSize >= 2, "Error in fetching counry name from "
        + completeDestination);

    String country = addressSplittedList.get(listSize - 1);
    hotelDetails.setDestinationCountry(country);
  }

  // Sets the rooms and their respective guest in search box
  private void setRoomsAndGuests(HotelDetails hotelDetails) {

    List<RoomDetails> roomDetailsList = hotelDetails.getRoomDetailsList();
    int totalRooms = hotelDetails.getRoomsCount();

    Log.info(testId, "Total Room(s) :: " + totalRooms);
    Log.divider(testId);

    for (int roomIndex = 1; roomIndex <= totalRooms; roomIndex++) {

      Log.info(testId, "-----------------       Room " + roomIndex
          + " Details        ----------------");
      RoomDetails guestDetails = roomDetailsList.get(roomIndex - 1);
      String roomId = "room" + roomIndex;
      setRoom(roomId, guestDetails);
      // If its not the last room, add a new room
      if (roomIndex != totalRooms) {
        hotelsSearchElements.addRoom().click();
      }
    }

    // Close the room box after all room are set
    hotelsSearchElements.doneRoomDetails().click();
  }

  // Resets the room in its zero stage
  private void resetRoom(String roomId) {
    int adultsCount = getAdultsCount(roomId);
    int childrenCount = getChildrenCount(roomId);

    while (adultsCount != 1) {
      decreaseAdult(roomId);
      adultsCount--;
    }
    while (childrenCount != 0) {
      decreaseChild(roomId);
      childrenCount--;
    }
  }

  // Sets a room adults and children with given roomId
  private void setRoom(String roomId, RoomDetails roomDetails) {
    resetRoom(roomId);
    int adults = roomDetails.getAdultsCount();
    int children = roomDetails.getChildrenCount();
    List<Integer> childrenAgeList = roomDetails.getChildAge();

    Log.info(testId, "Adult(s) :: " + adults);
    Log.info(testId, "Children :: " + children);

    // Sets the adults
    for (int adultIndex = 1; adultIndex < adults; adultIndex++) {
      increaseAdult(roomId);
    }

    // Sets the children and their ages
    for (int childIndex = 1; childIndex <= children; childIndex++) {
      increaseChildren(roomId);
      String childAgeId = roomId.concat("childAge" + childIndex);
      setChildAge(childAgeId, childrenAgeList.get(childIndex - 1));
    }

    Log.divider(testId);
  }

  // Increase adults in search box corresponding to roomId
  private void increaseAdult(String roomId) {
    WebElement increaseAdultsElement =
        hotelsSearchElements.getElementByPageObject(hotelsSearchElements.modifyPageElementOnce(
            "increaseAdult", roomId));
    increaseAdultsElement.click();
  }

  // Gets adults count from search box corresponding to roomId
  private int getAdultsCount(String roomId) {
    String adultsCount =
        hotelsSearchElements.getElementByPageObject(
            hotelsSearchElements.modifyPageElementOnce("adultCount", roomId)).getText();
    return NumberUtils.toInt(adultsCount);
  }

  // Decrease adults in search box corresponding to roomId
  private void decreaseAdult(String roomId) {
    WebElement decreaseAdultsElement =
        hotelsSearchElements.getElementByPageObject(hotelsSearchElements.modifyPageElementOnce(
            "decreaseAdult", roomId));
    decreaseAdultsElement.click();
  }

  // Increase children in search box corresponding to roomId
  private void increaseChildren(String roomId) {
    WebElement increaseChildElement =
        hotelsSearchElements.getElementByPageObject(hotelsSearchElements.modifyPageElementOnce(
            "increaseChild", roomId));
    increaseChildElement.click();
  }

  // Gets children count from search box corresponding to roomId
  private int getChildrenCount(String roomId) {
    String childrenCount =
        hotelsSearchElements.getElementByPageObject(
            hotelsSearchElements.modifyPageElementOnce("childCount", roomId)).getText();
    return NumberUtils.toInt(childrenCount);
  }

  // Decrease children in search box corresponding to roomId
  private void decreaseChild(String roomId) {
    WebElement decreaseChildElement =
        hotelsSearchElements.getElementByPageObject(hotelsSearchElements.modifyPageElementOnce(
            "decreaseChild", roomId));
    decreaseChildElement.click();
  }

  // Sets child age in search box corresponding to roomId
  private void setChildAge(String childAgeId, int age) {
    WebElement childAgeElement =
        hotelsSearchElements.getElementByPageObject(hotelsSearchElements.modifyPageElementOnce(
            "childAge", childAgeId));
    Select ageSelect = new Select(childAgeElement);
    ageSelect.selectByValue(Integer.toString(age));
  }
}
