/*
 * XMLFileOut.java
 *
 * Created on March 13, 2005, 1:10 PM
 */

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
  import java.awt.*;
  import java.io.*;

/**
 *
 * @author b286172
 */
public class XMLFileOut {
  
  private Trouble trouble = Trouble.getShared();
  private XTextFile xmlFile;
  private StringBuffer textOut = new StringBuffer();
  String docType = "";
  private int level = 0;
  
  /** Creates a new instance of XMLFileOut */
  public XMLFileOut() {
  }
  
  public void open (String fileDesc, File baseDir, Frame parent, String docType, String dtd) {
    XFileChooser chooser = new XFileChooser (); 
    chooser.setFileSelectionMode(XFileChooser.FILES_ONLY);
    if (fileDesc != null
        && fileDesc.length() > 0) {
      chooser.setDialogTitle ("Save " + fileDesc);
    }
    if (baseDir != null
        && baseDir.exists()) {
      chooser.setCurrentDirectory (baseDir);
    }
    File outXMLFile = chooser.showSaveDialog (parent); 
    if (outXMLFile != null) {  
      open (outXMLFile, docType, dtd);
    }
  }
  
  public void open (File outFile, String docType, String dtd) {
    this.docType = docType;
    textOut = new StringBuffer();
    level = 0;
    if (outFile != null) { 
      try {
        xmlFile = new XTextFile (outFile);
        xmlFile.openForOutput();
        writeLine ("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        append ("<!DOCTYPE ");
        append (docType);
        if (dtd != null
            && dtd.length() > 0) {
          append (" SYSTEM '");
          append (dtd);
          append ("'");
        }
        append (">");
        writeLine ();
        startTag (docType);
      } catch (IOException e) {
        trouble.report ("Trouble opening " + outFile.toString(), "I/O Exception");
      }
    } 
  } // end method open
  
  public void close () {
    try {
      endTag (docType);
      xmlFile.close();
    } catch (IOException e) {
      trouble.report ("Trouble closing " + xmlFile.toString(), "I/O Exception");
    }
  }
  
  public void writeTagWithText (String tag, String text) {
    appendSpaces (level * 2);
    append ("<");
    append (tag);
    append (">");
    append (text);
    append ("<");
    append ("/");
    append (tag);
    append (">");
    writeLine();
  }
  
  public void startTag (String tag) {
    writeTagged (level * 2, tag);
    level++;
  }
  
  public void writeText (String text) {
    appendSpaces (level * 2);
    append (text);
    writeLine();
  }
  
  public void endTag (String tag) {
    level--;
    writeTagged (level * 2, "/" + tag);
  }
  
  public void writeTagged (int indent, String line) {
    appendSpaces (indent);
    append ("<");
    append (line);
    append (">");
    writeLine();
  }
  
  /**
    Write the requested number of spaces.
   */
  public void appendSpaces (int spaces) {
    for (int i = 0; i < spaces; i++) {
      append (" ");
    }
  }
  
  public void append (String line) {
    textOut.append (line);
  }
  
  /**
    Actually write the line that has been built in the buffer. 
   */
  public void writeLine() {
    try {
      xmlFile.writeLine (textOut.toString());
    } catch (IOException e) {
      trouble.report ("Trouble writing to " + xmlFile.toString(), "I/O Exception");
    }
    textOut.setLength(0);
  }
  
  public void writeLine(String line) {
    try {
      xmlFile.writeLine (line);
    } catch (IOException e) {
      trouble.report ("Trouble writing to " + xmlFile.toString(), "I/O Exception");
    }
  }
  
  public String toString () {
    return xmlFile.toString();
  }
  
  public File getParentFile() {
    return xmlFile.getParentFile();
  }
  
  public String getName() {
    return xmlFile.getName();
  }
}
