package com.via.pageobjects.erecharge;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RechargeDetails {
  private String operator;
  private String plan;
  private String customerId;
  private Double price;
  private String contactNo;
  private String contactEmail;
}
