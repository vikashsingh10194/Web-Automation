package com.via.reseller.appmodules.common;

import java.util.List;

import jxl.biff.CountryCode;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.via.reseller.pageobjects.common.LoginPageElementsCorp;
import com.via.utils.Constant;
import com.via.utils.Constant.VIA_COUNTRY;
import com.via.utils.CustomAssert;
import com.via.utils.Log;
import com.via.utils.RepositoryParser;
import com.via.utils.StringUtilities;

public class LoginControllerCorp {
  private LoginPageElementsCorp loginPageObjects;
  private String testId;
  private VIA_COUNTRY countryCode;
  private RepositoryParser repositoryParser;

  public LoginControllerCorp(WebDriver driver, RepositoryParser repositoryParser, String testId,
      VIA_COUNTRY countryCode) {
    loginPageObjects = new LoginPageElementsCorp(testId, driver, repositoryParser);
    this.testId = testId;
    this.repositoryParser = repositoryParser;
    this.countryCode = countryCode;
  }

  public boolean agentLogin(String userDetails) {

    Log.info(testId, ":::::::::::::::::::       Login Page      :::::::::::::::::::");
    Log.divider(testId);
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

    CustomAssert.assertTrue(testId, loginSuccess, "Successfully login as: " + userType,
        "Invalid Login credicential.");
    Log.divider(testId);
    return loginSuccess;
  }

  /*** login agent as user ***/
  private boolean loginAsUser() {

    /*** get login id element and send login id to it ***/
    WebElement element = loginPageObjects.login();
    element.click();

    element = loginPageObjects.emailId();
    element.sendKeys(repositoryParser.getPropertyValue(countryCode + "Login"));

    element = loginPageObjects.deskUserLink();
    element.click();

    element = loginPageObjects.deskUserInput();
    element.sendKeys(repositoryParser.getPropertyValue(countryCode + "DeskUser"));

    /*** get password element and send password to it ***/
    element = loginPageObjects.password();
    element.sendKeys(repositoryParser.getPropertyValue(countryCode + "Password"));

    /*** find login button and click on it ***/
    element = loginPageObjects.signIn();
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
  }

  /*** login agent as administrator ***/
  private boolean loginAsAdministrator() {

    /*** get login id element and send login id to it ***/
    WebElement element = loginPageObjects.login();
    element.click();

    element = loginPageObjects.emailId();
    element.sendKeys(repositoryParser.getPropertyValue(countryCode + "Login"));

    /*** get password element and send password to it ***/
    element = loginPageObjects.password();
    element.sendKeys(repositoryParser.getPropertyValue(countryCode + "Password"));

    /*** find login button and click on it ***/
    element = loginPageObjects.signIn();
    element.click();

    return isLoginSuccess();
  }

  public void signOut() {
    WebElement element = loginPageObjects.getSignOutButton();
    element.click();
    Log.info(testId + " Successfully signed out.");
  }
}
