package com.powersurgepub.psutils;

/**
  A String Buffer that replaces runs of white space with single spaces. 
 */
public class PureText {
  
  private   StringBuffer      text = new StringBuffer();
  private   boolean           lastCharWhiteSpace = false;
  
  /** Creates a new instance of PureText */
  public PureText() {
  }
  
  public PureText (String in) {
    append (in);
  }
  
  public PureText (StringBuffer in) {
    append (in);
  }
  
  public void append (char [] ch, int start, int length) {
    
    StringBuffer chars = new StringBuffer();
    chars.append (ch, start, length);
    append (chars);
  } 
  
  public void append (StringBuffer in) {
    append (in.toString());
  }
  
  public void append (String in) {
    char c;
    boolean charWhiteSpace;
    for (int i = 0; i < in.length(); i++) {
      c = in.charAt (i);
      charWhiteSpace = Character.isWhitespace (c);
      if (charWhiteSpace) {
        if (lastCharWhiteSpace) {
          // do nothing
        } else {
          lastCharWhiteSpace = true;
          text.append (" ");
        }
      } else {
        text.append (c);
        lastCharWhiteSpace = false;
      }
    } // end for each passed character
  }
  
  public String getFirstFifty () {
    
    int end = text.length();
    String continued = "";
    if (text.length() >= 50) {
      continued = "...";
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
    return text.substring (0, end) + continued;
  }
  
  public int length() {
    return text.length();
  }
  
  public String toString () {
    return text.toString();
  }
  
}
