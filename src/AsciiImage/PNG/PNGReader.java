package AsciiImage.PNG;

import AsciiImage.Util.ArrayList2D;
import AsciiImage.Util.PNGUtil;

public class PNGReader {
    
    private PNGUtil util = new PNGUtil();
    private PNG png;
    private byte[] data;
    private int lineLength;

    public PNGReader(PNG png) {
        if (png.isCorrupted()) return;
        this.png = png;
        data = png.imageData();
        int bytesPerPixel = switch(png.colorType()) {
            case GRAYSCALE -> 1; // G
            case GRAYSCALE_ALPHA -> 2; // G, A
            case PALETTE -> 1; // Indexed Color
            case RGB -> 3; // R, G, B
            case RGB_ALPHA -> 4; // R, G, B, A
        };
        lineLength = bytesPerPixel * png.width() + 1;
        if (data.length / lineLength == png.height()) System.out.println("HOORAY!"); // something is lining up here
    }
    /** 4 * 5 + 1 = 21 times, 20 bytes
     * {@link} https://www.w3.org/TR/PNG/#9Filter-types
     */
    public ArrayList2D<Pixel> parseImageData() {
        ArrayList2D<Pixel> pixels = new ArrayList2D<>();
        for (int i = 0; i < data.length; i += lineLength) { // go through every scan line's first byte to see the filter
            switch(data[i]) {
                case 0: 
                    for (int j = 0; j < png.width(); j++) { // data has +1 offset for the first byte being filter byte
                        Pixel p = new Pixel(util.toUInt8(data[4*j+i+1]), util.toUInt8(data[4*j+i+2]), 
                        util.toUInt8(data[4*j+i+3]), util.toUInt8(data[4*j+i+4]), j, i/lineLength);
                        pixels.add(i/lineLength, p);
                    } break;
                case 1: // r g b a r g b a
                // System.out.println((i/lineLength));
                //     for (int j = 1; j<lineLength; j++) {
                //         int xR = util.toUInt8(data[4*j+i+1]);
                //         int xG = util.toUInt8(data[4*j+i+2]);
                //         int xB = util.toUInt8(data[4*j+i+3]);
                //         int xA = util.toUInt8(data[4*j+i]);
                //         if (j > 1) {
                //             int aR = pixels.get(j-2 + (png.width()*(i/lineLength))).R();
                //             int aG = pixels.get(j-2 + (png.width()*(i/lineLength))).G();
                //             int aB = pixels.get(j-2 + (png.width()*(i/lineLength))).B();
                //             int aA = pixels.get(j-2 + (png.width()*(i/lineLength))).A();
                //             pixels.add(new Pixel((xR + aR)%256, (xG + aG)%256, (xB + aB)%256, (xA + aA)%256, j, i/lineLength));
                //         } else {
                //             pixels.add(new Pixel(xR, xB, xG, xA, j, i/lineLength));
                //         }
                //     } break;
                case 2:
                case 3:
                case 4:
            }
        }
        return pixels;
    }

}
