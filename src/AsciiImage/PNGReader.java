package AsciiImage;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.stream.FileImageInputStream;

@SuppressWarnings("unused")
public class PNGReader {

    private PNGUtil util = new PNGUtil();
    private boolean finished = false;
    private int width = -1; // Width of image in pixels
    private int height = -1; // Height of image in pixels
    private int bitDepth = -1; // Valid values are 1, 2, 4, 8, and 16
    private int colorType = -1; // Valid values are 0, 2, 3, 4, and 6
    private int compression = -1; // Valid values are 0
    private int filter = -1; // Valid values are 0
    private int interlace = -1; // Valid values are 0 or 1
    private int gamma = -1; // gAMA
    private List<Integer> chromaticities = new ArrayList<>(); // cHRM
    private List<Integer> backgroundColor = new ArrayList<>(); // bKGD

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
        filter, interlace, gamma, chromaticities, backgroundColor);
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
            case "cHRM" -> readcHRM(length, stream); 
            case "gAMA" -> readgAMA(length, stream); 
            // case "iCCP" -> 
            // case "sBIT" -> 
            // case "sRGB" -> 
            // case "PLTE" ->  // Critical, Optional
            case "bKGD" -> readbKGD(length, stream); 
            // case "hIST" -> 
            // case "tRNS" -> 
            case "pHYs" -> readpHYs(length, stream); 
            // case "sPLT" -> 
            // case "tIME" -> 
            // case "iTXt" -> 
            // case "tEXt" -> 
            // case "zTXt" -> 
            case "IDAT" -> readIDAT(length, stream);  // Critical
            case "IEND" -> readIEND(length, stream);  // Critical
            default -> stream.skipBytes(length); 
        }
        stream.skipBytes(4); // crc
    }

    private boolean readPNGSignature(FileImageInputStream stream) throws IOException {
        if (stream.read() != 137) return false;
        if (stream.read() != 80) return false;
        if (stream.read() != 78) return false;
        if (stream.read() != 71) return false;
        if (stream.read() != 13) return false;
        if (stream.read() != 10) return false;
        if (stream.read() != 26) return false;
        if (stream.read() != 10) return false;
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
        switch (colorType) {
            case 3 -> backgroundColor.add(stream.read()); 
            case 0, 4 -> backgroundColor.add(util.combine2Bytes(stream.readByte(), stream.readByte()));  
            case 2, 6 -> {
                int r = util.combine2Bytes(stream.readByte(), stream.readByte()); 
                int g = util.combine2Bytes(stream.readByte(), stream.readByte());
                int b = util.combine2Bytes(stream.readByte(), stream.readByte());
                backgroundColor.addAll(Arrays.asList(r, g, b));
            }
        }
    }

    private void readpHYs(int length, FileImageInputStream stream) throws IOException {
        int pixelsPerUnitX = stream.readInt();
        int pixelsPerUnitY = stream.readInt();
        int unitSpecifer = stream.read();
    }

    // tIME, IDAT, tEXt
}
