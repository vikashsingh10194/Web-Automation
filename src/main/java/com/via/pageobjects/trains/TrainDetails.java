package com.via.pageobjects.trains;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainDetails {
  private String trainName;
  private String coachType;
  private String departTime;
  private String arrivalTime;
  private String departStation;
  private String arrivalStation;
}
