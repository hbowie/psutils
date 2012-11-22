package com.powersurgepub.psutils;

  import java.text.*;
	import java.util.*;

/**
   An object representing a time of day, in hours and minutes. <p>
  
   This code is copyright (c) 2004 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
      2004/01/20 - Originally written. 
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 
      2004/01/20 - Originally written. 
 */
public class TimeOfDay {
  
  // public final static String   TIME_FORMAT_STRING = "hh:mm aa";
  
  // public final static DateFormat TIME_FORMAT 
  //     = new SimpleDateFormat (TIME_FORMAT_STRING);
  
  private int hours = 0;
  
  private int minutes = 0;
  
  /** Creates a new instance of TimeOfDay */
  public TimeOfDay() {
  }
  
  /**
    Constructor with TimeOfDay input.
   
    @param Another TimeOfDay instance.
   */
  public TimeOfDay (TimeOfDay timeOfDay) {
    set (timeOfDay);
  }
  
  /**
    Constructor with Date input.
   */
  public TimeOfDay(Date timeOfDay) {
    set (timeOfDay);
  }
  
  /**
    Constructor with String input.
   */
  public TimeOfDay(String timeOfDay) {
    set (timeOfDay);
  }
  
  /**
    Sets the time of day from another TimeOfDay instance.
   
    @param timeOfDay Another instance of this class.
   */
  public void set (TimeOfDay timeOfDay) {
    setHours (timeOfDay.getHours());
    setMinutes (timeOfDay.getMinutes());
  }
  
  /**
    Sets the time of day from a Date instance.
   
    @param date A date with a time of day.
   */
  public void set (Date date) {
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTime (date);
    setHours (cal.get (Calendar.HOUR_OF_DAY));
    setMinutes (cal.get (Calendar.MINUTE));
  }
  
  /**
     Sets the time of day from a String. Will accurately recognize time
     in multiple formats. Unqualified numbers are assumed to be hours.
     Hours specified without an am/pm qualifier will be assumed to refer to the 
     afternoon, if they are less than 6.
 
     @param  timeOfDay A time of day formatted as a string.
   */
  public void set (String timeOfDay) {
    hours = 0;
    minutes = 0;
    int timeType = 0;
    int time = 0;
    int amPM = 0;
    char c = ' ';
    for (int i = 0; i < timeOfDay.length(); i++) {
      c = Character.toLowerCase (timeOfDay.charAt (i));
      if (c == 'h' || c == ':') {
        hours = time;
        timeType = 1;
        time = 0;
      }
      else
      if (c == 'a' || c == 'i') {
        amPM = 1;
      }
      else
      if (c == 'p' || c == 'o') {
        amPM = 2;
      }
      else
      if (Character.isDigit (c)) {
        time = (time * 10) + Character.getNumericValue (c);
      } 
    } // end for each character in input parameter
    
    if (time > 0) {
      if (timeType == 0) {
        hours = time;
      } else {
        minutes = time;
      }
    } // end if more time to process
    
    if (amPM == 2) { 
      // pm or noon
      if (hours < 12) {
        hours = hours + 12;
      }
    }
    else
    if (amPM == 0) { 
      // no am/pm/noon/midnight indicator found
      if (hours < 6 && hours > 0) {
        hours = hours + 12;
      }
    }
    else
    if (amPM == 1) {
      // am or midnight
      if (hours == 12) {
        hours = 0;
      }
    }
      
  } // end set method
  
  /**
     Gets the time of day in Date format.
   
     @param Input date, with year, month and day already specified.
   */
  public Date get (Date inDate) {
    GregorianCalendar work = new GregorianCalendar ();
    work.setTime (inDate);
    work.set (Calendar.HOUR_OF_DAY, hours);
    work.set (Calendar.MINUTE, minutes);
    return work.getTime();
  }
  
  /**
    Adds the time duration to this time of day.
   
    @param duration Time duration.
   */
  public void add (TimeDuration duration) {
    minutes = minutes + duration.get();
    while (minutes > 60) {
      hours++;
      minutes = minutes - 60;
    }
    while (hours > 23) {
      hours = hours - 24;
    }
  }
  
  /**
    Sets the number of hours, in a military format (0 - 23).
   */
  public void setHours (int hours) {
    this.hours = hours;
  }
  
  /**
    Returns the number of hours in a military format (0 - 23).
   */
  public int getHours () {
    return hours;
  }
  
  /**
    Sets the number of minutes after the hour (0 -59).
   */
  public void setMinutes (int minutes) {
    this.minutes = minutes;
  }
  
  /**
    Returns the number of minutes after the hour (0 -59).
   */
  public int getMinutes () {
    return minutes;
  }
  
  /**
   */
  public int compareTo (TimeOfDay time2) {
    if (hours < time2.getHours()) {
      return -1;
    }
    else
    if (hours > time2.getHours()) {
      return 1;
    }
    else
    if (minutes < time2.getMinutes()) {
      return -1;
    }
    else
    if (minutes > time2.getMinutes()) {
      return 1;
    } else {
      return 0;
    }
  }
  
  /**
    Determines if this time of day is equal to another.
   
    @return True if both are equal.
   
    @param  timeOfDay2 Time of day to compare to this one.
   */
  public boolean equals (TimeOfDay timeOfDay2) {
    return (hours == timeOfDay2.getHours()
        && minutes == timeOfDay2.getMinutes());
  }
  
  /**
    Returns the time of day formatted as a string.
   */
  public String toString() {
    int hrs = hours;
    String amPM = "AM";
    if (hours > 12) {
      hrs = hours - 12;
    }
    if (hours > 11) {
      amPM = "PM";
    }
    
    String h = String.valueOf (hrs);
    if (h.equals("0")) {
      h = "00";
    }
    
    String m = String.valueOf (minutes);
    if (m.length() == 0) {
      m = "00";
    }
    else
    if (m.length() == 1) {
      m = "0" + m;
    }
    return h + ":" + m + " " + amPM;
  }
  
}
