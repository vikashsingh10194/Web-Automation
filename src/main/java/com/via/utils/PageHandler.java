package com.via.utils;

import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackMessage;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.via.pageobjects.common.PageElement;
import com.via.utils.Constant.VIA_COUNTRY;

@NoArgsConstructor
@AllArgsConstructor
public class PageHandler {
  protected String testId;
  protected WebDriver driver;

  public WebElement findElement(PageElement pageObject) {
    WebElement element = null;
    String locatorType = pageObject.getLocatorType();
    String locatorValue = pageObject.getLocatorValue();
    try {
      switch (locatorType) {
        case "id":
          element = driver.findElement(By.id(locatorValue));
          break;
        case "name":
          element = driver.findElement(By.name(locatorValue));
          break;
        case "className":
          element = driver.findElement(By.className(locatorValue));
          break;
        case "tagName":
          element = driver.findElement(By.tagName(locatorValue));
          break;
        case "linkTest":
          element = driver.findElement(By.linkText(locatorValue));
          break;
        case "partialLinkTest":
          element = driver.findElement(By.partialLinkText(locatorValue));
          break;
        case "cssSelector":
          element = driver.findElement(By.cssSelector(locatorValue));
          break;
        case "xpath":
          element = driver.findElement(By.xpath(locatorValue));
          break;
        default:
          break;
      }
      // Log.info(pageObject.getName() + " element was found in " +
      // webDriver.getTitle());
    } catch (Exception e) {
      Log.error(testId,
          pageObject.getName() + " element could not be found in " + driver.getTitle());
      throw (e);
    }

    CustomAssert.assertTrue(element != null, "element cannot be null");
    return element;
  }

  public WebElement findElement(RepositoryParser repositoryParser, String pageName,
      String objectName) {

    PageElement pageObject = repositoryParser.getPageObject(pageName, objectName);
    return findElement(pageObject);

  }

  public List<WebElement> findElements(RepositoryParser repositoryParser, String pageName,
      String objectName) {

    PageElement pageObject = repositoryParser.getPageObject(pageName, objectName);
    return findElements(pageObject);

  }

  public List<WebElement> findElements(PageElement pageObject) {
    List<WebElement> elements = null;
    String locatorType = pageObject.getLocatorType();
    String locatorValue = pageObject.getLocatorValue();

    try {
      switch (locatorType) {
        case "id":
          elements = driver.findElements(By.id(locatorValue));
          break;
        case "name":
          elements = driver.findElements(By.name(locatorValue));
          break;
        case "className":
          elements = driver.findElements(By.className(locatorValue));
          break;
        case "tagName":
          elements = driver.findElements(By.tagName(locatorValue));
          break;
        case "linkTest":
          elements = driver.findElements(By.linkText(locatorValue));
          break;
        case "partialLinkTest":
          elements = driver.findElements(By.partialLinkText(locatorValue));
          break;
        case "cssSelector":
          elements = driver.findElements(By.cssSelector(locatorValue));
          break;
        case "xpath":
          elements = driver.findElements(By.xpath(locatorValue));
          break;
        default:
          break;
      }
      // Log.info(pageObject.getName() + " elements were found in " +
      // webDriver.getTitle());
    } catch (Exception e) {
      Log.error(testId,
          pageObject.getName() + " elements could not be found in " + driver.getTitle());
      throw (e);
    }

    Assert.assertTrue(elements != null, "elements cannot be null");

    return elements;
  }

  public WebElement findElement(WebElement webElement, PageElement pageObject) {
    WebElement element = null;
    String locatorType = pageObject.getLocatorType();
    String locatorValue = pageObject.getLocatorValue();
    try {
      switch (locatorType) {
        case "id":
          element = webElement.findElement(By.id(locatorValue));
          break;
        case "name":
          element = webElement.findElement(By.name(locatorValue));
          break;
        case "className":
          element = webElement.findElement(By.className(locatorValue));
          break;
        case "tagName":
          element = webElement.findElement(By.tagName(locatorValue));
          break;
        case "linkTest":
          element = webElement.findElement(By.linkText(locatorValue));
          break;
        case "partialLinkTest":
          element = webElement.findElement(By.partialLinkText(locatorValue));
          break;
        case "cssSelector":
          element = webElement.findElement(By.cssSelector(locatorValue));
          break;
        case "xpath":
          element = webElement.findElement(By.xpath(locatorValue));
          break;
        default:
          break;
      }
      // Log.info(pageObject.getName() + " element was found in " +
      // webDriver.getTitle());
    } catch (Exception e) {
      Log.error(testId, pageObject.getName() + " element could not be found");
      throw (e);
    }

    Assert.assertTrue(element != null, "element cannot be null");
    return element;
  }

  public WebElement findElement(WebElement webElement, RepositoryParser repositoryParser,
      String pageName, String objectName) {

    PageElement pageObject = repositoryParser.getPageObject(pageName, objectName);
    return findElement(webElement, pageObject);

  }

  public List<WebElement> findElements(WebElement webElement, RepositoryParser repositoryParser,
      String pageName, String objectName) {

    PageElement pageObject = repositoryParser.getPageObject(pageName, objectName);
    return findElements(webElement, pageObject);

  }

  public List<WebElement> findElements(WebElement webElement, PageElement pageObject) {
    List<WebElement> elements = null;
    String locatorType = pageObject.getLocatorType();
    String locatorValue = pageObject.getLocatorValue();

    try {
      switch (locatorType) {
        case "id":
          elements = webElement.findElements(By.id(locatorValue));
          break;
        case "name":
          elements = webElement.findElements(By.name(locatorValue));
          break;
        case "className":
          elements = webElement.findElements(By.className(locatorValue));
          break;
        case "tagName":
          elements = webElement.findElements(By.tagName(locatorValue));
          break;
        case "linkTest":
          elements = webElement.findElements(By.linkText(locatorValue));
          break;
        case "partialLinkTest":
          elements = webElement.findElements(By.partialLinkText(locatorValue));
          break;
        case "cssSelector":
          elements = webElement.findElements(By.cssSelector(locatorValue));
          break;
        case "xpath":
          elements = webElement.findElements(By.xpath(locatorValue));
          break;
        default:
          break;
      }
      // Log.info(pageObject.getName() + " elements were found in " +
      // webDriver.getTitle());
    } catch (Exception e) {
      Log.error(testId, pageObject.getName() + " elements could not be found");
      throw (e);
    }

    Assert.assertTrue(elements != null, "elements cannot be null");

    return elements;
  }

  public static void javaScriptExecuterClick(WebDriver driver, WebElement element) {
    JavascriptExecutor js = (JavascriptExecutor) driver;
    js.executeScript("arguments[0].scrollIntoView(true);", element);
    js.executeScript("arguments[0].click();", element);
  }

  // Added to scrollDown untill element is not visible...
  public static void jSExecuterScrolldown(WebDriver driver, WebElement element) {
    JavascriptExecutor js = (JavascriptExecutor) driver;
    js.executeScript("arguments[0].scrollIntoView(true);", element);
  }

  public static Object executeJQuery(WebDriver driver, String query) {
    JavascriptExecutor js = (JavascriptExecutor) driver;
    Object result = js.executeScript(query);
    return result;
  }

  public void maximizeScreen(WebDriver driver) {
    java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Point position = new Point(0, 0);
    driver.manage().window().setPosition(position);
    Dimension maximizedScreenSize =
        new Dimension((int) screenSize.getWidth(), (int) screenSize.getHeight());
    driver.manage().window().setSize(maximizedScreenSize);
  }

  public WebDriver openBrowser(String browserName, String Url) {
    String envTestServer = System.getenv("TESTSERVER");
    if (envTestServer != null
        && (envTestServer.equals("jenkinserver") || envTestServer.equals("jenkinintlserver"))) {
      String path = "/home/jenkins/firebug-2.0.17.xpi";
      if (envTestServer.equals("jenkinintlserver")) {
        path = "/var/lib/jenkins/firebug-2.0.8.xpi";
      }
      if (System.getProperty("firefox.driver") != null) {
        path = System.getProperty("firefox.driver");
      }
      File file = new File(path);
      FirefoxProfile firefoxProfile = new FirefoxProfile();
      try {
        firefoxProfile.addExtension(file);
      } catch (Exception e) {
        // Log.error(driver, e.getMessage()); Scrren shot
        Log.error(e.getMessage());
      }

      // Avoid startup screen
      firefoxProfile.setPreference("extensions.firebug.currentVersion", "2.0b8");
      driver = new FirefoxDriver(firefoxProfile);
      System.out.println("browser opened");
    } else if (browserName.equalsIgnoreCase("firefox")) {

      // System.setProperty("webdriver.gecko.driver",Constant.FIREFOX_GECKODRIVER_PATH);
      // DesiredCapabilities capabilities = DesiredCapabilities.firefox();
      // capabilities.setCapability("marionette", false);
      //
      // driver=new FirefoxDriver(capabilities);
      driver = new FirefoxDriver();
    } else if (browserName.equalsIgnoreCase("chrome")) {
      String osName = System.getProperty("os.name");
      if (StringUtils.containsIgnoreCase(osName, "windows")) {
        System.setProperty("webdriver.chrome.driver", Constant.WINDOWS_CHROMEDRIVER_PATH);
      } else if (StringUtils.containsIgnoreCase(osName, "mac")) {
        System.setProperty("webdriver.chrome.driver", Constant.MAC_CHROMEDRIVER_PATH);
      }
      Map<String, Object> prefs = new HashMap<String, Object>();
      prefs.put("profile.default_content_setting_values.notifications", 2);
      ChromeOptions options = new ChromeOptions();
      options.setExperimentalOption("prefs", prefs);
      
      // Added to disable DNS Prefetch...Because ,not able to take screen
      // shot in case of Chrome.
      // and getting the error as "Timed out receiving message from renderer: 100.000"
      // DNS prefetching is an attempt to resolve domain names before a user tries to follow a link
      options.addArguments("--dns-prefetch-disable");
      
      
      // options.addArguments("--disable-extensions");
      driver = new ChromeDriver(options);
    } else if (browserName.equalsIgnoreCase("IE")) {
      System.setProperty("webdriver.ie.driver", Constant.IE_DRIVER_PATH);
      driver = new InternetExplorerDriver();
    }

    maximizeScreen(driver);
    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    driver.manage().window().maximize();
    //driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
    driver.get(Url);

    return driver;
  }

  // public static void takeScreenshot(WebDriver driver, String sTestCaseName) throws Exception {
  // try {
  // File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
  // FileUtils.copyFile(scrFile, new File(Constant.SCREENSHOTS_PATH + sTestCaseName + ".jpg"));
  // } catch (Exception e) {
  // Log.error(driver, sTestCaseName,
  // "Class Utils | Method takeScreenshot | Exception occured while capturing ScreenShot : "
  // + e.getMessage());
  // throw new Exception();
  // }
  // }

  public static void selectFromDropDown(WebElement options, String value) {
    Select dropDown = new Select(options);
    dropDown.selectByVisibleText(value);
  }

  public static String selectDropdownValueContains(String testId, WebElement options, String text) {
    Select dropDown = new Select(options);
    int index = 0;
    String selectedText = "";
    for (WebElement option : dropDown.getOptions()) {
      selectedText = option.getText();
      if (StringUtils.containsIgnoreCase(selectedText, text)) {
        dropDown.selectByIndex(index);
        return StringUtils.trimToEmpty(selectedText);
      }
      index++;
    }

    CustomAssert.assertFail(testId, "No option containing text: " + text);
    return null;
  }

  public static String getSelectedOptionText(WebElement options) {
    Select dropDown = new Select(options);
    String selectedText = dropDown.getFirstSelectedOption().getText();
    return StringUtils.trimToEmpty(selectedText);
  }

  public static String selectRandomOption(WebElement options) {
    Select dropDown = new Select(options);
    List<WebElement> optionList = dropDown.getOptions();
    int index = RandomValues.getRandomNumberBetween(1, optionList.size() - 1);
    String selectedText = optionList.get(index).getText();

    dropDown.selectByIndex(index);
    return StringUtils.trimToEmpty(selectedText);
  }

  public static void waitForPageLoad(String testId, WebDriver driver) {
    waitForDomLoad(testId, driver);

    int count = 0;
    while (true) {
      sleep(testId, 1000L);
      count++;

      if (count == 120) {
        CustomAssert.assertFail(testId, "Page Not Loaded in 2 minutes.");
      }
      WebElement loadPercent = driver.findElement(By.className("progressBar"));
      String loadAttr = loadPercent.getAttribute("style");
      if (StringUtils.isBlank(loadAttr) || StringUtils.contains(loadAttr, "opacity: 0")) {
        break;
      }
    }
    sleep(testId, 5 * 1000L);
  }

  public static void sleep(String testId, Long milliseconds) {
    try {
      Thread.sleep(milliseconds);
    } catch (Exception e) {
      CustomAssert.assertFail(testId, "Interrupt occured.");
    }
  }

  public static void takeScreenshot(WebDriver driver, String sTestCaseName) throws Exception {
    try {
      String scrFileName, dirName;
      String currDate = CalendarUtils.getCurrentDate(Constant.DD_MM_YYYY);
      dirName = Constant.SCREENSHOTS_PATH + currDate;
      File file = new File(dirName);
      if (!file.exists()) {
        file.mkdir();
      }
      sTestCaseName = sTestCaseName.replace("/", "");
      scrFileName =
          dirName + Constant.SLASH + sTestCaseName + "_"
              + CalendarUtils.getCurrentDateTime(Constant.DD_MM_YYYY_HH_MM_SS) + ".jpg";
      File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
      FileUtils.copyFile(scrFile, new File(scrFileName));
      Log.info(sTestCaseName, "Screen loaction is " + dirName);
    } catch (Exception e) {
      Log.error(sTestCaseName,
          "Class Utils | Method takeScreenshot | Exception occured while capturing ScreenShot : "
              + e.getMessage());
      throw new Exception();
    }
  }

  public static void waitForDomLoad(String testId, WebDriver driver) {
    ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
      public Boolean apply(WebDriver driver) {
        return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals(
            "complete");
      }
    };
    WebDriverWait wait = new WebDriverWait(driver, 40);
    wait.until(pageLoadCondition);
  }

  public static void setImplicitWaitTime(WebDriver driver, long time) {
    driver.manage().timeouts().implicitlyWait(time, TimeUnit.MILLISECONDS);
  }

  public static void setImplicitWaitTime(WebDriver driver) {
    driver.manage().timeouts().implicitlyWait(Constant.IMPLICIT_WAIT_TIME, TimeUnit.SECONDS);
  }

  // Set Explicit wait
  public static WebElement explicitWait(WebDriver driver, WebElement element) {
    WebDriverWait wait = new WebDriverWait(driver, 30);
    WebElement elementFound = wait.until(ExpectedConditions.visibilityOf(element));
    return elementFound;
  }

  // Set Fluent wait
  // public WebElement fluentWait(WebDriver driver,PageElement pageObject) {
  // Wait<WebDriver> wait=new FluentWait<WebDriver>(driver).withTimeout(30,
  // TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS);
  // WebElement element=wait.until(new Function<WebDriver, WebElement>() {
  //
  // @Override
  // public WebElement apply(WebDriver driver) {
  //
  // return findElement(pageObject);
  // }
  //
  // });
  // return element;
  // }
  public static void alertPopUp(WebDriver driver, String keyValue) {
    Alert alrt = driver.switchTo().alert();
    if (keyValue.equalsIgnoreCase("ok")) {
      alrt.accept();
    } else if (keyValue.equalsIgnoreCase("cancel")) {
      alrt.dismiss();
    }
  }

  public static boolean acceptAlert(String testId, WebDriver driver, String acceptedSubString) {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (StringUtils.containsIgnoreCase(alertText, acceptedSubString)) {
        alert.accept();
        return true;
      }
      CustomAssert.assertFail(testId, "Wrong alert Appeared : " + alertText);
    } catch (Exception e) {

    }
    return true;
  }

  public static void sendFailureMessageSlack(VIA_COUNTRY countryCode, String testSuite,
      String testId, String message) {
    String envTestServer = System.getenv("TESTSERVER");

    if (envTestServer != null) {
      return;
    }

    SlackApi api = new SlackApi(EntityMap.getSlackWebHookupUrl(countryCode));
    api.call(new SlackMessage("TestAutomation", "Test Suite: " + testSuite + "\nTest Failed: "
        + testId + "\n" + message));
  }

  /**
   * Modifies the elements locator value Replaces the string "toModify" in the locator value with
   * the list of strings passed.
   * 
   * @param repositoryParser Repository parser
   * @param pageName The objects page name
   * @param objectName The objects name
   * @param modification The modification
   * @return The modified page element
   */
  public static PageElement modifyPageElement(RepositoryParser repositoryParser, String pageName,
      String objectName, String... modificationStrings) {
    PageElement element = repositoryParser.getPageObject(pageName, objectName);
    PageElement modifiedElement = new PageElement(element);
    String locatorValue = modifiedElement.getLocatorValue();

    for (String modification : modificationStrings) {
      locatorValue = StringUtils.replaceOnce(locatorValue, "toModify", modification);
    }
    modifiedElement.setLocatorValue(locatorValue);
    return modifiedElement;
  }
}
