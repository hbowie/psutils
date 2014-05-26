/*
 * Copyright 1999 - 2014 Herb Bowie
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

  import java.io.File;
  import java.net.*;
           
/**
   A file name with some methods that allow it to be
   manipulated in various useful ways. 
 */
public class FileName {
  
  /** Potential separator characters found within a filename */
  private static final String SEPS = "/\\" + File.pathSeparator;
  
  /** A null file extension */
  private static final String NULL_EXT = "   ";
  
  /** 0 if this routine should guess the type. */
  public static final int GUESS_TYPE = 0;

  /** 1 if a File. */
  public static final int FILE_TYPE = 1;
  
  /** 2 if a Directory. */
  public static final int DIR_TYPE = 2;
  
  /** The file name as provided and before any modification */
  private  String        fileNameIn;
  
  /** 1 for a File, 2 for a directory. */
  private  int					 fileOrDir = 0;					
  
  /** Used for scanning the file name to determine its components */
  private StringScanner  fileNameScanner;
  
  /** The file name all in lower case characters */
  private String         fileNameLowerCase;
  
  /** The file name without path or extension information */
  private String         fileNameBase;
  
  /** The file name's extension */
  private String         fileNameExt;
  
  /** File name with extension, but without any path information */
  private String         fileName;
  
  /** The lowest level directory in which the file is stored */
  private String         folder;
  
  /** The protocol, if any. */
  private String         protocol;
  
  /** Number of directories/folders in path */
  private int						 numberOfFolders;
  
  /** The information leading up to the file name, including a final slash. */
  private String				 path;
  
  /** The length of the original file name (fileNameIn) */
  private  int           fileNameLength;
  
  /** The string as a URL. */
  private String         urlString;
  
  /** The beginning of a folder structure within the name 
   (following http:// and domain name, if present).
   */
  private int             folderStartPosition = 0;
  
  /** 
     The location of the last dot in the filename, presumably indicating the
     beginning of the extension 
   */
  private int            dotPosition = 0;
  
  /**
     The location of the last slash, or directory separator, presumably
     indicating the beginning of the file name, and the end of the path.
   */
  private  int          lastSlashPosition;
  
  /**
     The location of the next slash, or directory separator, moving
     backwards in the file name from the first slash. This presumably
     indicates the beginning of the folder name.
   */
  private  int          nextSlashPosition;
  
  /**
     A scanner used to extract individual folder names
     from the path.
   */
  private	 StringScanner	folderScanner;
  
  /**
     Current folder name
   */
  private  String					currFolder;
  
  /**
     Current folder number (where first is numbered 1)
   */
  private	 int						currFolderNumber = 0;
  
  /** 
     Tests the class. It is called by psutilsTest.main.
   */
  public static void test () {
    
    System.out.println(" ");
    System.out.println(" ");
    System.out.println ("Testing class FileName");
    testFileName ("test");
    testFileName ("test", 1);
    testFileName ("test", 2);
    testFileName ("test.java");
    testFileName ("psutils" + File.pathSeparator + "test.java");
    testFileName ("com" + File.pathSeparator 
      + "powersurgepub" + File.pathSeparator
      + "psutils" + File.pathSeparator
      + "test.java");
    testFileName ("com/powersurgepub/psutils");
    testFileName ("com/powersurgepub/psutils/test.java");
    testFileName ("i:folder1\\file.txt");
    testFileName ("k:folder1\\folder2\\file.txt");
    testFileName ("/users/hbowie/java/source.java");
    testFileName ("/Users/hbowie/test/tdfczar");
    testFileName ("/Users/hbowie/Java/aux/tdfczar/tdfczar_testing/example3_pages/");
    testResolve  ("/Users/hbowie/Sites/ReasonToRock.com/artists/bruce_springsteen.html", "../index.html");
  }

  /**
     Tests one file name, by displaying various results available.
   */  
  public static void testFileName (String test) {
    System.out.println(" ");
    FileName test1 = new FileName (test);
    System.out.println ("Input String = " + test);
    testDisplay (test1);
    File file2 = new File (test);
    System.out.println ("Input File  = " + file2.toString());
    FileName test2 = new FileName (file2);
    testDisplay (test2);
  }
  
  /** 
     Tests one file name, by displaying various results available.
   */  
  public static void testFileName (String test, int type) {
    System.out.println(" ");
    FileName test1 = new FileName (test, type);
    System.out.println ("Input String = " + test);
    System.out.println ("Input Type   = " + String.valueOf(type));
    testDisplay (test1);
    File file2 = new File (test);
    System.out.println ("Input File  = " + file2.toString());
    FileName test2 = new FileName (file2, type);
    testDisplay (test2);
  }
  
  /** 
     Tests resolveRelative method.
   */  
  public static void testResolve (String test1, String test2) {
    System.out.println(" ");
    FileName tester = new FileName (test1);
    System.out.println ("Input String 1 = " + test1);
    System.out.println ("Input String 2 = " + test2);
    System.out.println ("Resolution  = " + tester.resolveRelative(test2));
  }
  
  /**
     Display results of all the getters for the class.
   */
  public static void testDisplay (FileName testFN) {
    System.out.println ("Path    = " + testFN.getPath());
    System.out.println ("Folder  = " + testFN.getFolder());
    System.out.println ("No Fldrs= " + String.valueOf(testFN.getNumberOfFolders()));
    System.out.println ("Fldr 3  = " + testFN.getFolder(3));
    System.out.println ("Fldr 1  = " + testFN.getFolder(1));
    String folder = testFN.getFirstFolder();
    int folderNum = 1;
    while (folder.length() > 0) {
      System.out.println ("Fldr " + String.valueOf (folderNum) + " = " + folder + String.valueOf(testFN.getFolderSeparator()));
      folder = testFN.getNextFolder();
      folderNum++;
    } 
    System.out.println ("Base    = " + testFN.getBase());
    System.out.println ("Ext     = " + testFN.getExt());
    System.out.println ("Fl Name = " + testFN.getFileName());
    System.out.println ("English = " + testFN.getFileNameEnglish());
    System.out.println ("New Ext = " + testFN.replaceExt("class"));
  }
  
  /**
     Constructor that accepts a File object.
    
     @param fileIn File to be analyzed. 
   */
  public FileName (File fileIn) {
    this (fileIn.getPath(), 0);
  }
  
  /**
     Constructor that accepts a File object and type.
    
     @param fileIn File to be analyzed.
     @param fileOrDirIn 1 if a file, 2 if a directory, 0 if not sure.
   */
  public FileName (File fileIn, int fileOrDirIn) {
    this (fileIn.getPath(), fileOrDirIn);
  }
  
  /**
     Constructor that accepts a String file name.
    
     @param fileNameIn Name of file to be analyzed.
   */
  public FileName (String fileNameIn) {
    this (fileNameIn, 0);
  }
  
  /**
     Analyzes the file name passed to it as a String.
    
     @param fileNameIn   the file name to be analyzed
     @param fileOrDirIn  1 if a file, 2 if a directory, 0 if not sure.
   */
  public FileName (String fileNameIn, int fileOrDirIn) {

    // System.out.println ("FileName Constructor file name in = " + fileNameIn);
    StringBuilder fileNameMod = new StringBuilder(fileNameIn);
    int i = fileNameMod.indexOf("%");
    while (i >= 0) {
      if (fileNameMod.length() > (i + 2)) {
        if (fileNameMod.substring(i + 1, i + 3).equals("20")) {
          fileNameMod.delete(i, i + 3);
          fileNameMod.insert(i, " ");
        }
      }
      i = fileNameMod.indexOf("%", i + 1);
    }
    // System.out.println ("  Transformed to " + fileNameMod.toString());
    this.fileNameIn = fileNameMod.toString();
    this.fileOrDir  = fileOrDirIn;
    fileNameLowerCase = this.fileNameIn.toLowerCase ();
    fileNameLength = this.fileNameIn.length ();
    fileNameScanner = new StringScanner (this.fileNameIn, -1);
    
    boolean endsWithSlash = false;
    fileNameScanner.stopAtChars (SEPS);
    if ((fileNameScanner.getIndex() + 1) == fileNameLength) {
    	endsWithSlash = true;
    }
    fileNameScanner.resetIndex();
    
    if (this.fileOrDir == FILE_TYPE || this.fileOrDir == DIR_TYPE) {
    } else {
      this.fileOrDir = GUESS_TYPE;
    }
    if (this.fileOrDir == GUESS_TYPE) {
      if (endsWithSlash) {
        this.fileOrDir = DIR_TYPE;
      }
      else {
        fileNameScanner.stopAtChars (GlobalConstants.PERIOD_STRING);
        dotPosition = fileNameScanner.getIndex();
        if (dotPosition > 0) {
          this.fileOrDir = FILE_TYPE;
        } else {
          this.fileOrDir = DIR_TYPE;
        }
        fileNameScanner.resetIndex();
      } // end else not ending in slash
    } // end guessing
    
    // See if file name starts with a protocol
    int delimsLength = 0;
    folderStartPosition = this.fileNameIn.indexOf("://");
    if (folderStartPosition >= 0) {
      delimsLength = 3;
    } else {
      folderStartPosition = this.fileNameIn.indexOf (":/");
      if (folderStartPosition >= 0) {
        delimsLength = 1;
      }
    }
    // System.out.println ("  folderStartPosition = " + String.valueOf (folderStartPosition));
    // System.out.println ("  delimsLength = " + String.valueOf (delimsLength));
    if (folderStartPosition < 0) {
      folderStartPosition = 0;
      protocol = "";
    } else {
      protocol = this.fileNameIn.substring(0, folderStartPosition);
      folderStartPosition = folderStartPosition + delimsLength;
      if (protocol.equals ("http")) {
        folderStartPosition = this.fileNameIn.indexOf ("/", folderStartPosition);
        if (folderStartPosition < 0) {
          folderStartPosition = fileNameLength;
        }
      }
    }
    // System.out.println ("  protocol = " + protocol);
    // System.out.println ("  folderStartPosition = " + String.valueOf (folderStartPosition));
    
    if (this.fileOrDir == FILE_TYPE) {
      fileNameScanner.stopAtChars (SEPS + GlobalConstants.PERIOD_STRING);
      dotPosition = fileNameScanner.getIndex();
      fileNameScanner.incrementIndex (+1);
      fileNameScanner.stopAtChars (SEPS);
      lastSlashPosition = fileNameScanner.getIndex();
      fileNameScanner.incrementIndex();
    } else {
      dotPosition = 0;
      lastSlashPosition = fileNameLength;
      if (endsWithSlash) {
        lastSlashPosition--;
        fileNameScanner.incrementIndex();
      }
    }
    fileNameScanner.stopAtChars (SEPS);
    nextSlashPosition = fileNameScanner.getIndex();
    if (dotPosition <= 0 
        || dotPosition <= lastSlashPosition 
        || dotPosition < folderStartPosition) {
      fileNameExt = NULL_EXT;
      dotPosition = fileNameLength;
    } else {
      fileNameExt = fileNameLowerCase.substring ((dotPosition + 1));
    }
    if (dotPosition > lastSlashPosition) {
      fileNameBase = this.fileNameIn.substring (lastSlashPosition + 1, dotPosition);
      fileName     = this.fileNameIn.substring (lastSlashPosition + 1);
    } else {
      fileNameBase = "";
      fileName = "";
    }
    if (lastSlashPosition > nextSlashPosition) {
      folder = this.fileNameIn.substring (nextSlashPosition + 1, lastSlashPosition);
      numberOfFolders = 1;
      while (fileNameScanner.getIndex() > folderStartPosition) {
        numberOfFolders++;
        fileNameScanner.incrementIndex();
        fileNameScanner.stopAtChars (SEPS);
      }
    } else {
      folder = "";
      numberOfFolders = 0;
    }
    if (lastSlashPosition >= fileNameLength) {
      path = this.fileNameIn;
    } else {
      path = this.fileNameIn.substring (folderStartPosition, lastSlashPosition + 1);
    }
    
    urlString = this.fileNameIn;
    if (folderStartPosition <= 0) { 
      try {
        urlString = new File(this.fileNameIn).toURI().toURL().toString();
      } catch (MalformedURLException e) {
        // do nada
      }
    }
    
    folderScanner = new StringScanner (this.fileNameIn);
    String firstFolder = getFirstFolder();
    getFirstFolderNext();
  } // end constructor
  
  /**
   See if this file/folder is beneath another folder in the directory structure. 
  
   @param fn2 The second folder, to be compared to this one. 
  
   @return True if this file/folder is beneath the other one; false otherwise. 
  */
  public boolean isBeneath (FileName fn2) {
    boolean matched = true;
    this.getFirstFolderNext();
    fn2.getFirstFolderNext();
    if (this.getNumberOfFolders() >= fn2.getNumberOfFolders()) {
      int i = 1;
      while (matched && i <= fn2.getNumberOfFolders()) {
        String folder1 = this.getFolder(i);
        String folder2 = fn2.getFolder(i);
        if (folder1.equals(folder2)) {
          i++;
        } else {
          matched = false;
        }
      } // end of folder comparison
    } else {
      matched = false;
    }
    return matched;
  }
  
  /**
     Resolve a relative file reference using this File as
     the base.
     
     @return Complete path and file name for the passed file.
     
     @param relativeFile A string defining a file location
                         relative to this file (as in a relative
                         URL link).
  */
  public String resolveRelative (String relativeFile) {
    int fileStart = 0;
    int nextFileStart = 3;
    int pathFolders = getNumberOfFolders();
    while (relativeFile.length() >= nextFileStart
        && relativeFile.substring(fileStart, nextFileStart).equals ("../")) {
      pathFolders--;
      fileStart = nextFileStart;
      nextFileStart = fileStart + 3;
    }
    StringBuffer resolved = new StringBuffer();
    getFirstFolderNext();
    char firstCharSep = getFolderSeparator();
    if (firstCharSep != ' ') {
      resolved.append (firstCharSep);
    }
    char lastSep = '/';
    char sep;
    for (int i = 1; i <= pathFolders; i++) {
      resolved.append (getFolder(i));
      sep = getFolderSeparator();
      if (sep == ' ') {
        resolved.append (lastSep);
      } else {
        resolved.append (sep);
        lastSep = sep;
      }
    } // end for loop
    resolved.append (relativeFile.substring (fileStart));
    return resolved.toString();
  }

  /**
   Return as much of the file path as will fit in the requested maximum length.
   @param desiredMaxLength The maximum number of characters desired.
   @return As much of the file path and name as will fit, starting with the
           file name itself, and then preceding leftwards/upwards with
           containing directories/folders.
   */
  public String getToFit (int desiredMaxLength) {
    StringBuilder work = new StringBuilder(fileName);
    int i = numberOfFolders;
    boolean roomToSpare = work.length() < desiredMaxLength;
    while (i >= 0 && roomToSpare) {
      String nextFolder = getFolder (i);
      if ((nextFolder.length() + 1 + work.length()) <= desiredMaxLength) {
        work.insert(0, getFolderSeparator());
        work.insert(0, nextFolder);
        i--;
      } else {
        roomToSpare = false;
      }
    }
    return work.toString();
  }
  
  /**
     Get a Folder for a specific sequence number.
    
     @param sequence number of folder, with 1 indicating first.
   */
  public String getFolder (int folderNumber) {
    String work = "";
    if (folderNumber < currFolderNumber) {
      work = getFirstFolder();
    }
    while (currFolderNumber < folderNumber) {
      work = getNextFolder();
    }
    if (currFolderNumber == folderNumber
        && currFolderNumber <= numberOfFolders) {
      return currFolder;
    } else {
      return "";
    }
  }
  
  /**
     Gets the first folder from the path.
    
     @return  First folder name, or a zero-length String if no
              folders left.
   */
  public String getFirstFolder () {
    getFirstFolderNext();
    return getNextFolder();
  }
  
  /**
     Resets folder index so as to start with first when getNextFolder
     is called. 
   */
  public void getFirstFolderNext () {
    folderScanner.resetIndex();
    currFolderNumber = 0;
  }
  
  /**
     Gets the next folder from the path.
    
     @return  Next folder name, or a zero-length String if no
              folders left.
   */
  public String getNextFolder () {
    currFolderNumber++;
    currFolder = folderScanner.getNextString (SEPS);
    if (currFolderNumber <= numberOfFolders) {
      return currFolder;
    } else {
      return "";
    }
  }
  
  /**
     Get the separator character following the last folder found.
    
     @return Character at current index position, or space
             if beyond end of file name.
   */
  public char getFolderSeparator() {
    if (folderScanner.nextCharInGroup (SEPS)) {
      return folderScanner.getNextChar();
    } else {
      return ' ';
    }
  }
  
  /**
     Returns the file name that was input, without modification.
    
     @return  original file name
   */
  public String toString () {
    return fileNameIn;
  }
  
  /**
     Returns the preceding path information without the file name.
      
     @return    the path without its file name. 
   */
  public String getPath () {    
    return path;
  }
  
  /**
     Returns the file name without its preceding path information.
      
     @return    the file name without its path. 
   */
  public String getFileName () {    
    return fileName;
  }
  
  /**
     Returns the file name without its path or extension.  
    
     @return    the file name without the path or trailing extension 
                (final period and anything following it).
   */
  public String getBase () {
    return fileNameBase;
  }
  
  /**
     Returns the file extension.
    
     @return    the file extension (anything following the final period),
                in lower-case.
   */  
  public String getExt () {
    return fileNameExt;
  }
  
  /**
     Returns the lowest level folder (directory) name.
    
     @return    the last element of the path.
   */  
  public String getFolder () {
    return folder;
  }
  
  /**
     Returns the total number of folders in the path.
    
     @return    number of folders in the path.
   */  
  public int getNumberOfFolders () {
    return numberOfFolders;
  }
  
  public String getFileOrFolderNameEnglish () {
    if (fileNameBase.length() > 0) {
      return StringUtils.wordSpace (fileNameBase, false);
    } else {
      return StringUtils.wordSpace (folder, false);
    }
  }
  
  /**
     Returns the file name in as English-like a form as possible, 
     with spaces between apparent words.
    
     @return    the file name in semi-English
   */
  public String getFileNameEnglish () {
    return StringUtils.wordSpace (fileNameBase, false);
  }
  
  /**
   Returns the file name as a URL string.
   */
  public String getURLString () {
    return urlString;
  }
  
  /**
   Returns a File object.
   */
  public File getFile () {
    StringBuilder fileName = new StringBuilder();
    if (folderStartPosition > 0) {
      if (protocol.equals ("file")) {
        fileName.append (fileNameIn.substring (folderStartPosition));
      } 
    } else {
      fileName.append (fileNameIn);
    }
    int i, j, k;
    char c, d, e;
    for (i = 0; i < fileName.length(); i++) {
      c = fileName.charAt(i);
      j = i + 1;
      d = ' ';
      if (j < fileName.length()) {
        d = fileName.charAt(j);
      }
      k = i + 2;
      e = ' ';
      if (k < fileName.length()) {
        e = fileName.charAt(k);
      }
      if (c == '%' && d == '2' && e == '0') {
        fileName.delete(i, k + 1);
        fileName.insert(i, ' ');
      }
    }
    if (fileName.length() > 0) {
      return new File(fileName.toString());
    } else {
      return null;
    }
  }
  
  /**
     Creates a new file name using the original base and a new extension.
    
     @return    a new file name, consisting of the original base plus a period 
                plus the new extension.
    
     @param    newExt  A String containing the desired new file extension 
                       (not including the period).
   */  
  public String replaceExt (String newExt) {
    return (fileNameBase + GlobalConstants.PERIOD_STRING + newExt.toLowerCase ());
  }
} // end of class FileName
