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
  
/**
   An object representing a substring of a string. <p>
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

