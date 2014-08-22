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
  import java.text.*;
  import java.util.*;
 
/**
 A financial transaction. <p>

 @author Herb Bowie
 */
public class CalcTransaction {
  
  public final static NumberFormat CURRENCY_FORMAT
      = NumberFormat.getCurrencyInstance(Locale.US);

  /**
   The date on which the financial transaction occurred, in Year-Month-Day format.
   */
  private String date = "";
  
  /**
   The Check number or other identifier associated with the transaction. 
  */
  private String checkNumber = "";

  /**
   Identified whether this is an income or an expense item.
   */
  private String incomeExpense = "Income";

  /**
   The person or company from which we received money, or to whom we paid money.
   */
  private String fromTo = "";
 
  /**
   A description of the item.
   */
  private String paidFor = "";

  /**
   The amount of the transaction.
   */
  private BigDecimal amount = new BigDecimal(0);
  
  /**
   The title of the event with which this transaction is associated. 
  */
  private String what = "";

  /**
   A constructor without any arguments.
   */
  public CalcTransaction() {

  }

  /**
    Return a string value representing the given item.
 
    @return The string by which this item shall be known.

   */
  public String toString() {

    StringBuilder str = new StringBuilder();

    return str.toString();
  }
 
  /**
     Sets the date for this event transaction.
 
     @param  date The date for this event transaction.
   */
  public void setDate (String date) {
    this.date = date;
  }

  /**
    Determines if the date for this event transaction is null.
 
    @return True if the date for this event transaction is not null.
   */
  public boolean hasDate () {
    return (date != null);
  }

  /**
    Determines if the date for this event transaction
    is null or is empty.
 
    @return True if the date for this event transaction
    is not null and not empty.
   */
  public boolean hasDateWithData () {
    return (date != null && date.length() > 0);
  }

  /**
    Returns the date for this event transaction.
 
    @return The date for this event transaction.
   */
  public String getDate () {
    return date;
  }
  
  /**
   Sets the check number for this event transaction. 
  
   @param checkNumber The check number or other identifier associated with
                      the transaction.
  */
  public void setCheckNumber (String checkNumber) {
    this.checkNumber = checkNumber;
  }
  
  /**
   Determine whether check number is null. 
  
   @return true if check number not null; false otherwise.
  */
  public boolean hasCheckNumber() {
    return (checkNumber != null);
  }
  
  /**
   Determine whether we have a check number or not. 
  
   @return true if check number not null and not empty string. 
  */
  public boolean hasCheckNumberWithData() {
    return (checkNumber != null && checkNumber.length() > 0);
  }
  
  /**
   Return the check number or other identifier associated with this transaction.
  
   @return Check number or other identifier.  
  */
  public String getCheckNumber() {
    return checkNumber;
  }
 
  /**
     Sets the income expense for this event transaction.
 
     @param  incomeExpense The income expense for this event transaction.
   */
  public void setIncomeExpense (String incomeExpense) {
    String incExpLowerCase = incomeExpense.toLowerCase();
    if (incomeExpense.length() > 0
        && incExpLowerCase.charAt(0) == 'e') {
      this.incomeExpense = "Expense";
    } else {
      this.incomeExpense = "Income";
    }
  }

  /**
    Determines if the income expense for this event transaction is null.
 
    @return True if the income expense for this event transaction is not null.
   */
  public boolean hasIncomeExpense () {
    return (incomeExpense != null);
  }

  /**
    Determines if the income expense for this event transaction
    is null or is empty.
 
    @return True if the income expense for this event transaction
    is not null and not empty.
   */
  public boolean hasIncomeExpenseWithData () {
    return (incomeExpense != null && incomeExpense.length() > 0);
  }

  /**
    Returns the income expense for this event transaction.
 
    @return The income expense for this event transaction.
   */
  public String getIncomeExpense () {
    return incomeExpense;
  }
 
  /**
     Sets the from to for this event transaction.
 
     @param  fromTo The from to for this event transaction.
   */
  public void setFromTo (String fromTo) {
    this.fromTo = fromTo;
  }

  /**
    Determines if the from to for this event transaction is null.
 
    @return True if the from to for this event transaction is not null.
   */
  public boolean hasFromTo () {
    return (fromTo != null);
  }

  /**
    Determines if the from to for this event transaction
    is null or is empty.
 
    @return True if the from to for this event transaction
    is not null and not empty.
   */
  public boolean hasFromToWithData () {
    return (fromTo != null && fromTo.length() > 0);
  }

  /**
    Returns the from to for this event transaction.
 
    @return The from to for this event transaction.
   */
  public String getFromTo () {
    return fromTo;
  }
 
  /**
     Sets the paid for for this event transaction.
 
     @param  paidFor The paid for for this event transaction.
   */
  public void setPaidFor (String paidFor) {
    this.paidFor = paidFor;
  }

  /**
    Determines if the paid for for this event transaction is null.
 
    @return True if the paid for for this event transaction is not null.
   */
  public boolean hasPaidFor () {
    return (paidFor != null);
  }

  /**
    Determines if the paid for for this event transaction
    is null or is empty.
 
    @return True if the paid for for this event transaction
    is not null and not empty.
   */
  public boolean hasPaidForWithData () {
    return (paidFor != null && paidFor.length() > 0);
  }

  /**
    Returns the paid for for this event transaction.
 
    @return The paid for for this event transaction.
   */
  public String getPaidFor () {
    return paidFor;
  }
 
  /**
     Sets the amount for this event transaction.
 
     @param  amount The amount for this event transaction.
   */
  public void setAmount (BigDecimal amount) {
    this.amount = amount;
  }

  /**
    Returns the amount for this event transaction as a string.
 
    @return The amount for this event transaction as a string.
   */
  public String getAmountAsString () {
    if (hasAmount()) {
      return CURRENCY_FORMAT.format(amount.doubleValue());
    } else {
      return "";
    }
  }

  /**
    Determines if the amount for this event transaction is null.
 
    @return True if the amount for this event transaction is not null.
   */
  public boolean hasAmount () {
    return (amount != null);
  }

  /**
    Determines if the amount for this event transaction
    is null or is empty.
 
    @return True if the amount for this event transaction
    is not null and not empty.
   */
  public boolean hasAmountWithData () {
    return (amount != null && (amount.compareTo(BigDecimal.ZERO) != 0));
  }

  /**
    Returns the amount for this event transaction.
 
    @return The amount for this event transaction.
   */
  public BigDecimal getAmount () {
    return amount;
  }
  
  public void setWhat (String what) {
    this.what = what;
  }
  
  public String getWhat() {
    return what;
  }

}
