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

  import java.awt.*;
  import java.awt.event.*;
  import java.util.*;
  import javax.swing.*;

/**
 A class to help a user manage the various windows generated by an application.

 @author Herb Bowie
 */
public class WindowMenuManager {
  
  public static final int    CHILD_WINDOW_X_OFFSET = 60;
	public static final int    CHILD_WINDOW_Y_OFFSET = 60;
  
  private static      WindowMenuManager sharedWindowMenuManager = null;
  
  private             ArrayList<JMenu>       menus = new ArrayList<JMenu>();

  private             ArrayList<WindowToManage> windows 
      = new ArrayList<WindowToManage>();

  private WindowMenuManager(JMenu windowMenu) {
    addWindowMenu (windowMenu);
  }

  private WindowMenuManager () {

  }

  /**
   Initialize the shared Window Menu Manager, if it hasn't already been
   initialized, and return it.

   @param windowMenu The Window JMenu to be managed.

   @return The shared Window Menu Manager.
   */
  public static WindowMenuManager getShared(JMenu windowMenu) {
    if (sharedWindowMenuManager == null) {
      sharedWindowMenuManager = new WindowMenuManager(windowMenu);
    }
    return sharedWindowMenuManager;
  }

  /**
   Initialize the shared Window Menu Manager, if it hasn't already been
   initialized, and return it.

   @return The shared Window Menu Manager.
   */
  public static WindowMenuManager getShared() {
    if (sharedWindowMenuManager == null) {
      sharedWindowMenuManager = new WindowMenuManager();
    }
    return sharedWindowMenuManager;
  }

  /**
   Set the Window Menu to be managed.

   @param windowMenu The Window Menu to be managed.
   */
  public void setWindowMenu (JMenu windowMenu) {
    addWindowMenu (windowMenu);
  }
  
  /**
   Add another Window Menu to be managed.
  
   @param windowMenu 
  */
  public void addWindowMenu (JMenu windowMenu) {
    boolean found = false;
    for (JMenu menuInList : menus) {
      if (menuInList != null) {
        if (menuInList == windowMenu) {
          found = true;
          break;
        } // end if the new menu is already in the list
      } // end if the menu in the list is not null
    } // end for each menu in the list
    
    if (! found) {
      menus.add(windowMenu);
    }
  }
  
  /**
   Add a new WindowToManage to the Window menu. It will be visible on the menu,
   but the window will not be made visible nor brought to the front. 

   @param window The WindowToManage to be added.
                  
   */
  public int add(WindowToManage window) {
    int i = getIndexOf(window);
    if (i < 0) {
      i = addAtEnd(window, KeyEvent.VK_UNDEFINED);
    }
    return i;
  }

  /**
   Add a new WindowToManage to the Window menu. It will be visible on the menu,
   but the window will not be made visible nor brought to the front. 

   @param window The WindowToManage to be added.
   @param keyChar The key character to be used to invoke the window, or zero
                  if no accelerator is to be defined. 
                  
   */
  public int add(WindowToManage window, int keyChar) {
    int i = getIndexOf(window);
    if (i < 0) {
      i = addAtEnd(window, keyChar);
    }
    return i;
  }
  
  public void locateUpperLeftAndMakeVisible 
      (Component refComponent, WindowToManage window) {
    
    locateUpperLeft(refComponent, window);
    makeVisible (window);
  }
  
  public void locateUpperLeft 
      (Component refComponent, WindowToManage window) {
    
    window.setLocation (
			refComponent.getX() + CHILD_WINDOW_X_OFFSET, 
			refComponent.getY() + CHILD_WINDOW_Y_OFFSET);
  }
  
  public void locateCenterAndMakeVisible
      (Component refComponent, WindowToManage window) {
    
    int w = refComponent.getWidth();
	  int h = refComponent.getHeight();
	  int x = refComponent.getX();
	  int y = refComponent.getY();
	  window.setLocation(
	      x + ((w - window.getWidth()) / 2),
	      y + ((h - window.getHeight()) / 2));
    makeVisible (window);
  }

  /**
   Add a new WindowToManage to the Window menu, and bring the window to the front.

   @param window The window to be made visible. 
   */
  public void makeVisible(WindowToManage window) {
    int i = getIndexOf(window);
    if (i < 0) {
      i = addAtEnd(window, KeyEvent.VK_UNDEFINED);
    }
    showWindow (window);
  }

  /**
   Hide the passed window and remove it from the Window menu.

   @param window The window to be hidden. 
   */
  public void hide(WindowToManage window) {
    int i = getIndexOf(window);
    if (i >= 0) {
      for (JMenu menuInList : menus) {
        if (i < menuInList.getItemCount()) {
          menuInList.remove(i);
        } else {
          System.out.println ("Trying to remove menu item " + String.valueOf(i) +
              " from menu of " + String.valueOf(menuInList.getItemCount()) +
              " items");
        }
      }
      windows.remove(i);
    }
    window.setVisible(false);
  }

  /**
   Add new window to the end of the list of menus. 
  
   @param window The window to be added. 
   @return The index at which the window was added to the windows list. 
  */
  private int addAtEnd(WindowToManage window, int keyChar) {
    windows.add(window);
    JMenuItem menuItem = new JMenuItem(window.getTitle());
    if (keyChar > KeyEvent.VK_UNDEFINED) {
      menuItem.setAccelerator(KeyStroke.getKeyStroke (keyChar,
        Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    }
    menuItem.setActionCommand (window.getTitle());
    menuItem.setToolTipText (window.getTitle());
    menuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        windowMenuItemActionPerformed(evt);
      }
    });
    for (JMenu menuInList : menus) {
      menuInList.add(menuItem);
    }
    return (windows.size() - 1);
  }

  /**
    Action listener for recent file menu items.

    @param evt = Action event.
   */
  private void windowMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    String title = evt.getActionCommand();
    WindowToManage window = getWindow(title);
    if (window != null) {
      showWindow (window);
    }
  } // end method

  private void showWindow(WindowToManage window) {
    window.setVisible(true);
    window.toFront();
  }

  public WindowToManage getWindow(String title) {
    int i = getIndexOf(title);
    if (i >= 0) {
      return get(i);
    } else {
      return null;
    }
  }

  public int getIndexOf(WindowToManage window) {
    String title = window.getTitle();
    return getIndexOf(title);
  }

  public int getIndexOf(String title) {
    int i = 0;
    boolean found = false;
    while (i < windows.size() && (! found)) {
      if (title.equals(getTitle(i))) {
        found = true;
      } else {
        i++;
      }
    } // end while looking for match
    if (! found) {
      return -1;
    } else {
      return i;
    }
  }

  public String getTitle(int i) {
    WindowToManage window = get(i);
    if (window == null) {
      return "";
    } else {
      return window.getTitle();
    }
  }

  public WindowToManage get(int i) {
    if (i < 0 || i >= windows.size()) {
      return null;
    } else {
      return windows.get(i);
    }
  }

}
