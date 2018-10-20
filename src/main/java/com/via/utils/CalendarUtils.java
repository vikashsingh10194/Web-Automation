package com.via.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.http.client.utils.DateUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.via.pageobjects.common.PageElement;
import com.via.utils.Constant.Traveller;

public class CalendarUtils {

  private final static String PAGE_NAME = "calendar";

  // Gets the pageObject and modifies the string "toModify" to the target
  // string in locator value
  public static PageElement modifyPageElementOnce(RepositoryParser repositoryParser,
      String objectName, String modification) {
    PageElement element = repositoryParser.getPageObject(PAGE_NAME, objectName);
    PageElement modifiedElement = new PageElement(element);
    String locatorValue = modifiedElement.getLocatorValue();
    String modifiedLocatorValue = StringUtils.replace(locatorValue, "toModify", modification);
    modifiedElement.setLocatorValue(modifiedLocatorValue);
    return modifiedElement;
  }

  public static WebElement getElementByPageObject(String testId, WebDriver driver,
      PageElement pageElement) {
    WebElement element = new PageHandler(testId, driver).findElement(pageElement);
    Assert.assertTrue(element != null);
    return element;
  }

  private static void setCalenderBlock(String testId, WebDriver driver,
      RepositoryParser repositoryParser, int currMonth, int currYear, int targetMonth,
      int targetYear, String calId) {
    WebElement blockShiftKey = null;

    // Total number of blocks to be shifted either backward or forward
    int blockCount = 0;
    blockCount += (targetYear - currYear) * 12 + (targetMonth - currMonth);

    while (blockCount != 0) {
      if (blockCount > 0) {
        blockShiftKey =
            getElementByPageObject(testId, driver,
                modifyPageElementOnce(repositoryParser, "calendarForwardKey", calId));
        blockCount--;
      } else {
        blockShiftKey =
            getElementByPageObject(testId, driver,
                modifyPageElementOnce(repositoryParser, "calendarBackwardKey", calId));
        blockCount++;
      }
      blockShiftKey.click();
    }
  }

  private static WebElement getMonthBoxDate(String testId, WebDriver driver,
      RepositoryParser repositoryParser, int targetDate, String calId) {

    WebElement element = null;
    WebElement monthBox = null;

    monthBox =
        getElementByPageObject(testId, driver,
            modifyPageElementOnce(repositoryParser, "monthBox", calId));

    // Iterating over all the dates in the box
    List<WebElement> rowTags = monthBox.findElements(By.className("vc-row"));
    for (WebElement row : rowTags) {
      List<WebElement> colTags = row.findElements(By.className("vc-cell"));
      for (WebElement date : colTags) {
        String classAttributeValue = date.getAttribute("class");
        if (!StringUtils.equals(classAttributeValue, "vc-cell vc-disabled-cell")
            && !StringUtils.equals(classAttributeValue, "vc-cell vc-head-cell")
            && (Integer.parseInt(date.getAttribute("data-date")) == targetDate)) {
          element = date;
          break;
        }
      }
      if (element != null)
        break;
    }

    return element;
  }

  private static String getCalenderBoxMonth(WebElement monthBoxHeader) {

    String month = monthBoxHeader.getAttribute("data-month");
    return month;
  }

  private static String getCalenderBoxYear(WebElement monthBoxHeader) {

    String year = monthBoxHeader.getAttribute("data-year");
    return year;
  }

  private static WebElement selectDate(String testId, WebDriver driver,
      RepositoryParser repositoryParser, Calendar calendar, WebElement monthBoxHeader, String calId) {

    WebElement element = null;

    int targetDate = calendar.get(Calendar.DATE);
    int targetMonth = calendar.get(Calendar.MONTH);
    int targetYear = calendar.get(Calendar.YEAR);

    int currMonth = Integer.parseInt(getCalenderBoxMonth(monthBoxHeader));
    int currYear = Integer.parseInt(getCalenderBoxYear(monthBoxHeader));

    setCalenderBlock(testId, driver, repositoryParser, currMonth, currYear, targetMonth,
        targetYear, calId);

    element = getMonthBoxDate(testId, driver, repositoryParser, targetDate, calId);

    return element;
  }

  public static WebElement selectDate(String testId, WebDriver driver,
      RepositoryParser repositoryParser, Calendar calendar, String calId) {
    WebElement monthBoxHeader =
        getElementByPageObject(testId, driver,
            modifyPageElementOnce(repositoryParser, "monthBoxHeader", calId));
    WebElement date = selectDate(testId, driver, repositoryParser, calendar, monthBoxHeader, calId);
    return date;
  }

  public static Calendar dateStringToCalendarDate(String dateString, String format)
      throws ParseException {
    dateString = StringUtils.replace(dateString, "Sept", "Sep");
    DateFormat dateFormat = new SimpleDateFormat(format);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(dateFormat.parse(dateString));
    return calendar;
  }

  public static Calendar getCalendarIncrementedByDays(int daysIncrement) {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DATE, daysIncrement);
    return calendar;
  }

  public static String calendarDateToDateString(Calendar calendar, String format) {
    DateFormat formatter = new SimpleDateFormat(format);

    formatter.setCalendar(calendar);
    // formatter.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));

    return formatter.format(calendar.getTime());
  }

  public static String getFormattedDateTime(Calendar calendar) {
    DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm");

    formatter.setCalendar(calendar);
    formatter.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));

    return formatter.format(calendar.getTime());
  }

  public static String getFormattedDateTimeExcludingYear(Calendar calendar) {
    DateFormat formatter = new SimpleDateFormat("dd MMM hh:mm");

    formatter.setCalendar(calendar);
    formatter.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));

    return formatter.format(calendar.getTime());
  }

  public static String getCurrentDate(String format) {
    return LocalDateTime.now().format(DateTimeFormatter.ofPattern(format));
  }

  public static String getFormattedDate(Calendar calendar) {
    return DateUtils.formatDate(calendar.getTime(), "dd MMM yyyy");
  }

  public static String getDateInFormat(Calendar calendar, String format) {
    DateFormat formatter = new SimpleDateFormat(format);
    formatter.setCalendar(calendar);
    formatter.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
    return formatter.format(calendar.getTime());
  }

  // set calendar c2 as same as calendar c1
  public static void setCalendar(Calendar c1, Calendar c2) {
    c1.set(c2.get(Calendar.YEAR), c2.get(Calendar.MONTH), c2.get(Calendar.DATE),
        c2.get(Calendar.HOUR), c2.get(Calendar.MINUTE));
  }

  // set calendar time to specific time
  public static void setCalendarTime(Calendar calendar, String time) {
    List<String> timeList = StringUtilities.split(time, ":");
    calendar.set(Calendar.HOUR, NumberUtils.toInt(timeList.get(0)));
    calendar.set(Calendar.MINUTE, NumberUtils.toInt(timeList.get(1)));
    calendar.set(Calendar.SECOND, 0);
  }

  public static boolean compareCalenderIgnoringYear(Calendar calendar1, Calendar calendar2) {
    if (calendar1.get(Calendar.DATE) != calendar2.get(Calendar.DATE)) {
      return false;
    } else if (calendar1.get(Calendar.MONTH) != calendar2.get(Calendar.MONTH)) {
      return false;
    } else if (calendar1.get(Calendar.HOUR) != calendar2.get(Calendar.HOUR)) {
      return false;
    } else if (calendar1.get(Calendar.MINUTE) != calendar2.get(Calendar.MINUTE)) {
      return false;
    } else {
      return true;
    }
  }

  public static String getDurationInTimeString(int duration) {
    int days = duration / (60 * 24);
    duration = duration % (60 * 24);
    int hours = duration / 60;
    int mins = duration % 60;
    return (days + "d " + hours + "h " + mins + "m");
  }

  public static String getFormattedTime(Calendar calendar) {
    DateFormat formatter = new SimpleDateFormat("HH:mm");
    formatter.setCalendar(calendar);
    formatter.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
    return formatter.format(calendar.getTime());
  }

  public static Calendar initializeRandomDOB(Traveller passengerType, Calendar onwardCalendar) {
    Calendar calendar = Calendar.getInstance();
    int year = 0;
    int month = onwardCalendar.get(Calendar.MONTH);
    int date = 1;
    if (passengerType == Traveller.ADULT) {
      year = calendar.get(Calendar.YEAR) - RandomValues.getRandomNumberBetween(14, 35);
      month = RandomValues.getRandomNumberBetween(0, 11);
      date = RandomValues.getRandomNumberBetween(1, 28);

    } else if (passengerType == Traveller.CHILD) {
      year = calendar.get(Calendar.YEAR) - RandomValues.getRandomNumberBetween(3, 10);
      month = RandomValues.getRandomNumberBetween(0, 11);
      date = RandomValues.getRandomNumberBetween(1, 28);
    } else {
      year = calendar.get(Calendar.YEAR) - 1;
    }
    calendar.set(year, month, date);
    return calendar;
  }
  
  
  public static Calendar getRandomDOB_New(String passengerType, Calendar onwardCalendar) {
    Calendar calendar = Calendar.getInstance();
    int year = 0;
    int month = onwardCalendar.get(Calendar.MONTH);
    int date = 1;
    if (StringUtils.contains(passengerType, "adt")) {
      year = calendar.get(Calendar.YEAR) - RandomValues.getRandomNumberBetween(14, 35);
      month = RandomValues.getRandomNumberBetween(0, 11);
      date = RandomValues.getRandomNumberBetween(1, 28);

    } else if (StringUtils.contains(passengerType, "chd")) {
      year = calendar.get(Calendar.YEAR) - RandomValues.getRandomNumberBetween(3, 10);
      month = RandomValues.getRandomNumberBetween(0, 11);
      date = RandomValues.getRandomNumberBetween(1, 28);
    } else {
      year = calendar.get(Calendar.YEAR) - 1;
    }
    calendar.set(year, month, date);
    return calendar;
  }

  public static Calendar getRandomDOB_OLD(String passengerType, Calendar onwardCalendar) {
    Calendar calendar = Calendar.getInstance();
    int year = 0;
    int month = onwardCalendar.get(Calendar.MONTH);
    int date = 1;
    if (StringUtils.contains(passengerType, "adult")) {
      year = calendar.get(Calendar.YEAR) - RandomValues.getRandomNumberBetween(14, 35);
      month = RandomValues.getRandomNumberBetween(0, 11);
      date = RandomValues.getRandomNumberBetween(1, 28);

    } else if (StringUtils.contains(passengerType, "child")) {
      year = calendar.get(Calendar.YEAR) - RandomValues.getRandomNumberBetween(3, 10);
      month = RandomValues.getRandomNumberBetween(0, 11);
      date = RandomValues.getRandomNumberBetween(1, 28);
    } else {
      year = calendar.get(Calendar.YEAR) - 1;
    }
    calendar.set(year, month, date);
    return calendar;
  }

  public static String getCurrentDateTime(String format) {
    return LocalDateTime.now().format(DateTimeFormatter.ofPattern(format));
  }

  public static String getCurrentTime(String hhMmSs) {
    DateFormat sdf = new SimpleDateFormat(hhMmSs);
    Calendar cal = Calendar.getInstance();
    return sdf.format(cal.getTime());
  }

}
