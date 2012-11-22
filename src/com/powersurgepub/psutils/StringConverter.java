package com.powersurgepub.psutils;

  import java.util.*;

/**
   An object that will replace a list of from Strings with
   corresponding to String within a passed String. <p>
  
   This code is copyright (c) 2004 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
      2004/08/23 - Created for use with Two Due.  <li>
       </ul>
  
   @author Herb Bowie of PowerSurge Publishing
  
   @version 2004/08/23 - Created for use with Two Due. 
 */
public class StringConverter {
  
  // public static final StringFromTo CR_LF_VISIBLE
  //     = new StringFromTo 
  //       (GlobalConstants.CARRIAGE_RETURN_STRING + GlobalConstants.LINE_FEED_STRING,
  //       "<CR><LF>" + GlobalConstants.LINE_FEED);
  public static final String CR_LF = 
      GlobalConstants.CARRIAGE_RETURN_STRING + GlobalConstants.LINE_FEED_STRING;
  
  public static final String CR_LF_VISIBLE =
      "<CR><LF>" + GlobalConstants.LINE_FEED;
  
  // public static final StringFromTo LF_CR_VISIBLE
  //     = new StringFromTo 
  //       (GlobalConstants.LINE_FEED_STRING + GlobalConstants.CARRIAGE_RETURN_STRING,
  //       "<LF><CR>" + GlobalConstants.LINE_FEED);
  
  public static final String LF_CR =
      GlobalConstants.LINE_FEED_STRING + GlobalConstants.CARRIAGE_RETURN_STRING;
  
  public static final String LF_CR_VISIBLE =
      "<LF><CR>" + GlobalConstants.LINE_FEED;
  
  // public static final StringFromTo CR_VISIBLE
  //     = new StringFromTo 
  //       (GlobalConstants.CARRIAGE_RETURN_STRING,
  //       "<CR>" + GlobalConstants.LINE_FEED);
  
  public static final String CR =
      GlobalConstants.CARRIAGE_RETURN_STRING;
  
  public static final String CR_VISIBLE =
      "<CR>" + GlobalConstants.LINE_FEED;
  
  // public static final StringFromTo LF_VISIBLE
  //     = new StringFromTo 
  //       (GlobalConstants.LINE_FEED_STRING,
  //       "<LF>" + GlobalConstants.LINE_FEED);
  
  public static final String LF =
      GlobalConstants.LINE_FEED_STRING;
  
  public static final String LF_VISIBLE =
      "<LF>" + GlobalConstants.LINE_FEED;
  
  // public static final StringFromTo CR_LF_TO_HTML
  //     = new StringFromTo 
  //       (GlobalConstants.CARRIAGE_RETURN_STRING + GlobalConstants.LINE_FEED_STRING,
  //       "<br>");
  
  public static final String HTML_BREAK = "<br>";
  
  // public static final StringFromTo LF_CR_TO_HTML
  //     = new StringFromTo 
  //       (GlobalConstants.LINE_FEED_STRING + GlobalConstants.CARRIAGE_RETURN_STRING,
  //       "<br>");
  
  // public static final StringFromTo CR_TO_HTML
  //     = new StringFromTo 
  //       (GlobalConstants.CARRIAGE_RETURN_STRING,
  //       "<br>");
  
  // public static final StringFromTo LF_TO_HTML
  //     = new StringFromTo 
  //       (GlobalConstants.LINE_FEED_STRING,
  //       "<br>",
  //       true);
  
  //public static final StringFromTo CR_LF_TO_HTML2
  //     = new StringFromTo 
  //       (GlobalConstants.CARRIAGE_RETURN_STRING + GlobalConstants.LINE_FEED_STRING,
  //       "<br/>");
  
  public static final String HTML_BREAK_2 = "<br/>";
  
  // public static final StringFromTo CR_LF_TO_HTML3
  //     = new StringFromTo 
  //       (GlobalConstants.CARRIAGE_RETURN_STRING + GlobalConstants.LINE_FEED_STRING,
  //       "<br />");
  
  public static final String HTML_BREAK_3 = "<br />";
  
  // public static final StringFromTo TAB_TO_SPACE
  //     = new StringFromTo 
  //       (GlobalConstants.TAB_STRING,
  //       " ");
  
  // public static final StringFromTo DOUBLE_QUOTE_TO_ML
  //     = new StringFromTo 
  //       ("\"", "&quot;");
  
  public static final String DOUBLE_QUOTE = "\"";
  
  public static final String DOUBLE_QUOTE_MARKUP = "&quot;";
  
  // public static final StringFromTo APOSTROPHE_TO_ML
  //     = new StringFromTo 
  //       ("'", "&apos;");
  
  public static final String APOSTROPHE = "'";
  
  public static final String APOSTROPHE_MARKUP = "&apos;";
  
  // public static final StringFromTo AMPERSAND_TO_ML
  //     = new StringFromTo 
  //       ("&", "&amp;");
  
  public static final String AMPERSAND = "&";
  
  public static final String AMPERSAND_MARKUP = "&amp;";
  
  // public static final StringFromTo LESS_THAN_TO_ML
  //     = new StringFromTo 
  //       ("<", "&lt;");
  
  public static final String LESS_THAN = "<";
  
  public static final String LESS_THAN_MARKUP = "&lt;";
  
  // public static final StringFromTo GREATER_THAN_TO_ML
  //     = new StringFromTo 
  //       (">", "&gt;");
  
  public static final String GREATER_THAN = ">";
  
  public static final String GREATER_THAN_MARKUP = "&gt;";
  
  
  
  private ArrayList list = new ArrayList();
  
  private boolean   forward = true;
  
  private boolean   listPrepared = false;
  
  private String    firstChars;
  
  private ArrayList firstCharFirstString;
  private ArrayList firstCharLastString;
  
  /** 
    Creates a new instance of StringConverter. 
   */
  public StringConverter() {
    
  }
  
  /** 
    Creates a new instance of StringConverter. 
   */
  public StringConverter (StringConverter anotherList) {
    list.addAll (anotherList.getList());
  }
  
  public void addAll (StringConverter anotherList) {
    list.addAll (anotherList.getList());
    checkPreparation (false);
  }
  
  public static StringConverter getCrLfVisible () {

    StringConverter makeCrLfVisible = new StringConverter();
    makeCrLfVisible.addCrLfVisible();
    return makeCrLfVisible;
  }
  
  /**
    Add rules to convert all carriage return/line feed combinations to plain
    ASCII markers plus single line feeds.
   */
  public void addCrLfVisible() {
    add (CR_LF, CR_LF_VISIBLE);
    add (LF_CR, LF_CR_VISIBLE);
    add (CR, CR_VISIBLE);
    add (LF, LF_VISIBLE);
  }
  
  public static StringConverter getMinimumHTML () {
    StringConverter convertMinimumHTML = new StringConverter();
    convertMinimumHTML.addMinimumHTML();
    return convertMinimumHTML;
  }
  
  /**
    Add rules to convert common problem characters into equivalent HTML.
   */
  public void addMinimumHTML() {
    add (CR_LF, HTML_BREAK);
    add (LF_CR, HTML_BREAK);
    add (CR,    HTML_BREAK);
    add (LF,    HTML_BREAK);
    add (CR_LF, HTML_BREAK_2);
    add (CR_LF, HTML_BREAK_3);
    add (DOUBLE_QUOTE, DOUBLE_QUOTE_MARKUP);
  }  
  
  public static StringConverter getXML () {
    StringConverter convertXML = new StringConverter();
    convertXML.addXML();
    return convertXML;
  }
  
  /**
    Add rules to convert characters to XML entities.
   */
  public void addXML() {
    add (DOUBLE_QUOTE, DOUBLE_QUOTE_MARKUP);
    add (APOSTROPHE,   APOSTROPHE_MARKUP);
    add (AMPERSAND,    AMPERSAND_MARKUP);
    add (LESS_THAN,    LESS_THAN_MARKUP);
    add (GREATER_THAN, GREATER_THAN_MARKUP);
  }

  
  /**
    Adds another from/to pair to the list of conversion strings.
   
    @param fromTo Another from/to pair to be added to the list.
   */
  public void add (StringFromTo fromTo) {
    list.add (fromTo);
    // System.out.println ("Adding " + fromTo.toString());
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
  
  /**
    Adds another from/to pair to the list of conversion strings.
   
    @param from From string.
    @param to   To string.
    @param preferred Is this pair preferred over others for the same from/to
                     string?
   */
  public void add (String from, String to, boolean preferred) {
    list.add (new StringFromTo (from, to, preferred));
    checkPreparation (false);
  }
  
  /**
    Sets the desired direction of conversion: forward (from -> to) or 
    backward (to -> from).
   
    @param forward True if the before string should have from strings replaced
                   with to strings, false if the to strings should be replaced
                   with from strings. 
   */
  public void setForward (boolean forward) {
    if (forward != isForward()) {
      for (int i = 0; i < list.size(); i++) {
        StringFromTo ft = (StringFromTo)list.get(i);
        ft.setForward (forward);
      }
      this.forward = forward;
      checkPreparation (false);
    } // end if direction is changing
  } // end method
  
  public boolean isForward() {
    return forward;
  }
  
  /**
    Convert an old string to a new string, using the list of from/to
    pairs already passed to this converter.
   
    @return         String after conversion.
    @param  before  String to be converted.
   */
  public String convert (String before) {
    checkPreparation (true);
    int beforeIndex = 0;
    int firstCharIndex = -1;
    char beforeChar = ' ';
    Integer firstFromIndexInteger;
    int firstFromIndex;
    Integer lastFromIndexInteger;
    int lastFromIndex;
    int fromIndex;
    StringFromTo fromTo = new StringFromTo (" ", " ");
    boolean foundMatch = false;
    StringBuffer after = new StringBuffer();
    while (beforeIndex < before.length()) {
      beforeChar = before.charAt (beforeIndex);
      firstCharIndex = firstChars.indexOf (beforeChar);
      if (firstCharIndex < 0) {
        
        // None of the from strings start with this character
        after.append (beforeChar);
        beforeIndex++;
        
      } else {
        firstFromIndexInteger 
          = (Integer)firstCharFirstString.get(firstCharIndex);
        firstFromIndex = firstFromIndexInteger.intValue();
        lastFromIndexInteger
          = (Integer)firstCharLastString.get(firstCharIndex);
        lastFromIndex = lastFromIndexInteger.intValue();
        fromIndex = firstFromIndex;
        foundMatch = false;
        while ((! foundMatch)
            && (fromIndex <= lastFromIndex)) {
          fromTo = get (fromIndex);
          if (fromTo.fromEquals (before, beforeIndex)) {
            foundMatch = true;
          } else {
            fromIndex++;
          }
        }
        if (foundMatch) {
          after.append (fromTo.getTo());
          beforeIndex = beforeIndex + fromTo.getFromLength();
        } else {
          
          // First character matched, but no match on complete from string
          after.append (beforeChar);
          beforeIndex++;
          
        } // end if no matching from string found
      } // end if got a match on first character 
    } // end  of characters in before string
    
    return after.toString();
  } // end convert method
  
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
    // System.out.println ("Starting StringConverter preparation");
    boolean swapped = true;
    int counter = 0;
    while (swapped) {
        // && counter < 20) {
      swapped = false;
      for (int i = 0, j = 1; j < list.size(); i++, j++) {
        StringFromTo ft1 = (StringFromTo)list.get(i);
        StringFromTo ft2 = (StringFromTo)list.get(j);
        if (ft1.compareTo (ft2) > 0) {
          list.set (i, ft2);
          list.set (j, ft1);
          // System.out.println ("Swapping...");
          // System.out.println ("  Before:");
          // System.out.println ("    " + String.valueOf (i) + " " + ft1.toString());
          // System.out.println ("    " + String.valueOf (j) + " " + ft2.toString());
          // System.out.println ("  After:");
          // System.out.println ("    " + String.valueOf (i) + " " + get(i).toString());
          // System.out.println ("    " + String.valueOf (j) + " " + get(j).toString());
          swapped = true;
        } // end if these two need to be swapped
        counter++;
      } // end of one pass through the list
    } // end of sorting
    
    // Now build array of first characters
    // System.out.println ("StringConverter building first character arrays");
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
    // System.out.println ("StringConverter Preparation done");
    
  } // end of method
  
  public Collection getList() {
    return list;
  }
  
  public StringFromTo get (int i) {
    return (StringFromTo)list.get(i);
  }
  
  public String toString () {
    StringBuffer str = new StringBuffer();
    for (int i = 0; i < list.size(); i++) {
      if (str.length() > 0) {
        str.append("; ");
      }
      str.append (get(i).toString());
    }
    return str.toString();
  }
  
  public static void test () {
    System.out.println ("Testing StringConverter");
    StringConverter sc1 = new StringConverter();
    testDisplay (sc1, "Line with old string");
    sc1.add ("old", "new");
    testDisplay (sc1, "Line with old string");
    testDisplay (sc1, "");
    sc1 = new StringConverter();
    sc1.add ("&", "&amp;");
    testDisplay (sc1, "This & that, then & now &");
    System.out.println (" ");
  }
  
  private static void testDisplay (StringConverter sc, String before) {
    sc.setForward (true);
    System.out.println ("Converter: " + sc.toString());
    String after = sc.convert (before);
    System.out.println ("Converting " + before + " to " + after);
    sc.setForward (false);
    System.out.println ("Converter: " + sc.toString());
    String before2 = sc.convert (after);
    System.out.println ("Converting " + after + " to " + before2);
    if (! before.equals (before2)) {
      System.out.println ("!!! Backwards conversion failed to recreate original. !!!");
    }
  }
  
}
