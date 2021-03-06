/*
 * Copyright 2001 - 2013 Herb Bowie
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

  import java.lang.Character;
  import java.lang.IllegalArgumentException;
  import java.lang.Object;
  import java.lang.String;
  import java.lang.System;
  import com.powersurgepub.psutils.GlobalConstants;
  
/**
   A String with additional convenience methods. <p>
 */
public class SuperString {

  /** The String to be processed. */
  private    String    str;
  
  /** The length of the String to be scanned. */
  private    int      lng;
  
  /** 
     Tests the class. This method is called by <code>psutilsTest.main</code>.
   */
  static void test () {
    
    System.out.println ("Testing class SuperString");
    System.out.println ("Testing method startsWithIgnoreCase");
    testStartsWithIgnoreCase ("This is a test", "this");
    testStartsWithIgnoreCase ("This is a test", "that");
    testStartsWithIgnoreCase ("", "this");
    System.out.println ("Testing method endsWithIgnoreCase");
    testEndsWithIgnoreCase ("This is a test", "TEST");
    testEndsWithIgnoreCase ("This is a test", "not");
    testEndsWithIgnoreCase ("T", "not");
  } // end of test method

  /**
     Tests the comparison of the beginning of one string to another string.
    
     @param  s1  A test String to be compared.
    
     @param  s2  A string to compare to the beginning of s1.
   */
  private static void testStartsWithIgnoreCase (String s1, String s2) {
    SuperString test1 = new SuperString (s1);
    boolean result = test1.startsWithIgnoreCase (s2);
    System.out.println 
      (s1 + " starts with " + s2 + "?" + (result ? "Yes" : "No"));
  } // end of testStartsWithIgnoreCase method
  
  /**
     Tests the comparison of the end of one string to another string.
    
     @param  s1  A test String to be compared.
    
     @param  s2  A string to compare to the end of s1.
   */
  private static void testEndsWithIgnoreCase (String s1, String s2) {
    SuperString test1 = new SuperString (s1);
    boolean result = test1.endsWithIgnoreCase (s2);
    System.out.println 
      (s1 + " ends with " + s2 + "?" + (result ? "Yes" : "No"));
  } // end of testEndsWithIgnoreCase method
  
  /**
     Constructor requiring only a string.
    
     @param str  the string to be tested or otherwise processed.
   */
  public SuperString (String str) {
    this.str = str;
    lng = str.length();
  } // end of SuperString constructor
    
  /**
     Tests to see if this string begins with the passed string.
    
     @return True if this string begins with the passed string
                  (comparison is not case-sensitive).
    
     @param  starter String to be compared to the beginning of this string.
   */  
  public boolean startsWithIgnoreCase (String starter) {
    int starterLng = starter.length();
    if (starterLng > lng) {
      return false;
    } else {
      String beginning = str.substring (0, starterLng);
      return beginning.equalsIgnoreCase (starter);
    } 
  } // end of method startsWithIgnoreCase

  /**
     Tests to see if this string ends with the passed string.
    
     @return True if this string ends with the passed string
                  (comparison is not case-sensitive).
    
     @param  trailer String to be compared to the ending of this string.
   */  
  public boolean endsWithIgnoreCase (String trailer) {
    int trailerLng = trailer.length();
    if (trailerLng > lng) {
      return false;
    } else {
      String ending = str.substring (lng - trailerLng);
      return ending.equalsIgnoreCase (trailer);
    } 
  } // end of method endsWithIgnoreCase
  
  /**
     Returns string as a string.
    
     @return String that was passed in constructor.
   */
  public String toString() {
    return str;
  }
  
} // end of class SuperString
