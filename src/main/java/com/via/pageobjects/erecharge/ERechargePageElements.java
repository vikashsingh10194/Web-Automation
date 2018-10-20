package com.via.pageobjects.erecharge;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;

public class ERechargePageElements extends PageHandler {
  private RepositoryParser repositoryParser;

  private final String RECHARGE_PAGE_NAME = "eRechargeHome";
  private final String CUSTOMER_DETAILS_PAGE_NAME = "customerDetails";
  private final String COMMON_HEADER_PAGE = "commonPageHeader";

  public ERechargePageElements(String testId, WebDriver driver, RepositoryParser repositoryParser) {
  //  super(testId, driver);
    this.repositoryParser = repositoryParser;
  }

  public WebElement getPrepaidRecharge() {
    return findElement(repositoryParser, RECHARGE_PAGE_NAME, "eRecharge");
  }

  public WebElement getElectricityRecharge() {
    return findElement(repositoryParser, RECHARGE_PAGE_NAME, "pln");
  }

  public WebElement getWaterRecharge() {
    return findElement(repositoryParser, RECHARGE_PAGE_NAME, "pam");
  }

  public WebElement getPostpaidRecharge() {
    return findElement(repositoryParser, RECHARGE_PAGE_NAME, "postpaid");
  }

  public WebElement getInternetRecharge() {
    return findElement(repositoryParser, RECHARGE_PAGE_NAME, "mobileRecharge");
  }

  public WebElement getOperator() {
    return findElement(repositoryParser, RECHARGE_PAGE_NAME, "operator");
  }

  public WebElement getPlan() {
    return findElement(repositoryParser, RECHARGE_PAGE_NAME, "plan");
  }

  public WebElement getPlanFare() {
    return findElement(repositoryParser, RECHARGE_PAGE_NAME, "planFare");
  }

  public WebElement getPayableAmount() {
    return findElement(repositoryParser, RECHARGE_PAGE_NAME, "payableAmount");
  }

  public WebElement getBuyButton() {
    return findElement(repositoryParser, RECHARGE_PAGE_NAME, "buyBtn");
  }

  public WebElement getOperatorName() {
    return findElement(repositoryParser, COMMON_HEADER_PAGE, "operatorName");
  }

  public WebElement getPlanDetails() {
    return findElement(repositoryParser, COMMON_HEADER_PAGE, "productName");
  }

  public WebElement getAmount() {
    return findElement(repositoryParser, COMMON_HEADER_PAGE, "productPrice");
  }

  public WebElement getCustomerId(String pageName) {
    return findElement(repositoryParser, pageName, "customerId");
  }

  public WebElement getContactNo(String pageName) {
    return findElement(repositoryParser, pageName, "contactNo");
  }

  public WebElement getContactEmail(String pageName) {
    return findElement(repositoryParser, pageName, "contactEmail");
  }

  public WebElement getDenominationPrice() {
    return findElement(repositoryParser, CUSTOMER_DETAILS_PAGE_NAME, "denominationPrice");
  }

  public WebElement getTotalAmount() {
    return findElement(repositoryParser, CUSTOMER_DETAILS_PAGE_NAME, "amountToBePaid");
  }

  public WebElement getTermsCheckBox() {
    return findElement(repositoryParser, CUSTOMER_DETAILS_PAGE_NAME, "termsCondition");
  }

  public WebElement getProceedPayBtn() {
    return findElement(repositoryParser, CUSTOMER_DETAILS_PAGE_NAME, "proceedPayment");
  }
}
