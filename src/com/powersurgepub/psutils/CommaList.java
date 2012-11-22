/*
 * CommaList.java
 *
 * Created on December 1, 2007, 4:43 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.powersurgepub.psutils;

/**
 *
 * @author hbowie
 */
public class CommaList {
  
  private StringBuffer list = new StringBuffer();
  
  /** Creates a new instance of CommaList */
  public CommaList() {
  }
  
  public void append (String next) {
    if (list.length() > 0) {
      list.append (", ");
    }
    list.append (next);
  }
  
  public String toString () {
    return list.toString();
  }
  
}
