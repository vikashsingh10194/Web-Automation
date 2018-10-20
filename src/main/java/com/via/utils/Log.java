package com.via.utils;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

public class Log {
  // Initialize Log4j logs
  private static Logger log = Logger.getLogger(Log.class.getName());

  // This is to print log for the beginning of the test case, as we usually
  // run so many test cases as a test suite
  public static void startTestCase(String sTestCaseName) {
    log.info("************************************************************************************");
    System.out.println("************************************************************************************");
    log.info("$$$$$$$$$$$$$$$$$$$              " + sTestCaseName + "           $$$$$$$$$$$$$$$$$$$");
    System.out.println("$$$$$$$$$$$$$$$$$$$              " + sTestCaseName + "           $$$$$$$$$$$$$$$$$$$");
    log.info("************************************************************************************");
    System.out.println("************************************************************************************");
  }

  // This is to print log for the ending of the test case
  public static void endTestCase(String sTestCaseName) {
    log.info("XXXXXXXXXXXXXXXXXXXXX            " + "-E---N---D-"
        + "            XXXXXXXXXXXXXXXXXXX");
    System.out.println("XXXXXXXXXXXXXXXXXXXXX            " + "-E---N---D-"
        + "            XXXXXXXXXXXXXXXXXXX");
    log.info("X");
    System.out.println("X");
    log.info("X");
    System.out.println("X");
    log.info("X");
    System.out.println("X");
  }

  public static void divider(String testId) {
    log.info(testId + "------------------------------------------------------------------");
    System.out.println(testId + "------------------------------------------------------------------");
  }

  // Need to create these methods, so that they can be called
  public static void info(String testId, String message) {
    log.info(testId + " " + message);
    System.out.println(testId + " " + message);
  }

  public static void warn(String testId, String message) {
    System.out.println("==============Warning==============");
    log.warn(testId + " " + message);
    System.out.println(testId + " " + message);
  }

  public static void error(String testId, String message) {
    System.out.println("==============Error==============");
    log.error(testId + " " + message);
    System.out.println(testId + " " + message);
  }
  
  //Overloaded version made for taking screen shot.
  public static void error(WebDriver driver, String testId, String message) {
    System.out.println("==============Error==============");
    log.error(testId + " " + message);
    System.out.println(testId + " " + message);
    /*try {
      PageHandler.takeScreenshot(driver, testId);
    } catch (Exception e) {
      Log.error(testId,
          "Class Utils | Method takeScreenshot | Exception occured while capturing ScreenShot : "
              + e.getMessage());
      System.out.println();
    }*/
  }

  public static void fatal(String testId, String message) {
    System.out.println("==============Fatal==============");
    log.fatal(testId + " " + message);
    System.out.println(testId + " " + message);
  }

  public static void debug(String testId, String message) {
    log.debug(testId + " " + message);
    System.out.println(testId + " " + message);
  }

  public static void info(String message) {
    log.info(message);
    System.out.println(message);
  }

  public static void warn(String message) {
    System.out.println("==============Warning==============");
    log.warn(message);
    System.out.println(message);
  }

  public static void error(String message) {
    System.out.println("==============Error==============");
    log.error(message);
    System.out.println(message);
  }

  //Overloaded version made for taking screen shot.
  public static void error(WebDriver driver, String message) {
    System.out.println("==============Error==============");
    log.error(message);
    System.out.println(message);
    /*try {
      PageHandler.takeScreenshot(driver, message);
    } catch (Exception e) {
      Log.error("Class Utils | Method takeScreenshot | Exception occured while capturing ScreenShot : "
          + e.getMessage());
      System.out.println();
    }*/
  }

  public static void fatal(String message) {
    System.out.println("==============Fatal==============");
    log.fatal(message);
    System.out.println(message);
  }

  public static void debug(String message) {
    System.out.println("==============Debug==============");
    log.debug(message);
    System.out.println(message);
  }
}
