package com.via.utils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CalendarUtilsReseller {

  private final static String PAGE_NAME = "calendar";

  private static void setCalenderBlock(String testId, WebDriver driver,
      RepositoryParser repositoryParser, int currMonth, int currYear, int targetMonth,
      int targetYear) {
    WebElement blockShiftKey = null;
    PageHandler commonUtils = new PageHandler(testId, driver);

    // Total number of blocks to be shifted either backward or forward
    int blockCount = 0;
    blockCount += (targetYear - currYear) * 12 + (targetMonth - currMonth);

    while (blockCount != 0) {
      if (blockCount > 0) {
        blockShiftKey =
            commonUtils.findElement(repositoryParser, PAGE_NAME, "calendarBlockRightShift");

        blockCount--;
      } else {
        blockShiftKey =
            commonUtils.findElement(repositoryParser, PAGE_NAME, "calendarBlockLeftShift");

        blockCount++;
      }
      blockShiftKey.click();
    }
  }

  private static WebElement getMonthBoxDate(WebElement calendarBox, int targetDate) {

    WebElement element = null;
    WebElement monthBox = calendarBox.findElement(By.tagName("tbody"));

    // Iterating over all the dates in the box
    List<WebElement> rowTags = monthBox.findElements(By.tagName("tr"));
    for (WebElement row : rowTags) {
      List<WebElement> colTags = row.findElements(By.tagName("td"));
      for (WebElement date : colTags) {
        try {
          // WebElement selector =
          // date.findElement(By.className("selector"));
          boolean isSelectable =
              StringUtils.containsIgnoreCase(date.getAttribute("class"), "selectable");
          if (isSelectable && Integer.parseInt(date.getText()) == targetDate) {
            element = date;
            break;
          }
        } catch (Exception e) {
          continue;
        }
      }
      if (element != null)
        break;
    }

    return element;
  }

  private static int getCalenderBoxMonth(WebElement monthBoxHeader) throws ParseException {

    String headerString = monthBoxHeader.getText();
    List<String> headerTextList = StringUtilities.split(headerString, Constant.WHITESPACE);
    String month = headerTextList.get(0);

    Calendar monthCal = CalendarUtils.dateStringToCalendarDate(month.substring(0, 3), "MMM");
    int monthIndex = monthCal.get(Calendar.MONTH);
    return monthIndex;
  }

  private static String getCalenderBoxYear(WebElement monthBoxHeader) {

    String headerString = monthBoxHeader.getText();
    List<String> headerTextList = StringUtilities.split(headerString, Constant.WHITESPACE);
    String year = headerTextList.get(1);
    return year;
  }

  private static WebElement selectDate(String testId, WebDriver driver,
      RepositoryParser repositoryParser, Calendar targetCalendar, WebElement calendarBlock)
      throws ParseException {

    WebElement element = null;

    int targetDate = targetCalendar.get(Calendar.DATE);
    int targetMonth = targetCalendar.get(Calendar.MONTH);
    int targetYear = targetCalendar.get(Calendar.YEAR);

    WebElement header = calendarBlock.findElement(By.xpath("//thead/tr[1]/th/div"));

    int currMonth = getCalenderBoxMonth(header);
    int currYear = Integer.parseInt(getCalenderBoxYear(header));

    setCalenderBlock(testId, driver, repositoryParser, currMonth, currYear, targetMonth, targetYear);
    Log.info("Target Calender Box Selected");

    element = getMonthBoxDate(calendarBlock, targetDate);
    Log.info("Target Date Selected");

    return element;
  }

  public static WebElement selectDepartureDate(String testId, WebDriver driver,
      RepositoryParser repositoryParser, String productCalendarId, Calendar calendar)
      throws ParseException {

    String departureCalId = productCalendarId.concat("0_0");
    WebElement departureCal = driver.findElement(By.id(departureCalId));

    WebElement departureDate = selectDate(testId, driver, repositoryParser, calendar, departureCal);

    return departureDate;
  }

  public static WebElement selectReturnDate(String testId, WebDriver driver,
      RepositoryParser repositoryParser, String productCalendarId, Calendar calendar)
      throws ParseException {
    String returnCalId = productCalendarId.concat("1_0");
    WebElement returnCal = driver.findElement(By.id(returnCalId));

    WebElement returnDate = selectDate(testId, driver, repositoryParser, calendar, returnCal);

    return returnDate;
  }

}
