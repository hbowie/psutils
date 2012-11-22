package com.powersurgepub.psutils;

/**
   A combination of a from String with a to String that should replace it. <p>
  
   This code is copyright (c) 2004 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
      2004/08/23 - Created for use with Two Due.  <li>
       </ul>
  
   @author Herb Bowie of PowerSurge Publishing
  
   @version 2004/08/23 - Created for use with Two Due. 
 */
public class StringFromTo 
    implements Comparable {
  
  private   String    from;
  private   String    to;
  private   boolean   preferred = false;
  private   boolean   forward = true;
  
  /** 
    Creates a new instance of StringFromTo. 
   
    @param from String to convert from.
    @param to   String to convert to.
   */
  public StringFromTo (String from, String to) {
    this.from = from;
    this.to = to;
  }
  
  /** 
    Creates a new instance of StringFromTo. 
   
    @param from String to convert from.
    @param to   String to convert to.
    @param preferred True if this conversion rule should take precedence over
                     others for the same string.
   */
  public StringFromTo (String from, String to, boolean preferred) {
    this.from = from;
    this.to = to;
    this.preferred = preferred;
  }
  
  /** 
    Sets the preferred flag. 

    @param preferred True if this conversion rule should take precedence over
                     others for the same string.
   */
  public void setPreferred (boolean preferred) {
    this.preferred = preferred;
  }
  
  /**
    Are we converting from to to fields, or going backwards (to back to from)?
   
    @param forward True if we are converting from ==> to, false if we are
                        converting to ==> from.
   */
  public void setForward (boolean forward) {
    this.forward = forward;
    // System.out.println ("StringFromTo.setForward to " + String.valueOf(forward));
  }
  
  /**
    Compare this from/to pair to another. Items are sorted based on the from 
    field (or the to field if going backwards). From fields are sorted by first
    characters, from field length (longer fields sort lower), then by the
    entie from field, and finally by preference.  
   
    @return -1 if this from field sorts lower than the other one;
            +1 if this from field sorts higher than the other one, otherwise
             0 if this from field equals the other one. 
   
    @param object Another StringFromTo instance to compare to this one.
   */
  public int compareTo (Object object2) {
    StringFromTo s2 = (StringFromTo)object2;
    if (getFromFirstChar() < s2.getFromFirstChar()) {
      return -1;
    }
    else
    if (getFromFirstChar() > s2.getFromFirstChar()) {
      return 1;
    }
    if (getFromLength() > s2.getFromLength()) {
      return -1;
    }
    else 
    if (getFromLength() < s2.getFromLength()) {
      return 1;
    }
    else {
      int stringResult = getFrom().compareTo (s2.getFrom());
      if (stringResult != 0) {
        return stringResult;
      } else {
        if (preferred) {
          return -1;
        } else {
          return 0;
        }
      } // end if from strings are identical
    } // end if first characters are the same and from lengths are the same
  } // end compareTo method
  
  public char getFromFirstChar () {
    if (getFromLength() < 1) {
      return ' ';
    } else {
      return getFrom().charAt(0);
    }
  }
  
  public boolean fromEquals (String s, int i) {
    int maxLength = s.length() - i;
    int fromLength = getFromLength();
    if (maxLength < fromLength) {
      return false;
    } else {
      return (getFrom().equals(s.substring (i, i + fromLength)));
    }
  }
  
  public int getFromLength () {
    return getFrom().length();
  }
  
  public String getFrom () {
    if (forward) {
      return from;
    } else {
      return to;
    }
  }
  
  public String getTo () {
    if (forward) {
      return to;
    } else {
      return from;
    }
  }
  
  public String toString() {
    return ("From " + from + (forward ? " ==> " : " <== ") + "to " + to);
  }
  
}
