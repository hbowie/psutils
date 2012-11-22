package com.powersurgepub.psutils;

  import java.io.*;
  import java.net.*;

/**
  This class represents a location for an information resource that 
  may be a file or a URL. 
 */
public class InfoResourceLocation {
  
  private               URL                 url = null;
  private               File                file = null;
  
  public InfoResourceLocation (String str) {
    setURLFromString (str);
    if (url == null) {
      setFileFromString (str);
    }
  }
  
  public InfoResourceLocation (File file) {
    setFile (file);
  }
  
  public void setURLFromString (String str) {
    try {
      url = new URL (str);
    } catch (MalformedURLException e) {
      url = null;
    }
  }
  
  public void setFileFromString (String str) {
    File strFile = new File (str);
    setFile (strFile);
  }
  
  public void setFile (File file) {
    this.file = file;
    URI uri = file.toURI();
    try {
      url = uri.toURL();
    } catch (MalformedURLException e) {
      url = null;
    }
  }
  
  public File getFile () {
    return file;
  }
  
  public URL getURL () {
    return url;
  }
  
  public boolean isValidURL () {
    return url != null;
  }
  
  public String toString() {
    if (isValidURL()) {
      return url.toString();
    }
    else
    if (file != null) {
      return file.toString();
    } else {
      return "new";
    }
  }

}
