package com.via.pageobjects.flights.common;

import java.util.Calendar;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;

import com.via.utils.Constant.BOOKING_MEDIA;
import com.via.utils.Constant.Flight;
import com.via.utils.Constant.Traveller;

@Getter
@Setter
public class TransactionRuleElement implements Comparable<TransactionRuleElement> {
  private String airlineName;
  private String flightNo;
  private Calendar date;
  private String sector;
  private BOOKING_MEDIA media;
  private Flight flightType;
  private Boolean fareRefundable;
  private Traveller travellerType;
  private Integer priority;
  private String transactionFee;
  private String discount;

  public TransactionRuleElement() {}

  public TransactionRuleElement(FlightDetails flightDetails, BOOKING_MEDIA media, Flight flightType) {
    this.airlineName = flightDetails.getName();
    this.flightNo = flightDetails.getCode();
    this.date = flightDetails.getDeparture();
    this.sector = flightDetails.getSourceCityCode() + "-" + flightDetails.getDestinationCityCode();
    this.fareRefundable = flightDetails.getFareRefundable();
    this.media = media;
    this.flightType = flightType;
  }

  @Override
  public int compareTo(TransactionRuleElement o) {
    return this.priority > o.priority ? -1 : 1;
  }

  @Override
  public boolean equals(Object o) {

    if (o == null) {
      return false;
    }

    if (!(o instanceof TransactionRuleElement)) {
      return false;
    }

    TransactionRuleElement ruleObject = (TransactionRuleElement) o;

    if (StringUtils.isNotBlank(ruleObject.airlineName)
        && !StringUtils.equalsIgnoreCase(ruleObject.airlineName, airlineName)) {
      return false;
    }

    if (StringUtils.isNotBlank(ruleObject.flightNo)
        && !StringUtils.equalsIgnoreCase(flightNo, ruleObject.flightNo)) {
      return false;
    }

    if (StringUtils.isNotBlank(ruleObject.sector)
        && !StringUtils.equalsIgnoreCase(sector, ruleObject.sector)) {
      return false;
    }

    if (ruleObject.date != null && DateUtils.isSameDay(date, ruleObject.date)) {
      return false;
    }

    if (ruleObject.fareRefundable != null && !ruleObject.fareRefundable.equals(fareRefundable)) {
      return false;
    }

    if (ruleObject.travellerType != null && !ruleObject.travellerType.equals(travellerType)) {
      return false;
    }

    if (ruleObject.flightType != null && !ruleObject.flightType.equals(flightType)) {
      return false;
    }

    if (ruleObject.media != null && !ruleObject.media.equals(media)) {
      return false;
    }

    return true;
  }
}
