package com.via.pageobjects.common;

import lombok.AllArgsConstructor;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;

@AllArgsConstructor
public class HomePage extends PageHandler {
  private RepositoryParser repositoryParser;

  private final String PAGE_NAME = "home";

  public HomePage(String testId, WebDriver driver, RepositoryParser repositoryParser) {
    super(testId, driver);
    this.repositoryParser = repositoryParser;
  }

  /*** get element of sign in menu ***/
  public WebElement getLoginSlider() {
    return findElement(repositoryParser, PAGE_NAME, "signInMenu");
  }
  
  /*** get element of via msg cross key of oman &  uae***/
  public WebElement crossForViaMsg() {
	    return findElement(repositoryParser, PAGE_NAME, "crossHomepageKey");
	  }


  /*** get sign up button element ***/
  public WebElement getSignUpButton() {
    return findElement(repositoryParser, PAGE_NAME, "signUp");
  }

  /*** get login email id box ***/
  public WebElement getEmailIdInputBox() {
    return findElement(repositoryParser, PAGE_NAME, "emailID");
  }

  /*** get password box element for login ***/
  public WebElement getPasswordBox() {
    return findElement(repositoryParser, PAGE_NAME, "password");
  }

  /*** get sign in button element ***/
  public WebElement getSignInButton() {
    return findElement(repositoryParser, PAGE_NAME, "signIn");
  }

  /*** get forgot password button element ***/
  public WebElement getForgotPasswordButton() {
    return findElement(repositoryParser, PAGE_NAME, "forgotPassword");
  }

  /*** get facebook login button element ***/
  public WebElement getFaceBookLoginButton() {
    return findElement(repositoryParser, PAGE_NAME, "faceBookLogin");
  }

  /*** get google+ login button element ***/
  public WebElement getGoogleLoginButton() {
    return findElement(repositoryParser, PAGE_NAME, "google+Login");
  }

  /*** get corporate login button element ***/
  public WebElement getCorporateLoginButton() {
    return findElement(repositoryParser, PAGE_NAME, "corporateLogin");
  }

  /*** get agent login button element ***/
  public WebElement getAgentLoginButton() {
    return findElement(repositoryParser, PAGE_NAME, "agentLogin");
  }

  public WebElement getAlertMsg() {
    return findElement(repositoryParser, PAGE_NAME, "alertMsg");
  }

  public WebElement getSingOutButton() {
    return findElement(repositoryParser, PAGE_NAME, "signOut");
  }

  public WebElement getUserMenu() {
    WebDriverWait wait = new WebDriverWait(driver, 5);
    return wait.until(ExpectedConditions.visibilityOf(findElement(repositoryParser, PAGE_NAME,
        "userMenu")));
  }

  public WebElement getLanguageSelector() {
    return findElement(repositoryParser, PAGE_NAME, "language");
  }

}
