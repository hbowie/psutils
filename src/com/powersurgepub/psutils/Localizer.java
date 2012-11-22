package com.powersurgepub.psutils;

  import java.awt.*;
  import java.util.*;
  import javax.swing.*;

/**
  A class that can be used to help tailor a user interface based on the
  user's language and country.
 */
public class Localizer {

  public static final String DEFAULT_RESOURCE_BUNDLE_NAME = "strings";
  private String resourceBundleName = DEFAULT_RESOURCE_BUNDLE_NAME;

  private String language = "";
  private String country = "";
  private Locale locale;
  private ResourceBundle strings;
  private static Localizer localizer = null;

  /**
   Get the single shared occurrence of the Localizer class, using either
   a Localizer that has already been initialized, or creating a new one
   using the specified resource bundle name and the system default
   language and country.

   @param  resourceBundleName The name of the resource bundle to be used.
   @return The single, shared occurrence of the Localizer class.
   */
  public static Localizer getShared (String resourceBundleName) {
    if (localizer == null) {
      localizer = new Localizer (resourceBundleName);
    }
    return localizer;
  }

  /**
   Get the single shared occurrence of the Localizer class, using either
   a Localizer that has already been initialized, or creating a new one
   using the specified resource bundle name, language and country.

   @param resourceBundleName
   @param language
   @param country
   @return The single, shared occurrence of the Localizer class.
   */
  public static Localizer getShared (String resourceBundleName,
      String language, String country) {
    if (localizer == null) {
      localizer = new Localizer (resourceBundleName, language, country);
    }
    return localizer;
  }

  /**
   Get the single shared occurrence of the Localizer class, using either
   a Localizer that has already been initialized, or creating a new one
   using the system default resource bundle name, language and country.

   @return The single, shared occurrence of the Localizer class.
   */
  public static Localizer getShared () {
    if (localizer == null) {
      localizer = new Localizer ();
    }
    return localizer;
  }

  /**
   Create a new occurence of the Localizer class, using the default
   resource bundle name.
   */
  private Localizer () {
    this.resourceBundleName = DEFAULT_RESOURCE_BUNDLE_NAME;
    locale = Locale.getDefault();
    language = locale.getLanguage();
    country = locale.getCountry();
    getBundle();
  }

  /**
   Create a new occurrence of the Localizer class, using the specified
   resource bundle name and the system default language and country.

   @param resourceBundleName
   */
  private Localizer (String resourceBundleName) {
    this.resourceBundleName = resourceBundleName;
    locale = Locale.getDefault();
    language = locale.getLanguage();
    country = locale.getCountry();
    getBundle();
  }

  /**
   Create a new occurrence of the Localizer class, using the specified
   resource bundle name, language and country.

   @param resourceBundleName
   @param language
   @param country
   */
  private Localizer (String resourceBundleName, String language, String country) {
    this.resourceBundleName = resourceBundleName;
    setLocale (language, country);
  }

  public void setLocale (String language, String country) {
    this.language = language;
    this.country = country;
    locale = new Locale (language, country);
    Locale.setDefault (locale);
    getBundle();
  }

  /**
   Get the resource bundle.
   */
  private void getBundle() {
    strings = null;
    try {
      strings = ResourceBundle.getBundle (resourceBundleName, locale);
    } catch (MissingResourceException e) {
      Logger.getShared().recordEvent (LogEvent.MEDIUM,
        "strings properties file not found: "+ e.toString(),
        false);
    }
  }

  /**
   Localize a JmenuBar, using the initial values for each menu and menu item
   as keys.

   @param menubar The JMenuBar to be localized.
   */
  public void localize (JMenuBar menubar) {

    for (int i = 0; i < menubar.getMenuCount(); i++) {
      JMenu menu = menubar.getMenu(i);
      localize (menu);
    }
  }

  /**
   Localize a Jmenu, using the initial values for each menu item as the key.

   @param menu The JMenu to be localized. 
   */
  public void localize (JMenu menu) {
    
    menu.setText (getValueFromInitialValue (menu.getText()));
    for (int i = 0; i < menu.getMenuComponentCount(); i++) {
      Component comp = menu.getMenuComponent(i);
      if (comp.getClass().getName().equals ("javax.swing.JMenuItem")) {
        JMenuItem item = (JMenuItem)comp;
        localize (item);
      } else {
        localize (comp);
      }
    }
  }

  /**
   Localize a JMenuItem, using its initial value as a key.

   @param menuItem
   */
  public void localize (JMenuItem menuItem) {
    menuItem.setText (getValueFromInitialValue (menuItem.getText()));
  }

  /**
   The default, if higher level Swing class can be identified.

   @param component
   */
  public void localize (Component component) {
    // Generic component -- nothing we can do
  }

  /**
   Look up a value using the initial value of a Swing component as the key. 

   @param str
   @return
   */
  public String getValueFromInitialValue (String str) {

    StringBuffer suffix = new StringBuffer();
    StringBuffer key = new StringBuffer();

    // If initial value has a suffix such as an ellipsis, then remove it
    // and save it, and add it to returned field.

    for (int i = str.length() - 1; i >= 0; i--) {
      char c = str.charAt (i);
      if (key.length() > 0 || Character.isLetterOrDigit(c)) {
        if (Character.isWhitespace(c)) {
          key.insert (0, '_');
        } else {
          key.insert (0, c);
        }
      } else {
        suffix.insert (0, c);
      }
    }
    if (strings == null) {
      return (str);
    } else {
      try {
        String newValue = strings.getString (key.toString());
        return (newValue + suffix.toString());
      } catch (MissingResourceException e) {
        return (str);
      }
    }
  }

  /**
   Look up a value in the resource bundle. If not found, return the key
   itself.
   
   @param key
   @return
   */
  public String getValue (String key) {
    if (strings == null) {
      return (key);
    } else {
      try {
        return strings.getString (key);
      } catch (MissingResourceException e) {
        return (key);
      }
    }
  }

}
