package com.via.pageobjects.trains;

import java.util.Calendar;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TravellerDetails {
  private String name;
  private String type;
  private String contactNo;
  private String idType;
  private String idNo;
  private Calendar dob;
  private Map<String, String> seatDetails;

}
