package com.via.utils;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;

public class ErrorPage {

  private static final String ERROR_504 = "Gateway Time-out";
  private static final String ERROR_503 = "Service Unavailable";
  private static final String ERROR_404 = "Not Found";
  private static final String ERROR_502 = "Bad Gateway";
  private static final String ERROR_500 = "Internal Server Error";

  public static void getErrorPageType(String testId, WebDriver driver) {
    String pageTitle = driver.getTitle();

    if (StringUtils.containsIgnoreCase(pageTitle, ERROR_404)) {
      CustomAssert.assertFail(testId, ERROR_404 + " occured.");
    }

    if (StringUtils.containsIgnoreCase(pageTitle, ERROR_500)) {
      CustomAssert.assertFail(testId, ERROR_500 + " occured.");
    }

    if (StringUtils.containsIgnoreCase(pageTitle, ERROR_502)) {
      CustomAssert.assertFail(testId, ERROR_502 + " occured.");
    }

    if (StringUtils.containsIgnoreCase(pageTitle, ERROR_503)) {
      CustomAssert.assertFail(testId, ERROR_503 + " occured.");
    }

    if (StringUtils.containsIgnoreCase(pageTitle, ERROR_504)) {
      CustomAssert.assertFail(testId, ERROR_504 + " occured.");
    }

  }
}
