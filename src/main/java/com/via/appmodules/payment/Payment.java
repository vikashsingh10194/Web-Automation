package com.via.appmodules.payment;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.via.pageobjects.common.PageElement;
import com.via.pageobjects.flights.common.FlightBookingDetails;
import com.via.pageobjects.payment.PaymentElements;
import com.via.pageobjects.trains.TrainBookingDetails;
import com.via.testcases.common.TestCaseExcelConstant;
import com.via.utils.Constant;
import com.via.utils.Constant.BOOKING_MEDIA;
import com.via.utils.Constant.Flight;
import com.via.utils.Constant.VIA_COUNTRY;
import com.via.utils.CustomAssert;
import com.via.utils.EntityMap;
import com.via.utils.Log;
import com.via.utils.NumberUtility;
import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;
import com.via.utils.StringUtilities;

public class Payment {
	
  private static final String VENDOR_JETCO = "VENDOR_JETCO";
  private static final String BANK = "BANK";
  private static final String MASTERPASS = "MASTERPASS";
  private static final String ATM = "ATM";
  private static final String INTERNETBANKING = "IB";
  private final String CITRUS_WALLET = "CITRUS";
  private final String FREECHARGE_WALLET = "FREECHARGE";
  private final String PAYUMONEY_WALLET = "PAYUMONEY";
  private final String SBI_WALLET = "SBI";
  private final String ICICI_BANK = "ICICI";
  private final String HDFC_BANK = "HDFC";
  private final String AXIS_BANK = "AXIS";
  private final String KOTAK_BANK = "KOTAK";
  private final String SBI_BANK = "SBI";
  private final String SAVED_CARD = "SAVEDCARD";
  private final String BANKING123 = "123BANKING";
  private final String CREDIT_CARD = "CreditCard";
  private final String NETBANKING = "NETBANKING";
  private final String CREDITCARD = "CREDITCARD";
  private final String DEBITCARD = "DEBITCARD";
  private final String WALLET = "WALLET";
  private final String EMI = "EMI";
  private final String NAME = "CreditCardName";
  private final String VALIDITY_MONTH = "CreditCardValidityMonth";
  private final String VALIDITY_YEAR = "CreditCardValidityYear";
  private final String CVV = "CreditCardCvv";
  
  
  private String paymentType;
  private String paymentMode;

  private String testId;
  private VIA_COUNTRY countryCode;
  private WebDriver driver;
  private RepositoryParser repositoryParser;
  private boolean bookingVerification = false;

  // Added for taking different credit card details
  private CreditCardDetails cardDetails;
  private static int index = 0;

  public Payment(String testId, VIA_COUNTRY countryCode, WebDriver driver,
      RepositoryParser repositoryParser) {
    this.testId = testId;
    this.countryCode = countryCode;
    this.driver = driver;
    this.repositoryParser = repositoryParser;
  }

  public Map<String, String> execute(WebDriver driver, double paymentfare,
      Map<Integer, String> flightTestCase) throws InterruptedException {
    if (flightTestCase.get(TestCaseExcelConstant.COL_PAYMENT_TYPE) != null) {
      paymentType = flightTestCase.get(TestCaseExcelConstant.COL_PAYMENT_TYPE);
    }
    if (flightTestCase.get(TestCaseExcelConstant.COL_PAYMENT_MODE) != null) {
      paymentMode = flightTestCase.get(TestCaseExcelConstant.COL_PAYMENT_MODE).toUpperCase();
    }
    if (paymentMode.equals(CREDITCARD) || paymentMode.equals(SAVED_CARD)
        || paymentMode.equalsIgnoreCase(DEBITCARD) ) {
      cardDetails = CreditCardDetails.cardDetails(repositoryParser, index++, paymentType);
    }
    PaymentElements paymentPage = new PaymentElements(driver, repositoryParser, testId);

    Log.info(testId, "::::::::::::::::         Payment Page        :::::::::::::::");
    Log.divider(testId);
    String fareWithConvenience = getPaymentMode(driver, paymentfare, paymentPage);

    Map<String, String> bookingDetails = new HashMap<String, String>();

    bookingDetails.put(Constant.AMOUNT_WITH_CONV, fareWithConvenience);

    if (bookingVerification) {
      bookingDetails.put(Constant.BOOKING_VERIFICATION, "true");
    } else {
      bookingDetails.put(Constant.BOOKING_VERIFICATION, "false");
    }

    return bookingDetails;
  }

  private String getPaymentMode(WebDriver driver, double paymentFare, PaymentElements paymentPage) {
    String fareWithConvenience = null;
    switch (paymentMode) {
    // select Saved Card option
      case SAVED_CARD:
        if (paymentPage.selectSavedCardPaymentMode() != null) {
          PageHandler.javaScriptExecuterClick(driver, paymentPage.selectSavedCardPaymentMode());
          Log.info(testId, "saved card Payment Mode Selected");
          fareWithConvenience = savedCardInput(repositoryParser, paymentPage);
        } else {
          Log.info(testId, "No Saved card is added");
        }
        break;
      // 123 Banking Mode added for country Thiland...
      case BANKING123:
        PageHandler.javaScriptExecuterClick(driver, paymentPage.select123BankingMode());
        PageHandler.javaScriptExecuterClick(driver,
            paymentPage.select123BankingPaymentType(paymentType));
        Log.info(testId, "123 Banking Mode Selected" + " payment type ---> " +paymentType);
        fareWithConvenience = select123BankingPayment(repositoryParser, paymentPage);
        break;
      // Select NetBanking...
      case NETBANKING:
        PageHandler.javaScriptExecuterClick(driver, paymentPage.selectNetBankingPaymentMode());
        Log.info(testId, "Netbanking selected");
        fareWithConvenience = selectPaymentBankName(paymentType, paymentPage);
        break;
      // select credit card option
      case CREDITCARD:
        PageHandler.javaScriptExecuterClick(driver, paymentPage.selectCreditCardPaymentMode());
        Log.info(testId, "CreditCard selected");
        fareWithConvenience = creditCardInput(testId, repositoryParser, paymentType, paymentPage);
        break;
      case DEBITCARD:
        // select debit card option
        PageHandler.javaScriptExecuterClick(driver, paymentPage.selectDebitCardPaymentMode());
        Log.info(testId, "Debitcard selected");
        fareWithConvenience = creditCardInput(testId, repositoryParser, paymentType, paymentPage);
        break;
      case WALLET:
        // select wallet option to pay.
        PageHandler.javaScriptExecuterClick(driver, paymentPage.selectWalletPaymentMode());
        Log.info(testId, "Wallet selected");
        fareWithConvenience = selectPaymentWallet(driver, paymentFare, paymentType, paymentPage);
        break;
      case EMI:
        // select emi option to pay.
        PageHandler.javaScriptExecuterClick(driver, paymentPage.selectEmiPaymentMode());
        Log.info(testId, "Emi mode selected");
        fareWithConvenience = selectEmi(paymentType, paymentPage);
        break;
      // Hongkong payment mode
      case VENDOR_JETCO:
        fareWithConvenience = proceedToPay(paymentPage);
        break;
      // BankTransfer payment mode of ID-Kishor
      case BANK:
        fareWithConvenience = bankTransferPayMode(paymentPage);
        break;
      // Atm payment mode of ID-Kishor
      case ATM:
        fareWithConvenience = atmPayMode(paymentPage);
        break;
      // internetBanking payment Mode of ID-Kishor
      case INTERNETBANKING:
        fareWithConvenience = internetBanking(paymentPage);
        break;
      // masterPass payment mode of Uae-Kishor
      case MASTERPASS:
        fareWithConvenience = masterPassPayMode(paymentPage);
        break;

    }
    getUrl(testId, driver);
    Log.info(testId, "FinalPayment button is clicked");
    return fareWithConvenience;
  }

  private String getCreditCardType(RepositoryParser repositoryParser, String creditCardType) {
    // get exact credit card number based on the type
    String getCreditCardNumber = CREDIT_CARD + creditCardType;
    return repositoryParser.getPropertyValue(getCreditCardNumber);
  }

  // click on the bank required.
  private String selectPaymentBankName(String paymentType, PaymentElements paymentPage) {

    if (paymentType.contains(AXIS_BANK)) {
      PageHandler.javaScriptExecuterClick(driver, paymentPage.netBankingSelectAxisBank());
      Log.info(testId, "Axis Bank netbanking is selected");
    } else if (paymentType.contains(HDFC_BANK)) {
      PageHandler.javaScriptExecuterClick(driver, paymentPage.netBankingSelectHdfcBank());
      Log.info(testId, "Hdfc Bank netbanking is selected");
    } else if (paymentType.contains(KOTAK_BANK)) {
      PageHandler.javaScriptExecuterClick(driver, paymentPage.netBankingSelectKotakBank());
      Log.info(testId, "Kotak Bank netbanking is selected");
    } else if (paymentType.contains("PNB")) {
      PageHandler.javaScriptExecuterClick(driver, paymentPage.netBankingSelectPnbBank());
      Log.info(testId, "Pnb Bank netbanking is selected");
    } else if (paymentType.contains(SBI_BANK)) {
      PageHandler.javaScriptExecuterClick(driver, paymentPage.netBankingSelectSbiBank());
      Log.info(testId, "Sbi Bank netbanking is selected");
    } else if (paymentType.contains(ICICI_BANK)) {
      PageHandler.javaScriptExecuterClick(driver, paymentPage.netBankingSelectIciciBank());
      Log.info(testId, "ICICI Bank netbanking is selected");
    } else {
      Select select = new Select(paymentPage.netBankingBankSelector());
      select.selectByVisibleText(EntityMap.getBankName(paymentType));
      Log.info(testId, paymentType + "Bank is selected");
    }

    PageHandler.sleep(testId, 2 * 1000L);

    String fareWithConvenience = paymentPage.totalFareAmountWithConvenience().getText();

    PageHandler.javaScriptExecuterClick(driver, paymentPage.finalPaySubmit());
    PageHandler.waitForDomLoad(testId, driver);

    return fareWithConvenience;
  }

  // make payment through Saved card option...
  private String savedCardInput(RepositoryParser repositoryParser, PaymentElements paymentPage) {
    // String cCNo = (String) getCreditCardType(repositoryParser,
    // paymentType);
    Select select = new Select(paymentPage.selectSavedCardDropDown());
    String cCNo = cardDetails.getCCNo();

    List<WebElement> options = select.getOptions();
    for (WebElement option : options) {
      if (option.getText().contains(cCNo.substring(12, 16))) {
        select.selectByVisibleText("XXXX-XXXX-XXXX-" + cCNo.substring(12, 16) + " " + paymentType);
        Log.info(testId, cCNo + " Is selected");;
      }
    }
    Log.info(testId, "credit card number is selected");

    // paymentPage.selectSavedCVV().sendKeys(repositoryParser.getPropertyValue(CVV));
    paymentPage.selectSavedCVV().sendKeys(cardDetails.getCCCVVNo());
    Log.info(testId, "credit card cvv number is entered");

    String fareWithConvenience = paymentPage.totalFareAmountWithConvenience().getText();

    PageHandler.javaScriptExecuterClick(driver, paymentPage.finalPaySubmit());

    PageHandler.sleep(testId, 5 * 1000L);

    if (StringUtils.equalsIgnoreCase(paymentType, "Master")) {
      PageHandler.sleep(testId, 15 * 1000L);

      // Check OTPPage Open or not
      if (checkOTPPage(paymentPage)) {
        paymentPage.getCardCancelBtn().click();
        PageHandler.acceptAlert(testId, driver, "Transaction Declined");
      }
    } else if (countryCode == VIA_COUNTRY.PH) {
      try {
        WebElement cancelVisa = driver.findElement(By.tagName("a"));
        PageHandler.javaScriptExecuterClick(driver, cancelVisa);
      } catch (Exception e) {

      }
    }

    if (!StringUtils.equalsIgnoreCase(paymentType, "AMEX")) {
      bookingVerification = true;
    }

    PageHandler.waitForDomLoad(testId, driver);
    PageHandler.sleep(testId, 4 * 1000L);

    return fareWithConvenience;
  }

  // make payment through 123Banking and proceed..
  private String select123BankingPayment(RepositoryParser repositoryParser,
      PaymentElements paymentPage) {
    PageHandler.sleep(testId, 2 * 1000L);

    String fareWithConvenience = paymentPage.totalFareAmountWithConvenience().getText();

    PageHandler.javaScriptExecuterClick(driver, paymentPage.finalPaySubmit());

    PageHandler.sleep(testId, 2 * 1000L);
    paymentPage.get123BankingCancelBtn().click();
    PageHandler.acceptAlert(testId, driver, "Transaction Declined");
    return fareWithConvenience;
  }

  // click on the payment wallet required
  private String selectPaymentWallet(WebDriver driver, double paymentFare, String paymentType,
      PaymentElements paymentPage) {
    DecimalFormat df = new DecimalFormat("#.#");
    df.setMinimumFractionDigits(1);
    String actFare = null;
    if (paymentType.contains(CITRUS_WALLET)) {
      PageHandler.javaScriptExecuterClick(driver, paymentPage.selectCitruswallet());
      Log.info(testId, "Citrus wallet is selected");
      actFare = walletValidation(paymentType, paymentFare, paymentPage);
      String citrusPaymentAmount = paymentPage.citursWalletAmount().getText();
      Log.info(testId, "citrus payment amount retrieved");
      CustomAssert.assertEquals(driver, testId, df.format(Double.parseDouble(citrusPaymentAmount)),
          actFare, "actual fare and expected wallet fare did not match" + citrusPaymentAmount + ":"
              + actFare);
      Log.info(testId, Double.parseDouble(citrusPaymentAmount) + ":Act vs Expected:" + actFare);
      PageHandler.javaScriptExecuterClick(driver, paymentPage.getCitrusCancelBtn());
      bookingVerification = true;

    } else if (paymentType.contains(FREECHARGE_WALLET)) {
      PageHandler.javaScriptExecuterClick(driver, paymentPage.selectFreeChargeWallet());
      Log.info(testId, "Free Charge wallet is selected");

      actFare = walletValidation(paymentType, paymentFare, paymentPage);
      String freeChargePaymentAmount = paymentPage.freeChargeWalletFinalFare().getText();
      String freeChargePaymentFareAmount = freeChargePaymentAmount.split(" ")[1];
      Log.info(testId, "Free charge payment amount retrieved");
      CustomAssert.assertEquals(driver, testId,
          df.format(Double.parseDouble(freeChargePaymentFareAmount)), actFare,
          "actual fare and expected wallet fare did not match" + freeChargePaymentFareAmount + ":"
              + actFare);
      Log.info(testId, Double.parseDouble(freeChargePaymentFareAmount) + ":Act vs Expected:"
          + actFare);

      PageHandler.javaScriptExecuterClick(driver, paymentPage.getFreeChargeCancelBtn());
      bookingVerification = true;
    } else if (paymentType.contains(PAYUMONEY_WALLET)) {
      PageHandler.javaScriptExecuterClick(driver, paymentPage.selectPayUmoneyWallet());
      actFare = walletValidation(paymentType, paymentFare, paymentPage);
      String payUMoneyPaymentAmount = paymentPage.payUMoneyWalletFare().getText();
      Log.info(testId, "Pay U money payment amount retrieved");
      CustomAssert.assertEquals(driver, testId,
          df.format(Double.parseDouble((payUMoneyPaymentAmount))), actFare,
          "actual fare and expected wallet fare did not match" + payUMoneyPaymentAmount + ":"
              + actFare);
      Log.info(testId, Double.parseDouble(payUMoneyPaymentAmount) + ":Act vs Expected:" + actFare);
      PageHandler.javaScriptExecuterClick(driver, paymentPage.getPayUCancelBtn());
      bookingVerification = true;
    } else if (paymentType.contains(SBI_WALLET)) {
      PageHandler.javaScriptExecuterClick(driver, paymentPage.selectSbiBuddyWallet());
      Log.info(testId, "Sbi Buddy wallet is selected");
      actFare = walletValidation(paymentType, paymentFare, paymentPage);
      String sbiPaymentAmount = paymentPage.sbiWalletFare().getText();
      Log.info(testId, "Sbi payment amount retrieved");
      CustomAssert.assertEquals(driver, testId, df.format(Double.parseDouble(sbiPaymentAmount)),
          actFare, "actual fare and expected wallet fare did not match" + sbiPaymentAmount + ":"
              + actFare);
      Log.info(testId, Double.parseDouble(sbiPaymentAmount) + ":Act vs Expected:" + actFare);
      PageHandler.javaScriptExecuterClick(driver, paymentPage.getSbiBuddyCancelBtn());
      bookingVerification = true;
    }

    PageHandler.waitForDomLoad(testId, driver);
    return actFare;
  }

  // Emi option
  private String selectEmi(String paymentType, PaymentElements paymentPage) {
    List<String> emiDetails = StringUtilities.split(paymentType, "_");
    String bankName = emiDetails.get(0);
    String emiTenure = emiDetails.get(1);
    String creditCardType = emiDetails.get(2);

    String totalFareAmount = null;

    if (StringUtils.contains(bankName, ICICI_BANK)) {
      PageHandler.javaScriptExecuterClick(driver, paymentPage.selectEmiICICIBank());
      Log.info(testId, "Emi with ICICI bank is selected");
    } else if (StringUtils.contains(bankName, HDFC_BANK)) {
      PageHandler.javaScriptExecuterClick(driver, paymentPage.selectEmiHDFCBank());
      Log.info(testId, "Emi with HDFC bank is selected");
    }

    PageElement paymentTenureObject = paymentPage.modifyPageElementOnce("emiTenure", emiTenure);
    WebElement paymentTenureElement = paymentPage.getElementByPageObject(paymentTenureObject);
    PageHandler.javaScriptExecuterClick(driver, paymentTenureElement);
    Log.info(testId, "creditcard with emi tenure " + emiTenure + "is selected");
    totalFareAmount = emiCreditCardInput(creditCardType, paymentPage);

    return totalFareAmount;
  }

  // input the credit card details
  private String creditCardInput(String testId, RepositoryParser repositoryParser,
      String paymentType, PaymentElements paymentPage) {
    // paymentPage.creditCardNumber().sendKeys(getCreditCardType(repositoryParser,
    // paymentType));
    paymentPage.creditCardNumber().sendKeys(cardDetails.getCCNo());
    Log.info(testId, cardDetails.getCCNo() + " " + paymentType
        + " Type:-credit card number is entered");

    // paymentPage.creditCardName().sendKeys(repositoryParser.getPropertyValue(NAME));
    paymentPage.creditCardName().sendKeys(cardDetails.getCCHolderName());
    Log.info(testId, "credit card name is entered");

    // paymentPage.creditCardValidityMonth().sendKeys(
    // repositoryParser.getPropertyValue(VALIDITY_MONTH));
    paymentPage.creditCardValidityMonth().sendKeys(cardDetails.getCCValidityMonth());
    Log.info(testId, "credit card validity month is entered");

    // paymentPage.creditCardValidityYear().sendKeys(repositoryParser.getPropertyValue(VALIDITY_YEAR));
    paymentPage.creditCardValidityYear().sendKeys(cardDetails.getCCValidityYear());
    Log.info(testId, "credit card validity year is entered");

    // paymentPage.creditCardCVVNumber().sendKeys(repositoryParser.getPropertyValue(CVV));
    paymentPage.creditCardCVVNumber().sendKeys(cardDetails.getCCCVVNo());
    Log.info(testId, "credit card cvv number is entered");
    // check country code and click to accept Terms & Conditions
    if (countryCode == VIA_COUNTRY.ID) {
      paymentPage.paymentTermsAndCond().click();
    }

    PageHandler.sleep(testId, 2 * 1000L);

    String fareWithConvenience = paymentPage.totalFareAmountWithConvenience().getText();

    PageHandler.javaScriptExecuterClick(driver, paymentPage.finalPaySubmit());

    PageHandler.sleep(testId, 5 * 1000L);

    if (StringUtils.equalsIgnoreCase(paymentType, "Master")) {
      PageHandler.sleep(testId, 15 * 1000L);

      // Check OTPPage Open or not
      if (checkOTPPage(paymentPage)) {
        paymentPage.getCardCancelBtn().click();
        PageHandler.acceptAlert(testId, driver, "Transaction Declined");
      } else {
        Log.info(testId, "OTP page is not opened");
      }

    } else if (countryCode == VIA_COUNTRY.PH) {
      try {
        WebElement cancelVisa = driver.findElement(By.tagName("a"));
        PageHandler.javaScriptExecuterClick(driver, cancelVisa);
      } catch (Exception e) {

      }
    }

    if (!StringUtils.equalsIgnoreCase(paymentType, "AMEX")) {
      bookingVerification = true;
    }

    PageHandler.waitForDomLoad(testId, driver);
    PageHandler.sleep(testId, 4 * 1000L);

    return fareWithConvenience;
  }

  private String proceedToPay(PaymentElements paymentPage) {
    PageHandler.sleep(testId, 1000L);
    paymentPage.selectCreditCardPaymentMode().click();
    Log.info(testId, " Vendor Jetco "  + " Payment Type ---> " +paymentType);
    paymentPage.HKBankingSelection(paymentType).click();
    PageHandler.sleep(testId, 2 * 1000L);
    String fareWithConvenience = paymentPage.totalFareAmountWithConvenience().getText();
    Log.info(testId, "Total fare with convenience : " + fareWithConvenience);
    PageHandler.javaScriptExecuterClick(driver, paymentPage.finalPaySubmit());
    PageHandler.javaScriptExecuterClick(driver, paymentPage.continueButton());
    PageHandler.javaScriptExecuterClick(driver, paymentPage.radioVM());
    paymentPage.proceed().click();
    PageHandler.sleep(testId, 5000L);
    paymentPage.cancelOfJetco().click();
    try {
      driver.switchTo().alert().accept();
      paymentPage.CancelButton().click();
      driver.switchTo().alert().accept();
    } catch (Exception e) {
      Log.info(testId, "Some popup box not opened");
    }

    return fareWithConvenience;
  }
  
  private boolean checkOTPPage(PaymentElements paymentPage) {
    String title = driver.getTitle();
    boolean status = false;
    if (title.equalsIgnoreCase(repositoryParser.getPropertyValue("OTPPageTitle"))) {
      status = true;
    }
    return status;
  }

  private String emiCreditCardInput(String paymentType, PaymentElements paymentPage) {
    // paymentPage.emiCreditCardNumber().sendKeys(getCreditCardType(repositoryParser,
    // paymentType));
    paymentPage.emiCreditCardNumber().sendKeys(cardDetails.getCCNo());
    Log.info(testId, "credit card number is entered");
    // paymentPage.emiCreditCardName().sendKeys(repositoryParser.getPropertyValue(NAME));
    paymentPage.emiCreditCardName().sendKeys(cardDetails.getCCHolderName());
    Log.info(testId, "credit card name is entered");
    // paymentPage.emiCreditCardValidityMonth().sendKeys(
    // repositoryParser.getPropertyValue(VALIDITY_MONTH));
    paymentPage.emiCreditCardValidityMonth().sendKeys(cardDetails.getCCValidityMonth());
    Log.info(testId, "credit card validity month is entered");
    // paymentPage.emiCreditCardValidityYear().sendKeys(
    // repositoryParser.getPropertyValue(VALIDITY_YEAR));
    paymentPage.emiCreditCardValidityYear().sendKeys(cardDetails.getCCValidityYear());
    Log.info(testId, "credit card validity year is entered");
    // paymentPage.emiCreditCardCVVNumber().sendKeys(repositoryParser.getPropertyValue(CVV));
    paymentPage.emiCreditCardCVVNumber().sendKeys(cardDetails.getCCCVVNo());
    Log.info(testId, "credit card cvv number is entered");

    PageHandler.sleep(testId, 2 * 1000L);

    String payment = paymentPage.totalFareAmountWithConvenience().getText();
    PageHandler.javaScriptExecuterClick(driver, paymentPage.finalPaySubmit());

    return payment;
  }

  // Wallet payment amount validation
  private String walletValidation(String paymentType, double paymentFare,
      PaymentElements paymentPage) {
    PageHandler.sleep(testId, 2 * 1000L);
    String actPaymentfare = paymentPage.totalFareAmount().getText();
    Log.info(testId, "Amount in the final page is " + actPaymentfare);
    double actFare = NumberUtility.getAmountFromString(actPaymentfare);

    double convinienceAmount = actFare - paymentFare;
    Log.info(Double.toString(convinienceAmount));
    PageHandler.javaScriptExecuterClick(driver, paymentPage.finalPaySubmit());
    Log.info(testId, "FinalPayment button is clicked");
    PageHandler.javaScriptExecuterClick(driver, paymentPage.walletPayementConformation());
    Log.info(testId, "Wallet confirmation is clicked");
    return Double.toString(actFare);
  }

  // UAE masterPassPayMode-Kishor
  private String masterPassPayMode(PaymentElements paymentPage) {
    PageHandler.sleep(testId, 2000L);
    paymentPage.selectMasterPassPayMode().click();
    Log.info(testId, " MasterPass ");
    paymentPage.masterPassBank().click();
    String fareWithConvenience = paymentPage.totalFareAmountWithConvenience().getText();
    Log.info(testId, "Total fare with convenience : " + fareWithConvenience);
    PageHandler.javaScriptExecuterClick(driver, paymentPage.finalPaySubmit());
    PageHandler.waitForPageLoad(testId, driver);
    paymentPage.cross().click();
    paymentPage.yesContinue().click();
    PageHandler.waitForDomLoad(testId, driver);
    PageHandler.sleep(testId, 2000L);
    return fareWithConvenience;
  }

  // ID bankTransfer-Kishor
  private String bankTransferPayMode(PaymentElements paymentPage) {
    PageHandler.sleep(testId, 1000L);
    paymentPage.bankTransferPaymentMode().click();
    Log.info(testId, " Bank Transfer "  + " Payment Type ---> " +paymentType);
    paymentPage.selectingBank(paymentType).click();
    String fareWithConvenience = paymentPage.totalFareAmountWithConvenience().getText();
    Log.info(testId, "Total fare with convenience : " + fareWithConvenience);
    PageHandler.javaScriptExecuterClick(driver, paymentPage.finalPaySubmit());
    PageHandler.sleep(testId, 2000L);
    paymentPage.bookingDetails().click();
    return fareWithConvenience;
  }

  // ID ATM-Kishor
  private String atmPayMode(PaymentElements paymentPage) {
    paymentPage.atmPaymode().click();
    Log.info(testId, "ATM");
    String fareWithConvenience = paymentPage.totalFareAmountWithConvenience().getText();
    Log.info(testId, "Total fare with convenience : " + fareWithConvenience);
    PageHandler.sleep(testId, 2 * 1000L);
    PageHandler.javaScriptExecuterClick(driver, paymentPage.finalPaySubmit());
    paymentPage.continueButton().click();
    PageHandler.sleep(testId, 2000L);
    paymentPage.bookingDetails().click();
    return fareWithConvenience;
  }

  // ID internet banking-Kishor
  public String internetBanking(PaymentElements paymentPage) {
    PageHandler.sleep(testId, 2 * 1000L);
    paymentPage.internetBanking().click();
    Log.info(testId, " Internet Banking "  + " Payment Type ---> "+paymentType);
    paymentPage.bankSelection(paymentType).click();
    String fareWithConvenience = paymentPage.totalFareAmountWithConvenience().getText();
    Log.info(testId, "Total fare with convenience : " + fareWithConvenience);
    PageHandler.javaScriptExecuterClick(driver, paymentPage.finalPaySubmit());
    if (paymentType.equalsIgnoreCase("Mandiri_Clikpay")) {
      paymentPage.cardEntering().click();
      paymentPage.cardEntering().sendKeys(repositoryParser.getPropertyValue("CreditCardMandiri"));
      paymentPage.tokenNumber().click();
      paymentPage.tokenNumber().sendKeys(repositoryParser.getPropertyValue("sixDigitMandiri"));
      paymentPage.submitKeyofMandri().click();
    } else {
      paymentPage.cancelPayment().click();
      driver.switchTo().alert().accept();
    }
    PageHandler.waitForDomLoad(testId, driver);
    PageHandler.sleep(testId, 2 * 1000L);
    return fareWithConvenience;
  }

  private static void getUrl(String testId, WebDriver driver) {
    Log.info(testId, driver.getCurrentUrl());
  }

  public void verifyConvenience(WebDriver driver, BOOKING_MEDIA media, Flight flightType,
      FlightBookingDetails flightBookingDetails, Double paymentFare, Double totalAmountWithConv) {

    Log.info(testId, "----------------   Convenience Fee Verification   --------------");

    Log.info(
        testId,
        "Total Amount without convenience: "
            + NumberUtility.getRoundedAmount(countryCode, paymentFare));
    Log.info(
        testId,
        "Total Amount with Convenience: "
            + NumberUtility.getRoundedAmount(countryCode, totalAmountWithConv));

    Double convenienceAmount = 0.0;

    Double percentage = 0.0;

    if (countryCode == VIA_COUNTRY.ID && media == BOOKING_MEDIA.B2B) {
      if (paymentMode.equalsIgnoreCase(CREDIT_CARD)) {
        // percentage = 0.025;
        percentage = 0.020;
        convenienceAmount = new Double((int) (paymentFare * percentage));
      }
      if (paymentMode.equalsIgnoreCase(INTERNETBANKING)) {
        if (paymentType.equalsIgnoreCase("Mandiri_Clikpay")
            || paymentType.equalsIgnoreCase("BCA_KlikPay")) {
          convenienceAmount = new Double(1000);
        } else {
          convenienceAmount = 0.0;
        }

        if (paymentMode.equalsIgnoreCase(ATM) || paymentMode.equalsIgnoreCase(BANK)) {
          convenienceAmount = 0.0;
        }
      }
    }

    else if (countryCode == VIA_COUNTRY.PH) {
      if (media == BOOKING_MEDIA.B2B) {
        percentage = 0.0285;
      } else {
        percentage = 0.020;
      }
      convenienceAmount = Math.round((paymentFare * percentage) * 100.0) / 100.0;
    }

    else if (countryCode == VIA_COUNTRY.UAE) {
      // Masterpass payment fee-Kishor
      if (paymentMode.equalsIgnoreCase(MASTERPASS)) {
        percentage = 0.0235;
        convenienceAmount = Math.round((paymentFare * percentage) * 100.0) / 100.0;
      } else {
        percentage = 0.024;
        convenienceAmount = Math.round((paymentFare * percentage) * 100.0) / 100.0;
      }
    } else if (countryCode == VIA_COUNTRY.OM) {
      percentage = 0.028;
      convenienceAmount = Math.round((paymentFare * percentage) * 100.0) / 100.0;
    }

    else if (countryCode == VIA_COUNTRY.SA) {
      percentage = 0.028;
      convenienceAmount = Math.round((paymentFare * percentage) * 100.0) / 100.0;
    }

    else if (countryCode == VIA_COUNTRY.SG) {
      percentage = 0.02;
      convenienceAmount = Math.round((paymentFare * percentage) * 100.0) / 100.0;
    }

    else if (countryCode == VIA_COUNTRY.HK) {
      //percentage = 0.018;
      percentage = 0.019;
      convenienceAmount = Math.round((paymentFare * percentage) * 100.0) / 100.0;
    } else if (countryCode == VIA_COUNTRY.TH) {
      // Added for 123Banking
      if (StringUtils.equalsIgnoreCase(paymentMode, BANKING123)) {
        percentage = 0.0193;
        convenienceAmount = Math.round((paymentFare * percentage) * 100.0) / 100.0;
      } else {
        // percentage = 0.0247;
        percentage = 0.03;
        convenienceAmount = Math.round((paymentFare * percentage) * 100.0) / 100.0;
      }

    } else if (countryCode == Constant.VIA_COUNTRY.IN_CORP) {
      percentage = 0.018;
      double perPersonConv;
      double convTravellers =
          flightBookingDetails.getAdultsCount() + flightBookingDetails.getChildrenCount();
      if (flightType == Constant.Flight.DOMESTIC) {
        if (StringUtils.equalsIgnoreCase(paymentMode, "FREECHARGE_WALLET")
            || StringUtils.equalsIgnoreCase(paymentMode, "NETBANKING")
            || StringUtils.equalsIgnoreCase(paymentMode, "SBI_WALLET")) {
          perPersonConv = 100.0;
          convenienceAmount = perPersonConv * convTravellers;
        } else if (StringUtils.equalsIgnoreCase("Amex", paymentType)) {
          percentage = 0.025;
          convenienceAmount = (paymentFare) * percentage;
        } else {
          convenienceAmount = (paymentFare) * percentage;
        }
      }
      if (flightType == Constant.Flight.INTERNATIONAL) {
        if (StringUtils.equalsIgnoreCase("Amex", paymentType)) {
          percentage = 0.025;
          convenienceAmount = (paymentFare) * percentage;
        } else {
          percentage = 0.01;
          if (StringUtils.equalsIgnoreCase(flightBookingDetails.getOnwardFlightName(), "IndiGo")
              || StringUtils.equalsIgnoreCase(flightBookingDetails.getOnwardFlightName(),
                  "SpiceJet")
              || StringUtils.equalsIgnoreCase(flightBookingDetails.getOnwardFlightName(), "GoAir")
              || StringUtils.equalsIgnoreCase(flightBookingDetails.getOnwardFlightName(),
                  "AirAsia India")
              || StringUtils.equalsIgnoreCase(flightBookingDetails.getOnwardFlightName(),
                  "AirArabia")) {
            perPersonConv = 225.0;
            convenienceAmount =
                (paymentFare) * percentage + perPersonConv * (double) convTravellers;
          } else {
            convenienceAmount = (paymentFare) * percentage;
          }
        }
      }
    }

    Log.info(testId, "Convenience Amount: " + NumberUtility.getRoundedAmount(convenienceAmount));

    Double diff = totalAmountWithConv - (paymentFare + convenienceAmount);
    Double correction = 0.1;

    boolean convenienceFlag = true;

    if (correction.compareTo(Math.abs(diff)) < 0) {
      convenienceFlag = false;
    }

    CustomAssert.assertEquals(driver, testId, true, convenienceFlag, "Convenience amount matched.",
        "Convenience amount didn't match.");
    Log.divider(testId);
  }

  public static void verifyTrainConvenience(String testId, VIA_COUNTRY countryCode,
      BOOKING_MEDIA media, TrainBookingDetails trainBookingDetails, Double payableAmount,
      Double amountWithConv) {
    Log.info(testId, "----------------   Convenience Fee Verification   --------------");

    Log.info(
        testId,
        "Total Amount without convenience: "
            + NumberUtility.getRoundedAmount(countryCode, payableAmount));
    Log.info(
        testId,
        "Total Amount with Convenience: "
            + NumberUtility.getRoundedAmount(countryCode, amountWithConv));

    Double convenienceAmount = 0.0;

    Double percentage = 0.0;

    if (countryCode == VIA_COUNTRY.ID) {
      if (media == BOOKING_MEDIA.B2B) {
        // percentage = 0.025;
        percentage = 0.020;
      }
      convenienceAmount = new Double((int) (payableAmount * percentage));
    }

    Log.info(
        testId,
        "Calculated Convenience Amount : "
            + NumberUtility.getRoundedAmount(countryCode, convenienceAmount));

    boolean validationFlag = amountWithConv.equals(payableAmount + convenienceAmount);

    CustomAssert.assertTrue(testId, validationFlag, "Convenience Amount matched.",
        "Convenience Amount didn't match.");
    Log.divider(testId);
  }

}
