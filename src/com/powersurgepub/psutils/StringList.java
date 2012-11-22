/*
 * StringList.java
 *
 * Created on July 22, 2004, 5:04 AM
 */

package com.powersurgepub.psutils;

  import java.util.*;

/**
 *
 * @author  hbowie
 */
public class StringList 
    implements Enumeration, Iterator {
  
  private   ArrayList list    = new ArrayList();
  private   int       index   = 0;
  
  /** Creates a new instance of StringList */
  public StringList() {
  }
  
  public void populate (String[] stringArray) {
    for (int i = 0; i < stringArray.length; i++) {
      list.add (stringArray [i]);
    }
    index = 0;
  }
  
  public boolean hasMoreElements() {
    return (index < list.size());
  }
  
  public Object nextElement() {
    String next = null;
    if (hasMoreElements()) {
      next = (String)list.get(index);
      index++;
    }
    return next;
  }
  
  public boolean hasNext() {
    return hasMoreElements();
  }
  
  public Object next() {
    return nextElement();
  }
  
  public void remove() 
      throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }
  
}
