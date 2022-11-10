package main.java.AsciiImage.PNG;

import java.util.ArrayList;
import java.util.List;

public record PNG(
    
    boolean isCorrupted,
    // Header Chunk
    int width, // Width of image in pixels
    int height, // Height of image in pixels
    int bitDepth, // Valid values are 1, 2, 4, 8, and 16
    ColorType colorType, // Valid values are 0, 2, 3, 4, and 6
    int compressionMethod, // Valid values are 0
    int filterMethod, // Valid values are 0
    int interlaceMethod, // Valid values are 0 or 1
    
    List<Integer> PLTEPalette, // PLTE entries storing rgb as a single integer
    byte[] imageData, // IDAT, uncompressed
    List<Integer> transparency, // tRNS, length varies by colorType
    Chromaticities chromaticities, // cHRM
    double gamma, // gAMA
    byte[] iccProfile, // iCCP, iCC profile, uncompressed
    List<Integer> significantBits, // sBIT
    int renderIntent, // sRGB, valid values are 0, 1, 2, or 3
    List<Integer> backgroundColor, // bKGD
    PixelDimensions pixelDimensions // pHYs

) {
    public static double gammaFactor = 100000;

    public enum ColorType {
        GRAYSCALE, // 0
        RGB, // 2
        PALETTE, // 3
        GRAYSCALE_ALPHA, // 4
        RGB_ALPHA // 6
    }
    
    public record Chromaticities(
    int whitePointX,
    int whitePointY,
    int redX,
    int redY,
    int greenX,
    int greenY,
    int blueX,
    int blueY
    ) {
        Chromaticities() {
            this(0, 0, 0, 0, 0, 0, 0, 0);
        }
    }

    public record PixelDimensions(
    int pixelsPerUnitX,
    int pixelsPerUnitY,
    int unitSpecifer // 0 is unknown, 1 is meter
    ) {
        PixelDimensions() {
            this(0, 0, 0);
        }
    }

    PNG() {
        this(true, 0, 0, 0, ColorType.GRAYSCALE, 0, 0, 0, new ArrayList<Integer>(0), new byte[0], new ArrayList<Integer>(0), 
        new Chromaticities(), 0, new byte[0], new ArrayList<Integer>(0), 0, new ArrayList<Integer>(0), new PixelDimensions());
    }
}
