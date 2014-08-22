/*
 * Copyright 2012 - 2014 Herb Bowie
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
  import java.util.*;

/**
 A parser that will return transaction details plus a total. <p>

 The string is broken down into words. Words must be separated by spaces, 
 or some other form of white space. 

 @author Herb Bowie
 */
public class CalcParser {
  
  public final static BigDecimal MINUS_ONE = new BigDecimal(-1);
  
  private String     str     = "";
  private String     incomeExpense = "Income";
  private BigDecimal tranResult = new BigDecimal(0);
  private BigDecimal finalResult  = new BigDecimal(0);
  private List<CalcTransaction> transactions = new ArrayList();
  private CalcTransaction transaction = new CalcTransaction();
  private int        transactionIndex = 0;
  
  private int        i       = 0;
  private char       c       = ' ';
  private char       tranOperand = '+';
  private char       calcOperand = '+';
  private boolean    skipNextNumber = false;
  private StringBuilder word = new StringBuilder();
  
  private StringBuilder phrase = new StringBuilder();
  private int        phraseType = AMOUNT;
  private final static int AMOUNT = 0;
  private final static int DATE = 1;
  private final static int CHECK_NUMBER = 2;
  private final static int FROM_TO = 3;
  private final static int FOR = 4;
  
  private StringDate strDate = new StringDate();
  private String     defaultDate = "";
  
  /**
   Constructor with no arguments. 
  */
  public CalcParser() {
    
  }
  
  /**
   Constructor with a string containing numbers. 
  
   @param str A string containing a number or a calculation. 
  */
  public CalcParser (String str) {
    calc(str);
  }
  
  public CalcParser (String incomeExpense, String str) {
    setIncomeExpense(incomeExpense);
    calc(str);
  }
  
  public void setStringDate (StringDate strDate) {
    this.strDate = strDate;
  }
  
  public StringDate getStringDate() {
    return strDate;
  }
  
  public void setDefaultDate (String defaultDate) {
    this.defaultDate = defaultDate;
  }
  
  public void calc (String incomeExpense, String str) {
    setIncomeExpense(incomeExpense);
    calc(str);
  }
  
  public void setIncomeExpense(String incomeExpense) {
    String incExpLowerCase = incomeExpense.toLowerCase();
    if (incomeExpense.length() > 0
        && incExpLowerCase.charAt(0) == 'e') {
      this.incomeExpense = "Expense";
    } else {
      this.incomeExpense = "Income";
    }
  }
  
  /**
   Parse the passed string and prepare a BigDecimal result.
  
   @param str A string containing a number or a calculation. 
  
  */
  public void calc (String str) {
    
    // System.out.println(" ");
    // System.out.println("CalcParser.calc input = " + str);
    
    this.str = str;
    finalResult = new BigDecimal(0);
    transactions = new ArrayList();
    word = new StringBuilder();
    phrase = new StringBuilder();
    phraseType = AMOUNT;
    tranOperand = '+';
    if (str != null) {
      i = 0;
      while (i < str.length()) {
        buildTransaction();
      }
    }
    
    // System.out.println("StringUtils.calc output = " + finalResult.toPlainString());
  } // end method set

  private void buildTransaction() {
    transaction = new CalcTransaction();
    transaction.setDate(defaultDate);
    if (word.length() > 0) {
      tranOperand = word.charAt(0);
    }
    tranResult = new BigDecimal(0);
    calcOperand = '+';
    skipNextNumber = false;
    word = new StringBuilder();
    phrase = new StringBuilder();
    phraseType = AMOUNT;
    while (i < str.length()
        && (! word.toString().equals("+"))
        && (! word.toString().equals("-"))) {
      buildWord();
    }
    processPhrase();
    if (tranResult != BigDecimal.ZERO) {
      if (tranOperand == '-') {
        transaction.setAmount(tranResult.multiply(MINUS_ONE));
      } else {
        transaction.setAmount(tranResult);
      }
      transaction.setIncomeExpense(incomeExpense);
      transactions.add(transaction);
      finalResult = operate (finalResult, tranOperand, tranResult);
    }
  }
  
  private void buildWord() {
    
    getNextCharacter();

    word = new StringBuilder();
    int digits = 0;
    int nonDigits = 0;
    boolean negative = false;

    // skip past white space
    while (i < str.length() && Character.isWhitespace(c)) {
      i++;
      getNextCharacter();
    }

    // Now build the word
    while (i < str.length() 
        && (! Character.isWhitespace(c))
        && (! word.equals("#"))) {
      if (c == '$' || c == ',') {
        // skip it
      }
      else
      if (c == '.' || c == '+' || c == '-') {
        word.append(c);
      }
      else
      if (c == '(' && digits == 0 && nonDigits == 0) {
        negative = true;
      }
      else
      if (c == ')' && digits > 0 && nonDigits == 0 && negative) {
        // skip it
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
      getNextCharacter();
    }
    
    String wordLower = word.toString().toLowerCase();

    // System.out.println(" ");
    // System.out.println("StringUtils.calc word = " + word.toString());
    // System.out.println("          word length = " + String.valueOf(word.length()));
    // System.out.println("          # of digits = " + String.valueOf(digits));
    // System.out.println("      # of non-digits = " + String.valueOf(nonDigits));
    // System.out.println("              operand = " + String.valueOf(calcOperand));
    // System.out.println("     Skip next number?  " + String.valueOf(skipNextNumber));

    if (digits > 0 
        && nonDigits == 0
        && phraseType == CHECK_NUMBER) {
      phrase.append(word);
      processPhrase();
    }
    else
    if (wordLower.equals("in")
        || wordLower.equals("from")
        || wordLower.equals("to")) {
      processPhrase();
      phraseType = FROM_TO;
      addWordToPhrase();
    }
    else
    if (wordLower.equals("for")) {
      processPhrase();
      phraseType = FOR;
      addWordToPhrase();
    }
    else
    if (digits > 0 && nonDigits == 0 
        && phraseType != DATE && phraseType != CHECK_NUMBER) {
      processPhrase();
      try {
        BigDecimal number = new BigDecimal(word.toString());
        if (negative) {
          number = number.multiply(MINUS_ONE);
        }
        tranResult = operate (tranResult, calcOperand, number);
        // System.out.println ("  " + calcOperand + " " + word);
      } catch (NumberFormatException e) {
        System.out.println("Could not convert " + word + " to a number");
      }

      // Reset operand to default
      calcOperand = '+';

    }
    else
    if (word.equals("#")
        || wordLower.equals("check")
        || wordLower.equals("ck")
        || wordLower.equals("via")) {
      processPhrase();
      phraseType = CHECK_NUMBER;
    }
    else
    if (wordLower.equals("dc")
        || wordLower.equals("debit card")
        || wordLower.equals("visa")
        || wordLower.equals("cc")
        || wordLower.equals("credit card")) {
      processPhrase();
      phraseType = CHECK_NUMBER;
      phrase.append(word);
      processPhrase();
    }
    else
    if (word.length() == 2 && word.toString().equalsIgnoreCase("on")) {
      processPhrase();
      phraseType = DATE;
    }
    else
    if (word.length() == 1 && 
       (word.charAt(0) == '=' || 
        word.charAt(0) == 'x' || 
        word.charAt(0) == 'X' || 
        word.charAt(0) == '*' 
        // || word.charAt(0) == '-' 
        // || word.charAt(0) == '+'
        )) {
      calcOperand = word.charAt(0);
      if (calcOperand == '=') {
        tranResult = BigDecimal.ZERO;
      }
    } 
    else
    if (word.length() == 1
        && (word.charAt(0) == '-'
          || word.charAt(0) == '+')) {
      // do nothing - let transaction end
    } else {
      addWordToPhrase();
    }

  } // end method buildWord
  
  private void addWordToPhrase() {
    if (word.length() > 0) {
      if (phrase.length() > 0) {
        phrase.append(' ');
      }
      phrase.append(word);
      word = new StringBuilder();
    }
  }
  
  private void processPhrase() {
    if (phrase.length() > 0) {
      switch (phraseType) {
        case AMOUNT:
          break;
        case DATE:
          strDate.parse(phrase);
          transaction.setDate(strDate.getYMD());
          break;
        case CHECK_NUMBER:
          transaction.setCheckNumber(phrase.toString());
          break;
        case FROM_TO:
          transaction.setFromTo(phrase.toString());
          break;
        case FOR:
          transaction.setPaidFor(phrase.toString());
          break;
      }
      phrase = new StringBuilder();
    } // end if we have a phrase to process
    phraseType = AMOUNT;
  } // end method processPhrase
  
  /**
   Perform arithmetic. 
  
   @param number1 - The first number. 
   @param operand - The operand. 
   @param number2 - The second number. 
   @return The answer. 
  */
  private BigDecimal operate (BigDecimal number1, char operand, BigDecimal number2) {
    BigDecimal answer = new BigDecimal(0);
    if (operand == '+') {
      answer = number1.add(number2);
    }
    else
    if (operand == '-') {
      answer = number1.subtract(number2);
    }
    else
    if (operand == '=') {
      answer = number2;
    }
    else
    if (operand == 'x' || operand == 'X' || operand == '*') {
      answer = number1.multiply(number2);
    }
    return answer;
  }
  
  private void getNextCharacter() {
    if (i < str.length()) {
      c = str.charAt(i);
    } else {
      c = ' ';
    }
  }
  
  public BigDecimal getResult() {
    return finalResult;
  }
  
  public CalcTransaction nextTransaction() {
    if (hasMoreTransactions()) {
      transaction = transactions.get(transactionIndex);
      transactionIndex++;
      return transaction;
    } else {
      return null;
    }
  }
  
  public boolean hasMoreTransactions() {
    return (transactionIndex < transactions.size());
  }

}
