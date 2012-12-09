package com.powersurgepub.psutils;

  import java.text.*;
  import java.util.*;

/**
 Representation of a full or partial date as a string, with constituent fields
 going from major (year) to minor. 

 @author Herb Bowie
 */
public class StringDate {
  
  public static final     String[] MONTH_NAMES = {
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December"
  };
  
  private    StringBuilder    year1 = new StringBuilder();
  private    StringBuilder    year2 = new StringBuilder();
  private    StringBuilder    opYear = new StringBuilder();
  
  private    boolean          future = false;
  public     static final String FUTURE = "future";
  public     static final String NEXT_YEAR = "next year";
  private    Calendar         today = Calendar.getInstance();
  private    int              currentYear;
  private    int              currentMonth;
  
  private    StringBuilder    word = new StringBuilder();
  private    boolean          numbers = false;
  private    boolean          letters = false;
  private    boolean          colon = false;
  private    boolean          lookingForTime = false;
  
  private    String           yyyy = "";
  private    String           mm = "";
  private    String           dd = "";
  private    StringBuilder    start = new StringBuilder();
  private    StringBuilder    end = new StringBuilder();
  
  private    SimpleDateFormat shortFormat = new SimpleDateFormat("EEE MMM dd");
  
  public StringDate() {
    currentYear = today.get(Calendar.YEAR);
    currentMonth = today.get(Calendar.MONTH);
    // System.out.println("StringDate constructor current year = "
    //     + String.valueOf(currentYear)
    //     + ", month = " 
    //     + String.valueOf(currentMonth));
  }
  
  /**
   Parse a field containing an operating year.
  
   @param years This could be a single year, or a range containing two
                consecutive years. If a range, months of July or later
                will be assumed to belong to the earlier year, with months
                of June or earlier will be assumed to belong to the 
                later year. 
  
   @return True if an operating year was found in the passed String.
  */
  public boolean parseOpYear (String years) {

    year1 = new StringBuilder();
    year2 = new StringBuilder();

    int i = 0;
    int yearCount = 0;
    while (i < years.length()) {
      char c = years.charAt(i);
      if (Character.isDigit(c)) {
        if (yearCount == 0 && year1.length() < 4) {
          year1.append(c);
        } 
        else 
        if (yearCount == 1 && year2.length() < 4) {
          year2.append(c);
        }
      } 
      else
      if (year1.length() > 0 && yearCount == 0) {
        yearCount++;
      }
      else
      if (year2.length() > 0 && yearCount == 1) {
        yearCount++;
      }
      i++;
    } // end while more chars to examine
    
    // Assume 21st century, if not specified
    if (year1.length() == 2) {
      year1.insert(0, "20");
    }
    if (year2.length() == 2) {
      year2.insert(0, "20");
    }

    opYear = new StringBuilder();
    if (year1.length() > 0) {
      opYear.append(year1);
      if (year2.length() > 0) {
        opYear.append(" - ");
        opYear.append(year2);
      }
      // System.out.println("  Op year = " 
      //     + year1.toString()
      //     + " - "
      //     + year2.toString());
    }
    
    return (year1.length() == 4);
  }
  
  /**
   If the status of an item is "Future", then adjust the year to be a future 
   year. 
  
   @param futureStr The status of an item. If it says "Future", then 
                    adjust the year to be a future year. 
  */
  public void setFuture(String futureStr) {
    String futureLower = futureStr.toLowerCase();
    if (futureLower.indexOf(FUTURE) >= 0) {
      setFuture(true);
    }
    else
    if (futureLower.indexOf(NEXT_YEAR) >= 0) {
      setFuture(true);
    } 
    else {
      setFuture(false);
    } 
  }
  
  /**
   If this is a future item, then adjust the year to be a future 
   year. 
  
   @param future If true, then adjust the year to be a future year. 
  */
  public void setFuture(boolean future) {
    this.future = future;
    // System.out.println("  Set future to " + String.valueOf(future));
  }
  
  /**
   Returns the operating year, which could be a single year, or a range
   of consecutive years. 
  
   @return Operating year. 
  */
  public String getOpYear() {
    return opYear.toString();
  }
  
  /**
   Parse a human-readable date string.
  
   @param when Human-readable date string.
  */
  public void parse(StringBuilder when) {
    parse(when.toString());
  }
  
  /**
   Parse a human-readable date string.
  
   @param when Human-readable date string. 
  */
  public void parse(String when) {

    yyyy = "";
    mm = "";
    dd = "";
    start.setLength(0);
    end.setLength(0);
    lookingForTime = false;

    int i = 0;
    resetWhenWord();

    while (i <= when.length()) {
      
      // Get next character
      char c;
      if (i < when.length()) {
        c = when.charAt(i);
      } else {
        c = ' ';
      }
      
      // Now parse into words
      if (numbers && c == ':') {
        lookingForTime = true;
        colon = true;
        word.append(c);
      }
      else
      if (Character.isDigit(c)) {
        if (letters) {
          processWhenLetters();
        }
        numbers = true;
        word.append(c);
      }
      else
      if (Character.isLetter(c)) {
        if (numbers) {
          processWhenNumbers();
        }
        letters = true;
        word.append(c);
      } else {
        // Not a digit or a letter, so interpret as punctuation or white space. 
        if (letters && word.length() > 0) {
          processWhenLetters();
        }
        else
        if (numbers && word.length() > 0) {
          processWhenNumbers();
          if (c == ',' && dd.length() > 0) {
            lookingForTime = true;
          }
        }
      }
      i++;
    } // end while more length characters

    // Fill in year if not explicitly stated
    if (yyyy.length() == 0 && mm.length() > 0) {
      int month = Integer.parseInt(mm);
      if (year2.length() > 0 && month < 7) {
        yyyy = year2.toString();
      } else {
        yyyy = year1.toString();
      }
      int year = 2012;
      try {
        year = Integer.parseInt(yyyy);
      } catch (NumberFormatException e) {
        // do nothing
      }
      while (future
          && ((year < currentYear)
            || (year == currentYear && month <= currentMonth))) {
        year++;
        yyyy = zeroPad(year, 4);
      }
    }
    // System.out.println("  Parse " + getYMD() + " from " + when);
  } // end parse method
  
  /**
   End of a string of letters -- process it now. 
   */
  private void processWhenLetters() {
    if (word.toString().equalsIgnoreCase("at")
        || word.toString().equalsIgnoreCase("from")) {
      lookingForTime = true;
    }
    else
    if (word.toString().equalsIgnoreCase("am")
        || word.toString().equalsIgnoreCase("pm")) {
      if (end.length() > 0) {
        end.append(" ");
        end.append(word);
      } 
      else 
      if (start.length() > 0) {
        start.append(" ");
        start.append(word);
      }
    } 
    else
    if (mm.length() > 0) {
      // Don't overlay the first month, if a range was supplied
    } else {
      boolean found = false;
      int m = 0;
      while (m < MONTH_NAMES.length && (! found)) {
        if (MONTH_NAMES[m].toLowerCase().startsWith(word.toString().toLowerCase())) {
          found = true;
        } else {
          m++;
        }
      }
      if (found) {
        mm = zeroPad(m + 1, 2);
      }
    } // end if word might be a month
    resetWhenWord();
  }
  
  public static String zeroPad(int in, int desiredLength) {
    return zeroPad(String.valueOf(in), desiredLength);
  }
  
  public static String zeroPad(String in, int desiredLength) {
    StringBuilder padded = new StringBuilder(in);
    while (padded.length() < desiredLength) {
      padded.insert(0, " ");
    }
    while (padded.length() > desiredLength) {
      padded.deleteCharAt(0);
    }
    for (int i = 0; i < padded.length(); i++) {
      if (Character.isWhitespace(padded.charAt(i))) {
        padded.replace(i, i + 1, "0");
      }
    }
    return padded.toString();
  }
  
  /**
   End of a number string -- process it now. 
   */
  private void processWhenNumbers() {
    int number = 0;
    if (! colon) {
      number = Integer.parseInt(word.toString());
    }
    if (number > 2000) {
      yyyy = word.toString();
    }
    else
    if (lookingForTime) {
      if (start.length() == 0) {
        start.append(word);
      } else {
        end.append(word);
      }
    } else {
      if (number > 31
          || (mm.length() > 0 && dd.length() > 0) && word.length() > 3) {
        yyyy = word.toString();
      }
      else
      if (number > 12
          || mm.length() > 0) {
        dd = word.toString();
      } else {
        mm = word.toString();
      }
    }
    resetWhenWord();
  }
  
  private void resetWhenWord() {
    word.setLength(0);
    numbers = false;
    letters = false;
    colon = false;
  }
  
  /**
   Return the parsed date in a year-month-day format.
  
   @return Parsed date in y-m-d format. 
  */
  public String getYMD() {
    StringBuilder ymd = new StringBuilder();
    if (yyyy.length() > 0) {
      ymd.append(yyyy);
      if (mm.length() > 0) {
        ymd.append("-");
        ymd.append(zeroPad(mm, 2));
        if (dd.length() > 0) {
          ymd.append("-");
          ymd.append(zeroPad(dd, 2));
        }
      }
    }
    return ymd.toString();
  }
  
  /**
   Get a short but easily human-readable version of the date, consisting 
   of a three-letter abbreviation for the day of the week, a three-letter
   abbreviation for the month plus the day of the month (year is assumed to 
   be known or understood by the user).
  
   @return A short, easily human-readable version of the date. 
  */
  public String getShort() {

    Calendar cal = Calendar.getInstance();
    int goodDateParts = 0;
    
    if (yyyy.length() > 0) {
      try {
        cal.set(Calendar.YEAR, Integer.parseInt(yyyy));
        goodDateParts++;
      } catch (NumberFormatException e) {
        // No joy
      }
    }
    
    if (mm.length() > 0) {
      try {
        cal.set(Calendar.MONTH, Integer.parseInt(mm) - 1);
        goodDateParts++;
      } catch (NumberFormatException e) {
        // Bummer
      }
    }
    
    if (dd.length() > 0) {
      try {
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dd));
        goodDateParts++;
      } catch (NumberFormatException e) {
        // No good
      }
    }
    
    if (goodDateParts >= 3) {
      return shortFormat.format(cal.getTime());
    } else {
      StringBuilder shortDate = new StringBuilder();
      try {
        int month = Integer.parseInt(mm);
        if (month >= 1 && month <= 12) {
          shortDate.append(MONTH_NAMES[month - 1].substring(0, 3));
          shortDate.append(" ");
          shortDate.append(dd);
          return shortDate.toString();
        } else {
          return "";
        }
      } catch (NumberFormatException e) {
        return "";
      }
    }
  }

}
