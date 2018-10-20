package com.via.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.via.pageobjects.flights.common.TravellerDetails;
import com.via.utils.Constant.Traveller;
import com.via.utils.Constant.VIA_COUNTRY;

public class RandomValues {

  private final static String NAME_LEXICON = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private final static String ALPHANUM_LEXICON = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  private final static Random rand = new Random();

  @SuppressWarnings("serial")
  private static List<String> nameList = new ArrayList<String>() {
    {
      add("RIAN");
      add("FAISAL");
      add("LUSI");
      add("RIZKY");
      add("HALIMAH");
      add("NADYA");
      add("CHERLY");
      add("MEI");
      add("MIRA");
      add("IDHAM");
      add("OKY");
      add("VANI");
      add("VINO");
      add("ZAHRA");
      add("MEGA");
      add("FATICH");
      add("MAHESA");
      add("LISTIKA");
      add("SAPTA");
      add("KARWANTO");
      add("REZA");
      add("RENTI");
      add("AZKIA");
      add("CHRISTY");
      add("TIKA");
      add("APRIL");
      add("SHAFA");
      add("JUHENDRA");
      add("AHMAD");
      add("NICO");
    }
  };
  
  @SuppressWarnings("serial")
  private static List<String> adultTitleList = new ArrayList<String>() {
    {
      add("Mr");
      add("Ms");
      add("Mrs");
    }
  };
  
  @SuppressWarnings("serial")
  private static List<String> titleList = new ArrayList<String>() {
    {
      add("Mstr");
      add("Miss");
      add("Mrs");
    }
  };

  private static Map<String, Boolean> usedNameList;

  public static void setUsedNameList() {
    usedNameList = new HashMap<String, Boolean>();
  }

  public static String getRandomName() {
    StringBuilder builder = new StringBuilder();
    int length = rand.nextInt(5) + 5;
    for (int i = 0; i < length; i++)
      builder.append(NAME_LEXICON.charAt(rand.nextInt(NAME_LEXICON.length())));
    return builder.toString();
  }

  public static String getRandomName(VIA_COUNTRY countryCode) {
    String name = null;

    do {
      if (countryCode == VIA_COUNTRY.ID) {
        name = nameList.get(rand.nextInt(30)) + " " + nameList.get(rand.nextInt(30));
      } else {
        name = getRandomName() + " " + getRandomName();
      }
    } while (usedNameList.get(name) != null);

    usedNameList.put(name, true);

    return name;
  }
  
  public static String getRandomTitle(TravellerDetails travellerType) {
    String title = null;
    if(travellerType.getType() == Traveller.ADULT){
      title = adultTitleList.get(rand.nextInt(3));
    }else{
      title = titleList.get(rand.nextInt(2));
    }
    return title;
  }

  public static int getRandomNumberBetween(int low, int high) {
    if (low == high) {
      return low;
    }
    int result = rand.nextInt(high - low) + low;
    return result;
  }

  public static long getRandomNumberBetween(long low, long high) {
    if (low == high) {
      return low;
    }
    long result = low + (rand.nextLong() % (high - low));
    return result;
  }

  public static String getRandomAlphaNumericString() {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < 8; i++)
      builder.append(ALPHANUM_LEXICON.charAt(rand.nextInt(ALPHANUM_LEXICON.length())));
    return builder.toString();
  }
}
