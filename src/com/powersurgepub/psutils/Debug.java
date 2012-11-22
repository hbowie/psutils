package com.powersurgepub.psutils;

  import java.util.*;
  
/**
   A utility class containing methods to assist with 
   debugging. <p>
     
   This code is copyright (c) 1999-2000 by Herb Bowie of PowerSurge Publishing. 
   All rights reserved. <p>
   
   Version History: <ul><li>
      2000/11/17 - Added code to display "NULL" for null object. <LI>
      2000/05/23 - Created new class.
    </ul>
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">herb@powersurgepub.com</a>)<br>
    <br>  of PowerSurge Publishing (<A href="http://www.powersurgepub.com/software/">www.powersurgepub.com/software</a>)
   @version 2000/11/18 - Added field to turn displays on or off.
 */
public class Debug {

  private     boolean         debugOn = true;

  /**
     Constructs a new Debug instance.
   */  
  public Debug () {
    this (true);
  }
  
  /**
     Constructs a new Debug instance with the passed debug flag.
   */  
  public Debug (boolean debugOn) {
    super ();
    this.debugOn = debugOn;
    if (debugOn) {
      System.out.println ("  DEBUG turned ON");
    }
  }

  /**
     Displays the passed String via a call to System.out.println(),
     with a special debugging prefix.
    
     @param   oneString The string to be displayed.
   */
  public void display (String oneString) {
    if (debugOn) {
      System.out.println ("  DEBUG -- " + oneString);
    }
  } // end display method
  
  /**
     Displays the passed String and object via a call to System.out.println(),
     with a special debugging prefix.
    
     @param   literal A string literal identifying the object.
    
     @param   variable An object to be displayed.
   */
  public void display (String literal, Object variable) {
    if (debugOn) {
      String varString;
      if (variable == null) {
        varString = "NULL";
      } else {
        varString = variable.toString();
      }
      System.out.println ("  DEBUG -- " + literal + ": " + varString);
    }
  } // end display method
  
  /**
     Displays the passed String and integer via a call to System.out.println(),
     with a special debugging prefix.
    
     @param   literal A string literal identifying the variable.
    
     @param   variable An integer to be displayed.
   */
  public void display (String literal, int variable) {
    if (debugOn) {
      String varString = String.valueOf (variable);
      System.out.println ("  DEBUG -- " + literal + ": " + varString);
    }
  } // end display method
  
  /**
     Displays the passed String and boolean via a call to System.out.println(),
     with a special debugging prefix.
    
     @param   literal A string literal identifying the variable.
    
     @param   variable A boolean to be displayed.
   */
  public void display (String literal, boolean variable) {
    if (debugOn) {
      String varString = String.valueOf (variable);
      System.out.println ("  DEBUG -- " + literal + "? " + varString);
    }
  } // end display method
  
  /**
     Displays all System properties.
   */
  public static void displaySystemProperties () {
    Properties p = System.getProperties();
    Enumeration list = p.propertyNames();
    while (list.hasMoreElements()) {
      String propName = (String)list.nextElement();
      String property = p.getProperty(propName);
      if (propName.equals ("line.separator")) {
        System.out.print (propName + ": ");
        for (int i = 0; i < property.length(); i++) {
          if (i > 0) {
            System.out.print ("/");
          }
          if (property.charAt(i) == GlobalConstants.CARRIAGE_RETURN) {
            System.out.print ("CR (ASCII 13)");
          } else
          if (property.charAt(i) == GlobalConstants.LINE_FEED) {
            System.out.print ("LF (ASCII 10)");
          } else {
            System.out.print (property.charAt(i));
          }
        }
        System.out.println(" ");
      } else {
        System.out.println (propName + ": " + property);
      }
    }
  } // end display method
	
} // end of Debug class
