package com.powersurgepub.psutils;

  import java.lang.Character;
  import java.lang.Object;
  import java.lang.String;
  import java.lang.System;
  import com.powersurgepub.psutils.StringScanner;
  import com.powersurgepub.psutils.StringPattern;
  import com.powersurgepub.psutils.StringUtils;
  import com.powersurgepub.psutils.FileName;
/**
   Method to test the other classes in the package.
  
   This code is copyright (c) 1999-2003 by Herb Bowie of PowerSurge Publishing. 
   All rights reserved. <p>
   
   Version History: <ul><li>
     2002/08/18 - Added test routine for RegistrationCode. <li>
     2000/04/21 - Modified to be consistent with "The Elements of Java Style". <li>
     1999/10/02 - Consolidated all the test routines for this package 
                into this class. </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing (<A href="http://www.powersurgepub.com/software/">
           www.powersurgepub.com/software</a>)
  
   @version 
    2002/08/18 - Added test routine for PSCollection.
 */

public class PsutilsTest {  
  
  /** 
     Tests all the psutils classes.
   */
  public static void main (String args[]) {
    
    UserPrefs prefs = UserPrefs.getShared("PsutilsTest");
    StringConverter.test();
    StringUtils.test();
    PSCollection.test();
    StringScanner.test();
    StringPattern.test();
    FileName.test();
    CommonName.test();
    SuperString.test();
    Debug.displaySystemProperties();
    SubSpec.test();
    SubSpecs.test();
    System.out.flush();
  }
} // end of class PsutilsTest