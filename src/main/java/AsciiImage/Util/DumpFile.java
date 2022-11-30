package AsciiImage.Util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class DumpFile {
    
    private final File file;
    private FileWriter writer;
    private Random r = new Random();

    public DumpFile(File name) {
        file = new File(FileUtil.removeFileExt(name) + r.nextInt(1000) + ".txt");
    }

    public void write(String s) {
        try {
            if (writer == null) writer = new FileWriter(file);
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

}
