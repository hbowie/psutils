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
   An object representing a pointer to an element in an array. Used by PSCollection.
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

