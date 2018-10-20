package com.via.appmodules.payment;

import lombok.Getter;
import lombok.Setter;

import com.via.utils.RepositoryParser;

@Getter
@Setter
public class CreditCardDetails {
  private static final String CREDIT_CARD_MASTER = "CreditCardMASTER";
  private static final String CREDIT_CARD_VISA = "CreditCardVISA";
  private static final String CREDIT_CARD_HOLDER_NAME = "CreditCardHolderNames";
  private static final String CREDIT_CARD_VALIDITY_MONTH = "CreditCardValidityMonths";
  private static final String CREDIT_CARD_VALIDITY_YEAR = "CreditCardValidityYears";
  private static final String CREDIT_CARD_CVV_NO = "CreditCardCVVNos";
  private static final String MASTER = "Master";
  private static final String VISA = "Visa";
  private String cCNo;
  private String cCHolderName;
  private String cCValidityMonth;
  private String cCValidityYear;
  private String cCCVVNo;
  public static int cardCount;

  public static CreditCardDetails cardDetails(RepositoryParser repositoryParser, int sNo,String paymentType) {
    String creditCardNo[]=null;
    switch(paymentType){
      case MASTER:
        creditCardNo = repositoryParser.getPropertyValue(CREDIT_CARD_MASTER).split(",");
        break;
      case VISA:
        creditCardNo = repositoryParser.getPropertyValue(CREDIT_CARD_VISA).split(",");
        break;
    }
    String creditcardHolderName[] =
        repositoryParser.getPropertyValue(CREDIT_CARD_HOLDER_NAME).split(",");
    String creditCardValiodityMonth[] =
        repositoryParser.getPropertyValue(CREDIT_CARD_VALIDITY_MONTH).split(",");
    String creditCardvalidityYear[] =
        repositoryParser.getPropertyValue(CREDIT_CARD_VALIDITY_YEAR).split(",");
    String creditCardCVVNo[] = repositoryParser.getPropertyValue(CREDIT_CARD_CVV_NO).split(",");
    CreditCardDetails cCDetails;

    int size = creditCardNo.length;
    sNo %= size;
    cCDetails = new CreditCardDetails();
    cCDetails.setCCNo(creditCardNo[sNo]);
    cCDetails.setCCHolderName(creditcardHolderName[sNo]);
    cCDetails.setCCValidityMonth(creditCardValiodityMonth[sNo]);
    cCDetails.setCCValidityYear(creditCardvalidityYear[sNo]);
    cCDetails.setCCCVVNo(creditCardCVVNo[sNo]);
    return cCDetails;
  }
}
