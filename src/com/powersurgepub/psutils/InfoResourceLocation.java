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
