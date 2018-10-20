package com.via.reseller.appmodules.common;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.via.reseller.pageobjects.common.AgentLoginPageElements;
import com.via.utils.Constant;
import com.via.utils.Constant.VIA_COUNTRY;
import com.via.utils.CustomAssert;
import com.via.utils.Log;
import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;
import com.via.utils.StringUtilities;

/***
 * 
 * this class controls the login of an agent as user or administrator.
 *
 */
public class AgentLoginController {
  private AgentLoginPageElements loginPageObjects;
  private String testId;
  private RepositoryParser repositoryParser;
  private VIA_COUNTRY countryCode;
  private WebDriver driver;

  public AgentLoginController(WebDriver driver, RepositoryParser repositoryParser, String testId,
      VIA_COUNTRY countryCode) {
    loginPageObjects = new AgentLoginPageElements(testId, driver, repositoryParser);
    this.driver = driver;
    this.testId = testId;
    this.repositoryParser = repositoryParser;
    this.countryCode = countryCode;

    Log.info(testId, "::::::::::::::::          Agent Login Page         :::::::::::::::::");
    Log.divider(testId);
  }

  /*** agent login method ***/
  public boolean agentLogin(String userDetails) {
    String userType = "admin";

    List<String> userLoginDetails = StringUtilities.split(userDetails, Constant.UNDERSCORE);

    if (StringUtils.equalsIgnoreCase(userLoginDetails.get(0), "no")) {
      return false;
    }
    if (userLoginDetails.size() > 1) {
      userType = userLoginDetails.get(1);
    }
    boolean loginSuccess = false;

    if (StringUtils.equalsIgnoreCase("admin", userType)) {
      loginSuccess = loginAsAdministrator();
      userType = "Admin";
    } else {
      loginSuccess = loginAsUser();
      userType = "Desk User";
    }

    ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
      public Boolean apply(WebDriver driver) {
        return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals(
            "complete");
      }
    };
    WebDriverWait wait = new WebDriverWait(driver, 40);
    wait.until(pageLoadCondition);

    CustomAssert.assertTrue(testId, loginSuccess, "Successfully login as: " + userType,
        "Invalid Login credicential.");
    Log.divider(testId);

    try {
      WebElement pushNotificationCancel =
          driver.findElement(By.xpath("//button[@id='wzrk-cancel']"));
      PageHandler.javaScriptExecuterClick(driver, pushNotificationCancel);
    } catch (Exception e) {

    }

    return loginSuccess;
  }

  /*** login agent as user ***/
  private boolean loginAsUser() {

    /*** select the user option from drop down ***/
    // selectOption("User", loginPageObjects); // For Old Login Page
    selectOption("USER", loginPageObjects);// For New Login Page

    /*** get login id element and send login id to it ***/
    WebElement element = loginPageObjects.getLoginId();
    element.sendKeys(repositoryParser.getPropertyValue("agentAsUserLogin"));

    /*** get desk id element and send desk id to it ***/
    element = loginPageObjects.getDeskId();
    element.sendKeys(repositoryParser.getPropertyValue("agentAsUserDeskId"));

    /*** get password element and send password to it ***/
    element = loginPageObjects.getPassword();
    element.sendKeys(repositoryParser.getPropertyValue("agentAsUserPassword"));

    /*** find login button and click on it ***/
    element = loginPageObjects.getSignInButton();
    element.click();

    return isLoginSuccess();
  }

  private boolean isLoginSuccess() {

    try {
      loginPageObjects.getErrorMsg();
    } catch (NoSuchElementException ex) {
      return true;
    }
    return false;

    // WebDriverWait wait = new WebDriverWait(driver, 5);
    // wait.until(ExpectedConditions.alertIsPresent());
    //
    // try {
    // wait.until(ExpectedConditions.alertIsPresent());
    // Alert alert = driver.switchTo().alert();
    // Log.error(testId, alert.getText());
    // alert.accept();
    // } catch (TimeoutException e) {
    // try {
    // loginPageElement.getErrorMsg();
    // } catch(NoSuchElementException ex) {
    // return true;
    // }
    // return false;
    // }
    // return false;
  }

  /*** login agent as administrator ***/
  private boolean loginAsAdministrator() {
    /*** select the user option from drop down ***/
    // Removed in new Login page
    // loginPageObjects.loginButton().click();
    // try {
    // Thread.sleep(2 * 1000);
    // } catch (Exception e) {
    // Log.error("Interrupt occured.");
    // }

    // selectOption("Administrator", loginPageObjects);// For old login page
    selectOption("ADMIN", loginPageObjects); // For new login page

    /*** get login id element and send login id to it ***/
    WebElement element = loginPageObjects.getLoginId();
    element.sendKeys(repositoryParser.getPropertyValue(countryCode + "AgentAsAdminLogin"));

    /*** get password element and send password to it ***/
    element = loginPageObjects.getPassword();
    element.sendKeys(repositoryParser.getPropertyValue(countryCode + "AgentAsAdminPassword"));

    /*** find login button and click on it ***/
    element = loginPageObjects.getSignInButton();
    element.click();

    return isLoginSuccess();
  }

  /*** select the user type from selection drop down ***/
  private void selectOption(String userType, AgentLoginPageElements loginPageObjects) {
    WebElement selectElement = loginPageObjects.getUserType();
    Select dropDown = new Select(selectElement);
    dropDown.selectByVisibleText(userType);
  }

  public void signOut() {
    WebElement element = loginPageObjects.getSignOutButton();
    element.click();
    Log.info(testId + " Successfully signed out.");
  }
}
