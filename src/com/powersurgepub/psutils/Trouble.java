package com.powersurgepub.psutils;

  import java.awt.*;
  import javax.swing.*;

/**
   A class for reporting problems.<p>
     
   This code is copyright (c) 2003 by Herb Bowie of PowerSurge Publishing. 
   All rights reserved. <p>
   
   Version History: <ul><li>
    </ul>
   @author Herb Bowie 
             (<a href="mailto:herb@powersurgepub.com">herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
             (<A href="http://www.powersurgepub.com/">www.powersurgepub.com/</a>)
  
   @version 2003/10/12 - Created this class for use with Two Due.
 */
public class Trouble {
  
  /** A single instance of Trouble that can be shared by many classes. */
  private static Trouble trouble;
  
  /** The default parent component to use for messaging in a GUI environment. */
  private Component parent;
  
  /** Should we use GUI messages or write messages to a batch log? */
  private boolean   gui = true;
  
  /** 
    Returns a single instance of Trouble that can be shared by many classes.
   
    @return A single, shared instance of a Trouble object.
   */  
  public static Trouble getShared() {
    if (trouble == null) {
      trouble = new Trouble();
    }
    return trouble;
  }
  
  /** 
    Creates a new instance of Trouble 
   */
  public Trouble() {
  }
  
  /** 
    Sets the default parent component to be passed to JOptionPane static methods.
   
    @param parent The default parent component to use in a GUI environment.
   */  
  public void setParent (Component parent) {
    this.parent = parent;
  }
  
  /** 
    Reports an error condition to the user. The JOptionPane message type defaults to
    ERROR_MESSAGE.
   
    @param parent The parent Component to use.
    @param title The title of the error report.
    @param message The error message to be reported.
   */  
  public void report (Component parent, String message, String title) {
    report (parent, message, title, JOptionPane.ERROR_MESSAGE);
  }
  
  /** 
    Reports an error condition to the user. The JOptionPane message type defaults to
    ERROR_MESSAGE. The default parent component is used, if previously set. 
   
    @param title The title of the error report.
    @param message The error message to be reported.
   */  
  public void report (String message, String title) {
    report (parent, message, title, JOptionPane.ERROR_MESSAGE);
  }
  
  /** 
    Reports an error condition to the user.  
   
    @param title The title of the error report.
    @param message The error message to be reported.
    @param messageType Using the JOptionPane standard values:
      <ul>
        <li>ERROR_MESSAGE
        <li>INFORMATION_MESSAGE
        <li>WARNING_MESSAGE
        <li>QUESTION_MESSAGE
        <li>PLAIN_MESSAGE
      </ul>
   */ 
  public void report (String message, String title,  int messageType) {
    report (parent, message, title, messageType);
  }
  
  /** 
    Reports an error condition to the user.  
   
    @param title The title of the error report.
    @param message The error message to be reported.
    @param messageType Using the JOptionPane standard values:
     <ul>
        <li>ERROR_MESSAGE
        <li>INFORMATION_MESSAGE
        <li>WARNING_MESSAGE
        <li>QUESTION_MESSAGE
        <li>PLAIN_MESSAGE
      </ul>
    @param parent The parent Component to use.
   */ 
  public void report (Component parentComponent, String message, String title, 
       int messageType) {
    if (parentComponent == null) {
      System.out.println (title + ": " + message);
    } else {
      JOptionPane.showMessageDialog (parentComponent,
          message,
          title,
          messageType,
          Home.getShared().getIcon());
    }
  }
  
}
