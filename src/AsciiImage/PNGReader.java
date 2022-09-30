package AsciiImage;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

import javax.imageio.stream.FileImageInputStream;

@SuppressWarnings("unused")
public class PNGReader {

    private File pngFile = null;
    private boolean finished = false;
    private int temp;
    public int width = 0; // Width of image in pixels
    public int height = 0; // Height of image in pixels
    public int bitDepth = 0; // Valid values are 1, 2, 4, 8, and 16
    public int colorType = 0; // Valid values are 0, 2, 3, 4, and 6
    public int compression = 0; // Valid values are 0
    public int filter = 0; // Valid values are 0
    public int interlace = 0; // Valid values are 0 or 1

    public PNGReader(File f) {
        pngFile = f;
    }

    public void read() { //todo write check crc and use it on image header and other chunks
        finished = false;
        try {
            FileImageInputStream stream = new FileImageInputStream(pngFile);
            if (!checkValidPNG(stream)) {
                System.out.println("Invalid File");
                return;
            }
            readImageHeader(stream);
            // for (int i = 0; i < 100; i++) {
            //     if (finished) break;
            //     readChunk(stream);
            // }
            stream.close();
        } catch (IOException e) {
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

    private static long computeCRC32(byte[] data) throws IOException {
        final long polynomial = 0x104C11DB7L; // 32 bit crc generator polynomial (divisor)
        long crc = 0;
        crc ^= polynomial;
        System.out.println(crc);
        return crc;
    }

    private void readImageHeader(FileImageInputStream stream) throws IOException {
        if (stream.readInt() != 13) throw new IOException("Header chunk corrupted");
        byte[] tBytes = new byte[4];
        stream.readFully(tBytes);
        String type = "";
        for (int i = 0; i < 4; i ++) {
            int charVal = tBytes[i];
            type += AsciiTable.decimalToAscii(charVal);
        }
        if (!type.equals("IHDR")) throw new IOException("Header chunk missing");
        byte[] data = new byte[13];
        stream.readFully(data);
        byte[] bWidth = new byte[4];
        byte[] bHeight = new byte[4];
        for (int i = 0; i < 4; i ++) {
            bWidth[i] = data[i];
            bHeight[i] = data[i+4];
        }
        width = new BigInteger(bWidth).intValue();
        height = new BigInteger(bHeight).intValue();
        bitDepth = data[8]; 
        colorType = data[9];
        compression = data[10];
        filter = data[11];
        interlace = data[12];
        int crc = stream.readInt();
        System.out.println("Real CRC: " + crc);
        if (computeCRC32(data) != crc); {
            throw new IOException("CRC Mismatch");
        }
    }

    private void readChunk(FileImageInputStream stream) throws IOException { // read all data chunks and concatenate it into one big chunk of data
        int length = stream.readInt();
        byte[] bytes = new byte[4];
        stream.readFully(bytes);
        String type = "";
        for (int i = 0; i < 4; i ++) {
            int charVal = bytes[i];
            type += AsciiTable.decimalToAscii(charVal);
        }
        System.out.println("Type: " + type);
        System.out.println("Length: " + length);
        stream.skipBytes(length + 4);
        if (type.equals("IEND")) {
            finished = true;
        }
    }

}
