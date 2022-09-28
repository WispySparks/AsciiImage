package AsciiImage;

import java.util.HashMap;

public class Ascii {

    private static HashMap<Integer, String> map = new HashMap<>();
    static {
        map.put(33, "!");
    }
    
    public static String decimalToAscii(int i) {
        if (map.containsKey(i)) {
            return map.get(i);
        }
        return "";
    }

}
