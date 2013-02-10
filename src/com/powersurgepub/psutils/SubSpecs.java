/*
 * Copyright 2003 - 2013 Herb Bowie
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
   A collection of substring specifications, intended to allow
   a string to be easily sub-divided into component parts. <p>
 */

public class SubSpecs {
	
	/** Collection of SubSpec objects. */
  private List		specs					= new ArrayList();

	/**
	   Constructor with no arguments.
	 */
	public SubSpecs () {
		
	}
  
	/**
	   Adds a new substring specification to the collection. If a
     substring specification with the same letter already exists,
     then the new one will replace the old one. The constituent fields
     of the substring specification are passed separately, allowing
     this method to bundle them into a SubSpec object.
   
     @return true to indicate a successful addition.
     @param type   one-character abbreviation indicating the type of data.
     @param start  beginning position of the sub-string.
     @param length Length of the sub-string.
	 */
	public boolean add (char type, int start, int length) {
    return add (new SubSpec (type, start, length));
	}

  /**
	   Adds a new substring specification to the collection. If a
     substring specification with the same letter already exists,
     then the new one will replace the old one.
	  
     @return true to indicate a successful addition.
	   @param spec the substring specification to be added.
	 */
  public boolean add (SubSpec spec) {
    int i = specs.indexOf (spec);
    if (i >= 0) {
      SubSpec oldSpec = (SubSpec)specs.set (i, spec);
    } else {
      specs.add (spec);
    }
    return true;
  }
  
  /**
	   Returns a substring of the passed string.
	  
     @return the requested substring, or a null string
             if no substring specification matched the
             passed type.
	   @param type a single character identifying the substring
                 specification to use for extracting the
                 substring.
     @param s    the string from which the substring is to
                 be extracted.
	 */
  public String substring (char type, String s) {
    boolean match = false;
    int i = 0;
    char t = Character.toLowerCase(type);
    while ((i < specs.size()) && (! match)) {
      if (t == ((SubSpec)specs.get(i)).getType()) {
        match = true;
      } else {
        i++;
      }
    }
    if (match) {
      return (((SubSpec)specs.get(i)).substring(s));
    } else {
      return "";
    }
  }
  	
	/**
	   Returns the object in string form.
	  
	   @return object formatted as a string
	 */
	public String toString() {
    return "SubSpecs";
	}
  
  /**
     Tests the class.
   */
  public static void test () {
    
    System.out.println(" ");
    System.out.println ("Testing Class SubSpecs");
    SubSpecs specs = new SubSpecs();
    specs.add ('M', 0, 2);
    specs.add ('d', 3, 2);
    specs.add ('Y', 6, 4); 
    testSubSpecs (specs, "05/05/1951", 'm');
    testSubSpecs (specs, "05/05/1951", 'D');
    testSubSpecs (specs, "05/05/1951", 'y');
    testSubSpecs (specs, "05/05/1951", 'a');
  }
  
  private static void testSubSpecs (SubSpecs specs, String s, char type) {
    System.out.println ("Substring of " + s  
      + " of type " + String.valueOf(type)
      + " is " + specs.substring(type, s));
  }
  
} // end of class

