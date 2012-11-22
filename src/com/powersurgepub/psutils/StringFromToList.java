package com.powersurgepub.psutils;

  import java.util.*;

/**
   A list of String conversions. <p>
  
   This code is copyright (c) 2004 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
      2004/08/23 - Created for use with Two Due.  <li>
       </ul>
  
   @author Herb Bowie of PowerSurge Publishing
  
   @version 2004/08/23 - Created for use with Two Due. 
 */
public class StringFromToList {
  
  private ArrayList list = new ArrayList();
  
  private boolean   listPrepared = false;
  
  private String    firstChars;
  
  private ArrayList firstCharFirstString;
  private ArrayList firstCharLastString;
  
  /** 
    Creates a new empty instance of StringFromToList.
   */
  public StringFromToList() {
  }
  
  /** 
    Creates a new  instance of StringFromToList.
   
    @param anotherList Another StringFromToList to be appended to this one.
   */
  public StringFromToList (StringFromToList anotherList) {
    list.addAll (anotherList.getList());
  }
  
  public void addAll (StringFromToList anotherList) {
    list.addAll (anotherList.getList());
    checkPreparation (false);
  }
  
  /**
    Adds another from/to pair to the list of conversion strings.
   
    @param fromTo Another from/to pair to be added to the list.
   */
  public void add (StringFromTo fromTo) {
    list.add (fromTo);
    checkPreparation (false);
  }
  
  /**
    Adds another from/to pair to the list of conversion strings.
   
    @param from From string.
    @param to   To string.
   */
  public void add (String from, String to) {
    list.add (new StringFromTo (from, to));
    checkPreparation (false);
  }
  
  public void setForward (boolean forward) {
    for (int i = 0; i < list.size(); i++) {
      StringFromTo ft = (StringFromTo)list.get(i);
      ft.setForward (forward);
    }
    checkPreparation (false);
  }
  
  /**
    See if we need to prepare list for use. We only need to do this once,
    unless StringFromTo objects are added to the list after the initial
    preparation.
   
    @param expected True if we expect the list to be prepared, 
                    false otherwise. 
   */
  private void checkPreparation (boolean expected) {
    if (listPrepared && expected) {
      // List is ready and it should be: ok
    }
    else
    if ((! listPrepared) && (! expected)) {
      // list isn't ready and it doesn't need to be: ok
    } else {
      prepare();
    }
  }
  
  /**
    Prepare the list for use. This should be done once, after the contents
    of the list has been finalized. 
   */
  public void prepare () {
    
    // Sort the list: longer from strings go first
    boolean swapped = true;
    while (swapped) {
      swapped = false;
      for (int i = 0, j = 1; j < list.size(); i++, j++) {
        StringFromTo ft1 = (StringFromTo)list.get(i);
        StringFromTo ft2 = (StringFromTo)list.get(j);
        if (ft1.compareTo (ft2) > 0) {
          list.set (i, ft2);
          list.set (j, ft1);
          swapped = true;
        } // end if these two need to be swapped
      } // end of one pass through the list
    } // end of sorting
    
    // Now build array of first characters
    StringBuffer firstCharsBuff = new StringBuffer();
    firstCharFirstString = new ArrayList();
    firstCharLastString = new ArrayList();
    char lastFirstChar = ' ';
    char nextFirstChar = ' ';
    int firstCharIndex = -1;
    for (int i = 0; i < list.size(); i++) {
      StringFromTo ft = get(i);
      nextFirstChar = ft.getFromFirstChar();
      if ((i < 1)
          || (nextFirstChar != lastFirstChar)) {
        firstCharsBuff.append (nextFirstChar);
        firstCharIndex++;
        Integer stringIndex = new Integer (i);
        firstCharFirstString.add (stringIndex);
        firstCharLastString.add (stringIndex);
        lastFirstChar = nextFirstChar;
      } else {
        Integer stringIndex = new Integer (i);
        firstCharLastString.set (firstCharIndex, stringIndex);
      }
    }
    
    firstChars = firstCharsBuff.toString();

    listPrepared = true;
    
  } // end of method
  
  public Collection getList() {
    return list;
  }
  
  public StringFromTo get (int i) {
    return (StringFromTo)list.get(i);
  }
  
}
