package com.powersurgepub.psutils;



  import java.awt.*;

  import java.util.*;

  import javax.swing.*;

  

/**

   A utility class containing static methods to do things

   with Swing objects. <p>

     

   This code is copyright (c) 2000 by Herb Bowie of PowerSurge Publishing. 

   All rights reserved. <p>

   

   Version History: <ul><li>

     

    </ul>

   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">herb@powersurgepub.com</a>)<br>

    <br>  of PowerSurge Publishing (<A href="http://www.powersurgepub.com/software/">www.powersurgepub.com/software</a>)

   @version 2000/12/20 - Originally written for TDFCzar project.

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

