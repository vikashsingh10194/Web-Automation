package com.via.pageobjects.flights.common;

import java.util.Calendar;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.via.utils.Constant.Journey;

@Getter
@Setter
public class FlightBookingDetails {
  private String testCaseId;
  private String promoCode ; 
  private Journey journeyType = Journey.ONE_WAY;

  private String sourceCity;
  private String sourceCityDetailed;
  private String sourceCityCode;

  private String destinationCity;
  private String destinationCityDetailed;
  private String destinationCityCode;

  private Calendar onwardDate;
  private Calendar returnDate;

  private int adultsCount = 1;
  private int childrenCount = 0;
  private int infantsCount = 0;

  private int stopsCount = 0;
  private String flightClass = "Economy";
  private String onwardFlightName;
  private String returnFlightName;

  private String onwardSSR;
  private String returnSSR;

  private FareDetails onwardFareDetails;
  private FareDetails returnFareDetails;

  private double totalFare = 0;
  
  
  //Added to verify no. of stops between source and destination.
  private List<FlightDetails> onwardStops;
  private List<FlightDetails> returnStops;

  private List<FlightDetails> onwardFlights;
  private List<FlightDetails> returnFlights;

  private String eMail;
  private String mobileNumber;
  private boolean insurance;

  private String bookingId;
}
