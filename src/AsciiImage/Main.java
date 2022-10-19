package AsciiImage;

import java.util.Scanner;

import AsciiImage.Util.ArrayList2D;

public class Main {
    public static void main(String[] args) { // https://stackoverflow.com/a/56678483 how to find lumiance and perceived lightness
        Scanner scanner = new Scanner(System.in);
        // File file = new File(scanner.nextLine());
        // File file = new File("C:\\Users\\wispy\\Downloads\\celeste.png");
        // File file = new File("C:\\Users\\wispy\\Pictures\\Saved Pictures\\artemispfp.png"); // only filter 0
        // File file = new File("C:\\Users\\wispy\\Pictures\\Saved Pictures\\artemispfp2.png");
        // File file = new File("C:\\Users\\wispy\\Pictures\\Saved Pictures\\Cypher&Omen.png"); // only filter 0
        // PNGDecoder reader = new PNGDecoder();
        // PNG png = reader.readPNG(file);
        scanner.close();
        // new Frame(png);
        ArrayList2D<String> list = new ArrayList2D<>();
        list.add(0, "j");
        list.add(0, "sheesh");
        list.add(1, "new line!");
        System.out.println(list);
    }
}