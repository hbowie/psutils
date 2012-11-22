package com.powersurgepub.psutils;



  import java.lang.Object;

  import java.lang.String; 

  import com.powersurgepub.psutils.LogOutput;

             

/**

   A null output destination for log records. This class accepts

   log input but does not write any output. This class is intended to

   be used when the user has chosen the option of not seeing any

   log output.<p>

   

   This code is copyright (c) 1999-2000 by Herb Bowie of PowerSurge Publishing. 

   All rights reserved. <p>

   

   Version History: <ul><li>

     </ul>

  

   @see com.powersurgepub.psutils.LogOutput

   @see com.powersurgepub.psutils.LogOutputDisk 

   @see com.powersurgepub.psutils.LogData

   @see com.powersurgepub.psutils.LogEvent

   @see com.powersurgepub.psutils.Logger

   @see com.powersurgepub.psutils.LogJuggler

  

   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">

           herb@powersurgepub.com</a>)<br>

           of PowerSurge Publishing (<A href="http://www.powersurgepub.com/software/">

           www.powersurgepub.com/software</a>)

  

   @version 00/04/11 - Modified to be consistent with "The Elements of Java Style".

 */

public class LogOutputNone extends LogOutput {



  /**

     The "noarg" constructor. The resulting object requires no further

     modification. 

   */

  public LogOutputNone () {

      // No initialization needed.

  } // end LogOutputNone constructor

  

  /**

     Accepts a line of output but does not send it anywhere.

    

     @param line  line of data to be written to the log file.

   */

  public void writeLine (String line) {

    // No output written for this LogOutput class.

  } // end writeLine method

  

  /**

     Returns the object as a String, with a standard identifier.

    

     @return the text "LogOutputNone".

   */

  public String toString () {

    return "LogOutputNone";

  } // end toString method



} // end LogOutputNone class

