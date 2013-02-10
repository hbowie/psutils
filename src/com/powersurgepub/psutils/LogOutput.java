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

/**

   A destination for log records. This class writes log lines 
   to System.out. It is designed to be overridden by other subclasses
   that write log records to other destinations.
 */

public class LogOutput {

  /** Indicates whether the log file is open. */
  private boolean    logOpen  = false;

  /** Indicates whether the log file is OK */
  private boolean    logOk    = true;

  /**

     The "noarg" constructor. This produces a fully functioning object. 

   */

  public LogOutput () {
      // No initialization needed.
  } // end LogOutput constructor

  /**
     Writes a line of output to the log file (in this case, System.out).
     If the log file is not yet open, then it will be opened automatically
     on the first write.

     @param line  line of data to be written to the log file.
   */

  public void writeLine (String line) {
    if (! logOpen) {
      open ();
    }
    System.out.println (line);
  } // end writeLine method

  /**
     Opens the output file, if necessary.
   */

  public void open () {
    if (isLogOk()) {
      setLogOpen (true);
    }
  } // end open method

  /**
     Closes the log file.
   */

  public void close () {
    setLogOpen (false);
  } // end close method

  /**
     Is log file open?
    
     @return true if log file is open
   */

  public boolean isLogOpen ()  { return logOpen; }
  
  /**
     Has any sort of disastrous error occurred?
    
     @return true if log file is OK
   */

  public boolean isLogOk ()     { return logOk; }

  /**
     Indicates whether the output log file is open or closed.

     @param logOpen the value to be set.
   */

  public void setLogOpen (boolean logOpen) {
    this.logOpen = logOpen;
  }

  /**
     Indicates whether the output log file is OK or not.

     @param logOK the value to be set.
   */

  public void setLogOk (boolean logOk) {
    this.logOk = logOk;
  }

  /**
     Returns the object as a String, with a standard identifier.
    
     @return the text "LogOutput to System.out".

   */

  public String toString () {
    return "LogOutput to System.out";
  } // end toString method

} // end LogOutput class

