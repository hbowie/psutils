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

  import java.lang.Object;
  import java.lang.String; 
             
/**
   A piece of data to be logged. 
   It is passed as an object. It might typically represent
   one record from a text file being processed. The log options
   settings will determine if and when each LogData object is 
   written to the log. 
 */

public class LogData {
  
  /** The data to be optionally written to the log. */
  private  Object      data;
  
  /** An identifier of the source of the data being logged. */
  private  String      sourceId;
  
  /** A number indicating the relative sequence of this log data within the data source. */
  private  int         sequenceNumber;

  /**
     The LogData "noarg" constructor.
   */
  public LogData () {
    this ("", "", 0);
  } // end LogData constructor
  
  /**
     The LogData constructor with all arguments passed.
    
     @param data            the data record being processed
     
     @param sourceId        a string identifying the source of the data (file name, etc.)
    
     @param sequenceNumber  an integer identifying the sequence of the data 
                            (as it appears in the source file, typically)
   */
  public LogData (Object data, String sourceId, int sequenceNumber) {
    setData (data);
    setSourceId (sourceId);
    setSequenceNumber (sequenceNumber);
  } // end LogData constructor
  
  /**
     Sets the data to be logged.
    
     @param data            the data record being processed
   */
  public void setData (Object data) {
    this.data = data;
  }
  
  /**
     Sets the source identifier for the data.
     
     @param sourceId        a string identifying the source of the data (file name, etc.)
   */
  public void setSourceId (String sourceId) {
    this.sourceId = sourceId;
  }
  
  /**
     Sets the sequence number for the data to be logged.
    
     @param sequenceNumber  an integer identifying the sequence of the data 
                            (as it appears in the source file, typically)
   */
  public void setSequenceNumber (int sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }
  
  /**
     Returns the object as a String.
    
     @return The source ID plus the "#" sign plus the sequence number plus the log data.
   */
  public String toString () {
    return sourceId + " # " + String.valueOf (sequenceNumber) + ": " + data.toString();
  } // end toString method

} // end LogData class
