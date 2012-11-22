package com.powersurgepub.psutils;

	import java.util.*;
  
/**
   An object representing a collection and using minimal internal
   storage. <p>
  
   This code is copyright (c) 2003 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 2003/05/31 - Originally written.
 */

public class PSCollection {
	
	/** Comparator to be used to compare elements of the collection. */
  private Comparator		comp;
  
	/** An array for holding the objects in the collection. */
  private ArrayList			array;
  
	/** A TreeMap for finding elements and keeping them in a sorted order.  */
  private TreeMap				map;
  
  /** 
     Tests the class. This method is called by <code>PsutilsTest.main</code>.
   */
  public static void test () {

    System.out.println (" ");
    System.out.println (" ");
    System.out.println ("Testing class PSCollection");
    PSCollection tester = new PSCollection (0);
    String s1 = "Joe (1)";
    String s2 = "Bill (2)";
    String s3 = "Megan (3)";
    int index1 = tester.put ((Object)s1);
    int index2 = tester.put ((Object)s2);
    int index3 = tester.put ((Object)s3);
    int index4 = tester.put ((Object)s3);
    System.out.println ("Position 1 = " + String.valueOf (index1) 
        + ", returns " + (String)tester.get (index1));
    System.out.println ("Position 2 = " + String.valueOf (index2) 
        + ", returns " + (String)tester.get (index2));   
    System.out.println ("Position 3 = " + String.valueOf (index3) 
        + ", returns " + (String)tester.get (index3)); 
    System.out.println ("Position 3 (replaced) = " + String.valueOf (index4) 
        + ", returns " + (String)tester.get (index4)); 
    System.out.println ("Key " + s1 + " returns " + String.valueOf (tester.get (s1)));
    System.out.println ("Key " + s2 + " returns " + String.valueOf (tester.get (s2)));
    System.out.println ("Key " + s3 + " returns " + String.valueOf (tester.get (s3)));
    System.out.println ("Index " + String.valueOf(index3) 
        + " returns " + (String)tester.get (index3));
    System.out.println ("Size of collection = " + String.valueOf (tester.size()));
  } // end of test method

	/**
	   Constructor.
     
     @param comp A comparator for the collection. 
     
     @param initialCapacity Starting size for the collection. 
	 */
	public PSCollection (int initialCapacity) {
    if (initialCapacity > 0) {
      array = new ArrayList (initialCapacity);
    } else {
      array = new ArrayList ();
    }
    comp = new PSComparator (array);
    map = new TreeMap (comp);
	}

  /**
	   Adds a new element to the collection, or updates
     the existing one, if the key already exists.
     
     @return The index position of the element. 
	  
	   @param element Object to be added.
	 */
  public int put (Object element) {
    int index = array.size();
    boolean ok = array.add (element);
    PSPointer newKey = new PSPointer (index);
    PSPointer newValue = newKey; 
    PSPointer priorValue = (PSPointer)map.put (newKey, newValue);
    if (priorValue != null) {
      PSPointer badValue = (PSPointer)map.put (newKey, priorValue);
      Object newElement = array.remove (index);
      index = priorValue.intValue();
      Object oldElement = array.set (index, element);
    } 
    return index;
  }
  
  /**
	   Gets an existing element from a specified position
     in the array.
     
     @return The requested Object. 
	  
	   @param index A pointer to a position in the collection. Once
                  an element is added, this index will not change.
	 */
  public Object get (int index) {
    return array.get (index);
  }
  
  /**
	   Gets an existing element location given a specified key.
     
     @return A pointer to an element's location, if the key exists 
             in the collection, otherwise -1. 
	  
	   @param key An object with the desired key.
	 */
  public int get (Object key) {
    Object value = map.get (key);
    if (value == null) {
      return -1;
    } else {
      PSPointer pointer = (PSPointer)value;
      return pointer.intValue();
    }
  }
  
  /**
    Returns an iterator that, in turn, will return each
    member of the collection in sorted sequence.
    
    @return A standard collection iterator.
   */
  public Iterator iterator() {
    return new PSIterator (array, map);
  }
  
  /**
    Returns the number of elements in the collection.
    
    @return the current size of the collection.
    */
  public int size() {
    checkSize();
    return array.size();
  }
  
  /**
    Checks to make sure the two collections are the same size.
    */
  public void checkSize() {
    if (array.size() != map.size()) {
      System.out.println ("Array holds " + String.valueOf (array.size()) + " elements, Map holds " + String.valueOf (map.size()) + " elements");
    }
  }
  	
	/**
	   Returns the object in string form.
	  
	   @return object formatted as a string
	 */
	public String toString() {
    return ("PSCollection");
	}
  
  class PSComparator 
    implements Comparator {
    
    private		ArrayList innerArray;
    
    /**
      Constructor for PSComparator.
      
      @param array ArrayList containing objects.
    */
    PSComparator (ArrayList innerArray) {
      this.innerArray = innerArray;
    }
    
    /**
      An object that will compare two comparable objects stored
      in the array. 
    
      @return a negative integer if comp1 is less than comp2,     <br>
              zero if comp1 equals comp2, or                      <br>
              a positive integer if comp1 is greater than comp2.
              
      @param inObj1 first object to be compared.
      @param inObj2 second object to be compared. 
    
     */
    public int compare (Object inObj1, Object inObj2) {
    
      Class c1 = inObj1.getClass();
      Object obj1;
      if (c1.getName().equals ("com.powersurgepub.psutils.PSPointer")) {
        PSPointer pointer1 = (PSPointer)inObj1;
        obj1 = innerArray.get (pointer1.intValue());
      } else {
        obj1 = inObj1;
      }
      Comparable comp1 = (Comparable)obj1;
      
      Class c2 = inObj2.getClass();
      Object obj2;
      if (c2.getName().equals ("com.powersurgepub.psutils.PSPointer")) {
        PSPointer pointer2 = (PSPointer)inObj2;
        obj2 = innerArray.get (pointer2.intValue()); 
      } else {
        obj2 = inObj2;
      }
      Comparable comp2 = (Comparable)obj2;
      
      return comp1.compareTo (comp2);
    }
    
  } // end of inner class
  
  class PSIterator 
      implements Iterator {
      
    private ArrayList innerArray;
    private TreeMap   innerMap;
    private Set keySet; 
    private Iterator keyList;
    
    public PSIterator (ArrayList innerArray, TreeMap innerMap) {
      this.innerArray = innerArray;
      this.innerMap = innerMap;
      keySet = innerMap.keySet();
      keyList = keySet.iterator();
    }
    
    public void remove() {
      throw new UnsupportedOperationException();
    }
    
    public boolean hasNext() {
      return keyList.hasNext();
    }
    
    public Object next() {
      if (keyList.hasNext()) {
        Object nextObj = keyList.next();
        PSPointer nextPointer = (PSPointer)nextObj;
        int nextInt = nextPointer.intValue();
        return innerArray.get(nextInt);
      } else {
        throw new NoSuchElementException();
      }
    }
  } // end of inner class
  
} // end of class

