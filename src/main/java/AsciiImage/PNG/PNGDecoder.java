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

    private boolean finished;
    private int width; 
    private int height; 
    private int bitDepth; 
    private ColorType colorType;
    private int compression; 
    private int filter; 
    private int interlace;
    private List<Integer> plteEntries = new ArrayList<>();
    private List<Integer> transparencyValues = new ArrayList<>();
    private List<Byte> iccProfile = new ArrayList<>();
    private double gamma; 
    private int renderIntent;
    private Chromaticities chromaticities; 
    private List<Integer> backgroundColor = new ArrayList<>();
    private PixelDimensions pixelDimensions; 
    private List<Byte> imageData = new ArrayList<>(); 

    public PNG readPNG(File pngFile) { 
        reset();
        try {
            FileImageInputStream stream = new FileImageInputStream(pngFile);
            readPNGSignature(stream); 
            while (!finished) {
                readNextChunk(stream);
            }
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return new PNG();
        }
        byte[] data = PNGUtil.listToArray(imageData);
        byte[] iccp = new byte[iccProfile.size()];
        for (int i = 0; i < iccProfile.size(); i++) {
            iccp[i] = iccProfile.get(i);
        }
        return new PNG(false, width, height, bitDepth, colorType, compression, 
        filter, interlace, plteEntries, transparencyValues, PNGUtil.decompress(iccp), gamma, renderIntent, chromaticities, backgroundColor, pixelDimensions,
        PNGUtil.decompress(data));
    }

    private void readNextChunk(FileImageInputStream stream) throws IOException { // reads the next chunk in the image byte stream
        int length = stream.readInt();
        byte[] typeBytes = new byte[4];
        stream.readFully(typeBytes);
        String type = "";
        for (int i = 0; i < 4; i ++) {
            int charVal = typeBytes[i];
            type += AsciiTable.decimalToAscii(charVal);
        }
        switch (type) {
            case "IHDR" -> readIHDR(stream, length, type); // Critical
            case "PLTE" -> readPLTE(stream, length); // Critical, Optional
            case "IDAT" -> readIDAT(length, stream);  // Critical
            case "IEND" -> readIEND();  // Critical
            case "tRNS" -> readtRNS(stream, length);
            case "cHRM" -> readcHRM(stream); 
            case "gAMA" -> readgAMA(stream); 
            case "iCCP" -> readiCCP(stream);
            // case sBIT -> readsBIT(stream);
            case "sRGB" -> readsRGB(stream);
            // case "iTXt" -> readiTXt(stream); compressed text
            // case "tEXt" -> readtEXt(stream); text
            // case "zTXt" -> readzTXt(stream); utf 8 text
            case "bKGD" -> readbKGD(stream); 
            // case "hIST" -> readhIST(stream);
            case "pHYs" -> readpHYs(stream); 
            // case "sPLT" -> readsPLT(stream);
            // case "tIME" -> readtIME(stream); time last modified
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

    private void readIHDR(FileImageInputStream stream, int length, String type) throws IOException {
        if (length != 13) throw new StreamCorruptedException("Header Chunk Length Incorrect");
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
    }

    private void readPLTE(FileImageInputStream stream, int length) throws IOException {
        if (length % 3 != 0) throw new StreamCorruptedException("PLTE Palette entry length incorrect");
        for (int i = 0; i < length/3; i++) {
            int r = stream.read();
            int rg = (r << 8) + stream.read();
            int rgb = (rg << 8) + stream.read();
            plteEntries.add(rgb);
        }
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

    private void readtRNS(FileImageInputStream stream, int length) throws IOException {
        switch(colorType) {
            case GRAYSCALE:
                transparencyValues.add(PNGUtil.toUInt16(stream.readByte(), stream.readByte())); // gray
            case PALETTE:
                for (int i = 0; i < length; i++) {
                    transparencyValues.add(stream.read());
                }
                break;
            case RGB:
                transparencyValues.add(PNGUtil.toUInt16(stream.readByte(), stream.readByte())); // r
                transparencyValues.add(PNGUtil.toUInt16(stream.readByte(), stream.readByte())); // g
                transparencyValues.add(PNGUtil.toUInt16(stream.readByte(), stream.readByte())); // b
                break;
            default:
        }
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

    private void readiCCP(FileImageInputStream stream) throws IOException {

    }

    private void readsRGB(FileImageInputStream stream) throws IOException {
        renderIntent = stream.read();
    }

    private void readbKGD(FileImageInputStream stream) throws IOException {
        switch (colorType) {
            case PALETTE -> backgroundColor.add(stream.read()); 
            case GRAYSCALE, GRAYSCALE_ALPHA -> backgroundColor.add(PNGUtil.toUInt16(stream.readByte(), stream.readByte()));  
            case RGB, RGB_ALPHA -> {
                int r = PNGUtil.toUInt16(stream.readByte(), stream.readByte()); 
                int g = PNGUtil.toUInt16(stream.readByte(), stream.readByte());
                int b = PNGUtil.toUInt16(stream.readByte(), stream.readByte());
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
        width = 0;
        height = 0;
        bitDepth = 0;
        colorType = ColorType.GRAYSCALE;
        compression = 0;
        filter = 0;
        interlace = 0;
        plteEntries.clear();
        transparencyValues.clear();
        iccProfile.clear();
        gamma = 0;
        renderIntent = 0;
        chromaticities = new Chromaticities();
        pixelDimensions = new PixelDimensions();
        backgroundColor.clear();    
        imageData.clear();
    }
}
