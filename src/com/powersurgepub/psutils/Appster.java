/*
 * Copyright 1999 - 2013 Herb Bowie
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

  import com.powersurgepub.xos2.*;
  import java.io.*;
  import java.net.*;
  import javax.swing.*;

/**
  A coordinating class that sets up the XOS, Home, UserPrefs, ProgramVersion,
  Trouble and Localizer classes for use by a Swing application.
 */
public class Appster {
  
  public static final String LEFT           = "left";
  public static final String TOP            = "top";
  public static final String WIDTH          = "width";
  public static final String HEIGHT         = "height";

  public static final int    DEFAULT_LEFT   = 40;
  public static final int    DEFAULT_TOP    = 40;
  public static final int    DEFAULT_WIDTH  = 620;
  public static final int    DEFAULT_HEIGHT = 540;
  
  private             XOS                 xos        = XOS.getShared();
  
  private             Home                home;
  
  private             UserPrefs           userPrefs;
  
  private             ProgramVersion      programVersion;
  
  private             Trouble             trouble    = Trouble.getShared();
  
  private             Localizer           localizer;
  
  private             JFrame              mainFrame;
  
  /** Creates a new instance of Appster */
  public Appster(
      String domainLevel1, 
      String domainLevel2, 
      String programName,
      String currentVersion,
      JFrame mainFrame,
      XHandler xhandler) {
    
    init (
      domainLevel1,
      domainLevel2,
      programName,
      currentVersion,
      "",
      "",
      mainFrame,
      xhandler);
  }
  
  /** Creates a new instance of Appster */
  public Appster(
      String domainLevel1, 
      String domainLevel2, 
      String programName,
      String currentVersion,
      String language,
      String country,
      JFrame mainFrame,
      XHandler xhandler) {
    init (
      domainLevel1,
      domainLevel2, 
      programName,
      currentVersion,
      language,
      country,
      mainFrame,
      xhandler);
  }
  
  private void init (String domainLevel1, 
      String domainLevel2, 
      String programName,
      String currentVersion,
      String language,
      String country,
      JFrame mainFrame,
      XHandler xhandler) {

    this.mainFrame = mainFrame;
    
    xos.setDomainLevel1 (domainLevel1);
    xos.setDomainLevel2 (domainLevel2);
    xos.setProgramName (programName);
    if (xhandler.preferencesAvailable()) {
      xos.enablePreferences();
    }
    xos.initialize();
    if (mainFrame != null) {
      mainFrame.setTitle (programName);
    }
    home = Home.getShared (programName, currentVersion);
    userPrefs = UserPrefs.getShared();
    if (mainFrame != null) {
      programVersion = ProgramVersion.getShared (mainFrame);
    } else {
      programVersion = ProgramVersion.getShared();
    }
    String resourceBundleName =
        domainLevel2 + "." +
        domainLevel1 + "." +
        home.getProgramNameLower() + "." +
        home.getProgramNameLower() + "strings";
    // System.out.println ("resource bundle = " + resourceBundleName);

    /*if ((language.length() > 0 && (! language.equals ("  ")))
        || (country.length() > 0 && (! country.equals ("  ")))) {
      localizer = Localizer.getShared (resourceBundleName, language, country);
    } else {
      localizer = Localizer.getShared (resourceBundleName);
    }*/

    trouble.setParent (mainFrame);
    if (mainFrame != null) {
      mainFrame.setBounds (
          userPrefs.getPrefAsInt (LEFT, DEFAULT_LEFT),
          userPrefs.getPrefAsInt (TOP,  DEFAULT_TOP),
          userPrefs.getPrefAsInt (WIDTH, DEFAULT_WIDTH),
          userPrefs.getPrefAsInt (HEIGHT, DEFAULT_HEIGHT));
    }
    xos.setXHandler (xhandler);
    if (mainFrame != null) {
      xos.setMainWindow (mainFrame);
    }
  }

  public void setMainFrame(JFrame mainFrame) {
    this.mainFrame = mainFrame;
    if (mainFrame != null) {
      xos.setMainWindow (mainFrame);
    }
  }
  
  public void handleQuit () {
    if (mainFrame != null) {
      userPrefs.setPref (LEFT, mainFrame.getX());
      userPrefs.setPref (TOP, mainFrame.getY());
      userPrefs.setPref (WIDTH, mainFrame.getWidth());
      userPrefs.setPref (HEIGHT, mainFrame.getHeight());
    }
    boolean prefsOK = userPrefs.savePrefs();
  }
  
  public boolean openURL (File file) {

      boolean ok = true;
      
      URI uri = file.toURI();
      try {
        URL url = uri.toURL();
        ok = openURL(url.toString());
      } catch (MalformedURLException e) {
        ok = false;
        Trouble.getShared().report(
            mainFrame, 
            "Trouble opening uri " + uri.toString(), 
            "URI Problem");
        Logger.getShared().recordEvent(
            LogEvent.MEDIUM, 
            "Attempt to open URI " + uri.toString() + 
              " returned exception: " + e.toString(), 
            false);
      }

      return ok;
  }
  
  public boolean openURL (String url) {
    
    boolean ok = true;
    
    Logger.getShared().recordEvent
        (LogEvent.NORMAL, 
        "openURL Passed URL = " + StringUtils.cleanURLString(url), 
        false);
    
    String urlToOpen = StringUtils.cleanURLString(url);
    
    Logger.getShared().recordEvent
        (LogEvent.NORMAL, 
        "openURL Cleaned URL = " + urlToOpen, 
        false);

    try {
      xos.openURL (urlToOpen);
    } catch (IOException e) {
      ok = false;
      Trouble.getShared().report(
          mainFrame, 
          "Trouble opening url " + url, 
          "URL Problem");
      Logger.getShared().recordEvent(
          LogEvent.MEDIUM, 
          "Attempt to open URL " + url + 
            " returned exception: " + e.toString(), 
          false);
    }
    
    return ok;

  }
  
}
