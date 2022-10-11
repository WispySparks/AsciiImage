package AsciiImage;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.stream.FileImageInputStream;

// @SuppressWarnings("unused")
public class PNGReader {

    private boolean finished = false;
    private int width = -1; // Width of image in pixels
    private int height = -1; // Height of image in pixels
    private int bitDepth = -1; // Valid values are 1, 2, 4, 8, and 16
    private int colorType = -1; // Valid values are 0, 2, 3, 4, and 6
    private int compression = -1; // Valid values are 0
    private int filter = -1; // Valid values are 0
    private int interlace = -1; // Valid values are 0 or 1
    private int gamma = -1;
    private List<Integer> chromaticities = new ArrayList<>();
    private List<Integer> backgroundColor = new ArrayList<>();

    public PNG readPNG(File pngFile) { //todo write check crc and use it on image header and other chunks
        finished = false;
        try {
            FileImageInputStream stream = new FileImageInputStream(pngFile);
            if (!readPNGSignature(stream)) {
                System.out.println("Invalid File");
            }
            readIHDR(stream);
            while (!finished) {
                readNextChunk(stream);
            }
            stream.close();
        } catch (IOException e) {
            System.out.println(e);
        }
        return new PNG(width, height, bitDepth, colorType, compression, 
        filter, interlace, gamma, chromaticities);
    }

    private void readNextChunk(FileImageInputStream stream) throws IOException { // read all data chunks and concatenate it into one big chunk of data
        int length = stream.readInt();
        byte[] typeBytes = new byte[4];
        stream.readFully(typeBytes);
        String type = "";
        for (int i = 0; i < 4; i ++) {
            int charVal = typeBytes[i];
            type += AsciiTable.decimalToAscii(charVal);
        }
        switch (type) {
            case "cHRM": readcHRM(length, stream); break;
            case "gAMA": readgAMA(length, stream); break;
            case "iCCP": break;
            case "sBIT": break;
            case "sRGB": break;
            case "PLTE": break; // Critical, Optional
            case "bKGD": readbKGD(length, stream); break;
            case "hIST": break;
            case "tRNS": break;
            case "pHYs": break;
            case "sPLT": break;
            case "tIME": break;
            case "iTXt": break;
            case "tEXt": break;
            case "zTXt": break;
            case "IDAT": readIDAT(length, stream); break; // Critical
            case "IEND": readIEND(length, stream); break; // Critical
            default: stream.skipBytes(length); break;
        }
        stream.skipBytes(4); // crc
    }

    private boolean readPNGSignature(FileImageInputStream stream) throws IOException {
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

    private void readIHDR(FileImageInputStream stream) throws IOException {
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
        stream.skipBytes(4);
    }

    private void readIDAT(int length, FileImageInputStream stream) throws IOException {
        stream.skipBytes(length);
    }

    private void readIEND(int length, FileImageInputStream stream) throws IOException {
        finished = true;
    }

    private void readcHRM(int length, FileImageInputStream stream) throws IOException {
        int whitePointX = stream.readInt();
        int whitePointY = stream.readInt();
        int redX = stream.readInt();
        int redY = stream.readInt();
        int greenX = stream.readInt();
        int greenY = stream.readInt();
        int blueX = stream.readInt();
        int blueY = stream.readInt();
        chromaticities.addAll(Arrays.asList(whitePointX, whitePointY, redX, redY, greenX, greenY, blueX, blueY));
    }

    private void readgAMA(int length, FileImageInputStream stream) throws IOException {
        gamma = stream.readInt();
    }

    private void readbKGD(int length, FileImageInputStream stream) throws IOException {
        System.out.println(length);
        switch (colorType) {
            case 3 -> backgroundColor.add(stream.read()); 
            case 0, 4 -> backgroundColor.add((stream.readByte() & 0xff) << 8 | (stream.readByte() & 0xff));  
            // case 2, 6 -> backgroundColor.addAll(Arrays.asList(stream.read(),stream.read()));
        }
    }

    public static long computeCRC32(byte[] data) throws IOException {
        final long polynomial = 0x104C11DB7L; // 32 bit crc generator polynomial (divisor)
        long crc = 0;
        crc ^= polynomial;
        System.out.println(crc);
        return crc;
    }

    // bKGD, pHYs, tIME, IDAT, tEXt
}
