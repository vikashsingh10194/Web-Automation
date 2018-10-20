package com.via.pageobjects.flights.common;

import java.util.Calendar;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import com.via.utils.Constant.Traveller;

@Getter
@Setter
public class TravellerDetails {

  private Traveller type;
  private String title;
  private String firstName;
  private String surName;
  private Calendar birthCalender;
  private Calendar passportExpDate;
  private String passportNo;
  private String country;
  private Map<String, SSR> ssrDetails;

  public String getName() {
    return this.title + " " + this.firstName + " " + this.surName;
  }
}
