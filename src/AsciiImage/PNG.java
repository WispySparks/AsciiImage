package AsciiImage;

import java.util.List;

public record PNG(
    
    int width, // Width of image in pixels
    int height, // Height of image in pixels
    int bitDepth, // Valid values are 1, 2, 4, 8, and 16
    ColorType colorType, // Valid values are 0, 2, 3, 4, and 6
    int compressionMethod, // Valid values are 0
    int filterMethod, // Valid values are 0
    int interlaceMethod, // Valid values are 0 or 1
    int gamma, // gAMA
    Chromaticities chromaticities, // cHRM
    List<Integer> backgroundColor, // bKGD
    PixelDimensions pixelDimensions, // pHYs
    byte[] imageData // IDAT, uncompressed

) {
    static int gammaFactor = 100000;

    enum ColorType {
        GRAYSCALE, // 0
        RGB, // 2
        PALETTE, // 3
        GRAYSCALE_ALPHA, // 4
        RGB_ALPHA // 6
    }
    
    record Chromaticities(
    int whitePointX,
    int whitePointY,
    int redX,
    int redY,
    int greenX,
    int greenY,
    int blueX,
    int blueY
    ) {}

    record PixelDimensions(
    int pixelsPerUnitX,
    int pixelsPerUnitY,
    int unitSpecifer // 0 is unknown, 1 is meter
    ) {}
}
