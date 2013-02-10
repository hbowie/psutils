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

  import java.io.*;
  import java.util.*;
  import java.util.prefs.*;
  import javax.swing.*;

/**
   A set of user preferences that can persist from session to session. These are
   now stored using the standard Java Preferences API. These
   were previously stored to disk as a properties file called parms.txt. 
   The file was saved in the same directory housing the application program.<p>
 
   A common use for UserPrefs is to store registration information for a product:
   a user name and registration code. The RegistrationCode class has special
   features that can attempt to synchronize changes between a RegistrationCode object
   and a UserPrefs object. 
 */
public class UserPrefs {
  
  public static final String LEFT                       = "left";
  public static final String TOP                        = "top";
  public static final String WIDTH                      = "width";
  public static final String HEIGHT                     = "height";

  /** Properties key for name of registered user. */
  public static final String USER_NAME_KEY              = "username";

  /** Properties key for registration code. */
  public static final String REG_CODE_KEY               = "regcode";
  
  /** Single shared occurrence of userPrefs. */
  private static UserPrefs    userPrefs;
  
  private Preferences         userPreferences = null;
  private Preferences         systemPreferences = null;
  private String[]            userKeys;
  private String[]            systemKeys;
  
  private String              userName;
  private Home                home;
  private File                prefsFolder;
  private File                parmsFile;
  
  private String              programName = "";
  
  private boolean             unsavedPrefs = false;
  private static final String PARM_FILE_NAME = "parms.txt";
  private static final String USER_PREFS_EXPORT_NAME    = "_userprefs.xml";
  private String              userPrefsExportName = "";
  private static final String SYSTEM_PREFS_EXPORT_NAME  = "_sysprefs.xml";
  private String              systemPrefsExportName = "";
  private static final String PARM_FILE_ID   = "User Prefs file";
  private FileInputStream     parmsIn;
  private FileOutputStream    parmsOut;
  private Properties					parmProperties;
  // private RegistrationCode		registration;
  
  private static final String USER_PREFS_DROPBOX_NAME   
      = "_dropbox_userprefs.xml";
  private String              userPrefsDropboxName = "";
  private static final String SYSTEM_PREFS_DROPBOX_NAME 
      = "_dropbox_sysprefs.xml";
  private String              systemPrefsDropboxName = "";
  
  private Trouble             trouble;
  
  private int                 parmsLoaded = 0;
  private int                 prefsLoaded = 0;
  private static final int    PREFS_NOT_AVAILABLE       = 0;
  private static final int    PREFS_FROM_PREFS          = 1;
  private static final int    PREFS_FROM_PREFS_BACKUP   = 2;
  private static final int    PREFS_FROM_PARMS          = 3;
  private static final int    PREFS_FROM_PARMS_BACKUP   = 4;
  
  private File                altParmsFolder;
  
  /** 
    Returns a single instance of UserPrefs that can be shared by many classes. This
    is the only way to obtain an instance of UserPrefs, since the constructor is
    private.
   
    @return A single, shared instance of UserPrefs.
   */  
  public static UserPrefs getShared() {
    if (userPrefs == null) {
      userPrefs = new UserPrefs();
    }
    return userPrefs;
  }
  
  /** 
    Returns a single instance of UserPrefs that can be shared by many classes. This
    is the only way to obtain an instance of UserPrefs, since the constructor is
    private.
   
    @return A single, shared instance of UserPrefs.
    @param  programName Name of this program (to be found in its 
                        enclosing folder).
   */  
  public static UserPrefs getShared(String programName) {
    if (userPrefs == null) {
      userPrefs = new UserPrefs(programName);
    }
    return userPrefs;
  }
  
  /** 
    Creates a new instance of UserPrefs 
   */
  private UserPrefs() {
    
    commonConstruction();
  }  
  
  /** 
    Creates a new instance of UserPrefs 
   
    @param  programName Name of this program (to be found in its 
                        enclosing folder).
   */
  private UserPrefs (String programName) {
    
    setProgramName (programName);
    commonConstruction();
  }  
  
  private void commonConstruction() {
    
    // No parms yet...
    parmsLoaded = 0;
    prefsLoaded = 0;
    
    // Prepare for trouble...
    trouble = Trouble.getShared();
    
    // Get default registration properties
    parmProperties = new Properties();
    parmProperties.setProperty(USER_NAME_KEY, "");
    parmProperties.setProperty(REG_CODE_KEY, "");
    
    // Get general-purpose system properties
    userName = System.getProperty (GlobalConstants.USER_NAME);
    home = Home.getShared ();
    if (programName.equals ("")) {
      programName = home.getProgramName();
    }
    prefsFolder = home.getPrefsFolder();
    userPrefsExportName 
        = home.getProgramNameNoSpace() + USER_PREFS_EXPORT_NAME;
    systemPrefsExportName 
        = home.getProgramNameNoSpace() + SYSTEM_PREFS_EXPORT_NAME;
    
    // Get Preferences, if they exist
    if (home.isRunningFromDropbox()) {
      loadDropboxPrefs();
    } 
    if (userPreferences == null) {
      userPreferences = home.getUserPreferences();
    }
    if (systemPreferences == null) {
      systemPreferences = home.getSystemPreferences();
    }

    try {
      userKeys = userPreferences.keys();
    } catch (BackingStoreException e) {
      trouble.report ("User Preferences not available", 
					"Backing Store Exception");
      userKeys = new String[0];
    }
    try {
      systemKeys = systemPreferences.keys();
    } catch (BackingStoreException e) {
      trouble.report ("System Preferences not available", 
					"Backing Store Exception");
      systemKeys = new String[0];
    }
    if (userKeys.length > 0 || systemKeys.length > 0) {
      // we found some properties
      prefsLoaded = PREFS_FROM_PREFS;
      log (LogEvent.NORMAL,
          "Loaded User Prefs from " + home.getPreferencesPath(),
          false);
      // System.out.println ("Displaying preferences...");
      
      // for (int i = 0; i < userKeys.length; i++) {
        // System.out.println ("  User Key = " + userKeys [i]
        //     + " Value = " 
        //     + userPreferences.get (userKeys [i], "-- No Value --"));
      // } // end for every user key
      
      // for (int i = 0; i < systemKeys.length; i++) {
        // System.out.println ("  System Key = " + systemKeys [i]
          //   + " Value = " 
          //   + systemPreferences.get (systemKeys [i], "-- No Value --"));
      // } // end for every system key
      
    } // end if any keys found

    // Try to get info from Parms file
    parmsFile = new File (prefsFolder, PARM_FILE_NAME);
    try {
      parmsIn = new FileInputStream (parmsFile);
      parmProperties.load (parmsIn);
      parmsLoaded = PREFS_FROM_PARMS;
      // convertParmsToPrefs();
    } catch (IOException e) {
      // No joy...
    }
    
    if (prefsLoaded == PREFS_NOT_AVAILABLE
        && parmsLoaded > PREFS_NOT_AVAILABLE) {
      convertParmsToPrefs();
      log (LogEvent.MINOR,
          "Loaded User Parms from " + prefsFolder.toString() + PARM_FILE_NAME,
          false);
    }
    
  } // end commonConstruction method
  
  /**
    Sets a second folder in which we can save a backup copy of the parms
    file. We will also try to load the user preferences from this file,
    if they could not be found in the primary location.
   
    @param altParmsFolder A folder in which a spare copy of the parms file
                          can be stashed.
   */
  public void setAltParmsFolder (File altParmsFolder) {
    if (altParmsFolder.isDirectory()
        && altParmsFolder.exists()) {
      this.altParmsFolder = altParmsFolder;
 
      if (prefsLoaded == PREFS_NOT_AVAILABLE) {
        File userPrefsFile = new File (altParmsFolder, userPrefsExportName);
        if (userPrefsFile.exists()) {
          try {
            FileInputStream userPrefsIn = new FileInputStream (userPrefsFile);
            userPreferences.importPreferences (userPrefsIn);
            prefsLoaded = PREFS_FROM_PREFS_BACKUP;
            log (LogEvent.MINOR,
                "Loaded backup user prefs from " 
                + altParmsFolder.toString() + userPrefsExportName,
                false);
          } catch (Exception e) {
            // No need to report, since this is only a spare
          }
        } // end if user prefs file exists

        File systemPrefsFile = new File (altParmsFolder, systemPrefsExportName);
        if (systemPrefsFile.exists()) {
          try {
            FileInputStream systemPrefsIn = new FileInputStream (systemPrefsFile);
            systemPreferences.importPreferences (systemPrefsIn);
            parmsLoaded = PREFS_FROM_PREFS_BACKUP;
            log (LogEvent.MINOR,
                "Loaded backup system prefs from " 
                + altParmsFolder.toString() + userPrefsExportName,
                false);
          } catch (Exception e) {
            // No need to report, since this is only a spare anyway
          }
        }
      } else {  // end if prefs not available
        saveAltPrefs();
      }
      
      if (parmsLoaded == PREFS_NOT_AVAILABLE) {
        try {
          File parms2 = new File (altParmsFolder, PARM_FILE_NAME);
          parmsIn = new FileInputStream (parms2);
          parmProperties.load (parmsIn);
          parmsLoaded = PREFS_FROM_PARMS_BACKUP;
          log (LogEvent.MINOR,
              "Loaded alt parms file from " 
              + altParmsFolder.toString() + "/" + PARM_FILE_NAME,
              false);
          // convertParmsToPrefs();
        } catch (IOException e) {
          // Damn... still no joy
        } // end catch IOException
      } // end if prefs still not available
      if (prefsLoaded == PREFS_NOT_AVAILABLE
          && parmsLoaded > PREFS_NOT_AVAILABLE) {
        convertParmsToPrefs();
      }
    } // end if passed File refers to a valid folder
  } // end method 
  
  /** 
    Sets a program name to be placed in the header of the preferences properties file.
    
    @param programName  The name of the application program 
                        for which preferences are being saved.
   */  
  public void setProgramName (String programName) {
    this.programName = programName + " ";
  }
  
  /**
    Returns a list of all user pref keys.
   */
  public Enumeration keys () {
    if (prefsLoaded > PREFS_NOT_AVAILABLE) {
      StringList keys = new StringList();
      try {
        keys.populate (userPreferences.keys());
      } catch (BackingStoreException e) {
        log (LogEvent.MINOR,
            "User Prefs Backing Store Exception",
            false);
      }
      return keys;
    } else {
      log (LogEvent.MINOR,
          "User Prefs not available",
          false);
      if (parmsLoaded > PREFS_NOT_AVAILABLE) {
        log (LogEvent.MINOR,
            "Returning Parms Keys",
            false);
        return parmProperties.keys();
      } else {
        return new StringList();
      }
    }
  }

  /** 
    Returns a user preference string, based on the passed key. Note that the user
    name for the user who is currently logged on to the computer will be appended to
    the passed key, so that each user using a shared computer will be able to save
    and retrieve their own unique preferences.
   
    @param key The string to be used as a key to identify this value.
    @return The desired value, if found.
   */  
  public String getPref (String key) {
    String pref = "";
    if (prefsLoaded > PREFS_NOT_AVAILABLE) {
      pref = userPreferences.get(key, "").trim();
    }
    if (pref.length() == 0
        && parmsLoaded > PREFS_NOT_AVAILABLE) {
      pref = parmProperties.getProperty(key + "." + userName);
      if (pref == null) {
        pref = "";
      } else {
        pref = pref.trim();
      }
      if (pref.length() > 0) {
        log (LogEvent.MINOR,
            "Retrieved " + key + "=" + pref + " from parms",
            false);
        convertParmsToPrefs();
      }
    }
    return pref;
  }
  
  public String getPref (String key, String defaultValue) {
    String pref = getPref (key);
    if (pref.length() == 0) {
      pref = defaultValue;
    }
    return pref;
  }
  
  public int getPrefAsInt (String key, int defaultValue) {
    String pref = getPref (key);
    int prefInt = 0;
    if (pref.length() == 0) {
      prefInt = defaultValue;
    } else {
      try {
        prefInt = Integer.parseInt (pref);
      } catch (NumberFormatException e) {
        prefInt = defaultValue;
      }
    }
    return prefInt;
  }
  
  public long getPrefAsLong (String key, long defaultValue) {
    String pref = getPref (key);
    long prefLong = 0;
    if (pref.length() == 0) {
      prefLong = defaultValue;
    } else {
      try {
        prefLong = Long.parseLong (pref);
      } catch (NumberFormatException e) {
        prefLong = defaultValue;
      }
    }
    return prefLong;
  }
  
  public boolean getPrefAsBoolean (String key, boolean defaultValue) {
    boolean prefBoolean = defaultValue;
    String pref = getPref (key);
    if (pref.length() > 0) {
      String prefChar = pref.toLowerCase().substring (0,1);
      if (prefChar.equals ("t") || prefChar.equals ("y")) {
        prefBoolean = true;
      } else {
        prefBoolean = false;
      }
    }
    return prefBoolean;
  }
  
  public int getPrefAsComboBoxIndex (
      String key, 
      JComboBox combo, 
      int defaultValue) {
    String pref = getPref(key);
    if (pref.length() == 0) {
      return defaultValue;
    } else {
      int i = 0;
      while (i < combo.getItemCount()) {
        Object comboObject = combo.getItemAt(i);
        String comboStr = comboObject.toString();
        if (pref.equalsIgnoreCase(comboStr)) {
          return i;
        } // end if match found
        i++;
      } // end while looking for match
      return defaultValue;
    } // end if pref was found
  } // end method
  
  /** 
    Stores a user preference string, based on the passed key. Note that the user
    name for the user who is currently logged on to the computer will be appended to
    the passed key, so that each user using a shared computer will be able to save
    and retrieve their own unique preferences. Note that the passed data will only
    be saved in memory, and will not be saved to disk until savePrefs is called.
   
    @param key The key of the data to be stored.
    @param data The data to be associated with the passed key.
   */  
  public void setPref (String key, String data) {
    userPreferences.put (key, data);
    // parmProperties.setProperty (key + "." + userName, data);
    prefsChanged();
  }
  
  public void setPref (String key, int data) {
    userPreferences.putInt (key, data);
    prefsChanged();
  }
  
  public void setPref (String key, long data) {
    userPreferences.putLong (key, data);
    prefsChanged();
  }
  
  public void setPref (String key, boolean data) {
    userPreferences.putBoolean (key, data);
    prefsChanged();
  }
  
  /**
    Returns a list of all user pref keys.
   */
  public Enumeration commonKeys () {
    if (prefsLoaded > PREFS_NOT_AVAILABLE) {
      StringList keys = new StringList();
      try {
        keys.populate (systemPreferences.keys());
      } catch (BackingStoreException e) {
        log (LogEvent.MINOR,
            "Common Prefs Backing Store Exception",
            false);
      }
      return keys;
    } else {
      log (LogEvent.MINOR,
          "Common Prefs not available",
          false);
      if (parmsLoaded > PREFS_NOT_AVAILABLE) {
        log (LogEvent.MINOR,
            "Returning Parms Keys",
            false);
        return parmProperties.keys();
      } else {
        return new StringList();
      }
    }
  }
  
  /** 
    Returns a user preference string, based on the passed key. Nothing will be
    appended to the passed key, so multiple users of a single computer will all
    share the same values for this key.
   
    @param key The key used to identify this data.
    @return The data associated with this key.
   */  
  public String getCommonPref (String key) {
    // return systemPreferences.get (key, "");
    // return parmProperties.getProperty (key);
    String pref = "";
    if (prefsLoaded > PREFS_NOT_AVAILABLE) {
      pref = systemPreferences.get(key, "").trim();
    }
    if (pref.length() == 0
        && parmsLoaded > PREFS_NOT_AVAILABLE) {
      pref = parmProperties.getProperty(key).trim();
      log (LogEvent.MINOR,
          "Retrieved common pref " + key + "=" + pref + " from parms",
          false);
      if (pref.length() > 0) {
        convertParmsToPrefs();
      }
    }
    return pref;
  }
  
  public String getCommonPref (String key, String defaultValue) {
    String pref = getCommonPref (key);
    if (pref.length() == 0) {
      pref = defaultValue;
    }
    return pref;
  }
  
  public int getCommonPrefAsInt (String key, int defaultValue) {
    String pref = getCommonPref (key);
    int prefInt = 0;
    if (pref.length() == 0) {
      prefInt = defaultValue;
    } else {
      try {
        prefInt = Integer.parseInt (pref);
      } catch (NumberFormatException e) {
        prefInt = defaultValue;
      }
    }
    return prefInt;
  }
  
  /** 
    Stores a user preference string, based on the passed key. Nothing will be
    appended to the passed key, so multiple users of a single computer will all
    share the same values for this key. Note that the passed data will only
    be saved in memory, and will not be saved to disk until savePrefs is called.
   
    @param key The key used to identify this data.
    @param data The data to be associated with the passed key.
   */  
  public void setCommonPref (String key, String data) {
    systemPreferences.put (key, data);
    // parmProperties.setProperty (key, data);
    prefsChanged();
  }
  
  public void setCommonPref (String key, int data) {
    systemPreferences.putInt (key, data);
    prefsChanged();
  }
  
  /**
    Sets a flag indicating that preferences have been modified, and need to be
    saved.
   */
  private void prefsChanged() {
    unsavedPrefs = true;
  }
  
  /** 
    Saves the preferences to disk, if any prefs have been modified since the last
    open or save. If problems are encountered during the save, then they will be
    reported to Trouble.
   
    @return True if no problems were encountered, false otherwise.
   */  
  public boolean savePrefs() {

    /* No longer saving a parms file
     boolean ok = false;
     if (unsavedPrefs) {
      try {
        parmsOut = new FileOutputStream (parmsFile);
        parmProperties.save (parmsOut, programName + PARM_FILE_ID);
        unsavedPrefs = false;
        ok = true;
      } catch (IOException e) {
        String userName = getCommonPref (RegistrationCode.USER_NAME_KEY);
        if (userName != null
            && userName.length() > 0) {
          trouble.report ("User Prefs Error",
              "User Preferences could not be saved");
        }
      }
      saveAltPrefs();
    } // end if anything to save
    return ok;
     */
    try {
      userPreferences.sync();
    } catch (java.util.prefs.BackingStoreException e) {
      log (LogEvent.MEDIUM,
          "Preferences backing store exception", false);
    }
    
    try {
      systemPreferences.sync();
    } catch (java.util.prefs.BackingStoreException e) {
      log (LogEvent.MEDIUM,
          "Preferences backing store exception", false);
    }
    
    if (home.isRunningFromDropbox()) {
      saveDropboxPrefs();
    }
    if (unsavedPrefs) {
      saveAltPrefs();
    }
    unsavedPrefs = false;
    return true;
  } // end method
  
  /**
    Stash a copy of the preferences somewhere else, if an alternate location
    has been provided.
   */
  private void saveAltPrefs () {
    
    if (altParmsFolder != null) {
      try {
        File userPrefsFile = new File (altParmsFolder, userPrefsExportName);
        FileOutputStream userPrefsOut = new FileOutputStream (userPrefsFile);
        userPreferences.exportSubtree (userPrefsOut);
      } catch (Exception e) {
        // No need to report failure
      }
      
      try {
        File systemPrefsFile = new File (altParmsFolder, systemPrefsExportName);
        FileOutputStream systemPrefsOut = new FileOutputStream (systemPrefsFile);
        systemPreferences.exportSubtree (systemPrefsOut);
      } catch (Exception e) {
        // No need to report failure
      }
      
      /*
      try {
        File parms2 = new File (altParmsFolder, PARM_FILE_NAME);
        parmsOut = new FileOutputStream (parms2);
        parmProperties.save (parmsOut, programName + PARM_FILE_ID);
      } catch (IOException e) {
        // no need to report anything, since this is a spare
      }
       */
    } // end if folder available
  } // end method
  
  /**
   If the program was launched from a Dropbox folder, then read the
   preferences from the program folder. 
  */
  private void loadDropboxPrefs () {
    
    System.out.println("UserPrefs.loadDropboxPrefs");
    userPrefsDropboxName 
        = home.getProgramNameNoSpace() + USER_PREFS_DROPBOX_NAME;
    File userPrefsFile 
        = new File (home.getPrefsFolder(), userPrefsDropboxName);
    if (userPrefsFile.exists()) {
      try {
        FileInputStream userPrefsIn = new FileInputStream (userPrefsFile);
        userPreferences.importPreferences (userPrefsIn);
        log (LogEvent.NORMAL,
            "Loaded user prefs from Dropbox folder",
            false);
      } catch (Exception e) {
        log (LogEvent.MEDIUM,
            "Trouble loading user prefs from Dropbox folder", false);
      }
    } else {
        log (LogEvent.NORMAL,
            "User prefs do not yet exist in Dropbox folder", false);
    }

    systemPrefsDropboxName 
        = home.getProgramNameNoSpace() + SYSTEM_PREFS_DROPBOX_NAME;
    File systemPrefsFile 
        = new File (home.getPrefsFolder(), systemPrefsDropboxName);
    if (systemPrefsFile.exists()) {
      try {
        FileInputStream systemPrefsIn = new FileInputStream (systemPrefsFile);
        systemPreferences.importPreferences (systemPrefsIn);
        log (LogEvent.NORMAL,
            "Loaded system prefs from Dropbox folder",
            false);
      } catch (Exception e) {
        log(LogEvent.MEDIUM,
            "Trouble loading system prefs from Dropbox folder", false);
      }
    } else {
        log (LogEvent.NORMAL,
            "System prefs do not yet exist in Dropbox folder", false);
    }

  }
  
  /**
    If the program was launched from a Dropbox folder, then save the 
    preferences within the program folder. 
   */
  private void saveDropboxPrefs () {
    
    userPrefsDropboxName 
        = home.getProgramNameNoSpace() + USER_PREFS_DROPBOX_NAME;
    try {
      File userPrefsFile = new File (home.getPrefsFolder(), userPrefsDropboxName);
      FileOutputStream userPrefsOut = new FileOutputStream (userPrefsFile);
      userPreferences.exportSubtree (userPrefsOut);
    } catch (Exception e) {
      log(LogEvent.MEDIUM, 
          "Trouble saving user prefs to Dropbox folder", false);
    }

    systemPrefsDropboxName 
        = home.getProgramNameNoSpace() + SYSTEM_PREFS_DROPBOX_NAME;
    try {
      File systemPrefsFile = new File (home.getPrefsFolder(), systemPrefsDropboxName);
      FileOutputStream systemPrefsOut = new FileOutputStream (systemPrefsFile);
      systemPreferences.exportSubtree (systemPrefsOut);
    } catch (Exception e) {
      log(LogEvent.MEDIUM, 
          "Trouble saving system prefs to Dropbox folder", false);
    }
  } // end method
  
  /**
    If old-style parms file is found, convert the properties to the new
    Preferences API.
   */
  public void convertParmsToPrefs () {
    Enumeration props = parmProperties.propertyNames();
    while (props.hasMoreElements()) {
      String key = (String) props.nextElement();
      String data = parmProperties.getProperty (key);
      if (key.endsWith("." + userName)) {
        String newKey = key.substring (0, key.length() - userName.length() - 1);
        userPreferences.put (newKey, data);
        System.out.println ("Converting user prefs key = " 
            + newKey + " data = " + data);
      } else {
        systemPreferences.put (key, data);
        System.out.println ("Converting user prefs key = " 
            + key + " data = " + data);
      } // end if key doesn't end with user name
    } // end while we have more properties
    log (LogEvent.MINOR,
        "Converted parms to prefs",
        false);
    prefsLoaded = PREFS_FROM_PARMS;
    parmsLoaded = PREFS_NOT_AVAILABLE;
  } // end method convertParmsToPrefs
  
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
  
} // end class UserPrefs
