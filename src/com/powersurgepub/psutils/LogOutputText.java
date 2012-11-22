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

  import javax.swing.*;

             

/**

   A log destination that writes log records 

   to a JTextArea.<p>

   

   This code is copyright (c) 1999-2002 by Herb Bowie of PowerSurge Publishing. 

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

  

   @version 02/04/10 - Created to allow logging data to be written to the Log tab for TDFCzar.

 */

public class LogOutputText extends LogOutput {

  

  private    JTextArea			logTextArea;

  private    BufferedWriter logFileBufWriter;

  private    int            logFileLineNumber;

  private    String         logFileLine;

  private    int            logFileLineLength;



  /**

     The "noarg" constructor. The text area will have to be passed separately. 

   */

  public LogOutputText () {

  } 

  

/**

   The constructor with a passed text area.

  

   @param textArea the text area to be used for output. 

 */

  public LogOutputText (JTextArea textArea) {

    logTextArea = textArea;

  } 

	

/**

   Set the text area to be used for output.

  

   @param textArea the text area to be used for output. 

 */

  public void setTextArea (JTextArea textArea) {

    logTextArea = textArea;

  } 

  

  /**

     Writes a line of output to the log text area.

    

     @param line  line of data to be written to the log file.

   */  

  public void writeLine (String line) {

    if (logTextArea != null) {

			logTextArea.append (line + GlobalConstants.LINE_FEED_STRING);

    }

  } 

  

  public void open () {

    super.open();

  } 

  

  public void close () {

    super.close();

  } 

  

  public String toString () {

    return "LogOutputText";

  } 



} 

