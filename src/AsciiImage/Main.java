package AsciiImage;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // File file = new File(scanner.nextLine());
        File file = new File("C:\\Users\\wispy\\Downloads\\celeste.png");
        PNGReader reader = new PNGReader(file);
        reader.readPNG();
        // System.out.println("Width: " + reader.width);
        // System.out.println("Height: " + reader.height);
        // System.out.println("Bit Depth: " + reader.bitDepth);
        // System.out.println("Color Type: " + reader.colorType);
        // System.out.println("Compression Method: " + reader.compression);
        // System.out.println("Filter Method: " + reader.filter);
        // System.out.println("Interlace Method: " + reader.interlace);
        scanner.close();
    }
}
