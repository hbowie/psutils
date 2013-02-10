/*
 * Copyright 2004 - 2013 Herb Bowie
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
   An object representing a time duration, in hours and minutes. <p>
 */
public class TimeDuration {
  
  /** 
    Time duration in minutes. A negative value indicates "N/A" or
    "Not Applicable".
   */
  private   int       duration     = -1;
  
  /** Creates a new instance of TimeDuration */
  public TimeDuration() {
  }
  
  /** Creates a new instance of TimeDuration */
  public TimeDuration(String duration) {
    set (duration);
  }
  
  /**
    Sets the time duration value from a String.
    
    @param duration Time duration formatted as a string.
   */
  public void set (String duration) {

    int hours = 0;
    int minutes = 0;
    boolean na = false;
    int timeType = 1;
    int time = 0;
    char c = ' ';
    for (int i = 0; i < duration.length(); i++) {
      c = Character.toLowerCase (duration.charAt (i));
      if (c == 'h' || c == ':') {
        hours = time;
        timeType = 1;
        time = 0;
      }
      else
      if (c == 'n' || c == '/' || c == 'a') {
        na = true;
      }
      else
      if (Character.isDigit (c)) {
        time = (time * 10) + Character.getNumericValue (c);
      }  
    } // end for each character in input parameter
    if (time > 0) {
      minutes = time;
    } // end if more time to process
    
    if (hours == 0
        && minutes == 0
        && na) {
      this.duration = -1;
    } else {
      this.duration = (hours * 60) + minutes;
    }
  }

  /**
    Sets the time duration value from a total number of minutes.
    
    @param duration Time duration in total number of minutes.
   */
  public void set (int duration) {
    this.duration = duration;
  }
  
  /**
    Returns time duration as a total number of minutes.
   
    @return Total number of minutes.
   */
  public int get () {
    return duration;
  }
  
  public String toString() {
    if (duration >= 0) {
      int hours = duration / 60;
      int minutes = duration % 60;
      String h = String.valueOf (hours);
      String m = String.valueOf (minutes);
      if (m.length() == 0) {
        m = "00";
      }
      else
      if (m.length() == 1) {
        m = "0" + m;
      }
      return h + ":" + m;
    } else {
      return "N/A";
    }
  }
  
}
