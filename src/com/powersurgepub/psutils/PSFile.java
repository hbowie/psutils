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
public class PSFile 
  extends File {
  
  private Date lastReferenceDate = new Date();
  private JMenuItem fileMenuItem;
  private PSFileOpener fileOpener;
  private PSFile file;
  
  
  /** Creates a new instance of PSFile */
  public PSFile(String pathname, PSFileOpener opener) {
    super (pathname);
    fileOpener = opener;
    file = this;
    fileMenuItem = new JMenuItem(getName() + " - " + toString());
    fileMenuItem.setActionCommand (getPath());
    fileMenuItem.setToolTipText (getPath());
    fileMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        fileOpener.handleOpenFile (file);
      }
    });
  }
  
  public void reference (Date date) {
    lastReferenceDate = date;
  }
  
  public void reference(long date) {
    lastReferenceDate.setTime (date);
  }
  
  public void reference () {
    lastReferenceDate = new Date();
  }
  
  public Date getLastReferenceDate() {
    return lastReferenceDate;
  }
  
  public JMenuItem getMenuItem () {
    return fileMenuItem;
  }
  
  
}
