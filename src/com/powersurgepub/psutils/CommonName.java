package com.powersurgepub.psutils;



  import java.lang.Object;

  import java.lang.String;

  import java.lang.StringBuffer;

  

/**

   A name of something, reduced to its lowest common denominator form,

   by removing all spaces and punctuation and making all characters

   lower-case. <p>

     

   This code is copyright (c) 1999-2000 by Herb Bowie of PowerSurge Publishing. 

   All rights reserved. <p>

   

   Version History: <ul><li>

    </ul>

   @author Herb Bowie 

             (<a href="mailto:herb@powersurgepub.com">herb@powersurgepub.com</a>)<br>

           of PowerSurge Publishing 

             (<A href="http://www.powersurgepub.com/software/">www.powersurgepub.com/software</a>)

  

   @version 00/05/04 - Created this class from static method.

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

    StringBuffer workStr = new StringBuffer();

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

