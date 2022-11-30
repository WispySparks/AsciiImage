package AsciiImage.Util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DumpFile {
    
    private final File file;
    private FileWriter writer;

    public DumpFile(File name) {
        file = new File(FileUtil.removeFileExt(name) + (int) System.currentTimeMillis() + ".txt");
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
