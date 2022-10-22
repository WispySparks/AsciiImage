package main.java.AsciiImage.Util;

import java.util.HashMap;

public class AsciiTable {

    private static HashMap<Integer, String> map = new HashMap<>();
    static {
        map.put(32, " ");
        map.put(33, "!");
        map.put(34, "\"");
        map.put(35, "#");
        map.put(36, "$");
        map.put(37, "%");
        map.put(38, "&");
        map.put(39, "'");
        map.put(40, "(");
        map.put(41, ")");
        map.put(42, "*");
        map.put(43, "+");
        map.put(44, ",");
        map.put(45, "-");
        map.put(46, ".");
        map.put(47, "/");
        map.put(48, "0");
        map.put(49, "1");
        map.put(50, "2");
        map.put(51, "3");
        map.put(52, "4");
        map.put(53, "5");
        map.put(54, "6");
        map.put(55, "7");
        map.put(56, "8");
        map.put(57, "9");
        map.put(58, ":");
        map.put(59, ";");
        map.put(60, "<");
        map.put(61, "=");
        map.put(62, ">");
        map.put(63, "?");
        map.put(64, "@");
        map.put(65, "A");
        map.put(66, "B");
        map.put(67, "C");
        map.put(68, "D");
        map.put(69, "E");
        map.put(70, "F");
        map.put(71, "G");
        map.put(72, "H");
        map.put(73, "I");
        map.put(74, "J");
        map.put(75, "K");
        map.put(76, "L");
        map.put(77, "M");
        map.put(78, "N");
        map.put(79, "O");
        map.put(80, "P");
        map.put(81, "Q");
        map.put(82, "R");
        map.put(83, "S");
        map.put(84, "T");
        map.put(85, "U");
        map.put(86, "V");
        map.put(87, "W");
        map.put(88, "X");
        map.put(89, "Y");
        map.put(90, "Z");
        map.put(91, "[");
        map.put(92, "\\");
        map.put(93, "]");
        map.put(94, "^");
        map.put(95, "_");
        map.put(96, "`");
        map.put(97, "a");
        map.put(98, "b");
        map.put(99, "c");
        map.put(100, "d");
        map.put(101, "e");
        map.put(102, "f");
        map.put(103, "g");
        map.put(104, "h");
        map.put(105, "i");
        map.put(106, "j");
        map.put(107, "k");
        map.put(108, "l");
        map.put(109, "m");
        map.put(110, "n");
        map.put(111, "o");
        map.put(112, "p");
        map.put(113, "q");
        map.put(114, "r");
        map.put(115, "s");
        map.put(116, "t");
        map.put(117, "u");
        map.put(118, "v");
        map.put(119, "w");
        map.put(120, "x");
        map.put(121, "y");
        map.put(122, "z");
        map.put(123, "{");
        map.put(124, "|");
        map.put(125, "}");
        map.put(126, "~");
    }
    
    public static String decimalToAscii(int asciiCode) {
        if (map.containsKey(asciiCode)) {
            return map.get(asciiCode);
        }
        throw new IllegalArgumentException("asciiCode: " + asciiCode + " is not between 32-126");
    }

}
