package com.via.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CalendarUtilsBusesResaler {

  private final static String PAGE_NAME = "busesCalendar";

  private static void setCalenderBlock(String testId, WebDriver driver,
      RepositoryParser repositoryParser, int currMonth, int currYear, int targetMonth,
      int targetYear) {

    // Total number of blocks to be shifted either backward or forward
    int blockCount = (targetYear - currYear) * 12 + (targetMonth - currMonth);

    while (blockCount != 0) {
      new PageHandler(testId, driver).findElement(repositoryParser, PAGE_NAME,
          "calendarBlockRightShift").click();
      blockCount--;
    }
  }

  // Format -> Select Thursday, Aug 25, 2016
  private static String getDateString(Calendar targetCal) {
    // DateFormat formatter = new SimpleDateFormat("EEEE, MMM dd, YYYY");

    DateFormat formatter1 = new SimpleDateFormat("EEEE, MMM ");
    formatter1.setCalendar(targetCal);
    formatter1.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
    String month = formatter1.format(targetCal.getTime());

    String day = Integer.toString(targetCal.get(Calendar.DAY_OF_MONTH));

    DateFormat formatter2 = new SimpleDateFormat(", YYYY");
    formatter2.setCalendar(targetCal);
    formatter2.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
    String year = formatter2.format(targetCal.getTime());

    return month.concat(day).concat(year);
  }

  private static WebElement getMonthBoxDate(WebDriver driver, Calendar targetCal) {

    String dateString = getDateString(targetCal);
    String targetDateXpath = ".//*[@title='Select " + dateString + "']";
    return driver.findElement(By.xpath(targetDateXpath));
  }

  public static WebElement selectOnwardDate(String testId, WebDriver driver,
      RepositoryParser repositoryParser, Calendar targetCalendar) throws ParseException {

    // Retrieves target date details
    int targetMonth = targetCalendar.get(Calendar.MONTH);
    int targetYear = targetCalendar.get(Calendar.YEAR);

    // Retrieves current date details
    Calendar now = Calendar.getInstance();
    int currMonth = now.get(Calendar.MONTH);
    int currYear = now.get(Calendar.YEAR);

    // Clicks to get current date
    new PageHandler(testId, driver).findElement(repositoryParser, PAGE_NAME, "calendarBlockToday")
        .click();

    // Sets the calendar block to target date
    setCalenderBlock(testId, driver, repositoryParser, currMonth, currYear, targetMonth, targetYear);

    // Retrieves currrent date
    return getMonthBoxDate(driver, targetCalendar);
  }

  public static WebElement selectReturnDate(String testId, WebDriver driver,
      RepositoryParser repositoryParser, Calendar onwardCalendar, Calendar returnCalendar)
      throws ParseException {

    // Retrieves target date details
    int targetMonth = returnCalendar.get(Calendar.MONTH);
    int targetYear = returnCalendar.get(Calendar.YEAR);

    // Retrieves current date details
    Calendar now = (Calendar) onwardCalendar.clone();
    now.add(Calendar.DATE, 1);
    int currMonth = now.get(Calendar.MONTH);
    int currYear = now.get(Calendar.YEAR);

    // Sets the calendar block to target date
    setCalenderBlock(testId, driver, repositoryParser, currMonth, currYear, targetMonth, targetYear);

    // Retrieves currrent date
    return getMonthBoxDate(driver, returnCalendar);
  }

}
