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
    
    double gamma, // gAMA
    Chromaticities chromaticities, // cHRM
    List<Integer> backgroundColor, // bKGD
    PixelDimensions pixelDimensions, // pHYs
    byte[] imageData // IDAT, uncompressed

) {
    public static int gammaFactor = 100000;

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
            this(-1, -1, -1, -1, -1, -1, -1, -1);
        }
    }

    public record PixelDimensions(
    int pixelsPerUnitX,
    int pixelsPerUnitY,
    int unitSpecifer // 0 is unknown, 1 is meter
    ) {
        PixelDimensions() {
            this(-1, -1, -1);
        }
    }

    PNG() {
        this(true, -1, -1, -1, ColorType.GRAYSCALE, -1, -1, -1, -1, new Chromaticities(-1, -1, -1, -1, -1, -1, -1, -1),
        new ArrayList<Integer>(), new PixelDimensions(-1, -1, -1), new byte[0]);
    }
}
