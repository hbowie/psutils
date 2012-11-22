/*
 * FileOpener.java
 *
 * Created on May 28, 2007, 6:27 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.powersurgepub.psutils;

  import java.io.*;

/**
  Interface for a program capable of opening a file.
 
 */
public interface FileOpener {
  
  /**      
    Standard way to respond to a document being passed to this application on a Mac.
   
    @param inFile File to be processed by this application, generally
                  as a result of a file or directory being dragged
                  onto the application icon.
   */
  public void handleOpenFile (File inFile);
  
}
