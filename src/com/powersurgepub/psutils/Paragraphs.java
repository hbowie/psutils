/*
 * Copyright 2005 - 2013 Herb Bowie
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

  import com.powersurgepub.xos2.*;
  import java.util.*;

/**
 *
 * @author hbowie
 */
public class Paragraphs {
  
  public static final String LEFT_DOUBLE_QUOTATION_MARK = "&quot;";
  public static final String RIGHT_DOUBLE_QUOTATION_MARK = "&quot;";
  
  private XOS xos = XOS.getShared();
  
  private ArrayList paragraphs = new ArrayList();
  
  private boolean quotation = true;
  
  public Paragraphs () {
    
  }
  
  /** Creates a new instance of Paragraphs */
  public Paragraphs (String in, boolean ensureQuoted) {
    append (in, ensureQuoted);
  }
  
  public void set (String in) {
    paragraphs = new ArrayList();
    quotation = true;
    append (in, false);
  }
  
  public void set (String in, boolean ensureQuoted) {
    paragraphs = new ArrayList();
    quotation = true;
    append (in, ensureQuoted);
  }
  
  public void append (String in, boolean ensureQuoted) {
    // System.out.println ("Paragraphs append in=" + in 
    //     + " ensureQuoted=" + String.valueOf (ensureQuoted));
    int i = 0;
    while (i < in.length()) {
      // Process the next paragraph
      // System.out.println ("  Building paragraph # " 
      //       + String.valueOf (paragraphs.size()));
      StringBuffer para = new StringBuffer();
      int status = 0;
      int blankLine = 0;
      int consecutiveLineSeps = 0;
      while (i < in.length() && status < 2) {
      /*  System.out.println ("  Processing Character # " 
            + String.valueOf (i)
            + " value=" + in.charAt(i)
            + "para=" + para.toString()); */
        
        // See what kind of characters we have
        boolean paraDelim = false;
        boolean skip = false;
        boolean linesep = false;
        char n = in.charAt(i);
        if ((n == GlobalConstants.CARRIAGE_RETURN) 
            || (n == GlobalConstants.LINE_FEED)) {
          i++;
          skip = true;
          if (n == GlobalConstants.CARRIAGE_RETURN
              && (i < in.length()
              && in.charAt(i) == GlobalConstants.LINE_FEED)) {
            i++;
          }
          else
          if (n == GlobalConstants.LINE_FEED
              && (i < in.length()
              && in.charAt(i) == GlobalConstants.CARRIAGE_RETURN)) {
            i++;
          }
          consecutiveLineSeps++;
          if (consecutiveLineSeps > 1) {
            paraDelim = true;
            consecutiveLineSeps = 0;
          } // end if blank line between paragraphs
          else
          if ((para.length() > 0)
              && para.charAt (para.length() - 1) != ' ') {
            para.append(' ');
          }
        } // end if next character is a line separator
        else
        if (StringUtils.equalsSubstringIgnoreCase (in, i, "<p>")) {
          paraDelim = true;
          i = i + 3;
        }
        else
        if ((StringUtils.equalsSubstringIgnoreCase (in, i, "</p>"))
            || (StringUtils.equalsSubstringIgnoreCase (in, i, "<br>"))) {
          paraDelim = true;
          i = i + 4;
        } 
        else
        if (StringUtils.equalsSubstringIgnoreCase (in, i, "<br/>")) {
          paraDelim = true;
          i = i + 5;
        }
        
        if (paraDelim) {
          if (status == 1) {
            status = 2;
          } 
        } 
        else
        if (skip) {
          // 
        }
        else 
        if (in.charAt (i) == ' ' && para.length() == 0) {
          status = 1;
          i++;
        } 
        else
        if (consecutiveLineSeps == 1
            && in.charAt (i) == ' ') {
          status = 1;
          i++;
        }
        else {
          para.append (in.charAt (i));
          consecutiveLineSeps = 0;
          status = 1;
          i++;
        }
      } // end while not yet end of paragraph
      if (para.length() > 0) {
        Paragraph nextParagraph = new Paragraph (para.toString(), ensureQuoted);
        if (! nextParagraph.isQuoted()) {
          quotation = false;
        }
        paragraphs.add (nextParagraph);
      } // end if we have any content
    } // end while more characters to process
  } // end method append
  
  public String getFirstFifty () {
    if (paragraphs.size() < 1) {
      return "";
    } else {
      Paragraph p = (Paragraph)paragraphs.get(0);
      return p.getFirstFifty();
    }
  }
  
  /**
   Return text as a string, with HTML as applicable.
   */
  public String toString() {
    StringBuffer s = new StringBuffer();
    for (int i = 0; i < paragraphs.size(); i++) {
      Paragraph p = (Paragraph)paragraphs.get(i);
      String ps = p.toString();
      boolean list = false;
      if ((ps.length() > 4)
          && (ps.startsWith ("<ol>")
            || ps.startsWith ("<ul>")
            || ps.startsWith ("<li>")
            || ps.startsWith ("<OL>")
            || ps.startsWith ("<UL>")
            || ps.startsWith ("<LI>"))) {
        list = true; 
      } else {
        s.append ("<p>");
        if (p.isQuoted()) {
          s.append (LEFT_DOUBLE_QUOTATION_MARK);
        }
      }
      s.append (ps);
      if (! list) {
        if (p.isQuoted()) {
          if ((i + 1) > paragraphs.size()) {
            Paragraph nextParagraph = (Paragraph)paragraphs.get(i + 1);
            if (nextParagraph.isQuoted()) {
            // no closing quotation if quotation continues in next paragraph
            } else {
              s.append (RIGHT_DOUBLE_QUOTATION_MARK);
            }
          } else {
            s.append (RIGHT_DOUBLE_QUOTATION_MARK);
          }
        }
        s.append ("</p>");
      } // end if not part of a list
      s.append (xos.getLineSep());
    } // end for each paragraph
    return s.toString();
  }
  
  /**
   Return text as a string, without HTML.
   */
  public String toStringSansHTML() {
    StringBuffer s = new StringBuffer();
    for (int i = 0; i < paragraphs.size(); i++) {
      if (i > 0) {
        s.append (" " + XOS.getShared().getLineSep());
      }
      Paragraph p = (Paragraph)paragraphs.get(i);
      if (p.isQuoted()) {
        s.append ("\"");
      }
      s.append (p.toStringSansHTML());
      if (p.isQuoted()) {
        s.append ("\"");
      }
      s.append (XOS.getShared().getLineSep());
    } // end for each paragraph
    return s.toString();
  }
  
  public boolean isQuotation () {
    return quotation;
  }
  
  public Paragraph getParagraph (int i) {
    if (i < 0 || i >= getNumberOfParagraphs()) {
      return null;
    } else {
      return (Paragraph)paragraphs.get(i);
    }
  }
  
  public int getNumberOfParagraphs() {
    return paragraphs.size();
  }
  
}
