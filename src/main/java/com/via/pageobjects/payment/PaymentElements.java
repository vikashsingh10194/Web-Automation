package com.via.pageobjects.payment;

import lombok.AllArgsConstructor;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.via.pageobjects.common.PageElement;
import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;

@AllArgsConstructor
public class PaymentElements extends PageHandler {

	private RepositoryParser repositoryParser;
	private WebDriverWait wait;

	public PaymentElements(WebDriver driver, RepositoryParser repositoryParser,
			String testId) {
		this.driver = driver;
		this.repositoryParser = repositoryParser;
		wait = new WebDriverWait(driver, 30);
	}

	private final String PAGE_NAME = "payments";

	public WebElement selectCreditCardPaymentMode() {
		return wait.until(ExpectedConditions.visibilityOf(findElement(
				repositoryParser, PAGE_NAME, "creditCard")));
	}

	public PageElement modifyPageElementOnce(String objectName,
			String modification) {
		PageElement element = repositoryParser.getPageObject(PAGE_NAME,
				objectName);
		PageElement modifiedElement = new PageElement(element);
		String locatorValue = modifiedElement.getLocatorValue();
		String modifiedLocatorValue = StringUtils.replace(locatorValue,
				"toModify", modification);
		modifiedElement.setLocatorValue(modifiedLocatorValue);
		return modifiedElement;
	}

	// Its take the payment type or any inputs from excel sheet & pass it to
	// JSON objects in the name of"to modify" -Kishor
	public static PageElement modifyPageElement(
			RepositoryParser repositoryParser, String pageName,
			String objectName, String... modificationStrings) {
		PageElement element = repositoryParser.getPageObject(pageName,
				objectName);
		PageElement modifiedElement = new PageElement(element);
		String locatorValue = modifiedElement.getLocatorValue();

		for (String modification : modificationStrings) {
			locatorValue = StringUtils.replaceOnce(locatorValue, "toModify",
					modification);
		}
		modifiedElement.setLocatorValue(locatorValue);
		return modifiedElement;
	}

	public WebElement getElementByPageObject(PageElement pageElement) {
		WebElement element = findElement(pageElement);
		Assert.assertTrue(element != null);
		return element;
	}

	// method added to find saved card element
	public WebElement selectSavedCardPaymentMode() {
		return wait.until(ExpectedConditions.elementToBeClickable(findElement(
				repositoryParser, PAGE_NAME, "savedCard")));
	}

	// Method added to find 123Banking payment type-Kishor
	public WebElement select123BankingPaymentType(String banks123) {
		return findElement(modifyPageElement(repositoryParser, PAGE_NAME,
				"123Banking", banks123));
	}

	// Selecting bank transfer paymode-Kishor
	public WebElement bankTransferPaymentMode() {
		return findElement(repositoryParser, PAGE_NAME, "bankTransfer");
	}

	// selecting bank transfer payment type-Kishor
	public WebElement selectingBank(String bank) {
		return findElement(modifyPageElement(repositoryParser, PAGE_NAME,
				"selectingBank", bank));
	}

	// clicking on booking detail for validation after blocking ticket on bank
	// transfer & atm-Kishor
	public WebElement bookingDetails() {
		return findElement(repositoryParser, PAGE_NAME, "bookingDetails");
	}

	// selecting payment mode as ATM-Kishor
	public WebElement atmPaymode() {
		return findElement(repositoryParser, PAGE_NAME, "atmPay");
	}

	// selecting payment mode as InternetBanking-Kishor
	public WebElement internetBanking() {
		return findElement(repositoryParser, PAGE_NAME, "internetBanking");
	}

	// selecting payment type of bank transfer-Kishor
	public WebElement bankSelection(String bankName) {
		return findElement(modifyPageElement(repositoryParser, PAGE_NAME,
				"internetBankingBank", bankName));
	}

	public WebElement cardEntering() {
		return findElement(repositoryParser, PAGE_NAME, "cardNumber");
	}

	public WebElement tokenNumber() {
		return findElement(repositoryParser, PAGE_NAME, "tokenNumber");
	}

	public WebElement submitKeyofMandri() {
		return findElement(repositoryParser, PAGE_NAME, "submitKey");
	}

	public WebElement cancelPayment() {
		return findElement(repositoryParser, PAGE_NAME, "cancelPayment");
	}

	public WebElement selectMasterPassPayMode() {
		return findElement(repositoryParser, PAGE_NAME, "masterpass");
	}

	public WebElement masterPassBank() {
		return wait.until(ExpectedConditions.elementToBeClickable(findElement(
				repositoryParser, PAGE_NAME, "masterPassBank")));
	}

	public WebElement cross() {
		return findElement(repositoryParser, PAGE_NAME, "crossOfUAEBANK");
	}

	public WebElement yesContinue() {
		return findElement(repositoryParser, PAGE_NAME, "yesContinue");
	}

	public WebElement selectNetBankingPaymentMode() {
		return wait.until(ExpectedConditions.elementToBeClickable(findElement(
				repositoryParser, PAGE_NAME, "netBanking")));
	}

	public WebElement select123BankingMode() {
		return wait.until(ExpectedConditions.elementToBeClickable(findElement(
				repositoryParser, PAGE_NAME, "selecting123")));
	}

	public WebElement selectDebitCardPaymentMode() {
		return wait.until(ExpectedConditions.elementToBeClickable(findElement(
				repositoryParser, PAGE_NAME, "debitCard")));
	}

	public WebElement selectWalletPaymentMode() {
		return wait.until(ExpectedConditions.elementToBeClickable(findElement(
				repositoryParser, PAGE_NAME, "wallet")));
	}

	public WebElement selectEmiPaymentMode() {
		return wait.until(ExpectedConditions.elementToBeClickable(findElement(
				repositoryParser, PAGE_NAME, "emi")));
	}

	// Return SavedCard DropDown Element
	public WebElement selectSavedCardDropDown() {
		return findElement(repositoryParser, PAGE_NAME, "dropDownOption");
	}

	public WebElement selectSavedCVV() {
		return findElement(repositoryParser, PAGE_NAME, "savedCardCVV");
	}

	public WebElement creditCardNumber() {
		return findElement(repositoryParser, PAGE_NAME, "creditCardNumber");
	}

	public WebElement creditCardName() {
		return findElement(repositoryParser, PAGE_NAME, "creditCardName");
	}

	public WebElement creditCardValidityMonth() {
		return findElement(repositoryParser, PAGE_NAME,
				"creditCardValidityMonth");
	}

	public WebElement creditCardValidityYear() {
		return findElement(repositoryParser, PAGE_NAME,
				"creditCardValidityYear");
	}

	public WebElement creditCardCVVNumber() {
		return findElement(repositoryParser, PAGE_NAME, "creditCardCVV");
	}

	public WebElement netBankingSelectAxisBank() {
		return findElement(repositoryParser, PAGE_NAME, "axisBankNetBanking");
	}

	public WebElement netBankingSelectHdfcBank() {
		return findElement(repositoryParser, PAGE_NAME, "hdfcBankNetBanking");
	}

	public WebElement netBankingSelectIciciBank() {
		return findElement(repositoryParser, PAGE_NAME, "iciciBankNetBanking");
	}

	public WebElement netBankingSelectKotakBank() {
		return findElement(repositoryParser, PAGE_NAME, "kotakBankNetBanking");
	}

	public WebElement netBankingSelectPnbBank() {
		return findElement(repositoryParser, PAGE_NAME, "pnbBankNetBanking");
	}

	public WebElement netBankingSelectSbiBank() {
		return findElement(repositoryParser, PAGE_NAME, "sbiBankNetBanking");
	}

	public WebElement netBankingBankSelector() {
		return findElement(repositoryParser, PAGE_NAME, "netbankSelector");
	}

	public WebElement selectCitruswallet() {
		return findElement(repositoryParser, PAGE_NAME, "citrusWallet");
	}

	public WebElement selectFreeChargeWallet() {
		return findElement(repositoryParser, PAGE_NAME, "freeChargeWallet");
	}

	public WebElement selectPayUmoneyWallet() {
		return findElement(repositoryParser, PAGE_NAME, "payUmoneyWallet");
	}

	public WebElement selectSbiBuddyWallet() {
		return findElement(repositoryParser, PAGE_NAME, "sbiBuddyWallet");
	}

	public WebElement selectEmiHDFCBank() {
		return findElement(repositoryParser, PAGE_NAME, "emiHDFCBank");
	}

	public WebElement selectEmiICICIBank() {
		return findElement(repositoryParser, PAGE_NAME, "emiICICIBank");
	}

	public WebElement finalPaySubmit() {
		return findElement(repositoryParser, PAGE_NAME, "payButton");
	}

	// Method to find CheckBox element for payment terms and condition..
	public WebElement paymentTermsAndCond() {
		return findElement(repositoryParser, PAGE_NAME, "paymentTermsCond");
	}

	// Added for , Payment Page for Country HK...

	public WebElement HKBankingSelection(String hkBank) {
		return findElement(modifyPageElement(repositoryParser, PAGE_NAME,
				"HKBankingSelection", hkBank));
	}

	public WebElement continueButton() {
		return findElement(repositoryParser, PAGE_NAME, "continue");
	}

	public WebElement CancelButton() {
		return findElement(repositoryParser, PAGE_NAME, "cancel");
	}

	public WebElement paymentAmount3dSecurePage() {
		return findElement(repositoryParser, PAGE_NAME, "3dSecurePaymentTotal");
	}

	public WebElement walletPayementConformation() {
		return findElement(repositoryParser, PAGE_NAME,
				"paymentWalletConfirmation");
	}

	public WebElement citursWalletAmount() {
		return findElement(repositoryParser, PAGE_NAME, "citrusWalletAmount");
	}

	public WebElement citrusCreditCard() {
		return findElement(repositoryParser, PAGE_NAME, "citrusCreditCard");
	}

	public WebElement totalFareAmount() {
		return findElement(repositoryParser, PAGE_NAME, "totalAmount");
	}

	public WebElement freeChargeWalletFinalFare() {
		return findElement(repositoryParser, PAGE_NAME, "freeChargeAmount");
	}

	public WebElement payUMoneyWalletFare() {
		return findElement(repositoryParser, PAGE_NAME, "payUmoneyAmount");
	}

	public WebElement sbiWalletFare() {
		return findElement(repositoryParser, PAGE_NAME, "sbiWalletAmount");
	}

	public WebElement emiCreditCardNumber() {
		return findElement(repositoryParser, PAGE_NAME, "emiCreditCardNumber");
	}

	public WebElement emiCreditCardName() {
		return findElement(repositoryParser, PAGE_NAME, "emiCreditCardName");
	}

	public WebElement emiCreditCardValidityMonth() {
		return findElement(repositoryParser, PAGE_NAME,
				"emiCreditCardValidityMonth");
	}

	public WebElement emiCreditCardValidityYear() {
		return findElement(repositoryParser, PAGE_NAME,
				"emiCreditCardValidityYear");
	}

	public WebElement emiCreditCardCVVNumber() {
		return findElement(repositoryParser, PAGE_NAME, "emiCreditCardCVV");
	}

	public WebElement paymentResalerIframe() {
		WebDriverWait wait = new WebDriverWait(driver, 20);
		return wait.until(ExpectedConditions.visibilityOf(findElement(
				repositoryParser, PAGE_NAME, "paymentFrame")));
	}

	public WebElement totalFareAmountWithConvenience() {
		return findElement(repositoryParser, PAGE_NAME,
				"totalAmountWithConvenience");
	}

	public WebElement bookingRefNo() {
		return findElement(repositoryParser, PAGE_NAME, "bookingRefNo");
	}

	public WebElement getCitrusCancelBtn() {
		return findElement(repositoryParser, PAGE_NAME, "citrusCancel");
	}

	public WebElement getFreeChargeCancelBtn() {
		return findElement(repositoryParser, PAGE_NAME, "freeChargeCancel");
	}

	public WebElement getPayUCancelBtn() {
		return findElement(repositoryParser, PAGE_NAME, "payUCancel");
	}

	public WebElement getSbiBuddyCancelBtn() {
		return findElement(repositoryParser, PAGE_NAME, "sbiBuddyCancel");
	}

	public WebElement getCardCancelBtn() {
		WebDriverWait wait = new WebDriverWait(driver, 20);
		return wait.until(ExpectedConditions.elementToBeClickable(findElement(
				repositoryParser, PAGE_NAME, "cardCancelBtn")));
	}

	// getting 123banking cancel button
	public WebElement get123BankingCancelBtn() {
		WebDriverWait wait = new WebDriverWait(driver, 20);
		return wait.until(ExpectedConditions.elementToBeClickable(findElement(
				repositoryParser, PAGE_NAME, "123BankingCancelBtn")));
	}

	public WebElement hKCreditCardNo(String block) {
		return findElement(modifyPageElementOnce("hKCreditCardNo", block));
	}

	public WebElement hKCCValidityMonth() {
		return findElement(repositoryParser, PAGE_NAME, "hKCCValidityMonth");
	}

	public WebElement hKCCValidityYear() {
		return findElement(repositoryParser, PAGE_NAME, "hKCCValidityYear");
	}

	public WebElement hKCCCVVNo() {
		return findElement(repositoryParser, PAGE_NAME, "hKCCCVVNo");
	}

	public WebElement proceed() {
		return findElement(repositoryParser, PAGE_NAME, "proceed");
	}

	public WebElement cancelOfJetco() {
		return findElement(repositoryParser, PAGE_NAME, "cancelOfJetco");
	}

	public WebElement radioVM() {
		return findElement(repositoryParser, PAGE_NAME, "radioVM");
	}

}
