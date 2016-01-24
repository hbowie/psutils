/*
 * Copyright 1999 - 2016 Herb Bowie
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

  import java.io.*;
  import java.nio.channels.*;
  import java.util.*;

/**
/*
 * FileUtils.java
 *
 * A variety of utility methods to do useful things with files and folders.
 *
 * @author Herb Bowie
 */
public class FileUtils {
  
  /** Creates a new instance of FileUtils */
  public FileUtils() {
  }
  
  /**
   Copy the visible contents of one folder to another folder, including all
   sub-folders. Skip any hidden files. 
   */
  public static boolean copyFolder (File fromFolder, File toFolder) {

    boolean ok = (fromFolder.exists() 
        && fromFolder.canRead() 
        && fromFolder.isDirectory());
    if (ok) {
      ok = ensureFolder (toFolder);
    }

    if (ok) {
      ArrayList fromDirList = new ArrayList();
      ArrayList outDirList = new ArrayList();
      fromDirList.add (fromFolder);
      outDirList.add (toFolder);
      int i = 0;
      while (i < fromDirList.size()) {
        // for each directory or sub-directory
        File fromDir = (File)fromDirList.get(i);
        File toDir = (File)outDirList.get(i);
        String[] dirEntry = fromDir.list();

        for (int j = 0; j < dirEntry.length; j++) {
          String entry = dirEntry [j];
          File fromFile = new File (fromDir, entry);
          File toFile = new File (toDir, entry);
          if (fromFile.exists()
              && fromFile.canRead()) {
            if (fromFile.isDirectory()) {
              fromDirList.add (fromFile);
              ok = ensureFolder (toFile);
              outDirList.add (toFile);
            } else {
              if (entry.startsWith(".")
                  || fromFile.isHidden()) {
                // Skip hidden files
              } else {
                ok = copyFile (fromFile, toFile);
              }
            } // end if not a directory
          } // end if directory entry is readable
        } // end for each directory entry in current directory
        i++;
      } // end for each directory to be exploded
    } // end if ok
    return ok;
  } // end copyFolder method
  
  public static boolean copyFile (File fromFile, File toFile) {
    boolean ok = true;
    try {
      // Create channel on the source
      FileChannel fromChannel = new FileInputStream(fromFile).getChannel();
    
      // Create channel on the destination
      FileChannel toChannel = new FileOutputStream(toFile).getChannel();
    
      // Copy file contents from source to destination
      toChannel.transferFrom(fromChannel, 0, fromChannel.size());
      
      // Close the channels
      fromChannel.close();
      toChannel.close();
    } catch (IOException e) {
      ok = false;
    }
    return ok;
  }
  
  /**
   Ensure folder exists (if it doesn't, try to create it).
   */
  public static File ensureFolder (File parent, String child) {
    // make sure folder exists
    boolean ok = true;
    File folder = null;
    if (parent == null) {
      ok = false;
    } else {
      folder = new File (parent, child);
      ok = ensureFolder (folder);
    }
    if (ok) {
      return folder;
    } else {
      return null;
    }
  }
  
  /**
    Deletes the passed file/folder, then checks to see if the parent folder is
    empty; if so, uses a recursive call to this same method to delete the parent,
    proceeding up the tree until all empty folders have been deleted. 

    @param fileToDelete The file or folder to be deleted

    @return True if everything went ok; false if any problems. 
  */
  public static boolean deleteFileAndEmptyParents (File fileToDelete) {
    
    boolean ok = true;
    
    File parentFile = fileToDelete.getParentFile();
    
    ok = deleteFile(fileToDelete);
    
    if (ok) {
      if (parentFile != null && parentFile.exists()) {
      File[] contents = fileToDelete.getParentFile().listFiles();
        if (contents.length == 0) {
          ok = deleteFileAndEmptyParents (parentFile);
        }
      }
    }

    return ok;
  }
  
  /**
   Delete a single file. 
  
   @param fileToDelete The file to be deleted. 
  
   @return True if the file was deleted successfully.
   */
  public static boolean deleteFile(File fileToDelete) {

    boolean ok = true;

    try {
      ok = fileToDelete.delete();
      if (! ok) {
        Logger.getShared().recordEvent (LogEvent.MEDIUM, 
            "File or folder "+ fileToDelete.toString() + " could not be deleted", 
                false); 
      }
    } catch (SecurityException sx) {
      Logger.getShared().recordEvent (LogEvent.MEDIUM,
          "Access Denied when trying to delete file or folder " 
          + fileToDelete.toString(), false);
    }

    return ok;
    
  }
  
  /**
   Delete the contents of one folder, including all sub-folders.
   */
  public static boolean deleteFolderContents (File folder) {
    boolean ok = true;
    ok = (folder.exists() && folder.isDirectory() && folder.canWrite());
    if (ok) {
      String[] dirEntry = folder.list();
      for (int j = 0; j < dirEntry.length; j++) {
        String entry = dirEntry [j];
        File file = new File (folder, entry);
        if (file.exists()
            && file.canRead()) {
          if (file.isDirectory()) {
            ok = deleteFolderContents (file);
          } // end if not a directory
          ok = file.delete();
        } // end if directory entry is readable
      } // end for each directory entry in current directory
    } // end if ok
    return ok;
  } // end deleteFolderContents method
  
  /**
   Ensure folder exists (if it doesn't, try to create it).
   */
  public static boolean ensureFolder (File folder) {
    // make sure folder exists
    Trouble trouble = Trouble.getShared();
    boolean ok = true;
    if (folder == null) {
      ok = false;
    } 
    if (ok) {
      if (! folder.exists()) {
        File parent = folder.getParentFile();
        if (parent != null) {
          if (! parent.exists()) {
            ok = ensureFolder(parent);
          } // end if parent doesn't yet exist
        } // end if parent folder is defined
      } // end if desired folder does not exist
    } // end if ok
    if (ok && (! folder.exists())) {
      try {
        ok = folder.mkdir();
        if (! ok) {
          trouble.report 
              ("Folder "+ folder.toString() + " could not be created", 
                  "File Save Error");  
        }
      } catch (SecurityException sx) {
        trouble.report ("Access Denied when trying to create folder " 
            + folder.toString(), "File Save Error");
      }
    } // end if ok so far and folder does not exist
    return ok;
  }
  
  public static boolean isGoodInputDirectory(File file) {
    return (file != null
        && file.exists()
        && file.isDirectory()
        && file.canRead());
  }
  
  public static boolean isGoodInputFile(File file) {
    return (file != null
        && file.exists()
        && file.isFile()
        && file.canRead());
  }
  
}
