package com.via.reseller.pageobjects.common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;

/***
 * 
 * this class return all the page elements present in agent login page
 *
 */
public class AgentLoginPageElements extends PageHandler {
  private RepositoryParser repositoryParser;
  private final String PAGE_NAME = "agentLogin";

  public AgentLoginPageElements(String testId, WebDriver driver, RepositoryParser repositoryParser) {
    super(testId, driver);
    this.repositoryParser = repositoryParser;
  }

  // On New B2B page remove Login Panel
  // public WebElement loginButton(){
  // return findElement(repositoryParser, PAGE_NAME, "loginButton_Old");
  // }

  // Used for Old login page
  // public WebElement getUserType() {
  // return findElement(repositoryParser, PAGE_NAME, "userType_Old");

  // }

  // Used for Old login page
  // public WebElement getLoginId() {
  // return findElement(repositoryParser, PAGE_NAME, "loginId_Old");
  // }

  // Used for Old login page
  // public WebElement getPassword() {
  // return findElement(repositoryParser, PAGE_NAME, "password_Old");
  // }

  // Used for Old login page
  // public WebElement getDeskId() {
  // return findElement(repositoryParser, PAGE_NAME, "deskId_Old");
  // }
  // public WebElement getSignInButton() {
  // return findElement(repositoryParser, PAGE_NAME, "signIn_Old");
  // }

  /** Used for New login page **/

  public WebElement getUserType() {
    return findElement(repositoryParser, PAGE_NAME, "userType_New");
  }

  public WebElement getLoginId() {
    return findElement(repositoryParser, PAGE_NAME, "loginId_New");
  }

  public WebElement getPassword() {
    return findElement(repositoryParser, PAGE_NAME, "password_New");
  }

  public WebElement getDeskId() {
    return findElement(repositoryParser, PAGE_NAME, "deskId_New");
  }

  // Used for New login page

  public WebElement getSignInButton() {
    return findElement(repositoryParser, PAGE_NAME, "signIn_New");
  }

  /*** get error message div ***/
  public WebElement getErrorMsg() {
    WebElement element = driver.findElement(By.xpath("//div[@class='error-messages']"));
    return element;
  }

  public WebElement getSignOutButton() {
    return findElement(repositoryParser, PAGE_NAME, "logOut");
  }
}
