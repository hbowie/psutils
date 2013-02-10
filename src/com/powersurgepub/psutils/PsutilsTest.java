/*
 * Copyright 1999 - 2013 Herb Bowie
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