package com.powersurgepub.psutils;



  import java.io.BufferedWriter;

  import java.io.File;

  import java.io.FileNotFoundException;

  import java.io.FileWriter;

  import java.io.IOException;

  import java.lang.Exception;

  import java.lang.Object;

  import java.lang.String;

  import java.lang.System;

  import java.util.Properties;

             

/**

   A log destination that writes log records 

   to a disk file.<p>

   

   This code is copyright (c) 1999-2000 by Herb Bowie of PowerSurge Publishing. 

   All rights reserved. <p>

   

   Version History: <ul><li>

     </ul>

  

   @see com.powersurgepub.psutils.LogOutput 

   @see com.powersurgepub.psutils.LogOutputNone

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

public class LogOutputDisk extends LogOutput {



  public    static final String   USER_DIR = "user.dir";

  

  /** Default disk file name if another is not specified. */

  public    static final String   DEFAULT_FILE_NAME = "log.txt";

  

  /** String that will be appended to a program ID that is passed. */

  public    static final String   FILE_SUFFIX = "_log.txt";

  

  private    Properties     systemProperties;

  private    String         userDirString;

  private    File           userDirFile;

  private    String         logFileName;

  private    File           logFile;

  private    FileWriter     logFileWriter;

  private    BufferedWriter logFileBufWriter;

  private    int            logFileLineNumber;

  private    String         logFileLine;

  private    int            logFileLineLength;



  /**

     The "noarg" constructor. This produces a fully functioning object

     with a default file name. 

   */

  public LogOutputDisk () {

    logFileName = DEFAULT_FILE_NAME;

  } // end LogOutputDisk "noarg" constructor

  

/**

   The constructor for a specific program ID. The file name

   will be constructed by appending the default file suffix

   to the passed program ID.

  

   @param programID the name of the program being executed. 

*/

  public LogOutputDisk (String programID) {

    logFileName = programID + FILE_SUFFIX;

  } // end LogOutputDisk constructor

  

  /**

     Writes a line of output to the log disk file.

     If the log file is not yet open, then it will be opened automatically

     on the first write.

    

     @param line  line of data to be written to the log file.

   */  

  public void writeLine (String line) {

    if ((isLogOk()) && (! isLogOpen())) {

      open ();

    }

    if (isLogOk()) {

      try { 

        logFileBufWriter.write(line, 0, line.length());

        logFileBufWriter.newLine();

      } catch (IOException e) {

        System.err.println (this.toString() + " suffered an I/O Exception on write.");

        setLogOk (false);

      }

    }

  } // end writeLine method

  

  public void open () {

    systemProperties = System.getProperties();

    userDirString = systemProperties.getProperty (USER_DIR);

    logFile = new File (userDirString, logFileName);

    if (logFile.isDirectory () ) {

      System.err.println (logFile.toString() + " is a directory.");

      setLogOk (false);;

    }

    if (isLogOk()) {

      try {

        logFileWriter = new FileWriter (logFile);

        logFileBufWriter = new BufferedWriter (logFileWriter);

      } catch (IOException e) {

        System.err.println (logFile.toString() + " suffered an I/O exception during open.");

        setLogOk (false);

      }

    }

    super.open();

  } // end open method

  

  public void close () {

    if (isLogOk() && isLogOpen()) {

      try {

        logFileBufWriter.close();

      } catch (IOException e) {

        System.err.println (this.toString() + " suffered an I/O Exception on close.");

        setLogOk (false);

      }

    }

    super.close();

  } // end close method

  

  public String toString () {

    return "LogOutputDisk";

  } // end toString method



} // end LogOutputDisk class

