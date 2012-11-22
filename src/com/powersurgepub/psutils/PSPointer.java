package com.powersurgepub.psutils;

	import java.util.*;
  
/**
   An object representing a pointer to an element in an array. Used by PSCollection.<p>
  
   This code is copyright (c) 2003 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 2003/05/31 - Originally written.
 */

public class PSPointer {
	
	/** Pointer to an element in an array. */
  private int		pointer;

	/**
	   Constructor.
     
     @param pointer The integer pointer to be stored in this object.
	 */
	public PSPointer (int pointer) {
		this.pointer = pointer;
	}

  /**
	   Returns the pointer as an integer.
	  
	   @return interger value of the pointer.
	 */
  public int intValue () {
    return pointer;
  }
  	
	/**
	   Returns the object in string form.
	  
	   @return object formatted as a string
	 */
	public String toString() {
    return String.valueOf (pointer);
	}
  
} // end of class

