package com.powersurgepub.psutils;
  
/**
   An object representing a substring of a string. <p>
  
   This code is copyright (c) 2003 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 2003/04/10 - Originally written.
 */

public class SubSpec {
	
	/** 
     A one-character abbreviation indicating the type of data
     contained in the sub-string. 
   */
  private char		type					= ' ';
  
	/** The beginning position of the sub-string. */
  private int			start					= 0;  
  
  /** The length of the sub-string. */
  private int			length				= 0;
  
	/**
	   Constructor.
   
	 */
	public SubSpec () {

	}
  
	/**
	   Constructor.
   
     @param type   one-character abbreviation indicating the type of data.
     @param start  beginning position of the sub-string.
     @param length Length of the sub-string.
	 */
	public SubSpec (char type, int start, int length) {
		setType (type);
    setStart (start);
    setLength (length);
	}
  
  /**
     Returns a sub-string from the passed string.
     
     @param string String from which sub-string is to be extracted.
   */
  public String substring (String string) {
    if (start < 0) {
      start = 0;
    }
    if (start >= string.length()) {
      start = string.length() - 1;
    }
    if (length < 0) {
      length = 0;
    }
    if ((start + length) > string.length()) {
      length = string.length() - start;
    }
    return string.substring (start, (start + length));
  }

  /**
     Sets the type.
	   
	   @param type one-character abbreviation indicating the type of data.
	 */
  public void setType (char type) {
    this.type = Character.toLowerCase (type);
  }
  
  /**
     Gets the type.
	   
	   @return one-character abbreviation indicating the type of data.
	 */
  public char getType () {
    return type;
  }
  
  /**
     Sets the starting position.
	   
	   @param start the starting position of the sub-string.
	 */
  public void setStart (int start) {
    this.start = start;
  }
  
  /**
     Gets the starting position.
	   
	   @return starting position of the sub-string.
	 */
  public int getStart () {
    return start;
  }
  
  /**
     Sets the length.
	   
	   @param length the length of the sub-string.
	 */
  public void setLength (int length) {
    this.length = length;
  }
  
  /**
     Gets the length of the sub-string.
	   
	   @return length of the sub-string.
	 */
  public int getLength () {
    return length;
  }
  
  /**
     Tests to see if this object is equal to another, using
     the single-character type value (converted to lower case)
     as the sole comparator.
     
     @return if both specifications have the same identifying type.
     @param  spec2 Second substring specification to compare to
                      this one.
   */
  public boolean equals (SubSpec spec2) {
    return (getType() == spec2.getType());
  }
  	
	/*
	   Returns the object in string form.
	   
	   @return object formatted as a string
	 */
	public String toString() {
    return (String.valueOf(type) + ": "
      + String.valueOf(start) + ", "
      + String.valueOf(length));
	}
  
  /**
     Tests the class.
   */
  public static void test () {
    
    System.out.println(" ");
    System.out.println ("Testing Class SubSpec");
    testSubSpec ("abcdefg", 0, 7);
    testSubSpec ("abcdefg", -1, 1);
    testSubSpec ("abcdefg", 7, 3);
    testSubSpec ("abcdefg", 6, 3);
    testSubSpec ("abcdefg", 1, 3);
  }
  
  private static void testSubSpec (String s, int begin, int length) {
    SubSpec test = new SubSpec ('a', begin, length);
    System.out.println ("Substring of " + s  
      + " starting at " + String.valueOf(begin)
      + " for " + String.valueOf(length)
      + " is " + test.substring(s));
  }
  
}

