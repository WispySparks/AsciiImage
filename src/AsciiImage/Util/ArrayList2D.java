package AsciiImage.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a 2D ArrayList. Holds a List of ArrayLists.
 * 
 * @param <T> the type of elements in the 2D list 
 * @author Wispy Sparks
 */
public class ArrayList2D<T> {
    
    /**
     * Two dimensional list.
     */
    private List<List<T>> list2D;

    /**
     * Creates a 2D list with a default 10 lists.
     *         
     */
    public ArrayList2D() {
        list2D = new ArrayList<>();
    }

    /**
     * Creates a 2D list with the specified amount of lists.
     *
     * @param  size  the initial size of the 2D list
     * @throws IllegalArgumentException if the specified size is negative
     *         
     */
    public ArrayList2D(int size) {
        if (size < 0) throw new IllegalArgumentException("Size can't be negative: " + size); 
        list2D = new ArrayList<>(size);
    }

    public void add(int i, T item) {
        if (i >= list2D.size()) {
            list2D.add(new ArrayList<>());
            list2D.get(i).add(item);
        }
        else {
            list2D.get(i).add(item);
        }
    }

    public T get(int i, int j) {
        return list2D.get(i).get(j);
    }

    public int size() {
        return list2D.size();
    }

    public int size(int i) {
        if (i >= list2D.size()) {
            return 0;
        }
        return list2D.get(i).size();
    }

    @Override
    public String toString() {
        String full = "";
        for (List<T> list : list2D) {
            String s = "[";
            for (T t : list) {
                s = s.concat(t.toString() + ", ");
            }
            s = s.substring(0, s.length()-2);
            s += "]";
            full += s + "\n";
        }
        full = full.trim();
        return full;
    }

}