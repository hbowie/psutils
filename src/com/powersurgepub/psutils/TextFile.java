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

  import com.powersurgepub.psutils.*;
  import java.io.*;
  import java.net.URL;
  import com.apple.mrj.MRJOSType;
  import com.apple.mrj.MRJFileUtils;
import com.powersurgepub.xos2.XOS;

/**
   A text file that can be opened for input or output, read from
   or written to, and closed. 
 */
public class TextFile extends File {
  
  /** Open and close events are logged, and data read and written is sent as log data. */
  private  Logger       log;
  
  /** Do we want to log all data, or only data preceding significant events? */
  private  boolean      dataLogging = false;
  
  /** Data to be sent to the log. */
  private  LogData      logData;
  
  /** Events to be logged. */
  private  LogEvent     logEvent;
  
  /** Debug instance. */
           Debug				debug = new Debug(false);
  
  /** Identifier for this file (to be printed in the log as a source ID). */
  private  String       fileId;
  
  /** 
     File numbers will be assigned sequentially for each new text file
     created, and used to create default file IDs.
   */
  private  static int   fileNumber = 0;
  
  /** The name of this file, without any path info. */
  private  String       fileName;
  
  /** The file name including path information. */
  private  String       filePathAndName;
  
  /** Indicates whether URL was passed to constructor. */
  private  boolean       isURL = false;
  
  /** URL that was passed to constructor. */
  private  URL           url;
  
  /** Input stream created from url. */
  private  InputStream   urlIn;
  
  /** Input stream reader for url. */
  private  InputStreamReader urlReader;
  
  /** The reader used when input is requested. */
  private  FileReader   textFileReader;
  
  /** The buffered reader used for input. */
  private  BufferedReader textFileBufReader;
  
  /** The writer used when output is requested. */
  private  FileWriter   textFileWriter;
  
  /** The buffered writer used for output. */
  private  BufferedWriter textFileBufWriter;
  
  /** 
     The sequentially assigned line number of the last record
     read or written.
   */
  private  int          lineNumber;
  
  /** Have we hit end of file on input? */
  protected  boolean    atEnd;
  
  /** The line being read or written. */
  private  String       line;
  
  /** Is this file currently open for input? */
  private  boolean      openAsInput = false;
  
  /** Is this file currently open for output? */
  private  boolean      openAsOutput = false;
  
  /**
     A constructor that accepts a path and file name.
    
     @param path      A path to the directory containing the file.
    
     @param fileName  The file name itself (without path info).
   */
  public TextFile (String path, String fileName) {
    super (path, fileName);
    this.fileName = fileName;
    filePathAndName = path + File.separatorChar + fileName;
    isURL = false;
    initialize();
  }
  
  /**
     A constructor that accepts a file name.
    
     @param inFileName  A file name.
   */
  public TextFile (String inFileName) {
    super (inFileName);
    fileName = this.getName();
    filePathAndName = this.getAbsolutePath();
    isURL = false;
    initialize();
  }
  
  /**
     A constructor that accepts two paramenters: a File object
     representing the path to the file, and a String containing the file
     name itself.
     
     @param inPathFile  A path to the directory containing the file
     
     @param inFileName  The file name itself (without path info).
   */
  public TextFile (File inPathFile, String inFileName) {
    super (inPathFile, inFileName);
    fileName = this.getName();
    filePathAndName = this.getAbsolutePath();
    isURL = false;
    initialize();
  }
  
  /**
     A constructor that accepts a single File object
     representing the file.</p>
     
     @param inFile  The text file itself.
   */  
  public TextFile (File inFile) {
    super (inFile.getPath());
    fileName = this.getName();
    filePathAndName = this.getAbsolutePath();
    isURL = false;
    initialize();
  }
  
  /**
     A constructor that accepts a URL pointing to a Web resource.</p>
     
     @param url  The URL of a text file.
   */  
  public TextFile (URL url) {
    super (url.getFile());
    this.url = url;
    fileName = url.getFile();
    filePathAndName = url.getFile();
    isURL = true;
    initialize();
  }
  
  /**
     Initialize common fields for all constructors.
   */ 
  private void initialize () {
    lineNumber = 0;
    atEnd = false;
    fileNumber++;
    fileId = "file" + String.valueOf (fileNumber);
    logData = new LogData ("", fileId, 0);
    logEvent = new LogEvent (0, "");
  }

  /**
     Returns the next line from the text file. <p>
     
     If the text file has not yet been opened, then the first execution of this 
     method will automatically attempt to open the file. When the end of the file 
     is encountered, an empty String will be returned as the next line, and the 
     atEnd variable will be turned on. I/O exceptions will be thrown.
     
     @return    The next line in the file (or an empty string at end of file).
    
     @throws IOException If read failure.
     @throws FileNotFoundException On first read for file, if file name 
                                   passed to constructor cannot be found.
   */
  public String readLine () 
      throws IOException, FileNotFoundException {
    line = GlobalConstants.EMPTY_STRING;
    if ((! openAsInput) && (! atEnd)) {
      this.openForInput();
    }
    if (openAsInput) {
      try { 
        line = textFileBufReader.readLine();
        if (line == null) {
          line = GlobalConstants.EMPTY_STRING;
          atEnd = true;
          // ensureLog();
          // logEvent.setSeverity (LogEvent.NORMAL);
          // logEvent.setMessage (fileName + " reached end of file");
          // logEvent.setDataRelated (false);
          // log.recordEvent (logEvent);
          close();
        } else {
          lineNumber++;
          if (dataLogging) {
            ensureLog();
            logData.setData (line);
            logData.setSequenceNumber (lineNumber);
            log.nextLine (logData);
          }
        }
      } catch (IOException e) {
        line = GlobalConstants.EMPTY_STRING;
        atEnd = true;
        throw e;
      }
      if (atEnd && openAsInput) {
        this.close();
      }
    }
    return line;
  }
  
  /**
     Opens the text file for input. Note that this method need not 
     be explictly executed, since the first execution of readLine 
     will cause this method to be invoked automatically. 
    
     @throws FileNotFoundException If the input file name passed to the
                                   constructor cannot be found.
   */
  public void openForInput () 
      throws FileNotFoundException, IOException {
    openAsOutput = false;
    openAsInput = false;
    atEnd = true;
    if (isURL) {
      urlIn = url.openStream();
      urlReader = new InputStreamReader (urlIn);
      textFileBufReader = new BufferedReader (urlReader);
    } else {
      if (! this.exists() ) {
        throw new FileNotFoundException (this.toString() + " does not exist.");
      }
      if (! this.isFile () ) {
        throw new FileNotFoundException (this.toString() + " is not a file.");
      }
      if (! this.canRead () ) {
        throw new FileNotFoundException (this.toString() + " cannot be read.");
      }
      textFileReader = new FileReader (this);
      textFileBufReader = new BufferedReader (textFileReader);
    }
    ensureLog();
    // logEvent.setSeverity (LogEvent.NORMAL);
    // logEvent.setMessage (fileName + " opened for input successfully");
    // logEvent.setDataRelated (false);
    // log.recordEvent (logEvent);
    openAsInput = true;
    atEnd = false;
  }
  
  /**
     Indicates whether the file has reached its end.</p>
    
     @return    True if file is at end, false if there are more records to read.
   */
  public boolean isAtEnd() {
    return atEnd;
  }
  
  /**
     Writes a String to the text file. If the text file has not yet 
     been opened, then the first execution of this method will 
     automatically attempt to open the file for output. 
     
     @param inStr   The next block of text to be written to the text file.
    
     @throws IOException If there is trouble writing to the disk file.
   */
  public void write (String inStr) 
      throws IOException {
    line = inStr;
    if (! openAsOutput) {
      openForOutput();
    }
    if (openAsOutput) {
      textFileBufWriter.write(line, 0, line.length());
      if (dataLogging) {
        ensureLog();
        logData.setData (line);
        logData.setSequenceNumber (lineNumber);
        log.nextLine (logData);
      } // end if dataLogging
    } // end if openAsOutput
    return;
  } // end method writeLine
  
  /**
     Writes the next line to the text file. If the text file has not yet 
     been opened, then the first execution of this method will 
     automatically attempt to open the file for output. 
     
     @param inStr   The next line to be written to the text file.
    
     @throws IOException If there is trouble writing to the disk file.
   */
  public void writeLine (String inStr) 
      throws IOException {
    line = inStr;
    if (! openAsOutput) {
      openForOutput();
    }
    if (openAsOutput) {
      textFileBufWriter.write(line, 0, line.length());
      textFileBufWriter.newLine();
      lineNumber++;
      if (dataLogging) {
        ensureLog();
        logData.setData (line);
        logData.setSequenceNumber (lineNumber);
        log.nextLine (logData);
      } // end if dataLogging
    } // end if openAsOutput
    return;
  } // end method writeLine
  
  /**
     Opens the text file for output. Note that this method 
     need not be explictly executed, since the first execution
     of writeLine will cause this method to be invoked automatically. 
    
     @throws IO Exception If there is trouble opening the disk file.
   */
  public void openForOutput () 
      throws IOException {
    openAsOutput = false;
    openAsInput = false;
    if (this.exists() ) {
      ensureLog();
      logEvent.setSeverity (LogEvent.MEDIUM);
      logEvent.setMessage (this.toString() + " will be overwritten");
      logEvent.setDataRelated (false);
      log.recordEvent (logEvent);
    }
    if (this.isDirectory () ) {
      throw new IOException (this.toString() + " is a directory.");
    }
    textFileWriter = new FileWriter (this);
    textFileBufWriter = new BufferedWriter (textFileWriter);
    // ensureLog();
    // logEvent.setSeverity (LogEvent.NORMAL);
    // logEvent.setMessage (fileName + " opened for output successfully");
    // logEvent.setDataRelated (false);
    // log.recordEvent (logEvent);
    openAsOutput = true;
  } // end method openForOutput
  
  /**
     Closes the file, if it is currently open for input or output. 
    
     @throws IOException If there is trouble closing the disk file.
   */  
  public void close() 
      throws IOException {
    if (openAsInput) {
      textFileBufReader.close ();
      ensureLog();
      logEvent.setSeverity (LogEvent.NORMAL);
      logEvent.setMessage (String.valueOf (lineNumber) 
          + " lines read from " + filePathAndName);
      logEvent.setDataRelated (false);
      log.recordEvent (logEvent);
    }
    if (openAsOutput) {
      textFileBufWriter.close();
      ensureLog();
      logEvent.setSeverity (LogEvent.NORMAL);
      logEvent.setMessage (String.valueOf (lineNumber)
          + " lines written to " + filePathAndName);
      logEvent.setDataRelated (false);
      log.recordEvent (logEvent);
    }
    if (openAsInput) {
      openAsInput = false;
      atEnd = true;
    }
    if (openAsOutput) {
      openAsOutput = false;
    }
  } // end method close
  
  /**
     Ensures that a log file has been created.
   */
  private void ensureLog () {
    if (log == null) {
      log = new Logger (new LogOutput());
    }
  }
  
  /**
     Returns the file name, without any path info.
    
     @return  The name of the file, without any path info.
   */
  public String getFileName() {
    return fileName;
  }
  
  /**
     Returns the file path and name.
    
     @return  The name of the file, including all path information.
   */
  public String getFilePathAndName() {
    return filePathAndName;
  }
  
  /**
     Returns the number of the last line read from or written to the file.
     
     @return  The line number of the last line read or written.
   */
  public int getLineNumber() {
    return lineNumber;
  }
  
  /**
     Sets the Logger object to be used for logging. 
    
     @param log The Logger object being used for logging significant events.
   */
  public void setLog (Logger log) {
    this.log = log;
  }
  
  /**
     Gets the Logger object to be used for logging. 
    
     @return The Logger object being used for logging significant events.
   */
  public Logger getLog () {
    return log;
  } 
  
  /**
     Sets the option to log all data off or on. 
    
     @param dataLogging True to send all data read or written to the
                        log file.
   */
  public void setDataLogging (boolean dataLogging) {
    this.dataLogging = dataLogging;
  }
  
  /**
     Gets the option to log all data. 
    
     @return True to send all data read or written to the
             log file.
   */
  public boolean getDataLogging () {
    return dataLogging;
  }
  
  /**
     Sets the debug instance to the passed value.
    
     @param debug Debug instance. 
   */
  public void setDebug (Debug debug) {
    this.debug = debug;
  }
  
  /**
     Sets the file ID to be passed to the Logger.
    
     @param fileId Used to identify the source of the data being logged.
   */
  public void setFileId (String fileId) {
    this.fileId = fileId;
    logData.setSourceId (fileId);
  }
} // end class TextFile
