package com.via.pageobjects.flights.common;

import java.util.Calendar;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightDetails {

  private String name;
  private String code;
  private Calendar departure;
  private Calendar arrival;
  private Integer duration;
  private Integer layover;

  private String sourceCity;
  private String sourceCityCode;
  private String destinationCity;
  private String destinationCityCode;

  private Integer adultCheckInBaggage;
  private Integer adultCabinBaggage;
  private Integer childCheckInBaggage;
  private Integer childCabinBaggage;
  private Boolean fareRefundable;
}
