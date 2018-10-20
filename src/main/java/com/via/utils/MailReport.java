package com.via.utils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.io.FileUtils;

public class MailReport {

  public static void mail(String status, String recipients) throws Exception {

    final String username = "viacommkt";
    final String password = "via1send2grid3";
    String from = "ViaTestAutomation";

    // String to = "automationreport@via.com,qatest@via.com";

    Properties props = new Properties();
    props.put("mail.smtp.auth", true);
    props.put("mail.smtp.starttls.enable", true);
    props.put("mail.smtp.host", "smtp.sendgrid.net");
    props.put("mail.smtp.port", "587");

    Session session = Session.getInstance(props, new javax.mail.Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
      }
    });

    try {

      // To be mentioned in the body
      String fileCustom = System.getProperty("user.dir") + "/test-output/custom-report.html";

      Scanner sc = new Scanner(new File(fileCustom));
      String fileContent = sc.useDelimiter("\\Z").next();
      sc.close();

      // File to be attached
      String file = System.getProperty("user.dir") + "/test-output/emailable-report.html";

      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(from));

      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
      message.setSubject("Test Automation Report at " + getDateAsString() + ":" + status);
      message.setText("PFA");

      MimeBodyPart messageBodyPart = new MimeBodyPart();
      messageBodyPart.setContent(fileContent, "text/html");

      Multipart multipart = new MimeMultipart();
      multipart.addBodyPart(messageBodyPart);

      // Added to attach screen shot..
      String dateString = CalendarUtils.getCurrentDate(Constant.DD_MM_YYYY);
      File directory = new File(Constant.SCREENSHOTS_PATH + dateString);
      if (directory.exists()) {
        File[] listFiles = directory.listFiles();
        for (File listFile : listFiles) {
          MimeBodyPart attachPart = new MimeBodyPart();
          attachPart.attachFile(listFile);
          multipart.addBodyPart(attachPart);
        }
      }


      MimeBodyPart attachPart = new MimeBodyPart();
      attachPart.attachFile(file);
      multipart.addBodyPart(attachPart);

      message.setContent(multipart);

      System.out.println("Sending");

      Transport.send(message);

      System.out.println("Done");
      // Added to delete Screenshot files after sending mail.
      try {
        FileUtils.deleteDirectory(directory);
      } catch (IOException e) {
        Log.error("Folder not found. Path: " + directory);
      }


    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }

  private static String getDateAsString() {
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Date date = new Date();
    return dateFormat.format(date);
  }
}
