package com.via.appmodules.holidays;

import java.text.ParseException;
import java.util.Calendar;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.via.pageobjects.common.PageElement;
import com.via.pageobjects.holidays.HolidayBookingDetails;
import com.via.pageobjects.holidays.HolidaySearchResultElements;
import com.via.utils.CalendarUtils;
import com.via.utils.Constant;
import com.via.utils.CustomAssert;
import com.via.utils.Log;
import com.via.utils.PageHandler;
import com.via.utils.RandomValues;
import com.via.utils.RepositoryParser;

public class HolidaySearchResults {

  public static final String BOOK = "BOOK";
  private static final String CONTACT_MOBILE = "9611577993";
  private static final String CONTACT_EMAIL = "qa@via.com";
  private static final String DATE_FORMAT = "EEE, MMM dd yyyy";

  public static boolean execute(String testId, WebDriver driver, RepositoryParser repositoryParser,
      HolidayBookingDetails holidayDetails) {

    Log.info(testId, ":::::::::::::      Holidays Search Result Page      ::::::::::::::");
    Log.divider(testId);

    HolidaySearchResultElements holidaySearchResultElements =
        new HolidaySearchResultElements(testId, driver, repositoryParser);

    PageHandler.sleep(testId, 5 * 1000L);

    boolean holidaySearchvalidate =
        validateHolidaySearch(driver, testId, holidaySearchResultElements, holidayDetails);

    CustomAssert.assertTrue(testId, holidaySearchvalidate, "Holidays Search Validated",
        "Error in validation Holidays search");
    Log.divider(testId);

    if (holidayDetails.getBookingType().toUpperCase().equals(BOOK)) {
      String packageName = holidaySearchResultElements.packageName().getText();
      holidayDetails.setName(packageName);
      Actions action = new Actions(driver);
      action.moveToElement(holidaySearchResultElements.hotelElement()).perform();
      String hotelName = holidaySearchResultElements.hotelName().getText();
      holidayDetails.setHotelName(hotelName);

      holidaySearchResultElements.bookPackage().click();
      Log.info(testId, "Package book button clicked");
      Calendar checkinDate = holidayDetails.getCheckInDate();

      int year = checkinDate.get(Calendar.YEAR);
      String month = CalendarUtils.getDateInFormat(checkinDate, "MMM");
      CalendarUtils.getDateInFormat(checkinDate, "dd MMM yyyy");
      selectMonthAndYear(testId, holidaySearchResultElements, month, year);

      PageElement calendarBookButton =
          holidaySearchResultElements.modifyPageElementOnce("packageCalenderBookButton",
              CalendarUtils.getDateInFormat(checkinDate, "MMM dd yyyy"));
      WebElement calendarButtonElement =
          holidaySearchResultElements.getElementByPageObject(calendarBookButton);
      PageHandler.javaScriptExecuterClick(driver, calendarButtonElement);
      Log.info(testId, "Package date is selected");
      return false;
    } else {
      scrollToBottom(holidaySearchResultElements, driver, testId);
      WebElement element = holidaySearchResultElements.sendEnquiry();
      PageHandler.javaScriptExecuterClick(driver, element);
      fillEnquiryForm(testId, driver, repositoryParser, holidaySearchResultElements, holidayDetails);
      Log.info(testId, "enquiry form filled");
      return true;
    }

  }

  private static void fillEnquiryForm(String testId, WebDriver driver,
      RepositoryParser repositoryParser, HolidaySearchResultElements holidaySearchResultElements,
      HolidayBookingDetails holidayDetails) {

    Log.info(testId, "-------------------         Enquiry Form        ------------------");

    String firstName = RandomValues.getRandomName();
    holidaySearchResultElements.enquiryFirstName().sendKeys(firstName);
    Log.info(testId, "Enquired traveller's first name is filled");
    String LastName = RandomValues.getRandomName();
    holidaySearchResultElements.enquiryLastName().sendKeys(LastName);
    Log.info(testId, "Enquired traveller's last name is filled");
    holidaySearchResultElements.enquiryEmail().sendKeys(CONTACT_EMAIL);
    Log.info(testId, "Enquired traveller's email id is filled");
    holidaySearchResultElements.enquiryMobileno().sendKeys(CONTACT_MOBILE);
    Log.info(testId, "Enquired traveller's contact number is filled");
    holidaySearchResultElements.enquiryCalendar().click();
    Log.info(testId, "Enquired traveller's calendar  is clicked");
    // Departure Date
    WebElement departureDate =
        CalendarUtils.selectDate(testId, driver, repositoryParser, holidayDetails.getCheckInDate(),
            Constant.ENQUIRY_DEPART_CAL_ID);
    CustomAssert.assertTrue(departureDate != null, "Unable to select departure date");
    departureDate.click();
    Log.info(testId, "Departure Date selected");
    holidaySearchResultElements.submitSendEnquiry().click();
    // String message = holidaySearchResultElements.alertMsg().getText();
    // Log.info(testId, message);
    holidaySearchResultElements.cancelEnquiry();
  }

  private static boolean validateHolidaySearch(WebDriver driver, String testId,
      HolidaySearchResultElements holidaySearchResultElements, HolidayBookingDetails holidayDetails) {

    Log.info(testId, "-------------   Search Result Page Header Validation  ------------");

    String destinationCity = holidaySearchResultElements.destinationCity().getText();

    String checkInDate = holidaySearchResultElements.checkInOutDate().getText();
    Calendar checkInDateCalendar = null;

    try {
      checkInDateCalendar = CalendarUtils.dateStringToCalendarDate(checkInDate, DATE_FORMAT);
    } catch (ParseException e) {
      CustomAssert.assertFail(testId, "Unable to parse check In date");
      return false;
    }

    int rooms = NumberUtils.toInt(holidaySearchResultElements.roomCount().getText());

    int adults = NumberUtils.toInt(holidaySearchResultElements.adultCount().getText());

    int children = NumberUtils.toInt(holidaySearchResultElements.childCount().getText());

    CustomAssert.assertEquals(driver, testId, destinationCity, holidayDetails.getDestination(),
        "Destination is not same as actual destination");

    CustomAssert.assertTrue(testId,
        DateUtils.isSameDay(checkInDateCalendar, holidayDetails.getCheckInDate()),
        "error in validating checkin date");

    CustomAssert.assertEquals(driver, testId, rooms, holidayDetails.getRoomCount(),
        "room count did not match");
    CustomAssert.assertEquals(driver, testId, adults, holidayDetails.getAdultsCount(),
        "adults count did not match");
    CustomAssert.assertEquals(driver, testId, children, holidayDetails.getChildrenCount(),
        "children count did not match");

    return true;
  }

  private static void selectMonthAndYear(String testcaseId,
      HolidaySearchResultElements holidaySearchResultElements, String month, int year) {

    String pageMonth = (holidaySearchResultElements.calendarMonth().getText()).substring(0, 3);
    String pageYear = (holidaySearchResultElements.calendarYear().getText());
    while (!pageMonth.equals(month) && pageYear.equals(Integer.toString(year))) {
      holidaySearchResultElements.calendarNavigation().click();
    }
  }

  public static void scrollToBottom(HolidaySearchResultElements holidaySearchResultElements,
      WebDriver driver, String testId) {

    int holidayCount = 0;
    try {
      WebElement totalHolidayCount = holidaySearchResultElements.getTotalholidayCount();
      holidayCount = NumberUtils.toInt(totalHolidayCount.getText());
    } catch (Exception e) {
      CustomAssert.assertFail(testId, "No holidays load for this destination.");
      Log.divider(testId);
    }

    JavascriptExecutor js = (JavascriptExecutor) driver;

    int scrollCount = holidayCount / 5;
    int count = 1;

    while (count <= scrollCount) {
      WebElement scrolledElement = holidaySearchResultElements.getHolidayResultDiv(count * 5);
      js.executeScript("arguments[0].scrollIntoView(true);", scrolledElement);
      count++;
    }
  }
}
