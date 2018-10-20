package com.via.utils;

public class Constant {

  public enum Flight {
    DOMESTIC, INTERNATIONAL;
  }

  public enum VIA_COUNTRY {
    ID, UAE, SG, PH, TH, IN_CORP, OM, SA, HK;
  }

  public enum BOOKING_MEDIA {
    B2B, B2C, CORP;
  }

  public enum Journey {
    ONE_WAY, ROUND_TRIP, MULTI_CITY;
  }

  public enum HOTEL_FLOW {
    CITY_SEARCH, NAME_SEARCH;
  }

  public enum TRAIN_TYPE {
    RAILINK, KAI;
  }

  public enum Traveller {
    ADULT, CHILD, INFANT;
  }

  public enum Product {
    Flight, Hotel, Holiday, ERecharge, Rail;
  }

  public static final String RESELLER_CORP_COMMON_LOCATOR_REPOSITORY =
      "ResellerCommonCorpLocatorRepository.JSON";
  public static final String COMMON_LOCATOR_REPOSITORY = "CommonLocatorRepository.JSON";
  public static final String FLIGHTS_COMMON_LOCATOR_REPOSITORY =
      "FlightsCommonLocatorRepository.JSON";
  public static final String FLIGHTS_DOM_LOCATOR_REPOSITORY = "FlightsDomLocatorRepository.JSON";
  public static final String FLIGHTS_CORP_DOM_LOCATOR_REPOSITORY = "FlightsCorpDomLocatorRepository.JSON";
  public static final String FLIGHTS_INTL_LOCATOR_REPOSITORY = "FlightsIntlLocatorRepository.JSON";
  public static final String FLIGHTS_CORP_INTL_LOCATOR_REPOSITORY = "FlightsCorpIntlLocatorRepository.JSON";
  public static final String HOTELS_LOCATOR_REPOSITORY = "HotelsLocatorRepository.JSON";
  public static final String HOLIDAYS_LOCATOR_REPOSITORY = "HolidaysLocatorRepository.JSON";
  public static final String TRAINS_LOCATOR_REPOSITORY = "TrainsCommonLocatorRepository.JSON";
  public static final String ERECHARGE_LOCATOR_REPOSITORY = "ERechargeLocatorRepository.JSON";
  public static final String BOOKING_DETAILS_LOCATOR_REPOSITORY =
      "BookingDetailsLocatorRepository.JSON";

  public static final String RESELLER_COMMON_LOCATOR_REPOSITORY =
      "ResellerCommonLocatorRepository.JSON";

  public static final String CONFIGURATION_PROPERTIES = "Config.properties";

  public static final String MAC_CHROMEDRIVER_PATH = System.getProperty("user.dir")
      + "/Driver/macchromedriver";

  public static final String IE_DRIVER_PATH = System.getProperty("user.dir")
      + "/Driver/IEDriverServer.exe";
  public static final String FIREFOX_GECKODRIVER_PATH = System.getProperty("user.dir")
      + "/Driver/geckodriver";

  public static final String WINDOWS_CHROMEDRIVER_PATH = System.getProperty("user.dir")
      + "/Driver/windowschromedriver.exe";

  public static final String SCREENSHOTS_PATH = System.getProperty("user.dir") + "/screenshots/";

  public static final int IMPLICIT_WAIT_TIME = 45;

  public static final String ISD_CODE = "91";
  public static final String CONTACT_MOBILE = "9611577993";
  public static final String CONTACT_MOBILE_COMPLETE = "+91-9611577993";
  public static final String CONTACT_LANDLINE = "8040433000,9723560302";
  public static final String CONTACT_EMAIL = "qa@via.com";
  public static final String CONTACT_CITY = "Bangalore";
  public static final String CONTACT_PINCODE = "560045";
  public static final String CONTACT_STREET = "Manyata";

  public static final String WHITESPACE = " ";
  public static final String COMMA = ",";
  public static final String SLASH = "/";
  public static final String COLON = ":";
  public static final String PIPE = "|";
  public static final String UNDERSCORE = "_";
  public static final String HYPHEN = "-";
  public static final String FULLSTOP = ".";
  public static final String NEW_LINE = "\\r?\\n";

  public static final String TOPUP_MOBILE = "Mobile";
  public static final String TOPUP_DTH = "DTH";
  public static final String TOPUP_BILL_PAYMENT = "Bill Payment";
  public static final String TOPUP_DATA_CARD = "Data Card";

  public static final String TH_FLIGHTS_TESTDATA = "/TestData/Thailand/FlightExcelSheet.xlsx";
  public static final String ID_FLIGHTS_TESTDATA = "/TestData/Indonesia/FlightExcelSheet.xlsx";
  public static final String UAE_FLIGHTS_TESTDATA = "/TestData/Uae/FlightExcelSheet.xlsx";
  public static final String OM_FLIGHTS_TESTDATA = "/TestData/Oman/FlightExcelSheet.xlsx";
  public static final String SA_FLIGHTS_TESTDATA = "/TestData/Saudi/FlightExcelSheet.xlsx";
  public static final String PH_FLIGHTS_TESTDATA = "/TestData/Philippines/FlightExcelSheet.xlsx";
  public static final String IN_FLIGHTS_TESTDATA = "/TestData/India/FlightExcelSheet.xlsx";
  public static final String SG_FLIGHTS_TESTDATA = "/TestData/Singapore/FlightExcelSheet.xlsx";
  public static final String HK_FLIGHTS_TESTDATA = "/TestData/Hongkong/FlightExcelSheet.xlsx";

  public static final String FLIGHTS_DOM_SHEETNAME = "DomFlight";
  public static final String FLIGHTS_INTL_SHEETNAME = "International";
  public static final String FLIGHTS_DOM_RESELLER_SHEETNAME = "DomFlightReseller";
  public static final String FLIGHTS_INTL_RESELLER_SHEETNAME = "InternationalReseller";

  public static final String SG_HOTELS_TESTDATA = "/TestData/Singapore/HotelExcelSheet.xlsx";
  public static final String PH_HOTELS_TESTDATA = "/TestData/Philippines/HotelExcelSheet.xlsx";
  public static final String ID_HOTELS_TESTDATA = "/TestData/Indonesia/HotelExcelSheet.xlsx";
  public static final String TH_HOTELS_TESTDATA = "/TestData/Thailand/HotelExcelSheet.xlsx";
  public static final String HK_HOTELS_TESTDATA = "/TestData/Hongkong/HotelExcelSheet.xlsx";
  public static final String UAE_HOTELS_TESTDATA = "/TestData/Uae/HotelExcelSheet.xlsx";
  public static final String OM_HOTELS_TESTDATA = "/TestData/Oman/HotelExcelSheet.xlsx";
  public static final String SA_HOTELS_TESTDATA = "/TestData/Saudi/HotelExcelSheet.xlsx";
  public static final String Corp_HOTELS_TESTDATA="/TestData/India/CorpHotelExcelSheet.xlsx";

  public static final String HOTELS_SHEETNAME = "Hotels";
  public static final String HOTELS_RESELLER_SHEETNAME = "HotelsReseller";

  public static final String ID_TRAINS_TESTDATA = "/TestData/Indonesia/TrainExcelSheet.xlsx";

  public static final String TRAINS_SHEETNAME = "Trains";
  public static final String TRAINS_RESELLER_SHEETNAME = "TrainsReseller";

  public static final String TOPUP_TESTDATA = "/TestData/TopupExcelSheet.xlsx";
  public static final String TOPUP_SHEETNAME = "topups";

  public static final String ID_HOLIDAYS_TESTDATA = "/TestData/Indonesia/HolidayExcelSheet.xlsx";
  public static final String PH_HOLIDAYS_TESTDATA = "/TestData/Philippines/HolidayExcelSheet.xlsx";
  public static final String UAE_HOLIDAYS_TESTDATA = "/TestData/Uae/HolidayExcelSheet.xlsx";
  public static final String SG_HOLIDAYS_TESTDATA = "/TestData/Singapore/HolidayExcelSheet.xlsx";
  public static final String HOLIDAYS_SHEETNAME = "Holidays";
  public static final String HOLIDAYS_RESELLER_SHEETNAME = "HolidaysReseller";

  public static final String ID_ERECHARGE_TESTDATA = "/TestData/Indonesia/ERechargeExcelSheet.xlsx";
  public static final String ERECHARGE_RESELLER_SHEETNAME = "ERechargeReseller";

  public static final String DEPART_CAL_ID = "depart-cal";
  public static final String RETURN_CAL_ID = "return-cal";
  public static final String ENQUIRY_DEPART_CAL_ID = "enquiryDep-cal";
  public static final String SG_TRANSACTION_RULE_JSON = System.getProperty("user.dir")
      + "/TransactionRule/SGTransactionRule.JSON";
  public static final String TH_TRANSACTION_RULE_JSON = System.getProperty("user.dir")
      + "/TransactionRule/THTransactionRule.JSON";
  public static final String UAE_TRANSACTION_RULE_JSON = System.getProperty("user.dir")
      + "/TransactionRule/UAETransactionRule.JSON";
  public static final String SA_TRANSACTION_RULE_JSON = System.getProperty("user.dir")
      + "/TransactionRule/SAUDITransactionRule.JSON";
  public static final String ID_TRANSACTION_RULE_JSON = System.getProperty("user.dir")
      + "/TransactionRule/IDTransactionRule.JSON";
  public static final String PH_TRANSACTION_RULE_JSON = System.getProperty("user.dir")
      + "/TransactionRule/PHTransactionRule.JSON";

  public static final String AMOUNT_WITH_CONV = "convAmount";
  public static final String BOOKING_VERIFICATION = "bookingVerification";
  public static final String DD_MM_YYYY = "dd-MM-yyyy";
  public static final String DD_MM_YYYY_HH_MM_SS = "dd-MM-yyyy HH:mm:ss";
  public static final String HH_MM_SS = "HH:mm:ss";

  public interface BankConstants {
    public static final String SBI = "STATE BANK OF INDIA";
    public static final String AXIS = "AXIS BANK";
    public static final String HDFC = "HDFC BANK RETAIL";
    public static final String ICICI = "ICICI BANK";
    public static final String KMB = "KOTAK MAHINDRA BANK";
    public static final String PNB = "PNB RETAIL ACCOUNTS";
    public static final String ALLAHABAD = "ALLAHABAD BANK";
    public static final String ANDHRA = "ANDHRA BANK";
    public static final String BAHRAIN_BANK = "BANK OF BAHRAIN AND KUWAIT";
    public static final String BOBCA = "BANK OF BARODA CORPORATE ACCOUNTS";
    public static final String BOBRA = "BANK OF BARODA RETAIL ACCOUNTS";
    public static final String BOI = "BANK OF INDIA";
    public static final String BOM = "BANK OF MAHARASHTRA";
    public static final String CANARA = "CANARA BANK";
    public static final String CSB = "CATHOLIC SYRIAN BANK";
    public static final String CBI = "CENTRAL BANK OF INDIA";
    public static final String CITI = "CITI BANK";
    public static final String CUB = "CITY UNION BANK";
    public static final String CORPORATION = "CORPORATION BANK";
    public static final String DBS = "DBS Bank Ltd";
    public static final String DCBB = "DCB Bank Business";
    public static final String DCBP = "DCB Bank Personal";
    public static final String DEUTSCHE = "DEUTSCHE BANK";
    public static final String DHANLAXMI = "DHANLAXMI BANK";
    public static final String FEDERAL = "FEDERAL BANK";
    public static final String IDBI = "IDBI BANK";
    public static final String INDIAN = "INDIAN BANK";
    public static final String IOB = "INDIAN OVERSEAS NET BANKING";
    public static final String INDUSIND = "INDUSIND BANK";
    public static final String JKB = "JAMMU AND KASHMIR BANK";
    public static final String KARNATAKA = "KARNATAKA BANK";
    public static final String KVB = "KARUR VYSYA BANK";
    public static final String ING = "Kotak (ING Vysya)";
    public static final String LVB = "LAKSHMI VILAS NETBANKING";
    public static final String OBC = "ORIENTAL BANK OF COMMERCE";
    public static final String PNBC = "PNB CORPORATE ACCOUNTS";
    public static final String SARASWAT = "Saraswat Bank";
    public static final String SIB = "SOUTH INDIAN BANK";
    public static final String SCB = "STANDARD CHARTERED BANK";
    public static final String SBJ = "STATE BANK OF BIKANER AND JAIPUR";
    public static final String SBH = "STATE BANK OF HYDERABAD";
    public static final String SBM = "STATE BANK OF MYSORE";
    public static final String SBP = "STATE BANK OF PATIALA";
    public static final String SBT = "STATE BANK OF TRAVANCORE";
    public static final String SYNDIACATE = "SYNDIACATE BANK";
    public static final String UBI = "UNION BANK OF INDIA";
    public static final String UTBI = "UNITED BANK OF INDIA";
    public static final String VIJAYA = "VIJAYA BANK";
    public static final String YES = "YES BANK";
  }

  public interface CityConstantsHotels {
    public static final String BANGALORE = "Bangalore,Karnataka,India";
    public static final String DELHI = "Delhi,New Delhi,India";
    public static final String GOA = "Goa,Goa,India";
    public static final String KOLKATA = "Kolkata,West Bengal,India";
    public static final String MUMBAI = "Mumbai,Maharashtra,India";
    public static final String JAIPUR = "Jaipur,Rajasthan,India";
    public static final String VARANASI = "Varanasi,Uttar Pradesh,India";
    public static final String HYDERABAD = "Hyderabad,Andhra Pradesh,India";
    public static final String CHENNAI = "Chennai,Tamil Nadu,India";
    public static final String KUL = "Kuala Lumpur,Malaysia";
    public static final String HKG = "Hong Kong,Hong Kong";
    public static final String BALI = "Bali,Indonesia";
    public static final String CGK = "Jakarta,Indonesia";
    public static final String BD = "Bandung,Indonesia";
    public static final String SUB = "Surabaya,Indonesia";
    public static final String YOG = "Yogyakarta,Indonesia";
    public static final String BPN = "Balikpapan,Indonesia";
    public static final String SIN = "Singapore,Singapore";
    public static final String SAN = "Shanghai,China";
    public static final String BKK = "Bangkok,Thailand";
    public static final String DXB = "Dubai,United Arab Emirates";
    public static final String OMN = "Muscat,Oman";
    public static final String AMN = "Ajman,United Arab Emirates";
    public static final String SHJ = "Sharjah,United Arab Emirates";
    public static final String AMM = "Amman,Jordan";
    public static final String IXE = "Mangalore,Karnataka,India";
    public static final String PUE = "Puerto Princesa,Philippines";
    public static final String MNL = "Manila,Philippines";
    public static final String ELN = "El Nido - Palawan,Philippines";
    public static final String CEB = "Cebu,Philippines";
    public static final String KNO = "Medan,Indonesia";
    public static final String UMM = "Umm al Quwain,United Arab Emirates";

  }

  public interface CityConstantsFlights {
    public static final String RJA = "Rajahmundry - India";
    public static final String BOM = "Mumbai - India";
    public static final String BLR = "Bangalore - India";
    public static final String DEL = "Delhi - India";
    public static final String IXC = "Chandigarh - India";
    public static final String GOI = "Goa - India";
    public static final String GAU = "Guwahati - India";
    public static final String CCU = "Kolkata - India";
    public static final String JAI = "Jaipur - India";
    public static final String SXR = "Srinagar - India";
    public static final String COK = "Kochi - India";
    public static final String PNQ = "Pune - India";
    public static final String VTZ = "Vigaz - India";
    public static final String IMF = "Imphal - India";
    public static final String MAA = "Chennai - India";
    public static final String AMD = "Ahmedabad - India";
    public static final String HYD = "Hyderabad - India";
    public static final String IXM = "Madurai - India";
    public static final String TRV = "Trivandrum - India";
    public static final String IXE = "Mangalore - India";
    public static final String IXB = "Bagdogra - India";
    public static final String IXZ = "Port Blair - India";
    public static final String RPR = "Raipur - India";
    public static final String HBX = "Hubli - India";
    public static final String VNS = "Varanasi - India";
    public static final String VGA = "Vijaywada - India";
    public static final String CJB = "Coimbatore - India";
    public static final String TIR = "Tirupati - India";
    public static final String ATQ = "Amritsar - India";

    public static final String SIN = "Changi Intl Arpt - Singapore";
    public static final String DXB = "Dubai Intl Arpt - United Arab Emirates";
    public static final String BKK = "Suvarnabhumi Intl Arpt - Thailand";
    public static final String DMK = "Don Mueang Intl Arpt - Thailand";
    public static final String KTM = "Tribuvan Arpt - Nepal";
    public static final String MCT = "Seeb Intl - Oman, Sultanate Of";
    public static final String KUL = "Kuala Lumpur International Arpt - Malaysia";
    public static final String SHJ = "Sharjah Airport - United Arab Emirates";
    public static final String CEB = "Cebu - Philippines";
    public static final String MNL = "Manila - Philippines";
    public static final String HKT = "Phuket Intl Airport - Thailand";
    public static final String CNX = "Chiang Mai Intl Arpt - Thailand";
    public static final String UTH = "Udon Thani Arpt - Thailand";
    public static final String URT = "Surat Thani Arpt - Thailand";
    public static final String CEI = "Chaing Rai Arpt - Thailand";
    public static final String KBV = "Krabi Arpt - Thailand";
    public static final String CGK = "Jakarta-soekarno Hatta - Indonesia";
    public static final String DPS = "Bali/ Denpasar - Indonesia";
    public static final String SUB = "Surabaya - Indonesia";
    public static final String BTH = "Batam - Indonesia";
    public static final String BPN = "Balikpapan - Indonesia";
    public static final String KNO = "Medan/ Kualanamu - Indonesia";
    public static final String UPG = "Makassar/ Ujung Pandang - Indonesia";
    public static final String JOG = "Yogyakarta - Indonesia";
    public static final String KOE = "Kupang - Indonesia";
    public static final String MOF = "Maumere - Indonesia";
    public static final String PNK = "Pontianak - Indonesia";
    public static final String PLM = "Palembang - Indonesia";
    public static final String PGK = "Pangkalpinang - Indonesia";
    public static final String WGP = "Waingapu - Indonesia";
    public static final String HKG = "Hong Kong Intl - Hong Kong";
    public static final String BCD = "Bacolod - Philippines";
    public static final String KLO = "Kalibo - Philippines";
    public static final String ILO = "Iloilo - Philippines";
    public static final String RXS = "Roxas City - Philippines";
    public static final String PAG = "Pagadian - Philippines";
    public static final String DVO = "Davao - Philippines";
    public static final String SUG = "Surigao - Philippines";
    public static final String BSO = "Batanes - Philippines";
    public static final String WNP = "Naga - Philippines";
    public static final String PPS = "Puerto Princesa - Philippines";
    public static final String LGP = "Legaspi - Philippines";
    public static final String VRC = "Virac - Philippines";
    public static final String TAC = "Tacloban - Philippines";
    public static final String AMS = "Schiphol Arpt - Netherlands";
    public static final String SSN = "Seoul Air Base - Korea, Republic Of";
    public static final String SYD = "Sydney Kingsford Smith Arpt - Australia";
    public static final String BTJ = "Banda Aceh - Indonesia";
    public static final String PEN = "Penang Intl Arpt - Malaysia";
    public static final String ICN = "Incheon Intl Arpt - Korea, Republic Of";
    public static final String NRT = "Narita - Japan";
    public static final String KIX = "Kansai International Arpt - Japan";
    public static final String CPH = "Copenhagen Arpt - Denmark";
    public static final String CJU = "Jeju Intl Arpt - Korea, Republic Of";
    public static final String TPE = "Taiwan Taoyuan Intl Arpt - Taiwan, Republic Of China";
    public static final String HTY = "Hatay Airport - Turkey";
    public static final String JED = "Jeddah Intl - Saudi Arabia";
    public static final String LHE = "Lahore Arpt - Pakistan";
    public static final String AMM = "Queen Alia Intl Arpt - Jordan";
    public static final String SRG = "Semarang - Indonesia";
    public static final String PDG = "Padang - Indonesia";
    public static final String SOC = "Solo - Indonesia";
    public static final String BDO = "Bandung - Indonesia";
    public static final String PKU = "Pekan Baru - Indonesia";
    public static final String BDJ = "Banjarmasin - Indonesia";
    public static final String TJQ = "Tanjung Pandan - Indonesia";
    public static final String KCZ = "Kochi Airport - Japan";
    public static final String AXL = "Alexandria Airport - Australia";
    public static final String HBE = "Borg El Arab Arpt - Egypt";
    public static final String TBS = "Novo Alexeyevka Arpt - Georgia";
    public static final String SGN = "Tan Son Nhut Arpt - Vietnam";
    public static final String ZJR = "Penang Port Airport - Malaysia";
    public static final String JKT = "Soekarno–hatta - Indonesia";
    public static final String IST = "Ataturk Arpt - Turkey";
    public static final String DOH = "Doha International Arpt - Qatar";
    public static final String CCJ = "Calicut - India";
    public static final String CAN = "Baiyun Airport - China";
    public static final String AHB = "Abha Airport - Saudi Arabia";
    public static final String AUH = "Abu Dhabi Intl Arpt - United Arab Emirates";
    public static final String DMM = "King Fahad Arpt - Saudi Arabia";
    public static final String BHH = "Bisha Airport - Saudi Arabia";

  }

  public interface CorpCityConstantsFlights {
  public static final String RJA = "Rajahmundry,Rajahmundry - India";
  public static final String BOM = "Mumbai,Mumbai - India";
  public static final String BLR = "Bangalore,Bangalore - India";
  public static final String DEL = "Delhi,Delhi - India";
  public static final String IXC = "Chandigarh,Chandigarh - India";
  public static final String GOI = "Goa,Goa - India";
  public static final String GAU = "Guwahati,Guwahati(gauhati) - India";
  public static final String CCU = "Kolkata,Kolkata - India";
  public static final String JAI = "Jaipur,Jaipur - India";
  public static final String SXR = "Srinagar,Srinagar - India";
  public static final String COK = "Cochin,Cochin - India";
  public static final String PNQ = "Pune,Pune - India";
  public static final String VTZ = "Vizag,Visakhapatnam - India";
  public static final String IMF = "Imphal,Imphal - India";
  public static final String MAA = "Chennai,Chennai - India";
  public static final String AMD = "Ahmedabad,Ahmedabad - India";
  public static final String HYD = "Hyderabad,Hyderabad - India";
  public static final String IXM = "Madurai,Madurai - India";
  public static final String TRV = "Trivandrum,Trivandrum(thiruvananthapuram) - India";
  public static final String IXE = "Mangalore,Mangalore - India";
  public static final String IXB = "Bagdogra,Bagdogra - India";
  public static final String IXZ = "Port Blair,Portblair - India";
  public static final String RPR = "Raipur,Raipur - India";
  public static final String HBX = "Hubli,Hubli - India";
  public static final String VNS = "Varanasi,Varanasi(benares) - India";
  public static final String VGA = "Vijaywada,Vijayawada - India";
  public static final String CJB = "Coimbatore,Coimbatore - India";
  public static final String TIR = "Tirupati,Tirupati - India";
  public static final String ATQ = "Amritsar,Amritsar - India";

  public static final String SIN = "Changi Intl Arpt,Singapore - Singapore";
  public static final String DXB = "Dubai Intl Arpt,Dubai - United Arab Emirates";
  public static final String BKK = "Suvarnabhumi Intl Arpt,Bangkok - Thailand";
  public static final String KTM = "Tribuvan Arpt,Kathmandu - Nepal";
  public static final String MCT = "Seeb Intl,Muscat - Oman, Sultanate Of";
  public static final String KUL = "Kuala Lumpur International Arpt,Kuala-lumpur - Malaysia";
  public static final String SHJ = "Sharjah Airport,Sharjah - United Arab Emirates";
  public static final String CEB = "Cebu Intl,Cebu - Philippines";
  public static final String MNL = "Ninoy Aquino Intl,Manila - Philippines";
}
  
  public interface CityConstantsHolidays {
    public static final String GOA = "Goa, India";
    public static final String KERALA = "Cochin, Kerala, India";
    public static final String COORG = "Coorg, Karnataka, India";
    public static final String BANGALORE = "Bangalore, Karnataka, India";
    public static final String HONEYMOON = "Honeymoon";
  }

}

