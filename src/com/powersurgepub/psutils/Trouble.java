/*
 * Copyright 2003 - 2013 Herb Bowie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.powersurgepub.psutils;

  import java.awt.*;
  import javax.swing.*;

/**
   A class for reporting problems.<p>
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
  public void report (String message, String title, int messageType) {
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
