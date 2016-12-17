/*
 * Copyright 2004 - 2016 Herb Bowie
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
  
  private     static  final int   DEFAULT_WIDTH = 780;
  private     static  final int   DEFAULT_HEIGHT = 540;
  
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
  
  private JMenuItem           helpCheckForUpdatesMenuItem;
  
  private JMenuItem           helpPSPubWebSite;
  
  private JMenuItem           helpSubmitFeedbackMenuItem;
  
  private JMenuItem           helpReduceWindowSize;
  
  private JFrame              mainWindow = null;
  
  
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
                       we can use it to find the program's folder).
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
    
    logNormalEvent("Java Version: " + System.getProperty("java.version"));
    logNormalEvent("Java Home: " + System.getProperty("java.home"));
    // Find the user's primary documents folder
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
    
    /*
     * Compute the absolute file path to the jar file.
     * The framework is based on http://stackoverflow.com/a/12733172/1614775
     * But that gets it right for only one of the four cases.
     * 
     * @param aclass A class residing in the required jar.
     * 
     * @return A File object for the directory in which the jar file resides.
     * During testing with NetBeans, the result is ./build/classes/,
     * which is the directory containing what will be in the jar.
     */
    URL url;
    String extURL;      //  url.toExternalForm();

    // get an url
    try {
        url = Home.class.getProtectionDomain().getCodeSource().getLocation();
          // url is in one of two forms
          //        ./build/classes/   NetBeans test
          //        jardir/JarName.jar  froma jar
    } catch (SecurityException ex) {
        url = Home.class.getResource(Home.class.getSimpleName() + ".class");
        // url is in one of two forms, both ending "/com/physpics/tools/ui/PropNode.class"
        //          file:/U:/Fred/java/Tools/UI/build/classes
        //          jar:file:/U:/Fred/java/Tools/UI/dist/UI.jar!
    }

    // convert to external form
    extURL = url.toExternalForm();

    // prune for various cases
    if (extURL.endsWith(".jar"))   // from getCodeSource
        extURL = extURL.substring(0, extURL.lastIndexOf("/"));
    else {  // from getResource
        String suffix = "/"+(Home.class.getName()).replace(".", "/")+".class";
        extURL = extURL.replace(suffix, "");
        if (extURL.startsWith("jar:") && extURL.endsWith(".jar!"))
            extURL = extURL.substring(4, extURL.lastIndexOf("/"));
    }

    // convert back to url
    try {
        url = new URL(extURL);
    } catch (MalformedURLException mux) {
        // leave url unchanged; probably does not happen
    }

    // convert url to File
    File jarFile;
    try {
        jarFile = new File(url.toURI());
    } catch(URISyntaxException ex) {
        jarFile = new File(url.getPath());
    }
    Logger.getShared().recordEvent(
        LogEvent.NORMAL, 
        "Home jar file = " + jarFile.toString(), 
        false);
    
    File mainExecFolder = jarFile;
    
    if (jarFile.getName().equalsIgnoreCase("lib")) {
      mainExecFolder = jarFile.getParentFile();
    }
    
    Logger.getShared().recordEvent(
        LogEvent.NORMAL,
        "Main Exec Folder = " + mainExecFolder.toString(),
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
    boolean bundleFound = false;
    File bundleFile = null;
    if ((userDirString.toLowerCase().indexOf ("netbeans") >= 0)
        || (userDirString.toLowerCase().indexOf ("nbproj") >= 0)
        || (userDirString.toLowerCase().indexOf ("source") >= 0)
        || (userDirString.toLowerCase().indexOf ("build") >= 0)) {
      Logger.getShared().recordEvent(
        LogEvent.NORMAL, 
        "Running in Development", 
        false);
      File pspubDocs = new File (userHome, "PSPub Docs");
      File jars = new File (pspubDocs, "jars");
      appFolder = jars;
    } else {
      Logger.getShared().recordEvent(LogEvent.NORMAL, 
          "Main Exec Folder name = " + mainExecFolder.getName(), false);
      if (mainExecFolder.getName().equals("Java")) {
        File contentsFolder = mainExecFolder.getParentFile();
        Logger.getShared().recordEvent(LogEvent.NORMAL, 
            "Contents Folder name = " + contentsFolder.getName(), false);
        if (contentsFolder.getName().equals("Contents")) {
          bundleFile = contentsFolder.getParentFile();
          File resourcesFolder = new File (contentsFolder, "Resources");
          File tryAppFolder = new File (resourcesFolder, "appfolder");
          Logger.getShared().recordEvent(LogEvent.NORMAL, 
              "Pontential App Folder = " + tryAppFolder.toString(), false);
          Logger.getShared().recordEvent(LogEvent.NORMAL, 
              "Pontential App Folder exists? " + String.valueOf(tryAppFolder.exists()), false);
          Logger.getShared().recordEvent(LogEvent.NORMAL, 
              "Pontential App Folder can be read? " + String.valueOf(tryAppFolder.canRead()), false);
          if (tryAppFolder.exists() && tryAppFolder.canRead()) {
            appFolder = tryAppFolder;
            bundleFound = true;
            Logger.getShared().recordEvent(
                LogEvent.NORMAL, 
                "Mac App Bundle? " 
                  + String.valueOf(bundleFound) 
                  + " - " 
                  + bundleFile.toString(), 
                false);
          }
        }
      }
    }

    // Create the image icon if we can find the icon file
    icon = null;
    File iconFolder = new File (appFolder, "logos");
    File iconFile = new File (iconFolder, programNameNoSpace + ".png");
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
    if (icon == null) {
      Logger.getShared().recordEvent(LogEvent.MEDIUM, 
          "The icon file could not be found at " + iconFile.toString(), 
          false);
    } else {
      Logger.getShared().recordEvent(LogEvent.NORMAL, 
          "Icon file loaded from " + iconFile.toString(), 
          false);
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
  public void setHelpMenu (JFrame mainWindow, JMenu helpMenu) {
    
    this.mainWindow = mainWindow;
    
    // Add User Guide Menu Item
    helpUserGuideMenuItem = new javax.swing.JMenuItem();
    helpUserGuideMenuItem.setText("User Guide");
    helpUserGuideMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        openUserGuide();
      }
    });
    helpMenu.insert(helpUserGuideMenuItem, 0);
    
    // Add Program History Menu Item
    helpHistoryMenuItem = new javax.swing.JMenuItem();
    helpHistoryMenuItem.setText("Program History");
    helpHistoryMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        openProgramHistory();
      }
    });
    helpMenu.insert(helpHistoryMenuItem, 1);
    
    helpMenu.insertSeparator(2);
    
    // Add Check for Updates Menu Item
    helpCheckForUpdatesMenuItem = new javax.swing.JMenuItem();
    helpCheckForUpdatesMenuItem.setText("Check for Updates...");
    helpCheckForUpdatesMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        ProgramVersion.getShared().informUserIfNewer();
        ProgramVersion.getShared().informUserIfLatest();
      }
    });
    helpMenu.insert(helpCheckForUpdatesMenuItem, 3);
    
    helpPSPubWebSite = new JMenuItem (programName + " Home Page");
    helpPSPubWebSite.addActionListener (new ActionListener() 
      {
        public void actionPerformed (ActionEvent event) {
          openHomePage();
        } // end ActionPerformed method
      } // end action listener
    );
    helpMenu.insert(helpPSPubWebSite, 4);
    
    helpSubmitFeedbackMenuItem = new JMenuItem ("Submit Feedback");
    helpSubmitFeedbackMenuItem.addActionListener (new ActionListener() 
      {
        public void actionPerformed (ActionEvent event) {
          openURL ("mailto:support@powersurgepub.com");
        } // end ActionPerformed method
      } // end action listener
    );
    helpMenu.insert(helpSubmitFeedbackMenuItem, 5);
    
    helpMenu.insertSeparator(6);
    
    helpReduceWindowSize = new JMenuItem ("Reduce Window Size");
    helpReduceWindowSize.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_W,
        Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    helpReduceWindowSize.addActionListener(new ActionListener()
      {
        public void actionPerformed (ActionEvent event) {
          setDefaultScreenSizeAndLocation();
        }
      });
    helpMenu.insert(helpReduceWindowSize, 7);

    
  } // end method
  
  public void setDefaultScreenSizeAndLocation() {

		mainWindow.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    mainWindow.setResizable (true);
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    int defaultX = (d.width - mainWindow.getSize().width) / 2;
    int defaultY = (d.height - mainWindow.getSize().height) / 2;
		mainWindow.setLocation (defaultX, defaultY);
  }
  
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
        "Home opening URL " + url + cleaningMsg,
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
            "I/O Exception with msg: " + ex.getMessage(),
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
   Log a normal event. 
  
   @param message The message to be written to the log. 
  */
  public void logNormalEvent(String message) {
    Logger.getShared().recordEvent(LogEvent.NORMAL, message, false);
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

