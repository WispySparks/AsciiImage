package AsciiImage.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a 2D ArrayList. Holds a List of ArrayLists.
 * 
 * @param <T> the type of elements in the 2D ArrayList 
 * @author Wispy Sparks
 */
public class ArrayList2D<T> {
    
    /**
     * Two dimensional list.
     */
    private final List<List<T>> list2D;

    /**
     * Creates a 2D ArrayList.
     */
    public ArrayList2D() {
        list2D = new ArrayList<>();
    }

    /**
     * Creates a 2D ArrayList with the specified amount of lists.
     *
     * @param  size  the initial size of the 2D ArrayList
     * @throws IllegalArgumentException if the specified size is negative
     */
    public ArrayList2D(int size) {
        list2D = new ArrayList<>(size);
    }

    /**
     * Adds an element to a list in the 2D ArrayList
     * @param i Index of list in 2D ArrayList
     * @param item element to add to the specified list
     */
    public void add(int i, T item) {
        if (i >= list2D.size()) {
            for (int j = list2D.size(); j <= i; j++) {
                list2D.add(j, new ArrayList<>());
            }
            list2D.get(i).add(item);
        }
        else {
            list2D.get(i).add(item);
        }
    }

    /**
     * @param i Index of list in 2D ArrayList
     * @param elementIndex Index within specified list to remove
     */
    public void set(int i, int elementIndex) {
        list2D.get(i).remove(elementIndex);
    }

    /**
     * Retrieves an element from a list in the 2D ArrayList
     * @param i Index of list in 2D ArrayList
     * @param elementIndex Index of element within specified list
     * @return element at j index of the list at i index of 2D ArrayList
     */
    public T get(int i, int elementIndex) {
        return list2D.get(i).get(elementIndex);
    }

    /**
     * @param i Index of list in 2D ArrayList
     * @param elementIndex Index within specified list
     * @param item element to remove
     */
    public void remove(int i, int elementIndex, T item) {
        list2D.get(i).set(elementIndex, item);
    }

    /**
     * @param i Index of list in 2D ArrayList
     */
    public void clear(int i) {
        list2D.get(i).clear();
    }

    /**
     * @param i Index of list in 2D ArrayList
     */
    public void clearAll(int i) {
        list2D.clear();
    }

    /**
     * Retrieves size of 2D ArrayList (number of lists)
     * @return number of lists in the 2D ArrayList
     */
    public int size() {
        return list2D.size();
    }
    
    /**
     * Retrieves size of a list in the 2D ArrayList
     * @param i Index of specific list in the 2D ArrayList
     * @return size of that list
     */
    public int size(int i) {
        if (i >= list2D.size()) {
            return 0;
        }
        return list2D.get(i).size();
    }   

    /**
     * Transfers everything in this 2D list into a single list
     * @return list
     */
    public List<T> toSingleList() {
        List<T> list = new ArrayList<>();
        for (List<T> l : list2D) {
            list.addAll(l);
        }
        return list;
    }

    /**
     * Used to grab the list and perform all the operations that I don't implement here
     * @param i Index of specific list in the 2D ArrayList
     * @return that list object
     */
    public List<T> getList(int i) {
        return list2D.get(i);
    }

    /**
     * @return Multidimensional list contained in this class
     */
    public List<List<T>> get2DList() {
        return list2D;
    }

    @Override
    public String toString() { 
        String full = "";
        for (List<T> list : list2D) { // Go through each list and add together all the elements of it
            String s = "[";
            for (T t : list) { 
                s = s.concat(t.toString() + ", ");
            }
            s = s.substring(0, s.length()-2);
            s += "]";
            full += s + "\n"; // Add a new line between every list
        }
        full = full.trim();
        return full;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof ArrayList2D) {
            ArrayList2D<?> aL = (ArrayList2D<?>) obj; 
            if (list2D.equals(aL.get2DList())) return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return list2D.hashCode();
    }

}