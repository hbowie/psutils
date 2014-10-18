/*
 * Copyright 1999 - 2014 Herb Bowie
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

/**
   A name of something, reduced to its lowest common denominator form,
   by removing all spaces and punctuation and making all characters
   lower-case. 
 */

public class CommonName {

  /** The string as converted to its lowest common denominator format. */
  private   String    commonForm;

  /**
     Tests the class.
   */
  public static void test () {

    testOneCommonName ("Herb Bowie");
    testOneCommonName ("Street Address");
    testOneCommonName ("Line-Number:");
  }

  private static void testOneCommonName (String name) {

    CommonName test = new CommonName (name);
    System.out.println (name 
      + " becomes common name " 
      + test);
  }


  /** 
     Removes spaces and punctuation from string, and converts
     to lowercase, to reduce the string to a lowest common
     denominator, for comparison and identification purposes.

     @param name String to be converted.
   */

  public CommonName (String name) {
    char workChar;
    StringBuilder workStr = new StringBuilder();
    for (int i = 0; i < name.length(); i++) {
      workChar = name.charAt (i);
      if (Character.isLetterOrDigit (workChar)) {
        if (Character.isUpperCase (workChar)) {
          workChar = Character.toLowerCase (workChar);
        }
        workStr.append (workChar);
      }
    }
    commonForm = workStr.toString();
  } // end constructor

  /**
     Returns common name as a simple string.

     @return Common form of name, as a string.
   */
  public String getCommonForm() {
    return commonForm;
  }

  /**
     Determines if another common name is equal to this one.

     @return True if two strings are equal.

     @param  another Another common name, 
                     to be compared to this one.
   */
  public boolean equals (CommonName another) {
    return commonForm.equals (another.getCommonForm());
  }

  /**
     Compares this common name to another one.

     @return A positive number if the input name is greater than this one,
             a negative number if the input name is less than this one, or
             zero if the two are equal.

     @param  another Another CommonName to be compared to this one.
   */
  public int compareTo (CommonName another) {
    return commonForm.compareTo (another.getCommonForm());
  }

  /**
     Return common name as a plain old string.

     @return common name as a simple string.
   */
  public String toString() {
    return commonForm;
  }

} // end of CommonName class

