package com.powersurgepub.psutils;

  import com.powersurgepub.psutils.LogData;
  import com.powersurgepub.psutils.LogEvent;
  import com.powersurgepub.psutils.Logger;
  import com.powersurgepub.psutils.LogOutput;
  import com.powersurgepub.psutils.LogOutputDisk;
  import com.powersurgepub.psutils.LogOutputNone;
	import com.powersurgepub.psutils.LogOutputText;
  import java.lang.Object;
  import java.lang.String;
  import javax.swing.*;

   

/**

   A complete set of all the possible logging destinations.  

   Allows a client program to easily switch between the various
   logging destinations. <p>

   This code is copyright (c) 1999-2002 by Herb Bowie of PowerSurge Publishing. 

   All rights reserved. <p>

   Version History: <ul><li>
    2002/04/10 - Added log output text option. <li>
    2000/04/13 - Modified to be consistent with "The Elements of Java Style".
     </ul>

   @see com.powersurgepub.psutils.LogOutput 
   @see com.powersurgepub.psutils.LogOutputNone
   @see com.powersurgepub.psutils.LogOutputDisk
   @see com.powersurgepub.psutils.LogOutputText
   @see com.powersurgepub.psutils.LogData
   @see com.powersurgepub.psutils.LogEvent
   @see com.powersurgepub.psutils.Logger
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing (<A href="http://www.powersurgepub.com/software/">
           www.powersurgepub.com/software</a>)

   @version 2004/07/09 - Modified to use shared log.
 */

public class LogJuggler {

  /** Requests null output when passed to setLog. */
  public    static final String LOG_NONE_STRING     = "None";

  /** Requests System.out output when passed to setLog. */
  public    static final String LOG_WINDOW_STRING   = "Window";

  /** Requests disk file output when passed to setLog. */
  public    static final String LOG_DISK_STRING     = "Disk"; 

  /** Requests JTextArea output when passed to setLog. */
  public    static final String LOG_TEXT_STRING   	= "Text";
  
  /** Requests System.out output when passed to setLog. */
  public    static final String LOG_SYSOUT_STRING   	= "SysOut";

  /** Default program ID to pass to the logging objects. */
  private    String    programID = "LogJuggler";

  /** The current logging destination. */
  private LogOutput    log;

  /** A logging destination of a window. */
  private LogOutput      windowLog;

  /** A null logging destination. */
  private LogOutputNone  noLog;
  
  /** A logging destination of a disk file. */
  private LogOutputDisk  diskLog;

  /** A logging destination of a text area. */
  private LogOutputText  textLog;
  
  /** A logging destination of System.out. */
  private LogOutput      sysLog;

  /**
     The "noarg" constructor sets default values for all
     the necessary logging objects. The default logging destination is
     the standard System.out destination.
   */

  public LogJuggler () {
    // noLog = new LogOutputNone();
    // log = noLog;
    // setLog (LOG_WINDOW_STRING);
    setLog (Logger.getShared().getLog());
  } // end LogJuggler constructor

  /**
     This constructor accepts a program ID, to be printed
     in the log.

     @param programID the name of the client program.
   */

  public LogJuggler (String programID) {
    this();
    this.programID = programID;
  } 

  /**
	   Sets the text area available for output.

	   @param textArea text area to be used as output.
	 */

	public void setTextArea (JTextArea textArea) {

		if (textLog == null) {
			textLog = new LogOutputText ();
		}
		textLog.setTextArea (textArea);
	}

  /** 
     Returns the current logging destination. 

     @return Current logging destination.
   */
  public LogOutput getLog ()     { return log; }

  /** 
     Returns the current logging object. 

     @return Current logging object.
   */
  public Logger   getLogger ()  { return Logger.getShared(); }

  /**
     Sets the logging destination based on the String
     literal passed.

     @param logString One of the following values: <ul><li>
                      LOG_NONE_STRING for null output <li>
                      LOG_DISK_STRING for disk output <li>
                      LOG_WINDOW_STRING for output to System.out <li>
	                    LOG_TEXT_STRING for output to a text area. <li>

                      All other values will also send output to System.out </ul>
   */
  public void setLog (String logString) {
    if (logString.equals (LOG_NONE_STRING)) {
      if (noLog == null) {
        noLog = new LogOutputNone();
      }
      log = noLog;
    } else
    if (logString.equals (LOG_DISK_STRING)) {
      if (diskLog == null) {
        diskLog = new LogOutputDisk (programID);
      }
      log = diskLog;
    } else 
		if (logString.equals (LOG_TEXT_STRING)) {
      if (textLog == null) {
        textLog = new LogOutputText ();
      }
      log = textLog;
    } else
    if (logString.equals (LOG_SYSOUT_STRING)) {
      if (sysLog == null) {
        sysLog = new LogOutput ();
      }
      log = sysLog;
    } else {
      if (windowLog == null) {
        windowLog = new LogOutput ();
      }
      log = windowLog;
		} 

    Logger.getShared().setLog (log);
  }

  /**
     Sets the logging destination to the passed value.

     @param log A valid logging output destination.
   */
  public void setLog (LogOutput log) {
    this.log = log;
    Logger.getShared().setLog (log);
  }

  /**
     Closes up logging operations, including all logs used.
   */

  public void close () {
    if (noLog != null) {
      noLog.close();
    }

    if (diskLog != null) {
      diskLog.close();
    }

    if (windowLog != null) {
      windowLog.close();
    }

		if (textLog != null) {
			textLog.close();
		}

  }

  /**
     Returns printable representation of the object.

     @return A literal of "LogJuggler" plus the object ID.
   */
  public String toString () {
    return "LogJuggler " + super.toString();
  } // end toString method

} // end Logger class

