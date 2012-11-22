package com.powersurgepub.psutils;

  import com.powersurgepub.psutils.LogData;
  import com.powersurgepub.psutils.LogEvent;
  import com.powersurgepub.psutils.LogOutput;
  import com.powersurgepub.psutils.StringUtils;
  import java.lang.Object;
  import java.lang.String;
  import java.lang.StringBuffer; 
  import java.util.*;
             
/**
   A mechanism for logging the results  
   of another program's processing. The primary purpose for 
   a log would be to track the significant actions of a batch
   (non-interactive) process, so that the user can view the log
   after the batch processing has completed. <p>
  
   This class should be passed events and data. Significant events will
   be written to the log. Data will be written when it is related to an event
   (or optionally all data can be written). <p>
    
   This code is copyright (c) 1999-2000 by Herb Bowie of PowerSurge Publishing. 
   All rights reserved. <p>
   
   Version History: <ul><li>
     2004/06/01 - Added methods to get logThreshold and get logAllData. <li>
     2000/04/18 - Added check for event's data relationship. <li>
     2000/04/12 - Modified to be consistent with "The Elements of Java Style". </ul>
  
   @see com.powersurgepub.psutils.LogOutput 
   @see com.powersurgepub.psutils.LogOutputNone
   @see com.powersurgepub.psutils.LogOutputDisk
   @see com.powersurgepub.psutils.LogData
   @see com.powersurgepub.psutils.LogEvent
   @see com.powersurgepub.psutils.LogJuggler
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing (<A href="http://www.powersurgepub.com/software/">
           www.powersurgepub.com/software</a>)
  
   @version 
   	2004/07/11 - Added getShared singleton method allow users to get a single,
                 shared instance of the Logger class.
 */

public class Logger {
  
  /** Single shared occurrence of Logger. */
  private static       Logger     sharedLogger;
  
  /** Hold area for log messages received before any log output is available. */
  private              ArrayList  hold = new ArrayList();
  
  /** 
     The actual output destination for the log records. Note that this
     could also be a sub-class of LogOutput.
   */
  private LogOutput    log;
  
  /**
     Events with severities greater than or equal to this value will 
     be logged. Events with severities less than this value
     will be ignored. The default is NORMAL, causing all events
     to be logged.
   */
  private int          logThreshold = LogEvent.NORMAL;
  
  /**
     Events with severities greater than or equal to this value will 
     cause the failure flag to be set. The default is for only MAJOR
     events to cause a failure.
   */
  private int          failureThreshold = LogEvent.MAJOR;

  /**
     Should all data be logged? If not, then only data immediately 
     preceding logged events will be logged.
   */
  private boolean      logAllData = true;
  
  /** 
     Has an event occurred whose significance has passed the failure 
     threshold?
   */
  private boolean      failure = false;
  
  /** The last data line passed. */
  private  LogData     lastData;
  
  /** 
     Has last data already been written to the log?
     (If so, then don't write it twice.)
   */
  private boolean      dataLogged = true;
  
  /** 
    Returns a single instance of Logger that can be shared by many classes. This
    is not the only way to obtain an instance of Logger, since this method was
    introduced long after many other programs were instantiating their own
    instances of Logger.
   
    @return A single, shared instance of Logger.
   */  
  public static Logger getShared() {
    if (sharedLogger == null) {
      sharedLogger = new Logger();
    }
    return sharedLogger;
  }
  
  /**
    Set a different logger to be shared by all classes.
   
    @param newLogger Logger to be shared.
   */
  public static void setShared (Logger newLogger) {
    sharedLogger = newLogger;
  }
  
  /**
     The getShared signature to use when providing LogOutput
     as a parm.
    
     @param log a destination for the log file.
   */
  public static Logger getShared (LogOutput log) {
    if (sharedLogger == null) {
      sharedLogger = new Logger(log);
    }
    return sharedLogger;
  } // end method

  /**
     The "noarg" constructor. A default LogOutput destination
     will be used.
   */
  public Logger () {
    // this.log = new LogOutput();
  } // end Logger constructor

  /**
     The constructor to use when providing LogOutput
     as a parm.
    
     @param log a destination for the log file.
   */
  public Logger (LogOutput log) {
    this.log = log;
  } // end Logger constructor
  
  /**
     Accepts lines of data being processed. Every line 
     processed should be passed. The data may or may not be written
     to the log, depending on the options settings.
    
     @param data a line of data being processed by the caller.
   */  
  public void nextLine (LogData data) {
    lastData = data;
    dataLogged = false;
    if (logAllData) {
      writeData();
    } // end if dataLogged
  } // end nextLine method
  
  public synchronized static void sharedRecordEvent
      (int severity, String message, boolean dataRelated) {
    LogEvent event = new LogEvent(severity, message, dataRelated);
    Logger.getShared().recordEvent (event);
  }
  
  /**
     Creates a LogEvent object and then records it.
    
     @param severity      the severity of the event
    
     @param message       the message to be written to the log
    
     @param dataRelated   indicates whether this event is related
                          to preceding data.
   */
  public synchronized void recordEvent (int severity, String message, boolean dataRelated) {
    LogEvent event = new LogEvent(severity, message, dataRelated);
    recordEvent (event);
  }
  
  /**
     Accepts events that have occurred. An event is
     something of potential interest which may be written 
     to the log file.
     
     @param event Something of interest that happened while processing
     data.
   */
  public synchronized void recordEvent (LogEvent event) {
    int severity = event.getSeverity();
    if (severity >= logThreshold) {
      if ((! dataLogged) && (event.isDataRelated())) {
        writeData();
      } // end of data logging
      String suffix;
      if (severity == LogEvent.NORMAL) {
        suffix = ".";
      } else {
        StringBuffer work = new StringBuffer();
        for (int i = LogEvent.NORMAL; i < severity; i++) {
          work.append ("!");
        }
        suffix = work.toString();
      }
      writeLine (
        // "S" +
        // StringUtils.stringFromInt (severity, 1) +
        // " " + 
        event.getMessage() + suffix);
      if (severity >= failureThreshold) {
        failure = true;
        writeLine ("Fatal Error!!!");
        close();
      }
    } // end if event is logged
  } // end nextEvent method
  
  /**
     Writes the last data line to the LogOutput destination.
   */
  private void writeData() {
    writeLine (lastData.toString());
    dataLogged = true;
  } // end writeData method
  
  private void writeLine (String line) {
    if (log == null) {
      hold.add (line);
    } else {
      log.writeLine (line);
    }
  }
  
  /**
     Closes the LogOutput destination.
   */
  public void close() {
    if (log != null) {
      log.close();
    }
  }
  
  /**
     Returns the LogOutput destination being used.
    
     @return The LogOutput destination being used.
   */
  public LogOutput getLog ()     { return log; }
  
  /**
     Has a failure occurred?
    
     @return failure flag
   */
  public boolean isFailure ()   { return failure; }
  
  /**
     Change the log destination being used.
    
     @param A new LogOutput destination.
   */
  public void setLog (LogOutput log) {
    this.log = log;
    if ((this.log != null) && (hold.size() > 0)) {
      for (int i = 0; i < hold.size(); i++) {
        String line = (String)hold.get (i);
        this.log.writeLine (line);
      }
    }
  }
  
  /**
    Gets the logging threshold.
   
    @return logging threshold.
   */
  public int getLogThreshold () {
    return logThreshold;
  }
  
  /**
     Changes the threshold value at or above which events
     are considered worth seeing on the log.
    
     @param logThreshold A new logging threshold.
   */
  public void setLogThreshold (int logThreshold) {
    this.logThreshold = logThreshold;
  }

  /**
     Changes the threshold value for declaring that a failure
     has occurred.
    
     @param failureThreshold A new failure threshold.
   */
  public void setFailureThreshold (int failureThreshold) {
    this.failureThreshold = failureThreshold;
  }
  
  /**
    Retrieves the flag indicating whether all data processed should be
    written to the log.
    
    @return Should all data be logged?
   */
  public boolean getLogAllData () {
    return logAllData;
  }

  /**
     Changes the flag determining whether all data
     (or only data immediately preceding a significant
     event) should be logged.
    
     @param logAllData Should all data be logged?
   */
  public void setLogAllData (boolean logAllData) {
    this.logAllData = logAllData;
  }
  
  /** 
     Print the "Logger " literal, plus the LogOutput string value, plus
     the logging threshold.
   */
  public String toString () {
    return "Logger " + log.toString() + " threshold=" + logThreshold;
  } // end toString method

} // end Logger class
