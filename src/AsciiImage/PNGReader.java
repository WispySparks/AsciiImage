package AsciiImage;

import java.io.File;
import java.io.IOException;

import javax.imageio.stream.FileImageInputStream;

public class PNGReader {

    private File pngFile = null;
    public int width = 0; // Width of image in pixels
    public int height = 0; // Height of image in pixels
    public int bitDepth = 0; // Valid values are 1, 2, 4, 8, and 16
    public int colorType = 0; // Valid values are 0, 2, 3, 4, and 6
    public int compression = 0; // Valid values are 0
    public int filter = 0; // Valid values are 0
    public int interlace = 0; // Valid values are 0 or 1

    PNGReader(File file) {
        pngFile = file;
    }

    public void read() {
        try {
            FileImageInputStream stream = new FileImageInputStream(pngFile);
            if (!checkValidPNG(stream)) {
                System.out.println("Invalid File");
                return;
            }
            readImageHeader(stream);
            readChunk(stream);
            // readPLTE(stream);
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }

    private boolean checkValidPNG(FileImageInputStream stream) throws IOException {
        if (stream.readByte() != -119) return false;
        if (stream.readByte() != 80) return false;
        if (stream.readByte() != 78) return false;
        if (stream.readByte() != 71) return false;
        if (stream.readByte() != 13) return false;
        if (stream.readByte() != 10) return false;
        if (stream.readByte() != 26) return false;
        if (stream.readByte() != 10) return false;
        return true;
    }

    private void readImageHeader(FileImageInputStream stream) throws IOException {
        stream.skipBytes(8); // skip chunk length and type
        width = stream.readInt();
        height = stream.readInt();
        bitDepth = stream.readByte();
        colorType = stream.readByte();
        compression = stream.readByte();
        filter = stream.readByte();
        interlace = stream.readByte();
        stream.skipBytes(4); // skip crc
    }

    @SuppressWarnings("unused")
    private void readPLTE(FileImageInputStream stream) throws IOException {

    }

    private void readChunk(FileImageInputStream stream) throws IOException {
        int length = stream.readInt();
        byte[] bytes = new byte[4];
        stream.readFully(bytes);
        String type = "";
        for (int i = 0; i < 4; i ++) {
            int charVal = bytes[i];
            type += AsciiTable.decimalToAscii(charVal);
            System.out.println(charVal);
        }
        System.out.println("Type: " + type);
        System.out.println("Length: " + length);
    }

}