package com.via.pageobjects.holidays;

import java.util.Calendar;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HolidayBookingDetails {

  private String testCaseId;

  private String bookingType;
  private String destination;
  private String destinationType;
  private Calendar checkInDate;

  // This list stores the guest details of each room
  private List<RoomDetails> roomDetailsList;

  private int roomCount = 1;
  private int adultsCount = 0;
  private int childrenCount = 0;
  private boolean bedRequirement;
  private int nightsCount = 0;
  private String name;
  private String hotelName;
  private String address;
  private double baseAmount;
  private double grandTotal;
  private double netPrice;

  private String contactMobile;
  private String contactEmail;

  public void increaseChildrenCount(int incChild) {
    this.childrenCount += incChild;
  }

  public void increaseAdultsCount(int incAdult) {
    this.adultsCount += incAdult;
  }

}
