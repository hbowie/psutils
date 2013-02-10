/*
 * Copyright 1999 - 2013 Herb Bowie
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
 *
 * @author hbowie
 */
public class CommonMarkup {
  
  private              char                c = ' ';
  private              char                c2 = ' ';
  private              char                c3 = ' ';
  private              boolean             lastCharWasWhiteSpace = true;
  private              boolean             lastCharWasEmDash = false;
  private              boolean             whiteSpace        = true;
  private              boolean             whiteSpacePending = false;
  private              boolean             startingQuote = true;
  private              int                 emphasisPending = 0;
  private              char                lastEmphasisChar = ' ';
  private              boolean             lastCharWasEmphasis = false;
  private              boolean             outputHTML = true;
  private              boolean             outputXML = false;

  public CommonMarkup (String input, String output) {
    if (output.length() > 0
        && output.toLowerCase().charAt(0) == 'x') {
      outputHTML = false;
      outputXML  = true;
    } else {
      outputHTML = true;
      outputXML  = false;
    }
  }

  public String markup (String s, boolean lastCharWasWhiteSpace) {
    this.lastCharWasWhiteSpace = lastCharWasWhiteSpace;
    if (lastCharWasWhiteSpace) {
      whiteSpacePending = false;
    }
    startingQuote = lastCharWasWhiteSpace;
    emphasisPending = 0;

    StringBuilder t = new StringBuilder();
    if (whiteSpacePending) {
      t.append (' ');
      lastCharWasWhiteSpace = true;
      whiteSpacePending = false;
      startingQuote = true;
    }
    int j;
    int k;
    int nextSemi = 0;
    
    for (int i = 0; i < s.length(); i++) {
      c = s.charAt (i);
      j = i + 1;
      if (j < s.length()) {
        c2 = s.charAt (j);
      } else {
        c2 = ' ';
      }
      k = j + 1;
      if (k < s.length()) {
        c3 = s.charAt(k);
      } else {
        c3 = ' ';
      }
      if (nextSemi <= i && nextSemi >= 0) {
        nextSemi = s.indexOf (';', i + 1);
      }

      // If this is the second char in the -- sequence, then just let
      // it go by, since we already wrote out the em dash
      if (lastCharWasEmDash) {
        lastCharWasEmDash = false;
        lastCharWasWhiteSpace = false;
        startingQuote = false;
      }
      else

      // If we have white space, write out only one space
      if (c == ' '
          || c == '\t'
          || c == '\r'
          || c == '\n') {
        lastCharWasEmDash = false;
        startingQuote = true;
        if (lastCharWasWhiteSpace) {
          // do nothing
        } else {
          t.append (' ');
          lastCharWasWhiteSpace = true;
        }
      }
      else

      // If we have an en dash, replace it with the appropriate entity
      if (c == 'Ð' && outputHTML) {
        t.append ("&#8211;");
        lastCharWasWhiteSpace = false;
        lastCharWasEmDash = false;
        startingQuote = false;
      }
      else

      // If we have an en dash, replace it with the appropriate entity
      if (c == '-' && lastCharWasWhiteSpace && c2 == ' ' && outputHTML) {
        t.append ("&#8211;");
        lastCharWasWhiteSpace = false;
        lastCharWasEmDash = false;
        startingQuote = false;
      }
      else

      // If we have two dashes, replace them with an em dash
      if (c == '-' && c2 == '-' && outputHTML) {
        t.append ("&#8212;");
        lastCharWasWhiteSpace = false;
        lastCharWasEmDash = true;
        startingQuote = false;
      }
      else

      // If we have a double quotation mark, replace it with a smart quote
      if (c == '"' && (outputHTML)) {
        if (startingQuote) {
          t.append ("&#8220;");
        } else {
          t.append ("&#8221;");
        }
        lastCharWasWhiteSpace = false;
        lastCharWasEmDash = false;
        startingQuote = false;
      }
      else

      // If we have a double quotation mark, replace it with a entity
      if (c == '"' && (outputXML)) {
        t.append ("&quot;");
        lastCharWasWhiteSpace = false;
        lastCharWasEmDash = false;
        startingQuote = false;
      }
      else

      // If we have a single quotation mark, replace it with the appropriate entity
      if (c == '\'' && outputHTML) {
        if (startingQuote) {
          t.append ("&#8216;");
        } else {
          t.append ("&#8217;");
        }
        lastCharWasWhiteSpace = false;
        lastCharWasEmDash = false;
        startingQuote = false;
      }
      else

      // If we have a single quotation mark, replace it with the appropriate entity
      if (c == '\'' && outputXML) {
        t.append ("&apos;");
        lastCharWasWhiteSpace = false;
        lastCharWasEmDash = false;
        startingQuote = false;
      }
      else

      // if an isolated ampersand, replace it with appropriate entity
      if (c == '&' && (nextSemi < 0 || nextSemi > (i + 7))) {
        t.append ("&amp;");
        lastCharWasWhiteSpace = false;
        lastCharWasEmDash = false;
        startingQuote = false;
      }
      else

      // Check for certain forms of punctuation
      if (c == '(' || c == '[' || c == '{' || c == '/') {
        lastCharWasWhiteSpace = false;
        lastCharWasEmDash = false;
        startingQuote = true;
        t.append (c);
      }
      else
        
      if (c == '*' || c == '_') {
        if (lastCharWasEmphasis) {
          // If this is the second char in the emphasis sequence, then just let
          // it go by, since we already wrote out the appropriate html
          lastCharWasEmphasis = false;
        }
        else
        if (emphasisPending == 1 
            && c == lastEmphasisChar 
            && (! lastCharWasWhiteSpace)) {
          t.append("</em>");
          emphasisPending = 0;
        }
        else
        if (emphasisPending == 2
            && c == lastEmphasisChar
            && c2 == lastEmphasisChar
            && (! lastCharWasWhiteSpace)) {
          t.append("</strong>");
          emphasisPending = 0;
          lastCharWasEmphasis = true;
        }
        else
        if (emphasisPending == 0
            && c2 == c
            && (! Character.isWhitespace(c3))) {
          t.append("<strong>");
          emphasisPending = 2;
          lastCharWasEmphasis = true;
          lastEmphasisChar = c;
        }
        else
        if (emphasisPending == 0
            && (! Character.isWhitespace(c2))) {
          t.append("<em>");
          emphasisPending = 1;
          lastEmphasisChar = c;
        } else {
          lastCharWasWhiteSpace = false;
          lastCharWasEmDash = false;
          startingQuote = false;
          t.append(c);
        }
      }
      else

      // If nothing special, then just move it to the output
      {
        lastCharWasWhiteSpace = false;
        lastCharWasEmDash = false;
        startingQuote = false;
        t.append (c);
      }
    } // end for each char in text
    whiteSpace = lastCharWasWhiteSpace;
    return (t.toString());
  }

}
