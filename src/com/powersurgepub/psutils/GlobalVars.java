package com.powersurgepub.psutils;
  
/**
   A utility class containing global variables. <p>
    
   This code is copyright (c) 2003 by Herb Bowie of PowerSurge Publishing. 
   All rights reserved. <p>
   
   Version History: <ul><li>
       </ul>
 
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing (<A href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>) 
  
   @version 2003/04/13 - Initial creation of class.
 */
public class GlobalVars {

  private static 				String    dateSeparator         = "-";

  public GlobalVars() {
  }
  
  public static String getDateSeparator() {
    return dateSeparator;
  }
  
  public static void setDateSeparator (String dateSeparator) {
    GlobalVars.dateSeparator = dateSeparator;
  }
}
