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

  import java.awt.*;
  import java.io.*;
  import java.math.*;
  
/**
   A utility class containing static methods to do things
   with strings. 
 */
public class StringUtils {

  public final static String VOWELS = "aeiou";
  public final static int    UPPER_CASE = 1;
  public final static int    LOWER_CASE = -1;
  public final static int    ANY_CASE = 0;
  public final static String HTML_LINE_BREAK = "<br />";
  public final static String HTML_DOUBLE_QUOTE = "&quot;";
  public final static String HTTP = "http://";
  public final static char   FILE_NAME_WORD_SEPARATOR = '-';

  /**
     Tests the class.
   */
  public static void test () {
    
    System.out.println (" ");
    System.out.println ("Testing StringUtils.println");
    println (System.out, "Line 1 cr " 
          + GlobalConstants.CARRIAGE_RETURN_STRING
        + "Line 2 lf " 
          + GlobalConstants.LINE_FEED_STRING
        + "Line 3 cr-lf " 
          + GlobalConstants.CARRIAGE_RETURN_STRING 
          + GlobalConstants.LINE_FEED_STRING
        + "Line 4 lf-cr " 
          + GlobalConstants.LINE_FEED_STRING 
          + GlobalConstants.CARRIAGE_RETURN_STRING);
    
    System.out.println (" ");
    System.out.println ("Testing method replaceString");
    testReplaceString ("abb acc aa", "a", "dd"); 
    
    System.out.println (" ");
    System.out.println ("Testing HTML coding");
    testHTMLCoding ("Line 1"
        + GlobalConstants.CARRIAGE_RETURN_STRING
        + GlobalConstants.LINE_FEED_STRING
        + "Line 2"
        + GlobalConstants.LINE_FEED_STRING
        + GlobalConstants.CARRIAGE_RETURN_STRING
        + GlobalConstants.CARRIAGE_RETURN_STRING
        + GlobalConstants.LINE_FEED_STRING
        + "Line 3"
        + GlobalConstants.TAB_STRING
        + "Line 3 after Tab"
        + GlobalConstants.CARRIAGE_RETURN_STRING
        + GlobalConstants.LINE_FEED_STRING
        + "\"Line 4\"");
    System.out.println (" ");
    testHTMLDecoding ("Line 1 <br />Line 2 <br />Line 3");
    System.out.println(" ");
    System.out.println ("Testing method wordDemarcation");
    testWordDemarcation ("This_is-a   test", "", LOWER_CASE, UPPER_CASE, LOWER_CASE);
    testWordDemarcation ("ThisTooIsA Test", "_", UPPER_CASE, UPPER_CASE, UPPER_CASE);
    testWordDemarcation ("AndHowAboutThis", " ", UPPER_CASE, UPPER_CASE, LOWER_CASE);
    System.out.println ("Testing Class StringUtils");
    System.out.println ("Testing method stringFromInt");
    testStringFromInt (0, 2);
    testStringFromInt (5, 2);
    testStringFromInt (1951, 2);
    testStringFromInt (12, 2);
    System.out.println ("Testing method removeQuotes");
    testRemoveQuotes ("'Single Quotes'");
    System.out.println ("Testing method wordSpace");
    testWordSpace ("This_is-a   test", false);
    testWordSpace ("ThisTooIsA Test", false);
    testWordSpace ("AndHowAboutThis", true);
    System.out.println ("Testing method initialCaps");
    testInitialCaps ("HERBERT HUGHES BOWIE JR");
    testInitialCaps ("stephen lee bowie");
    testInitialCaps ("10557 e mercer lane, scottsdale");
    System.out.println ("Testing method replaceChars");
    testReplaceChars ("UG II", " ", "_");
    System.out.println ("Testing method almostEqual");
    testAlmostEqual ("H", "Home");
    testAlmostEqual ("Hm", "Home");
    testAlmostEqual ("E-Mail", "email");
    testAlmostEqual ("Rd.", "Road");
    testAlmostEqual ("St.", "Street");
    testAlmostEqual ("Mr.", "Mister");
    testAlmostEqual ("This", "That");
    testIndexOfIgnoreCase ("and", "Andover, MA");
    testIndexOfIgnoreCase ("ma", "Andover, MA");
    testIndexOfIgnoreCase ("over", "Andover, MA");
    testIndexOfIgnoreCase ("Andy", "Andover, MA");
    testIndexOfIgnoreCase ("land", "Andover, MA");
    testIndexOfIgnoreCase ("Andy", "And Andy Andover");
    System.out.println (" ");
    System.out.println ("Testing method cleanURLString");
    testCleanURLString ("http://");
    testCleanURLString ("");
    testCleanURLString (" ");
    testCleanURLString ("www.powersurgepub.com");
    testCleanURLString ("<www.amazon.com>");
  }
  
  private static void testHTMLCoding (String inStr) {
    System.out.println ("Encoding:");
    System.out.println (inStr);
    System.out.println ("To HTML:");
    String codedStr = encodeHTML (inStr);
    System.out.println (codedStr);
    System.out.println ("Decoding:");
    System.out.println (decodeHTML (codedStr));
  }
  
  private static void testHTMLDecoding (String codedStr) {
    System.out.println ("Decoding:");
    System.out.println (codedStr);
    System.out.println ("Without HTML:");
    System.out.println (decodeHTML (codedStr));
  }
  
  private static void testStringFromInt (int inInt, int desiredLength) {
    String test = stringFromInt (inInt, desiredLength);
    System.out.println (inInt 
      + " with length of " 
      + desiredLength 
      + " becomes "
      + test);
  }
  
  private static void testRemoveQuotes (String inStr) {
    String test = removeQuotes (inStr);
    System.out.println (inStr 
      + " becomes "
      + test);
  }
  
  private static void testWordDemarcation
      (String inStr, String delimiter, int firstCase, int leadingCase, int normalCase) {
    String test = wordDemarcation (inStr, delimiter, firstCase, leadingCase, normalCase);
    System.out.println (inStr 
      + " with delimiter (" + delimiter
      + ") firstCase (" + String.valueOf (firstCase)
      + ") leadingCase (" + String.valueOf (leadingCase)
      + ") normalCase (" + String.valueOf (normalCase)
      + ") becomes "
      + test);
  }
  
  private static void testWordSpace (String inStr, boolean toLower) {
    String test = wordSpace (inStr, toLower);
    System.out.println (inStr 
      + " with toLower "
      + toLower
      + " becomes "
      + test);
  }
  
  private static void testInitialCaps (String inStr) {
    String test = initialCaps (inStr);
    System.out.println (inStr
      + " with initalCaps becomes "
      + test);
    }
    
  private static void testReplaceChars 
      (String inStr, String fromStr, String toStr) {
    String test = replaceChars (inStr, fromStr, toStr);
    System.out.println (inStr
      + " with " + fromStr + " replaced by " + toStr + " becomes "
      + test);
    }

  private static void testReplaceString 
      (String inStr, String fromStr, String toStr) {
    String test = replaceString (inStr, fromStr, toStr);
    System.out.println (inStr
      + " with " + fromStr + " replaced by " + toStr + " becomes "
      + test);
    }
    
  private static void testAlmostEqual 
      (String s1, String s2) {
    System.out.println ("Is " +
       s1 + " almost equal to " + s2 + "? " + String.valueOf (almostEqual(s1, s2)));
    }
  
  private static void testIndexOfIgnoreCase (String s1, String s2) {
    String sLower = s1.toLowerCase();
    String sUpper = s1.toUpperCase();
    System.out.println ("String " + s1 
        + " found within " + s2
        + " at " + String.valueOf (indexOfIgnoreCase(sLower, sUpper, s2, 0)));
  }
  
  private static void testCleanURLString (String dirty) {
    String clean = cleanURLString (dirty);
    System.out.println ("URL " + dirty + " becomes " + clean);
  }

  private StringUtils () {
    super ();
  }
  
  public static String pluralize(String thing, int number) {
    if (number == 1) {
      return thing;
    } else {
      StringBuilder things = new StringBuilder(thing);
      things.append('s');
      return things.toString();
    }
  }
  
  /**
   Replace fancy typographic symbols -- "smart quotes" and the like 
   -- with their plain country cousins.
  
   @param s The string to be stupefied. 
  
   @return The stupefied string.
  */
  public static String stupefy(String s) {

    StringBuilder str = new StringBuilder(s);
    int i = 0;
    while (i < str.length()) {
      char c = str.charAt(i);
      if (Character.isLetter(c)) {
        // Leave it alone
      }
      else
      if (Character.isDigit(c)) {
        // Leave it alone
      }
      else
      if (isApostrophe(c)) {
        str.deleteCharAt(i);
        str.insert(i, '\'');
      }
      else
      if (isDoubleQuote(c)) {
        str.deleteCharAt(i);
        str.insert(i, '"');
      }
      i++;
    }
    return str.toString();
  }
  
  public static boolean isDash (char c) {
    return (c == '-' || c == '~' || c == '—' || c == '–');
  }
  
  public static boolean isDoubleQuote (char c) {
    return (c == '"' || c == '“' || c == '”');
  }
  
  public static boolean isApostrophe(char c) {
    return (c == '’' || c == '\'');
  }
  
  public static String colorToHexString (Color color) {
    int red = color.getRed();
    int green = color.getGreen();
    int blue = color.getBlue();
    return (oneColorToHexString(red) 
        + oneColorToHexString(green)
        + oneColorToHexString(blue));
  }
  
  public static Color hexStringToColor (String color) {
    int red = 0;
    int green = 0;
    int blue = 0;
    try {
      red = Integer.parseInt (color.substring (0, 2), 16);
      green = Integer.parseInt (color.substring (2, 4), 16);
      blue = Integer.parseInt (color.substring (4, 6), 16);
    } catch (NumberFormatException e) {
      // ok
    }
    return new Color (red, green, blue);
  }
  
  public static String oneColorToHexString (int color) {
    if (color < 16) {
      return "0" + Integer.toHexString(color).toUpperCase();
    } else {
      return Integer.toHexString(color).toUpperCase();
    }
  }
  
  /**
     Cleans up a string that is to be used as a URL. 
   
     @param inURLString String to be used as a URL.
     @return Cleaned up string with obvious errors removed.
   */
  public static String cleanURLString (String inURLString) {
    StringBuffer work = new StringBuffer (purify(inURLString.trim()));
    if (work.length() > 0
        && work.charAt (0) == '<') {
      work.deleteCharAt (0);
    }
    if (work.length() > 0
        && work.charAt (work.length() - 1) == '>') {
      work.deleteCharAt (work.length() - 1);
    }
    int periods = 0;
    int nextPeriod = 0;
    int startRemainder = 3;
    while (nextPeriod >= 0) {
      nextPeriod = work.indexOf (".", startRemainder);
      if (nextPeriod > 0) {
        periods++;
        startRemainder = nextPeriod + 1;
      }
    }
    if ((work.indexOf(":") < 0)
        && (work.length() > 0)
        && ((periods > 1) 
            || (work.toString().endsWith (".com"))
            || (work.toString().endsWith (".org"))
            || (work.toString().endsWith (".net")))) {
      work.insert (0,HTTP);
    }
    if (work.toString().equals (HTTP)) {
      work.setLength(0);
    }
    return work.toString();
  }
  
  public static String legitimizeURL(String inURL) {
    StringBuilder work = new StringBuilder(inURL);
    int i = 0;
    while (i < (work.length())) {
      if (work.charAt(i) == ' ') {
        work.replace (i, i + 1, "%20");
        i = i + 3;
      }
      else
      if (work.charAt(i) == '&') {
        work.replace (i, i + 1, "%26");
        i = i + 3;
      } else {
        i++;
      }
    }
    return work.toString();
  }

  /**
   Remove special characters inserted into a URL string to make it
   machine-readable, but that hamper human readability.

   @param  dirtyURL URL string with % encoded entities
   @return Clean url with these special codings replaced by the characters
           they are meant to represent.
   */
  public static String restoreSpacesToURL(String dirtyURL) {
    StringBuffer cleanURL = new StringBuffer(dirtyURL);
    int i = 0;
    while (i >= 0 && i < cleanURL.length()) {
      i = cleanURL.indexOf("%20", i);
      if (i >= 0 && i < cleanURL.length()) {
        cleanURL.delete(i, i + 3);
        cleanURL.insert(i, " ");
      }
    } // end while more string to replace
    return cleanURL.toString();
  }

  /**
     Converts an integer to a String of a given length. 
     After converting the integer to a String, it will be
     padded with zeros if too short, or have digits removed 
     on the left if it is too long.
    
     @param   inInt          The integer to be converted to a String.
    
     @param   desiredLength  The desired length for the String to be returned.
    
     @return  A String of the desired length.
   */
  public static String stringFromInt (int inInt, int desiredLength) {
    return stringFromInt(inInt, desiredLength, false, '0', false);
  } // end stringFromInt method
  
  /**
     Converts an integer to a String of a given length. 
     After converting the integer to a String, it will be
     padded with zeros if too short, or have digits removed 
     on the left if it is too long.
    
     @param   inInt          The integer to be converted to a String.
    
     @param   desiredLength  The desired length for the String to be returned.
  
     @param   insertCommas   Insert commas every three characters?
  
     @param   padChar        Generally a zero or space. 
  
     @param   blankIfZero    Leave the entire string blank if the integer 
                             is zero. 
    
     @return  A String of the desired length.
   */
  public static String stringFromInt (
      int     inInt, 
      int     desiredLength,
      boolean insertCommas, 
      char    padChar,
      boolean blankIfZero) {
    String startStr = String.valueOf (inInt);
    StringBuilder work = new StringBuilder ();
    int j = startStr.length() - 1;
    int commaCounter = 0;
    while (work.length() < desiredLength) {
      if (work.length() == 0) {
        if (inInt == 0) {
          if (blankIfZero) {
            work.insert(0, " ");
          } else {
            work.insert(0, "0");
          }
          if (startStr.length() > 0) {
            j--;
          }
        }
      }
      if (work.length() < desiredLength) {
        if (j >= 0) {
          if (commaCounter > 2) {
            work.insert(0, ",");
            commaCounter = 0;
          }
          work.insert(0, startStr.charAt(j));
          j--;
          commaCounter++;
        } else {
          work.insert(0, padChar);
        }
      }
    }
    return work.toString();
  } // end stringFromInt method
  
    /**
     Converts an integer to a String of a given length. 
     After converting the integer to a String, it will be
     padded with zeros if too short, or have digits removed 
     on the left if it is too long.
    
     @param   inLong          The integer to be converted to a String.
    
     @param   desiredLength  The desired length for the String to be returned.
  
     @param   insertCommas   Insert commas every three characters?
  
     @param   padChar        Generally a zero or space. 
  
     @param   blankIfZero    Leave the entire string blank if the integer 
                             is zero. 
    
     @return  A String of the desired length.
   */
  public static String stringFromLong (
      long    inLong, 
      int     desiredLength,
      boolean insertCommas, 
      char    padChar,
      boolean blankIfZero) {
    String startStr = String.valueOf (inLong);
    StringBuilder work = new StringBuilder ();
    int j = startStr.length() - 1;
    int commaCounter = 0;
    while (work.length() < desiredLength) {
      if (work.length() == 0) {
        if (inLong == 0) {
          if (blankIfZero) {
            work.insert(0, " ");
          } else {
            work.insert(0, "0");
          }
          if (startStr.length() > 0) {
            j--;
          }
        }
      }
      if (work.length() < desiredLength) {
        if (j >= 0) {
          if (commaCounter > 2) {
            work.insert(0, ",");
            commaCounter = 0;
          }
          work.insert(0, startStr.charAt(j));
          j--;
          commaCounter++;
        } else {
          work.insert(0, padChar);
        }
      }
    }
    return work.toString();
  } // end stringFromInt method


  /**
     Removes leading and trailing spaces, and then 
     quotation marks, from a string.
    
     @param  inStr    The input string.
    
     @return          A String without surrounding quotation marks.
   */
  public static String removeQuotes (String inStr) {
    if (inStr.length() > 0) {
      int i, j;
      i = 0;
      j = inStr.length() - 1;
      while ((i < j) 
        && (inStr.charAt (i) == GlobalConstants.SPACE)){
        i++;
      }
      while ((j >= 0)
        && (inStr.charAt (j) == GlobalConstants.SPACE)) {
        j--;
      }
      if ((j > i)
        && (inStr.charAt (i) == inStr.charAt (j))
        && ((inStr.charAt (i) == GlobalConstants.SINGLE_QUOTE)
          || (inStr.charAt (i) == GlobalConstants.DOUBLE_QUOTE))) {
        i++;
        j--;
      }
      j++;
      if ((j > i) && (j > 0)) {
        return inStr.substring (i, j);
      } 
      else {
        return GlobalConstants.EMPTY_STRING;
      }
    } else {
      return inStr;
    }
  } // end removeQuotes method
  
  /**
     Removes trailing white space from a String.
    
     @param  inStr      The input string.
    
     @return            A String without trailing white space.
   */
  public static String trimRight (String inStr) {
    int l = inStr.length();
    if (l == 0) {
      return inStr;
    } else {
      int r = l;
      char nextChar = GlobalConstants.SPACE;
      while ((r > 0)
        && ((nextChar == GlobalConstants.SPACE)
          || (nextChar == GlobalConstants.TAB)
          || (nextChar == GlobalConstants.CARRIAGE_RETURN)
          || (nextChar == GlobalConstants.LINE_FEED))) {
        r--;
        nextChar = inStr.charAt (r);
      }
      r++;
      return inStr.substring (0, r);
    }
  } // end method trimRight
  
  /**
     Puts single spaces between apparent words,
     replacing punctuation, and adding spaces where lower-case letters 
     are followed by upper-case ones. Useful for making things like file
     names appear more like regular English, and for sorting Strings into 
     sequences that appear more natural to humans.
    
     @param  inStr      The input string.
    
     @param  toLower    If true, then all letters will additionally
                        be converted to lower case (handy for sorting).
    
     @return            A String with spaces added between words.
   */
  public static String wordSpace (String inStr, boolean toLower) {
    int firstCase, leadingCase, normalCase;
    if (toLower) {
      firstCase = LOWER_CASE;
      leadingCase = LOWER_CASE;
      normalCase = LOWER_CASE;
    } else {
      firstCase = ANY_CASE;
      leadingCase = ANY_CASE;
      normalCase = ANY_CASE;
    }
    return wordDemarcation (inStr, " ", firstCase, leadingCase, normalCase);
  } // end of wordSpace method
    /*
    StringBuffer workBuf = new StringBuffer ();
    char c, cout;
    boolean lastCharSpace = true;
    boolean lastCharUpper = true;
    int length = inStr.length();
    for (int i = 0; i < length; i++) {
      c = inStr.charAt (i);
      if (toLower) {
        cout = Character.toLowerCase (c);
      } else {
        cout = c;
      }
      if (Character.isLetterOrDigit (c)) {
        if (Character.isUpperCase (c)) {
          if ((! lastCharSpace) && (! lastCharUpper)) {
            workBuf.append (" ");
            workBuf.append (cout);
            lastCharSpace = false;
            lastCharUpper = true;
          } // end of space insertion
          else {
            workBuf.append (cout);
            lastCharSpace = false;
            lastCharUpper = true;
          } // end of no space insertion
        } // end upper case logic
        else { // character a lower-case letter or digit
          workBuf.append (cout);
          lastCharSpace = false;
          lastCharUpper = false;
        } // end of letter or digit logic
      } else {
        if (! lastCharSpace) {
          workBuf.append (" ");
          lastCharSpace = true;
          lastCharUpper = false;
        } 
      } // end punctuation or space logic
    } // end of inStr
    return workBuf.toString();
  } // end of wordSpace method
  */
  
   /**
     Modifies the demarcation of words within a string.
    
     @return            A String with modified demarcation between words.
    
     @param  inStr      The input string. It will be broken up into words by 
                        looking for punctuation or white space between words, 
                        or transitions from lower- to upper-case. 
    
     @param  delimiter  Something to be inserted between words. May contain 
                        zero, one or more characters. 
    
     @param  firstCase  Indicates whether first letter of first word should be
                        forced to upper-case, forced to lower-case, or left as-is. <ul> <li>
                        1 = Forced to upper. <li>
                        0 = Left as-is. <li>
                       -1 = Forced to lower. </ul>
    
     @param  leadingCase  Indicates whether first letters of remaining words should be
                          forced to upper-case, forced to lower-case, or left as-is. <ul> <li>
                          1 = Forced to upper. <li>
                          0 = Left as-is. <li>
                         -1 = Forced to lower. </ul>
    
     @param  normalCase   Indicates whether remaining letters of words should be
                          forced to upper-case, forced to lower-case, or left as-is. <ul> <li>
                          1 = Forced to upper. <li>
                          0 = Left as-is. <li>
                         -1 = Forced to lower. </ul>
   */
  public static String wordDemarcation 
      (String inStr, String delimiter, int firstCase, int leadingCase, int normalCase) {
    StringBuffer workBuf = new StringBuffer ();
    char c;
    boolean charUpper = false;
    boolean lastCharUpper = true;
    boolean newWord = true;
    int wordCount = 0;
    int newCase = 0;
    int length = inStr.length();
    for (int i = 0; i < length; i++) {
      lastCharUpper = charUpper;
      c = inStr.charAt (i);
      newCase = normalCase;
      charUpper = false;
      if (Character.isUpperCase (c)) {
        charUpper = true;
      }
      if (Character.isLetterOrDigit (c)) {
        if (charUpper && (! lastCharUpper)) {
          newWord = true;
        }
        if (newWord) {
          wordCount++;          
          if (wordCount == 1) {
            newCase = firstCase;
          } else {
            newCase = leadingCase;
            workBuf.append (delimiter);
          }
          newWord = false;
        } // end if new word
        if (newCase > 0) {
          workBuf.append (Character.toUpperCase (c));
        }
        else
        if (newCase < 0) {
          workBuf.append (Character.toLowerCase (c));
        } else {
          workBuf.append (c);
        }
      } else {
        newWord = true;
      }
    } // end for every character in input string
    return workBuf.toString();
  } // end of wordDemarcation method
  
  /**
     Converts a string to initial (leading) capital letters.
    
     @return Input string, but with first letter of each word
             capitalized, and other letters in lower-case.
    
     @param inString String to be converted.
   */
  public static String initialCaps (String inString) {
		char newChar = ' ';
		char workChar = ' ';
		char lastChar = ' ';
		StringBuffer s = new StringBuffer ();
		int i = 0;
		while (i < inString.length()) {
			lastChar = workChar;
			workChar = inString.charAt (i);
			if (Character.isLetter(lastChar)) {
				newChar = Character.toLowerCase (workChar);
			} else {
				newChar = Character.toUpperCase (workChar);
			}
			s.append (newChar);
			i++;
		}
		return s.toString();
	} // end of method initialCaps
  
  /**
     Replaces one or more characters, when found in a String, to 
     alternate characters.
    
     @return Input string, but with any characters appearing in the 
             from string replaced with the corresponding characters in 
             the to string.
    
     @param inString   String to be converted.
    
     @param fromString String containing characters to be converted. 
                       There should be a one-to-one correlation between
                       each character in this string and each
                       replacement character in the to string. 
    
     @param toString   String containing characters to replace 
                       corresponding characters in from string. 
   */
  public static String replaceChars 
      (String inString, String fromString, String toString) {
		char workChar = ' ';
    int j;
		StringBuffer s = new StringBuffer (inString);
    int inLength = inString.length();
    int toLength = toString.length();
		for (int i = 0; i < inLength; i++) {
			workChar = inString.charAt (i);
      j = fromString.indexOf (workChar);
      if (j >= 0 && j < toLength) {
        s.setCharAt (i, toString.charAt (j));
      }
		}
		return s.toString();
	} // end of method replaceChars
  
  /**
     Purifies the string by removing carriage returns, line feeds, tabs
     and double quotation marks.
    
     @return Input string, but with carriage returns, line feeds, tabs and 
             double quotation marks removed. A space will be added unless one 
             already immediately precedes or follows the purged character.
    
     @param inString   String to be purified.
   */
  public static String purify (String inString) {
		char workChar = ' ';
		StringBuilder s = new StringBuilder (inString);
    boolean junk = false;
    int spaceCount = 0;
    int i = 0;
		while (i < s.length()) {
			workChar = s.charAt (i);
      if (workChar == GlobalConstants.CARRIAGE_RETURN 
          || workChar == GlobalConstants.LINE_FEED
          || workChar == GlobalConstants.TAB
          || workChar == GlobalConstants.DOUBLE_QUOTE) {
        s.deleteCharAt (i);
        junk = true;
      } 
      else 
      if (workChar == ' ') {
        spaceCount++;
        i++;
      } 
      else 
      if (junk && (spaceCount == 0)) {
        s.insert (i, ' ');
        i = i + 2;
        spaceCount = 0;
        junk = false;
      } else {
        i++;
        spaceCount = 0;
        junk = false; 
      } 
		}
		return s.toString();
	} // end of method purify
  
  /**
   Purifies the string by removing carriage returns, line feeds, tabs
   and awkward punctuation.
  
   @param inString The string to be purified.
  
   @return The purified string. 
  */
  public static String purifyPunctuation (String inString) {
		char workChar = ' ';
		StringBuilder s = new StringBuilder (inString);
    boolean junk = false;
    int spaceCount = 0;
    int i = 0;
		while (i < s.length()) {
			workChar = s.charAt (i);
      if    (workChar == GlobalConstants.CARRIAGE_RETURN 
          || workChar == GlobalConstants.LINE_FEED
          || workChar == GlobalConstants.TAB) {
        s.deleteCharAt (i);
        junk = true;
      }
      else
      if (workChar == GlobalConstants.DOUBLE_QUOTE
          || workChar == GlobalConstants.SINGLE_QUOTE
          || workChar == GlobalConstants.COMMA
          || workChar == GlobalConstants.APOSTROPHE
          || workChar == '/'
          || workChar == '\\'
          || workChar == ':'
          || workChar == ';'
          || workChar == '?'
          || workChar == '(' 
          || workChar == ')' 
          || workChar == '=' 
          || workChar == '&' 
          || workChar == '%' 
          || workChar == '$'
          || workChar == '!') {
        s.deleteCharAt (i);
      } 
      else 
      if (workChar == ' ') {
        spaceCount++;
        i++;
      } 
      else 
      if (junk && (spaceCount == 0)) {
        s.insert (i, ' ');
        i = i + 2;
        spaceCount = 0;
        junk = false;
      } else {
        i++;
        spaceCount = 0;
        junk = false; 
      } 
		}
		return s.toString();
	} // end of method purify
  
  /**
     Purifies the string by removing carriage returns, line feeds and tabs.
    
     @return Input string, but with carriage returns, line feeds and tabs 
             removed. A space will be added unless one 
             already immediately precedes or follows the purged character.
    
     @param inString   String to be purified.
   */
  public static String purifyInvisibles (String inString) {
		char workChar = ' ';
		StringBuilder s = new StringBuilder (inString);
    boolean junk = false;
    int spaceCount = 0;
    int i = 0;
		while (i < s.length()) {
			workChar = s.charAt (i);
      if (workChar == GlobalConstants.CARRIAGE_RETURN 
          || workChar == GlobalConstants.LINE_FEED
          || workChar == GlobalConstants.TAB) {
        s.deleteCharAt (i);
        junk = true;
      } 
      else 
      if (workChar == ' ') {
        spaceCount++;
        i++;
      } 
      else 
      if (junk && (spaceCount == 0)) {
        s.insert (i, ' ');
        i = i + 2;
        spaceCount = 0;
        junk = false;
      } else {
        i++;
        spaceCount = 0;
        junk = false; 
      } 
		}
		return s.toString();
	} // end of method purify
  
  /**
   Return the lowest common denominator of two strings naming something.
  
   @param inString String to be reduced to its lowest common denominator. 
  
   @return The lowest common denominator of the string. If the string starts
           with the words "a", "an" or "the", remove them. 
   */
  public static String commonName (String inString) {
    StringBuilder s = new StringBuilder ();
    int i = 0;
    int j = inString.indexOf(' ');
    if (j < 0) {
      j = inString.indexOf('_');
    }
    if (j < 0) {
      j = inString.indexOf('-');
    }
    if (j > 0) {
      String firstWord = inString.substring(0, j);
      if (firstWord.equalsIgnoreCase("a")) {
        i = 2;
      }
      else
      if (firstWord.equalsIgnoreCase("an")) {
        i = 3;
      }
      else
      if (firstWord.equalsIgnoreCase("the")) {
        i = 4;
      }
    } 
    char c = ' ';
    while (i < inString.length()) {
      c = inString.charAt(i);
      if (Character.isLetter (c)) {
        s.append (Character.toLowerCase (c));
      }
      else
      if (Character.isDigit (c)) {
        s.append (c);
      } 
      i++;
    }
    return s.toString();
  }

  /**
    Encodes the string by converting carriage returns and
    line feeds to break tags, by converting double quotation marks
    to HTML entities, and by removing tabs.
    
    @return Input string, but with dangerous characters converted
            to their HTML equivalents.
    
    @param inString   String to be encoded.
   */
  public static String encodeHTML (String inString) {
    StringConverter sc = StringConverter.getMinimumHTML();
    sc.setForward (true);
    return sc.convert (inString);
    /*
    StringBuffer s = new StringBuffer (inString);
    char lastCRLFChar = ' ';
		char workChar = ' ';
    boolean spaceNeeded = false;
    boolean brLast = false;
    int spaceCount = 0;
    int i = 0;
		while (i < s.length()) {
			workChar = s.charAt (i);
      if (workChar == GlobalConstants.CARRIAGE_RETURN
          || workChar == GlobalConstants.LINE_FEED) {
        if ((workChar != lastCRLFChar) && brLast) {
          s.deleteCharAt (i);
          brLast = false;
          lastCRLFChar = workChar;
        } else {
          s.deleteCharAt (i);
          s.insert (i, HTML_LINE_BREAK);
          i = i + HTML_LINE_BREAK.length();
          brLast = true;
          lastCRLFChar = workChar;
        } // end if first LF/BR in possible sequence
      } // end if CR or LF
      else
      if (workChar == GlobalConstants.TAB) {
        s.deleteCharAt (i);
        spaceNeeded = true;
        brLast = false;
        lastCRLFChar = ' ';
      } // end if tab
      else
      if (workChar == GlobalConstants.DOUBLE_QUOTE) {
        s.deleteCharAt (i);
        s.insert (i, HTML_DOUBLE_QUOTE);
        i = i + HTML_DOUBLE_QUOTE.length();
        brLast = false;
        lastCRLFChar = ' ';
      } 
      else 
      if (workChar == ' ') {
        spaceCount++;
        i++;
        brLast = false;
        lastCRLFChar = ' ';
      } 
      else 
      if (spaceNeeded && (spaceCount == 0)) {
        s.insert (i, ' ');
        i = i + 2;
        spaceCount = 0;
        spaceNeeded = false;
        brLast = false;
        lastCRLFChar = ' ';
      } else {
        i++;
        spaceCount = 0;
        spaceNeeded = false; 
        brLast = false;
        lastCRLFChar = ' ';
      } 
		} // end while more characters to inspect
		return s.toString();
    */
	} // end of method encodeHTML
  
  /**
    Checks to see if the first string is equal to the specified
    substring of the second string, ignoring case and bad starting
    and ending points.
   
    @return True if they are equal, false otherwise. 
    @param  str1  A string providing a substring to be compared.
    @param  start The starting point for the substring (inclusive).
    @param  str2  A string to be compared in its entirety.
   */
  public static boolean equalsSubstringIgnoreCase 
      (String str1, int start, String str2) {
    int end = start + str2.length();
    String str1sub = substringLenient (str1, start, end);
    return (str2.equalsIgnoreCase (str1sub));
  }
  
  /**
    A more tolerant substring function, that will adjust the start and
    end points, if necessary, to prevent an exception.
   
    @return substring with adjusted begin and end points if necessary
    @param inStr  String containing the desired substring.
    @param start  Starting point for substring extraction (inclusive)
    @param end    Ending point for substring extraction (exclusive)
   */
  public static String substringLenient (String inStr, int start, int end) {
    int start2 = start;
    if (start < 0) {
      start2 = 0;
    }
    int end2 = end;
    if (end > inStr.length()) {
      end2 = inStr.length();
    }
    return inStr.substring (start2, end2);
  }

  /**
    Takes a string encoded by encodeHTML and restores carriage
    returns, line feeds and double quotation marks.
    
    @return Input string, but with dangerous characters restored
            from their HTML equivalents.
    
    @param inString   String to be decoded.
   */
  public static String decodeHTML (String inString) {
    StringConverter sc = StringConverter.getMinimumHTML();
    sc.setForward (false);
    return sc.convert (inString);
    /*
    StringBuffer s = new StringBuffer (inString);
		char workChar = ' ';
    int i = 0;
		while (i < s.length()) {
			workChar = s.charAt (i);
      if (workChar == '<') {
        if (equalsSubstringIgnoreCase (s.toString(), i, HTML_LINE_BREAK)) {
          s.delete (i, i + HTML_LINE_BREAK.length());
          s.insert (i, GlobalConstants.CARRIAGE_RETURN_STRING
              + GlobalConstants.LINE_FEED_STRING);
          i++;
        }
      }
      else
      if (workChar == '&') {
        if (equalsSubstringIgnoreCase (s.toString(), i, HTML_DOUBLE_QUOTE)) {
          s.delete (i, i + HTML_DOUBLE_QUOTE.length());
          s.insert (i, "\"");
        } // end if double quote
      } // end if HTML entity
      i++;
		} // end while more characters to inspect
		return s.toString();
    */
	} // end of method decodeHTML
  
  /**
     Replaces a from String, when found in an input String, with a to
     String. All occurrences of the from String will be replaced. The from
     String and the to String may be different lengths. 
    
     @return Input string, but with any occurrences of the from string
             replaced with the to string.
    
     @param inString   String to be converted.
    
     @param fromString String to be replaced. 
    
     @param toString   Replacement string. 
   */
  public static String replaceString 
      (String inString, String fromString, String toString) {
    StringConverter sc = new StringConverter();
    sc.add (fromString, toString);
    return sc.convert (inString);
    /*
    int i = 0;
    int fromLength = fromString.length();
    int toLength = toString.length();
		StringBuffer s = new StringBuffer (inString);
    while (i <= (s.length() - fromLength)) {
      // System.out.println ("i = " + String.valueOf(i));
      // System.out.println ("fromLength = " + String.valueOf(fromLength));
      // System.out.println ("fromString = " + fromString);
      // System.out.println ("inString = " + inString);
      if (fromString.equals (s.substring (i, i + fromLength))) {
        s.replace (i, i + fromLength, toString);
        i = i + toLength;
      } else {
        i++;
      }
    }
		return s.toString();
    */
	} // end of method replaceString
  
  /**
     Compare two strings to see if they are approximately equal.
     
     @return True if the two strings are equal, ignoring case, ignoring punctuation
             and whitespace, ignoring missing vowels, ignoring 
             any "extra" characters in either string, and ignoring any
             missing characters if final significant characters match
             (so that "Street" and "St." will match, for example).
             
     @param  string1 First string to be compared.
     
     @param  string2 Second string to be compared.
    */
  public static boolean almostEqual (String string1, String string2) {
    // make sure s1 is the shorter of the two strings
    String s1;
    String s2;
    if (string2.length() < string1.length()) {
      s1 = string2;
      s2 = string1;
    } else {
      s1 = string1;
      s2 = string2;
    }
    // compare character by characer, ignoring insignificant characters
    int i = 0;
    int j = 0;
    char a;
    char b;
    boolean equal = true;
    int matchingChars = 0;
    boolean more = true;
    while (equal && more) {
      if (i >= s1.length() || j >= s2.length()) {
        more = false;
      } else {
        a = Character.toLowerCase(s1.charAt(i));
        b = Character.toLowerCase(s2.charAt(j));
        if (a == b) {
          matchingChars++;
          i++;
          j++;
        }
        else
        if ((! Character.isLetterOrDigit(a))
            && (! Character.isLetterOrDigit(b))) {
          i++;
          j++;
        } 
        else 
        if ((! Character.isLetterOrDigit(a))
            || (VOWELS.indexOf(a) >= 0)) {
          i++;
        }
        else
        if ((! Character.isLetterOrDigit(b)) 
            || (VOWELS.indexOf(b) >= 0)) {
          j++;
        }
        else {
          equal = false;
        }
      } // end if two more characters to compare
    } // end while more characters to consider
    
    // If we are unequal, check for possible Street/St. match
    // (All significant characters in shorter string match,
    // except for last, which matches last significant character
    // in longer string).
    if ((! equal)
        && (i > 0)
        && ((s1.length() - i) <= 2)) {
      int x = s1.length() - 1;
      if (! Character.isLetterOrDigit(s1.charAt(x))) {
        x--;
      }
      if (x == i) {
        int y = s2.length() - 1;
        if (! Character.isLetterOrDigit(s2.charAt(y))) {
          y--;
        }
        if (s1.charAt(x) == s2.charAt(y)) {
          equal = true;
        } // end if last significant characters are equal
      } // end if first unequal character in shorter string 
        // was also last significant character
    } // end if possible conditions for Street/St. match
    
    // Make sure we have at least one matching character to return a match
    if (equal && (matchingChars < 1)) {
      equal = false;
    }
    return equal;
  } // end method almostEqual
  
  /**
     Searches for a substring within a string, ignoring case. 
    
     @return -1 if the substring is not found, otherwise the starting
             position of the substring within the string.
    
     @param findStringUpper The substring we are looking for, in all upper-case.
   
     @param findStringLower The substring we are looking for, in all lower-case.
    
     @param searchString    The string we are searching. 
    
     @param start           The starting position for the search. Zero will 
                            cause the entire string to be searched. 
   */
  public static int indexOfIgnoreCase 
      (String findStringLower, String findStringUpper, 
          String searchString, int start) {
    int s = start;
    if (s < 0) {
      s = 0;
    }
    int len = findStringLower.length();
    int x = searchString.length() - len + 1;
    int i, j;
    boolean matched = false;
    while (s >= 0 && (s < x) && (! matched)) {
      if (searchString.charAt (s) == findStringLower.charAt (0)
          || searchString.charAt (s) == findStringUpper.charAt (0)) {
        i = s + 1;
        j = 1;
        matched = true;
        while (matched && j < len) {
          if (searchString.charAt (i) == findStringLower.charAt (j)
              || searchString.charAt (i) == findStringUpper.charAt (j)) {
            i++;
            j++;
          } else {
            matched = false;
          } // end if not matched
        } // end while still matching
      } // end if first character found
      if (! matched) {
        s++;
      }
    } // end while more characters to search
    if (! matched) {
      s = -1;
    }
		return s;
	} // end of method indexOfIgnoreCase
  
  /**
   Converts a string to an HTML ID, removing white space, 
   removing any odd characters, and making all letters lower-case. Added
   for consistency with Marked app. 
  
   @param in The string to be converted to an HTML ID. 
  
   @return The resulting HTML ID. 
  */
  public static String makeID (String in) {

    StringBuffer out = new StringBuffer();
    char c;
    int ci;
    for (int i = 0; i < in.length(); i++) {
      c = in.charAt (i);
      ci = c;
      // System.out.println ("  c = " + c + " (" + 
      //     String.valueOf (ci) + ")");
      if (ci > 127) {
        // ignore funny characters
      }
      else
      if (Character.isLetter (c)) {
        out.append (Character.toLowerCase (c));
      }
      else
      if (Character.isDigit (c)) {
        out.append (c);
      } 
    } // end for each character in input string
    return out.toString();
  }
  
  /**
   Converts a string to a conventional, universal file name, changing spaces
   to dashes, removing any odd characters, making all letters lower-case, and
   converting white space to hyphens.
  
   @param in The string to be converted to a file name. 
   @param stopAtColon Stop processing the input string when 
                      a colon is encountered?
  
   @return The resulting file name. 
  */
  public static String makeFileName (String in, boolean stopAtColon) {
    // System.out.println ("makeFileName");
    // System.out.println ("  in = " + in);
    StringBuffer out = new StringBuffer();
    char c;
    int ci;
    boolean done = false;
    boolean whiteSpace = true;
    for (int i = 0; ((i < in.length()) && (! done)); i++) {
      c = in.charAt (i);
      ci = c;
      // System.out.println ("  c = " + c + " (" + 
      //     String.valueOf (ci) + ")");
      if (ci > 127) {
        // ignore funny characters
      }
      else
      if (Character.isLetter (c)) {
        out.append (Character.toLowerCase (c));
        whiteSpace = false;
      }
      else
      if (Character.isDigit (c)) {
        out.append (c);
        whiteSpace = false;
      } 
      else
      if (c == ' ' || c == '_' || c == FILE_NAME_WORD_SEPARATOR) {
        if (whiteSpace) {
          // do nothing -- don't want multiple separators in a row
        } else {
          out.append (FILE_NAME_WORD_SEPARATOR);
          whiteSpace = true;
        }
      }
      else
      if ((c == ':') && (stopAtColon)) {
        done = true;
      }
    } // end for each character in input string
    if (out.length() > 0) {
      if (out.charAt (out.length() - 1) == FILE_NAME_WORD_SEPARATOR) {
        out.deleteCharAt (out.length() - 1);
      }
    }
    return out.toString();
  }
  
  /**
   Converts a string to a human readable file name, allowing mixed-case 
   and spaces and dashes, removing any odd characters.
  
   @param in The string to be converted to a file name. 
  
   @return The resulting file name. 
  */
  public static String makeReadableFileName (String in) {
    // System.out.println ("makeFileName");
    // System.out.println ("  in = " + in);
    StringBuilder out = new StringBuilder();
    char c;
    char lastIn = ' ';
    char lastOut = ' ';
    char nextChar = ' ';
    int ci;
    int i = 0;
    if (in.startsWith("http://")) {
      i = 7;
    }
    while (i < in.length()) {
      
      // Gather data about our position in the strings
      c = in.charAt (i);
      if (in.length() > (i + 1)) {
        nextChar = in.charAt(i + 1);
      } else {
        nextChar = ' ';
      }
      ci = c;
      if (out.length() > 0) {
        lastOut = out.charAt(out.length() - 1);
      }
      
      // System.out.println ("  c = " + c + " (" + 
      //     String.valueOf (ci) + ")");
      if (ci > 127) {
        // ignore funny characters
      }
      else
      if (Character.isLetter (c)) {
        out.append (c);
      }
      else
      if (Character.isDigit (c)) {
        out.append (c);
      } 
      else
      if (c == ' ' && lastOut == ' ') {
        // avoid consecutive spaces
      }
      else
      if (c == ':' && lastIn == ':') {
        // avoid consecutive colons
      }
      else
      if (c == ' ' || c == '_' || c == '-') {
        out.append (c);
      } 
      else
      if (c == '\''
          || c == '('
          || c == ')'
          || c == '['
          || c == ']'
          || c == '{'
          || c == '}') {
        // Let's just drop some punctuation
      }
      else
      if (c == '/') {
        if (lastOut != ' ') {
          out.append(' ');
        }
      }
      else
      if (c == '.'
          && ((i + 3) < in.length())
          && (in.substring(i + 1, i + 4).equals("com"))) {
        i = i + 3; // Drop .com
      }
      else
      if (c == '&') {
        if (lastOut != ' ') {
          out.append(' ');
        }
        out.append("and ");
      }
      else
      if (out.length() > 0) {
        // if (out.charAt(out.length() - 1) != ' ') {
        //   out.append (' ');
        // }
        if (nextChar == ' ' && lastOut != ' ') {
          out.append(' ');
        }
        out.append (FILE_NAME_WORD_SEPARATOR);
      }
      lastIn = c;
      i++;
    } // end for each character in input string
    if (out.length() > 0) {
      if (out.charAt (out.length() - 1) == FILE_NAME_WORD_SEPARATOR) {
        out.deleteCharAt (out.length() - 1);
      }
    }
    return out.toString();
  }
  
  /**
    Prints the passed String to the passed PrintStream, making Line Feed
    and Carriage Return characters visible.
    
    @param out PrintStream, such as System.out.
    @param str String to be printed, making line endings visible.
   */
  public static void println (PrintStream out, String str) {
    StringConverter sc = StringConverter.getCrLfVisible();
    out.println (sc.convert(str));
    /*
    StringBuffer line;
    int index = 0;
    int lineStart = 0;
    int lineEnd = 0;
    while (index < str.length()) {
      int lineEndLength = 1;
      char c = ' ';
      char d = ' ';
      char x = ' ';
      lineEnd = index;
      boolean endOfLine = false;
      while (! endOfLine) {
        if (lineEnd >= str.length()) {
          endOfLine = true;
        } else {
          c = str.charAt (lineEnd);
          if (c == GlobalConstants.CARRIAGE_RETURN) {
            x = GlobalConstants.LINE_FEED;
            endOfLine = true;
          }
          else
          if (c == GlobalConstants.LINE_FEED) {
            x = GlobalConstants.CARRIAGE_RETURN;
            endOfLine = true;
          }
          if (endOfLine) {
            if ((lineEnd + 1) < str.length()) {
              d = str.charAt (lineEnd + 1);
              if (d == x) {
                lineEndLength = 2;
              }
            }
          } else { 
            // not end of line
            lineEnd++;
          }
        } // end if another char to look at
      } // end while not end of line
      lineStart = index;
      if (lineEnd < lineStart) {
        lineEnd = str.length();
      }
      if (lineEnd >= lineStart) {
        index = lineEnd + lineEndLength;
        line = new StringBuffer (str.substring (lineStart, lineEnd));
        if (c == GlobalConstants.CARRIAGE_RETURN) {
          line.append ("<CR>");
        }
        else
        if (c == GlobalConstants.LINE_FEED) {
          line.append ("<LF>");
        }
        if (d == GlobalConstants.CARRIAGE_RETURN) {
          line.append ("<CR>");
        }
        else
        if (d == GlobalConstants.LINE_FEED) {
          line.append ("<LF>");
        }
        out.println (line.toString());
      } else {
        // do nada
      } // end if no line to return
    } // end if more of input string left
    */
  } // end println method
  
  /**
   Convert a URL to an HTML anchor tag with that URL as the href value. 
  
   @param  text The text containing the URL. 
  
   @return The anchor tag. 
   */
  public static String convertLinks (String text) {
    StringBuffer out = new StringBuffer (text);
    int i = 0;
    int l = 7;
    while (i >= 0 && i < out.length()) {
      int j = out.indexOf ("http://", i);
      if (j < 0) {
        j = out.indexOf ("https://", i);
        l = 8;
      }
      if (j >= 0) {
        int k = j + l;
        char c = ' ';
        boolean done = false;
        while (! done) {
          if (k >= out.length()) {
            done = true;
          } else {
            c = out.charAt (k);
            if (c == ' '
                || c == '\r'
                || c == '\n'
                || c == '\t'
                || c == '<') {
              done = true;
            } else {
              k++;
            } // end if not a stopper character
          } // end if we have more characters in string
        } // end while trying to find end of link
        if (out.charAt (k - 1) == '.') {
          k = k - 1;
        }
        String url = out.substring (j, k);
        String anchor = "<a href=\"" + url + "\" target=\"ref\">";
        out.insert (j, anchor);
        k = k + anchor.length();
        String endAnchor = "</a>";
        out.insert (k, endAnchor);
        i = k + endAnchor.length();
      } else {
        i = j;
      }
    }
    return out.toString();
  }
	
} // end of StringUtils class
