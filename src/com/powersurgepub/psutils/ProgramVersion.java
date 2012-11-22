package com.powersurgepub.psutils;

  import com.powersurgepub.xos2.*;
  import java.util.*;
  import javax.swing.*;
  import org.xml.sax.*;
  import org.xml.sax.helpers.*;

/**
 *
 * @author hbowie
 */
public class ProgramVersion 
    extends DefaultHandler {
  
  public static final String      XML_DIZ_INFO    = "XML_DIZ_INFO";
  public static final String      PROGRAM_VERSION = "Program_Version";
  
  private static ProgramVersion   pv = null;
  
  private     Home                home = Home.getShared();
  private     XOS                 xos  = XOS.getShared();
  
  /** Log used to record events. */
  private     Logger              log = Logger.getShared();
  
  private     XMLReader           parser;
  
  // Array of character strings being built as characters are received from parser
  private     ArrayList           chars = new ArrayList();
  
  private     int                 elementLevel = -1;
  
  private     String              programVersion = "";
  private     String              latestVersion = "";
  private     boolean             newerVersionAvailable   = false;
  
  private     JFrame              frame;
  
  /** 
    Returns a single instance of ProgramVersion that can be shared by many classes. This
    is the only way to obtain an instance of ProgramVersion, since the constructor is
    private.
   
    @return A single, shared instance of ProgramVersion.
   
    @param programName Name of the current program (or enough of the name that 
                       we can use it to find the program's folder.
   */  
  public static ProgramVersion getShared (JFrame frame) {
    if (pv == null) {
      pv = new ProgramVersion (frame);
    }
    return pv;
  }
  
  public static ProgramVersion getShared () {
    if (pv == null) {
      pv = new ProgramVersion();
    }
    return pv;
  }
  
  /** Creates a new instance of ProgramVersion */
  private ProgramVersion(JFrame frame) {
    this.frame = frame;
    readPadFile();
    programVersion = home.getProgramVersion();
    newerVersionAvailable 
        = (latestVersion.compareTo (programVersion) > 0);
  }
  
  private ProgramVersion() {
    this.frame = null;
    readPadFile();
    programVersion = home.getProgramVersion();
    newerVersionAvailable 
        = (latestVersion.compareTo (programVersion) > 0);
  }
  
  private void readPadFile () {
    boolean ok = createParser();
    if (ok) {
      chars = new ArrayList();
      elementLevel = 0;
      String programName = home.getProgramNameNoSpace();
      String inputName = "http://www.powersurgepub.com/padinfo/" 
            + programName
            + ".xml";
      try {
        parser.parse (inputName);
      } 
      catch (SAXException saxe) {
          log.recordEvent (LogEvent.MEDIUM, 
              "Encountered SAX error while reading XML file " + inputName 
              + " " + saxe.toString(),
              false);   
      } 
      catch (java.io.IOException ioe) {
          log.recordEvent (LogEvent.MEDIUM, 
              "Could not verify program version using PAD file at " + inputName,
              false);   
      } // end catch
    } // end if ok
  } // end method readPadFile
  
  /**
   Create XML Parser.
   */
  private boolean createParser () {
    
    boolean ok = true;
    try {
      parser = XMLReaderFactory.createXMLReader();
    } catch (SAXException e) {
      log.recordEvent (LogEvent.MINOR, 
          "Generic SAX Parser Not Found",
          false);
      try {
        parser = XMLReaderFactory.createXMLReader
            ("org.apache.xerces.parsers.SAXParser");
      } catch (SAXException eex) {
        log.recordEvent (LogEvent.MEDIUM, 
            "Xerces SAX Parser Not Found",
            false);
        ok = false;
      } // end catch specific sax parser not found
    } // end catch generic sax parser exception
    if (ok) {
      parser.setContentHandler (this);
    }
    return ok;
  } // end method createParser
  
  public void startElement (
      String namespaceURI,
      String localName,
      String qualifiedName,
      Attributes attributes) {
    
    if (localName.equals (XML_DIZ_INFO)) {
      elementLevel = 0;
      StringBuffer str = new StringBuffer();
      storeField (elementLevel, str);
    }
    else
    if (localName.equals (PROGRAM_VERSION)) {
      elementLevel = 0;
      StringBuffer str = new StringBuffer();
      storeField (elementLevel, str);
    }
  } // end method
  
  /*
  private void harvestAttributes (Attributes attributes, DataField field) {
    for (int i = 0; i < attributes.getLength(); i++) {
      String name = attributes.getLocalName (i);
      DataFieldDefinition def = dict.getDef (name);
      if (def == null) {
        def = new DataFieldDefinition (name);
        dict.putDef (def);
      }
      System.out.println ("  Attribute " + name + " = " + attributes.getValue (i));
      DataField attr = new DataField (def, attributes.getValue (i));
      field.addField (attr);
    }
  }
   */
  
  public void characters (char [] ch, int start, int length) {
    StringBuffer xmlchars = new StringBuffer();
    xmlchars.append (ch, start, length);
    if (elementLevel >= 0 
        && elementLevel < chars.size()) {
      StringBuffer str = (StringBuffer)chars.get (elementLevel);
      boolean lastCharWhiteSpace = false;
      if (str.length() < 1
          || Character.isWhitespace (str.charAt (str.length() - 1))) {
        lastCharWhiteSpace = true;
      }
      char c;
      boolean charWhiteSpace;
      for (int i = start; i < start + length; i++) {
        c = ch [i];
        charWhiteSpace = Character.isWhitespace (c);
        if (charWhiteSpace) {
          if (lastCharWhiteSpace) {
            // do nothing
          } else {
            lastCharWhiteSpace = true;
            str.append (" ");
          }
        } else {
          str.append (c);
          lastCharWhiteSpace = false;
        }
      } // end for each passed character
      // str.append (ch, start, length);
    } // end if we are at a valid element level
  } // end method characters
  
  public void ignorableWhitespace (char [] ch, int start, int length) {
    
  }
  
  public void endElement (
      String namespaceURI,
      String localName,
      String qualifiedName) {
    
    StringBuffer str;
    if (elementLevel >= 0) {
      if (chars.size() > elementLevel) {
        str = (StringBuffer)chars.get (elementLevel);
      } else {
        str = new StringBuffer();
      }
      if (str.length() > 0
          && str.charAt (str.length() - 1) == ' ') {
        str.deleteCharAt (str.length() - 1);
      }
      if (localName.equals (PROGRAM_VERSION)) {
        // StringBuffer str = new StringBuffer();
        // storeField (elementLevel, str);
        latestVersion = str.toString();
        elementLevel--;
      } 
      elementLevel--;
    } // end if we are within a wisdom element
  } // end method
  
  private void storeField (int level, StringBuffer str) {
    if (chars.size() > level) {
      chars.set (level, str);
    } else {
      chars.add (level, str);
    }
  } // end method
  
  public String getLatestVersion () {
    return latestVersion;
  }
  
  public boolean isNewerVersionAvailable () {
    return newerVersionAvailable;
  }
  
  public void informUserIfNewer () {

    if (latestVersion.equals ("")
        || latestVersion.equals ("0.00")) {
      /* JOptionPane.showMessageDialog(frame,
          "Latest Program Version could not be accessed",
          "Connection Error",
          JOptionPane.WARNING_MESSAGE); */
    } 
    else
    if (newerVersionAvailable) {
      int response = JOptionPane.showConfirmDialog
          (frame, 
          "A newer version is available. Would you like to check it out?", 
          "New Version", 
          JOptionPane.OK_CANCEL_OPTION, 
          JOptionPane.INFORMATION_MESSAGE); 
      if (response == JOptionPane.OK_OPTION) {
        openURL ("http://www.powersurgepub.com/products/"
            + home.getProgramNameLower()
            + "/versions.html");
      }
    } 
  }
  
  public void informUserIfLatest () {
    if (! newerVersionAvailable) {
      JOptionPane.showMessageDialog(frame,
          "Program Version is up-to-date",
          "All is cool",
          JOptionPane.INFORMATION_MESSAGE);
    }
  }
  
  public void openURL (String url) {
    try {
      xos.openURL (StringUtils.cleanURLString(url));
    } catch (java.io.IOException e) {
      // ???
    }
  }
  
}
