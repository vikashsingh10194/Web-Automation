package com.via.pageobjects.flights.common;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.StringUtils;

import com.via.utils.Constant.BOOKING_MEDIA;
import com.via.utils.Constant.Flight;
import com.via.utils.Constant.Journey;
import com.via.utils.Constant.Traveller;
import com.via.utils.Constant.VIA_COUNTRY;
import com.via.utils.CustomAssert;
import com.via.utils.Log;
import com.via.utils.NumberUtility;
import com.via.utils.RepositoryParser;

@Getter
@Setter
public class FareDetails {
  private Double adultTotalFare;
  private Double childTotalFare;
  private Double infantTotalFare;
  private Double otherFare;
  private Double taxes;
  private Double transactionFee;
  private Double discount;
  private Double totalFare;

  public void incrementOtherFare(Double incAmount) {
    if (otherFare == null) {
      otherFare = incAmount;
      return;
    }
    otherFare += incAmount;
  }

  public boolean verifyFare(String testId) {
    Log.info(testId, "------------    Total Fare Verification    -----------");

    Double totalAmount = adultTotalFare;
    if (childTotalFare != null) {
      totalAmount += childTotalFare;
    }
    if (infantTotalFare != null) {
      totalAmount += infantTotalFare;
    }
    if (taxes != null) {
      totalAmount += taxes;
    }
    if (transactionFee != null) {
      totalAmount += transactionFee;
    }
    if (discount != null) {
      totalAmount -= discount;
    }
    if (otherFare != null) {
      totalAmount += otherFare;
    }

    Double diff = Math.abs(totalAmount - totalFare);

    if (diff > 0.1) {
      return false;
    }

    Log.divider(testId);
    return true;
  }

  public void verifyTransactionFee(String testId, VIA_COUNTRY countryCode, BOOKING_MEDIA media,
      Journey journey, Flight flightType, RepositoryParser repositoryParser,
      List<FlightDetails> flightDetailsList, FlightBookingDetails flightBookingDetails) {
    Log.divider(testId);
    Log.info(testId, "------------    Transaction Fee Verification    -----------");

    Double actualTransactionFee = 0.0;
    Double actualDiscount = 0.0;

    if (media == BOOKING_MEDIA.B2B) {
      actualTransactionFee =
          getB2BTransactionFee(testId, flightDetailsList, flightType, repositoryParser,
              flightBookingDetails);

      if (countryCode == VIA_COUNTRY.ID && journey == Journey.ROUND_TRIP) {
        actualTransactionFee *= 2;
      }
    } else {

      List<Double> discountTransactionList = new ArrayList<Double>();
      discountTransactionList.add(0.0);
      discountTransactionList.add(0.0);

      getCalculatedTransactionFee(flightDetailsList, media, flightType, repositoryParser,
          flightBookingDetails, discountTransactionList);

      actualTransactionFee = discountTransactionList.get(0);

      actualDiscount = discountTransactionList.get(1);
    }

    if (discount == null) {
      discount = 0.0;
    }

    if (transactionFee == null) {
      transactionFee = 0.0;
    }

    Log.info(testId,
        "Transaction Fee: " + NumberUtility.getRoundedAmount(countryCode, transactionFee));
    Log.info(
        testId,
        "Calculated Transaction Fee: "
            + NumberUtility.getRoundedAmount(countryCode, actualTransactionFee));
    CustomAssert.assertVerify(testId, true, transactionFee.equals(actualTransactionFee),
        "Transaction Fee Verified.", "Transaction fee not same as calculated Transaction fee.");

    Log.info(testId, "Discount : " + NumberUtility.getRoundedAmount(countryCode, discount));
    Log.info(testId,
        "Calculated Discount : " + NumberUtility.getRoundedAmount(countryCode, actualDiscount));
    CustomAssert.assertVerify(testId, true, discount.equals(actualDiscount), "Discount Verified.",
        "Discount not same as calculated Discount.");

    Log.divider(testId);
  }

  private Double getB2BTransactionFee(String testId, List<FlightDetails> flightList,
      Flight flightType, RepositoryParser repositoryParser,
      FlightBookingDetails flightBookingDetails) {

    Double transactionFee = 0.0;

    String adultFee = null;
    String childFee = null;
    String infantFee = null;
    boolean flag = true;

    for (int flight = 0; flight < flightList.size(); flight++) {
      TransactionRuleElement transactionElement =
          new TransactionRuleElement(flightList.get(flight), BOOKING_MEDIA.B2B, flightType);
      transactionElement.setTravellerType(Traveller.ADULT);

      TransactionRuleElement adultRule = repositoryParser.getTransactionRule(transactionElement);

      if (StringUtils.isBlank(adultFee)) {
        adultFee = adultRule.getTransactionFee();
      }

      if (!StringUtils.equals(adultFee, adultRule.getTransactionFee())) {
        flag = false;
        break;
      }

      transactionElement.setTravellerType(Traveller.CHILD);
      TransactionRuleElement childRule = repositoryParser.getTransactionRule(transactionElement);
      if (StringUtils.isBlank(childFee)) {
        childFee = childRule.getTransactionFee();
      }

      if (!StringUtils.equals(childFee, childRule.getTransactionFee())) {
        flag = false;
        break;
      }
      transactionElement.setTravellerType(Traveller.INFANT);
      TransactionRuleElement infantRule = repositoryParser.getTransactionRule(transactionElement);

      if (StringUtils.isBlank(infantFee)) {
        infantFee = infantRule.getTransactionFee();
      }

      if (!StringUtils.equals(infantFee, infantRule.getTransactionFee())) {
        flag = false;
        break;
      }

    }

    CustomAssert.assertTrue(testId, flag, "Transaction fee not computed for it.");

    if (flag) {
      transactionFee = getAmount(adultTotalFare, flightBookingDetails.getAdultsCount(), adultFee);
      transactionFee +=
          getAmount(childTotalFare, flightBookingDetails.getChildrenCount(), childFee);
      transactionFee +=
          getAmount(infantTotalFare, flightBookingDetails.getInfantsCount(), infantFee);
    }

    return transactionFee;
  }

  private void getCalculatedTransactionFee(List<FlightDetails> flightList, BOOKING_MEDIA media,
      Flight flightType, RepositoryParser repositoryParser,
      FlightBookingDetails flightBookingDetails, List<Double> discountTransactionFee) {

    if (flightList == null) {
      return;
    }

    int adults = flightBookingDetails.getAdultsCount();
    int children = flightBookingDetails.getChildrenCount();
    int infants = flightBookingDetails.getInfantsCount();
    boolean aTFlag = true;
    boolean cTFlag = true;
    boolean iTFlag = true;
    boolean aDFlag = true;
    boolean cDFlag = true;
    boolean iDFlag = true;

    for (int flight = 0; flight < flightList.size(); flight++) {
      TransactionRuleElement transactionElement =
          new TransactionRuleElement(flightList.get(flight), media, flightType);
      transactionElement.setTravellerType(Traveller.ADULT);

      TransactionRuleElement adultRule = repositoryParser.getTransactionRule(transactionElement);
      transactionElement.setTravellerType(Traveller.CHILD);
      TransactionRuleElement childRule = repositoryParser.getTransactionRule(transactionElement);
      transactionElement.setTravellerType(Traveller.INFANT);
      TransactionRuleElement infantRule = repositoryParser.getTransactionRule(transactionElement);

      if (aTFlag) {
        aTFlag =
            updateFee(adultRule.getTransactionFee(), adultTotalFare, adults,
                discountTransactionFee, 0);
      }

      if (aDFlag) {
        aDFlag =
            updateFee(adultRule.getDiscount(), adultTotalFare, adults, discountTransactionFee, 1);
      }

      if (cTFlag) {
        cTFlag =
            updateFee(childRule.getTransactionFee(), childTotalFare, children,
                discountTransactionFee, 0);
      }

      if (cDFlag) {
        cDFlag =
            updateFee(childRule.getDiscount(), childTotalFare, children, discountTransactionFee, 1);
      }

      if (iTFlag) {
        iTFlag =
            updateFee(infantRule.getTransactionFee(), infantTotalFare, infants,
                discountTransactionFee, 0);
      }

      if (iDFlag) {
        iDFlag =
            updateFee(infantRule.getDiscount(), infantTotalFare, infants, discountTransactionFee, 1);
      }

    }

  }

  private boolean updateFee(String amountString, Double fare, int count,
      List<Double> discountTransactionFee, int index) {
    if (fare == null) {
      return false;
    }

    Double fee = discountTransactionFee.get(index);

    if (StringUtils.contains(amountString, "%")) {
      Double percentage = NumberUtility.getAmountFromString(amountString);
      fee += (fare * percentage) / 100;
      discountTransactionFee.set(index, fee);
      return false;
    } else {
      Double amount = NumberUtility.getAmountFromString(amountString);
      fee += count * amount;
    }

    discountTransactionFee.set(index, fee);
    return true;
  }

  private Double getAmount(Double totalFare, int totalCount, String chargeString) {

    if (totalFare == null) {
      return 0.0;
    }
    Double fee = 0.0;

    if (StringUtils.contains(chargeString, "%")) {
      Double percentage = NumberUtility.getAmountFromString(chargeString);
      fee = (totalFare * percentage) / 100;
    } else {
      Double amount = NumberUtility.getAmountFromString(chargeString);
      fee = totalCount * amount;
    }

    return fee;
  }
}
