/*
 * PSFileOpener.java
 *
 * Created on April 28, 2005, 6:08 PM
 */

package com.powersurgepub.psutils;

import java.io.*;

/**
 * A standard interface for a user interfacing program capable
 * of opening a passed file. <p>
 *
 * This code is copyright (c) 2002-2005 by Herb Bowie.
 * All rights reserved. <p>
 *
 * Version History: <ul><li>
 *     </ul>
 *
 * @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
 *         herb@powersurgepub.com</a>)<br>
 *         of PowerSurge Publishing 
 *         (<a href="http://www.powersurgepub.com">
 *         www.powersurgepub.com</a>)
 *
 * @version 2003/10/20 - Originally written.
 */
public interface PSFileOpener {
  
  /**      
    Standard way to respond to a request to open a file.
   
    @param file File to be opened by this application.
   */
  public void handleOpenFile (PSFile file);
  
}

