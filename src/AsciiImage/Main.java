package AsciiImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Scanner;

import javax.imageio.stream.FileImageInputStream;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // File file = new File(scanner.nextLine());
        File file = new File("C:\\Users\\wispy\\Downloads\\celeste.png");
        if (file.exists() && file.getName().endsWith(".png")) {
            // System.out.println(file.getAbsolutePath());
            System.out.println(file.getName());
            try {
                FileImageInputStream stream = new FileImageInputStream(file);
                stream.skipBytes(8); // first eight bytes should be -119 80 78 71 13 10 26 10 because its a png
                stream.skipBytes(8); // skip IHDR chunk length and chunk type
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                for (int i = 0; i<4; i++) { // IHDR Image header chunk width
                    output.write(stream.readByte());
                }
                byte[] width = output.toByteArray();
                output.reset();
                for (int i = 0; i<4; i++) { // IHDR Image header chunk height
                    output.write(stream.readByte());                
                }
                byte[] height = output.toByteArray();
                System.out.println("Width: " + new BigInteger(width).intValue());
                System.out.println("Height: " + new BigInteger(height).intValue());
                stream.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        } else {
            System.out.println("invalid file");
        }
        scanner.close();
    }
}
