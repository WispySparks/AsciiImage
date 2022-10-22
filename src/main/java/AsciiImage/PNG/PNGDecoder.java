package main.java.AsciiImage.PNG;

import java.io.File;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.stream.FileImageInputStream;

import main.java.AsciiImage.PNG.PNG.Chromaticities;
import main.java.AsciiImage.PNG.PNG.ColorType;
import main.java.AsciiImage.PNG.PNG.PixelDimensions;
import main.java.AsciiImage.Util.AsciiTable;
import main.java.AsciiImage.Util.PNGUtil;

public class PNGDecoder {

    private PNGUtil util = new PNGUtil();
    private boolean finished;
    private int width; 
    private int height; 
    private int bitDepth; 
    private ColorType colorType;
    private int compression; 
    private int filter; 
    private int interlace;
    private double gamma; 
    private Chromaticities chromaticities; 
    private List<Integer> backgroundColor = new ArrayList<>();
    private PixelDimensions pixelDimensions; 
    private List<Byte> imageData = new ArrayList<>(); 

    public PNG readPNG(File pngFile) { // todo write check crc and use it on image header and other chunks
        reset();
        try {
            FileImageInputStream stream = new FileImageInputStream(pngFile);
            readPNGSignature(stream); 
            readIHDR(stream);
            while (!finished) {
                readNextChunk(stream);
            }
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return new PNG();
        }
        byte[] data = new byte[imageData.size()];
        for (int i = 0; i < imageData.size(); i++) {
            data[i] = imageData.get(i);
        }
        return new PNG(false, width, height, bitDepth, colorType, compression, 
        filter, interlace, gamma, chromaticities, backgroundColor, pixelDimensions,
        util.decompress(data));
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
            case "cHRM" -> readcHRM(stream); 
            case "gAMA" -> readgAMA(stream); 
            // case "iCCP" -> 
            // case "sRGB" -> 
            // case "PLTE" ->  // Critical, Optional
            case "bKGD" -> readbKGD(stream); 
            // case "tRNS" -> 
            case "pHYs" -> readpHYs(stream); 
            // case "tIME" -> time last modified
            // case "iTXt" -> compressed text
            // case "tEXt" -> text
            // case "zTXt" -> utf 8 text
            case "IDAT" -> readIDAT(length, stream);  // Critical
            case "IEND" -> readIEND();  // Critical
            default -> stream.skipBytes(length); 
        }
        stream.skipBytes(4); // crc
    }

    private void readPNGSignature(FileImageInputStream stream) throws IOException {
        int[] sig = {137, 80, 78, 71, 13, 10, 26, 10};
        for (int i = 0; i<sig.length; i++) {
            if (stream.read() != sig[i]) throw new StreamCorruptedException("Doesn't contain PNG Signature");
        }
    }

    private void readIHDR(FileImageInputStream stream) throws IOException {
        if (stream.readInt() != 13) throw new StreamCorruptedException("Header Chunk Length Incorrect");
        byte[] tBytes = new byte[4];
        stream.readFully(tBytes);
        String type = "";
        for (int i = 0; i < 4; i ++) {
            int charVal = tBytes[i];
            type += AsciiTable.decimalToAscii(charVal);
        }
        if (!type.equals("IHDR")) throw new StreamCorruptedException("Header Chunk Missing");
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
        switch(data[9]) {
            case 0 -> colorType = ColorType.GRAYSCALE;
            case 2 -> colorType = ColorType.RGB;
            case 3 -> colorType = ColorType.PALETTE;
            case 4 -> colorType = ColorType.GRAYSCALE_ALPHA;
            case 6 -> colorType = ColorType.RGB_ALPHA;
        }
        compression = data[10];
        filter = data[11];
        interlace = data[12];
        stream.skipBytes(4);
    }

    private void readIDAT(int length, FileImageInputStream stream) throws IOException {
        byte[] arr = new byte[length];
        stream.readFully(arr);
        for (byte b : arr) {
            imageData.add(b);
        }
    }

    private void readIEND() {
        finished = true;
    }

    private void readcHRM(FileImageInputStream stream) throws IOException {
        int whitePointX = stream.readInt();
        int whitePointY = stream.readInt();
        int redX = stream.readInt();
        int redY = stream.readInt();
        int greenX = stream.readInt();
        int greenY = stream.readInt();
        int blueX = stream.readInt();
        int blueY = stream.readInt();
        chromaticities = new Chromaticities(whitePointX, whitePointY, redX, redY, greenX, greenY, blueX, blueY);
    }

    private void readgAMA(FileImageInputStream stream) throws IOException {
        gamma = stream.readInt() / PNG.gammaFactor;
    }

    private void readbKGD(FileImageInputStream stream) throws IOException {
        switch (colorType) {
            case PALETTE -> backgroundColor.add(stream.read()); 
            case GRAYSCALE, GRAYSCALE_ALPHA -> backgroundColor.add(util.toUInt16(stream.readByte(), stream.readByte()));  
            case RGB, RGB_ALPHA -> {
                int r = util.toUInt16(stream.readByte(), stream.readByte()); 
                int g = util.toUInt16(stream.readByte(), stream.readByte());
                int b = util.toUInt16(stream.readByte(), stream.readByte());
                backgroundColor.addAll(Arrays.asList(r, g, b));
            }
        }
    }

    private void readpHYs(FileImageInputStream stream) throws IOException {
        int pixelsPerUnitX = stream.readInt();
        int pixelsPerUnitY = stream.readInt();
        int unitSpecifer = stream.read();
        pixelDimensions = new PixelDimensions(pixelsPerUnitX, pixelsPerUnitY, unitSpecifer);
    }

    private void reset() {
        finished = false;
        width = -1;
        height = -1;
        bitDepth = -1;
        colorType = ColorType.GRAYSCALE;
        compression = -1;
        filter = -1;
        interlace = -1;
        gamma = -1;
        // chromaticities = new Chromaticities();
        // pixelDimensions = new PixelDimensions();
        backgroundColor.clear();    
        imageData.clear();
    }
}
