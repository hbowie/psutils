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
 Build a string with reasonable spacing, dropping leading and trailing spaces,
 and reducing internal runs of spaces to a single space per run.

 @author Herb Bowie
 */
public class TextBuilder {
  
  private StringBuilder s = new StringBuilder();
  private boolean       allDigits = true;
  private boolean       whiteSpace = false;
  
  public TextBuilder() {
    
  }
  
  public TextBuilder (String t) {
    append (t);
  }
  
  public StringBuilder append(TextBuilder t) {
    return append (t.toString());
  }
  
  public StringBuilder append (String str) {
    for (int i = 0; i < str.length(); i++) {
      append (str.charAt(i));
    }
    return s;
  }
  
  public StringBuilder append (char c) {
    if (Character.isWhitespace(c)) {
      if (s.length() == 0) {
        // Drop leading spaces
      } else {
        whiteSpace = true;
      }
    } else {
      if (! Character.isDigit(c)) {
        allDigits = false;
      }
      if (whiteSpace) {
        s.append(' ');
        whiteSpace = false;
      }
      s.append(c);
    }
    return s;
  }
  
  public StringBuilder get() {
    return s;
  }
  
  public boolean isEmpty() {
    return (s.length() == 0);
  }
  
  public int length() {
    return s.length();
  }
  
  public String toString() {
    return s.toString();
  }
  
  public boolean isAllDigits() {
    return allDigits;
  }

}
