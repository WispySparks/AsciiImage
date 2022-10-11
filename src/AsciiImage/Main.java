package AsciiImage;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // File file = new File(scanner.nextLine());
        File file = new File("C:\\Users\\wispy\\Downloads\\celeste.png");
        PNGReader reader = new PNGReader();
        PNG png = reader.readPNG(file);
        // System.out.println("Width: " + png.width());
        // System.out.println("Height: " + png.height());
        // System.out.println("Bit Depth: " + png.bitDepth());
        System.out.println("Color Type: " + png.colorType());
        // System.out.println("Compression Method: " + png.compression());
        // System.out.println("Filter Method: " + png.filter());
        // System.out.println("Interlace Method: " + png.interlace());
        scanner.close();
    }
}
