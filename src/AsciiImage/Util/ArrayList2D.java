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
    private List<List<T>> list2D;

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
            list2D.add(new ArrayList<>());
            list2D.get(i).add(item);
        }
        else {
            list2D.get(i).add(item);
        }
    }

    /**
     * Retrieves an element from a list in the 2D ArrayList
     * @param i Index of list in 2D ArrayList
     * @param j Index of element in i list
     * @return element at j index of the list at i index of 2D ArrayList
     */
    public T get(int i, int j) {
        return list2D.get(i).get(j);
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

}