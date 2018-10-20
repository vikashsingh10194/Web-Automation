package com.via.utils;

import java.text.DecimalFormat;

import org.apache.commons.lang.StringUtils;

import com.via.utils.Constant.VIA_COUNTRY;

public class NumberUtility {

  // This string contains amount with comma eg -> 3,337.65
  public static double getAmountFromString(VIA_COUNTRY countryCode, String str) {

    str = str.replaceAll("[^0-9.]", "");

    double value = 0;
    str = StringUtils.trimToEmpty(str);

    if (StringUtils.isEmpty(str)) {
      return value;
    }

    if (countryCode == VIA_COUNTRY.ID) {
      str = StringUtils.remove(str, ".");
      return Double.parseDouble(str);
    }

    str = StringUtils.remove(str, ",");
    value = Double.parseDouble(str);

    return value;
  }

  public static double getAmountFromString(String str) {

    double value = 0;
    if (StringUtils.isEmpty(str)) {
      return value;
    }

    str = str.replaceAll("[^0-9.]", "");
    str = StringUtils.trimToEmpty(str);
    if (StringUtils.isEmpty(str)) {
      return value;
    }

    str = StringUtils.remove(str, ",");
    value = Double.parseDouble(str);

    return value;
  }

  public static String getRoundedAmount(double value) {
    DecimalFormat df = new DecimalFormat("#####.##");
    return df.format(value);
  }

  public static String getRoundedAmount(VIA_COUNTRY countryCode, double value) {
    DecimalFormat df;
    if (countryCode == VIA_COUNTRY.ID) {
      df = new DecimalFormat("#####");
    } else {
      df = new DecimalFormat("#####.##");
    }
    return df.format(value);
  }

  public static boolean equals(Double value1, Double value2) {
    if (value1 == null && value2 == null) {
      return true;
    }

    if (value1 == null && value2.equals(0.0)) {
      return true;
    }

    if (value1.equals(0.0) && value2 == null) {
      return true;
    }


    if (value1 != null && value2 != null) {
      return value1.equals(value2);
    }
    return false;
  }
}
