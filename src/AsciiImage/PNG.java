package AsciiImage;

import java.util.List;

public record PNG (
    
    int width, // Width of image in pixels
    int height, // Height of image in pixels
    int bitDepth, // Valid values are 1, 2, 4, 8, and 16
    int colorType, // Valid values are 0, 2, 3, 4, and 6
    int compression, // Valid values are 0
    int filter, // Valid values are 0
    int interlace, // Valid values are 0 or 1
    int gamma,
    List<Integer> chromaticities

) {}
