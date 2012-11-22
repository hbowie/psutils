package com.powersurgepub.psutils;

  import java.util.*;
  
/**
   A String and a pointer to a character within the string, along
   with methods to scan the string one character or token at a time. <p>
     
   This code is copyright (c) 1999-2000 by Herb Bowie of PowerSurge Publishing. 
   All rights reserved. <p>
    
   Version History: <ul><li>
     2002/12/19 - Added getNextString method. <li>
     2000/03/29 - Added ability to scan backwards (increment = -1). <li>
     2000/03/29 - Added ability to scan backwards (increment = -1). <li>
     2000/03/26 - Cleaned up code and documentation. </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
                                       herb@powersurgepub.com</a>)<br>
    <br>  of PowerSurge Publishing (<A href="http://www.powersurgepub.com/">
                                                      www.powersurgepub.com/</a>)
   @version 
     2002/12/20 - Added getNextWord method.
 */
public class StringScanner {

  public   static final char     DIGIT_CHAR = '#';
  public   static final char     LETTER_CHAR = 'A';
  public   static final char     PUNCT_CHAR = '.';
  public   static final char     SPACE_CHAR = ' ';
  public   static final String   WHITE_SPACE = " ";
  public   static final String   DEFAULT_DELIMITERS = " ,;:\t\r\n";

  /** The String to be scanned. */
  private    String    str;
  
  /** The increment to use -- either +1 or -1. */
  private    int      increment;
  
  /** The length of the String to be scanned. */
  private    int      lng;
  
  /** A pointer to a position in the String. This is incremented as the scan progresses. */
  private    int      index;
  
  /** the next character in the passed String */
  private    char    nextChar;
  
  /** The type of nextChar. */
  private    char    nextCharType;
  
  /** If nextChar is a number, then this field will hold its numeric value. */
  private    int      nextCharDigitValue;
  
  /** The delimiter that ended the last field extracted. */
  private    char    endingDelimiter;
  
  /** Indicates if the field being extracted began with a quotation mark. */
  private    boolean    quoted;
  
  /** Holds the type of quotation mark (single or double) that began the field. */
  private    char    quoteChar;
  
  /** delimiters used to indicate end of a field */
  private    String    delimiters = DEFAULT_DELIMITERS;  
  
  /** The String broken into apparent words with spaces separating. */
  private		 String		 wordStr;
  
  /** A pointer to a position in the word string, incremented as the scan progresses. */
  private		 int			 wordIndex;
  
  /** 
     Tests the class. This method is called by <code>psutilsTest.main</code>.
   */
  public static void test () {

    System.out.println (" ");
    System.out.println (" ");
    System.out.println ("Testing class StringScanner");
    System.out.println ("Testing method extractInteger");
    testMDY ("050551");
    testMDY ("5/5/51");
    testMDY ("01-01-00");
    testMDY ("02-28-2000");
    System.out.println (" ");
    System.out.println ("Testing method extractQuotedString");
    StringScanner test2 = new StringScanner ("'parm 1', parm2 \"parm 3\"");
    System.out.println ("Parm 1 = " + test2.extractQuotedString());
    System.out.println ("Parm 2 = " + test2.extractQuotedString());
    System.out.println ("Parm 3 = " + test2.extractQuotedString());
    System.out.println ("End of line = " + test2.noMoreChars());
    System.out.println (" ");
    System.out.println ("Testing method getNextString");
    StringScanner test3 = new StringScanner ("docs/published/in_work");
    System.out.println ("Str 1 = " + test3.getNextString("/"));
    System.out.println ("Str 2 = " + test3.getNextString("/"));
    System.out.println ("Str 3 = " + test3.getNextString("/"));
    System.out.println ("Str 4 = " + test3.getNextString("/"));
    System.out.println ("Testing method getNextWord");
    StringScanner test4 = new StringScanner ("thisIs-a Test");
    System.out.println ("Word 1 = " + test4.getNextWord());
    System.out.println ("Word 2 = " + test4.getNextWord());
    System.out.println ("Word 3 = " + test4.getNextWord());
    System.out.println ("Word 4 = " + test4.getNextWord());
    System.out.println ("Word 5 = " + test4.getNextWord());
  } // end of test method

  /**
     Tests the ability to extract integers from date strings.
     @param  String  a test String consisting of a month, a day and a year.
   */
  private static void testMDY (String test) {
    StringScanner test1 = new StringScanner (test);
    int month = test1.extractInteger (12);
    int day   = test1.extractInteger (31);
    int year  = test1.extractInteger (2100);
    System.out.println 
      (test1.getString() + " generates " + month + "/" + day + "/" + year);
  } // end of testMDY method
  
  /**
     Constructor requiring only a string, and assuming a forward scan.
    
     @param inStr  the string to be scanned, one character at a time.
   */
  public StringScanner (String str) {
    this (str, 1);
  } // end of StringScanner constructor
  
  /**
     An alternate constructor that requires an
     increment to use for the scan, as well as the string to be scanned.
    
     @param  str    the string to be scanned, one character at a time.
    
     @param  increment  the increment to be used for the scan: 
             either +1 or -1.
    
     @exception  IllegalArgumentException  if the increment is not + or -1.
   */
  public StringScanner (String str, int increment) 
      throws IllegalArgumentException {
    this.str = str;
    wordStr = StringUtils.wordSpace (str, false);
    lng = str.length();
    this.increment = increment;
    resetIndex ();
    if (! ((increment == 1) || (increment == -1))) {
      throw new IllegalArgumentException ("Increment must be +1 or -1");
    }
  }
  
  /**
    Parse a string to try to make a date out of it.
    
    @return Date composed from string, with some kind of punctuation between
            the date components.
    @param  inSeq String indicating the expected sequence of date components, 
                  using an "m" to indicate month, a "d" to indicate day, 
                  and a "y" to indicate year. Default is "mdy".
   */
  public Date getDate (String inSeq) {
    String seq;
    char id;
    int month = 1;
    int day = 1;
    int year = 1;
    if (inSeq.length() >= 3) {
      seq = inSeq.toLowerCase();
    } else {
      seq = "mdy";
    }
    for (int i = 0; i < 3; i++) {
      id = seq.charAt (i);
      if (id == 'm') {
        month = extractInteger (12);
      }
      else
      if (id == 'd') {
        day = extractInteger (31);
      }
      else
      if (id == 'y') {
        year = extractInteger (2200);
      }
    }
    if (year < 1900) {
      year = year + 2000;
    }
    month = month - 1;
    GregorianCalendar cal = new GregorianCalendar (year, month, day);
    return cal.getTime();
  }
  
  /**
     Returns the next integer in a string (such as a date). Increments the index
     until it finds a decimal digit. Stops when a 
     a character other than a digit is encountered. 
    
     @return      the desired integer
    
     @param  upper   The upper bound of an acceptable integer to be returned
   */  
  public int extractInteger (int upper) {
    stopAtCharType (DIGIT_CHAR);
    int work = 0;
    while ((moreChars()) 
      && (nextCharType == DIGIT_CHAR)
      && (((work * 10) + nextCharDigitValue) 
        <= upper)) {
      work = (work * 10) + nextCharDigitValue;
      incrementIndex();
    }
    return work;
  } // end of method extractInteger
  
  /**
     Returns the next delimited string (optionally quoted) 
     from a longer string.
    
     @return  the next parameter (surrounding quotation marks will be
              respected as the bounds for the string, but will not
              be included in the returned string).
   */  
  public String extractQuotedString () {
    // skip leading white space
    skipChars (WHITE_SPACE);
    // look for leading quotation mark (single or double)
    quoted = false;
    quoteChar = GlobalConstants.SPACE;
    if ((moreChars())
      && ((nextChar == GlobalConstants.SINGLE_QUOTE)
        || (nextChar == GlobalConstants.DOUBLE_QUOTE))) {
      quoted = true;
      quoteChar = nextChar;
      incrementIndex();
    }
    // set starting position of string to be extracted
    int start = index;
    // look for end of string to be extracted
    String delims = delimiters;
    if (quoted) {
      delims = GlobalConstants.EMPTY_STRING + quoteChar;
    }
    stopAtChars (delims);
    int end = index;
    if (quoted) {
      incrementIndex ();
    }
    endingDelimiter = nextChar;
    incrementIndex();
    skipChars (WHITE_SPACE);
    return substring (start, end);
  } // end of extractQuotedString method
  
  /**
     Returns the next sub-string, starting at current index position,
     and delimited by one of the characters passed. If character at
     current index is in the passed group of characters, then will start
     string at next position.
    
     @return  next sub-string, delimited by passed characters.
    
     @param charGroup  characters to be scanned for
   */
  public String getNextString (String charGroup) {
    if (nextCharInGroup(charGroup)) {
      incrementIndex();
    }
    int start = getIndex();
    while ((moreChars()) && (nextCharNotInGroup(charGroup))) {
      incrementIndex();
    }
    int finish = getIndex();
    return substring (start, finish);
  }
  
  /**
     Returns the next apparent word from the String, starting at current 
     index position, and delimited by white space, punctuation, or a transition
     from lower case to upper. 
    
     @return  next apparent word.
   */
  public String getNextWord () {
    if (wordIndex < wordStr.length()
        && wordStr.charAt (wordIndex) == ' ') {
      incrementIndex();
    }
    int start = wordIndex;
    while (wordIndex < wordStr.length() 
        && wordStr.charAt (wordIndex) != ' ') {
      incrementIndex();
    }
    int finish = wordIndex;
    if (wordIndex >= wordStr.length()) {
      index = lng;
    } else
    if (wordIndex < 0) {
      index = -1;
    }
    if (finish < start) {
      return wordStr.substring (finish, start);
    } else
    if (finish > start) {
      return wordStr.substring (start, finish);
    } else {
      return "";
    }
  }
  
  /**
     Returns a piece of the string being scanned.
    
     @return  desired substring, beginning with lower number and ending with higher,
          or a null string if the desired range makes no sense
    
     @param  start  beginning of string (either upper or lower bound)
    
     @param  end    end of string (either lower or upper bound)
   */
  public String substring (int start, int end) {
    int lower, upper;
    if (start > end) {
      upper = start + 1;
      lower = end + 1;
    } else {
      upper = end;
      lower = start;
    }
    if (lower >= lng) {
      return GlobalConstants.EMPTY_STRING;
    } else {
      return str.substring (lower, upper);
    }
  } // end of substring
  
  /**
     Skips the given character type, and sets 
     internal pointer index to a new value.
    
     @param charType  character type to be skipped
   */
  public void skipCharType (char charType) {
    while ((moreChars()) && (nextCharType == charType)) {
      incrementIndex();
    }
  } // end of method skipCharType
  
  /**
     Scans for a given character type, and sets the 
     internal pointer index to a new value
     when one of these characters is encountered.
    
     @param charType  character type to be scanned for
   */
  public void stopAtCharType (char charType) {
    while ((moreChars()) && (nextCharType != charType)) {
      incrementIndex();
    }
  } // end method stopAtCharType
  
  /**
     Skips the given list of characters, and sets the 
     internal pointer index to a new value.
    
     @param charGroup  characters to be skipped
   */
  public void skipChars (String charGroup) {
    while ((moreChars()) && (nextCharInGroup(charGroup))) {
      incrementIndex();
    }
  } // end of method skipChars
  
  /**
     Scans for the given list of characters, and sets 
     the internal pointer index to a new value
     when one of these characters is encountered.
    
     @param charGroup  characters to be scanned for
   */
  public void stopAtChars (String charGroup) {
    while ((moreChars()) && (nextCharNotInGroup(charGroup))) {
      incrementIndex();
    }
  } // end method stopAtChars
  
  /**
     Determines if nextChar is in the 
     group of characters passed as a string.
    
     @return         true if nextChar is in group
    
     @param  charGroup  group of characters, passed as a String
   */
  public boolean nextCharInGroup (String charGroup) {
    return ((charGroup.indexOf (nextChar)) > -1);
  } // end of method nextCharInGroup
  
  /**
     Determines if nextChar is in group of characters passed as a string.
    
     @return            true if nextChar is not in group
    
     @param  charGroup  group of characters, passed as a String
   */
  public boolean nextCharNotInGroup (String charGroup) {
    return ((charGroup.indexOf (nextChar)) <= -1);
  } // end of method nextCharNotInGroup
  
  /**
     Resets the index pointer index to the beginning of the string.
   */
  public void resetIndex () {
    if (increment > 0) {
      index = 0;
      wordIndex = 0;
    } else {
      index = lng - 1;
      wordIndex = wordStr.length() - 1;
    }
    setNextChar();
  }
  
  /**
     Increments the internal pointer by the increment (+1 or -1).</p>
   */
  public void incrementIndex () {
    incrementIndex (increment);
  }
    
  /**
     Decrements the internal pointer by 1.
   */
  public void decrementIndex () {
    incrementIndex (increment * -1);
  }
  
  /**
     Increments the internal pointer by the specified amount.
    
     @param plus   the number to be added to the current index pointer
   */
  public void incrementIndex (int plus) {
    index = (index + plus);
    wordIndex = (wordIndex + plus);
    if (index < -1) {
      index = -1;
    } else 
    if (index > lng) {
      index = lng;
    }
    if (wordIndex < -1) {
      wordIndex = -1;
    } else
    if (wordIndex > wordStr.length()) {
      wordIndex = wordStr.length();
    }
    setNextChar();
  }
  
  /**
     Returns type of next char.
    
     @return   character type (pattern character)
   */
  public char getNextCharType () {
    return nextCharType;
  }
  
  /**
     Returns next character in the string (at current pointer position index).
    
     @return  next character
   */
  public char getNextChar () {
    return nextChar;
  }
  
  /**
     Processes next character in the string, determining type of character.
   */
  private void setNextChar () {
    if (index >= 0 && index < str.length()) {
      nextChar = str.charAt (index);
    } else {
      nextChar = GlobalConstants.SPACE;
    }
    if (Character.isDigit (nextChar)) {
      nextCharType = DIGIT_CHAR;
      nextCharDigitValue = Character.getNumericValue (nextChar);
    } else
    if (Character.isLetter (nextChar)) {
      nextCharType = LETTER_CHAR;
    } else
    if (Character.isWhitespace (nextChar)) {
      nextCharType = SPACE_CHAR;
    } else {
      nextCharType = PUNCT_CHAR;
    }
  }
  
  /**
     Checks for end of string. If increment is positive, checks to see
     if pointer is at end. If increment is negative, checks to see if
     pointer is at beginning of the string. 
    
     @return true if not end of string
   */
  public boolean moreChars () {
    if (increment > 0) {
      return (index < lng);
    } else {
      return (index >= 0);
    }
  }
  
  /**
     Checks for end of string.
    
     @return true if end of string
   */
  public boolean noMoreChars () {
    if (increment > 0) {
      return (index >= lng);
    } else {
      return (index < 0);
    }
  }
  
  /**
     Returns pointer to current position in string.
    
     @return pointer to current character.
   */
  public int getIndex () { return index; }
  
  /**
     Returns this object as a String.
    
     @return  the original string used in the constructor. 
   */
  public String getString () { return str; }
  
} // end of class StringScanner
