package com.via.pageobjects.hotels;

import java.util.Calendar;
import java.util.List;

import com.via.utils.Constant.HOTEL_FLOW;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HotelDetails {

  private HOTEL_FLOW flowType;

  private String destinationCity;
  private String destinationCountry;

  private Calendar checkInDate;
  private Calendar checkOutDate;
  private int nights;

  private int roomsCount = 0;
  private int adultsCount = 0;
  private int childrenCount = 0;

  // This list stores the guest details of each room
  private List<RoomDetails> roomDetailsList;

  private String name;
  private String address;
  private String roomType;
  private String roomFacilities;

  private double pricePerNightPerRoom;
  private double baseAmount;
  private double taxes;
  private double extraAmount;
  private double grandTotal;
  private double netPrice;

  private int bookingFlowId;

  private String contactMobile;
  private String contactEmail;

  public void increaseAdultsCount(int adultsCount) {
    this.adultsCount = getAdultsCount() + adultsCount;
  }

  public void increaseChildrenCount(int childrenCount) {
    this.childrenCount = getChildrenCount() + childrenCount;
  }
}
