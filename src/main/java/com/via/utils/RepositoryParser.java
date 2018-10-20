package com.via.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.tika.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.via.pageobjects.common.PageElement;
import com.via.pageobjects.flights.common.TransactionRuleElement;
import com.via.utils.Constant.BOOKING_MEDIA;
import com.via.utils.Constant.Flight;
import com.via.utils.Constant.Traveller;
import com.via.utils.Constant.VIA_COUNTRY;

public class RepositoryParser {

  // This map maps the page name with the map that maps object name with its
  // element object
  private Map<String, Map<String, PageElement>> pageElementsMap;
  private Map<String, String> configPropertiesMap;
  private List<TransactionRuleElement> ruleElementList;

  private final String CAL_FORMAT = "dd-MMM-yyyy";
  
  // Initializes the page elements map from the JSON file
  public RepositoryParser() {
    pageElementsMap = new HashMap<String, Map<String, PageElement>>();
  }

  // Reads complete file into a string
  public String readFile(String filename) {

    String result = "";
    FileReader fileReader = null;
    BufferedReader bufferedReader = null;
    try {
      fileReader = new FileReader(filename);
      bufferedReader = new BufferedReader(fileReader);

      StringBuilder stringBuilder = new StringBuilder();
      String line = bufferedReader.readLine();
      while (line != null) {
        stringBuilder.append(line);
        line = bufferedReader.readLine();
      }
      result = stringBuilder.toString();
    } catch (Exception e) {
      Log.error("Error in reading Locator Repository file " + e.getMessage());
    } finally {
      try {
        if (bufferedReader != null) {
          bufferedReader.close();
        }
        if (fileReader != null) {
          fileReader.close();
        }
      } catch (IOException e) {
        Log.error("Error in closing Locator Repository file " + e.getMessage());
      } finally {
        CustomAssert
            .assertTrue(StringUtils.isNotEmpty(result), "Error in reading file " + filename);
      }
    }

    return result;
  }

  // Takes input the array of the pages and puts all the elements in the map
  private void putObjectFromPages(JSONArray pageArray) {

    for (int i = 0; i < pageArray.length(); i++) {
      JSONObject page = pageArray.optJSONObject(i);
      String pName = page.optString("name");
      JSONArray pageElementArray = page.optJSONArray("uiObjects");

      Map<String, PageElement> pageElements = new HashMap<String, PageElement>();
      pageElementsMap.put(pName, pageElements);
      // Searching for the target element in the current page
      for (int j = 0; j < pageElementArray.length(); j++) {
        JSONObject pageElement = pageElementArray.optJSONObject(j);
        String name = pageElement.optString("name");

        PageElement element = new PageElement();
        element.setName(name);
        element.setType(pageElement.optString("type"));
        element.setLocatorType(pageElement.optString("locatorType"));
        element.setLocatorValue(pageElement.optString("locatorValue"));
        pageElements.put(name, element);
      }

    }
  }

  // Retrieves the target page element from the map using its pageName and the
  // objectName
  public PageElement getPageObject(String pageName, String objectName) {

    Map<String, PageElement> pageElements = pageElementsMap.get(pageName);
    CustomAssert.assertTrue(pageElements != null, "Page not found: " + pageName);

    PageElement pageElement = pageElements.get(objectName);
    CustomAssert.assertTrue(pageElement != null, "Page element " + objectName
        + " not found on page " + pageName);

    return new PageElement(pageElement);
  }

  // Takes the JSON fileName as input and fetches all the pages from the file
  // and loads all the elements in the map.
  public void pageElementsLoader(String fileName) {
    InputStream inpStream = RepositoryParser.class.getClassLoader().getResourceAsStream(fileName);
    JSONObject locatorRepository = null;
    try {
      locatorRepository = new JSONObject(IOUtils.toString(inpStream, "UTF-8"));
    } catch (JSONException e) {
      Log.error("Error in parsing JSOn file " + e.getMessage());
    } catch (IOException e) {
      Log.error("IO Exception encounred " + e.getMessage());
    } finally {
      CustomAssert.assertTrue(locatorRepository != null, "Error in reading locator repository "
          + fileName);
    }

    JSONArray pageArray = locatorRepository.optJSONArray("pages");
    CustomAssert.assertTrue(pageArray != null, "Error in parsing locator repository pages "
        + fileName);

    putObjectFromPages(pageArray);
  }

  public void transactionRuleLoader(VIA_COUNTRY countryCode, String fileName) {
    ruleElementList = new ArrayList<TransactionRuleElement>();

    JSONObject transactionDetails = null;
    try {
      InputStream inpStream = new FileInputStream(fileName);
      transactionDetails = new JSONObject(IOUtils.toString(inpStream, "UTF-8"));
    } catch (JSONException e) {
      Log.error("Error in parsing JSON file " + e.getMessage());
    } catch (FileNotFoundException e) {
      Log.error("File Not found: " + fileName);
    } catch (IOException e) {
      Log.error("I/O Exception occured.");
    }

    JSONArray pageArray = transactionDetails.optJSONArray("transactionRuleDetails");

    for (int index = 0; index < pageArray.length(); index++) {
      JSONObject jObject = pageArray.optJSONObject(index);
      TransactionRuleElement element = new TransactionRuleElement();

      element.setAirlineName(jObject.optString("airline", null));
      element.setFlightNo(jObject.optString("flightNo", null));
      element.setSector(jObject.optString("sector", null));

      String value = jObject.optString("date", null);
      if (StringUtils.isNotBlank(value)) {
        try {
          element.setDate(CalendarUtils.dateStringToCalendarDate(value, CAL_FORMAT));
        } catch (java.text.ParseException e) {
          CustomAssert.assertFail("Date can't be parsed from transaction rule json file.");
        }
      }

      value = jObject.optString("media");

      if (StringUtils.isNotBlank(value)) {
        if (StringUtils.equalsIgnoreCase(value, "B2C")) {
          element.setMedia(BOOKING_MEDIA.B2C);
        } else {
          element.setMedia(BOOKING_MEDIA.B2B);
        }
      }

      value = StringUtils.trimToEmpty(jObject.optString("flightType"));

      if (StringUtils.isNotBlank(value)) {
        if (StringUtils.equalsIgnoreCase(value, "International")) {
          element.setFlightType(Flight.INTERNATIONAL);
        } else {
          element.setFlightType(Flight.DOMESTIC);
        }
      }

      element.setPriority(NumberUtils.toInt(jObject.optString("priority")));
      element.setTransactionFee(jObject.optString("transactionFee", "0"));

      element.setDiscount(jObject.optString("discount", "0"));
      value = jObject.optString("traveller", null);

      if (StringUtils.isNotBlank(value)) {
        if (StringUtils.equalsIgnoreCase(value, "adult")) {
          element.setTravellerType(Traveller.ADULT);
        } else if (StringUtils.equalsIgnoreCase(value, "child")) {
          element.setTravellerType(Traveller.CHILD);
        } else {
          element.setTravellerType(Traveller.INFANT);
        }
      }

      value = jObject.optString("fareRefundable", null);

      if (StringUtils.equalsIgnoreCase(value, "true")) {
        element.setFareRefundable(true);
      } else if (StringUtils.equalsIgnoreCase(value, "false")) {
        element.setFareRefundable(false);
      }

      ruleElementList.add(element);
    }

    Collections.sort(ruleElementList);
  }

  // Loads the properties file into the map
  @SuppressWarnings({"unchecked", "rawtypes"})
  public void loadConfigProperties() {
    Properties properties = new Properties();
    InputStream inputStream = null;
    try {
      String configFile = Constant.CONFIGURATION_PROPERTIES;
      inputStream = RepositoryParser.class.getClassLoader().getResourceAsStream(configFile);
      // Loading the property file
      properties.load(inputStream);
    } catch (FileNotFoundException e) {
      Log.error("Property file was not found");
    } catch (IOException e) {
      Log.error("The key was not found in the property file");
    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          Log.error("The property file was not closed");
        }
      }
    }

    // Getting the corresponding value of the key in the property file
    configPropertiesMap = new HashMap<String, String>((Map) properties);
  }

  public String getPropertyValue(String key) {
    String value = configPropertiesMap.get(key);
    CustomAssert.assertTrue(value != null, "Properties not found corresponding to " + key);
    return StringUtils.trimToEmpty(value);
  }

  public TransactionRuleElement getTransactionRule(TransactionRuleElement transactionElement) {
    int index = ruleElementList.indexOf(transactionElement);
    return ruleElementList.get(index);
  }
}
