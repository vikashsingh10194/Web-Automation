package com.via.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class StringUtilities {
  public static List<String> getFlightNameAndNo(String flightDetails) {
    List<String> flightDetailsList = new ArrayList<String>(split(flightDetails, "_"));

    if (flightDetailsList.size() == 1) {
    
      flightDetailsList.add("1");
    }
    return flightDetailsList;
  }

  public static List<String> split(String str, String seperator) {
    String metaCharacters = "<([{\\^-=$!|]})?*+.>";
    if (str == null) {
      return new ArrayList<String>();
    }
    if (metaCharacters.contains(seperator)) {
      seperator = "\\" + seperator;
    }
    str = StringUtils.trimToEmpty(str);
    List<String> splittedList = Arrays.asList(str.split(seperator));
    for (int index = 0; index < splittedList.size(); index++) {
      String splittedString = StringUtils.trimToEmpty(splittedList.get(index));
      splittedList.set(index, splittedString);
    }

    return splittedList;
  }
}
