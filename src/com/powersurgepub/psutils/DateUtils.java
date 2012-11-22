package com.powersurgepub.psutils;

  import java.util.*;

/**
   A utility class containing methods to assist with 
   dates. <p>
     
   This code is copyright (c) 2003 by Herb Bowie of PowerSurge Publishing. 
   All rights reserved. <p>
   
   Version History: <ul><li>
      2003/10/04 - Created new class.
      
    </ul>
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">herb@powersurgepub.com</a>)<br>
    <br>  of PowerSurge Publishing (<A href="http://www.powersurgepub.com/">www.powersurgepub.com/</a>)
   @version 
      2003/11/30 -  Fixed bug that caused all dates in next month to be flagged as 
                    tomorrow.
 */
public class DateUtils {
  
  public final static int LATE      = -1;
  public final static int TODAY     =  0;
  public final static int TOMORROW  =  1;
  public final static int FUTURE    =  2; 
  
  /** Current date and time. */
  private static GregorianCalendar today;
  
  /** Tomorrow. */
  private static GregorianCalendar tomorrow;
  
  /**
     Default constructor.
   */
  public DateUtils() {
  }
  
  /**
     Refreshes current and tomorrow's dates.
   */
  public static void refresh () {
    setToday();
    setTomorrow();
  }
  
  /**
     Returns today's date. Date is only initialized once.
    
     @return Today's date.
   */
  public static GregorianCalendar getToday() {
    if (today == null) {
      setToday();
    }
    return today;
  }
  
  public static void setToday () {
    today = new GregorianCalendar();
  }
  
  /**
     Returns tomorrow's date. Date is only initialized once.
    
     @return Tomorrow's date.
   */
  public static GregorianCalendar getTomorrow() {
    if (tomorrow == null) {
      setTomorrow();
    }
    return tomorrow;
  }
  
  public static void setTomorrow () {
    tomorrow = new GregorianCalendar();
    tomorrow.add (GregorianCalendar.DATE, 1);
  }
  
  /**
   Indicate whether item is overdue, due today, tomorrow, or in the future.
   
   @return -1 if due before today,
            0 if due today, or
           +1 if due tomorrow, or
           +2 if due in the future or already done. 
   @param  done Indicates whether item is complete, and therefore not late.
   @param  date Date to be compared to today.
   */
  public static int getLateCode (boolean done, Date date) {
    int returnCode = 0;
    if (done) {
      returnCode = 2;
    } else {
      GregorianCalendar due = new GregorianCalendar();
      due.setTime (date);

      int todayYear = getToday().get (GregorianCalendar.YEAR);
      int tomorrowYear = getTomorrow().get (GregorianCalendar.YEAR);
      int dueYear = due.get (GregorianCalendar.YEAR);

      int todayMonth = getToday().get (GregorianCalendar.MONTH);
      int tomorrowMonth = getTomorrow().get (GregorianCalendar.MONTH);
      int dueMonth = due.get (GregorianCalendar.MONTH);

      int todayDay = getToday().get (GregorianCalendar.DAY_OF_MONTH);
      int tomorrowDay = getTomorrow().get (GregorianCalendar.DAY_OF_MONTH);
      int dueDay = due.get (GregorianCalendar.DAY_OF_MONTH);
      
      if (tomorrowYear == dueYear
          && tomorrowMonth == dueMonth
          && tomorrowDay == dueDay) {
        returnCode = TOMORROW;
      }
      else 
      if (todayYear == dueYear
          && todayMonth == dueMonth
          && todayDay == dueDay) {
        returnCode = TODAY;
      }
      else
      if ((todayYear > dueYear) 
          || (todayYear == dueYear
              && todayMonth > dueMonth)
          || (todayYear == dueYear
              && todayMonth == dueMonth
              && todayDay > dueDay)) {
        returnCode = LATE;
      } else {
        returnCode = FUTURE;
      }
      
      /* System.out.println ("Late Code = " + String.valueOf (returnCode)
          + " for " + String.valueOf(dueMonth + 1)
          + "/" + String.valueOf(dueDay)
          + "/" + String.valueOf(dueYear)); */
      
    } // end if undone
    return (returnCode);
  } // end method
  
  public static boolean differentDate (GregorianCalendar date1, 
      GregorianCalendar date2) {
    
    int year1 = date1.get (GregorianCalendar.YEAR);
    int year2 = date2.get (GregorianCalendar.YEAR);

    int month1 = date1.get (GregorianCalendar.MONTH);
    int month2 = date2.get (GregorianCalendar.MONTH);

    int day1 = date1.get (GregorianCalendar.DAY_OF_MONTH);
    int day2 = date2.get (GregorianCalendar.DAY_OF_MONTH);

    if (year1 == year2
        && month1 == month2
        && day1 == day2) {
      return false;
    } else {
      return true;
    }
  }
  
} // end class
