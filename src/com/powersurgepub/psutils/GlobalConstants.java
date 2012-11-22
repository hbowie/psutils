package com.powersurgepub.psutils;

  import java.lang.Object;
  import java.lang.String;
  
/**
   A utility class containing global constants. <p>
    
   This code is copyright (c) 1999-2000 by Herb Bowie of PowerSurge Publishing. All rights reserved. <p>
   
   Version History: <ul><li>
     2000/06/01 - Added USER_DIR and FILE_SEPARATOR. <li>
     2000/05/10 - Added PIVOT_YEAR (equal to 50) as a global constant. Used by
                psdata.DateFormatRule for date windowing. <li>
     2000/04/04 - Applied standards according to "The Elements of Java Style". <br>
                Changed Class name from <code>GC</code> to <code>GlobalConstants</code>. <li>
     2000/03/26 - Cleaned up code and documentation.
       </ul>
 
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing (<A href="http://www.powersurgepub.com/software/">
           www.powersurgepub.com/software</a>) 
  
   @version 2000/11/15 - Added COMMA.
 */
public class GlobalConstants {

  public  final static char    CARRIAGE_RETURN          = '\r';
  public  final static String  CARRIAGE_RETURN_STRING   = "\r";
  public  final static char    DOUBLE_QUOTE             = '\"';
  public  final static String  DOUBLE_QUOTE_STRING      = "\"";
  public  final static String  EMPTY_STRING             = "";
  public  final static char    LINE_FEED                = '\n';
  public  final static String  LINE_FEED_STRING         = "\n";
  public  final static char    PERIOD                   = '.';
  public  final static String  PERIOD_STRING            = ".";
  public  final static char    SINGLE_QUOTE             = '\'';
  public  final static char    APOSTROPHE               = '\'';
  public  final static char    SPACE                    = ' ';
  public  final static String  SPACE_STRING             = " ";
  public  final static char    TAB                      = '\t';
  public  final static String  TAB_STRING               = "\t";
  public  final static char    COMMA                    = ',';
  public  final static String  COMMA_STRING             = ",";
  public  final static int     ZERO                     = 0;
  public  final static int     NOT_FOUND                = -1;
  public  final static int     PIVOT_YEAR               = 50;
  public  final static String  USER_DIR                 = "user.dir";
  public  final static String  USER_HOME                = "user.home";
  public  final static String  USER_NAME                = "user.name";
  public  final static String  FILE_SEPARATOR           = "file.separator";

}
