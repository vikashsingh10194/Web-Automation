package com.via.reseller.pageobjects.common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;

public class LoginPageElementsCorp extends PageHandler {

  private RepositoryParser repositoryParser;
  private final String PAGE_NAME = "corpLogin";

  public LoginPageElementsCorp(String testId, WebDriver driver, RepositoryParser repositoryParser) {
    super(testId, driver);
    this.repositoryParser = repositoryParser;
  }

  public WebElement login() {
    return findElement(repositoryParser, PAGE_NAME, "login");
  }

  public WebElement emailId() {
    return findElement(repositoryParser, PAGE_NAME, "emailId");
  }

  public WebElement password() {
    return findElement(repositoryParser, PAGE_NAME, "password");
  }

  public WebElement signIn() {
    return findElement(repositoryParser, PAGE_NAME, "signIn");
  }

  public WebElement deskUserLink() {
    return findElement(repositoryParser, PAGE_NAME, "deskUserLink");
  }

  public WebElement deskUserInput() {
    return findElement(repositoryParser, PAGE_NAME, "deskUser");
  }

  public WebElement getErrorMsg() {
    return findElement(repositoryParser, PAGE_NAME, "errorMessage");
  }

  public WebElement getSignOutButton() {
    return findElement(repositoryParser, PAGE_NAME, "logOut");
  }

}
