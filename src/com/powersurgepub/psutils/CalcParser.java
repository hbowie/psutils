/*
 * Copyright 2012 - 2013 Herb Bowie
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

import java.math.*;

/**
 A parser that will return transaction details plus a total. 

 @author Herb Bowie
 */
public class CalcParser {
  
  private String     str    = "";
  private BigDecimal result = BigDecimal.ZERO;
  
  /**
   Constructor with no arguments. 
  */
  public CalcParser() {
    
  }
  
  public CalcParser (String str) {
    calc(str);
  }
  
  /**
   Parse the passed string and return a BigDecimal result.
  
   @param str A string containing a number or a calculation. 
  
   @return The net result, as a BigDecimal.
  */
  public void calc (String str) {
    // System.out.println(" ");
    // System.out.println("StringUtils.calc input = " + str);
    this.str = str;
    result = BigDecimal.ZERO;
    if (str != null) {
      int i = 0;
      char c = ' ';
      if (i < str.length()) {
        c = str.charAt(i);
      }
      char operand = '+';
      boolean skipNextNumber = false;
      while (i < str.length()) {
        StringBuilder word = new StringBuilder();
        boolean endOfWord = false;
        int digits = 0;
        int nonDigits = 0;
        while (i < str.length() && (! endOfWord)) {

          // skip past white space
          while (i < str.length() && Character.isWhitespace(c)) {
            i++;
            if (i < str.length()) {
              c = str.charAt(i);
            } else {
              c = ' ';
            }
          }

          // Now build the word
          while (i < str.length() && (! Character.isWhitespace(c))) {
            if (c == '$' || c == ',') {
              // skip it
            }
            else
            if (c == '.' || c == '+' || c == '-') {
              word.append(c);
            }
            else
            if (Character.isDigit(c)) {
              digits++;
              word.append(c);
            } else {
              nonDigits++;
              word.append(c);
            }

            i++;
            if (i < str.length()) {
              c = str.charAt(i);
            } else {
              c = ' ';
            }

          }
          // System.out.println(" ");
          // System.out.println("StringUtils.calc word = " + word.toString());
          // System.out.println("          word length = " + String.valueOf(word.length()));
          // System.out.println("          # of digits = " + String.valueOf(digits));
          // System.out.println("      # of non-digits = " + String.valueOf(nonDigits));
          // System.out.println("              operand = " + String.valueOf(operand));
          // System.out.println("     Skip next number?  " + String.valueOf(skipNextNumber));
          endOfWord = true;
        } // end while more chars

        if (digits > 0 && nonDigits > 0) {
          // If it's a date or some other semi-number, make sure we reset the 
          // flag to skip the next number. 
          skipNextNumber = false;
        }
        else
        if (digits > 0 && nonDigits == 0) {
          if (skipNextNumber) {
            skipNextNumber = false;
          } else {
            try {
              BigDecimal number = new BigDecimal(word.toString());
              // System.out.println ("  " + operand + " " + word);
              if (operand == '+') {
                result = result.add(number);
              }
              else
              if (operand == '-') {
                result = result.subtract(number);
              }
              else
              if (operand == '=') {
                result = number;
              }
              else
              if (operand == 'x' || operand == 'X' || operand == '*') {
                result = result.multiply(number);
              }
            } catch (NumberFormatException e) {
              // No good
            }
            operand = '+';
          }
        }
        else
        if (word.length() == 1 && word.charAt(0) == '#') {
          // Don't treat a check number as an amount
          skipNextNumber = true;
        }
        else
        if (word.length() == 2 && word.toString().equalsIgnoreCase("on")) {
          skipNextNumber = true;
        }
        else
        if (word.length() == 1 && 
           (word.charAt(0) == '=' || 
            word.charAt(0) == 'x' || 
            word.charAt(0) == 'X' || 
            word.charAt(0) == '*' ||
            word.charAt(0) == '-' ||
            word.charAt(0) == '+')) {
          operand = word.charAt(0);
          if (operand == '=') {
            result = BigDecimal.ZERO;
          }
        }
      } // end while more words
    // System.out.println("StringUtils.calc output = " + result.toPlainString());
    }
  } // end method set
  
  public BigDecimal getResult() {
    return result;
  }

}
