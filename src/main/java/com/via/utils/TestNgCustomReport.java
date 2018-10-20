package com.via.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.testng.IInvokedMethod;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.collections.Lists;
import org.testng.internal.Utils;
import org.testng.xml.XmlSuite;

import com.via.utils.Constant.Product;

public class TestNgCustomReport implements IReporter {

  private PrintWriter writer;
  private int m_row;
  private int m_methodIndex = 0;

  private Integer m_testIndex;
  private String reportFileName = "custom-report.html";

  /** Creates summary of the run */
  // custom report generation of selenium tests report generation
  // displays the count of test cases **passed **failed
  // list of all the test cases which are failed are displayed

  @Override
  public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outdir) {
    try {
      writer = createWriter(outdir);
    } catch (IOException e) {
      System.err.println("Unable to create output file");
      e.printStackTrace();
      return;
    }

    String recipientName = getRecipientName(suites);
    startHtml(writer);
    // Suite Name in the Report
    writeReportTitle(suites.get(0).getName());
    // final summary report
    generateSuiteSummaryReport(suites);
    // Failed Test cases displayed
    generateMethodDetailReport(suites);
    endHtml(writer);
    writer.flush();
    writer.close();

    try {
      if (m_methodIndex == 0) {
        MailReport.mail("Successful Execution", recipientName);
      } else {
        MailReport.mail("Failed Execution", recipientName);
      }

    } catch (Exception e) {
      Log.error("Unable to Send Mail" + e.getMessage());
    }
  }

  private String getRecipientName(List<ISuite> suites) {

    String recipientName = "";

    for (ISuite suite : suites) {
      List<ITestNGMethod> testNgMethodList = suite.getAllMethods();
      for (ITestNGMethod testNgMethod : testNgMethodList) {
        List<String> testFilePath =
            StringUtilities.split(testNgMethod.getTestClass().getName(), ".");
        String countryName = testFilePath.get(3);
        Product productName = getProductName(testFilePath.get(4));

        if (StringUtils.isNotEmpty(recipientName)) {
          recipientName += ",";
        }
        recipientName += EntityMap.getEmailRecipient(countryName, productName);
      }
    }

    return recipientName;
  }

  private Product getProductName(String productTestName) {

    for (Product product : Product.values()) {
      if (StringUtils.containsIgnoreCase(productTestName, product.toString())) {
        return product;
      }
    }

    return null;
  }

  protected void generateMethodDetailReport(List<ISuite> suites) {

    for (ISuite suite : suites) {
      Map<String, ISuiteResult> r = suite.getResults();
      for (ISuiteResult r2 : r.values()) {
        ITestContext testContext = r2.getTestContext();
        IResultMap failedTests = testContext.getFailedTests();
        if (failedTests.getAllResults().size() == 0) {
          continue;
        }

        if (r.values().size() > 0) {
          writer.println("<h1>" + testContext.getName() + "</h1>");
        }
        tableStart("result", null);
        writer.print("<tr class=\"param\">");
        writer.print("<th style=\"border:1px solid #009;padding:.25em .5em\">TestCasesFailed" + ""
            + "</th>");
        resultDetail(failedTests);
        tableEnd();
      }
    }
  }

  private void resultDetail(IResultMap tests) {
    Set<ITestResult> testResults = tests.getAllResults();
    List<ITestResult> testResultsList = new ArrayList<ITestResult>(testResults);
    System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
    System.setProperty("java.util.Collections.useLegacyMergeSort", "true");
    Collections.sort(testResultsList, new TestResultsSorter());

    for (ITestResult result : testResultsList) {
      ITestNGMethod method = result.getMethod();
      m_methodIndex++;
      Set<ITestResult> resultSet = tests.getResults(method);
      generateResult(result, method, resultSet.size());
    }

  }

  private void generateResult(ITestResult ans, ITestNGMethod method, int resultSetSize) {
    Object[] parameters = ans.getParameters();
    boolean hasParameters = parameters != null && parameters.length > 0;
    if (hasParameters) {
      // writer.print("<th>TestCasesInput" + 2 + "</th>");
      writer.println("</tr>");
      writer.print("<tr class=\"param stripe\">");
      // for (Object p : parameters) {
      writer.println("<td  style=\"border:1px solid #009;padding:.25em .5em\">"
          + Utils.escapeHtml(Utils.toString(parameters[0])) + "</td>");
      // }
      writer.println("</tr>");
    }

  }

  private class TestResultsSorter implements Comparator<ITestResult> {
    @Override
    public int compare(ITestResult obj1, ITestResult obj2) {
      int result = obj1.getTestClass().getName().compareTo(obj2.getTestClass().getName());
      if (result == 0) {
        result = obj1.getMethod().getMethodName().compareTo(obj2.getMethod().getMethodName());
      }
      return result;
    }
  }

  protected PrintWriter createWriter(String outdir) throws IOException {
    new File(outdir).mkdirs();
    return new PrintWriter(new BufferedWriter(new FileWriter(new File(outdir, reportFileName))));
  }

  private String timeConversion(long seconds) {

    final int MINUTES_IN_AN_HOUR = 60;
    final int SECONDS_IN_A_MINUTE = 60;

    int minutes = (int) (seconds / SECONDS_IN_A_MINUTE);
    seconds -= minutes * SECONDS_IN_A_MINUTE;

    int hours = minutes / MINUTES_IN_AN_HOUR;
    minutes -= hours * MINUTES_IN_AN_HOUR;

    return prefixZeroToDigit(hours) + ":" + prefixZeroToDigit(minutes) + ":"
        + prefixZeroToDigit((int) seconds);
  }

  private String prefixZeroToDigit(int num) {
    int number = num;
    if (number <= 9) {
      String sNumber = "0" + number;
      return sNumber;
    } else
      return "" + number;

  }

  protected void generateExceptionReport(Throwable exception, ITestNGMethod method) {
    writer.print("<div class=\"stacktrace\">");
    writer.print(Utils.stackTrace(exception, true)[0]);
    writer.println("</div>");
  }

  /**
   * Since the methods will be sorted chronologically, we want to return the ITestNGMethod from the
   * invoked methods.
   */
  @SuppressWarnings("unused")
  private Collection<ITestNGMethod> getMethodSet(IResultMap tests, ISuite suite) {

    List<IInvokedMethod> r = Lists.newArrayList();
    List<IInvokedMethod> invokedMethods = suite.getAllInvokedMethods();
    for (IInvokedMethod im : invokedMethods) {
      if (tests.getAllMethods().contains(im.getTestMethod())) {
        r.add(im);
      }
    }

    System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
    System.setProperty("java.util.Collections.useLegacyMergeSort", "true");
    Collections.sort(r, new TestSorter());
    List<ITestNGMethod> result = Lists.newArrayList();

    // Add all the invoked methods
    for (IInvokedMethod m : r) {
      for (ITestNGMethod temp : result) {
        if (!temp.equals(m.getTestMethod()))
          result.add(m.getTestMethod());
      }
    }

    // Add all the methods that weren't invoked (e.g. skipped) that we
    // haven't added yet
    Collection<ITestNGMethod> allMethodsCollection = tests.getAllMethods();
    List<ITestNGMethod> allMethods = new ArrayList<ITestNGMethod>(allMethodsCollection);
    Collections.sort(allMethods, new TestMethodSorter());

    for (ITestNGMethod m : allMethods) {
      if (!result.contains(m)) {
        result.add(m);
      }
    }
    return result;
  }

  @SuppressWarnings("unused")
  public void generateSuiteSummaryReport(List<ISuite> suites) {
    tableStart("testOverview", null);
    writer.print("<tr>");
    tableColumnStart("Test");
    tableColumnStart("TestCases<br/>Passed");
    tableColumnStart("TestCases<br/>Skipped");
    tableColumnStart("TestCases<br/>Failed");
    tableColumnStart("Start<br/>Time");
    tableColumnStart("End<br/>Time");
    tableColumnStart("Total<br/>Time(hh:mm:ss)");
    tableColumnStart("Included<br/>Groups");
    tableColumnStart("Excluded<br/>Groups");

    writer.println("</tr>");
    NumberFormat formatter = new DecimalFormat("#,##0.0");
    int qty_tests = 0;
    int qty_pass_m = 0;
    int qty_pass_s = 0;
    int qty_skip = 0;
    long time_start = Long.MAX_VALUE;
    int qty_fail = 0;
    long time_end = Long.MIN_VALUE;
    m_testIndex = 1;
    for (ISuite suite : suites) {
      if (suites.size() >= 1) {
        titleRow(suite.getName(), 10);
      }
      Map<String, ISuiteResult> tests = suite.getResults();
      for (ISuiteResult r : tests.values()) {
        qty_tests += 1;
        ITestContext overview = r.getTestContext();

        startSummaryRow(overview.getName());
        int q = overview.getPassedTests().getAllResults().size();
        qty_pass_m += q;
        summaryCell(q, Integer.MAX_VALUE);
        q = overview.getSkippedTests().getAllResults().size();
        qty_skip += q;
        summaryCell(q, 0);
        q = overview.getFailedTests().getAllResults().size();
        qty_fail += q;
        summaryCell(q, 0);

        // Write OS and Browser
        // summaryCell(suite.getParameter("browserType"), true);
        // writer.println("</td>");

        SimpleDateFormat summaryFormat = new SimpleDateFormat("hh:mm:ss");
        summaryCell(summaryFormat.format(overview.getStartDate()), true);
        writer.println("</td>");

        summaryCell(summaryFormat.format(overview.getEndDate()), true);
        writer.println("</td>");

        time_start = Math.min(overview.getStartDate().getTime(), time_start);
        time_end = Math.max(overview.getEndDate().getTime(), time_end);
        summaryCell(timeConversion((overview.getEndDate().getTime() - overview.getStartDate()
            .getTime()) / 1000), true);

        summaryCell(overview.getIncludedGroups());
        summaryCell(overview.getExcludedGroups());
        writer.println("</tr>");
        m_testIndex++;
      }
    }
    if (qty_tests > 1) {
      writer
          .println("<tr class=\"total\"><td style=\" border:1px solid #009;padding:.25em .5em\" >Total</td>");
      summaryCell(qty_pass_m, Integer.MAX_VALUE);
      summaryCell(qty_skip, 0);
      summaryCell(qty_fail, 0);
      summaryCell(" ", true);
      summaryCell(" ", true);
      summaryCell(timeConversion(((time_end - time_start) / 1000)), true);
      summaryCell(" ", true);
      summaryCell(" ", true);
      writer.println("<td colspan=\"3\">&nbsp;</td></tr>");
    }
    writer.println("</table>");
  }

  private void summaryCell(String[] val) {
    StringBuffer b = new StringBuffer();
    for (String v : val) {
      b.append(v + " ");
    }
    summaryCell(b.toString(), true);
  }

  private void summaryCell(String v, boolean isgood) {
    writer
        .print("<td style=\" border:1px solid #009;padding:.25em .5em\" class=\"numi\" + (isgood ? \"\" : \"_attn\") + \"\">"
            + v + "</td>");
  }

  private void startSummaryRow(String label) {
    m_row += 1;
    writer
        .print("<tr"
            + (m_row % 2 == 0 ? " class=\"stripe\"" : "")
            + "><td style=\"text-align:left;padding-right:2em;border:1px solid #009;padding:.25em .5em\"><a href=\"#t\"+ m_testIndex + \"\" ><b>"
            + label + "</b></a>" + "</td>");

  }

  private void summaryCell(int v, int maxexpected) {
    summaryCell(String.valueOf(v), v <= maxexpected);
  }

  private void tableStart(String cssclass, String id) {
    writer
        .println("<table style=  \"margin-bottom:10px;border-collapse:collapse;empty-cells:show \"  cellspacing=\"0\" cellpadding=\"0\""
            + (cssclass != null ? " class=\"\" + cssclass + \"\"" : " style=\"padding-bottom:2em\"")
            + (id != null ? " id=\"\" + id + \"\"" : "") + ">");
    m_row = 0;
  }

  private void tableEnd() {
    writer.println("</table>");
  }

  private void tableColumnStart(String label) {
    writer.print("<th style=\" border:1px solid #009;padding:.25em .5em\">" + label + "</th>");
  }

  private void titleRow(String label, int cq) {
    titleRow(label, cq, null);
  }

  private void titleRow(String label, int cq, String id) {
    writer.print("<tr");
    if (id != null) {
      writer.print(" id=\"\" + id + \"\"");
    }
    writer.println("><th style=\" border:1px solid #009;padding:.25em .5em\"\" colspan=\"" + cq
        + "\">" + label + "</th></tr>");
    m_row = 0;
  }

  protected void writeReportTitle(String title) {
    String envTestServer = System.getenv("TESTSERVER");
    String userName = System.getProperty("user.name");
    if (envTestServer != null && envTestServer.equals("jenkinserver")) {
      userName += " : " + System.getenv("BUILD_USER");
    }
    writer.print("<center><h1>" + title + " - " + getDateAsString() + " executed by " + userName
        + "</h1></center>");

  }

  /*
   * Method to get Date as String
   */
  private String getDateAsString() {
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Date date = new Date();
    return dateFormat.format(date);
  }

  /** Starts HTML stream */
  protected void startHtml(PrintWriter out) {
    out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
    out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
    out.println("<head>");
    out.println("<title>TestNG Report</title>");
    out.println("<style type=\"text/css\">");
    // out.println("table
    // {margin-bottom:10px;border-collapse:collapse;empty-cells:show}");
    // out.println("td,th {border:1px solid #009;padding:.25em .5em}");
    // out.println(".result th {vertical-align:bottom}");
    // out.println(".param th {padding-left:1em;padding-right:1em}");
    // out.println(".param td {padding-left:.5em;padding-right:2em}");
    // out.println(".stripe td,.stripe th {background-color: #E6EBF9}");
    // out.println(".numi,.numi_attn {text-align:right}");
    // out.println(".total td {font-weight:bold}");
    // out.println(".passedodd td {background-color: #0A0}");
    // out.println(".passedeven td {background-color: #3F3}");
    // out.println(".skippedodd td {background-color: #CCC}");
    // out.println(".skippedodd td {background-color: #DDD}");
    // out.println(".failedodd td,.numi_attn {background-color: #F33}");
    // out.println(".failedeven td,.stripe .numi_attn {background-color:
    // #D00}");
    // out.println(".stacktrace {white-space:pre;font-family:monospace}");
    // out.println(".totop
    // {font-size:85%;text-align:center;border-bottom:2px solid #000}");
    out.println("</style>");
    out.println("</head>");
    out.println("<body>");

  }

  /** Finishes HTML stream */
  protected void endHtml(PrintWriter out) {
    out.println("<center> TestNG Report </center>");
    out.println("</body></html>");
  }

  // ~ Inner Classes --------------------------------------------------------
  /** Arranges methods by classname and method name */
  private class TestSorter implements Comparator<IInvokedMethod> {
    // ~ Methods
    // -------------------------------------------------------------

    /** Arranges methods by classname and method name */
    @Override
    public int compare(IInvokedMethod obj1, IInvokedMethod obj2) {
      int r =
          obj1.getTestMethod().getTestClass().getName()
              .compareTo(obj2.getTestMethod().getTestClass().getName());
      return r;
    }
  }

  private class TestMethodSorter implements Comparator<ITestNGMethod> {
    @Override
    public int compare(ITestNGMethod obj1, ITestNGMethod obj2) {
      int r = obj1.getTestClass().getName().compareTo(obj2.getTestClass().getName());
      if (r == 0) {
        r = obj1.getMethodName().compareTo(obj2.getMethodName());
      }
      return r;
    }
  }

}
