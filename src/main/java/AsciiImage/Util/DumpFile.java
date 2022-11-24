package main.java.AsciiImage.Util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DumpFile {
    
    private File file = new File("dump.txt");
    private FileWriter writer;

    public DumpFile() {
        try {
            file.delete();
            file.createNewFile();
            writer = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String s, boolean newLine) {
        try {
            if (newLine) writer.write(System.lineSeparator());
            writer.write(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
