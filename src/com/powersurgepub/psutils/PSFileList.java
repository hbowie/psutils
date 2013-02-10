/*
 * Copyright 2005 - 2013 Herb Bowie
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
  import javax.swing.*;

/**
 *
 * @author hbowie
 */
public class PSFileList {
  
    private String    prefsid;
    private UserPrefs prefs = UserPrefs.getShared();
    private PSFileOpener fileOpener;
    private ArrayList fileList;
    private JMenu     fileMenu;
    private int       max       = 4;
  
  /** Creates a new instance of PSFileList */
  public PSFileList(String menuName, String prefsid, PSFileOpener opener) {
    this.prefsid = prefsid;
    fileList = new ArrayList();
    fileMenu = new JMenu (menuName);
    fileOpener = opener;
    
    long now = new Date().getTime();
    int fileCount = prefs.getPrefAsInt (prefsid + "count", 0);
    if (fileCount > max) {
      max = fileCount;
    }
    for (int i = 0; i < fileCount; i++) {
      String fileName = prefs.getPref (prefsid + String.valueOf (i + 1), "");
      if (fileName.length() > 0) {
        PSFile file = new PSFile (fileName, fileOpener);
        long lastRef = prefs.getPrefAsLong 
            (prefsid + "date" + String.valueOf (i + 1), now);
        file.reference (lastRef);
        reference (file);
      }
    } // end for each pref
    // pruneToMaxSize();
  } // end constructor
  
  public void setMax (int max) {
    if (max >= 0 && max <= 30) {
      this.max = max;
      pruneToMaxSize();
    }
  }
  
  public void close() {
    int fileCount = fileList.size();
    prefs.setPref (prefsid + "count", fileCount);
    for (int i = 0; i < fileCount; i++) {
      PSFile file = (PSFile)fileList.get(i);
      String fileName = file.toString();
      if (fileName.length() > 0) {
        prefs.setPref (prefsid + String.valueOf (i + 1), fileName);
        prefs.setPref (prefsid + "date" + String.valueOf (i + 1), 
            file.getLastReferenceDate().getTime());
      }
    }
  }
  
  public void reference (String pathName) {
    PSFile file = new PSFile (pathName, fileOpener);
    reference (file);
  }
  
  public void reference (File inFile) {
    PSFile file = new PSFile (inFile.getPath(), fileOpener);
    reference (file);
  }
  
  public void reference (PSFile file) {
    int result = 1;
    int i = 0;
    while ((result > 0) && (i < fileList.size())) {
      result = file.compareTo ((PSFile)fileList.get(i));
      if (result > 0) {
        i++;
      }
    }
    if (i >= fileList.size()) {
      fileList.add (file);
      fileMenu.add (file.getMenuItem());
    }
    else
    if (result == 0) {
      PSFile existingFile = (PSFile)fileList.get(i);
      existingFile.reference();
    } else {
      fileList.add (i, file);
      fileMenu.insert (file.getMenuItem(), i);
    }
    
    // If list of recent files is greater than the stipulated maximum,
    // prune the oldest entry
    
    pruneToMaxSize();
    
  } // end reference method
  
  public void pruneToMaxSize () {
    while ((fileList.size() > max) && (max > 0)) {
      pruneOldest();
    }
  }
  
  public void pruneOldest () {
    Date oldestReferenceDate = new Date();
    int oldestEntry = -1;
    for (int j = 0; j < fileList.size(); j++) {
      PSFile jFile = (PSFile)fileList.get(j);
      if (jFile.getLastReferenceDate().before (oldestReferenceDate)) {
        oldestReferenceDate = jFile.getLastReferenceDate();
        oldestEntry = j;
      } // end if older
    } // end for each file in list
    if (oldestEntry >= 0) {
      fileList.remove (oldestEntry);
      fileMenu.remove (oldestEntry);
    }    
  }
  
  public JMenu getFileMenu () {
    return fileMenu;
  }
  
}
