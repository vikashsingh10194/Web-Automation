package com.via.pageobjects.holidays;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomDetails {

  private String roomType;

  private String guestTitle;
  private String guestFirstName;
  private String guestSurName;

  private int adultsCount = 1;
  private int childrenCount = 0;
  private int sharing = 1;
  private int nights = 1;

  private double pricePerNight;
  private double extraPrice;
  private double totalPrice;

  private List<Integer> childAge;

}
