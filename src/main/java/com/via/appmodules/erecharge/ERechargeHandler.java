package com.via.appmodules.erecharge;

import java.util.Map;

import lombok.AllArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.via.pageobjects.erecharge.ERechargePageElements;
import com.via.pageobjects.erecharge.RechargeDetails;
import com.via.testcases.common.TestCaseExcelConstant;
import com.via.utils.Constant.VIA_COUNTRY;
import com.via.utils.CustomAssert;
import com.via.utils.Log;
import com.via.utils.NumberUtility;
import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;

@AllArgsConstructor
public class ERechargeHandler {
  private String testId;
  private VIA_COUNTRY countryCode;
  private WebDriver driver;
  private RepositoryParser repositoryParser;

  private final String CONTACT_NO = "9611577993";
  private final String CONTACT_EMAIL = "qa@via.com";

  private final String CUSTOMER_DETAILS_PAGE_NAME = "customerDetails";
  private final String PAYMENT_PAGE_HEADER = "paymentPageHeader";

  public Double execute(Map<Integer, String> testData) {
    ERechargePageElements rechargeElements =
        new ERechargePageElements(testId, driver, repositoryParser);
    ((JavascriptExecutor) driver)
        .executeScript("document.getElementById('erechargeProductNav').click();");

    PageHandler.waitForDomLoad(testId, driver);

    RechargeDetails rechargeDetails = null;
    String rechargeType = testData.get(TestCaseExcelConstant.COL_RECHARGE_TYPE);
    if (StringUtils.equalsIgnoreCase(rechargeType, "prepaid")) {
      PageHandler.javaScriptExecuterClick(driver, rechargeElements.getPrepaidRecharge());
      rechargeDetails = operatorPlanFlow(rechargeElements, testData);
      setUserDetails(rechargeElements, rechargeDetails);
    } else if (StringUtils.equalsIgnoreCase(rechargeType, "pln")) {
      PageHandler.javaScriptExecuterClick(driver, rechargeElements.getElectricityRecharge());
      rechargeDetails = operatorPlanFlow(rechargeElements, testData);
      setUserDetails(rechargeElements, rechargeDetails);
    } else if (StringUtils.equalsIgnoreCase(rechargeType, "pam")) {
      PageHandler.javaScriptExecuterClick(driver, rechargeElements.getWaterRecharge());
      rechargeDetails = operatorFlow(rechargeElements, testData);
    } else if (StringUtils.equalsIgnoreCase(rechargeType, "postpaid")) {
      PageHandler.javaScriptExecuterClick(driver, rechargeElements.getPostpaidRecharge());
      rechargeDetails = operatorFlow(rechargeElements, testData);
    }

    else if (StringUtils.equalsIgnoreCase(rechargeType, "internet")) {
      PageHandler.javaScriptExecuterClick(driver, rechargeElements.getInternetRecharge());
      rechargeDetails = operatorFlow(rechargeElements, testData);
    }

    verifyPaymentHeaderDetails(rechargeElements, rechargeDetails);
    return rechargeDetails.getPrice();
  }

  private void verifyPaymentHeaderDetails(ERechargePageElements rechargeElements,
      RechargeDetails rechargeDetails) {
    verifyCommonDetails(rechargeElements, rechargeDetails);

    Log.info(testId, ":::::::::::::::::::::      Payment Page    :::::::::::::::::::::::");
    Log.divider(testId);

    Log.info(testId, "--------------     Payment Page Header Validation    -------------");

    String expectedCustId = rechargeDetails.getCustomerId();
    String actualCustId = rechargeElements.getCustomerId(PAYMENT_PAGE_HEADER).getText();

    CustomAssert.assertTrue(testId, StringUtils.equalsIgnoreCase(actualCustId, expectedCustId),
        "Customer Id didn't match. Expected : " + expectedCustId + " Actual : " + actualCustId);

    String expectedContactNo = rechargeDetails.getContactNo();
    String actualContactNo = rechargeElements.getContactNo(PAYMENT_PAGE_HEADER).getText();

    CustomAssert
        .assertTrue(testId, StringUtils.equals(actualContactNo, expectedContactNo),
            "Contact No didn't match. Expected : " + expectedContactNo + " Actual : "
                + actualContactNo);

    String expectedEmail = rechargeDetails.getContactEmail();
    String actualEmail = rechargeElements.getContactEmail(PAYMENT_PAGE_HEADER).getText();

    CustomAssert.assertTrue(testId, StringUtils.equalsIgnoreCase(expectedEmail, actualEmail),
        "Contact Email didn't match. Expected : " + expectedEmail + " Actual : " + actualEmail);

    Log.info(testId, "Payment Page Header Details verified.");
    Log.divider(testId);
  }

  public void verifyCommonDetails(ERechargePageElements rechargeElements,
      RechargeDetails rechargeDetails) {

    String actualOperator = rechargeElements.getOperatorName().getText();
    String expectedOperator = rechargeDetails.getOperator();

    CustomAssert.assertTrue(testId, StringUtils.equalsIgnoreCase(actualOperator, expectedOperator),
        "Operator Name didn't match. Expected : " + expectedOperator + " Actual : "
            + actualOperator);

    String actualPlan = rechargeElements.getPlanDetails().getText();
    String expectedPlan = rechargeDetails.getPlan();

    CustomAssert.assertTrue(testId, StringUtils.equalsIgnoreCase(actualPlan, expectedPlan),
        "Plan didn't match. Expected : " + expectedPlan + " Actual : " + actualPlan);

    Double actualAmount =
        NumberUtility.getAmountFromString(countryCode, rechargeElements.getAmount().getText()
            .replaceAll("[^0-9.]", ""));

    Double expectedAmount = rechargeDetails.getPrice();
    CustomAssert.assertTrue(
        testId,
        actualAmount.equals(expectedAmount),
        "Amount didn't match. Expected : "
            + NumberUtility.getRoundedAmount(countryCode, expectedAmount) + " Actual : "
            + NumberUtility.getRoundedAmount(countryCode, actualAmount));
  }

  private void setUserDetails(ERechargePageElements rechargeElements,
      RechargeDetails rechargeDetails) {

    Log.info(testId, "::::::::::::::::::         Customer Details Page        :::::::::::::::");
    Log.divider(testId);

    Log.info(testId, "------------    Customer Details Page Header Verification   -----------");

    verifyCommonDetails(rechargeElements, rechargeDetails);

    Log.info(testId, "Header Details verified.");
    Log.divider(testId);

    String customerId = rechargeDetails.getCustomerId();
    Log.info(testId, "Customer Id : " + customerId);
    WebElement custIdElement = rechargeElements.getCustomerId(CUSTOMER_DETAILS_PAGE_NAME);
    custIdElement.clear();
    custIdElement.sendKeys(customerId);

    WebElement contactNo = rechargeElements.getContactNo(CUSTOMER_DETAILS_PAGE_NAME);
    contactNo.clear();
    contactNo.sendKeys(CONTACT_NO);
    rechargeDetails.setContactNo(CONTACT_NO);

    WebElement contactEmail = rechargeElements.getContactEmail(CUSTOMER_DETAILS_PAGE_NAME);
    contactEmail.clear();
    contactEmail.sendKeys(CONTACT_EMAIL);
    rechargeDetails.setContactEmail(CONTACT_EMAIL);

    Double denominationPrice =
        NumberUtility.getAmountFromString(countryCode, rechargeElements.getDenominationPrice()
            .getText().replaceAll("[^0-9.]", ""));

    Double expectedAmount = rechargeDetails.getPrice();

    CustomAssert.assertTrue(
        testId,
        denominationPrice.equals(expectedAmount),
        "Denonination Price didn't match to plan Price. Expected : "
            + NumberUtility.getRoundedAmount(countryCode, expectedAmount) + " Actual: "
            + NumberUtility.getRoundedAmount(countryCode, denominationPrice));

    Double totalPayableAmount =
        NumberUtility.getAmountFromString(countryCode, rechargeElements.getTotalAmount().getText()
            .replaceAll("[^0-9.]", ""));

    rechargeDetails.setPrice(totalPayableAmount);

    Log.info(
        testId,
        "Total Amount to be paid : "
            + NumberUtility.getRoundedAmount(countryCode, totalPayableAmount));

    PageHandler.javaScriptExecuterClick(driver, rechargeElements.getTermsCheckBox());
    PageHandler.javaScriptExecuterClick(driver, rechargeElements.getProceedPayBtn());

    Log.divider(testId);
  }

  private RechargeDetails operatorFlow(ERechargePageElements rechargeElements,
      Map<Integer, String> testData) {
    CustomAssert.assertFail(testId, "Flow not available. Try after some times.");
    return null;
  }

  private RechargeDetails operatorPlanFlow(ERechargePageElements rechargeElements,
      Map<Integer, String> testData) {

    Log.info(testId, ":::::::::::::::::::          Recharge Home          ::::::::::::::::::");

    PageHandler.waitForDomLoad(testId, driver);

    RechargeDetails rechargeDetails = new RechargeDetails();

    String operator = testData.get(TestCaseExcelConstant.COL_RECHARGE_OPERATOR);
    WebElement operatorOptions = rechargeElements.getOperator();
    String operatorName =
        PageHandler.selectDropdownValueContains(testId, operatorOptions, operator);
    Log.info(testId, "Operator Name : " + operatorName);

    rechargeDetails.setOperator(operatorName);

    WebElement planOptions = rechargeElements.getPlan();
    String amount = testData.get(TestCaseExcelConstant.COL_RECHARGE_AMOUNT);
    String planName = PageHandler.selectDropdownValueContains(testId, planOptions, amount);

    Log.info(testId, "Plan Name : " + planName);
    rechargeDetails.setPlan(planName);

    String planFare = rechargeElements.getPlanFare().getText();
    CustomAssert.assertTrue(testId, StringUtils.equals(planName, planFare),
        "Plan Fare didn't match. Expected : " + amount + " Actual : " + planFare);

    Double payableAmount =
        NumberUtility.getAmountFromString(countryCode, rechargeElements.getPayableAmount()
            .getText().replaceAll("[^0-9.]", ""));

    rechargeDetails.setPrice(payableAmount);

    Log.info(testId,
        "Total Amount to be paid : " + NumberUtility.getRoundedAmount(countryCode, payableAmount));

    rechargeDetails.setCustomerId(testData.get(TestCaseExcelConstant.COL_RECHARGE_CUSTOMER_ID));

    PageHandler.javaScriptExecuterClick(driver, rechargeElements.getBuyButton());

    return rechargeDetails;
  }
}
