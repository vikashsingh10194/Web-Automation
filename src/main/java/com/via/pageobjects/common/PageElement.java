package com.via.pageobjects.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageElement {

  private String name;
  private String type;
  private String locatorType;
  private String locatorValue;

  public PageElement() {

  }

  public PageElement(PageElement pageElement) {
    this.name = pageElement.name;
    this.type = pageElement.type;
    this.locatorType = pageElement.locatorType;
    this.locatorValue = pageElement.locatorValue;
  }
}
