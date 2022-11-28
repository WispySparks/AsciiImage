package AsciiImage.Util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DumpFile {
    
    private FileWriter writer;
    private File file;
    private boolean create = false;

    public DumpFile(String name) {
        this(name, false);
    }

    public DumpFile(String name, boolean create) {
        file = new File(name + ".txt");
        this.create = create;
    }

    public void write(String s, boolean newLine) {
        try {
            if (writer == null) writer = new FileWriter(file);
            if (newLine) writer.write(System.lineSeparator());
            writer.write(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (writer == null) return;
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String removeFileExt(String fileName) {
        String name = "";
        String[] arr = fileName.split("\\.");
        for (int i = 0; i < arr.length; i++) {
            if (i != arr.length-1) {
                name += arr[i];
            }
        }
        return name;
    }

    public boolean shouldCreate() {
        return create;
    }

}
