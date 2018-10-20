package com.via.pageobjects.flights.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SSR {
  String meal;
  String baggage;
  String seatNo;

  Double mealPrice;
  Double baggagePrice;
  Double seatPrice;

  public SSR() {
    mealPrice = 0.0;
    baggagePrice = 0.0;
    seatPrice = 0.0;
  }
}
