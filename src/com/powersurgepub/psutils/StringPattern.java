package com.powersurgepub.psutils;



  import java.lang.Character;

  import java.lang.Object;

  import java.lang.String;

  import java.lang.StringBuffer;

  import com.powersurgepub.psutils.StringScanner;

/**

   A pattern string indicating types of characters found 

   in each position of an input string. It can be used to validate that a 

   data field is in an expected format, or to determine which of several 

   formats a certain field (a phone number, for example) may be in. <p>

  

   The resulting output pattern is made up of representative characters

   defined in StringScanner. <p>

   

   This code is copyright (c) 1999-2000 by Herb Bowie of PowerSurge Publishing. 

   All rights reserved. <p>

   

   Version History: <ul><li>

     </ul>

  

   @see com.powersurgepub.psutils.StringScanner

  

   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">

           herb@powersurgepub.com</a>)<br>

           of PowerSurge Publishing (<A href="http://www.powersurgepub.com/software/">

           www.powersurgepub.com/software</a>)

  

   @version 00/04/20 - Modified to be consistent with "The Elements of Java Style".

 */



public class StringPattern {



  /** Scanner used to examine the input string. */

  private StringScanner scanner;

  

  /** The resulting pattern being built. */

  private String  pattern;

  

  /** The length of the input string. */

  private int    length = 0;

  

  /** The number of numeric characters (0 - 9) in the string. */

  private int    digitCount = 0;

  

  /** The number of alphabetic characters (a - z) in the string. */

  private int    letterCount = 0;

  

  /** The number of spaces in the string. */

  private int    spaceCount = 0;

  

  /** The number of punctuation characters in the string. */

  private int    punctCount = 0;

  

  /** Are all of the punctuation characters found the same character? */

  private boolean punctAllSame = true;

  

  /** If punctuation is all the same character, then this is that character. */

  private char  punctChar = ' ';

  

  /** 

     The test method will test the class. It is called by psutilsTest.main.</p>

   */

  public static void test () {

    

    System.out.println ("Testing class StringPattern");

    System.out.println ("Testing method getPattern");

    testPattern ("05/05/51");

    testPattern ("220-48-5752");

    testPattern ("Herb Bowie");

    testPattern ("10557 E. Mercer Lane");

  }

  

  /**

     Test one string by accepting an input string and printing the 

     resulting pattern.

    

     @param test The input string for the test.

   */

  public static void testPattern (String test) {

    StringPattern test1 = new StringPattern (test);

    System.out.println ("String  = " + test);

    System.out.println ("Pattern = " + test1.getPattern());

  }

  

  /**

     Builds the pattern string and counts the numbers 

     of various character types contained within the string.

     

     @param inString   this is the string whose pattern is to be determined

   */  

  public StringPattern (String inString) {

    scanner = new StringScanner (inString);

    StringBuffer work = new StringBuffer();

    length = inString.length();

    while (scanner.moreChars()) {

      if (scanner.getNextCharType() == StringScanner.DIGIT_CHAR) {

        digitCount++;

      } else

      if (scanner.getNextCharType() == StringScanner.LETTER_CHAR) {

        letterCount++;

      } else

      if (scanner.getNextCharType() == StringScanner.SPACE_CHAR) {

        spaceCount++;

      } else {

        punctCount++;

        if (punctCount == 1) {

          punctChar = scanner.getNextChar();

          punctAllSame = true;

        } else

        if (scanner.getNextChar() != punctChar) {

          punctAllSame = false;

        }

      }

      work.append (scanner.getNextCharType());

      scanner.incrementIndex();

    }

    pattern = work.toString();

  }

  

  /**

     Returns the pattern for the string.

    

     @return  a pattern, containing one character for every character in 

     the original string. Representative characters are as follows.

     

       <table border=3 cellspacing=0 cellpadding=5>

     <caption align=top>Pattern Characters</caption>

     <tr><th> Character  </th><th> Representing                  </th></tr>

     <tr><td> #          </td><td> Digit                         </td></tr>

     <tr><td> A          </td><td> Letter                        </td></tr>

     <tr><td> (space)    </td><td> Space                         </td></tr>

     <tr><td> . (period) </td><td> Punctuation (anything else)   </td></tr>

       </table>

   */

  public     String     toString ()     { return pattern; }

  

  /**

     Returns the same result as toString().

    

     @return  a pattern, containing one character for every character 

     in the original string. See toString().

   */

  public     String    getPattern ()     { return pattern; }

  

  /**

     Returns the length of the pattern (should be 

     same as length of the original string).

     

     @return  length of the pattern.

   */

  public     int      getLength ()     { return length; }

  

  /**

     Returns the number of digits in the string.

    

     @return  number of digits in original string.

   */

  public    int      getDigitCount ()   { return digitCount; }

  

  /**

     Returns the number of letters in the string.

    

     @return  number of letters in original string.

   */

  public    int      getLetterCount ()   { return letterCount; }

  

  /**

     Returns the number of spaces in the string.</p>

     

     @return  number of spaces in original string.

   */

  public    int      getSpaceCount ()  { return spaceCount; }

  

  /**

     Returns the number of punctuation characters in the string.

    

     @return  Number of punctuation characters in original string.

   */

  public    int      getPunctCount ()  { return punctCount; }

  

  /**

     Returns the type of punctuation used in the string.

     

     @return  First punctuation character found in original string.

   */

  public    char    getPunctChar ()    { return punctChar; }

  

  /**

     Indicates whether only one punctuation character was 

     consistently used in the given string.

    

     @return  true if all punctuation characters are the same, 

              or if only one punctuation character was found.

   */

  public    boolean    isPunctAllSame ()  { return punctAllSame; }

  

  /**

     Returns the pattern character at the given position.

    

     @return  Pattern character, or space if index is out of bounds.

    

     @param   index Position of desired character (first = 0).

   */

  public    char       charAt (int index) {

    if ((index < 0) || (index >= length)) {

      return StringScanner.SPACE_CHAR;

    } else {

      return pattern.charAt(index);

    }

  }

  

  /**

     Checks to see if this pattern string exactly equals another one.

    

     @return  true if this pattern is identical to the passed string.

     

     @param   anotherPattern  Second pattern string to be compared to this one.

   */

  public    boolean    equals (String anotherPattern) {

    return (pattern.equals (anotherPattern));

  }

  

  /**

     Checks to see if this pattern string starts with another one.

    

     @return  true if this pattern starts with the passed string.

     

     @param   anotherPattern  Second pattern string to be compared to this one.

   */

  public    boolean    startsWith (String anotherPattern) {

    return (pattern.startsWith (anotherPattern));

  }



} // end class StringPattern

