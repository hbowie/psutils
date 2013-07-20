/*
 * Copyright 2013 Herb Bowie
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

/**
 Interface for a window to be managed by WindowMenuManager.

 @author Herb Bowie
 */
public interface WindowToManage {
  
  public String getTitle();
  
  public void setVisible (boolean visible);
  
  public void toFront();
  
  public int getWidth();
  
  public int getHeight();
  
  public void setLocation(int x, int y);
  
}
