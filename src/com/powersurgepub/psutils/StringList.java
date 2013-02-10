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
