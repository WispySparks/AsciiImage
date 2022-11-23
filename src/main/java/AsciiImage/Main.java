package main.java.AsciiImage;

import java.io.File;

import main.java.AsciiImage.Display.Frame;
import main.java.AsciiImage.PNG.PNG;
import main.java.AsciiImage.PNG.PNGDecoder;

public class Main { // todo crc, read other chunks, resize png so that it fits on screen
    public static void main(String[] args) { // todo ascii is looking good no?
        // PNGUtil.makeCRCTable();
        File file = new File("C:\\Users\\wispy\\Downloads\\celeste.png"); // RGBA
        // File file = new File("C:\\Users\\wispy\\Pictures\\Saved Pictures\\artemispfp2.png"); // RGBA
        // File file = new File("C:\\Users\\wispy\\Pictures\\Saved Pictures\\Cypher&Omen.png"); // only filter 0
        // File file = new File("C:\\Users\\wispy\\Pictures\\Saved Pictures\\artemispfp.png"); // only filter 0
        // File file = new File("C:\\Users\\wispy\\Pictures\\Saved Pictures\\Overlay.png"); // RGB
        PNGDecoder decoder = new PNGDecoder();
        PNG png = decoder.readPNG(file);
        new Frame(png);
    }
}