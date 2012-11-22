package com.powersurgepub.psutils;



  import java.lang.Object;

  import java.lang.String; 

             

/**

   An event to be logged. 

   

   This code is copyright (c) 1999-2000 by Herb Bowie of PowerSurge Publishing. 

   All rights reserved. <p>

   

   Version History: <ul><li>

     00/04/09 - Modified to be consistent with "The Elements of Java Style". </ul>

  

   @see com.powersurgepub.psutils.LogData

   @see com.powersurgepub.psutils.LogOutput

   @see com.powersurgepub.psutils.LogOutputNone

   @see com.powersurgepub.psutils.LogOutputDisk

   @see com.powersurgepub.psutils.Logger

   @see com.powersurgepub.psutils.LogJuggler

  

   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">

           herb@powersurgepub.com</a>)<br>

           of PowerSurge Publishing (<A href="http://www.powersurgepub.com/software/">

           www.powersurgepub.com/software</a>)

  

   @version 00/04/18 - Added flag to indicate whether event is data-related.

 */



public class LogEvent {

  

  /** Indicates an event that is expected to happen normally. */

  public static final int    NORMAL   = 0;

  

  /** Indicates an abnormal event of little consequence. */

  public static final int    MINOR    = 1;

  

  /** Indicates an unusual event of some consequence. */

  public static final int    MEDIUM   = 2;

  

  /** Indicates an alarming event with serious consequences. */

  public static final int    MAJOR    = 3;

  

  /** The lowest allowable severity code. */

  public static final int    LOW_SEVERITY   = NORMAL;

  

  /** The highest allowable severity code. */

  public static final int    HIGH_SEVERITY  = MAJOR;

  

  /** Default is to tie event to preceding data. */

  public static final boolean DATA_RELATED_DEFAULT = true;

  

  /** A description of the event that has taken place. */

  private  String        message;

  

  /** An indication of the seriousness of the event being logged. */

  private  int           severity;

  

  /** Is this event data-related? */

  private  boolean       dataRelated;



  /**

     The "noarg" LogEvent constructor. The resulting event is incomplete,

     and will need to have meaningful values set before it can be logged.

   */



  public LogEvent () {

    this (0, "", DATA_RELATED_DEFAULT);

  }

  

  /**

     The LogEvent constructor with two arguments passed.

    

     @param severity      the severity of the event

    

     @param message       the message to be written to the log

   */

  public LogEvent (int severity, String message) {

    this (severity, message, DATA_RELATED_DEFAULT);

  } // end LogEvent constructor

  

  /**

     The LogEvent constructor with all arguments passed.

    

     @param severity      the severity of the event

    

     @param message       the message to be written to the log

    

     @param dataRelated   indicates whether this event is related

                          to preceding data.

   */

  public LogEvent (int severity, String message, boolean dataRelated) {



    setSeverity (severity);

    setMessage (message);

    setDataRelated (dataRelated);

  } // end LogEvent constructor

  

  /**

     Returns current severity code.

    

     @return severity code

   */

  public int getSeverity() { return severity; }

  

  /**

     Returns current event description.

    

     @return description of event

   */

  public String getMessage() { return message; }

  

  /**

     Returns the flag indicating whether event is related to data.

    

     @return true if event is tied to preceding data.

   */

  public boolean isDataRelated() { return dataRelated; }

  

  /** 

     Sets the severity code. 

    

     @param severity code to be set

   */

  public void setSeverity (int severity) {

    this.severity = severity;

  }

  

  /** 

     Sets the event message.

    

     @param message description of the event.

   */

  public void setMessage (String message) {

    this.message = message;

  }

  

  /**

     Sets the data relationship flag.

    

     @param dataRelated True if event is related to preceding data.

   */

  public void setDataRelated (boolean dataRelated) {

    this.dataRelated = dataRelated;

  }

  

  /**

     Returns the object as a string.

    

     @return string "S" followed by severity code plus the message.

   */ 

  public String toString () {

    return "S" + String.valueOf (severity)

      + " " + message;

  } // end toString method



} // end LogData class

