/*
 * Copyright 2000 - 2013 Herb Bowie
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



  import java.awt.*;

  import java.util.*;

  import javax.swing.*;

  

/**
   A utility class containing static methods to do things
   with Swing objects. <p>
 */

public class SwingUtils {



  private SwingUtils () {

    super ();

  }



  /**

     Loads a JComboBox list from a Vector.

    

     @param   box   The JComboBox to be loaded.

    

     @param   list  The vector containing the objects to be loaded.

   */

  public static void comboBoxLoad (JComboBox box, Vector list) {

    if (box.getItemCount() > 0) {

      box.removeAllItems();

    }

    Enumeration seq = list.elements();

    while (seq.hasMoreElements()) {

      box.addItem(seq.nextElement());

    }

  } // end comboBoxLoad method



} // end of SwingUtils class

