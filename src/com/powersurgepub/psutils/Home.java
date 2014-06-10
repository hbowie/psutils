/*
 * Copyright 2004 - 2013 Herb Bowie
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
  import java.awt.*;
  import java.awt.event.*;
  import java.io.*;
  import java.net.*;
  import java.util.prefs.*;
  import javax.swing.*;

/**
   A class that will identify key folders in the program's environment.
 */
public class Home {
  
  private static Home         home = null;
  
  private              Desktop desktop;
  private              boolean browserAvailable = false;
  
  private static final String PREFS_PREFIX   = "/com/powersurgepub/";
  private static final String HTML_FILE_EXT  = ".html";

  private static final String MAC_DOCS                    = "Documents";
  private static final String WINDOWS_DOCS                = "My Documents";
  
  private String              userName;
  private String              userDirString;
  private String              programName = "";  
  private String              programNameLower = "";
  private String              programNameNoSpace = "";
  private String              programVersion = "";
  private String              copyrightYearFrom = "1999";
  private File                appFolder;
  private File                prefsFolder;
  private File                userHome = null;
  private File                userDocs = null;
  private File                programDefaultDataFolder = null;
  private ImageIcon           icon = null;
  private boolean             runningFromDropbox = false;
  
  private Preferences         userRoot;
  private Preferences         userPreferences;
  private Preferences         systemRoot;
  private Preferences         systemPreferences;
  private XOS                 xos = XOS.getShared();
  
  private URL                 pageURL;
  private String              programHistory;
  private URL                 programHistoryURL;
  private JMenuItem           helpHistoryMenuItem;
  
  private JMenuItem						helpUserGuideMenuItem;
  
  private JSeparator          helpSeparator1;
  
  private JMenuItem           helpCheckForUpdatesMenuItem;
  
  private JMenuItem           helpPSPubWebSite;
  
  private JMenuItem           helpSubmitFeedbackMenuItem;
  
  private JSeparator          helpSeparator2;
  
  /** 
    Returns a single instance of Home that can be shared by many classes. This
    is the only way to obtain an instance of Home, since the constructor is
    private.
   
    @return A single, shared instance of Home.
   */  
  public static Home getShared() {

    return getShared ("", "");
  }
  
  /** 
    Returns a single instance of Home that can be shared by many classes. This
    is the only way to obtain an instance of Home, since the constructor is
    private.
   
    @return A single, shared instance of Home.
   
    @param programName Name of the current program (or enough of the name that 
                       we can use it to find the program's folder.
   */  
  public static Home getShared (String programName, String programVersion) {
    if (home == null) {
      home = new Home (programName, programVersion);
    }
    return home;
  }
  
  /** 
    Creates a new instance of Home 
    
    @param programName Name of the current program (or enough of the name that 
                       we can use it to find the program's folder.
   */
  private Home (String programName, String programVersion) {
    
    this.programName = programName.trim();
    this.programVersion = programVersion.trim();
    programNameLower = programName.toLowerCase();
    programNameNoSpace = StringUtils.wordDemarcation
        (programNameLower, "", -1, -1, -1);
    userName = System.getProperty (GlobalConstants.USER_NAME);
    userDirString = System.getProperty (GlobalConstants.USER_DIR);
    Logger.getShared().recordEvent(
        LogEvent.NORMAL, 
        "Program = " + programName + " " + programVersion, 
        false);
    Logger.getShared().recordEvent(
        LogEvent.NORMAL, 
        "User Directory = " + userDirString, 
        false);
    appFolder = new File (userDirString);
    prefsFolder = new File (userDirString);

    userHome = new File (System.getProperty (GlobalConstants.USER_HOME));
    Logger.getShared().recordEvent(
        LogEvent.NORMAL, 
        "User Home = " + userHome.toString(), 
        false);
    userDocs = new File (userHome, MAC_DOCS);
    if (userDocs != null
        && userDocs.exists()
        && userDocs.canRead()) {
      // We're good!
    } else {
      userDocs = new File (userHome, WINDOWS_DOCS);
      if (userDocs != null
          && userDocs.exists()
          && userDocs.canRead()) {
        // We're good!
      } else {
        userDocs = userHome;
      }
    }
    Logger.getShared().recordEvent(
        LogEvent.NORMAL, 
        "User Docs = " + userDocs.toString(), 
        false);
    
    if (userDirString.indexOf("/Dropbox") >= 0) {
      runningFromDropbox = true;
    } else {
      runningFromDropbox = false;
    }
    Logger.getShared().recordEvent(
        LogEvent.NORMAL, 
        "Running from Dropbox folder? " + String.valueOf(runningFromDropbox), 
        false);
    
    programDefaultDataFolder = new File (userDocs, this.programName);
    
    
    // Get nodes for Preferences
    userRoot = Preferences.userRoot();
    systemRoot = Preferences.systemRoot();
    userPreferences = userRoot.node (getPreferencesPath());
    systemPreferences = systemRoot.node (getPreferencesPath());
    
    // If we are running in development, then look for 
    // the normal application folder and use that as home.
    
    if ((userDirString.toLowerCase().indexOf ("netbeans") >= 0)
        || (userDirString.toLowerCase().indexOf ("nbproj") >= 0)
        || (userDirString.toLowerCase().indexOf ("source") >= 0)
        || (userDirString.toLowerCase().indexOf ("build") >= 0)
        // || (userDirString.toLowerCase().indexOf (programNameLower) < 0)
        ){
      Logger.getShared().recordEvent(
        LogEvent.NORMAL, 
        "Running in Development", 
        false);
      File folder = new File (userDirString);
      File next = folder;
      displayDirectory (next);
      
      // Climb up the directory tree until we hit the folder containing
      // application programs, or the apps folder within the projects
      // folder.
      
      while ((folder != null)
          && (folder.getName().toLowerCase().indexOf ("applications") < 0)
          && (folder.getName().toLowerCase().indexOf ("program files") < 0)
          && (folder.getName().toLowerCase().indexOf ("apps") < 0)) {
        next = folder.getParentFile();
        displayDirectory (next);
        folder = next;
        if (folder != null
            && folder.getName().equals("projects")) {
          File apps = new File (folder, "apps");
          if (apps.exists()
              && apps.isDirectory()) {
            folder = apps;
            next = apps;
            displayDirectory (next);
          } // end if apps folder found in project structure
        } // end if we found projects folder
      } // end while looking for an applications folder
      
      // Now search this directory looking for a folder containing
      // the name of our program. 
      
      if (next == null) {
        next = new File ("C:/Program Files");
      }
      if (! next.exists()) {
        next = new File ("/Applications");
      }
      if (next != null
          && next.exists()) {
        displayDirectory (next);
        File[] apps = next.listFiles();
        int i = -1;
        while (next != null
            && (i + 1) < apps.length
            && ((next.getName().toLowerCase().indexOf (programNameLower) < 0
                && next.getName().toLowerCase().indexOf (programNameNoSpace) < 0) 
              || (! next.isDirectory()))) {
          i++;
          next = apps [i];
          // displayDirectory (next);
        } // end while looking for program folder
      } // end if we found the folder containing user's apps
      
      // Now see if we need to dive deeper
      if (next != null
          && next.isDirectory()) {
        displayDirectory (next);
        File programFolder = new File (next, "mac");
        if (! programFolder.exists() || ! programFolder.isDirectory()) {
          programFolder = new File (next, "execjar");
        }
        if (programFolder.exists()
            && programFolder.isDirectory()) {
          File[] macFolders = programFolder.listFiles();
          int j = -1;
          File nextMacFolder = programFolder;
          while (nextMacFolder != null
              && (j + 1) < macFolders.length
              && ((nextMacFolder.getName().toLowerCase().indexOf (programNameLower) < 0
                  && nextMacFolder.getName().toLowerCase().indexOf (programNameNoSpace) < 0) 
                || (! nextMacFolder.isDirectory()))) {
            j++;
            nextMacFolder = macFolders [j];
            displayDirectory (nextMacFolder);
          } // end while looking for program folder
          if (nextMacFolder != null
              && nextMacFolder.exists()
              && nextMacFolder.isDirectory()) {
            next = nextMacFolder;
            displayDirectory (next);
          }
        } // end if we found a mac folder
      } // end if we have a directory to work with
      
      // If we found something, then set the appropriate folders
      if (next != null
          && next.isDirectory()
          && (next.getName().toLowerCase().indexOf (programNameLower) >= 0
              || next.getName().toLowerCase().indexOf (programNameNoSpace) >= 0)) {
        appFolder = next;
        prefsFolder = next;
      }
    } // end if we are running in development
    
    // Check for a Mac application bundle
    boolean bundleFound = lookForMacAppBundleFolder (appFolder);
    if (! bundleFound) {
      bundleFound = lookForMacAppBundleFolder 
          (new File (appFolder, programNameNoSpace + ".app"));
    }
    if (! bundleFound) {
      bundleFound = lookForMacAppBundleFolder
          (new File (appFolder, programName + ".app"));
    }

    // Create the image icon if we can find the icon file
    icon = null;
    File iconFile = new File (appFolder, programNameNoSpace + "_icon.png");
    if (iconFile.exists()) {
      URI iconURI = iconFile.toURI();
      URL iconURL = null;
      try {
        iconURL = iconURI.toURL();
        icon = new ImageIcon(iconURL);
      } catch (MalformedURLException e) {
        System.out.println ("iconURI malformed URL");
      }
    } else {
      System.out.println ("Icon file not found at " + iconFile.toString());
    }
    
    // Create a URL pointing to the applications folder
    if (appFolder == null) {
      Trouble.getShared().report ("The " + getProgramName() 
          + " Folder could not be found", 
          "App Folder Missing");
    } else {
      Logger.getShared().recordEvent (LogEvent.NORMAL, 
        "App Folder = " + appFolder.toString(),
        false);
      try {
        pageURL = appFolder.toURI().toURL(); 
      } catch (MalformedURLException e) {
        Trouble.getShared().report ("Trouble forming pageURL from " + 
            appFolder.toString(), 
            "URL Problem");
      }
    }
    
    if (Desktop.isDesktopSupported()) {
      desktop = Desktop.getDesktop();
      if (desktop.isSupported(Desktop.Action.BROWSE)) {
        browserAvailable = true;
      } else {
        Logger.getShared().recordEvent(
            LogEvent.HIGH_SEVERITY, 
            "Desktop Browse not supported", 
            false);
      }
    } else {
      desktop = null;
        Logger.getShared().recordEvent(
            LogEvent.HIGH_SEVERITY, 
            "Desktop not available", 
            false);
    }
    
  } // end constructor
  
  private boolean lookForMacAppBundleFolder (File folder) {
    boolean itworked = false;
    if (xos.isRunningOnMacOS() && folder != null) {
      File macAppBundleFolder = new File (folder, 
          "Contents/Resources/appfolder");
      if (macAppBundleFolder != null
          && macAppBundleFolder.exists()) {
        appFolder = macAppBundleFolder;
        itworked = true;
      } // end if appfolder found with Mac app bundle
    } // end if we're running on a Mac and have a folder to work with
    Logger.getShared().recordEvent(
        LogEvent.NORMAL, 
        "Mac App Bundle? " 
          + String.valueOf(itworked) 
          + " - " 
          + folder.toString(), 
        false);
    return itworked;
  } // end method
  
  private void displayDirectory (File dir) {
    if (dir != null) {
      Logger.getShared().recordEvent(
          LogEvent.NORMAL, 
          "Navigating to => " + dir.toString(), 
          false);
    }
  }
  
  public boolean isAppFolder () {
    return (appFolder != null);
  }
  
  /**
   Is the application running from a Dropbox folder?
  
   @return True or false.
  */
  public boolean isRunningFromDropbox() {
    return runningFromDropbox;
  }
  
  /**
    Return the folder containing application resources.
   
    @return Folder containing the application and its resources.
   */
  public File getAppFolder () {
    return appFolder;
  }
  
  /**
   Return the app folder location as a URL.
  
   @return The App folder location as a URL, or null, if the conversion to a
           URL failed for some reason. 
  */
  public URL getPageURL () {
    return pageURL;
  }
  
  /**
    Return the folder containing user prefs for this application.
    
    @return Folder containing user prefs for this application.
   */
  public File getPrefsFolder () {
    return prefsFolder;
  }
  
  /**
    Return the Preferences node for the system.
   
    @return Preferences node for the system.
   */
  public Preferences getSystemPreferences () {
    return systemPreferences;
  }
  
  public File getUserHome () {
    return userHome;
  }
  
  /**
   Return the user's Documents folder (on a Mac) or My Documents folder (on
   Windows) or just the user's home folder, if nothing else is available. 
  
   @return The user's folder for storing documents. 
  */
  public File getUserDocs() {
    return userDocs;
  }
  
  /**
   Return the default data folder for this program and this user on this
   computer. 
  
   @return The default data folder.
  */
  public File getProgramDefaultDataFolder() {
    return programDefaultDataFolder;
  }
  
  /**
   If the default data folder does not yet exist, then create it. 
  
   @return True if the data folder now exists; false otherwise.
  */
  public boolean ensureProgramDefaultDataFolder() {
    boolean exists = false;
    if (programDefaultDataFolder.exists()) {
      exists = true;
    } else {
      exists = programDefaultDataFolder.mkdir();
    }
    return exists;
  }
  
  /**
    Return the Preferences node for the user.
   
    @return Preferences node for the user.
   */
  public Preferences getUserPreferences () {
    return userPreferences;
  }
  
  /**
    Return the Preferences path for this program.
   
    @return Preferences path for the program.
   */
  public String getPreferencesPath () {
    return PREFS_PREFIX + programNameNoSpace;
  }
  
  /**
    Return the name of the executing program.
   
    @return Program name.
   */
  public String getProgramName () {
    return programName;
  }
  
  /**
    Return the name of the executing program.
   
    @return Program name in all lower-case letters.
   */
  public String getProgramNameLower () {
    return programNameLower;
  }
  
  /**
    Return the name of the executing program.
   
    @return Program name in all lower-case letters and with any spaces
            removed.
   */
  public String getProgramNameNoSpace () {
    return programNameNoSpace;
  }

  public ImageIcon getIcon() {
    return icon;
  }
  
  /**
   Return the program version.
   
   @return Program Version.
   */
  public String getProgramVersion () {
    return programVersion;
  }
  
  /**
   Set the program version.
   
   */
  public void setProgramVersion (String programVersion) {
    this.programVersion = programVersion;
  }
  
  /**
   Set the first year this work was copyrighted. 
  
   @param copyrightYearFrom The first year that this program was copyrighted. 
  */
  public void setCopyrightYearFrom (String copyrightYearFrom) {
    this.copyrightYearFrom = copyrightYearFrom;
  }
  
  /**
   Get the first year that this work was copyrighted. 
  
   @return The first year that this work was copyrighted. 
  */
  public String getCopyrightYearFrom () {
    return copyrightYearFrom;
  }
  
  /**
    Pass the JMenu item acting as the Help menu. If not running on a Mac, then
    an About menu item will be added. 
   
    @param helpMenu JMenu acting as the Help menu. 
   */
  public void setHelpMenu (JMenu helpMenu) {
    
    // Add Program History Menu Item
    helpHistoryMenuItem = new javax.swing.JMenuItem();
    helpHistoryMenuItem.setText("Program History");
    helpHistoryMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        openProgramHistory();
      }
    });
    helpMenu.add(helpHistoryMenuItem);
    
    // Add User Guide Menu Item
    helpUserGuideMenuItem = new javax.swing.JMenuItem();
    helpUserGuideMenuItem.setText("User Guide");
    helpUserGuideMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        openUserGuide();
      }
    });
    helpMenu.add(helpUserGuideMenuItem);
    
    helpSeparator1 = new JSeparator();
    helpMenu.add (helpSeparator1);
    
    // Add Check for Updates Menu Item
    helpCheckForUpdatesMenuItem = new javax.swing.JMenuItem();
    helpCheckForUpdatesMenuItem.setText("Check for Updates...");
    helpCheckForUpdatesMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        ProgramVersion.getShared().informUserIfNewer();
        ProgramVersion.getShared().informUserIfLatest();
      }
    });
    helpMenu.add(helpCheckForUpdatesMenuItem);
    
    helpPSPubWebSite = new JMenuItem (programName + " Home Page");
    helpMenu.add (helpPSPubWebSite);
    helpPSPubWebSite.addActionListener (new ActionListener() 
      {
        public void actionPerformed (ActionEvent event) {
          openHomePage();
        } // end ActionPerformed method
      } // end action listener
    );
    
    helpSubmitFeedbackMenuItem = new JMenuItem ("Submit Feedback");
    helpMenu.add (helpSubmitFeedbackMenuItem);
    helpSubmitFeedbackMenuItem.addActionListener (new ActionListener() 
      {
        public void actionPerformed (ActionEvent event) {
          openURL ("mailto:support@powersurgepub.com");
        } // end ActionPerformed method
      } // end action listener
    );
    
    helpSeparator2 = new JSeparator();
    helpMenu.add (helpSeparator2);
    
  } // end method
  
  public JMenuItem getHelpMenuItem() {
    return helpUserGuideMenuItem;
  }
  
  /**
   Open the user guide for the program. Presumed to be found in the application
   resource folder, named with the program name in all lower cases, no spaces,
   with an html file extension. 
  */
  public void openUserGuide() {
    URL userGuideURL;
    try {
      userGuideURL = new URL 
          (pageURL, "help/" + programNameNoSpace + "-user-guide.html");
      openURL (userGuideURL);
    } catch (MalformedURLException e) {
    }
  }
  
  public void openProgramHistory() {
  
    try {
      programHistoryURL = new URL 
          (pageURL, "help/" + programNameNoSpace + "-history.html");
      openURL (programHistoryURL);
    } catch (MalformedURLException e) {
      System.out.println("Home.openProgramHistory MalformedURLException " + e.toString());
    }
  }
  
  public void openHomePage() {
    openURL ("http://www.powersurgepub.com/products/"
        + programNameNoSpace + "/index.html");
  }
  
  /**
   Open the passed URL in the user's preferred browser. 
  
   @param url The url to be opened. 
  
   @return True if everything seemed to go OK. 
  */
  public boolean openURL (URL url) {
    boolean ok = openURL (url.toString());
    return ok;
  }
  
  /**
   Open the passed local file in the user's preferred browser. 
  
   @param file The file to be opened. 
  
   @return True if everything seemed to go OK. 
  */
  public boolean openURL (File file) {

    boolean ok = true;

    URI uri = file.toURI();
    try {
      URL url = uri.toURL();
      ok = openURL(url.toString());
    } catch (MalformedURLException e) {
      ok = false;
      Trouble.getShared().report(
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
  
  /**
   Open the passed URL in the user's preferred browser. 
  
   @param url The url to be opened. 
  
   @return True if everything seemed to go OK. 
  */
  public boolean openURL (String url) {
    
    boolean ok = true;
    
    String urlToOpen = StringUtils.cleanURLString(url);
    
    String cleaningMsg = "";
    if (! url.equals(urlToOpen)) {
      cleaningMsg = " (" + urlToOpen + " after cleaning)";
    }
    
    Logger.getShared().recordEvent
        (LogEvent.NORMAL,
        "Home opening URL " + url + " using BrowserLauncher2" + cleaningMsg,
        false);

    if (browserAvailable) {
        try {
          URI uri = new URI(urlToOpen);
          desktop.browse(uri);
        } catch (URISyntaxException e) {
          Logger.getShared().recordEvent
            (LogEvent.NORMAL,
            "URI Syntax Exception",
            false);
        } catch (IOException ex) {
          Logger.getShared().recordEvent
            (LogEvent.NORMAL,
            "I/O Exception",
            false);
        }
    } else {
      ok = false;
      Trouble.getShared().report(
          "Trouble opening url " + url, 
          "URL Problem");
      Logger.getShared().recordEvent(
          LogEvent.MEDIUM, 
          "Attempt to open URL " + url + 
            " failed", 
          false);
    }
    
    return ok;

  }
  
  /**
     Creates a LogEvent object and then records it.
    
     @param severity      the severity of the event
    
     @param message       the message to be written to the log
    
     @param dataRelated   indicates whether this event is related
                          to preceding data.
   */
  public void log (int severity, String message, boolean dataRelated) {
    Logger.getShared().recordEvent (severity, message, dataRelated);
  }
  
} // end class

