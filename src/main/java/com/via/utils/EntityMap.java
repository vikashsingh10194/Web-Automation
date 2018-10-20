package com.via.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.via.utils.Constant.BankConstants;
import com.via.utils.Constant.CityConstantsFlights;
import com.via.utils.Constant.CityConstantsHotels;
import com.via.utils.Constant.CorpCityConstantsFlights;
import com.via.utils.Constant.Product;
import com.via.utils.Constant.VIA_COUNTRY;

public class EntityMap {

  // The map store the mapping of the city name visible in excel sheet with
  // the city name in autoComplete box

  @SuppressWarnings("serial")
  private static final Map<String, String> airportCityMap = new HashMap<String, String>() {
    {
      
      /***
       * domestic airport India
       ***/
      put("BANGALORE", CityConstantsFlights.BLR);
      put("DELHI", CityConstantsFlights.DEL);
      put("GOA", CityConstantsFlights.GOI);
      put("KOLKATA", CityConstantsFlights.CCU);
      put("MUMBAI", CityConstantsFlights.BOM);
      put("JAIPUR", CityConstantsFlights.JAI);
      put("HYDERABAD", CityConstantsFlights.HYD);
      put("RAJAHMUNDRY", CityConstantsFlights.RJA);
      put("HUBLI", CityConstantsFlights.HBX);
      put("KOCHI", CityConstantsFlights.COK);
      put("TRIVANDRUM", CityConstantsFlights.TRV);
      put("CHENNAI", CityConstantsFlights.MAA);
      put("AHMEDABAD", CityConstantsFlights.AMD);
      put("PUNE", CityConstantsFlights.PNQ);
      put("RAIPUR", CityConstantsFlights.RPR);
      put("CHANDIGARH", CityConstantsFlights.IXC);
      put("AMRITSAR", CityConstantsFlights.ATQ);
      put("MANGALORE", CityConstantsFlights.IXE);
      put("KOZHIKODE", CityConstantsFlights.CCJ);
      put("COIMBATORE", Constant.CityConstantsFlights.CJB);
      put("MADURAI", Constant.CityConstantsFlights.IXM);
      
      /***
       * international airports
       ***/

      put("KATHMANDU", CityConstantsFlights.KTM);
      put("BANGKOK", CityConstantsFlights.BKK);
      put("DUBAI", CityConstantsFlights.DXB);
      put("SINGAPORE", CityConstantsFlights.SIN);
      put("MANILA", CityConstantsFlights.MNL);
      put("KUALA LUMPUR", CityConstantsFlights.KUL);
      put("SHARJAH", CityConstantsFlights.SHJ);
      put("MUSCAT", CityConstantsFlights.MCT);
      put("CHIANG MAI", CityConstantsFlights.CNX);
      put("UDON THANI", CityConstantsFlights.UTH);

      /***
       * Indonesia domestic
       ***/
      put("JAKARTA", CityConstantsFlights.CGK);
      put("BALI/ DENPASAR", CityConstantsFlights.DPS);
      put("PANGKAL PINANG", CityConstantsFlights.PGK);
      put("HONG KONG", CityConstantsFlights.HKG);
      put("PEKANBARU", CityConstantsFlights.PKU);

      /***
       * Philippines Domestic
       */
      put("BASCO", CityConstantsFlights.BSO);
      put("VIRAC", CityConstantsFlights.VRC);

      /***
       * Saudi domestic
       ***/
      put("Abha", CityConstantsFlights.AHB);
      put("ABU DHABI", CityConstantsFlights.AUH);
      put("Jeddah", CityConstantsFlights.JED);
      put("KingFahad", CityConstantsFlights.DMM);
      put("Bisha", CityConstantsFlights.BHH);

      /***
       * International
       ***/

      put("AMSTERDAM", CityConstantsFlights.AMS);
      put("SYDNEY", CityConstantsFlights.SYD);
      put("PENANG", CityConstantsFlights.PEN);
      put("SEOUL", CityConstantsFlights.ICN);
      put("TOKYO", CityConstantsFlights.NRT);
      put("OSAKA", CityConstantsFlights.KIX);
      put("COPENHAGEN", CityConstantsFlights.CPH);
      put("JEJU", CityConstantsFlights.CJU);
      put("TAIPEI", CityConstantsFlights.TPE);
      put("DOHA", CityConstantsFlights.DOH);
      // put("ABU DHABI", CityConstantsFlights.AUH);
      put("AMMAN", CityConstantsFlights.AMM);
      put("ALEXANDRIA", CityConstantsFlights.HBE);
      put("TBILISI", CityConstantsFlights.TBS);
      put("HO CHI MINH", CityConstantsFlights.SGN);
      put("ISTANBUL", CityConstantsFlights.IST);
      put("GUANGZHOU", CityConstantsFlights.CAN);
      put("CHIANG RAI", CityConstantsFlights.CEI);

      /***
       * India international airports
       ***/

      put("NEPAL", Constant.CityConstantsFlights.KTM);
      put("BANGKOK", Constant.CityConstantsFlights.BKK);
      put("DUBAI", Constant.CityConstantsFlights.DXB);
      put("SINGAPORE", Constant.CityConstantsFlights.SIN);
      put("MANILA", Constant.CityConstantsFlights.MNL);
      put("KUALA-LUMPUR", Constant.CityConstantsFlights.KUL);
      put("SHARJAH", Constant.CityConstantsFlights.SHJ);
      put("CEBU", Constant.CityConstantsFlights.CEB);
      put("MUSCAT", Constant.CityConstantsFlights.MCT);
    }
  };

  @SuppressWarnings("serial")
  private static final Map<String, String> corpAirportCityMap = new HashMap<String, String>() {
    {
      
      /***
       * domestic airport India
       ***/
      put("BANGALORE", CorpCityConstantsFlights.BLR);
      put("DELHI", CorpCityConstantsFlights.DEL);
      put("GOA", CorpCityConstantsFlights.GOI);
      put("KOLKATA", CorpCityConstantsFlights.CCU);
      put("MUMBAI", CorpCityConstantsFlights.BOM);
      put("JAIPUR", CorpCityConstantsFlights.JAI);
      put("HYDERABAD", CorpCityConstantsFlights.HYD);
      put("RAJAHMUNDRY", CorpCityConstantsFlights.RJA);
      put("HUBLI", CorpCityConstantsFlights.HBX);
      put("KOCHI", CorpCityConstantsFlights.COK);
      put("TRIVANDRUM", CorpCityConstantsFlights.TRV);
      put("CHENNAI", CorpCityConstantsFlights.MAA);
      put("AHMEDABAD", CorpCityConstantsFlights.AMD);
      put("PUNE", CorpCityConstantsFlights.PNQ);
      put("RAIPUR", CorpCityConstantsFlights.RPR);
      put("CHANDIGARH", CorpCityConstantsFlights.IXC);
      put("AMRITSAR", CorpCityConstantsFlights.ATQ);
      put("MANGALORE", CorpCityConstantsFlights.IXE);
      put("COIMBATORE", CorpCityConstantsFlights.CJB);
      put("MADURAI", CorpCityConstantsFlights.IXM);
      
      /***
       * international airports
       ***/

      put("KATHMANDU", CorpCityConstantsFlights.KTM);
      put("BANGKOK", CorpCityConstantsFlights.BKK);
      put("DUBAI", CorpCityConstantsFlights.DXB);
      put("SINGAPORE", CorpCityConstantsFlights.SIN);
      put("MANILA", CorpCityConstantsFlights.MNL);
      put("KUALA LUMPUR", CorpCityConstantsFlights.KUL);
      put("SHARJAH", CorpCityConstantsFlights.SHJ);
      put("MUSCAT", CorpCityConstantsFlights.MCT);
      /***
       * India international airports
       ***/

      put("NEPAL", CorpCityConstantsFlights.KTM);
      put("BANGKOK", CorpCityConstantsFlights.BKK);
      put("DUBAI", CorpCityConstantsFlights.DXB);
      put("SINGAPORE", CorpCityConstantsFlights.SIN);
      put("MANILA", CorpCityConstantsFlights.MNL);
      put("KUALA-LUMPUR", CorpCityConstantsFlights.KUL);
      put("SHARJAH", CorpCityConstantsFlights.SHJ);
      put("CEBU", CorpCityConstantsFlights.CEB);
      put("MUSCAT", CorpCityConstantsFlights.MCT);
    }
  };

  
  public static String getAirportCityName(VIA_COUNTRY country, String cityName) {
    cityName = StringUtils.upperCase(cityName);
    String cityNameFromMap = null;
    if(country == Constant.VIA_COUNTRY.IN_CORP){
      cityNameFromMap = corpAirportCityMap.get(cityName);
    }else{
      cityNameFromMap = airportCityMap.get(cityName);
    }    
    if (cityNameFromMap == null) {
      return cityName;
    }
    return StringUtils.upperCase(cityNameFromMap);
  }

  public static void replaceAirportCity(String cityName, String airportName) {
    airportCityMap.put(StringUtils.upperCase(cityName), airportName);
  }

  @SuppressWarnings("serial")
  private static Map<String, String> hotelsCityMap = new HashMap<String, String>() {
    {
      put("BANGALORE", CityConstantsHotels.BANGALORE);
      put("DELHI", CityConstantsHotels.DELHI);
      put("GOA", CityConstantsHotels.GOA);
      put("KOLKATA", CityConstantsHotels.KOLKATA);
      put("MUMBAI", CityConstantsHotels.MUMBAI);
      put("JAIPUR", CityConstantsHotels.JAIPUR);
      put("VARANASI", CityConstantsHotels.VARANASI);
      put("HYDERABAD", CityConstantsHotels.HYDERABAD);
      put("CHENNAI", CityConstantsHotels.CHENNAI);
      put("KUALA LUMPUR", CityConstantsHotels.KUL);
      put("HONG KONG", CityConstantsHotels.HKG);
      put("BALI", CityConstantsHotels.BALI);
      put("JAKARTA", CityConstantsHotels.CGK);
      put("BANDUNG", CityConstantsHotels.BD);
      put("SURABAYA", CityConstantsHotels.SUB);
      put("YOGYAKARTA", CityConstantsHotels.YOG);
      put("BALIKPAPAN", CityConstantsHotels.BPN);
      put("SINGAPORE", CityConstantsHotels.SIN);
      put("SANGHAI", CityConstantsHotels.SAN);
      put("BANGKOK", CityConstantsHotels.BKK);
      put("MEDAN", CityConstantsHotels.KNO);
      put("DUBAI", CityConstantsHotels.DXB);
      put("OMAN", CityConstantsHotels.OMN);
      put("AJMAN", CityConstantsHotels.AMN);
      put("SHARJAH", CityConstantsHotels.SHJ);
      put("AMMAN", CityConstantsHotels.AMM);
      put("MUSCAT", CityConstantsHotels.OMN);
      put("MANGALORE", CityConstantsHotels.IXE);
      put("PUERTO PRINCESA", CityConstantsHotels.PUE);
      put("MANILA", CityConstantsHotels.MNL);
      put("SINGAPORE", CityConstantsHotels.SIN);
      put("EL NIDO", CityConstantsHotels.ELN);
      put("CEBU", CityConstantsHotels.CEB);
      put("UMM AL QUWAIN", CityConstantsHotels.UMM);
    }
  };

  public static String getHotelCityName(String hotelCity) {
    hotelCity = StringUtils.upperCase(hotelCity);
    String hotelCityFromMap = hotelsCityMap.get(hotelCity);
    if (hotelCityFromMap == null) {
      return hotelCity;
    }
    return StringUtils.upperCase(hotelCityFromMap);
  }

  @SuppressWarnings("serial")
  private static Map<String, String> hotelsNameMap = new HashMap<String, String>() {
    {
      put("OXFORD GOLF AND COUNTRY CLUB", "Oxford Golf and Country Club,Pune");
      put("JW MARRIOTT HOTEL PUNE", "JW Marriott Hotel Pune,Pune");
      put("TAJ WHITEFIELD", "Vivanta By Taj, Whitefield,Bangalore");
      put("IBIS BENGALURU TECHPARK", "Ibis Bengaluru Techpark,Bangalore");
      put("GINGER BANGALORE", "Ginger Bangalore Whitefield,Bangalore");
      put("VICEROY BALI", "Viceroy Bali,Ubud");
      put("GRAND HYATT JAKARTA", "Grand Hyatt Jakarta,Jakarta");
      put("NIDA ROOMS UMAR SESAPI DENPASAR", "NIDA Rooms Umar Sesapi Denpasar,Bali");
      put("PANORAMA DEIRA HOTEL", "Panorama Deira Hotel,Dubai");
      put("RUSS HILL HOTEL", "Russ Hill Hotel,London");

      put("JAMP PENSION", "Jamp Pension,Puerto Princesa");
      put("SOLAIRE RESORT AND CASINO", "Solaire Resort And Casino,Paranaque");
      put("IBIS SINGAPORE NOVENA", "ibis Singapore Novena,Singapore");
      put("CROWNE PLAZA HONG KONG CAUSEWAY BAY", "Crowne Plaza Hong Kong Causeway Bay,Hong Kong");
      put("NOVOTEL MUMBAI JUHU BEACH", "Novotel Mumbai Juhu Beach,Mumbai");
      put("ANGELIC TOURIST INN EL NIDO,EL NIDO", "Angelic Tourist Inn El Nido,El Nido - Palawan");
      put("CEBU GUEST HOUSE,CEBU", "Cebu Guest House,Cebu");
    }
  };

  public static String getHotelName(String hotelName) {
    hotelName = StringUtils.upperCase(hotelName);
    String hotelNameFromMap = hotelsNameMap.get(hotelName);
    if (hotelNameFromMap == null) {
      return hotelName;
    }
    return StringUtils.upperCase(hotelNameFromMap);
  }

  @SuppressWarnings("serial")
  private static Map<String, String> holidaysCityMap = new HashMap<String, String>() {
    {

    }
  };

  public static String getHolidayCity(String city) {
    city = StringUtils.upperCase(city);
    String cityFromMap = holidaysCityMap.get(city);
    if (cityFromMap == null) {
      return city;
    }

    return cityFromMap;
  }

  @SuppressWarnings("serial")
  private static final Map<String, String> bankMap = new HashMap<String, String>() {
    {
      put("SBI", BankConstants.SBI);
      put("AXIS", BankConstants.AXIS);
      put("HDFC", BankConstants.HDFC);
      put("ICICI", BankConstants.ICICI);
      put("KMB", BankConstants.KMB);
      put("PNB", BankConstants.PNB);
      put("ALLAHABAD", BankConstants.ALLAHABAD);
      put("ANDHRA", BankConstants.ANDHRA);
      put("BAHRAIN", BankConstants.BAHRAIN_BANK);
      put("BOBCA", BankConstants.BOBCA);
      put("BOBRA", BankConstants.BOBRA);
      put("BOI", BankConstants.BOI);
      put("BOM", BankConstants.BOM);
      put("CANARA", BankConstants.CANARA);
      put("CSB", BankConstants.CSB);
      put("CBI", BankConstants.CBI);
      put("CITI", BankConstants.CITI);
      put("CUB", BankConstants.CUB);
      put("CORPORATION", BankConstants.CORPORATION);
      put("DBS", BankConstants.DBS);
      put("DCBB", BankConstants.DCBB);
      put("DCBP", BankConstants.DCBP);
      put("DEUTSCHE", BankConstants.DEUTSCHE);
      put("DHANLAXMI", BankConstants.DHANLAXMI);
      put("FEDERAL", BankConstants.FEDERAL);
      put("IDBI", BankConstants.IDBI);
      put("INDIAN", BankConstants.INDIAN);
      put("IOB", BankConstants.IOB);
      put("INDUSIND", BankConstants.INDUSIND);
      put("JKB", BankConstants.JKB);
      put("KARNATAKA", BankConstants.KARNATAKA);
      put("KVB", BankConstants.KVB);
      put("ING", BankConstants.ING);
      put("LVB", BankConstants.LVB);
      put("OBC", BankConstants.OBC);
      put("PNBC", BankConstants.PNBC);
      put("SARASWAT", BankConstants.SARASWAT);
      put("SIB", BankConstants.SIB);
      put("SCB", BankConstants.SCB);
      put("SBJ", BankConstants.SBJ);
      put("SBH", BankConstants.SBH);
      put("SBM", BankConstants.SBM);
      put("SBP", BankConstants.SBP);
      put("SBT", BankConstants.SBT);
      put("SYNDIACATE", BankConstants.SYNDIACATE);
      put("UBI", BankConstants.UBI);
      put("UTBI", BankConstants.UTBI);
      put("VIJAYA", BankConstants.VIJAYA);
      put("YES", BankConstants.YES);
    }
  };

  public static String getBankName(String bankCode) {
    return bankMap.get(bankCode);
  }

  @SuppressWarnings("serial")
  private static final Map<String, String> airlinesMap = new HashMap<String, String>() {
    {
      put("INDIGO", "6E");
      put("SPICEJET", "SG");
      put("JETAIRWAYS", "9W");
      put("GOAIR", "G8");
      put("AIRINDIA", "AI");
      put("VISTARA", "UK");
      put("AIRASIA_INDIA", "I5");
      put("AIR_COSTA", "LB");
      put("AIRASIA", "I5");
      put("AIRCOSTA", "LB");
      put("AIR_PEGASUS", "OP");
      put("AIRPEGASUS", "OP");
      put("TRUEJET", "2T");
      put("TIGERAIRWAYS", "TR");
      put("AIRASIAINTL", "AK");
      put("AIRARABIA", "G9");
      //put("SCOOT", "TZ");
      put("SCOOT", "TR");
      put("ZESTAIR", "Z2");
      put("ETIHAD", "EY");
      put("THAILION", "SL");
      put("AIRASIATHAI", "FD");
      put("CITILINK", "QG");
      put("SRIWIJAYA", "SJ");
      put("AIRASIAINDONESIA", "QZ");
      put("AIRASIAEXTRA", "XT");
      put("LIONAIRWAYS", "JT");
      put("BATIKAIR", "ID");
      put("GARUDA", "GA");
      put("NAMAIR", "IN");
      put("WINGSAIR", "IW");
      put("KALSTAR", "KD");
      put("CEBUPACIFIC", "5J");
      put("PHILIPPINEAIRLINES", "PR");
      put("CEBGO", "DG");
      put("SKYJET", "M8");
      put("MALAYSIAAIRLINES", "MH");
      put("SINGAPOREAIRLINES", "SQ");
      put("MALINDO", "OD");
      put("CATHAYPACIFIC", "CX");
      put("EXPRESSAIR", "XN");
      put("THAI", "TG");
      put("FIREFLY", "FY");
      put("EVAAIRWAYS", "BR");
      put("ANA", "NH");
      put("EMIRATES", "EK");
      put("TURKISHAIR", "TK");
      put("CHINAEASTERNAIRLINES", "MU");
      put("CHINASOUTHERNAIRLINES", "CZ");
      put("XIAMENAIRLINES", "");
      put("JETSTARASIA", "3K");
      put("KOREANAIR", "KE");
      put("VIETNAMAIRLINES", "VN");
      put("ASIANAAIRLINES", "OZ");
      put("XIAMENAIRLINES", "MF");
      put("DELTA", "DL");
      put("CHINAAIRLINES", "CI");
      put("CEBGO", "DG");
      put("FLYDUBAI", "FZ");
      put("OMANAIR", "WY");
      put("QATARAIRWAYS", "QR");
      put("AIRBLUE", "PA");
      put("ROYALJORDANIAN", "RJ");
      put("GULFAIR", "GF");
      put("MYANMAR", "8M");
      put("BANGKOKAIRWAYS", "PG");
      put("KLM", "KL");
      put("FLYNAS", "XY");
      put("SAUDIARABIAN", "SV");
      put("SAUDIGULFAIRWAYS", "6S");
      put("NOKAIR", "DD");
      put("FLYNAS", "XY.");
      put("SALAM","OV");
    }
  };

  public static String getAirlineCode(String AirlineName) {
	  return airlinesMap.get(StringUtils.upperCase(AirlineName));
  }

  @SuppressWarnings("serial")
  private static final Map<String, String> airportCityNameMap = new HashMap<String, String>() {
    {
      put("CHANGI INTL ARPT", "Singapore");
      put("SUVARNABHUMI INTL ARPT", "Bangkok");
      put("DON MUEANG AIRPORT", "Bangkok");
      put("DON MUEANG INTL ARPT", "Bangkok");
      put("DUBAI INTL ARPT", "Dubai");
      put("SEEB INTL", "Muscat");
      put("KUALA LUMPUR INTERNATIONAL ARPT", "Kuala Lumpur");
      put("NINOY AQUINO INTL", "Manila");
      put("BANDARANAIKE INTL ARPT", "Colombo");
      put("HONG KONG INTL", "Hong Kong");
      put("DOHA INTERNATIONAL ARPT", "Doha");
      put("ABU DHABI INTL ARPT", "Abu Dhabi");
      put("ABHA AIRPORT", "Abha"); // Abha Abha Airport
      put("CEBU INTL", "Cebu");
      put("SHARJAH AIRPORT", "Sharjah");
      put("KUWAIT INTL", "Kuwait");
      put("KING KHALED INTL", "Riyadh");
      put("DHABI INTL ARPT", "Abu Dhabi");
      put("ETIHAD TRAVEL MALL", "Dubai");
      put("PHUKET INTL AIRPORT", "Phuket");
      put("CHIANG MAI INTL ARPT", "Chiang Mai");
      put("CHAING RAI ARPT", "Chiang Rai");
      put("SURAT THANI ARPT", "Surat Thani");
      put("UDON THANI ARPT", "Udon Thani");
      put("KRABI ARPT", "Krabi");
      put("JAKARTA-SOEKARNO HATTA", "Jakarta");
      put("MAKASSAR/ UJUNG PANDANG", "Makassar");
      put("HONG KONG INTL", "Hong Kong");
      put("BATANES", "Basco");
      put("INCHEON INTL ARPT", "Seoul");
      put("SYDNEY KINGSFORD SMITH ARPT", "Sydney");
      put("PENANG INTL ARPT", "Penang");
      put("NARITA", "Tokyo");
      put("KANSAI INTERNATIONAL ARPT", "Osaka");
      put("JEJU INTL ARPT", "Jeju");
      put("COPENHAGEN ARPT", "Copenhagen");
      put("TAIWAN TAOYUAN INTL ARPT", "Taipei");
      put("HATAY AIRPORT", "Hatay");
      put("JEDDAH INTL", "Jeddah");
      put("Abha Airport", "Abha");
      put("LAHORE ARPT", "Lahore");
      put("CALICUT", "Kozhikode");
      put("QUEEN ALIA INTL ARPT", "Amman");
      put("BAIYUN AIRPORT", "Guangzhou");
      put("TAN SON NHUT ARPT", "Ho Chi Minh");
      put("NOIBAI ARPT", "Hanoi");
      put("PUDONG INTERNATIONAL ARPT", "Shanghai");

      put("BANDAR LAMPUNG/ TANJUNG KARANG", "Bandar Lampung");
      put("MEDAN/ KUALANAMU", "Medan");
      put("PEKAN BARU", "Pekanbaru");
      put("PANGKALPINANG", "Pangkal pinang");
      put("DUBAI AL MAKTOUM INTERNATIONAL AIRPORT", "United Arab Emirates");
      put("BEIRUT INTL ARPT", "Beirut");
      put("TRIBUVAN ARPT", "Kathmandu");
      put("BORG EL ARAB ARPT", "Alexandria");
      put("NOVO ALEXEYEVKA ARPT", "Tbilisi");
      put("ISLAMABAD INTL", "Islamabad");
      put("PESHAWAR ARPT", "Peshawar");
      put("JAKARTA-HALIM", "Jakarta");
      put("TAN SON NHUT ARPT", "Ho Chi Minh");
      put("PENANG PORT AIRPORT", "Penang");
      put("SOEKARNO HATTA INTL", "JAKARTA");
      put("ATATURK ARPT", "Istanbul");
      put("SABIHA GOKCEN INTERNATIONAL AIRPORT", "Istanbul");
      put("QUAID E AZAM INTERNATIONAL", "Karachi");
      put("BAIYUN AIRPORT", "Guangzhou");
      put("LOMBOK/ MATARAM", "Lombok");
      put("CHAING RAI ARPT", "Chiang Rai");
    }
  };

  public static String getCityNameFromAirportName(String airportName) {

    String cityName = airportCityNameMap.get(StringUtils.upperCase(airportName));
    if (StringUtils.isBlank(cityName)) {
      return airportName;
    }
    return cityName;
  }

  @SuppressWarnings("serial")
  private static Map<VIA_COUNTRY, String> countryNameMap = new HashMap<VIA_COUNTRY, String>() {
    {
      put(VIA_COUNTRY.SG, "Singapore");
      put(VIA_COUNTRY.HK, "Hong Kong");
      put(VIA_COUNTRY.ID, "Indonesia");
      put(VIA_COUNTRY.UAE, "United Arab Emirates");
      put(VIA_COUNTRY.OM, "Oman, Sultanate Of");
      put(VIA_COUNTRY.SA, "Saudi Arabia");
      put(VIA_COUNTRY.TH, "Thailand");
      put(VIA_COUNTRY.PH, "Philippines"); 
      put(VIA_COUNTRY.IN_CORP, "India");
    }
  };

  public static String getCountryName(VIA_COUNTRY countryCode) {

    return countryNameMap.get(countryCode);
  }

  @SuppressWarnings("serial")
  private static Map<String, Map<Product, String>> recipientMap =
      new HashMap<String, Map<Product, String>>() {
        {
          // uae
          Map<Product, String> productRecipientMap = new HashMap<Product, String>();
          productRecipientMap.put(Product.Flight, DEFAULT_RECIPIENT + ",sweta.kumari@via.com");

          productRecipientMap.put(Product.Hotel, DEFAULT_RECIPIENT + ",sweta.kumari@via.com");

          put("uae", productRecipientMap);

          // singapore

          productRecipientMap = new HashMap<Product, String>();

          productRecipientMap.put(Product.Flight, DEFAULT_RECIPIENT
              + ",ankitkumar.t@via.com,saurav.raj@via.com,rinki.kumari@via.com");
          productRecipientMap.put(Product.Hotel, DEFAULT_RECIPIENT
              + ",ankitkumar.t@via.com,saurav.raj@via.com,rinki.kumari@via.com");
          put("singapore", productRecipientMap);

          put("thiland", productRecipientMap);

        }
      };

  // For Local Use

  private static final String DEFAULT_RECIPIENT = "qatest@via.com,automationreport@via.com";

  // For Production Use
  // private static final String DEFAULT_RECIPIENT =
  // "qatest@via.com,automationreport@via.com,devupdate@via.com";

  public static String getEmailRecipient(String countryName, Product productName) {

    Map<Product, String> productRecipientMap = recipientMap.get(countryName);
    if (productRecipientMap == null) {
      return DEFAULT_RECIPIENT;
    }

    String recipientList = productRecipientMap.get(productName);

    if (recipientList == null) {
      return DEFAULT_RECIPIENT;
    }

    return recipientList;
  }

  @SuppressWarnings("serial")
  private static Map<VIA_COUNTRY, String> slackWebHookupMap = new HashMap<VIA_COUNTRY, String>() {
    {
      put(VIA_COUNTRY.ID,
          "https://hooks.slack.com/services/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");
      put(VIA_COUNTRY.PH,
          "https://hooks.slack.com/services/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");
      put(VIA_COUNTRY.SG,
          "https://hooks.slack.com/services/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");
      put(VIA_COUNTRY.TH,
          "https://hooks.slack.com/services/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");
      put(VIA_COUNTRY.UAE,
          "https://hooks.slack.com/services/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");
    }
  };

  public static String getSlackWebHookupUrl(VIA_COUNTRY countryCode) {
    return slackWebHookupMap.get(countryCode);
  }

}
