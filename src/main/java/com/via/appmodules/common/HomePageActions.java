package com.via.appmodules.common;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.via.pageobjects.common.HomePage;
import com.via.utils.Constant;
import com.via.utils.Constant.VIA_COUNTRY;
import com.via.utils.CustomAssert;
import com.via.utils.Log;
import com.via.utils.PageHandler;
import com.via.utils.RepositoryParser;
import com.via.utils.StringUtilities;

public class HomePageActions {
	private HomePage homePage;
	private WebDriver driver;
	private String testId;
	private RepositoryParser repositoryParser;

	public HomePageActions(WebDriver driver, RepositoryParser repositoryParser,
			String testId) {
		homePage = new HomePage(testId, driver, repositoryParser);
		this.driver = driver;
		this.testId = testId;
		this.repositoryParser = repositoryParser;

		Log.info(testId,
				"::::::::::::::::::            Home Page         ::::::::::::::::::");
		Log.divider(testId);
	}

	public boolean loginUser(VIA_COUNTRY countryCode, String userDetails) {

		if (StringUtils.isBlank(userDetails)) {
			return false;
		}

		List<String> userLoginDetails = StringUtilities.split(userDetails,
				Constant.UNDERSCORE);

		/*** return false if excel signed cell has value no ***/
		if (StringUtils.equalsIgnoreCase(userLoginDetails.get(0), "no")) {
			return false;
		}

		boolean loginSuccess = false;
		String user = "Via user id.";
		if (userLoginDetails.size() == 1
				|| StringUtils.equalsIgnoreCase(userLoginDetails.get(1), "via")) {
			loginSuccess = loginByViaId(countryCode);

		} else if (StringUtils.equalsIgnoreCase(userLoginDetails.get(1),
				"facebook")) {
			loginSuccess = loginByFacebookId();
			user = "FaceBook user id.";
		} else if (StringUtils.equalsIgnoreCase(userLoginDetails.get(1),
				"google")) {
			loginSuccess = loginByGoogleId();
			user = "Google+ user id.";
		}

		PageHandler.waitForDomLoad(testId, driver);

		CustomAssert.assertTrue(testId, loginSuccess, "Successfully login as: "
				+ user, "Invalid Login credicential.");
		Log.divider(testId);

		return loginSuccess;
	}

	private boolean loginByGoogleId() {
		/*** updated in next level ***/
		return false;
	}

	private boolean loginByFacebookId() {
		/*** updated in next level ***/
		return false;
	}

	// Handling via msg pop-up for uae & oman
	public boolean closePopup(VIA_COUNTRY countryCode) {
		try {
			homePage.crossForViaMsg().click();
			PageHandler.sleep(testId, 1000L);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	private boolean loginByViaId(VIA_COUNTRY countryCode) {

		// Handling via msg of oman & uae.
		if (countryCode == VIA_COUNTRY.OM || countryCode == VIA_COUNTRY.UAE || countryCode == VIA_COUNTRY.SA) {
			closePopup(countryCode);
		}
		/*** login slider element ***/
		WebElement element = homePage.getLoginSlider();
		PageHandler.javaScriptExecuterClick(driver, element);
		/*** get email id input box and send email id to it ***/
		element = homePage.getEmailIdInputBox();
		element.sendKeys(repositoryParser.getPropertyValue(countryCode
				+ "viaLoginEmail"));
		/*** get password input box and send password to it ***/
		element = homePage.getPasswordBox();
		element.sendKeys(repositoryParser.getPropertyValue(countryCode
				+ "viaLoginPassword"));
		/*** get sign in button and click on it ***/
		element = homePage.getSignInButton();
		PageHandler.javaScriptExecuterClick(driver, element);
		if (countryCode == VIA_COUNTRY.OM || countryCode == VIA_COUNTRY.UAE || countryCode == VIA_COUNTRY.SA) {
			closePopup(countryCode);
		}
		/*** get alert message element to check weather login success or not ***/
		try {
			element = homePage.getUserMenu();
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public void signOut() {
		/*** user menu element ***/
		WebElement element = homePage.getUserMenu();
		PageHandler.javaScriptExecuterClick(driver, element);

		/*** get sign out element and click on it ***/
		element = homePage.getSingOutButton();
		PageHandler.javaScriptExecuterClick(driver, element);
		Log.info(testId, "Successfully Signed Out.");
	}

	public void changeLanguageToEnglish() {
		WebElement langElement = homePage.getLanguageSelector();
		if (StringUtils.equalsIgnoreCase(langElement.getText(), "English")) {
			PageHandler.javaScriptExecuterClick(driver, langElement);
		}

		ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript(
						"return document.readyState").equals("complete");
			}
		};
		WebDriverWait wait = new WebDriverWait(driver, 40);
		wait.until(pageLoadCondition);

		Log.info(testId, "Language changed to english.");
		Log.divider(testId);
	}
}
