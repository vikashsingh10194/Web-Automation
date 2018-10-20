package com.via.pageobjects.trains;

import java.util.Calendar;

import com.via.utils.Constant.Journey;
import com.via.utils.Constant.TRAIN_TYPE;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainBookingDetails {
  private String sourceCity;
  private String sourceCode;
  private String destinationCity;
  private String destinationCode;
  private TRAIN_TYPE trainType;
  private int adultCount;
  private int infantCount;
  private String onwardTrain;
  private String returnTrain;
  private Calendar onwardDate;
  private Calendar returnDate;
  private TrainDetails onwardTrainDetails;
  private TrainDetails returnTrainDetails;
  private Boolean splitBooking;
  private Boolean manualSeatSelection;
  private Double totalFare;
  private String isdCode;
  private String contactNo;
  private String contactEmail;

  private Journey journeyType;
}
