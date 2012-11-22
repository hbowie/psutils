package com.powersurgepub.psutils;

/**

   A destination for log records. This class writes log lines 
   to System.out. It is designed to be overridden by other subclasses
   that write log records to other destinations.<p>
   
   This code is copyright (c) 1999-2000 by Herb Bowie of PowerSurge Publishing. 

   All rights reserved. <p>

   Version History: <ul><li>
     </ul>

   @see com.powersurgepub.psutils.LogOutputNone

   @see com.powersurgepub.psutils.LogOutputDisk 

   @see com.powersurgepub.psutils.LogData

   @see com.powersurgepub.psutils.LogEvent

   @see com.powersurgepub.psutils.Logger

   @see com.powersurgepub.psutils.LogJuggler

  

   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">

           herb@powersurgepub.com</a>)<br>

           of PowerSurge Publishing (<A href="http://www.powersurgepub.com/software/">

           www.powersurgepub.com/software</a>)

  

   @version 00/04/10 - Modified to be consistent with "The Elements of Java Style".

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

