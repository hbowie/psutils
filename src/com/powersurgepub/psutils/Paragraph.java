/*
 * Paragraph.java
 *
 * Created on December 30, 2005, 8:32 AM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.powersurgepub.psutils;

/**
 *
 * @author hbowie
 */
public class Paragraph {
  
  private boolean quoted = false;
  private StringBuffer text = new StringBuffer();
  int i = 0;
  
  /** Creates a new instance of Paragraph */
  public Paragraph() {
    
  }
  
  public Paragraph (String in, boolean ensureQuoted) {
    append (in, ensureQuoted);
  }
  
  public void append (String in, boolean ensureQuoted) {
    i = 0;
    if (ensureQuoted) {
      quoted = true;
    }
    while (i < in.length()) {
      // Replace tab characters with spaces
      if (StringUtils.equalsSubstringIgnoreCase 
          (in, i, GlobalConstants.TAB_STRING)) {
        text.append (GlobalConstants.SPACE_STRING);
        i++;
      }
      else
      // Replace apostrophe entity with character
      if (StringUtils.equalsSubstringIgnoreCase 
          (in, i, "&apos;")) {
        text.append (GlobalConstants.APOSTROPHE);
        i++;
      }
      else
      // Replace ampersand entity with equivalent character
      if (StringUtils.equalsSubstringIgnoreCase 
          (in, i, "&amp;")) {
        text.append ("&");
        i++;
      }
      else
      // Replace less than entity with equivalent character
      if (StringUtils.equalsSubstringIgnoreCase 
          (in, i, "&lt;")) {
        text.append ("<");
        i++;
      }
      else
      // Replace greater than entity with equivalent character
      if (StringUtils.equalsSubstringIgnoreCase 
          (in, i, "&gt;")) {
        text.append (">");
        i++;
      }
      else
      // See if we have a double quotation mark in any form
      if (isDoubleQuotationMarks (in, GlobalConstants.DOUBLE_QUOTE_STRING)) {
        // taken care of
      }
      else
      if (isDoubleQuotationMarks (in, "&quot;")) {
        // taken care of
      }
      else
      if (isDoubleQuotationMarks (in, "&#34;")) {
        // taken care of
      }
      else
      if (isDoubleQuotationMarks (in, "&ldquo;")) {
        // taken care of
      }
      else
      if (isDoubleQuotationMarks (in, "&#8220;")) {
        // taken care of
      }
      else
      if (isDoubleQuotationMarks (in, "&rdquo;")) {
        // taken care of
      }
      else
      if (isDoubleQuotationMarks (in, "&#8221;")) {
        // taken care of
      }
      else
      {
        text.append (in.charAt (i));
        i++;
      }
    } // end while more characters to process
  } // end method append
  
  private boolean isDoubleQuotationMarks (String in, String quot) {
    // See if we have a double quotation mark in the given form
    boolean found = false;
    if (StringUtils.equalsSubstringIgnoreCase (in, i, quot)) {
      found = true;
      i = i + quot.length();
      // If leading quotation mark, indicate paragraph as quoted. 
      if (text.length() < 1) {
        quoted = true;
      }
      else
      // If trailing quotation mark, and paragraph already marked as quoted,
      // then ignore it. 
      if (i >= text.length()) {
        if (quoted) {
          // Don't need to do anything
        } else {
          // Replace with " character
          text.append (GlobalConstants.DOUBLE_QUOTE);
        }
      } 
      else 
      // In middle of paragraph
      {
        text.append (GlobalConstants.DOUBLE_QUOTE);
      }
    } // end if a &quot; entity
    return found;
  } // end method isDoubleQuotationMark
  
  public String getFirstFifty () {
    int end = text.length();
    if (text.length() >= 50) {
      end = 50;
      char c = ' ';
      for (int i = 49; i > 24; i--) {
        c = text.charAt (i);
        if (c == ' ' && end == 50) {
          end = i;
        }
        else
        if (c == ',' || c == '-' || c == '.' || c == ';') {
          end = i;
        }
      } // end for each character from position 49 to 24
    } // end if text length greater than or equal to 50
    return text.substring (0, end);
  }
  
  public boolean isQuoted () {
    return quoted;
  }
  
  public String toString() {
    trimText();
    return text.toString();
  }
  
  public String toStringSansHTML() {
    trimText();
    return text.toString();
  }
  
  private void trimText() {
    int i = text.length() - 1;
    while (i > 0
        && text.charAt (i) == ' ') {
      text.deleteCharAt (i);
      i--;
    }
  }
  
}
