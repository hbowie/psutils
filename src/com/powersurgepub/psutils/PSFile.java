/*
 * PSFile.java
 *
 * Created on April 27, 2005, 7:19 PM
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
