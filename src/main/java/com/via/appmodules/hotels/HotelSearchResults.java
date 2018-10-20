package com.via.appmodules.hotels;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.via.pageobjects.hotels.HotelDetails;
import com.via.pageobjects.hotels.HotelSearchResultElements;
import com.via.utils.CalendarUtils;
import com.via.utils.Constant;
import com.via.utils.Constant.HOTEL_FLOW;
import com.via.utils.Constant.VIA_COUNTRY;
import com.via.utils.CustomAssert;
import com.via.utils.Log;
import com.via.utils.NumberUtility;
import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;
import com.via.utils.StringUtilities;

public class HotelSearchResults {

  private String testId;
  private WebDriver driver;
  private HotelDetails hotelDetails;
  private VIA_COUNTRY countryCode;
  private HotelSearchResultElements hotelSearchResultElements;

  private final String VALIDATION_CALENDAR_FORMAT = "dd MMM yyyy";

  public HotelSearchResults(String testId, WebDriver driver, VIA_COUNTRY countryCode,
      RepositoryParser repositoryParser, HotelDetails hotelDetails) {
    this.testId = testId;
    this.driver = driver;
    this.countryCode = countryCode;
    this.hotelDetails = hotelDetails;
    hotelSearchResultElements = new HotelSearchResultElements(testId, driver, repositoryParser);
  }

  public void execute() throws InterruptedException, ParseException {
    Log.info(testId, ":::::::::::::::::       Search Result Page      ::::::::::::::::::");
    Log.divider(testId);

    PageHandler.waitForPageLoad(testId, driver);

    int totalHotels = 0;
    if (hotelDetails.getFlowType() == HOTEL_FLOW.CITY_SEARCH) {
      // Gets total hotels loaded in the result page
      totalHotels = getAllHotelsCount();
    } else {
      PageHandler.sleep(testId, 10 * 1000L);
    }

    // Validating the hotel search details
    validateHotelsSearch();

    if (hotelDetails.getFlowType() == HOTEL_FLOW.CITY_SEARCH) {
      // Finds the target room

      WebElement targetRoom = findHotelAndRoom(totalHotels);
      CustomAssert.assertTrue(testId, targetRoom != null, "No room found with that flow");
      PageHandler.javaScriptExecuterClick(driver, targetRoom);
    } else {
      WebElement targetRoom = findRoomHotelNameSearch(countryCode);
      CustomAssert.assertTrue(testId, targetRoom != null, "No rooms found");
      // CommonUtils.javaScriptExecuterClick(driver, targetRoom);
      PageHandler.javaScriptExecuterClick(driver, targetRoom);
    }
    Log.divider(testId);
  }

  // Validates the hotel search details on the top of the page
  private void validateHotelsSearch() throws ParseException {

    Log.info(testId, "-----------    Search Result Page Header Validation   -----------");

    // Fetching details from results page
    String destinationCity = hotelSearchResultElements.destinationCity().getText();
    String destinationCountry = hotelSearchResultElements.destinationCountry().getText();

    // checkInOutDate -> 13 Aug 2016 | 14 Aug 2016
    String checkInOutDate = hotelSearchResultElements.checkInOutDate().getText();
    List<String> checkInOutDateList = StringUtilities.split(checkInOutDate, Constant.PIPE);
    CustomAssert.assertTrue(testId, checkInOutDateList.size() >= 2,
        "Error in parsing checkinout date");
    Calendar checkInDateCalendar =
        CalendarUtils.dateStringToCalendarDate(checkInOutDateList.get(0),
            VALIDATION_CALENDAR_FORMAT);
    Calendar checkOutDateCalendar =
        CalendarUtils.dateStringToCalendarDate(checkInOutDateList.get(1),
            VALIDATION_CALENDAR_FORMAT);

    String roomsString = hotelSearchResultElements.roomCount().getText();
    int rooms = NumberUtils.toInt(roomsString);

    String adultsString = hotelSearchResultElements.adultCount().getText();
    int adults = NumberUtils.toInt(adultsString);

    String childrenString = hotelSearchResultElements.childCount().getText();
    int children = NumberUtils.toInt(childrenString);

    // Validating the details

    // Validates the city name and country in case of city search and in
    // case of hotel name search set the details
    if (hotelDetails.getFlowType() == HOTEL_FLOW.CITY_SEARCH) {
      String expected = hotelDetails.getDestinationCity();
      CustomAssert.assertTrue(testId, StringUtils.equalsIgnoreCase(expected, destinationCity),
          "Error in validating destination city. Expected : " + expected + " Actual : "
              + destinationCity);

      expected = hotelDetails.getDestinationCountry();
      CustomAssert.assertEquals(driver, testId, true,
          StringUtils.equalsIgnoreCase(expected, destinationCountry),
          "Error in validating  destination country. Expected : " + expected + " Actual : "
              + destinationCountry);
    } else {
      hotelDetails.setDestinationCity(destinationCity);
      hotelDetails.setDestinationCountry(destinationCountry);
    }

    CustomAssert.assertTrue(testId,
        DateUtils.isSameDay(checkInDateCalendar, hotelDetails.getCheckInDate()),
        "Error in validating checkin date");
    CustomAssert.assertTrue(testId,
        DateUtils.isSameDay(checkOutDateCalendar, hotelDetails.getCheckOutDate()),
        "Error in validating checkout date");

    int expectedValue = hotelDetails.getRoomsCount();
    CustomAssert.assertEquals(driver, testId, expectedValue, rooms,
        "Error in validating rooms count. Expected : " + expectedValue + " Actual : " + rooms);

    expectedValue = hotelDetails.getAdultsCount();
    CustomAssert.assertEquals(driver, testId, expectedValue, adults,
        "Error in validating adults count. Expected : " + expectedValue + " Actual : " + adults);

    expectedValue = hotelDetails.getChildrenCount();
    CustomAssert
        .assertEquals(testId, expectedValue, children,
            "Error in validating children count. Expected : " + expectedValue + " Actual : "
                + children);

    Log.info(testId, "Search Result Page Header Validated.");
    Log.divider(testId);
  }

  // Waits for the page to be loaded and gets the total hotels count
  private int getAllHotelsCount() throws InterruptedException {

    int allHotelsCount = -1;
    int loopCount = 0;

    while (allHotelsCount <= 0) {
      // Stops the exection if the page is not loaded even after 15
      // seconds
      if (loopCount > 15) {
        CustomAssert.assertFail(testId, "Error in loading search result page");
      }

      Thread.sleep(1000);

      String allHotelsCountString = hotelSearchResultElements.allHotelCount().getText();
      if (!StringUtils.isEmpty(allHotelsCountString)) {
        allHotelsCount = NumberUtils.toInt(allHotelsCountString);
      }
    }
    return allHotelsCount;
  }

  // Return target room if the room is found and sets the details in case room
  // is found else return null
  private WebElement findRoom(List<WebElement> rooms) {

    // For each room in all the rooms of the hotel
    for (WebElement room : rooms) {

      String bookingFlowIdString =
          room.findElement(By.className("bookingFlowId")).getAttribute("value");
      int bookingFlowId = NumberUtils.toInt(bookingFlowIdString);

      if (bookingFlowId == hotelDetails.getBookingFlowId()) {

        // Gets all the room details
        WebElement roomDetails = room.findElement(By.className("eachRoomDetails"));
        WebElement roomInfo = roomDetails.findElement(By.className("roomDesc"));
        String roomType = roomInfo.findElement(By.tagName("h4")).getText();
        // String roomFacilities =
        // roomInfo.findElement(By.tagName("p")).getText();

        // Sets the room details in POJO
        hotelDetails.setRoomType(roomType);
        // hotelDetails.setRoomFacilities(roomFacilities);

        // Fill rent details

        // Returns the book room element
        WebElement bookRoom =
            roomDetails.findElement(By.xpath(".//*[@class='bookBtn js-bookRoom via-processed']"));
        return bookRoom;
      }
    }

    // If target room is not found in that hotel
    return null;
  }

  // Finds the target hotel and sets its details and returns true if target
  // hotel is found, else false
  private WebElement findHotelAndRoom(int totalHotels) {

    // Iterate for each hotel
    for (int hotelIndex = 0; hotelIndex < totalHotels; hotelIndex++) {

      // Click the select room button for dispaying all available rooms
      WebElement selectRoom;
      try {
        selectRoom =
            hotelSearchResultElements.getElementByPageObject(hotelSearchResultElements
                .modifyPageElementOnce("selectRooms", Integer.toString(hotelIndex)));
      } catch (Exception e) {
        // Select rooms not found, on request button found
        return null;
      }
      PageHandler.javaScriptExecuterClick(driver, selectRoom);

      // Conatins all the rooms of thar hotel
      List<WebElement> rooms =
          hotelSearchResultElements.getElementsByPageObject(hotelSearchResultElements
              .modifyPageElementOnce("hotelRooms", Integer.toString(hotelIndex)));

      // Finds the target room corresponding to the booking flow id
      WebElement targetRoom = findRoom(rooms);

      // If target room is found
      if (targetRoom != null) {

        String hotelName =
            hotelSearchResultElements.getElementByPageObject(
                hotelSearchResultElements.modifyPageElementOnce("hotelName",
                    Integer.toString(hotelIndex))).getText();
        hotelDetails.setName(hotelName);
        Log.info(testId, "Hotel Name :: " + hotelName);

        String hotelAddress =
            hotelSearchResultElements.getElementByPageObject(
                hotelSearchResultElements.modifyPageElementOnce("hotelAddress",
                    Integer.toString(hotelIndex))).getText();
        hotelDetails.setAddress(hotelAddress);
        Log.info(testId, "Hotel Address :: " + hotelAddress);
        Log.info(testId, "Room Type :: " + hotelDetails.getRoomType());

        return targetRoom;
      }

      // If the hotel is the last hotel of the page currently loaded
      if ((hotelIndex + 1) % 30 == 0) {
        scrollToBottom(selectRoom);
      }
    }

    return null;
  }

  // This is called when the hotel name is searched on search page instead of
  // city name
  // In case a room is found, it sets all the details of the room and returns
  // the book room button
  private WebElement findRoomHotelNameSearch(VIA_COUNTRY countryCode) {

    String hotelName = hotelSearchResultElements.nameSearchHotelName().getText();
    String hotelAddress = hotelSearchResultElements.nameSearchHotelAddress().getText();
    WebElement selectRoom = null;

    try {
      selectRoom =
          driver
              .findElement(By
                  .xpath(".//*[@class='roomDetailsSmall']/div[1]//*[@class='bookBtn js-bookRoom via-processed']"));
    } catch (Exception e) {
      return null;
    }

    String roomType = hotelSearchResultElements.nameSearchRoomType().getText();
    String roomPrice = hotelSearchResultElements.nameSearchRoomPrice().getText();
    Double price =
        NumberUtility.getAmountFromString(countryCode, roomPrice.replaceAll("[^0-9.]", ""));

    hotelDetails.setName(hotelName);
    hotelDetails.setAddress(hotelAddress);
    hotelDetails.setRoomType(roomType);
    hotelDetails.setPricePerNightPerRoom(price);

    Log.info(testId, "Hotel Name :: " + hotelName);
    Log.info(testId, "Hotel Address :: " + hotelAddress);
    Log.info(testId, "Room Type :: " + roomType);
    Log.info(testId, "Price :: " + NumberUtility.getRoundedAmount(countryCode, price));

    scrollToBottom(hotelSearchResultElements.nameSearchHotelName());
    return selectRoom;
  }

  // Scrolls to the bottom of page
  private void scrollToBottom(WebElement scrolledElement) {

    JavascriptExecutor js = (JavascriptExecutor) driver;
    js.executeScript("arguments[0].scrollIntoView(true);", scrolledElement);

  }
}
