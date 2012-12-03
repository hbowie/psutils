package com.powersurgepub.psutils;

  import java.io.*;
  import java.net.*;
  import java.util.*;
  import javax.swing.*;

/**
 Obtain a simple list of Strings from a text file stored as a resource, 
 within the "/resources" directory, and return the list in alphabetical order. 

 @author Herb Bowie
 */
public class ResourceList {
  
  private boolean ok = false;
  private BufferedReader reader = null;
  private JComboBox comboBox = null;
  private List list = null;
  
  /**
  
   Identify a list of items from a text file containing one item per line. 
  
   @param klass The reference class from which the resource will be found. 
  
   @param name The name of the resource. If it lacks a file extension, then
               ".txt" will be added to the end. 
  */
  public ResourceList (Class klass, String name) {
    useClassAndName (klass, name);
  }
  
  /**
   Identify a list of items from a text file containing one item per line. 
  
   @param url The url pointing to the list to be loaded. 
  */
  public ResourceList (URL url) {
    useURL (url);
  }
  
  /**
   Identify a list of items from a text file containing one item per line.
  
   @param stream The stream containing the list to be loaded. 
  */
  public ResourceList (InputStream stream) {
    useStream (stream);
  }
  
  /**
   Create a buffered reader from a Class and a resource name. 
  
   @param klass The reference class from which the resource will be found. 
  
   @param name The name of the resource. If it lacks a file extension, then
               ".txt" will be added to the end. 
  */
  private void useClassAndName (Class klass, String name) {
    String resourceName;
    if (name.indexOf('.') < 0) {
      resourceName = "resources/" + name + ".txt";
    } else {
      resourceName = "resources/" + name;
    }
    URL url = klass.getResource(resourceName);
    if (url == null) {
      ok = false;
      Logger.getShared().recordEvent(LogEvent.MEDIUM, 
          "Resource " + resourceName + " not found", false);
    } else {
      ok = true;
      // System.out.println ("Loading resource from url " + url.toExternalForm());
    }
    if (ok) {
      useURL (url);
    } 
  }
  
  /**
   Create a buffered reader from a URL. 
  
   @param url The url pointing to the list to be loaded. 
  */
  private void useURL (URL url) {
    InputStream stream = null;
    try {
      stream = url.openStream();
      ok = true;
    } catch (IOException e) {
      ok = false;
      Logger.getShared().recordEvent(LogEvent.MEDIUM, 
          "Resource " + url.toExternalForm() + " could not be read", false);
    }
    if (ok && stream != null) {
      useStream (stream);
    }
  }
  
  /**
   Create a buffered reader from an input stream. 
  
   @param stream The stream containing the list to be loaded. 
  */
  private void useStream (InputStream stream) {
    InputStreamReader streamReader = new InputStreamReader (stream);
    reader = new BufferedReader (streamReader);
    ok = true;
  }
  
  /**
   Is everything OK so far?
  
   @return True if A-OK, false if any problems encountered. 
  */
  public boolean isOK () {
    return ok;
  }
  
  /**
   Load a list of items into the combo box list, reading the list of items
   from a text file containing one item per line.
  
   @param comboBox The Combo Box whose model is to be populated with the list. 
  
   @return @return True if the list was loaded successfully, false otherwise.
  */
  public boolean load (JComboBox comboBox) {
    this.comboBox = comboBox;
    this.list = null;
    return load();
  }
  
  /**
   Load a list of items into the list, reading the list of items
   from a text file containing one item per line. 
  
   @param list The list to be loaded. 
  
   @return True if the list was loaded successfully, false otherwise.
  */
  public boolean load (List list) {
    this.comboBox = null;
    this.list = list;
    return load();
  }
  
  /**
   Load a list of items into the passed list, reading the list of items
   from a text file containing one item per line. 
  
   @return True if the list was loaded successfully, false otherwise.
  */
  private boolean load () {
    if (! ok) {
      return ok;
    }
    String line = "";
    while (line != null) {
      try {
        line = reader.readLine();
      } catch (IOException e) {
        ok = false;
        line = null;
          Logger.getShared().recordEvent(LogEvent.MEDIUM, 
            "Resource could not be read", false);
      }
      if (line != null) {
        addAlphabetical (line);
      }
    }
    return ok;
  }
  
  /**
   Add another item to the list . Keep the list
   in alphabetical order, and do not allow duplicates. 
  
   @param anotherItem The item to be added. If it has leading or trailing
                      white space, then this will be removed. 
  
   @return An index pointing to the position in the list at which the given
           item was located, or added. 
  */
  public int addAlphabetical (String anotherItem) {
    
    String newItem = anotherItem.trim();
    int i = 0;
    int comparison = 1;
    while (comparison > 0 && i < size()) {
      String nextItem = get(i);
      comparison = newItem.compareToIgnoreCase(nextItem);
      if (comparison > 0) {
        i++;
      }
      else
      if (comparison < 0) {
        add (i, newItem);
      }
    }
    if (i >= size()) {
      add(newItem);
    }
    return i;
  }
  
  /**
   Add an item to the end of the list. 
  
   @param anotherItem The item to be added. 
  */
  public void add (String anotherItem) {
    if (comboBox != null) {
      comboBox.addItem(anotherItem);
    } else {
      list.add(anotherItem);
    }
  }
  
  /**
   Insert an item at a specific point in the list. 
  
   @param i The position at which the item should be added. 
  
   @param anotherItem The item to be added. 
  */
  public void add (int i, String anotherItem) {
    if (comboBox != null) {
      comboBox.insertItemAt(anotherItem, i);
    } else {
      list.add(i, anotherItem);
    }
  }
  
  /**
   Return the index location of the specified item, if it exists in the list 
   (ignoring upper- or lower-case), otherwise return -1.
  
   @param item The string being searched for. 
  
   @return The given item's index location, or -1 if the item is not in the list.
  */
  public int indexOf (String item) {
    int i = 0;
    boolean found = false;
    while (i < size() && (! found)) {
      if (item.equalsIgnoreCase(get(i))) {
        found = true;
      } else {
        i++;
      }
    } // end while looking for a match
    if (found) {
      return i;
    } else {
      return -1;
    }
  }
  
  /**
   Return an item from the list. 
  
   @param i An index pointing to the desired item. 
  
   @return The item from the list. 
  */
  public String get (int i) {
    if (comboBox != null) {
      return comboBox.getItemAt(i).toString();
    } else {
      return list.get(i).toString();
    }
  }
  
  /**
   Return the size of the list. 
  
   @return The number of items in the list. 
  */
  public int size() {
    if (comboBox != null) {
      return comboBox.getItemCount();
    } else {
      return list.size();
    }
  }
  


}
