/*
 * Copyright 1999 - 2013 Herb Bowie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.powersurgepub.psutils;

  import java.util.*;
  
/**
   A utility class containing methods to assist with 
   debugging. <p>
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
